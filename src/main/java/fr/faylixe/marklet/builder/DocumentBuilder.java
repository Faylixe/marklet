package fr.faylixe.marklet.builder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.Tag;
import com.sun.javadoc.ThrowsTag;

import fr.faylixe.marklet.IGenerationContext;
import fr.faylixe.marklet.MarkdownUtils;
import fr.faylixe.marklet.MarkletConstant;

/**
 * 
 * @author fv
 */
public final class DocumentBuilder {

	/** **/
	private static final String MARKLET_LINK = "[![Marklet](https://img.shields.io/badge/Generated%20by-Marklet-green.svg)](https://github.com/Faylixe/marklet)";

	/** **/
	private static final String HR = "---";

	/** **/
	private final IGenerationContext context;

	/** **/
	private final StringBuffer writer;

	/** **/
	private final PackageDoc source;

	/**
	 * 
	 * @param context
	 * @param source
	 * @param writer
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
		for (int i = 0; i < headers.length; i++) {
			writer.append(" --- "); // TODO : Export sequence to markdown ?
			if (i < headers.length - 1) {
				writer.append('|');
			}
		}
		newLine();
	}

	/**
	 * 
	 * @param cells
	 */
	public void appendTableRow(final String ... cells) {
		writer.append("| ");
		for (final String cell : cells) {
			writer.append(cell);
			writer.append(" |");
		}
		newLine();
	}

	/**
	 * 
	 * @param method
	 */
	public void appendMethodHeader(final MethodDoc method) {
		final MethodSignatureBuilder builder = new MethodSignatureBuilder(context, method);
		MarkdownUtils.asRow(builder.buildReturn(), builder.buildName());
		writer.append("| ");
		writer.append(builder.buildReturn());
		writer.append(" | ");
		writer.append(builder.buildName());
		writer.append(" |");
		newLine();
	}

	/**
	 * 
	 */
	public void newLine() {
		writer.append("\n");
	}
	
	/**
	 * 
	 * @param field
	 */
	public void appendField(final FieldDoc field) {
		writer.append("| ");
		writer.append(""); // TODO : Write link type.
		writer.append(" | ");
		writer.append(field.name());
		writer.append(" |");
		newLine();
	}
	
	/**
	 * 
	 * @param constructorDoc
	 */
	public void appendConstructor(final ConstructorDoc constructorDoc) {
		appendHeader(constructorDoc.flatSignature(), 3);
		newLine();
		final String description = context.getDescription(constructorDoc);
		writer.append(description);
		appendParameters(constructorDoc.paramTags());
		appendsException(constructorDoc.throwsTags());
		newLine();
		writer.append(HR);
		newLine();
	}

	/**
	 * 
	 * @param methodDoc
	 */
	public void appendMethod(final MethodDoc methodDoc) {
		appendHeader(methodDoc.flatSignature(), 3);
		newLine();
		final String description = context.getDescription(methodDoc);
		writer.append(description);
		appendParameters(methodDoc.paramTags());
		appendReturn(methodDoc.tags("return"));
		appendsException(methodDoc.throwsTags());
		newLine();
		writer.append(HR);
		newLine();
		
	}

	/**
	 * 
	 * @param parameters
	 * @throws IOException
	 */
	private void appendParameters(final ParamTag[] parameters) {
		if (parameters.length > 0) {
			newLine();
			appendHeader(MarkletConstant.PARAMETERS, 5);
			newLine();
			for (final ParamTag parameter : parameters) {
				writer.append("* ");
				writer.append(parameter.parameterName());
				writer.append(" ");
				writer.append(parameter.parameterComment());
				newLine();
			}
		}
	}
	
	/**
	 * 
	 * @param type
	 */
	private void appendReturn(final Tag[] tag) {
		if (tag.length > 0) {
			newLine();
			appendHeader(MarkletConstant.RETURNS, 5);
			newLine();
			writer.append("* ");
			writer.append(tag[0].text());
			newLine();
		}
	}

	/**
	 * 
	 * @throws IOException
	 */
	private void appendsException(final ThrowsTag[] exceptions) {
		if (exceptions.length > 0) {
			newLine();
			appendHeader(MarkletConstant.THROWS, 5);
			for (final ThrowsTag exception : exceptions) {
				writer.append("* ");
				writer.append(context.getClassLink(source, exception.exception())); // TODO : Link to exception class.
				writer.append(" ");
				writer.append(exception.exceptionComment());
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
		writer.append(HR);
		newLine();
		writer.append(MARKLET_LINK);
		final String content = writer.toString();
		final InputStream stream = new ByteArrayInputStream(content.getBytes());
		Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING);
	}
	
	/**
	 * 
	 * @param context
	 * @param source
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static DocumentBuilder create(final IGenerationContext context, final PackageDoc source) {
		return new DocumentBuilder(context, source);
	}

}
