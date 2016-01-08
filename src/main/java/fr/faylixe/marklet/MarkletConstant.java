package fr.faylixe.marklet;

/**
 * 
 * 
 * @author fv
 */
public final class MarkletConstant {

	/** Label for package. **/
	public static final String PACKAGE = "Package ";

	/** Label for interfaces. **/
	public static final String INTERFACES = "Interfaces";

	/** Label for classes. **/
	public static final String CLASSES = "Classes";

	/** Label for enumerations. **/
	public static final String ENUMERATIONS = "Enumerations";

	/** Label for constructor. **/
	public static final String CONSTRUCTORS = "Constructors";

	/** Label for methods. **/
	public static final String METHODS = "Methods";

	/** Label for fields. **/
	public static final String FIELDS = "Fields";

	/** Package index filename. **/
	public static final String README = "README.md";

	/** Label for name. **/
	public static final String NAME = "Name";

	/** Label for description. **/
	public static final String DESCRIPTION = "Description";

	/** Label for throws. **/
	public static final String THROWS = "Throws";

	/** Label for returns. **/
	public static final String RETURNS = "Returns";

	/** Label for parameters. **/
	public static final String PARAMETERS = "Parameters";

	/** Label for summary. **/
	public static final String SUMMARY = "Summary";

	/** Header labels for methods summary. **/
	public static final String [] METHODS_SUMMARY_HEADERS = {"Type and modifiers", "Method signature"};

	/** Marklet link using dynamic badge. **/
	public static final String BADGE = "[![Marklet](https://img.shields.io/badge/Generated%20by-Marklet-green.svg)](https://github.com/Faylixe/marklet)";

	/** Markdown sequence for cell separator. **/
	public static final String TABLE_SEPARATOR = " | ";

	/**
	 * Private constructor for avoiding instantiation.
	 */
	private MarkletConstant() {
		// Do nothing.
	}

}
