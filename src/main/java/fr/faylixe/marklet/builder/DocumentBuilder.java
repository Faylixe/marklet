package fr.faylixe.marklet.builder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.Tag;
import com.sun.javadoc.ThrowsTag;
import com.sun.javadoc.Type;

import fr.faylixe.marklet.IGenerationContext;

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
	private static final String TABLE_HEADER = "| --- | --- | --- |";

	/** **/
	private static final String METHOD_SUMMARY_HEADER = "| Type | Method |";
	
	/** **/
	private static final String FIELD_SUMMARY_HEADER = "| Type | Field | Description |";

	/** **/
	private final IGenerationContext context;

	/** **/
	private final BufferedWriter writer;

	/** **/
	private final PackageDoc source;

	/**
	 * 
	 * @param context
	 * @param source
	 * @param writer
	 */
	public DocumentBuilder(final IGenerationContext context, final PackageDoc source, final BufferedWriter writer) {
		this.context = context;
		this.source = source;
		this.writer = writer;
	}

	/**
	 * 
	 * @param label
	 * @param level
	 * @throws IOException
	 */
	public void appendHeader(final String label, final int level) throws IOException {
		for (int i = 0; i < level; i++) {
			writer.write('#');
		}
		writer.write(label);
		writer.newLine();
		writer.newLine();
	}

	/**
	 * 
	 * @param text
	 * @throws IOException 
	 */
	public void append(final String text) throws IOException {
		writer.write(text);
		writer.newLine();
	}

	/**
	 * 
	 * @param leaf
	 * @throws IOException 
	 */
	public void appendHierarchy(final ClassDoc leaf) throws IOException {
		final StringBuilder hiearchyBuilder = new StringBuilder();
		ClassDoc current = leaf;
		while (current != null) {			
			hiearchyBuilder.insert(0, context.getClassLink(leaf.containingPackage(), current));
			current = current.superclass();
			if (current != null) {
				hiearchyBuilder.insert(0, " > ");
			}
		}
		writer.write("> ");
		writer.write(hiearchyBuilder.toString());
		writer.newLine();
		writer.newLine();
	}
	
	/**
	 * 
	 * @param headers
	 * @throws IOException
	 */
	public void appendTableHeader(final String  ... headers) throws IOException {
		appendTableRow(headers);
		writer.write("|"); 
		for (int i = 0; i < headers.length; i++) {
			writer.write(" --- |");
		}
		writer.newLine();
	}

	/**
	 * 
	 * @param cells
	 */
	public void appendTableRow(final String ... cells) throws IOException {
		writer.write("| ");
		for (final String cell : cells) {
			writer.write(cell);
			writer.write(" |");
		}
		writer.newLine();
	}
	
	/**
	 * @throws IOException 
	 * 
	 */
	public void initializeMethodHeader() throws IOException {
		writer.newLine();
		writer.write(METHOD_SUMMARY_HEADER);
		writer.newLine();
		writer.write(TABLE_HEADER);
		writer.newLine();
	}

	/**
	 * 
	 * @param method
	 */
	public void appendMethodHeader(final MethodDoc method) throws IOException  {
		final MethodSignatureBuilder builder = new MethodSignatureBuilder(context, method);
		writer.write("| ");
		writer.write(builder.buildReturn());
		writer.write(" | ");
		writer.write(builder.buildName());
		writer.write(" |");
		writer.newLine();
	}
	
	/**
	 * 
	 * @param field
	 * @throws IOException
	 */
	public void appendField(final FieldDoc field) throws IOException {
		writer.write("| ");
		writer.write(""); // TODO : Write link type.
		writer.write(" | ");
		writer.write(field.name());
		writer.write(" |");
		writer.newLine();

	}

	/**
	 * 
	 * @throws IOException
	 */
	public void initializeFieldHeader() throws IOException {
		writer.newLine();
		writer.write(FIELD_SUMMARY_HEADER);
		writer.newLine();
		writer.write(TABLE_HEADER);
		writer.newLine();
	}

	/**
	 * 
	 */
	public void newLine() throws IOException {
		writer.newLine();
	}

	/**
	 * 
	 * @param fieldDoc
	 */
	public void append(final FieldDoc fieldDoc) {
	}

	/**
	 * 
	 * @param methodDoc
	 * @throws IOException 
	 */
	public void appendMethod(final MethodDoc methodDoc) throws IOException {
		appendHeader(methodDoc.name(), 3);
		writer.newLine();
		writer.write(methodDoc.commentText());
		appendParameters(methodDoc.paramTags());
		appendReturn(methodDoc.tags("return"));
		appendsException(methodDoc.throwsTags());
		writer.newLine();
		writer.write("--");
		writer.newLine();
		
	}

	/**
	 * 
	 * @param parameters
	 * @throws IOException
	 */
	private void appendParameters(final ParamTag[] parameters) throws IOException {
		if (parameters.length > 0) {
			writer.newLine();
			appendHeader("Parameters", 5);
			writer.newLine();
			for (final ParamTag parameter : parameters) {
				writer.write("* ");
				writer.write(parameter.parameterName());
				writer.write(" ");
				writer.write(parameter.parameterComment());
				writer.newLine();
			}
		}
	}
	
	/**
	 * 
	 * @param type
	 */
	private void appendReturn(final Tag[] tag) throws IOException {
		if (tag.length > 0) {
			writer.newLine();
			appendHeader("Returns", 5);
			writer.newLine();
			writer.write("* ");
			writer.write(tag[0].text());
			writer.newLine();
		}
	}

	/**
	 * 
	 * @throws IOException
	 */
	private void appendsException(final ThrowsTag[] exceptions) throws IOException {
		if (exceptions.length > 0) {
			writer.newLine();
			appendHeader("Throws", 5);
			for (final ThrowsTag exception : exceptions) {
				writer.write("* ");
				writer.write(context.getClassLink(source, exception.exception())); // TODO : Link to exception class.
				writer.write(" ");
				writer.write(exception.exceptionComment());
				writer.newLine();
			}
		}
	}

	/**
	 * 
	 * @param path
	 * @throws IOException 
	 */
	public void build() throws IOException {
		writer.write(HR);
		writer.newLine();
		writer.write(MARKLET_LINK);
		writer.close();
	}
	
	/**
	 * 
	 * @param context
	 * @param source
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static DocumentBuilder create(final IGenerationContext context, final PackageDoc source, final Path path) throws IOException {
		final FileWriter fileWriter = new FileWriter(path.toFile());
		final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		return new DocumentBuilder(context, source, bufferedWriter);
	}

}
