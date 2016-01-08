package fr.faylixe.marklet;

import org.apache.commons.lang3.StringUtils;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.SeeTag;
import com.sun.javadoc.Type;

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
			final StringBuffer urlBuilder = new StringBuffer();
			urlBuilder
				.append(path)
				.append(target.simpleTypeName())
				.append(Markdown.FILE_EXTENSION);
			return Markdown.link(target.simpleTypeName(), urlBuilder.toString());
		}
		// TODO : Figure out what can be returned here ?
		return "";
	}
	
	/**
	 * Creates and returns a valid markdown link
	 * for the given ``type``. If this ``type``
	 * is a primitive one, then only a bold label
	 * is produced. Otherwise it return a link
	 * created by the {@link #getClassLink(PackageDoc, ClassDoc)}
	 * method.
	 * 
	 * @param source Source package to start URL from.
	 * @param type Target type to reach from this package.
	 * @return Bold label if the given ``type`` is primitive, a valid class link otherwise.
	 */
	default String getTypeLink(final PackageDoc source, final Type type) {
		final StringBuffer returnBuilder = new StringBuffer();
		if (type.isPrimitive()) {
			returnBuilder.append(Markdown.bold(type.simpleTypeName()));
		}
		else {
			final ClassDoc classDoc = type.asClassDoc();
			final String link = getClassLink(source, classDoc);
			returnBuilder.append(link);
		}
		return returnBuilder.toString();
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
	 * Builds and returns ``member`` returns label,
	 * which is composed of the given ``member``
	 * modifiers if any, followed by the return 
	 * type link, if the given ``member`` is a method, 
	 * 
	 * @param member Member to build return label for.
	 * @return Built label.
	 */
	default String getReturn(final ExecutableMemberDoc member) {
		final String modifiers = Markdown.bold(member.modifiers());
		final StringBuffer returnBuilder = new StringBuffer().append(modifiers);
		if (member instanceof MethodDoc) {
			final MethodDoc method = (MethodDoc) member;
			returnBuilder
				.append(' ')
				.append(getTypeLink(method.containingPackage(), method.returnType()));
		}
		return returnBuilder.toString();
	}

	/**
	 * Builds and returns signature for the given
	 * ``member`` as a markdown table row.
	 * 
	 * @param member Member to build table row from.
	 * @return Built row as an array.
	 */
	default String[] getRowSignature(final ExecutableMemberDoc member) {
		return new String[] {
				getReturn(member),
				getLinkedName(member)
		};
	}
	
	/**
	 * Builds and returns signature for the given
	 * ``member`` as a markdown list item.
	 * 
	 * @param member Member to build list item from.
	 * @return Built list item.
	 */
	default String getItemSignature(final ExecutableMemberDoc member) {
		return Markdown.listItem(
				new StringBuffer()
					.append(getReturn(member))
					.append(' ')
					.append(getLinkedName(member))
					.toString());
	}
	
	/**
	 * 
	 * @param member
	 * @return
	 */
	static String getLinkedName(final ExecutableMemberDoc member) {
		final StringBuffer anchorBuilder = new StringBuffer()
			.append('#')
			.append(member.name());
		final Parameter[] parameters = member.parameters();
		for (int i = 0; i < parameters.length; i++) {
			anchorBuilder.append(parameters[i].typeName());
			if (i < parameters.length - 1) {
				anchorBuilder.append('-');
			}
		}
		final String url = anchorBuilder.toString().toLowerCase();
		return Markdown.link(member.name(), url);
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
		final StringBuffer pathBuilder = new StringBuffer();
		for (int i = 0; i < StringUtils.countMatches(back, '.'); i++) {
			pathBuilder.append("../");
		}
		final String forward = target.substring(start); // TODO : Checkout index evolution (+1, 0).
		pathBuilder.append(forward.replace('.', '/'));
		pathBuilder.append('/');
		return pathBuilder.toString();
	}
	
}
