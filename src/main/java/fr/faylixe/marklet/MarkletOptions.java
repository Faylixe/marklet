package fr.faylixe.marklet;

import java.util.HashMap;
import java.util.Map;

import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.RootDoc;

/**
 * Class that reads and stores provided options
 * for javadoc execution. Options that we care about are :
 * 
 * * `-d` specifies the output directory (default: `javadocs`)
 * * `-e` specifies the file ending for files to be created (default `md`)
 * * `-l` specifies the file ending used in internal links (default `md`)
 *
 * > The default options are ideal if you want to serve the documentation using GitHub's
 * > built-in README rendering. If you are using a tool like Slate, change the options as follows:
 * ```
 * $ javadoc -doclet fr.faylixe.marklet.Marklet -e html.md -l html …
 * ```
 * 
 * @author fv
 */
public final class MarkletOptions {

	/** Option name for the target output directory. **/
	private static final String OUTPUT_DIRECTORY_OPTION = "-d";

	/** Option name for the file ending (`-e`) **/
	private static final String FILE_ENDING_OPTION = "-e";

	/** Option name for the link ending (`-l`) **/
	private static final String LINK_ENDING_OPTION = "-l";

	/** Number of token per option. **/
	private static final Map<String, Integer> OPTIONS_COUNT = new HashMap<>();

	static {
		OPTIONS_COUNT.put(OUTPUT_DIRECTORY_OPTION, 2);
		OPTIONS_COUNT.put(FILE_ENDING_OPTION, 2);
		OPTIONS_COUNT.put(LINK_ENDING_OPTION, 2);
	}

	/** Default output directory to use. **/
	private static final String DEFAULT_OUTPUT_DIRECTORY = "javadoc/";

	/** Default output file ending (`md`) **/
	private static final String DEFAULT_FILE_ENDING = "md";

	/** Default ending for internal links (`md`). **/
	private static final String DEFAULT_LINK_ENDING = "md";

	/** Output directory file are generated in. **/
	private String outputDirectory;

	/** Extension to use for generated file. **/
	private String fileEnding;

	/** Extension to use for generated link. **/
	private String linkEnding;

	/**
	 * Default constructor.
	 * Sets options with their default parameters if available.
	 */
	private MarkletOptions(final Map<String, String> options) {
		this.outputDirectory = options.getOrDefault(OUTPUT_DIRECTORY_OPTION, DEFAULT_OUTPUT_DIRECTORY);
		this.fileEnding = options.getOrDefault(FILE_ENDING_OPTION, DEFAULT_FILE_ENDING);
		this.linkEnding = options.getOrDefault(LINK_ENDING_OPTION, DEFAULT_LINK_ENDING);
	}

	/**
	 * Getter for the output directory option.
	 * 
	 * @return Output directory file are generated in.
	 * @see #outputDirectory
	 */
	public String getOutputDirectory() {
		return outputDirectory;
	}

	/**
	 * Getter for file extension option.
	 * 
	 * @return Extension to use for generated file.
	 * @see #fileEnding
	 */
	public String getFileEnding() {
		return fileEnding;
	}

	/**
	 * Getter for link extension option.
	 * 
	 * @return Extension to use for generated link.
	 * @see #linkEnding
	 */
	public String getLinkEnding() {
		return linkEnding;
	}

	/**
	 * Options validation method.
	 * 
	 * @param options Options from command line.
	 * @param reporter Reporter instance to use in case of error.
	 * @return <tt>true</tt> if given set of options are valid, <tt>false</tt> otherwise.
	 * @see Doclet#validOptions(String[][], DocErrorReporter)
	 */
	public static boolean validOptions(final String options[][], final DocErrorReporter reporter) {
		// TODO : Perform options validation here.
		return true;
	}
	
	/**
	 * Computes number of arguments (as token) for the given option.
	 * 
	 * @param option Target option to get token number for.
	 * @return Number of token expected for the given option.
	 * @see Doclet#optionLength(String)
	 */
	public static int optionLength(final String option) {
		return OPTIONS_COUNT.getOrDefault(option, 0);
	}

	/**
	 * Static factory.
	 * 
	 * @param rawOptions Raw options array to parse.
	 * @return Built options instance.
	 */
	public static MarkletOptions parse(final RootDoc root) {
		final Map<String, String> options = new HashMap<>();
		// NOTE :	Work since we only have 2D option.
		//			Consider redesign option parsing if this predicate change.
		for (final String [] option : root.options()) {
			options.put(option[0], option[1]);
		}
		return new MarkletOptions(options);
	}

}
