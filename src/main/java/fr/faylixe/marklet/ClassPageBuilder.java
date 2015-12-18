package fr.faylixe.marklet;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;

/**
 * 
 * @author fv
 */
public final class ClassPageBuilder {

	/** **/
	private final DocumentBuilder documentBuilder;

	/** **/
	private final ClassDoc classDoc;

	/**
	 * 
	 * @param documentBuilder
	 * @param classDoc
	 */
	private ClassPageBuilder(final DocumentBuilder documentBuilder, final ClassDoc classDoc) {
		this.classDoc = classDoc;
		this.documentBuilder = documentBuilder;
	}

	/**
	 * Builds and writes the documentation header.
	 * Consists in the class name with a H1 level,
	 * the class hierarchy, and the comment text.
	 * 
	 * @throws IOException If any error occurs while writing heaer.
	 */
	private void buildHeader() throws IOException {
		documentBuilder.appendHeader(classDoc.name(), 1);
		documentBuilder.appendHierarchy(classDoc);
		documentBuilder.append(classDoc.commentText());		
	}

	/**
	 * 
	 * @return
	 */
	private Stream<MethodDoc> getMethods() {
		return Arrays
				.stream(classDoc.methods())
				.sorted((a, b) -> {
					return a.name().compareTo(b.name());
				});
	}
	
	/**
	 * Builds class summary. Consists in an overview of
	 * available constructor, method, and field, in a
	 * table form.
	 * 
	 * @throws IOException If any error occurs while writing summary.
	 */
	private void buildSummary() throws IOException {
		documentBuilder.initializeMethodHeader();
		getMethods().forEach(method -> {
			try {
				documentBuilder.appendMethodHeader(method);				
			}
			catch (final IOException e) {
				// TODO : Throw runtime here.
			}
		});
	}

	/**
	 * Builds and writes the documentation file
	 * associated to the given <tt>classDoc</tt>
	 * into the directory denoted by the given path.
	 * 
	 * @param classDoc Class to generated documentation for.
	 * @param directoryPath Path of the directory to write documentation in.
	 * @throws IOException If any error occurs while writing documentation.
	 */
	public static void build(final ClassDoc classDoc, final Path directoryPath) throws IOException {
		final Path classPath = Paths.get(
				new StringBuilder()
					.append(classDoc.simpleTypeName())
					.append(Marklet.FILE_EXTENSION)
					.toString());
		final DocumentBuilder documentBuilder = DocumentBuilder.create(classPath);
		final ClassPageBuilder builder = new ClassPageBuilder(documentBuilder, classDoc);
		builder.buildHeader();
		builder.buildSummary();
	}

}
