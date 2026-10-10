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
import javax.xml.xpath.*;
import org.xml.sax.*;
import javax.xml.*;
import javax.xml.namespace.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.util.regex.*;
import org.xml.sax.helpers.DefaultHandler;
import org.w3c.dom.ls.*;

/**
 * Examples for the tutorial "Java Convert String to XML Document (DOM, Secure Parsing)".
 * https://howtodoinjava.com/java/xml/parse-string-to-xml-dom/
 */
public class StringToXmlDocument {
    static Document toDocument(String xml) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new DefaultHandler());
        return builder.parse(new InputSource(new StringReader(xml)));
    }
    static record Day(LocalDate date, int high, int low) {}
    public static void main(String[] args) throws Exception {
        {
            String xml = """
            <forecast city="Pune">
            <day date="2026-10-11" high="31" low="22"/>
            <day date="2026-10-12" high="29" low="21"/>
            </forecast>""";

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xml)));

            String root = doc.getDocumentElement().getTagName();                  // "forecast"
            show("root", root);
            String city = doc.getDocumentElement().getAttribute("city");          // "Pune"
            show("city", city);
            int days = doc.getElementsByTagName("day").getLength();               // 2
            show("days", days);
        }
        {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            try { Document wrong = builder.parse("<forecast city=\"Pune\"/>"); show("wrong", wrong); } catch (Throwable _t) { System.out.println("wrong -> " + _t); }
        }
        {
            String withDoctype = "<!DOCTYPE forecast [<!ENTITY c \"Pune\">]><forecast city=\"&c;\"/>";
            try { Document blocked = toDocument(withDoctype); show("blocked", blocked); } catch (Throwable _t) { System.out.println("blocked -> " + _t); }
        }
        {
            Document doc = toDocument("<!-- generated --><forecast city=\"Pune\"/>");

            String firstChild = doc.getFirstChild().getNodeName();             // "#comment"
            show("firstChild", firstChild);
            String rootElement = doc.getDocumentElement().getNodeName();       // "forecast"
            show("rootElement", rootElement);
        }
        {
            Document doc = toDocument("""
            <forecast city="Pune">
            <day date="2026-10-11" high="31" low="22"/>
            <day date="2026-10-12" high="29" low="21"/>
            </forecast>""");

            List<Day> forecast = new ArrayList<>();
            NodeList dayNodes = doc.getElementsByTagName("day");
            for (int i = 0; i < dayNodes.getLength(); i++) {
                Element day = (Element) dayNodes.item(i);
                forecast.add(new Day(LocalDate.parse(day.getAttribute("date")),
                Integer.parseInt(day.getAttribute("high")),
                Integer.parseInt(day.getAttribute("low"))));
            }
            Day first = forecast.get(0);           // Day[date=2026-10-11, high=31, low=22]
            show("first", first);
        }
        {
            String broken = "<forecast><day></forecast>";

            String problem;
            try {
                toDocument(broken);
                problem = "none";
            } catch (SAXParseException e) {
                problem = e.getLineNumber() + ":" + e.getColumnNumber() + " " + e.getMessage();
            }
            String report = problem;   // "1:18 The element type "day" must be terminated by the matching end-tag "</day>"."
            show("report", report);
        }
        {
            String padded = "   <?xml version=\"1.0\"?><forecast city=\"Pune\"/>";

            try { Document fails = toDocument(padded); show("fails", fails); } catch (Throwable _t) { System.out.println("fails -> " + _t); }
            Document works = toDocument(padded.strip());
            String city = works.getDocumentElement().getAttribute("city");     // "Pune"
            show("city", city);
        }
        {
            byte[] latin1 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><city>Z\u00fcrich</city>"
                    .getBytes(StandardCharsets.ISO_8859_1);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            String fromBytes = builder.parse(new ByteArrayInputStream(latin1)).getDocumentElement().getTextContent();
            boolean correct = fromBytes.equals("Z\u00fcrich");                                  // true
            show("correct", correct);

            String decodedWrong = new String(latin1, StandardCharsets.UTF_8);
            String fromString = builder.parse(new InputSource(new StringReader(decodedWrong))).getDocumentElement().getTextContent();
            boolean broken = fromString.equals("Z\u00fcrich");                                  // false
            show("broken", broken);
        }
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            ThreadLocal<DocumentBuilder> builders = ThreadLocal.withInitial(() -> {
                try {
                    return factory.newDocumentBuilder();
                } catch (ParserConfigurationException e) {
                    throw new IllegalStateException(e);
                }
            });

            DocumentBuilder builder = builders.get();
            builder.reset();
            Document doc = builder.parse(new InputSource(new StringReader("<forecast city=\"Oslo\"/>")));
            String city = doc.getDocumentElement().getAttribute("city");    // "Oslo"
            show("city", city);
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
