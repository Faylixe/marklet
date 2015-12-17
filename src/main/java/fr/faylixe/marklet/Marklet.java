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
	 * @param delegate
	 * @param root
	 */
	private Marklet(final MarkletOptions options, final RootDoc root) {
		this.root = root;
		this.classes = new HashSet<String>();
		this.sourcePath = options.getSourcePath();
		this.documentationPath = options.getDocumentationPath();
		this.outputDirectory = ""; // TODO : Retrives from options.
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

	/**
	 * 
	 */
	private void indexClasses() {
		root.printNotice("Indexing classes");
		for (final ClassDoc classDoc : root.classes()) {
			classes.add(classDoc.name());
		}
		root.printNotice(classes.size() + " class discovered, starting generation.");
	}
	
	/**
	 * @throws IOException 
	 * 
	 */
	private void generateClasses() throws IOException {
		final ClassPageBuilder builder = new ClassPageBuilder(this);
		for (final ClassDoc classDoc : root.classes()) {
			builder.build(classDoc);
		}
	}

	/**
	 * 
	 * @return
	 */
	private boolean start() {
		indexClasses();
		try {
			final Path outputDirectory = Paths.get(getOutputDirectory());
			if (!Files.exists(outputDirectory)) {
				Files.createDirectories(outputDirectory);
			}
			generateClasses();
		}
		catch (final IOException e) {
			root.printError(e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param root
	 * @return
	 */
	public static boolean start(final RootDoc root) {
		final MarkletOptions options = MarkletOptions.parse(root.options());
		final Marklet marklet = new Marklet(options, root);
		return marklet.start();
	}

}
