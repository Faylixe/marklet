package fr.faylixe.marklet.builder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Supplier;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;

import fr.faylixe.marklet.IGenerationContext;

/**
 * 
 * @author fv
 */
public final class PackagePageBuilder {

	/** Suffix of the Package documentation header. **/
	private static final String PACKAGE_HEADER = "Package ";

	/** Header for the interface section. **/
	private static final String INTERFACES = "Interfaces";

	/** Header for the class section. **/
	private static final String CLASSES = "Classes";

	/** Header for the enumeration section. **/
	private static final String ENUMERATIONS = "Enumerations";

	/** **/
	private final DocumentBuilder documentBuilder;

	/** **/
	private final PackageDoc packageDoc;

	/**
	 * Default constructor.
	 * 
	 * @param documentBuilder
	 * @param packageDoc
	 */
	private PackagePageBuilder(final DocumentBuilder documentBuilder, final PackageDoc packageDoc) {
		this.packageDoc = packageDoc;
		this.documentBuilder = documentBuilder;
	}

	/**
	 * 
	 * @throws IOException 
	 */
	private void buildHeader() throws IOException {
		documentBuilder.appendHeader(
				new StringBuilder(PACKAGE_HEADER)
					.append(packageDoc.name())
					.toString(),
				1);
		documentBuilder.newLine();
		documentBuilder.append(packageDoc.commentText());
	}

	/**
	 * 
	 * @param label
	 * @param classSupplier
	 * @throws IOException 
	 */
	private void buildIndex(final String label, final Supplier<ClassDoc[]> classSupplier) throws IOException {
		documentBuilder.appendHeader(label, 2);
		for (final ClassDoc classDoc : classSupplier.get()) {
			
		}
	}

	/**
	 * 
	 * @throws IOException
	 */
	private void buildIndexes() throws IOException {
		buildIndex(ENUMERATIONS, packageDoc::enums);
		buildIndex(INTERFACES, packageDoc::interfaces);
		buildIndex(CLASSES, packageDoc::allClasses);
	}

	/**
	 * 
	 * @param context
	 * @param directoryPath
	 * @param packageDoc
	 * @throws IOException 
	 */
	public static void build(final IGenerationContext context, final Path directoryPath, final PackageDoc packageDoc) throws IOException {
		final Path path = directoryPath.resolve("README.md");
		final DocumentBuilder documentBuilder = DocumentBuilder.create(context, path);
		final PackagePageBuilder packageBuilder = new PackagePageBuilder(documentBuilder, packageDoc);
		packageBuilder.buildHeader();
		packageBuilder.buildIndexes();
	}

}
