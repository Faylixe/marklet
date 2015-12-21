package fr.faylixe.marklet;

import com.sun.javadoc.ClassDoc;

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
	default String getClassLink(final ClassDoc classDoc) {
		final String qualifiedName = classDoc.qualifiedName();
		if (containsClass(qualifiedName)) {
			final StringBuilder urlBuilder = new StringBuilder();
			urlBuilder
				.append(getDocumentationPath())
				.append(getOutputDirectory())
				.append(qualifiedName.replace('.', '/'))
				.append(FILE_EXTENSION);
			return MarkdownUtils.buildLink(classDoc.simpleTypeName(), urlBuilder.toString());
		}
		// TODO : Process offline link.
		return "";
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
