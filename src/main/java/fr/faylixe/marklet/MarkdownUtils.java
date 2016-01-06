package fr.faylixe.marklet;

/**
 * 
 * @author fv
 */
public final class MarkdownUtils {

	/** **/
	private static final String BOLD = "**";

	/** Extension used for markdown file. **/
	public static final String FILE_EXTENSION = ".md";

	/**
	 * 
	 * @param label
	 * @param url
	 * @return
	 */
	public static String buildLink(final String label, final String url) {
		return new StringBuilder()
			.append('[').append(label).append(']')
			.append('(').append(url).append(')')
			.toString();
	}
	
	/** **/
	public static String bold(final String text) {
		return new StringBuilder(BOLD).append(text).append(BOLD).toString();
	}

	/**
	 * 
	 */
	private MarkdownUtils() {
		// Do nothing.
	}

}
