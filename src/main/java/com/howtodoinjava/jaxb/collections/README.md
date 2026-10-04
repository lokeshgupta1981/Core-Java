# JAXB List, Set and Map Examples

Source code for the article [JAXB: Marshal / Unmarshal a List or Set](https://howtodoinjava.com/jaxb/jaxb-exmaple-marshalling-and-unmarshalling-list-or-set-of-objects/).

Marshals and unmarshals collections with Jakarta XML Binding 4: a root class with a `List` field
(`@XmlElementWrapper` and `@XmlElement`), the errors for a bare `List`, a generic `Wrapper<T>` with
`JAXBElement`, the XML with and without `@XmlElementWrapper`, the order of a `Set`, a `Map` with and
without an `XmlAdapter`, and empty and null collections.

## Versions

- Java 21 (bytecode), tested on JDK 25
- jakarta.xml.bind:jakarta.xml.bind-api 4.0.5
- org.glassfish.jaxb:jaxb-runtime 4.0.9 (also runs with com.sun.xml.bind:jaxb-impl 4.0.2 from this repo's pom)

## Run

```bash
mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.jaxb.collections.CollectionsDemo
```
