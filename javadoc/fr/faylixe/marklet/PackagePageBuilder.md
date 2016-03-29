#Class PackagePageBuilder
Package [fr.faylixe.marklet](README.md)<br>

> *java.lang.Object* > [MarkdownDocumentBuilder](MarkdownDocumentBuilder.md) > [MarkletDocumentBuilder](MarkletDocumentBuilder.md) > [PackagePageBuilder](PackagePageBuilder.md)



Builder that aims to create documentation
 page for a given ``package``. Such documentation
 consists in a package description followed by
 type listing over following categories :
 
 * Classes
 * Interfaces
 * Enumerations
 * Annotations


##Summary
####Methods
| Type and modifiers | Method signature |
| --- | --- |
| **public static** **void** | [build](#buildpackagedoc-path)(*com.sun.javadoc.PackageDoc* packageDoc, *java.nio.file.Path* directoryPath) |

---


##Methods
####build(PackageDoc, Path)
> Builds and writes the documentation file associated
 to the given ``packageDoc`` into the directory denoted
 by the given ``directoryPath``.

> **Parameters**
* packageDoc : Package to generated documentation for.
* directoryPath : Path of the directory to write documentation in.

> **Throws**
* *java.io.IOException* If any error occurs while writing package page.


---

---

[![Marklet](https://img.shields.io/badge/Generated%20by-Marklet-green.svg)](https://github.com/Faylixe/marklet)