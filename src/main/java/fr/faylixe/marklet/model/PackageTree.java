package fr.faylixe.marklet.model;

import java.util.Map;

/**
 * TODO : Ignore nameless default package 
 * 
 * @author fv
 */
public final class PackageTree {

	/** **/
	private static final IllegalArgumentException UNKNOWN_PACKAGE = new IllegalArgumentException("");

	/** Index of the root of the tree, also named alpha node here. **/
	private final int alpha;

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
	private final int [][] tree;

	/** Reverse index built to speed up node to package resolution. **/
	private final String [] reverseIndex;

	/** Package index that maps a given package name to a integer index. **/
	private final Map<String, Integer> index;

	/** **/
	private final int size;

	/**
	 * Default constructor.
	 * 
	 * @param tree Tree representation as a adjacency matrix.
	 * @param index Package index that maps a given package name to a integer index.
	 * @param reverseIndex Reverse index built to speed up node to package resolution.
	 */
	protected PackageTree(final int [][] tree, final Map<String, Integer> index, final String [] reverseIndex) {
		this.tree = tree;
		this.size = tree.length;
		this.index = index;
		this.alpha = tree.length - 1;
		this.reverseIndex = reverseIndex;
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	private int getParent(final int node) {
		for (int m = 0; m < size; m++) {
			if (tree[node][m] < 0) {
				return m;
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public String getShortestPath(final String source, final String target) {
		if (!index.containsKey(source) || !index.containsKey(target)) {
			throw UNKNOWN_PACKAGE;
		}
		final StringBuilder pathBuilder = new StringBuilder();
		int current = index.get(source);
		while (!target.equals(reverseIndex[current])) {
			// Case 1 : Valid branch. Go down.
			if (target.startsWith(source)) {
				for (int m = 0; m < size; m++) {
					if (target.startsWith(reverseIndex[m])) {
						// TODO : Append terminal package if required.
						current = m;
						break;
					}
				}
			}
			// Case 2 : Go up to find the valid branch.
			else {
				pathBuilder.append("../");
				current = getParent(current);
			}
		}
		return pathBuilder.toString();
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

}
