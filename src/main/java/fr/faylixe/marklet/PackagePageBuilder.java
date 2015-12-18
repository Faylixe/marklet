package fr.faylixe.marklet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import com.sun.javadoc.PackageDoc;

/**
 * 
 * @author fv
 */
public final class PackagePageBuilder {

	/**
	 * 
	 * @param target
	 * @param packageDoc
	 * @throws IOException 
	 */
	public static void build(final Path target, final PackageDoc packageDoc) throws IOException {
		final Path readmePath = target.resolve("README.md");
		final StringBuilder overviewBuilder = new StringBuilder();
		overviewBuilder.append("# Package " + packageDoc.name());
		overviewBuilder.append("\n\n");
		overviewBuilder.append(packageDoc.commentText());
		final String overview = overviewBuilder.toString();
		Files.copy(new ByteArrayInputStream(overview.getBytes()), readmePath, StandardCopyOption.REPLACE_EXISTING);
	}

}
