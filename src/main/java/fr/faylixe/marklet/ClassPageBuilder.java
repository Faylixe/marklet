package fr.faylixe.marklet;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.PackageDoc;

/**
 * Builder that aims to create documentation
 * page for a given ``class``. As for a standard
 * class javadoc generation, it will contains a
 * class summary, followed by details about class
 * field, constructor, and methods.
 * 
 * @author fv
 */
public final class ClassPageBuilder extends MarkletDocumentBuilder {

	/** Target class that page is built from. **/
	private final ClassDoc classDoc;

	/**
	 * Default constructor. 
	 * 
	 * @param classDoc Target class that page is built from.
	 */
	private ClassPageBuilder(final ClassDoc classDoc) {
		super(classDoc.containingPackage());
		this.classDoc = classDoc;
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
	 * 
	 * @param methodDoc
	 * @return
	 */
	private boolean isInherited(final MethodDoc methodDoc) {
		return !methodDoc.commentText().equals("{@inheritDoc}");
	}

	/**
	 * Builds and write a class hierarchy from the
	 * current class. Such hierarchy consists in the
	 * class inheritance path.
	 */
	private void buildHierachy() {
		final List<ClassDoc> hierarchy = new ArrayList<ClassDoc>();
		ClassDoc current = classDoc;
		while (current != null) {
			hierarchy.add(current);
			current = current.superclass();
		}
		quote();
		for (int i = hierarchy.size() - 1; i >= 0; i--) {
			classLink(classDoc.containingPackage(), hierarchy.get(i));
			if (i > 0) {
				text(" > ");
			}
		}
	}

	/**
	 * Builds and writes the documentation header.
	 * Consists in the class name with a H1 level,
	 * the class hierarchy, and the comment text.
	 */
	private void buildHeader() {
		header(1);
		text(classDoc.name());
		newLine();
		final PackageDoc packageDoc = classDoc.containingPackage();
		final String packageName = packageDoc.name();
		text(MarkletConstant.PACKAGE);
		link(packageName, MarkletConstant.README);
		breakingReturn();
		newLine();
		buildHierachy();
		newLine();
		newLine();
		description(classDoc);
		newLine();
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
			header(4);
			text(MarkletConstant.METHODS);
			newLine();
			tableHeader(MarkletConstant.METHODS_SUMMARY_HEADERS);
			getOrderedElements(classDoc::methods)
				.filter(this::isInherited)
				.forEach(this::rowSignature);
			newLine();
		}
		// TODO : Build inherited method hierachy here.
	}
	
	/**
	 * Builds the summary for all exposed fields if any.
	 */
	private void buildFieldSummary() {
		if (hasField()) {
			header(4);
			text(MarkletConstant.FIELDS);
			newLine();
			getOrderedElements(classDoc::fields)
				.filter(FieldDoc::isStatic)
				.forEach(this::itemSignature);
			getOrderedElements(classDoc::fields)
				.filter(field -> !field.isStatic())
				.forEach(this::itemSignature);
			newLine();
		}
	}

	/**
	 * Builds the summary for all exposed constructors if any.
	 */
	private void buildConstructorSummary() {
		if (hasConstructor()) {
			header(4);
			text(MarkletConstant.CONSTRUCTORS);
			newLine();
			getOrderedElements(classDoc::constructors)
				.forEach(this::itemSignature);
			newLine();
		}
	}

	/**
	 * Builds class summary. Consists in an overview of
	 * available constructor, method, and field, in a
	 * table form.
	 */
	private void buildSummary() {
		if (hasField() || hasMethod() || hasConstructor()) {
			newLine();
			header(2);
			text(MarkletConstant.SUMMARY);
			newLine();
			buildFieldSummary();
			buildConstructorSummary();
			buildMethodsSummary();
			horizontalRule();
			newLine();
		}
	}

	/**
	 * Builds constructors documentation.
	 */
	private void buildConstructors() {
		if (hasConstructor()) {
			newLine();
			header(2);
			text(MarkletConstant.CONSTRUCTORS);
			newLine();
			getOrderedElements(classDoc::constructors).forEach(this::member);
		}
	}

	/**
	 * Builds fields documentation.
	 */
	private void buildFields() {
		if (hasField()) {
			newLine();
			header(2);
			text(MarkletConstant.FIELDS);
			newLine();
			getOrderedElements(classDoc::fields)
				.filter(field -> !field.isStatic())
				.forEach(this::field);
			getOrderedElements(classDoc::fields)
				.filter(FieldDoc::isStatic)
				.forEach(this::field);
		}
	}
	
	/**
	 * Builds methods documentation.
	 */
	private void buildMethods() {
		if (hasMethod()) {
			newLine();
			header(2);
			text(MarkletConstant.METHODS);
			newLine();
			getOrderedElements(classDoc::methods)
				.filter(this::isInherited)
				.forEach(this::member);
		}
	}

	/**
	 * Builds and writes the documentation file
	 * associated to the given ``classDoc`` into
	 * the directory denoted by the given ``directoryPath``.
	 * 
	 * @param classDoc Class to generated documentation for.
	 * @param directoryPath Path of the directory to write documentation in.
	 * @throws IOException If any error occurs while writing documentation.
	 */
	public static void build(final ClassDoc classDoc, final Path directoryPath) throws IOException {
		final Path classPath = Paths.get(
				new StringBuffer()
					.append(classDoc.simpleTypeName())
					.append(MarkdownDocumentBuilder.FILE_EXTENSION)
					.toString());
		final ClassPageBuilder builder = new ClassPageBuilder(classDoc);
		builder.buildHeader();
		builder.buildSummary();
		builder.buildConstructors();
		builder.buildFields();
		builder.buildMethods();
		builder.build(directoryPath.resolve(classPath));
	}

}
