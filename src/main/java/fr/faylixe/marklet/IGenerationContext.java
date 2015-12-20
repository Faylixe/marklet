package fr.faylixe.marklet;

/**
 * 
 * @author fv
 */
public interface IGenerationContext {

	/** **/
	static final String FILE_EXTENSION = ".md";

	/**
	 * 
	 * @return
	 */
	String getSourcePath();

	/**
	 * 
	 * @return
	 */
	String getDocumentationPath();

	/**
	 * 
	 * @return
	 */
	String getOutputDirectory();

	/**
	 * 
	 * @param qualifiedName
	 * @return
	 */
	boolean containsClass(final String qualifiedName);

	/**
	 * 
	 * @param qualifiedName
	 * @return
	 */
	default String getClassURL(final String qualifiedName) {
		final StringBuilder urlBuilder = new StringBuilder();
		if (containsClass(qualifiedName)) {
			urlBuilder
				.append(getDocumentationPath())
				.append(getOutputDirectory())
				.append(qualifiedName.replace('.', '/'))
				.append(FILE_EXTENSION);
		}
		return urlBuilder.toString();
	}

	/**
	 * 
	 * @param qualifiedName
	 * @return
	 */
	default String getPackageURL(final String qualifiedName) {
		final StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(getDocumentationPath());
		urlBuilder.append(qualifiedName.replace('.', '/'));
		return urlBuilder.toString();
	}

}
