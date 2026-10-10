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
 * Examples for the tutorial "Java XPath NamespaceContext: Namespace Resolution Example".
 * https://howtodoinjava.com/java/xml/xpath-namespace-resolution-example/
 */
public class XpathNamespacecontextResolution {
    static Document parseNamespaceAware(Path file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        return factory.newDocumentBuilder().parse(file.toFile());
    }
    static record MapNamespaceContext(Map<String, String> uris) implements NamespaceContext {

        MapNamespaceContext {
            uris = Map.copyOf(uris);
        }

        @Override
        public String getNamespaceURI(String prefix) {
            if (prefix == null) {
                throw new IllegalArgumentException("prefix is null");
            }
            return switch (prefix) {
                case XMLConstants.XML_NS_PREFIX -> XMLConstants.XML_NS_URI;
                case XMLConstants.XMLNS_ATTRIBUTE -> XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
                default -> uris.getOrDefault(prefix, XMLConstants.NULL_NS_URI);
            };
        }

        @Override
        public String getPrefix(String namespaceURI) {
            Iterator<String> prefixes = getPrefixes(namespaceURI);
            return prefixes.hasNext() ? prefixes.next() : null;
        }

        @Override
        public Iterator<String> getPrefixes(String namespaceURI) {
            if (namespaceURI == null) {
                throw new IllegalArgumentException("namespaceURI is null");
            }
            return uris.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(namespaceURI))
                    .map(Map.Entry::getKey)
                    .iterator();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            Document doc = factory.newDocumentBuilder().parse(new File("tracking.xml"));

            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(new MapNamespaceContext(Map.of("t", "https://example.com/tracking/v2")));
            String status = xpath.evaluate("//t:parcel[@id='2']/t:status", doc);     // "DELIVERED"
            show("status", status);
        }
        {
            Document doc = parseNamespaceAware(Path.of("tracking.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();

            Double noPrefix = xpath.evaluateExpression("count(//parcel)", doc, Double.class);        // 0.0
            show("noPrefix", noPrefix);
            Double unbound = xpath.evaluateExpression("count(//trk:parcel)", doc, Double.class);     // 0.0
            show("unbound", unbound);
        }
        {
            Document doc = parseNamespaceAware(Path.of("tracking.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(new MapNamespaceContext(Map.of(
            "s", "http://schemas.xmlsoap.org/soap/envelope/",
            "t", "https://example.com/tracking/v2")));

            Double parcels = xpath.evaluateExpression("count(/s:Envelope/s:Body/t:TrackResponse/t:parcel)", doc, Double.class);   // 2.0
            show("parcels", parcels);
            String first = xpath.evaluate("//t:parcel[1]/t:status", doc);       // "IN_TRANSIT"
            show("first", first);
            String unknown = xpath.getNamespaceContext().getNamespaceURI("x");  // ""
            show("unknown", unknown);
            try { Double typo = xpath.evaluateExpression("count(//tr:parcel)", doc, Double.class); show("typo", typo); } catch (Throwable _t) { System.out.println("typo -> " + _t); }
        }
        {
            Document doc = parseNamespaceAware(Path.of("sitemap.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(new MapNamespaceContext(Map.of("sm", "http://www.sitemaps.org/schemas/sitemap/0.9")));

            Double withoutPrefix = xpath.evaluateExpression("count(//url/loc)", doc, Double.class);   // 0.0
            show("withoutPrefix", withoutPrefix);
            List<String> urls = new ArrayList<>();
            for (Node loc : xpath.evaluateExpression("//sm:url/sm:loc", doc, XPathNodes.class)) {
                urls.add(loc.getTextContent());
            }
            List<String> found = urls;                 // [https://example.com/, https://example.com/blog]
            show("found", found);
        }
        {
            Document doc = parseNamespaceAware(Path.of("tracking.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(new MapNamespaceContext(Map.of("t", "https://example.com/tracking/v2")));

            String carrier = xpath.evaluate("//t:parcel[@id='1']/@t:carrier", doc);    // "DHL"
            show("carrier", carrier);
            String noNs = xpath.evaluate("//t:parcel[@id='1']/@carrier", doc);         // ""
            show("noNs", noNs);
            String wrongId = xpath.evaluate("//t:parcel/@t:id", doc);                  // ""
            show("wrongId", wrongId);
        }
        {
            Document doc = parseNamespaceAware(Path.of("tracking.xml"));
            Element response = (Element) doc.getElementsByTagNameNS("https://example.com/tracking/v2", "TrackResponse").item(0);

            String soapFromRoot = doc.lookupNamespaceURI("soap");          // "http://schemas.xmlsoap.org/soap/envelope/"
            show("soapFromRoot", soapFromRoot);
            String trkFromRoot = doc.lookupNamespaceURI("trk");            // null
            show("trkFromRoot", trkFromRoot);
            String trkFromResponse = response.lookupNamespaceURI("trk");   // "https://example.com/tracking/v2"
            show("trkFromResponse", trkFromResponse);
        }
        {
            Document doc = parseNamespaceAware(Path.of("tracking.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();

            Double anyNs = xpath.evaluateExpression("count(//*[local-name()='parcel'])", doc, Double.class);   // 2.0
            show("anyNs", anyNs);
            String status = xpath.evaluate("//*[local-name()='parcel'][@id='2']/*[local-name()='status']", doc);   // "DELIVERED"
            show("status", status);
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
