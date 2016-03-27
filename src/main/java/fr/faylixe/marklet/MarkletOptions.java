package fr.faylixe.marklet;

import com.sun.javadoc.DocErrorReporter;

/**
 * Class that reads and stores provided options
 * for javadoc execution. Options that we care about are :
 * 
 * * Output directory
 * 
 * @author fv
 */
public final class MarkletOptions {

	/** Default output directory to use. **/
	private static final String DEFAULT_OUTPUT_DIRECTORY = "javadoc/";

	/** Option name for the target output directory. **/
	private static final String OUTPUT_DIRECTORY_OPTION = "-d";

	/** Output directory file are generated in. **/
	private String outputDirectory;

	/**
	 * Default constructor.
	 * Sets options with their default parameters if availaible.
	 */
	private MarkletOptions() {
		this.outputDirectory = DEFAULT_OUTPUT_DIRECTORY;
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
	 * @param options
	 * @param reporter
	 * @return
	 */
	public static boolean validOptions(final String options[][], final DocErrorReporter reporter) {
		return true;
	}
	
	/**
	 * TODO : Process other option.
	 * @param option
	 * @return
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
			}
		}
		return options;
	}

}
