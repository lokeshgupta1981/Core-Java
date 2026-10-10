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
 * Examples for the tutorial "Java Convert XML Document to String and Write XML File".
 * https://howtodoinjava.com/java/xml/xml-to-string-write-xml-file/
 */
public class XmlDocumentToStringTransformer {
    static String toXmlString(Node node, Map<String, String> outputProperties) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        Transformer transformer = factory.newTransformer();
        outputProperties.forEach(transformer::setOutputProperty);

        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(node), new StreamResult(writer));
        return writer.toString();
    }
    static Document parseXml(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        return factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
    }
    static void stripWhitespaceNodes(Document doc) throws XPathExpressionException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList blanks = (NodeList) xpath.evaluate("//text()[normalize-space()='']", doc, XPathConstants.NODESET);
        for (int i = 0; i < blanks.getLength(); i++) {
            Node blank = blanks.item(i);
            blank.getParentNode().removeChild(blank);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(new InputSource(new StringReader("<tasks><task id=\"1\">Buy milk</task></tasks>")));

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String xml = writer.toString();         // "<tasks><task id="1">Buy milk</task></tasks>"
            show("xml", xml);
        }
        {
            Document doc = parseXml("<tasks><task id=\"1\">Buy milk</task><task id=\"2\">Call mom</task></tasks>");

            String full = toXmlString(doc, Map.of());
            boolean declaration = full.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><tasks>");   // true
            show("declaration", declaration);

            Element second = (Element) doc.getElementsByTagName("task").item(1);
            String one = toXmlString(second, Map.of(OutputKeys.OMIT_XML_DECLARATION, "yes"));   // "<task id="2">Call mom</task>"
            show("one", one);
        }
        {
            Document doc = parseXml("<tasks><task id=\"1\">Buy milk</task></tasks>");
            doc.setXmlStandalone(true);

            String xml = toXmlString(doc, Map.of());     // "<?xml version="1.0" encoding="UTF-8"?><tasks><task id="1">Buy milk</task></tasks>"
            show("xml", xml);
        }
        {
            Document doc = parseXml("<tasks>\n  <task id=\"1\">Buy milk</task>\n  <task id=\"2\">Call mom</task>\n</tasks>");
            Map<String, String> pretty = Map.of(OutputKeys.INDENT, "yes",
            "{http://xml.apache.org/xslt}indent-amount", "2");

            String messy = toXmlString(doc, pretty);
            long blankLines = messy.lines().filter(String::isBlank).count();     // 3
            show("blankLines", blankLines);
        }
        {
            Document doc = parseXml("<tasks>\n  <task id=\"1\">Buy milk</task>\n  <task id=\"2\">Call mom</task>\n</tasks>");
            Map<String, String> pretty = Map.of(OutputKeys.INDENT, "yes",
            "{http://xml.apache.org/xslt}indent-amount", "2");

            stripWhitespaceNodes(doc);
            String clean = toXmlString(doc, pretty);
            long blankLines = clean.lines().filter(String::isBlank).count();     // 0
            show("blankLines", blankLines);
            long lineCount = clean.lines().count();                              // 5
            show("lineCount", lineCount);
        }
        {
            Document doc = parseXml("<tasks><task id=\"1\">Buy milk</task><task id=\"2\">Call mom</task></tasks>");
            Path file = Files.createTempFile("tasks", ".xml");

            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            try (OutputStream out = Files.newOutputStream(file)) {
                transformer.transform(new DOMSource(doc), new StreamResult(out));
            }
            String firstLine = Files.readAllLines(file).get(0);    // "<?xml version="1.0" encoding="UTF-8" standalone="no"?>"
            show("firstLine", firstLine);
            int lineCount = Files.readAllLines(file).size();         // 5
            show("lineCount", lineCount);
        }
        {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element tasks = doc.createElement("tasks");
            doc.appendChild(tasks);

            Element task = doc.createElement("task");
            task.setAttribute("id", "3");
            task.setTextContent("Milk & <eggs>");
            tasks.appendChild(task);

            String xml = toXmlString(doc, Map.of(OutputKeys.OMIT_XML_DECLARATION, "yes"));   // "<tasks><task id="3">Milk &amp; &lt;eggs&gt;</task></tasks>"
            show("xml", xml);
        }
        {
            Document doc = parseXml("<tasks><task id=\"1\">Buy milk</task></tasks>");
            DOMImplementationLS ls = (DOMImplementationLS) doc.getImplementation();
            LSSerializer serializer = ls.createLSSerializer();

            String xml = serializer.writeToString(doc);     // "<?xml version="1.0" encoding="UTF-16"?><tasks><task id="1">Buy milk</task></tasks>"
            show("xml", xml);
            serializer.getDomConfig().setParameter("xml-declaration", false);
            String noDecl = serializer.writeToString(doc);  // "<tasks><task id="1">Buy milk</task></tasks>"
            show("noDecl", noDecl);
        }
        {
            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            String identity = """
            <xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
            <xsl:output method="xml" omit-xml-declaration="yes"/>
            <xsl:template match="@*|node()"><xsl:copy><xsl:apply-templates select="@*|node()"/></xsl:copy></xsl:template>
            </xsl:stylesheet>""";
            Templates templates = factory.newTemplates(new StreamSource(new StringReader(identity)));

            Document doc = parseXml("<tasks><task id=\"1\">Buy milk</task></tasks>");
            StringWriter writer = new StringWriter();
            templates.newTransformer().transform(new DOMSource(doc), new StreamResult(writer));
            String xml = writer.toString();     // "<tasks><task id="1">Buy milk</task></tasks>"
            show("xml", xml);
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
