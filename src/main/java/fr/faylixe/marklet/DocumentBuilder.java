package fr.faylixe.marklet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.Tag;
import com.sun.javadoc.ThrowsTag;

/**
 * 
 * @author fv
 */
public final class DocumentBuilder {

	/** Generation context used. **/
	private final IGenerationContext context;

	/** Container in which document content is written. **/
	private final StringBuffer writer;

	/** Target source package from which document will be written. **/
	private final PackageDoc source;

	/**
	 * Default constructor. 
	 * 
	 * @param context Generation context used. 
	 * @param source Target source package from which document will be written. 
	 */
	public DocumentBuilder(final IGenerationContext context, final PackageDoc source) {
		this.context = context;
		this.source = source;
		this.writer = new StringBuffer();
	}

	/**
	 * Appends the given ``text`` to
	 * the current document.
	 * 
	 * @param text Text to append.
	 * @throws IOException If any error occurs while writing text.
	 */
	public void appendText(final String text) {
		writer.append(text);
		newLine();
	}

	/**
	 * Appends a header to the current document,
	 * with the given ``label`` at the given
	 * ``level``.
	 * 
	 * @param label Label of the header to write.
	 * @param level Header level to use.
	 * @throws IOException If any error occurs while writing header.
	 */
	public void appendHeader(final String label, final int level) {
		for (int i = 0; i < level; i++) {
			writer.append('#');
		}
		writer.append(label);
		newLine();
		newLine();
	}

	/**
	 * Appends the given ``headers`` as a table header row.
	 * 
	 * @param headers Headers to write.
	 * @throws IOException If any error occurs while writing headers.
	 */
	public void appendTableHeader(final String  ... headers) {
		appendTableRow(headers);
		if (headers.length == 1) {
			writer.append("| ");
		}
		for (int i = 0; i < headers.length; i++) {
			writer.append(" --- "); // TODO : Export sequence to markdown ?
			if (i < headers.length - 1) {
				writer.append('|');
			}
		}
		if (headers.length == 1) {
			writer.append(" |");
		}
		newLine();
	}

	/**
	 * 
	 * @param cells
	 */
	public void appendTableRow(final String ... cells) {
		if (cells.length == 1) {
			writer.append("| ");
		}

		for (int i = 0; i < cells.length; i++) {
			writer.append(cells[i]);
			if (i < cells.length - 1) {
				writer.append(MarkletConstant.TABLE_SEPARATOR);
			}
		}
		if (cells.length == 1) {
			writer.append(" |");
		}
		newLine();
	}

	/**
	 * 
	 * @param member
	 */
	private void appendSignature(final ExecutableMemberDoc member) {
		final String signature = new StringBuilder()
			.append(member.name())
			.append(member.flatSignature())
			.toString();
		appendHeader(signature, 3);
	}

	/**
	 * 
	 */
	public void newLine() {
		writer.append("\n");
	}
	
	/**
	 * 
	 * @param fieldDoc
	 */
	public void appendField(final FieldDoc fieldDoc) {
		writer.append(fieldDoc.name());
		newLine();
	}

	/**
	 * Appends the method documentation body. Using the
	 * following format :
	 * 
	 * * method signature (as header)
	 * * method description (as text)
	 * * method parameters (as list)
	 * * method return type (as single item list)
	 * * method exception (as list)
	 * 
	 * @param methodDoc Method documentation to append.
	 */
	public void appendMember(final ExecutableMemberDoc member) {
		appendSignature(member);
		newLine();
		final String description = context.getDescription(member);
		writer.append(description);
		appendParameters(member.paramTags());
		if (member instanceof MethodDoc) {
			final MethodDoc methodDoc = (MethodDoc) member;
			appendReturn(methodDoc.tags("return")); // TODO : Exports as static constant.
		}
		appendsException(member.throwsTags());
		newLine();
		writer.append(Markdown.HR);
		newLine();
	}

	/**
	 * Appends parameters defined by the
	 * given list, as a markdown list of
	 * the following format :
	 * 
	 * * ``Type : Description``
	 * 
	 * @param parameters
	 */
	private void appendParameters(final ParamTag[] parameters) {
		if (parameters.length > 0) {
			newLine();
			appendHeader(MarkletConstant.PARAMETERS, 5);
			newLine();
			for (final ParamTag parameter : parameters) {
				final String parameterItem = new StringBuffer()
					.append(parameter.parameterName())
					.append(' ')
					.append(parameter.parameterComment()) // TODO : Link processing ?
					.toString();
				writer.append(Markdown.listItem(parameterItem));
				newLine();
			}
		}
	}
	
	/**
	 * Appends the description of the return type
	 * defined by the given ``tag``, as a markdown
	 * item of the following format :
	 * 
	 * * ``Type : Description``
	 * 
	 * @param tag Return type tag to use.
	 */
	private void appendReturn(final Tag[] tag) {
		if (tag.length > 0) {
			newLine();
			appendHeader(MarkletConstant.RETURNS, 5);
			newLine();
			writer.append(Markdown.listItem(tag[0].text()));  // TODO : Link processing ?
			newLine();
		}
	}

	/**
	 * Appends the given ``exception`` to the current
	 * document, as a markdown item of the following format :
	 * 
	 * * ``Type : Description``
	 */
	private void appendsException(final ThrowsTag[] exceptions) {
		if (exceptions.length > 0) {
			newLine();
			appendHeader(MarkletConstant.THROWS, 5);
			for (final ThrowsTag exception : exceptions) {
				final String exceptionItem = new StringBuffer()
					.append(context.getClassLink(source, exception.exception()))
					.append(' ')
					.append(exception.exceptionComment())  // TODO : Link processing ?
					.toString();
				writer.append(Markdown.listItem(exceptionItem));
				newLine();
			}
		}
	}

	/**
	 * Finalizes document building by adding a
	 * horizontal rule, the **marklet** generation
	 * badge, and closing the internal writer.
	 * 
	 * @throws IOException If any error occurs while closing document.
	 */
	public void build(final Path path) throws IOException {
		writer.append(Markdown.HR);
		newLine();
		writer.append(MarkletConstant.BADGE);
		final String content = writer.toString();
		final InputStream stream = new ByteArrayInputStream(content.getBytes());
		Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING);
	}

}
