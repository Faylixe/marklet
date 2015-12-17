package fr.faylixe.marklet;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;

/**
 * 
 * @author fv
 */
public final class ClassPageBuilder {

	/** **/
	private final IGenerationContext context;

	/**
	 * 
	 * @param context
	 */
	public ClassPageBuilder(final IGenerationContext context) {
		this.context = context;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	private Path getOutputPath(final String name) {
		final StringBuilder pathBuilder = new StringBuilder();
		pathBuilder
			.append(context.getOutputDirectory())
			.append(name.substring(name.lastIndexOf('.') + 1))
			.append(".md");
		return Paths.get(pathBuilder.toString());
	}

	/**
	 * 
	 * @param classDoc
	 * @throws IOException 
	 */
	public void build(final ClassDoc classDoc) throws IOException {
		final DocumentBuilder builder = new DocumentBuilder();
		builder.append(classDoc.commentText());
		for (final FieldDoc fieldDoc : classDoc.fields()) {
			builder.append(fieldDoc);
		}
		for (final MethodDoc methodDoc : classDoc.methods()) {
			builder.append(methodDoc);
		}
		builder.build(getOutputPath(classDoc.name()));
	}

}
