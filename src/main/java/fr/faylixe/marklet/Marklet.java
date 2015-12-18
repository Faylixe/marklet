package fr.faylixe.marklet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import com.sun.javadoc.*;

/**
 * 
 * @author fv
 */
public final class Marklet implements IGenerationContext {

	/** **/
	public static final String FILE_EXTENSION = ".md";

	/** **/
	private final RootDoc root;

	/** **/
	private final Set<String> classes;

	/** **/
	private final String sourcePath;

	/** **/
	private final String documentationPath;

	/** **/
	private final String outputDirectory;

	/**
	 * Default constructor.
	 * 
	 * @param options
	 * @param root
	 */
	private Marklet(final MarkletOptions options, final RootDoc root) {
		this.root = root;
		this.classes = new HashSet<String>();
		this.sourcePath = options.getSourcePath();
		this.documentationPath = options.getDocumentationPath();
		this.outputDirectory = "markdoc/"; // TODO : Retrives from options.
	}

	/** {@inheritDoc} **/
	public String getSourcePath() {
		return sourcePath;
	}

	/** {@inheritDoc} **/
	public String getDocumentationPath() {
		return documentationPath;
	}

	/** {@inheritDoc} **/
	public String getOutputDirectory() {
		return outputDirectory;
	}

	/** {@inheritDoc} **/
	public String getClassURL(final String qualifiedName) {
		final StringBuilder urlBuilder = new StringBuilder();
		if (classes.contains(qualifiedName)) {
			urlBuilder.append(getDocumentationPath());
			urlBuilder.append(qualifiedName.replace('.', '/'));
			urlBuilder.append(".md");
		}
		return urlBuilder.toString();
	}
	
	/**
	 * Builds and retrieves the path for the
	 * directory associated to the package
	 * with the given <tt>name</tt>.
	 * 
	 * @param name Name of the package to get directory for.
	 * @return Built path.
	 */
	private Path getPackageDirectory(final String packageName) {
		final String directory = packageName.replace('.', '/');
		final String path = new StringBuilder()
			.append(outputDirectory)
			.append(directory)
			.toString();
		return Paths.get(path);
	}

	/**
	 * Generates package documentation for the given
	 * <tt>packageDoc</tt>.
	 * 
	 * @param packageDoc Package to generate documentation for.
	 * @throws IOException If any error occurs while creating file or directories.
	 */
	private Path generatePackage(final PackageDoc packageDoc) throws IOException {
		final String name = packageDoc.name();
		root.printNotice("Generates package documentation for " + name);
		if (!name.isEmpty()) {
			final Path directoryPath = getPackageDirectory(name);
			if (!Files.exists(directoryPath)) {
				Files.createDirectories(directoryPath);
			}
			PackagePageBuilder.build(directoryPath, packageDoc);
			return directoryPath;
		}
		return Paths.get(".");
	}

	/**
	 * Builds the javadoc, for each available classes
	 * and associated package.
	 * 
	 * @throws IOException If any error occurs during generation process.
	 */
	private void build() throws IOException {
		final Set<PackageDoc> packages = new HashSet<PackageDoc>();
		for (final ClassDoc classDoc : root.classes()) {
			final PackageDoc packageDoc = classDoc.containingPackage();
			if (!packages.contains(packageDoc)) {
				packages.add(packageDoc);
				final Path directoryPath = generatePackage(packageDoc);			
				ClassPageBuilder.build(classDoc, directoryPath);
			}
			else {
				ClassPageBuilder.build(classDoc, getPackageDirectory(packageDoc.name()));				
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	private boolean start() {
		try {
			final Path outputDirectory = Paths.get(getOutputDirectory());
			if (!Files.exists(outputDirectory)) {
				Files.createDirectories(outputDirectory);
			}
			build();
		}
		catch (final IOException e) {
			root.printError(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Doclet entry point. Parses user provided options
	 * and starts a Marklet execution.
	 * 
	 * @param root Doclet API root.
	 * @return <tt>true</tt> if the generation went well, <tt>false<tt> otherwise.
	 */
	public static boolean start(final RootDoc root) {
		final MarkletOptions options = MarkletOptions.parse(root.options());
		final Marklet marklet = new Marklet(options, root);
		return marklet.start();
	}

}
