package fr.faylixe.marklet.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;

/**
 * Builder that aims to build package tree using the following algorithm :
 * 
 * * First builds the package index, namely a mapping that assigns an integer index to each package.
 * * Builds an initial package hiearchy graph, where two packages are connected if a direct link exist (parent or child)
 * * Makes the built graph connected, by introducing virtual node that correspond to the most common package.
 * * Builds an alpha rooted tree from the connected graph.
 * 
 * An alpha rooted tree consists in a root named alpha which correspond to an nameless package,
 * then direct child are connected to such root, and child of child, etc ... .
 * 
 * @author fv
 */
public final class PackageTreeBuilder {

	/** Index counter. **/
	private final AtomicInteger counter;

	/** Built package index. **/
	private final Map<String, Integer> index;

	/** Reverse index built to speed up node to package resolution. **/
	private String [] reverseIndex;

	/** Built graph. **/
	private int [][] graph;

	/**
	 * Default constructor.
	 * Initializes index counter.
	 */
	private PackageTreeBuilder() {
		this.counter = new AtomicInteger();
		this.index = new ConcurrentHashMap<String, Integer>();
	}

	/**
	 * Static factory method that builds a package index
	 * from the given documentation ``root`` in
	 * parallel.
	 * 
	 * @param root Documentation root to build index from.
	 */
	private void buildPackageIndex(final RootDoc root) {
		Arrays.stream(root.classes())
			.parallel()
			.map(ClassDoc::containingPackage)
			.map(PackageDoc::name)
			.filter(name -> !index.containsKey(name))
			.forEach(name -> index.put(name, counter.getAndIncrement()));
		this.reverseIndex = new String[index.size()];
		for (final String packageName : index.keySet()) {
			reverseIndex[index.get(packageName)] = packageName;
		}
	}

	/**
	 * Static factory method that builds the adjacency
	 * matrix for the internal package index. The reference
	 * graph is used in order to copy already computed value,
	 * for a given graph, if ``i`` is different from ``j`` and
	 * ``graph[i][j]`` = 0, then the value has never been computed.
	 * 
	 * @param reference Reference graph to copy value from if not computed.
	 */
	private void buildGraph(final int [][] reference) {
		final int n = index.size();
		final int [][] graph = new int[n][n];
		// TODO : Think about parallel processing.
		for (int i = 0; i < index.size(); i++) {
			for (int j = 0; j < index.size(); j++) {
				if (reference == null || (i != j && reference[i][j] == 0)) {
					graph[i][j] = computeDistance(reverseIndex[i], reverseIndex[j]);
				}
				else {
					graph[i][j] = reference[i][j];
				}
			}
		}
		this.graph = graph;
	}

	/**
	 * Indicates if the given ``node`` is connected
	 * at least to one node in the graph.
	 * 
	 * @param node Node to check connectivity from.
	 * @return ``true`` if the given ``node`` is connected, ``false`` otherwise.
	 */
	private boolean isConnected(final int node) {
		for (int i = 0; i < index.size(); i++) {
			if (node != i) {
				final int distance = graph[node][i];
				if (distance != 0 && distance != Integer.MAX_VALUE) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Connects the given ``node`` to the closest neighbor.
	 * Such neighbor is the node which have the longest
	 * common prefix with the given ``node``.
	 * 
	 * @param node Node to connect to the graph.
	 */
	private void connect(final int node) {
		String longestPrefix = "";
		for (final String candidate : index.keySet()) {
			final String prefix = StringUtils.getCommonPrefix(reverseIndex[node], candidate);
			if (prefix.length() > longestPrefix.length()) {
				longestPrefix = prefix;
			}
		}
		if (!longestPrefix.isEmpty()) {
			// TODO : Resize once, fill all.
			reverseIndex = Arrays.copyOf(reverseIndex, reverseIndex.length + 1);
			index.put(longestPrefix, counter.getAndIncrement());
			reverseIndex[reverseIndex.length - 1] = longestPrefix;
		}
	}

	/**
	 * Builds a graph that is connected from the internal graph
	 * if not already connected. To do so, it will create virtual node
	 * that links any disconnected node from the graph to the closest one.
	 * A closest node is here defined as a package with the longest common
	 * prefix.
	 */
	private void buildConnectedGraph() {
		for (int i = 0; i < index.size(); i++) {
			if (!isConnected(i)) {
				connect(i);
			}
		}
		buildGraph(graph);
	}

	/**
	 * The alpha distance is used to retrieves final distance that will
	 * be used as weight between two alpha rooted tree nodes. Such distance
	 * correspond to the initial distance in the connected graph, if ``source``
	 * or``target`` do not match the alpha root. Otherwise, the virtual distance
	 * between the alpha node and the other node is computed, as the number of dot
	 * in the other node name.
	 * 
	 * @param source Source node to retrieve distance from.
	 * @param target Target node to retrieve distance from.
	 * @return Computed distance.
	 */
	private int getAlphaDistance(final int source, final int target) {
		if (source == target) {
			return 0;
		}
		else if (source == graph.length) {
			return StringUtils.countMatches(reverseIndex[target], '.');
		}
		else if (target == graph.length) {
			return -1 * getAlphaDistance(target, source);
		}
		return graph[source][target];
	}

	/**
	 * Computes and returns the closest positive
	 * and non null distance from the given ``node``
	 * to any other node.
	 * 
	 * @param node Node to find best distance from.
	 * @return Closest distance possible.
	 */
	private int getClosestDistance(final int node) {
		int best = Integer.MAX_VALUE;
		for (int m = 0; m < index.size(); m++) {
			final int distance = getAlphaDistance(node, m);
			if (distance > 0 && distance < best) {
				best = distance;
			}
		}
		return best;
	}

	/**
	 * Retrieves and returns set of closest nodes
	 * of the given one based on the closest distance.
	 * 
	 * @param node Node to find closest node from.
	 * @return Set of closest node.
	 */
	private Set<Integer> getClosestNodes(final int node, final Set<Integer> visited) {
		final Set<Integer> closestNodes = new HashSet<Integer>();
		final int best = getClosestDistance(node);
		for (int m = 0; m < index.size(); m++) {
			if (!visited.contains(m) && graph[node][m] == best) {
				closestNodes.add(m);
			}
		}
		return closestNodes;
	}

	/**
	 * Final step of the algorithm that consists in
	 * building an alpha rooted tree from the initial
	 * connected graph, by adding the alpha root to it
	 * and normalizes link and distance between nodes.
	 */
	private void buildAlphaRootedTree() {
		index.put("", graph.length);
		// TODO : Add to reverse index.
		final int n = graph.length + 1;
		final int [][] tree = new int[n][n];
		final Queue<Integer> queue = new LinkedList<Integer>();
		final Set<Integer> visited = new HashSet<Integer>(graph.length);
		queue.add(graph.length);
		visited.add(graph.length);
		while (!queue.isEmpty()) {
			final int current = queue.poll();
			final Set<Integer> closests = getClosestNodes(current, visited);
			for (final int node : closests) {
				tree[current][node]	= getAlphaDistance(current, node);
				tree[node][current]	= -1 * getAlphaDistance(current, node);
				queue.add(node);
				visited.add(node);
			}
		}
	}

	/**
	 * Computes the distance between the two given package name.
	 * Such distance consists in the number of separating directory.
	 * If ``source`` equals the ``target`` then the distance is 0.
	 * If the two package has a direct relation (e.g a.b.c and a.b,
	 * or a.b.c and a.b.c.e.f) then the distance will be equals to
	 * the number of separating directory. Otherwise distance is
	 * considered as infinite and should be computed as a path.
	 * 
	 * @param source Source node to compute distance from.
	 * @param target Target node to compute distance to.
	 * @return Computed distance.
	 */
	private static int computeDistance(final String source, final String target) {
		if (source.equals(target)) { // Same package.
			return 0;
		}
		else if (target.startsWith(source)) { // Sub package
			final String suffix = target.substring(source.length());
			return StringUtils.countMatches(suffix, '.');
		}
		if (source.startsWith(target)) { // Parent package
			final String suffix = source.substring(source.length());
			return -1 * StringUtils.countMatches(suffix, '.');			
		}
		return Integer.MAX_VALUE;
	}

	/**
	 * Static factory method that builds a package graph
	 * from the given documentation ``root``.
	 * 
	 * @param root Documentation root to build graph from.
	 * @return Built package graph
	 */
	public static PackageTree build(final RootDoc root) {
		final PackageTreeBuilder builder = new PackageTreeBuilder();
		builder.buildPackageIndex(root);
		builder.buildGraph(null);
		builder.buildConnectedGraph();
		builder.buildAlphaRootedTree();
		return new PackageTree(builder.graph, builder.index, builder.reverseIndex);
	}

}
