# JAXB Unmarshaller Examples

Source code for the article [JAXB Unmarshaller Example](https://howtodoinjava.com/jaxb/jaxb-unmarshaller-example/).

Reads XML into Java objects with Jakarta XML Binding 4: File, String, InputStream, URL, DOM Node and
XMLStreamReader sources, `JAXBElement` for classes without `@XmlRootElement`, XSD validation with
`setSchema()` and a `ValidationEventHandler`, namespaces with `package-info.java`, default values and
unmarshal callbacks.

## Versions

- Java 21 (bytecode), tested on JDK 21 and JDK 25
- jakarta.xml.bind:jakarta.xml.bind-api 4.0.5
- org.glassfish.jaxb:jaxb-runtime 4.0.9

The XML and XSD files are in `src/main/resources/jaxb/unmarshaller/`.

## Run

```bash
mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.jaxb.unmarshaller.UnmarshallerDemo
```
