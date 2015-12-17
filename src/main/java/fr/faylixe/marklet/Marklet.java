package fr.faylixe.marklet;

import com.sun.javadoc.*;

/**
 * 
 * @author fv
 */
public final class Marklet {

	/**
	 * 
	 * @param root
	 * @return
	 */
	public static boolean start(final RootDoc root) {
		final ClassDoc [] classes = root.classes();
		for (final ClassDoc classDoc : classes) {
			root.printNotice("Class parsed : " + classDoc.name());
		}
		return true;
	}

}
