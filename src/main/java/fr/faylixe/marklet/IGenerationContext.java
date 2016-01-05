package fr.faylixe.marklet;

import org.apache.commons.lang3.StringUtils;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.SeeTag;

/**
 * An {@link IGenerationContext} aims to wrap the
 * original API entry point {@link RootDoc}, and providing
 * contextual facilities such as class link generation,
 * class / package presence predicate or link tag processing
 * over comment text.
 * 
 * @author fv
 */
public interface IGenerationContext {

	/**
	 * Indicates if the class denoted by the
	 * given ``qualifiedName`` is present in
	 * this documentation process.
	 * 
	 * @param qualifiedName Name of the class to check presence.
	 * @return ``true`` if the class is present, ``false`` otherwise.
	 */
	boolean containsClass(final String qualifiedName);

	/**
	 * Indicates if the package denoted by the
	 * given ``qualifiedName`` is present in
	 * this documentation process.
	 * 
	 * @param qualifiedName Name of the package to check presence.
	 * @return ``true`` if the package is present, ``false`` otherwise.
	 */
	boolean containsPackage(final String qualifiedName);

	/**
	 * Creates and returns a valid markdown link
	 * that aims to be the shortest one, by using the
	 * {@link #getPath(String, String)} method. The
	 * built URL will start from the given ``source``
	 * package to the given ``target`` class.
	 *  
	 * @param source Source package to start URL from.
	 * @param target Target class to reach from this package.
	 * @return Built markdown link.
	 */
	default String getClassLink(final PackageDoc source, final ClassDoc target) {
		if (containsClass(target.qualifiedName())) {
			final String path = getPath(source.name(), target.containingPackage().name());
			final StringBuilder urlBuilder = new StringBuilder();
			urlBuilder
				.append(path)
				.append(target.simpleTypeName())
				.append(MarkdownUtils.FILE_EXTENSION);
			return MarkdownUtils.buildLink(target.simpleTypeName(), urlBuilder.toString());
		}
		// TODO : Figure out what can be returned here ?
		return "";
	}

	/**
	 * This methods will process the given ``doc``
	 * comment text, by replacing each link tags
	 * by effective markdown link.
	 * 
	 * @param doc Documentation element to process descrption from.
	 * @return Processed documentation text.
	 */
	default String getDescription(final Doc doc) {
		for (final SeeTag tag : doc.seeTags()) {
			tag.referencedClass(); // TODO : Build markdown link here.
		}
		return doc.commentText();
	}

	/**
	 * Static method that builds a shortest URL path, from
	 * the given ``source`` package to the ``target`` package.
	 * Such path is built by taking the longest common prefix
	 * from both package name, trying to move from source to this
	 * prefix using ``../`` path, then moving to the target path
	 * vertically.
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
		builder.append('/');
		return builder.toString();
	}

	
}
