#Class MarkletOptions
Package [fr.faylixe.marklet](README.md)<br>

> *java.lang.Object* > [MarkletOptions](MarkletOptions.md)



Class that reads and stores provided options
 for javadoc execution. Options that we care about are :
 
 * Output directory


##Summary
####Methods
| Type and modifiers | Method signature |
| --- | --- |
| **public** *java.lang.String* | [getOutputDirectory](#getoutputdirectory)() |
| **public static** **int** | [optionLength](#optionlengthstring)(*java.lang.String* option) |
| **public static** [MarkletOptions](MarkletOptions.md) | [parse](#parsestring)(*java.lang.String* rawOptions) |
| **public static** **boolean** | [validOptions](#validoptionsstring-docerrorreporter)(*java.lang.String* options, *com.sun.javadoc.DocErrorReporter* reporter) |

---


##Methods
####getOutputDirectory()
> Getter for the output directory option.

> **Returns**
* Output directory file are generated in.


---

####optionLength(String)
> TODO : Process other option.

> **Parameters**
* option : 

> **Returns**
* 


---

####parse(String[][])
> Static factory.

> **Parameters**
* rawOptions : Raw options array to parse.

> **Returns**
* Built options instance.


---

####validOptions(String[][], DocErrorReporter)
> TODO : Perform validation.

> **Parameters**
* options : 
* reporter : 

> **Returns**
* 


---

---

[![Marklet](https://img.shields.io/badge/Generated%20by-Marklet-green.svg)](https://github.com/Faylixe/marklet)