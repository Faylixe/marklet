package fr.faylixe.marklet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.commons.lang3.StringUtils;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.SeeTag;
import com.sun.javadoc.Tag;
import com.sun.javadoc.ThrowsTag;
import com.sun.javadoc.Type;

/**
 * 
 * @author fv
 */
public class MarkletDocumentBuilder extends MarkdownDocumentBuilder {

	/** Target source package from which document will be written. **/
	private final PackageDoc source;

	/**
	 * Default constructor. 
	 * 
	 * @param source Target source package from which document will be written. 
	 */
	public MarkletDocumentBuilder(final PackageDoc source) {
		this.source = source;
	}

	/**
	 * Creates and returns a valid markdown link
	 * that aims to be the shortest one, by using the
	 * {@link #getPath(String, String)} method. The
	 * built URL will start from the given ``source``
	 * package to the given ``target`` class.
	 *  
	 * @param source Source package to start URL from.
	 * @param target Target class to reach from this package.
	 */
	public void classLink(final PackageDoc source, final ClassDoc target) {
		if (target.isIncluded()) {
			final String path = getPath(source.name(), target.containingPackage().name());
			final StringBuffer urlBuilder = new StringBuffer();
			urlBuilder
				.append(path)
				.append(target.simpleTypeName())
				.append(MarkdownDocumentBuilder.FILE_EXTENSION);
			link(target.simpleTypeName(), urlBuilder.toString());
		}
		else {
			// TODO : Process external link here.
			italic(target.qualifiedName());
		}
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
	 */
	public void typeLink(final PackageDoc source, final Type type) {
		if (type.isPrimitive()) {
			bold(type.simpleTypeName());
		}
		else {
			final ClassDoc classDoc = type.asClassDoc();
			classLink(source, classDoc);
		}
	}

	/**
	 * This methods will process the given ``doc``
	 * comment text, by replacing each link tags
	 * by effective markdown link.
	 * 
	 * @param doc Documentation element to process descrption from.
	 * @return Processed documentation text.
	 */
	public void description(final Doc doc) {
		for (final SeeTag tag : doc.seeTags()) {
			tag.referencedClass(); // TODO : Build markdown link here.
		}
		text(doc.commentText());
	}

	/**
	 * Builds and returns ``member`` returns label,
	 * which is composed of the given ``member``
	 * modifiers if any, followed by the return 
	 * type link, if the given ``member`` is a method, 
	 * 
	 * @param element Member to build return label for.
	 * @return Built label.
	 */
	public void returnSignature(final ProgramElementDoc element) {
		bold(element.modifiers());
		if (element.isMethod()) {
			final MethodDoc method = (MethodDoc) element;
			text(" ");
			typeLink(method.containingPackage(), method.returnType());
		}
	}
	
	/**
	 * 
	 * @param element
	 * @return
	 */
	public void linkedName(final ProgramElementDoc element) {
		final StringBuffer anchorBuilder = new StringBuffer()
			.append('#')
			.append(element.name());
		if (element instanceof ExecutableMemberDoc) {
			final ExecutableMemberDoc member = (ExecutableMemberDoc) element;
			final Parameter[] parameters = member.parameters();
			for (int i = 0; i < parameters.length; i++) {
				anchorBuilder.append(parameters[i].typeName());
				if (i < parameters.length - 1) {
					anchorBuilder.append('-');
				}
			}
		}
		final String url = anchorBuilder.toString().toLowerCase();
		link(element.name(), url);
	}

	/**
	 * 
	 * @param parameters
	 */
	private void inlineParameters(final Parameter[] parameters) {
		text("(");
		for (int i = 0; i < parameters.length; i++) {
			System.out.println("Parameter type : " + parameters[i].typeName());
			typeLink(source, parameters[i].type());
			text(" ");
			text(parameters[i].name());
			if (i < parameters.length - 1) {
				text(", ");
			}
		}
		text(")");
	}

	/**
	 * 
	 * @param member
	 */
	private void headerSignature(final ExecutableMemberDoc member) {
		final String signature = new StringBuffer()
			.append(member.name())
			.append(member.flatSignature())
			.toString();
		header(4);
		text(signature);
	}

	/**
	 * 
	 * @param member
	 */
	public void rowSignature(final ProgramElementDoc element) {
		startTableRow();
		returnSignature(element);
		cell();
		linkedName(element);
		if (element instanceof ExecutableMemberDoc) {
			final ExecutableMemberDoc member = (ExecutableMemberDoc) element;
			inlineParameters(member.parameters());
		}
		endTableRow();
		newLine();
	}

	/**
	 * Builds and returns signature for the given
	 * ``member`` as a markdown list item.
	 * 
	 * @param element Member to build list item from.
	 * @return Built list item.
	 */
	public void itemSignature(final ProgramElementDoc element) {
		item();
		returnSignature(element);
		text(" ");
		linkedName(element);
		if (element instanceof ExecutableMemberDoc) {
			final ExecutableMemberDoc member = (ExecutableMemberDoc) element;
			inlineParameters(member.parameters());
		}
		newLine();
	}

	/**
	 * 
	 * @param fieldDoc
	 */
	public void field(final FieldDoc fieldDoc) {
		header(4);
		text(fieldDoc.name());
		newLine();
		quote();
		bold(fieldDoc.modifiers());
		text(" ");
		typeLink(source, fieldDoc.type());
		newLine();
		newLine();
		quote();
		description(fieldDoc);
		newLine();
		newLine();
		horizontalRule();
		newLine();
	}

	/**
	 * Appends the method documentation body. Using the
	 * following format :
	 * 
	 * * method signature (as header)
	 * * method description (as text)
	 * * method parameters (as list)
	 * * method return type (as single item list)
	 * * method exception (as list)
	 * 
	 * @param methodDoc Method documentation to append.
	 */
	public void member(final ExecutableMemberDoc member) {
		headerSignature(member);
		newLine();
		quote();
		if (!member.isIncluded()) {
			italic("Default inherited constructor");
		}
		description(member);
		newLine();
		newLine();
		parameters(member.paramTags());
		if (member.isMethod()) {
			final MethodDoc methodDoc = (MethodDoc) member;
			returnType(methodDoc.tags("return")); // TODO : Exports as static constant.
		}
		exceptions(member.throwsTags());
		newLine();
		horizontalRule();
		newLine();
	}

	/**
	 * Appends parameters defined by the
	 * given list, as a markdown list of
	 * the following format :
	 * 
	 * * ``Type : Description``
	 * 
	 * @param parameters
	 */
	private void parameters(final ParamTag[] parameters) {
		if (parameters.length > 0) {
			quote();
			bold(MarkletConstant.PARAMETERS);
			newLine();
			for (final ParamTag parameter : parameters) {
				item();
				// TODO : Think about including parameter Type here.
				text(parameter.parameterName());
				text(" : ");
				text(parameter.parameterComment());
				newLine();
			}
			newLine();
		}
	}
	
	/**
	 * Appends the description of the return type
	 * defined by the given ``tag``, as a markdown
	 * item of the following format :
	 * 
	 * * ``Type : Description``
	 * 
	 * @param tag Return type tag to use.
	 */
	private void returnType(final Tag[] tag) {
		if (tag.length > 0) {
			quote();
			bold(MarkletConstant.RETURNS);
			newLine();
			item();
			text(tag[0].text());  // TODO : Link processing ?
			newLine();
			newLine();
		}
	}

	/**
	 * Appends the given ``exception`` to the current
	 * document, as a markdown item of the following format :
	 * 
	 * * ``Type : Description``
	 */
	private void exceptions(final ThrowsTag[] exceptions) {
		if (exceptions.length > 0) {
			quote();
			bold(MarkletConstant.THROWS);
			newLine();
			for (final ThrowsTag exception : exceptions) {
				item();
				classLink(source, exception.exception());
				text(" ");
				text(exception.exceptionComment());
				newLine();
			}
			newLine();
		}
	}

	/**
	 * Finalizes document building by adding a
	 * horizontal rule, the **marklet** generation
	 * badge, and closing the internal writer.
	 * 
	 * @throws IOException If any error occurs while closing document.
	 */
	public void build(final Path path) throws IOException {
		horizontalRule();
		newLine();
		text(MarkletConstant.BADGE);
		final String content = super.build();
		final InputStream stream = new ByteArrayInputStream(content.getBytes());
		Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING);
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
	public static String getPath(final String source, final String target) {
		if (source.equals(target)) {
			return "";
		}
		final StringBuffer pathBuilder = new StringBuffer();
		final String common = StringUtils.getCommonPrefix(source, target);
		final int start = common.length();
		if (!common.equals(source)) {
			final String back = source.substring(start);
			for (int i = 0; i < StringUtils.countMatches(back, '.'); i++) {
				pathBuilder.append("../");
			}
		}
		final String forward = (start >= 0 ? target.substring(start + 1) : target); // TODO : Checkout index evolution (+1, 0).
		pathBuilder.append(forward.replace('.', '/'));
		pathBuilder.append('/');
		return pathBuilder.toString();
	}

}
