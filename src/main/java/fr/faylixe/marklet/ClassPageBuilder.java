package fr.faylixe.marklet;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.Type;

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

	/** Separator used in the class hierarchy.**/
	private static final String HIERARCHY_SEPARATOR = " > ";

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
	private boolean isNotInherited(final MethodDoc methodDoc) {
		return methodDoc.overriddenMethod() == null;
	}

	/**
	 * Appends to the current document the class hierarchy
	 * from the current class. Such hierarchy consists in the
	 * class inheritance path.
	 */
	private void classHierarchy() {
		final List<ClassDoc> hierarchy = new ArrayList<ClassDoc>();
		ClassDoc current = classDoc;
		while (current != null) {
			hierarchy.add(current);
			current = current.superclass();
		}
		quote();
		for (int i = hierarchy.size() - 1; i >= 0; i--) {
			classLink(getSource(), hierarchy.get(i));
			if (i > 0) {
				text(HIERARCHY_SEPARATOR);
			}
		}
	}

	/**
	 * Appends to the current document the interface hierarchy
	 * from the current class. Such hiearchy consists in all
	 * implemented interface.
	 */
	private void interfaceHierarchy() {
		final Set<Type> implementedInterfaces = new HashSet<Type>();
		ClassDoc current = classDoc;
		while (current != null) {
			implementedInterfaces.addAll(Arrays.asList(current.interfaceTypes()));
			current = current.superclass();
		}
		if (!implementedInterfaces.isEmpty()) {
			text(MarkletConstant.INTERFACE_HIEARCHY_HEADER);
			newLine();
			quote();
			final int limit = implementedInterfaces.size() - 1;
			final Type [] types = implementedInterfaces.toArray(new Type[implementedInterfaces.size()]);
			for (int i = 0; i < types.length; i++) {
				typeLink(getSource(), types[i]);
				if (i < limit) {
					character(',');
					character(' ');
				}
			}
		}
	}

	/**
	 * Appends to the current document it title, which consists
	 * in the type of the target documentation (interface, enumeration,
	 * annotation or class) and it name.
	 */
	private void title() {
		header(1);
		final StringBuilder builder = new StringBuilder();
		if (classDoc.isInterface()) {
			builder.append(MarkletConstant.INTERFACE);
		}
		else if (classDoc.isEnum()) {
			builder.append(MarkletConstant.ENUMERATION);
		}
		else if (classDoc.isAnnotationType()) {
			builder.append(MarkletConstant.ANNOTATION);
		}
		else {
			builder.append(MarkletConstant.CLASS);
		}
		builder
			.append(' ')
			.append(classDoc.name());
		text(builder.toString());
	}

	/**
	 * Appends to the current document the class
	 * header. Consists in the class name with a
	 * level 1 header, the class hierarchy, and
	 * the comment text.
	 */
	private void header() {
		title();
		newLine();
		final PackageDoc packageDoc = classDoc.containingPackage();
		final String packageName = packageDoc.name();
		text(MarkletConstant.PACKAGE);
		character(' ');
		link(packageName, MarkletConstant.README);
		breakingReturn();
		newLine();
		classHierarchy();
		newLine();
		newLine();
		interfaceHierarchy();
		newLine();
		newLine();
		description(classDoc);
		newLine();
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
	 * Appends to the current document the
	 * method summary if any method is exposed.
	 */
	private void methodsSummary() {
		if (hasMethod()) {
			header(4);
			text(MarkletConstant.METHODS);
			newLine();
			tableHeader(MarkletConstant.METHODS_SUMMARY_HEADERS);
			getOrderedElements(classDoc::methods)
				.filter(this::isNotInherited)
				.forEach(this::rowSignature);
			newLine();
		}
		// TODO : Build inherited method hierarchy here.
	}
	
	/**
	 * 
	 */
	public void inheritedMethodSummary() {
		ClassDoc current = classDoc.superclass();
		while (current != null) {
			header(5);
			text("Inherited method from ");
			classLink(getSource(), current);
			newLine();
			for (final MethodDoc methodDoc : current.methods()) {
				link(methodDoc.flatSignature(), ""); // TODO : Build method link ?.
			}
			current = current.superclass();
		}
	}
	
	/**
	 * Appends to the current document the
	 * field summary if any field is exposed.
	 */
	private void fieldsSummary() {
		if (hasField()) {
			header(4);
			text(MarkletConstant.FIELDS);
			newLine();
			tableHeader(MarkletConstant.FIELDS_SUMMARY_HEADERS);
			getOrderedElements(classDoc::fields)
				.filter(FieldDoc::isStatic)
				.forEach(this::rowSignature);
			getOrderedElements(classDoc::fields)
				.filter(field -> !field.isStatic())
				.forEach(this::rowSignature);
			newLine();
		}
	}

	/**
	 * Appends to the current document the
	 * constructor summary if any constructor
	 * is exposed.
	 */
	private void constructorsSummary() {
		if (hasConstructor()) {
			header(4);
			text(MarkletConstant.CONSTRUCTORS);
			newLine();
			tableHeader(MarkletConstant.CONSTRUCTOR_SUMMARY_HEADERS);
			getOrderedElements(classDoc::constructors)
				.forEach(this::rowSignature);
			newLine();
		}
	}

	/**
	 * Appends to the current document the class
	 * summary. Consists in an overview of available
	 * constructor, method, and field, in a table form.
	 */
	private void summary() {
		if (hasField() || hasMethod() || hasConstructor()) {
			newLine();
			header(2);
			text(MarkletConstant.SUMMARY);
			newLine();
			fieldsSummary();
			constructorsSummary();
			methodsSummary();
			horizontalRule();
			newLine();
		}
	}

	/**
	 * Appends to the current document detail
	 * about target class constructors.
	 */
	private void constructors() {
		if (hasConstructor()) {
			newLine();
			header(2);
			text(MarkletConstant.CONSTRUCTORS);
			newLine();
			getOrderedElements(classDoc::constructors).forEach(this::member);
		}
	}

	/**
	 * Appends to the current document detail
	 * about target class fields.
	 */
	private void fields() {
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
	 * Appends to the current document detail
	 * about target class methods.
	 */
	private void methods() {
		if (hasMethod()) {
			newLine();
			header(2);
			text(MarkletConstant.METHODS);
			newLine();
			getOrderedElements(classDoc::methods)
				.filter(this::isNotInherited)
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
		builder.header();
		builder.summary();
		builder.constructors();
		builder.fields();
		builder.methods();
		builder.build(directoryPath.resolve(classPath));
	}

}
