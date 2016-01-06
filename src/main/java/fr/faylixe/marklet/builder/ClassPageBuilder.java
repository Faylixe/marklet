package fr.faylixe.marklet.builder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.PackageDoc;

import fr.faylixe.marklet.IGenerationContext;
import fr.faylixe.marklet.MarkdownUtils;
import fr.faylixe.marklet.MarkletConstant;

/**
 * 
 * @author fv
 */
public final class ClassPageBuilder {

	/** Generation context used. **/
	private final IGenerationContext context;

	/** Document builder instance for filling class page content.  **/
	private final DocumentBuilder documentBuilder;

	/** Target class that page is built from. **/
	private final ClassDoc classDoc;

	/**
	 * Default constructor. 
	 * 
	 * @param context Generation context used.
	 * @param documentBuilder Document builder instance for filling class page content.
	 * @param classDoc Target class that page is built from.
	 */
	private ClassPageBuilder(final IGenerationContext context, final DocumentBuilder documentBuilder, final ClassDoc classDoc) {
		this.context = context;
		this.classDoc = classDoc;
		this.documentBuilder = documentBuilder;
	}
	
	/**
	 * Indicates if the target class exposes any method.
	 * 
	 * @return ``true`` if the target class exposes at least one method, ``false`` otherwise.
	 */
	private boolean hasMethod() {
		return classDoc.methods().length > 0;
	}
	
	/**
	 * Indicates if the target class exposes any field.
	 * 
	 * @return ``true`` if the target class exposes at least one field, ``false`` otherwise.
	 */
	private boolean hasField() {
		return classDoc.fields().length > 0;
	}
	
	/**
	 * Indicates if the target class exposes any constructor.
	 * 
	 * @return ``true`` if the target class exposes at least one constructor, ``false`` otherwise.
	 */
	private boolean hasConstructor() {
		return classDoc.constructors().length > 0;
	}

	/**
	 * Builds and write a class hierarchy from the
	 * current class. Such hierarchy consists in the
	 * class inheritance path.
	 */
	private String buildHierachy() {
		final StringBuilder hiearchyBuilder = new StringBuilder();
		ClassDoc current = classDoc;
		while (current != null) {			
			hiearchyBuilder.insert(0, context.getClassLink(classDoc.containingPackage(), current));
			current = current.superclass();
			if (current != null) {
				hiearchyBuilder.insert(0, " > "); // TODO : Constant
			}
		}
		hiearchyBuilder.insert(0, "> "); // TODO : Constant
		return hiearchyBuilder.toString();
	}

	/**
	 * Builds and writes the documentation header.
	 * Consists in the class name with a H1 level,
	 * the class hierarchy, and the comment text.
	 */
	private void buildHeader() {
		documentBuilder.appendHeader(classDoc.name(), 1);
		final PackageDoc packageDoc = classDoc.containingPackage();
		final String packageName = packageDoc.name();
		final String packageHeader = new StringBuilder(MarkletConstant.PACKAGE)
			.append(MarkdownUtils.buildLink(packageName, MarkletConstant.README))
			.append("<br>") // TODO : Constant
			.toString();
		documentBuilder.appendText(packageHeader);
		documentBuilder.appendText(buildHierachy());
		documentBuilder.newLine();
		final String description = context.getDescription(classDoc);
		documentBuilder.appendText(description);		
	}

	/**
	 * Returns an ordered stream of element that are provided
	 * by the given ``supplier``, using element name for sorting.
	 * 
	 * @param supplier Supplier that provides elements to stream.
	 * @return Ordered stream.
	 */
	private <T extends Doc> Stream<T> getOrderedElements(final Supplier<T[]> supplier) {
		return Arrays
				.stream(supplier.get())
				.sorted((a, b) -> {
					return a.name().compareTo(b.name());
				});
	}

	/**
	 * Builds the summary for all exposed methods if any.
	 */
	private void buildMethodsSummary() {
		if (hasMethod()) {
			documentBuilder.appendTableHeader(MarkletConstant.METHODS_SUMMARY_HEADERS);
			getOrderedElements(classDoc::methods).forEach(documentBuilder::appendMethodHeader);
		}
	}
	
	/**
	 * Builds the summary for all exposed fields if any.
	 */
	private void buildFieldSummary() {
		if (hasField()) {
			documentBuilder.appendTableHeader(MarkletConstant.FIELDS_SUMMARY_HEADERS);
		}
	}

	/**
	 * Builds the summary for all exposed constructors if any.
	 */
	private void buildConstructorSummary() {
		if (hasConstructor()) {
			documentBuilder.appendTableHeader(MarkletConstant.CONSTRUCTOR_SUMMARY_HEADERS);
			getOrderedElements(classDoc::constructors).forEach(constructor -> {
			});
		}
	}

	/**
	 * Builds class summary. Consists in an overview of
	 * available constructor, method, and field, in a
	 * table form.
	 */
	private void buildSummary() {
		if (hasField() || hasMethod() || hasConstructor()) {
			documentBuilder.newLine();
			documentBuilder.appendHeader("Summary", 2);
			buildConstructorSummary();
			buildMethodsSummary();
			buildFieldSummary();
		}
	}

	/**
	 * Builds fields documentation.
	 */
	private void buildFields() {
		if (hasField()) {
			documentBuilder.newLine();
			documentBuilder.appendHeader(MarkletConstant.FIELDS, 2);
//			documentBuilder.appendTableHeader(MarkletConstant.FIELDS_SUMMARY_HEADERS);
//			buildFields(getOrderedElements(classDoc::fields).filter(field -> !field.isStatic()));
//			buildFields(getOrderedElements(classDoc::fields).filter(FieldDoc::isStatic));
		}
	}
	
	/**
	 * Builds methods documentation.
	 */
	private void buildMethods() {
		if (hasMethod()) {
			documentBuilder.newLine();
			documentBuilder.appendHeader(MarkletConstant.METHODS, 2);
			getOrderedElements(classDoc::methods).forEach(documentBuilder::appendMethod);
		}
	}

	/**
	 * Builds and writes the documentation file
	 * associated to the given <tt>classDoc</tt>
	 * into the directory denoted by the given path.
	 * 
	 * @param context Context used.
	 * @param classDoc Class to generated documentation for.
	 * @param directoryPath Path of the directory to write documentation in.
	 * @throws IOException If any error occurs while writing documentation.
	 */
	public static void build(final IGenerationContext context, final ClassDoc classDoc, final Path directoryPath) throws IOException {
		final Path classPath = Paths.get(
				new StringBuilder()
					.append(classDoc.simpleTypeName())
					.append(MarkdownUtils.FILE_EXTENSION)
					.toString());
		final DocumentBuilder documentBuilder = DocumentBuilder.create(context, classDoc.containingPackage());
		final ClassPageBuilder builder = new ClassPageBuilder(context, documentBuilder, classDoc);
		builder.buildHeader();
		builder.buildSummary();
		builder.buildFields();
		builder.buildMethods();
		documentBuilder.build(directoryPath.resolve(classPath));
	}

}
