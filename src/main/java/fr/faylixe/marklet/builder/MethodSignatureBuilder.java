package fr.faylixe.marklet.builder;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.Type;

import fr.faylixe.marklet.IGenerationContext;
import fr.faylixe.marklet.MarkdownUtils;

/**
 * 
 * @author fv
 *
 */
public final class MethodSignatureBuilder {

	/** **/
	private final MethodDoc method;

	/** **/
	private final StringBuilder signatureBuilder;

	/** **/
	private final IGenerationContext context;

	/**
	 * 
	 * @param context
	 * @param method
	 */
	public MethodSignatureBuilder(final IGenerationContext context, final MethodDoc method) {
		this.method = method;
		this.context = context;
		this.signatureBuilder = new StringBuilder();
	}
	
	/**
	 * 
	 */
	public String buildReturn() {
		final StringBuilder returnBuilder = new StringBuilder();
		final Type type = method.returnType();
		if (type.isPrimitive()) {
			returnBuilder.append(MarkdownUtils.bold(type.simpleTypeName()));
		}
		else {
			final ClassDoc classDoc = type.asClassDoc();
			final String url = context.getClassURL(classDoc.qualifiedName());
			final String link = MarkdownUtils.buildLink(classDoc.name(), url);
			returnBuilder.append(link);
		}
		return returnBuilder.toString();
	}
	
	
	public String buildName() {
		return method.name();
	}

	/**
	 * 
	 * @param parameter
	 */
	public void buildParameter(final Parameter parameter) {
		
	}
	
	/**
	 * 
	 */
	public String build() {
		final String modifiers = MarkdownUtils.bold(method.modifiers());
		signatureBuilder.append(modifiers);
		signatureBuilder.append(' ');
		buildReturn();
		signatureBuilder.append(' ');
		signatureBuilder
			.append(method.name())
			.append('(');
		for (final Parameter parameter : method.parameters()) {
			buildParameter(parameter);
		}
		signatureBuilder.append(')');
		return signatureBuilder.toString();
	}

}
