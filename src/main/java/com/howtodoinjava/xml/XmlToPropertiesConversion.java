package com.howtodoinjava.xml;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 * Examples for the tutorial "Convert XML to Properties and Properties to XML in Java".
 * https://howtodoinjava.com/java/xml/convert-xml-to-properties/
 */
public class XmlToPropertiesConversion {
    static Properties flatten(Path file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        Document doc = factory.newDocumentBuilder().parse(file.toFile());
        Properties props = new Properties();
        addEntries(doc.getDocumentElement(), "", props);
        return props;
    }
    static void addEntries(Element element, String key, Properties props) {
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            props.setProperty(key + "." + attribute.getNodeName(), attribute.getNodeValue());
        }
        boolean hasChildElements = false;
        for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof Element childElement) {
                hasChildElements = true;
                String childKey = key.isEmpty() ? childElement.getTagName() : key + "." + childElement.getTagName();
                addEntries(childElement, childKey, props);
            }
        }
        if (!hasChildElements && attributes.getLength() == 0) {
            props.setProperty(key, element.getTextContent().strip());
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Properties props = new Properties();
            try (InputStream in = Files.newInputStream(Path.of("app-config.xml"))) {
                props.loadFromXML(in);                                             // XML -> Properties
            }
            try (Writer out = Files.newBufferedWriter(Path.of("app.properties"), StandardCharsets.UTF_8)) {
                props.store(out, "Converted from app-config.xml");                 // Properties -> .properties
            }
            String port = props.getProperty("server.port");                        // "8080"
            show("port", port);

            Properties back = new Properties();
            try (Reader in = Files.newBufferedReader(Path.of("app.properties"), StandardCharsets.UTF_8)) {
                back.load(in);                                                     // .properties -> Properties
            }
            try (OutputStream out = Files.newOutputStream(Path.of("app-copy.xml"))) {
                back.storeToXML(out, "Converted from app.properties", StandardCharsets.UTF_8);   // Properties -> XML
            }
            boolean sameEntries = back.equals(props);                              // true
            show("sameEntries", sameEntries);
        }
        {
            Properties props = new Properties();
            try (InputStream in = Files.newInputStream(Path.of("app-config.xml"))) {
                props.loadFromXML(in);
            }
            try (OutputStream out = Files.newOutputStream(Path.of("app.properties"))) {
                props.store(out, "Converted from app-config.xml");
            }
            List<String> lines = Files.readAllLines(Path.of("app.properties"), StandardCharsets.ISO_8859_1);
            String greeting = lines.get(2);               // "app.greeting=Hi, Zo\u00EB"
            show("greeting", greeting);
            String uploadDir = lines.get(5);              // "upload.dir=C\:/notes/uploads"
            show("uploadDir", uploadDir);
        }
        {
            Properties props = new Properties();
            try (Reader in = Files.newBufferedReader(Path.of("app.properties"), StandardCharsets.ISO_8859_1)) {
                props.load(in);
            }
            try (OutputStream out = Files.newOutputStream(Path.of("app-copy.xml"))) {
                props.storeToXML(out, "Converted from app.properties", StandardCharsets.US_ASCII);
            }
            String dir = props.getProperty("upload.dir");                // "C:/notes/uploads"
            show("dir", dir);
            String xml = Files.readString(Path.of("app-copy.xml"));
            boolean hasDoctype = xml.contains("properties.dtd");         // true
            show("hasDoctype", hasDoctype);
        }
        {
            Properties props = flatten(Path.of("legacy-config.xml"));
            Set<String> keys = new TreeSet<>(props.stringPropertyNames());   // [db.url, db.user, server.port]
            show("keys", keys);
            String url = props.getProperty("db.url");                         // "jdbc:h2:mem:notes"
            show("url", url);
            String port = props.getProperty("server.port");                   // "8080"
            show("port", port);
        }
        {
            String noDoctype = "<properties><entry key=\"a\">1</entry></properties>";
            Properties props = new Properties();
            try { props.loadFromXML(new ByteArrayInputStream(noDoctype.getBytes(StandardCharsets.UTF_8)));  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            Properties props = new Properties();
            props.put("retries", 3);                                          // compiles, the value is an Integer
            String retries = props.getProperty("retries");                    // null, getProperty() ignores non-String values
            show("retries", retries);
            try { props.store(new StringWriter(), null);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
    }

    static void show(String name, Object value) {
        String text = value instanceof int[] a ? Arrays.toString(a)
        : value instanceof long[] a ? Arrays.toString(a)
        : value instanceof double[] a ? Arrays.toString(a)
        : value instanceof Object[] a ? Arrays.deepToString(a)
        : value instanceof String str ? "\"" + str + "\""
        : String.valueOf(value);
        System.out.println(name + " = " + text);
    }
}
