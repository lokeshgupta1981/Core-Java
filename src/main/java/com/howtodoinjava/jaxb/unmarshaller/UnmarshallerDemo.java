package com.howtodoinjava.jaxb.unmarshaller;

import com.howtodoinjava.jaxb.unmarshaller.ns.MusicSong;
import jakarta.xml.bind.JAXB;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.UnmarshalException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.ValidationEvent;
import jakarta.xml.bind.ValidationEventLocator;
import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Document;

/**
 * Prints every result shown in the article "JAXB Unmarshaller Example": reading XML from
 * different sources, JAXBElement, callbacks, default values, schema validation,
 * namespaces and the common errors.
 */
public class UnmarshallerDemo {

  static final String SONG_XML = """
      <song id="7">
        <title>Imagine</title>
        <artist>John Lennon</artist>
        <seconds>183</seconds>
      </song>""";

  public static void main(String[] args) throws Exception {
    URL songUrl = UnmarshallerDemo.class.getResource("/jaxb/unmarshaller/song.xml");
    File songFile = new File(songUrl.toURI());

    // 1. Quick answer: unmarshal a File
    JAXBContext context = JAXBContext.newInstance(Song.class);
    Unmarshaller unmarshaller = context.createUnmarshaller();
    Song song = (Song) unmarshaller.unmarshal(songFile);
    print("File", song);

    // 2. Other input sources
    print("String", unmarshaller.unmarshal(new StringReader(SONG_XML)));
    try (InputStream in = Files.newInputStream(songFile.toPath())) {
      print("InputStream", unmarshaller.unmarshal(in));
    }
    print("URL", unmarshaller.unmarshal(songUrl));
    print("Path", unmarshaller.unmarshal(Path.of(songUrl.toURI()).toFile()));

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    Document document = dbf.newDocumentBuilder().parse(songFile);
    print("DOM Node", unmarshaller.unmarshal(document));

    XMLInputFactory xif = XMLInputFactory.newFactory();
    xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
    XMLStreamReader xsr = xif.createXMLStreamReader(new StringReader(SONG_XML));
    print("XMLStreamReader", unmarshaller.unmarshal(xsr));

    print("JAXB.unmarshal", JAXB.unmarshal(new StringReader(SONG_XML), Song.class));

    // 3. Callbacks
    Song.printCallbacks = true;
    unmarshaller.unmarshal(new StringReader(SONG_XML));
    Song.printCallbacks = false;

    // 4. Missing, empty and unknown elements
    print("missing artist", unmarshaller.unmarshal(new StringReader(
        "<song id=\"8\"><title>Yesterday</title><seconds>125</seconds></song>")));
    print("empty artist", unmarshaller.unmarshal(new StringReader(
        "<song id=\"8\"><title>Yesterday</title><artist/><seconds>125</seconds></song>")));
    print("unknown element", unmarshaller.unmarshal(new StringReader(
        "<song id=\"8\"><title>Yesterday</title><genre>pop</genre><seconds>125</seconds></song>")));
    print("bad number", unmarshaller.unmarshal(new StringReader(
        "<song id=\"8\"><title>Yesterday</title><seconds>two</seconds></song>")));

    // 5. Root-less XML with unmarshal(source, Class) -> JAXBElement
    String trackXml = "<track><title>Hey Jude</title><seconds>431</seconds></track>";
    JAXBContext trackContext = JAXBContext.newInstance(Track.class);
    JAXBElement<Track> element = trackContext.createUnmarshaller()
        .unmarshal(new StreamSource(new StringReader(trackXml)), Track.class);
    print("JAXBElement name", element.getName());
    print("JAXBElement value", element.getValue());
    print("declaredType", element.getDeclaredType().getSimpleName());

    // 6. Missing @XmlRootElement
    printError("Track without root", () ->
        trackContext.createUnmarshaller().unmarshal(new StringReader(trackXml)));
    printError("track into Song context", () ->
        unmarshaller.unmarshal(new StringReader(trackXml)));

    // 7. Malformed XML
    printError("malformed", () -> unmarshaller.unmarshal(new StringReader(
        "<song id=\"9\"><title>Help</song>")));

    // 8. Schema validation
    SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Schema schema = sf.newSchema(UnmarshallerDemo.class.getResource("/jaxb/unmarshaller/song.xsd"));
    String invalidXml = """
        <song id="9">
          <artist>The Beatles</artist>
          <seconds>-5</seconds>
        </song>""";

    Unmarshaller validating = context.createUnmarshaller();
    validating.setSchema(schema);
    print("valid with schema", validating.unmarshal(new StringReader(SONG_XML)));
    try {
      validating.unmarshal(new StringReader(invalidXml));
    } catch (UnmarshalException e) {
      print("schema error getMessage", e.getMessage());
      print("schema error linked", e.getLinkedException());
      print("schema error cause", e.getCause());
    }

    List<String> problems = new ArrayList<>();
    validating.setEventHandler(event -> {
      ValidationEventLocator loc = event.getLocator();
      problems.add(severity(event) + " line " + loc.getLineNumber() + ", column "
          + loc.getColumnNumber() + ": " + event.getMessage());
      return true; // continue
    });
    Song partial = (Song) validating.unmarshal(new StringReader(invalidXml));
    problems.forEach(p -> print("event", p));
    print("result after events", partial);

    // 8b. Event handler without schema
    Unmarshaller strict = context.createUnmarshaller();
    strict.setEventHandler(event -> {
      print("no-schema event", severity(event) + ": " + event.getMessage());
      return false; // stop
    });
    printError("strict unknown element", () -> strict.unmarshal(new StringReader(
        "<song id=\"8\"><title>Yesterday</title><genre>pop</genre><seconds>125</seconds></song>")));
    printError("strict bad number", () -> strict.unmarshal(new StringReader(
        "<song id=\"8\"><title>Yesterday</title><seconds>two</seconds></song>")));

    // 9. Namespaces
    String nsXml = """
        <m:song xmlns:m="https://example.com/music">
          <m:title>Imagine</m:title>
          <m:seconds>183</m:seconds>
        </m:song>""";
    printError("namespace into Song", () -> unmarshaller.unmarshal(new StringReader(nsXml)));
    print("root-only namespace", JAXBContext.newInstance(RootOnlyNamespaceSong.class)
        .createUnmarshaller().unmarshal(new StringReader(nsXml)));
    print("package-info namespace", JAXBContext.newInstance(MusicSong.class)
        .createUnmarshaller().unmarshal(new StringReader(nsXml)));
    String defaultNsXml = """
        <song xmlns="https://example.com/music">
          <title>Imagine</title>
          <seconds>183</seconds>
        </song>""";
    print("default namespace", JAXBContext.newInstance(MusicSong.class)
        .createUnmarshaller().unmarshal(new StringReader(defaultNsXml)));

    // 10. External entities (XXE)
    Path secret = Files.createTempFile("secret", ".txt");
    Files.writeString(secret, "top-secret");
    String xxe = "<?xml version=\"1.0\"?><!DOCTYPE song [<!ENTITY x SYSTEM \""
        + secret.toUri() + "\">]><song id=\"1\"><title>&x;</title><seconds>1</seconds></song>";
    printError("XXE with StringReader", () -> unmarshaller.unmarshal(new StringReader(xxe)));
    printError("XXE with StAX, DTD off", () ->
        unmarshaller.unmarshal(xif.createXMLStreamReader(new StringReader(xxe))));
    Files.delete(secret);
  }

  static String severity(ValidationEvent event) {
    return switch (event.getSeverity()) {
      case ValidationEvent.WARNING -> "WARNING";
      case ValidationEvent.ERROR -> "ERROR";
      default -> "FATAL_ERROR";
    };
  }

  static void print(String label, Object value) {
    System.out.println(label + " -> " + value);
  }

  interface Action {
    Object run() throws Exception;
  }

  static void printError(String label, Action action) {
    try {
      print(label, action.run());
    } catch (Exception e) {
      print(label, e.getClass().getName() + ": " + e.getMessage());
      Throwable cause = e instanceof JAXBException je && je.getLinkedException() != null
          ? je.getLinkedException() : e.getCause();
      if (cause != null) {
        print(label + " (cause)", cause.getClass().getName() + ": " + cause.getMessage());
      }
    }
  }
}
