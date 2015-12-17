package fr.faylixe.marklet;

/**
 * 
 * @author fv
 */
public interface IGenerationContext {

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

}
