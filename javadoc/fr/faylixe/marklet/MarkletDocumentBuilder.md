#Class MarkletDocumentBuilder
Package [fr.faylixe.marklet](README.md)<br>

> *java.lang.Object* > [MarkdownDocumentBuilder](MarkdownDocumentBuilder.md) > [MarkletDocumentBuilder](MarkletDocumentBuilder.md)



Custom [MarkdownDocumentBuilder](MarkdownDocumentBuilder.md) implementation
 that aims to be used for building Marklet generated
 document. Such document are defined by a source package
 from which link are built.


##Summary
####Constructors
| Visibility | Signature |
| --- | --- |
| **public** | [MarkletDocumentBuilder](#markletdocumentbuilderpackagedoc)(*com.sun.javadoc.PackageDoc* source) |

####Methods
| Type and modifiers | Method signature |
| --- | --- |
| **public** **void** | [build](#buildpath)(*java.nio.file.Path* path) |
| **public** **void** | [classLink](#classlinkpackagedoc-classdoc)(*com.sun.javadoc.PackageDoc* source, *com.sun.javadoc.ClassDoc* target) |
| **public** **void** | [description](#descriptiondoc)(*com.sun.javadoc.Doc* doc) |
| **public** **void** | [description](#descriptiontag)(*com.sun.javadoc.Tag* inlineTags) |
| **public** **void** | [field](#fieldfielddoc)(*com.sun.javadoc.FieldDoc* fieldDoc) |
| **public static** *java.lang.String* | [getPath](#getpathstring-string)(*java.lang.String* source, *java.lang.String* target) |
| **public final** *com.sun.javadoc.PackageDoc* | [getSource](#getsource)() |
| **public** **void** | [itemSignature](#itemsignatureprogramelementdoc)(*com.sun.javadoc.ProgramElementDoc* element) |
| **public** **void** | [linkedName](#linkednameprogramelementdoc)(*com.sun.javadoc.ProgramElementDoc* element) |
| **public** **void** | [member](#memberexecutablememberdoc)(*com.sun.javadoc.ExecutableMemberDoc* member) |
| **public** **void** | [methodLink](#methodlinkpackagedoc-methoddoc)(*com.sun.javadoc.PackageDoc* source, *com.sun.javadoc.MethodDoc* method) |
| **public** **void** | [returnSignature](#returnsignatureprogramelementdoc)(*com.sun.javadoc.ProgramElementDoc* element) |
| **public** **void** | [rowSignature](#rowsignatureprogramelementdoc)(*com.sun.javadoc.ProgramElementDoc* element) |
| **public** **void** | [typeLink](#typelinkpackagedoc-type)(*com.sun.javadoc.PackageDoc* source, *com.sun.javadoc.Type* type) |

---


##Constructors
####MarkletDocumentBuilder(PackageDoc)
> Default constructor.

> **Parameters**
* source : Target source package from which document will be written.


---


##Methods
####build(Path)
> Finalizes document building by adding a
 horizontal rule, the **marklet** generation
 badge, and closing the internal writer.

> **Parameters**
* path : Path of the document to write.

> **Throws**
* *java.io.IOException* If any error occurs while closing document.


---

####classLink(PackageDoc, ClassDoc)
> Appends to the current document a valid markdown link
 that aims to be the shortest one, by using the
 [MarkletDocumentBuilder](MarkletDocumentBuilder.md) method. The
 built URL will start from the given ``source``
 package to the given ``target`` class.

> **Parameters**
* source : Source package to start URL from.
* target : Target class to reach from this package.


---

####description(Doc)
> This methods will process the given ``doc``
 comment text, by replacing each link tags
 by effective markdown link.

> **Parameters**
* doc : Documentation element to process description from.


---

####description(Tag[])
> This methods will process the given ``inlineTags``
 comment text, by replacing each link tags
 by effective markdown link.

> **Parameters**
* inlineTags : Inline tags to generate description from.


---

####field(FieldDoc)
> Appends to the current document the detail
 about the given ``fieldDoc``. Using the
 following format :
 
 * Field name (as header)
 * Field signature (as quoted text)
 * Field description (as quoted text)

> **Parameters**
* fieldDoc : Field documentation to append.


---

####getPath(String, String)
> Static method that builds a shortest URL path, from
 the given ``source`` package to the ``target`` package.
 Such path is built by taking the longest common prefix
 from both package name, trying to move from source to this
 prefix using ``../`` path, then moving to the target path
 vertically.

> **Parameters**
* source : Source package to build path from.
* target : Target package to build path to.

> **Returns**
* Built path.


---

####getSource()
> Source getter.

> **Returns**
* Target source package from which document will be written.


---

####itemSignature(ProgramElementDoc)
> Appends to the current document the signature
 of the given ``member`` as a list item.

> **Parameters**
* element : Member to write signature from.


---

####linkedName(ProgramElementDoc)
> Appends to the current document a link that is
 built from the given ``element``. Such links is
 usually leading to the internal corresponding
 document section

> **Parameters**
* element : Element to build link from.


---

####member(ExecutableMemberDoc)
> Appends the method documentation body. Using the
 following format :
 
 * method signature (as header)
 * method description (as text)
 * method parameters (as list)
 * method return type (as single item list)
 * method exception (as list)

> **Parameters**
* member : Method documentation to append.


---

####methodLink(PackageDoc, MethodDoc)
> To document.

> **Parameters**
* source : 
* method : 


---

####returnSignature(ProgramElementDoc)
> Appends to the current document the ``member``
 returns label, which is composed of the given
 ``member`` modifiers if any, followed by the
 return type link, if the given ``member`` is
 a method,

> **Parameters**
* element : Member to build return label for.


---

####rowSignature(ProgramElementDoc)
> Appends to the current document the signature
 of the given ``member`` as a table row.

> **Parameters**
* element : Member to write signature from.


---

####typeLink(PackageDoc, Type)
> Appends to the current document a valid markdown
 link for the given ``type``. If this ``type``
 is a primitive one, then only a bold label
 is produced. Otherwise it return a link
 created by the [MarkletDocumentBuilder](MarkletDocumentBuilder.md)
 method.

> **Parameters**
* source : Source package to start URL from.
* type : Target type to reach from this package.


---

---

[![Marklet](https://img.shields.io/badge/Generated%20by-Marklet-green.svg)](https://github.com/Faylixe/marklet)