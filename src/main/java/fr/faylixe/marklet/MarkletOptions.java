package fr.faylixe.marklet;

import com.sun.javadoc.DocErrorReporter;

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

	/** Default output directory to use. **/
	private static final String DEFAULT_OUTPUT_DIRECTORY = "javadoc/";

	/** Option name for the target output directory. **/
	private static final String OUTPUT_DIRECTORY_OPTION = "-d";

	/** Default output file ending (`md`) **/
	private static final String DEFAULT_FILE_ENDING = "md";

	/** Option name for the file ending (`-e`) **/
	private static final String FILE_ENDING_OPTION = "-e";

	/** Default ending for internal links (`md`). **/
	private static final String DEFAULT_LINK_ENDING = "md";

	/** Option name for the link ending (`-l`) **/
	private static final String LINK_ENDING_OPTION = "-l";

	/** Output directory file are generated in. **/
	private String outputDirectory;

	private String fileEnding;

	private String linkEnding;

	/**
	 * Default constructor.
	 * Sets options with their default parameters if available.
	 */
	private MarkletOptions() {
		this.outputDirectory = DEFAULT_OUTPUT_DIRECTORY;
		this.fileEnding = DEFAULT_FILE_ENDING;
		this.linkEnding = DEFAULT_LINK_ENDING;
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
	 * Private setter that sets the output directory option.
	 * 
	 * @param outputDirectory Output directory file are generated in.
	 * @see #outputDirectory
	 */
	private void setOutputDirectory(final String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	/**
	 * TODO : Perform validation.
	 * 
	 * @param options Options from command line.
	 * @param reporter Reporter instance to use in case of error.
	 * @return <tt>true</tt> if given set of options are valid, <tt>false</tt> otherwise.
	 */
	public static boolean validOptions(final String options[][], final DocErrorReporter reporter) {
		return true;
	}
	
	/**
	 * TODO : Process other option.
	 * @param option To document.
	 * @return To document.
	 */
	public static int optionLength(final String option) {
		if (option.equals(OUTPUT_DIRECTORY_OPTION)) {
			return 2;
		}
		return 0;
	}

	/**
	 * Static factory.
	 * 
	 * @param rawOptions Raw options array to parse.
	 * @return Built options instance.
	 */
	public static MarkletOptions parse(final String [][] rawOptions) {
		final MarkletOptions options = new MarkletOptions();
		for (final String [] option : rawOptions) {
			final String name = option[0];
			System.out.println("Checking option : " + name);
			if (name.equals(OUTPUT_DIRECTORY_OPTION)) {
				System.out.println("Matching output directory : " + option[1]);
				options.setOutputDirectory(option[1]);
			} else if (name.equals(LINK_ENDING_OPTION)) {
				System.out.println("Matching link ending : " + option[1]);
				options.setLinkEnding(option[1]);
			} else if (name.equals(FILE_ENDING_OPTION)) {
				System.out.println("Matching file ending : " + option[1]);
				options.setFileEnding(option[1]);
			}
		}
		return options;
	}

	public String getFileEnding() {
		return fileEnding;
	}

	public void setFileEnding(String fileEnding) {
		this.fileEnding = fileEnding;
	}

	public String getLinkEnding() {
		return linkEnding;
	}

	public void setLinkEnding(String linkEnding) {
		this.linkEnding = linkEnding;
	}
}
