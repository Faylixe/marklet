package fr.faylixe.marklet;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Type;

/**
 * 
 * @author fv
 */
public final class DocumentBuilder {
	
	/** **/
	private static final String TABLE_HEADER = "--- | ---:";

	/** **/
	private static final String METHOD_SUMMARY_HEADER = "Return type | Signature";

	/** **/
	private final IGenerationContext context;

	/** **/
	private final BufferedWriter writer;

	/**
	 * 
	 * @param context
	 */
	public DocumentBuilder(final IGenerationContext context, final BufferedWriter writer) {
		this.context = context;
		this.writer = writer;
	}

	/**
	 * 
	 * @param label
	 * @param url
	 * @return
	 */
	private static String buildLink(final String label, final String url) {
		return new StringBuilder()
			.append('[').append(label).append(']')
			.append('(').append(url).append(')')
			.toString();
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
			final String url = context.getClassURL(current.qualifiedName());
			final String link = buildLink(current.name(), url);
			hiearchyBuilder.insert(0, link);
			current = current.superclass();
			if (current != null) {
				hiearchyBuilder.insert(0, " > ");
			}
		}
		writer.write(hiearchyBuilder.toString());
		writer.newLine();
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
		final Type type = method.returnType();
		if (type.isPrimitive()) {
			writer.write("**");
			writer.write(type.simpleTypeName());
			writer.write("**");
		}
		else {
			final ClassDoc classDoc = type.asClassDoc();
			final String url = context.getClassURL(classDoc.qualifiedName());
			final String link = buildLink(classDoc.name(), url);
			writer.write(link);
		}
		writer.write(" | ");
		writer.write(method.name()); // TODO : Build link;
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
	public void append(final MethodDoc methodDoc) throws IOException {
		writer.write("### ");
		writer.write(methodDoc.name());
		writer.newLine();
	}

	/**
	 * 
	 * @param path
	 * @throws IOException 
	 */
	public void build() throws IOException {
		writer.close();
	}
	
	/**
	 * 
	 * @param context
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static DocumentBuilder create(final IGenerationContext context, final Path path) throws IOException {
		final FileWriter fileWriter = new FileWriter(path.toFile());
		final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		return new DocumentBuilder(context, bufferedWriter);
	}

}
