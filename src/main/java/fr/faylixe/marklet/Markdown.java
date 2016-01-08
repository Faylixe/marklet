package fr.faylixe.marklet;

/**
 * 
 * @author fv
 */
public final class Markdown {

	/** **/
	private static final String BOLD = "**";

	/** **/
	private static final String LIST_ITEM = "* ";

	/** Extension used for markdown file. **/
	public static final String FILE_EXTENSION = ".md";

	/** **/
	public static final String NEWLINE = "<br>";

	/** Markdown sequence for horizontal rules. **/
	public static final String HR = "---";

	/**
	 * 
	 * @param label
	 * @param url
	 * @return
	 */
	public static String link(final String label, final String url) {
		return new StringBuffer()
			.append('[')
			.append(label)
			.append(']')
			.append('(')
			.append(url)
			.append(')')
			.toString();
	}
	
	/**
	 * 
	 * @param item
	 * @return
	 */
	public static String listItem(final String item) {
		return new StringBuffer()
			.append(LIST_ITEM)
			.append(item)
			.toString();
	}
	
	/**
	 * 
	 * @param text
	 * @return
	 */
	public static String bold(final String text) {
		return new StringBuffer()
			.append(BOLD)
			.append(text)
			.append(BOLD)
			.toString();
	}
	
	/**
	 * Private constructor for avoiding instantiation.
	 */
	private Markdown() {
		// Do nothing.
	}

}
