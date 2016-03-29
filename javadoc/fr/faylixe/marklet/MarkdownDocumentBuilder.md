#Class MarkdownDocumentBuilder
Package [fr.faylixe.marklet](README.md)<br>

> *java.lang.Object* > [MarkdownDocumentBuilder](MarkdownDocumentBuilder.md)



This class aims to build Markdown document.
 It is built in a top of a *java.lang.StringBuffer*
 instance which will contains our document
 content.


##Summary
####Fields
| Type and modifiers | Field name |
| --- | --- |
| **public static final** | [FILE_EXTENSION](#file_extension) |

####Constructors
| Visibility | Signature |
| --- | --- |
| **public** | [MarkdownDocumentBuilder](#markdowndocumentbuilder)() |

####Methods
| Type and modifiers | Method signature |
| --- | --- |
| **public final** **void** | [bold](#boldstring)(*java.lang.String* text) |
| **public final** **void** | [breakingReturn](#breakingreturn)() |
| **public final** *java.lang.String* | [build](#build)() |
| **public final** **void** | [cell](#cell)() |
| **public final** **void** | [character](#characterchar)(**char** character) |
| **public final** **void** | [endTableRow](#endtablerow)() |
| **public final** **void** | [header](#headerint)(**int** level) |
| **public final** **void** | [horizontalRule](#horizontalrule)() |
| **public final** **void** | [italic](#italicstring)(*java.lang.String* text) |
| **public final** **void** | [item](#item)() |
| **public final** **void** | [link](#linkstring-string)(*java.lang.String* label, *java.lang.String* url) |
| **public final** **void** | [newLine](#newline)() |
| **public final** **void** | [quote](#quote)() |
| **public final** **void** | [rawLink](#rawlinkstring-string)(*java.lang.String* label, *java.lang.String* url) |
| **public final** **void** | [startTableRow](#starttablerow)() |
| **public final** **void** | [tableHeader](#tableheaderstring)(*java.lang.String* headers) |
| **public final** **void** | [tableRow](#tablerowstring)(*java.lang.String* cells) |
| **public final** **void** | [text](#textstring)(*java.lang.String* text) |

---


##Constructors
####MarkdownDocumentBuilder()
> Default constructor.
 Initializes internal buffer.


---


##Fields
####FILE_EXTENSION
> **public static final** *java.lang.String*

> Extension used for markdown file.

---


##Methods
####bold(String)
> Appends the given ``text`` to the current
 document with a bold decoration.

> **Parameters**
* text : Text to append to the document with the bold decoration.


---

####breakingReturn()
> Appends a horizontal rule sequence
 to the current document.


---

####build()
> Builds and returns the document content.

> **Returns**
* Built document content.


---

####cell()
> Appends a table cell separator
 to the current document.


---

####character(char)
> Appends the given ``character`` to the current
 document.

> **Parameters**
* character : Character to append to the document.


---

####endTableRow()
> Appends a table row end separator
 to the current document.


---

####header(int)
> Starts a header text, in the current document
 using the given header ``level``

> **Parameters**
* level : Level of the header to start.


---

####horizontalRule()
> Appends a horizontal rule sequence 
 to the current document.


---

####italic(String)
> Appends the given ``text`` to the current
 document with an italic decoration.

> **Parameters**
* text : Text to append to the document with the italic decoration.


---

####item()
> Starts a list item in the current document.


---

####link(String, String)
> Appends a link to the current document
 using the given ``label`` and the given
 ``url``.

> **Parameters**
* label : Label of the built link.
* url : Target URL of the built link.


---

####newLine()
> Appends a new line to the current document.


---

####quote()
> Starts a quote in the current document.


---

####rawLink(String, String)
> Appends a raw HTML link to the current document
 using the given ``label`` and the given
 ``url``.

> **Parameters**
* label : Label of the built link.
* url : Target URL of the built link.


---

####startTableRow()
> Appends a table row start separator
 to the current document.


---

####tableHeader(String...)
> Appends the given ``headers`` in the current
 document as a table header row.

> **Parameters**
* headers : Headers to write.


---

####tableRow(String...)
> Appends the given ``cell`` in the current
 document as a table row.

> **Parameters**
* cells : Cell to write.


---

####text(String)
> Appends the given ``text`` to the current
 document.

> **Parameters**
* text : Text to append to the document.


---

---

[![Marklet](https://img.shields.io/badge/Generated%20by-Marklet-green.svg)](https://github.com/Faylixe/marklet)