package fr.faylixe.marklet.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;

/**
 * 
 * @author fv
 */
public final class PackageGraph {

	/**
	 * Graph representation as a adjacency matrix.
	 * For a given cell ``(i, j)``, ``graph[i][j]``
	 * gives the distance between two corresponding
	 * package such as :
	 * 
	 * * A positive distance indicates that ``j`` is a subpackage of ``i`` 
	 * * A negative distance indicates that ``i`` is a subpackage of ``j``
	 * * A null distance indicates that ``i`` and ``j`` are the same.
	 */
	private final int [][] graph;

	/** Package index that maps a given package name to a integer index. **/
	private final Map<String, Integer> index;

	/**
	 * Default constructor.
	 * 
	 * @param graph Graph representation as a adjacency matrix.
	 * @param index Package index that maps a given package name to a integer index.
	 */
	private PackageGraph(final int [][] graph, final Map<String, Integer> index) {
		this.graph = graph;
		this.index = index;
	}

	/**
	 * Retrieves and returns the distance between
	 * the two given package.
	 * 
	 * @param source Source node to get distance for.
	 * @param target Target node to get distance for.
	 * @return Distance between given nodes, or &infin; if one of the given nodes does not exist in the graph.
	 * @throws IllegalArgumentException
	 */
	public int getDistance(final String source, final String target) {
		if (!index.containsKey(source) || !index.containsKey(target)) {
			throw new IllegalArgumentException(); // TODO Add error message.
		}
		final int i = index.get(source);
		final int j = index.get(target);
		return graph[i][j];
	}
	
	/**
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public String getShortestPath(final String source, final String target) {
		// TODO : Perform dijkstra algorithm here.
		return null;
	}
	
	/**
	 * Indicates if the given ``packageName`` is
	 * present into this graph.
	 * 
	 * @param packageName Name of the package to check presence for.
	 * @return ``true`` if the given package is into this graph, ``false`` otherwise.
	 * @see Map#containsKey(Object)
	 */
	public boolean contains(final String packageName) {
		return index.containsKey(packageName);
	}

	/**
	 * 
	 * @author fv
	 */
	private static class Builder {

		/** **/
		private Map<String, Integer> index;
	
		/** **/
		private int [][] graph;

		/**
		 * Static factory method that builds a package index
		 * from the given documentation ``root`` in
		 * parallel.
		 * 
		 * @param root Documentation root to build index from.
		 */
		private void buildPackageIndex(final RootDoc root) {
			this.index = new ConcurrentHashMap<>();
			final AtomicInteger counter = new AtomicInteger();
			Arrays.stream(root.classes())
				.parallel()
				.map(ClassDoc::containingPackage)
				.map(PackageDoc::name)
				.filter(name -> !index.containsKey(name))
				.forEach(name -> index.put(name, counter.getAndIncrement()));
		}

		/**
		 * Static factory method that builds the adjacency
		 * matrix for the given package ``index``.
		 */
		private void buildGraph() {
			final int n = index.size();
			this.graph = new int[n][n];
			// TODO : Think about parallel processing.
			for (final String source : index.keySet()) {
				for (final String target : index.keySet()) {
					final int i = index.get(source);
					final int j = index.get(target);
					graph[i][j] = computeDistance(source, target);
				}
			}
		}
	
		/**
		 * Builds a graph that is connected from the given ``originalGraph``
		 * if not already connected. To do so, it will create virtual node
		 * that links any disconnected node from the graph to the closest one.
		 * A closest node is here defined as a package with the longest common
		 * prefix.
		 */
		private void buildConnectedGraph() {
			final Set<Integer> disconnected = getDisconnectedNodes();
			if (!disconnected.isEmpty()) {
				// Rebuild graph here.
			}
		}

		/**
		 * 
		 * @return
		 */
		private Set<Integer> getDisconnectedNodes() {
			final Set<Integer> disconnectedNodes = new HashSet<Integer>();
			for (final String source : index.keySet()) {
				final int i = index.get(source);
				for (final String target : index.keySet()) {
					if (!source.equals(target)) {
						final int j = index.get(target);
						// TODO : Check here.
					}
				}
			}
			return disconnectedNodes;
		}
	
	}

	/**
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	private static int computeDistance(final String source, final String target) {
		if (source.equals(target)) { // Same package.
			return 0;
		}
		else if (target.startsWith(source)) { // Sub package
			final String suffix = target.substring(source.length());
			return StringUtils.countMatches(suffix, ".");
		}
		if (source.startsWith(target)) { // Parent package
			final String suffix = source.substring(source.length());
			return -1 * StringUtils.countMatches(suffix, ".");			
		}
		// TODO : Insert case with no common prefix here.
		return Integer.MAX_VALUE;
	}

	/**
	 * Static factory method that builds a package graph
	 * from the given documentation ``root``.
	 * 
	 * @param root Documentation root to build graph from.
	 * @return Built package graph
	 */
	public static PackageGraph createGraph(final RootDoc root) {
		final Builder builder = new Builder();
		builder.buildPackageIndex(root);
		builder.buildGraph();
		builder.buildConnectedGraph();
		return new PackageGraph(builder.graph, builder.index);
	}

}
