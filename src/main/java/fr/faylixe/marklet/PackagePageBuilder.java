package fr.faylixe.marklet;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.Supplier;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;

/**
 * Builder that aims to create documentation
 * page for a given ``package``. Such documentation
 * consists in a package description followed by
 * type listing over following categories :
 * 
 * * Classes
 * * Interfaces
 * * Enumerations
 * * Annotations
 * 
 * @author fv
 */
public final class PackagePageBuilder extends MarkletDocumentBuilder {

	/** Target package that page is built from. **/
	private final PackageDoc packageDoc;

	/**
	 * Default constructor.
	 * 
	 * @param packageDoc Target package that page is built from.
	 */
	private PackagePageBuilder(final PackageDoc packageDoc) {
		super(packageDoc);
		this.packageDoc = packageDoc;
	}

	/**
	 * Appends package header to the current
	 * document . Which consists in the package
	 * name, and the package text description.
	 */
	private void header() {
		header(1);
		text(MarkletConstant.PACKAGE);
		character(' ');
		text(packageDoc.name());
		newLine();
		description(packageDoc);
		newLine();
	}

	/**
	 * Appends a class based index to the current
	 * document, namely list each type in a markdown
	 * table. Such type could be either class,
	 * interface, or enumeration.
	 * 
	 * @param label Label of the type categories.
	 * @param classSupplier Type supplier.
	 */
	private void classIndex(final String label, final Supplier<ClassDoc[]> classSupplier) {
		final ClassDoc [] classDocs = classSupplier.get();
		if (classDocs.length > 0) {
			header(2);
			text(label);
			newLine();
			tableHeader(MarkletConstant.NAME);
			Arrays
				.stream(classDocs)
				.forEach(this::classRow);
			newLine();
		}
	}

	/**
	 * Appends a class link row to the current
	 * index built in the current document.
	 * 
	 * @param classDoc Class to append link from.
	 */
	private void classRow(final ClassDoc classDoc) {
		startTableRow();
		classLink(packageDoc, classDoc);
		endTableRow();
		newLine();
	}

	/**
	 * Main package building process, build listing
	 * for the following type category :
	 * 
	 * * Classes
	 * * Interfaces
	 * * Enumerations
	 * * Annotations
	 */
	private void indexes() {
		classIndex(MarkletConstant.ANNOTATIONS, packageDoc::annotationTypes);
		classIndex(MarkletConstant.ENUMERATIONS, packageDoc::enums);
		classIndex(MarkletConstant.INTERFACES, packageDoc::interfaces);
		classIndex(MarkletConstant.CLASSES, packageDoc::allClasses);
	}

	/**
	 * Builds and writes the documentation file associated
	 * to the given ``packageDoc`` into the directory denoted
	 * by the given ``directoryPath``.
	 * 
	 * @param packageDoc Package to generated documentation for.
	 * @param directoryPath Path of the directory to write documentation in.
	 * @throws IOException If any error occurs while writing package page.
	 */
	public static void build(final PackageDoc packageDoc, final Path directoryPath) throws IOException {
		final PackagePageBuilder packageBuilder = new PackagePageBuilder( packageDoc);
		packageBuilder.header();
		packageBuilder.indexes();
		final Path path = directoryPath.resolve(MarkletConstant.README_FILE);
		packageBuilder.build(path);
	}

}
