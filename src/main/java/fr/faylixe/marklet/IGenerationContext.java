package fr.faylixe.marklet;

import org.apache.commons.lang3.StringUtils;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;

/**
 * 
 * @author fv
 */
public interface IGenerationContext {

	/** **/
	static final String FILE_EXTENSION = ".md";

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
	boolean containsPackage(final String qualifiedName);

	/**
	 * 
	 * @param currentPackage
	 * @param target
	 * @return
	 */
	default String getClassLink(final PackageDoc source, final ClassDoc target) {
		final String qualifiedName = target.qualifiedName();
		if (containsClass(qualifiedName)) {
			final String path = getPath(source.name(), target.containingPackage().name());
			final StringBuilder urlBuilder = new StringBuilder();
			urlBuilder
				.append(path)
				.append(target.simpleTypeName())
				.append(FILE_EXTENSION);
			return MarkdownUtils.buildLink(target.simpleTypeName(), urlBuilder.toString());
		}
		return "";
	}

	/**
	 * Static method that builds a shortest URL path, from
	 * the given ``source`` package to the ``target`` package.
	 * 
	 * @param source Source package to build path from.
	 * @param target Target package to build path to.
	 * @return Built path.
	 */
	static String getPath(final String source, final String target) {
		if (source.equals(target)) {
			return "";
		}
		final String common = StringUtils.getCommonPrefix(source, target);
		final int start = common.length();
		final String back = source.substring(start);
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < StringUtils.countMatches(back, '.'); i++) {
			builder.append("../");
		}
		final String forward = target.substring(start);
		builder.append(forward.replace('.', '/'));
		return builder.toString();
	}

}
