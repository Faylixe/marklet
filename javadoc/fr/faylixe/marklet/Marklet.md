#Class Marklet
Package [fr.faylixe.marklet](README.md)<br>

> *java.lang.Object* > [Marklet](Marklet.md)



Marklet entry point. This class declares
 the [Marklet](Marklet.md) method required
 by the doclet API in order to be called by the
 javadoc tool.


##Summary
####Methods
| Type and modifiers | Method signature |
| --- | --- |
| **public static** *com.sun.javadoc.LanguageVersion* | [languageVersion](#languageversion)() |
| **public static** **int** | [optionLength](#optionlengthstring)(*java.lang.String* option) |
| **public static** **boolean** | [start](#startrootdoc)(*com.sun.javadoc.RootDoc* root) |
| **public static** **boolean** | [validOptions](#validoptionsstring-docerrorreporter)(*java.lang.String* options, *com.sun.javadoc.DocErrorReporter* reporter) |

---


##Methods
####languageVersion()
> 

> **Returns**
* 


---

####optionLength(String)
> 

> **Parameters**
* option : 

> **Returns**
* 


---

####start(RootDoc)
> **Doclet** entry point. Parses user provided options
 and starts a **Marklet** execution.

> **Parameters**
* root : Doclet API root.

> **Returns**
* ``true`` if the generation went well, ``false`` otherwise.


---

####validOptions(String[][], DocErrorReporter)
> 

> **Parameters**
* options : 
* reporter : 

> **Returns**
* 


---

---

[![Marklet](https://img.shields.io/badge/Generated%20by-Marklet-green.svg)](https://github.com/Faylixe/marklet)