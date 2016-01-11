# Marklet

[![Build Status](https://travis-ci.org/Faylixe/marklet.svg)](https://travis-ci.org/Faylixe/marklet)

**Marklet** is a custom Java Doclet which aims to generate a Javadoc in a markdown format which is ready to use in GitHub. You can check a **Marklet** generated javadoc on the following project :

* [Marklet](https://github.com/Faylixe/market/tree/master/javadoc/fr/faylixe/marklet)
* [Google Code Jam API](https://github.com/Faylixe/googlecodejam-client/tree/master/javadoc/fr/faylixe/googlecodejam/client)

In order to use it with Maven, adds the following configuration for the ``maven-javadoc-plugin``
in your project ``POM`` :

```xml
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-javadoc-plugin</artifactId>
	<version>2.9</version>
	<configuration>
		<doclet>fr.faylixe.marklet.Marklet</doclet>
		<docletArtifact>
			<groupId>fr.faylixe</groupId>
			<artifactId>marklet</artifactId>
			<version>1.0.0</version>
		</docletArtifact>
		<useStandardDocletOptions>false</useStandardDocletOptions>
	</configuration>
</plugin>
```