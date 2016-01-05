package fr.faylixe.marklet;

/**
 * 
 * @author fv
 */
public final class MarkdownUtils {

	/** **/
	private static final String BOLD = "**";

	/** **/
	private static final String TABLE_SEPARATOR = " | ";

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
	 * @param cells
	 * @return
	 */
	public static String asRow(final String ...cells) {
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < cells.length; i++) {
			builder.append(cells[i]);
			if (i < cells.length - 1) {
				builder.append(TABLE_SEPARATOR);
			}
		}
		return builder.toString();
	}

	/**
	 * 
	 */
	private MarkdownUtils() {
		// Do nothing.
	}

}
