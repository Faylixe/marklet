package fr.faylixe.marklet;

import java.io.IOException;

/**
 * 
 * @author fv
 */
public class MarkdownDocumentBuilder {

	/** Extension used for markdown file. **/
	public static final String FILE_EXTENSION = ".md";

	/** **/
	private static final String BOLD = "**";

	/** **/
	private static final String LIST_ITEM = "* ";

	/** **/
	private static final String BR = "<br>";

	/** Markdown sequence for horizontal rules. **/
	private static final String HR = "---";

	/** **/
	private final StringBuffer buffer;

	/**
	 * 
	 */
	public MarkdownDocumentBuilder() {
		this.buffer = new StringBuffer();
	}
	

	/**
	 * 
	 */
	public void newLine() {
		buffer.append("\n");
	}

	/**
	 * 
	 * @param label
	 * @param level
	 */
	public void header(final int level) {
		for (int i = 0; i < level; i++) {
			buffer.append('#');
		}
	}

	/**
	 * 
	 * @param text
	 */
	public void text(final String text) {
		buffer.append(text);
	}

	/**
	 * 
	 * @param label
	 * @param url
	 */
	public void link(final String label, final String url) {
		buffer
			.append('[')
			.append(label)
			.append(']')
			.append('(')
			.append(url)
			.append(')');
	}
	
	/**
	 * 
	 */
	public void item() {
		buffer.append(LIST_ITEM);
	}
	
	/**
	 * 
	 * @param text
	 */
	public void bold(final String text) {
		buffer.append(BOLD).append(text).append(BOLD);
	}
	
	/**
	 * 
	 */
	public void quote() {
		buffer.append("> ");
	}
	
	/**
	 * 
	 * @param text
	 * @return
	 */
	public void italic(final String text) {
		buffer.append('*').append(text).append('*').toString();
	}
	
	/**
	 * 
	 */
	public void horizontalRule() {
		buffer.append(HR);
		newLine();
	}

	/**
	 * 
	 */
	public void breakingReturn() {
		buffer.append(BR);
		newLine();
	}
	
	/**
	 * 
	 */
	public void startTableRow() {
		buffer.append("| ");
	}
	
	/**
	 * 
	 */
	public void endTableRow() {
		buffer.append(" |");
	}

	/**
	 * 
	 */
	public void cell() {
		buffer.append(" | ");
	}
	

	/**
	 * Appends the given ``headers`` as a table header row.
	 * 
	 * @param headers Headers to write.
	 * @throws IOException If any error occurs while writing headers.
	 */
	public void tableHeader(final String  ... headers) {
		tableRow(headers);
		startTableRow();
		for (int i = 0; i < headers.length; i++) {
			buffer.append("---"); // TODO : Export sequence to markdown ?
			if (i < headers.length - 1) {
				cell();
			}
		}
		endTableRow();
		newLine();
	}

	/**
	 * 
	 * @param cells
	 */
	public void tableRow(final String ... cells) {
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
	 * 
	 * @return
	 */
	public String build() {
		return buffer.toString();
	}

}
