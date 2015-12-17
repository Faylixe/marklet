package fr.faylixe.marklet;

/**
 * 
 * @author fv
 */
public final class MarkletOptions {

	/** **/
	private static final String SOURCE_PATH_OPTION = "-sourceurl";

	/** **/
	private static final String DOCUMENTATION_PATH_OPTION = "-documentationurl";

	/** **/
	private String sourcePath;

	/** **/
	private String documentationPath;

	/**
	 * 
	 */
	private MarkletOptions() {
		
	}

	/**
	 * 
	 * @return
	 */
	public String getSourcePath() {
		return sourcePath;
	}

	/**
	 * 
	 * @return
	 */
	public String getDocumentationPath() {
		return documentationPath;
	}

	/**
	 * 
	 * @param name
	 * @param value
	 */
	private void set(final String name, final String value) {
		if (SOURCE_PATH_OPTION.equals(name)) {
			sourcePath = value;
		}
		else if (DOCUMENTATION_PATH_OPTION.equals(name)) {
			documentationPath = value;
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean isConsistent() {
		return sourcePath != null && documentationPath != null;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	private static boolean isOption(final String name) {
		return SOURCE_PATH_OPTION.equals(name) || DOCUMENTATION_PATH_OPTION.equals(name);
	}

	/**
	 * 
	 * @param options
	 * @return
	 */
	public static MarkletOptions parse(final String [][] rawOptions) {
		final MarkletOptions options = new MarkletOptions();
		for (final String [] option : rawOptions) {
			final String name = option[0];
			if (isOption(name)) {
				if (option.length < 2) {
					throw new IllegalArgumentException("Error when parsing option :'(");
				}
				final String value = option[1];
				options.set(name, value);
			}
		}
		return options;
	}

}
