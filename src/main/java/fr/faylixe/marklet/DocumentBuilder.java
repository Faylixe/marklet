package fr.faylixe.marklet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;

/**
 * 
 * @author fv
 */
public final class DocumentBuilder {

	/** **/
	private final StringBuilder contentBuilder;

	/**
	 * 
	 */
	public DocumentBuilder() {
		this.contentBuilder = new StringBuilder();
	}

	/**
	 * 
	 * @param text
	 */
	public void append(final String text) {
		contentBuilder.append(text);
	}

	/**
	 * 
	 * @param fieldDoc
	 */
	public void append(final FieldDoc fieldDoc) {
	}

	/**
	 * 
	 * @param methodDoc
	 */
	public void append(final MethodDoc methodDoc) {
		
	}

	/**
	 * 
	 * @param path
	 * @throws IOException 
	 */
	public void build(final Path path) throws IOException {
		final String content = contentBuilder.toString();
		final InputStream stream = new ByteArrayInputStream(content.getBytes());
		Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING);
	}

}
