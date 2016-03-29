#Class ClassPageBuilder
Package [fr.faylixe.marklet](README.md)<br>

> *java.lang.Object* > [MarkdownDocumentBuilder](MarkdownDocumentBuilder.md) > [MarkletDocumentBuilder](MarkletDocumentBuilder.md) > [ClassPageBuilder](ClassPageBuilder.md)



Builder that aims to create documentation
 page for a given ``class``. As for a standard
 class javadoc generation, it will contains a
 class summary, followed by details about class
 field, constructor, and methods.


##Summary
####Methods
| Type and modifiers | Method signature |
| --- | --- |
| **public static** **void** | [build](#buildclassdoc-path)(*com.sun.javadoc.ClassDoc* classDoc, *java.nio.file.Path* directoryPath) |
| **public** **void** | [inheritedMethodSummary](#inheritedmethodsummary)() |

---


##Methods
####build(ClassDoc, Path)
> Builds and writes the documentation file
 associated to the given ``classDoc`` into
 the directory denoted by the given ``directoryPath``.

> **Parameters**
* classDoc : Class to generated documentation for.
* directoryPath : Path of the directory to write documentation in.

> **Throws**
* *java.io.IOException* If any error occurs while writing documentation.


---

####inheritedMethodSummary()
> 


---

---

[![Marklet](https://img.shields.io/badge/Generated%20by-Marklet-green.svg)](https://github.com/Faylixe/marklet)