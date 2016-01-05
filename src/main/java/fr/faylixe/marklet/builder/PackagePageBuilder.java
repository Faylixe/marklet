package fr.faylixe.marklet.builder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.Supplier;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;

import fr.faylixe.marklet.IGenerationContext;
import fr.faylixe.marklet.MarkletConstant;

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
public final class PackagePageBuilder {

	/** Generation context used. **/
	private final IGenerationContext context;

	/** Document builder instance for filling package page content. **/
	private final DocumentBuilder documentBuilder;

	/** Target package that page is built from. **/
	private final PackageDoc packageDoc;

	/**
	 * Default constructor.
	 * 
	 * @param context Generation context used.
	 * @param documentBuilder Document builder instance for filling package page content.
	 * @param packageDoc Target package that page is built from.
	 */
	private PackagePageBuilder(final IGenerationContext context, final DocumentBuilder documentBuilder, final PackageDoc packageDoc) {
		this.context = context;
		this.packageDoc = packageDoc;
		this.documentBuilder = documentBuilder;
	}

	/**
	 * Builds package header. Which consists in
	 * the package name, and the package text
	 * description.
	 * 
	 * @throws IOException If any error occurs while writing header.
	 */
	private void buildHeader() throws IOException {
		documentBuilder.appendHeader(
				new StringBuilder(MarkletConstant.PACKAGE)
					.append(packageDoc.name())
					.toString(),
				1);
		documentBuilder.newLine();
		documentBuilder.appendText(packageDoc.commentText());
		documentBuilder.newLine();
	}

	/**
	 * Builds an index row for the given ``classDoc``.
	 * 
	 * @param classDoc Target class to write as an index row.
	 * @throws IOException If any error occurs while writing row.
	 */
	private void buildClassRow(final ClassDoc classDoc) {
		try {			
			documentBuilder.appendTableRow(context.getClassLink(packageDoc, classDoc));
		}
		catch (final IOException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Builds a class based index, namely list each
	 * type in a markdown table. Such type could be
	 * either class, interface, or enumeration.
	 * 
	 * TODO : Add type description snippet.
	 * 
	 * @param label Label of the type categories.
	 * @param classSupplier Type supplier.
	 * @throws IOException If any error occurs while writing type listing.
	 */
	private void buildIndex(final String label, final Supplier<ClassDoc[]> classSupplier) throws IOException {
		final ClassDoc [] classDocs = classSupplier.get();
		if (classDocs.length > 0) {
			documentBuilder.appendHeader(label, 2);
			documentBuilder.appendTableHeader(MarkletConstant.NAME);
			Arrays
				.stream(classDocs)
				.forEach(this::buildClassRow);
			documentBuilder.newLine();
		}
	}

	/**
	 * Main package building process, build listing
	 * for the following type category :
	 * 
	 * * Classes
	 * * Interfaces
	 * * Enumerations
	 * * Annotations
	 * 
	 * @throws IOException If any error occurs while writing indexes.
	 */
	private void buildIndexes() throws IOException {
		// TODO : Build annotation index.
		buildIndex(MarkletConstant.ENUMERATIONS, packageDoc::enums);
		buildIndex(MarkletConstant.INTERFACES, packageDoc::interfaces);
		buildIndex(MarkletConstant.CLASSES, packageDoc::allClasses);
	}

	/**
	 * Builds and writes the documentation file associated
	 * to the given ``packageDoc`` into the directory denoted
	 * by the given ``directoryPath``.
	 * 
	 * @param context Context used.
	 * @param packageDoc Package to generated documentation for.
	 * @param directoryPath Path of the directory to write documentation in.
	 * @throws IOException 
	 */
	public static void build(final IGenerationContext context, final PackageDoc packageDoc, final Path directoryPath) throws IOException {
		final Path path = directoryPath.resolve(MarkletConstant.README);
		final DocumentBuilder documentBuilder = DocumentBuilder.create(context, packageDoc, path);
		final PackagePageBuilder packageBuilder = new PackagePageBuilder(context, documentBuilder, packageDoc);
		packageBuilder.buildHeader();
		packageBuilder.buildIndexes();
		documentBuilder.build();
	}

}
