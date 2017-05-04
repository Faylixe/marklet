package fr.faylixe.marklet;

/**
 * This class aims to build Markdown document.
 * It is built in a top of a {@link StringBuffer}
 * instance which will contains our document
 * content.
 * 
 * @author fv
 */
public class MarkdownDocumentBuilder {

	/** Extension used for markdown file. **/
	public static final String FILE_EXTENSION = ".md";

	/** Bold text decoration. **/
	private static final String BOLD = "**";

	/** Italic text decoration. **/
	private static final String ITALIC = "*";

	/** List item prefix. **/
	private static final String LIST_ITEM = "* ";

	/** Quoted text prefix. **/
	private static final String QUOTE = "> ";

	/** Breaking return sequence. **/
	private static final String BR = "<br>";

	/** Horizontal rule sequence. **/
	private static final String HR = "---";

	/** Table row start prefix. **/
	private static final String ROW_START = "| ";

	/** Table row cell separator. **/
	private static final String CELL_SEPARATOR = " | ";
	
	/** Table row end suffix. **/
	private static final String ROW_END = " |";

	/** HTML link opening tag. **/
	private static final String LINK_OPEN = "<a href=";

	/** HTML link closing tag. **/
	private static final String LINK_CLOSE =  "</a>";

	/** HTML paragraph opening tag. **/
	private static final String PARAGRAPH_OPEN = "<p>";

	/** HTML paragraph closing tag. **/
	private static final String PARAGRAPH_CLOSE = "</p>";

	/** Buffer in which markdown document is stored. **/
	private final StringBuffer buffer;

	/**
	 * Default constructor.
	 * Initializes internal buffer.
	 */
	public MarkdownDocumentBuilder() {
		this.buffer = new StringBuffer();
	}
	
	/**
	 * Filters and returns the given ``text``
	 * freed from HTML paragraph.
	 * 
	 * @param text Text to remove paragraph tag from.
	 * @return Filtered text.
	 */
	private final String filterParagraph(final String text) {
		return text
				.replaceAll(PARAGRAPH_OPEN, "")
				.replaceAll(PARAGRAPH_CLOSE, "");
	}
	
	/**
	 * Appends a new line to the current document.
	 */
	public final void newLine() {
		buffer.append("\n");
	}

	/**
	 * Appends the given ``text`` to the current
	 * document.
	 * 
	 * @param text Text to append to the document.
	 */
	public final void text(final String text) {
		buffer.append(filterParagraph(text));
	}
	
	/**
	 * Appends the given ``character`` to the current
	 * document.
	 * 
	 * @param character Character to append to the document.
	 */
	public final void character(final char character) {
		buffer.append(character);
	}

	/**
	 * Appends the given ``text`` to the current
	 * document with a bold decoration.
	 * 
	 * @param text Text to append to the document with the bold decoration.
	 */
	public final void bold(final String text) {
		buffer
			.append(BOLD)
			.append(text)
			.append(BOLD);
	}
	
	/**
	 * Appends the given ``text`` to the current
	 * document with an italic decoration.
	 * 
	 * @param text Text to append to the document with the italic decoration.
	 */
	public final void italic(final String text) {
		buffer
			.append(ITALIC)
			.append(text)
			.append(ITALIC);
	}

	/**
	 * Starts a quote in the current document.
	 */
	public final void quote() {
		buffer.append(QUOTE);
	}
	
	/**
	 * Starts a list item in the current document.
	 */
	public final void item() {
		buffer.append(LIST_ITEM);
	}

	/**
	 * Appends a horizontal rule sequence 
	 * to the current document.
	 */
	public final void horizontalRule() {
		buffer.append(HR);
		newLine();
	}

	/**
	 * Appends a horizontal rule sequence
	 * to the current document.
	 */
	public final void breakingReturn() {
		buffer.append(BR);
		newLine();
	}

	/**
	 * Starts a header text, in the current document
	 * using the given header ``level``
	 * 
	 * @param level Level of the header to start.
	 */
	public final void header(final int level) {
		for (int i = 0; i < level; i++) {
			buffer.append('#');
		}
		buffer.append(' ');
	}

	/**
	 * Appends a raw HTML link to the current document
	 * using the given ``label`` and the given
	 * ``url``.
	 * 
	 * @param label Label of the built link.
	 * @param url Target URL of the built link.
	 */
	public final void rawLink(final String label, final String url) {
		buffer
			.append(LINK_OPEN)
			.append('"')
			.append(url)
			.append('"')
			.append('>')
			.append(label)
			.append(LINK_CLOSE);
	}

	/**
	 * Appends a link to the current document
	 * using the given ``label`` and the given
	 * ``url``.
	 * 
	 * @param label Label of the built link.
	 * @param url Target URL of the built link.
	 */
	public final void link(final String label, final String url) {
		buffer
			.append('[').append(label).append(']')
			.append('(').append(url).append(')');
	}
	
	/**
	 * Appends a table row start separator
	 * to the current document.
	 */
	public final void startTableRow() {
		buffer.append(ROW_START);
	}

	/**
	 * Appends a table cell separator
	 * to the current document.
	 */
	public final void cell() {
		buffer.append(CELL_SEPARATOR);
	}

	/**
	 * Appends a table row end separator
	 * to the current document.
	 */
	public final void endTableRow() {
		buffer.append(ROW_END);
	}

	/**
	 * Appends the given ``headers`` in the current
	 * document as a table header row.
	 * 
	 * @param headers Headers to write.
	 */
	public final void tableHeader(final String  ... headers) {
		tableRow(headers);
		startTableRow();
		for (int i = 0; i < headers.length; i++) {
			buffer.append(HR);
			if (i < headers.length - 1) {
				cell();
			}
		}
		endTableRow();
		newLine();
	}

	/**
	 * Appends the given ``cell`` in the current
	 * document as a table row.
	 * 
	 * @param cells Cell to write.
	 */
	public final void tableRow(final String ... cells) {
		startTableRow();
		for (int i = 0; i < cells.length; i++) {
			buffer.append(cells[i]);
			if (i < cells.length - 1) {
				cell();
			}
		}
		endTableRow();
		newLine();
	}

	/**
	 * Builds and returns the document content.
	 * 
	 * @return Built document content.
	 * @see StringBuffer#toString()
	 */
	public final String build() {
		return buffer.toString();
	}

}
