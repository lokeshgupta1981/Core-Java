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
import javax.xml.namespace.NamespaceContext;
import javax.xml.XMLConstants;
import org.xml.sax.InputSource;

/**
 * Examples for the tutorial "Java XPath Tutorial With Examples (javax.xml.xpath)".
 * https://howtodoinjava.com/java/xml/java-xpath-tutorial-example/
 */
public class XpathNodeTree {
    static Document load(Path file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);   // blocks XXE
        try (InputStream in = Files.newInputStream(file)) {
            return factory.newDocumentBuilder().parse(in);
        }
    }
    static List<String> texts(XPath xpath, String expression, Object context) throws XPathExpressionException {
        List<String> result = new ArrayList<>();
        for (Node node : xpath.evaluateExpression(expression, context, XPathNodes.class)) {
            result.add(node.getTextContent());
        }
        return result;
    }
    public static void main(String[] args) throws Exception {
        {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("inventory.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();

            String first = xpath.evaluate("/inventory/book[1]/title", doc);                        // "Snow Crash"
            show("first", first);
            String year = xpath.evaluate("//book[title='Zodiac']/@year", doc);                     // "1995"
            show("year", year);
            Integer books = xpath.evaluateExpression("count(//book)", doc, Integer.class);         // 3
            show("books", books);
            Boolean hasNiven = xpath.evaluateExpression("boolean(//book[author='Larry Niven'])", doc, Boolean.class);   // true
            show("hasNiven", hasNiven);
            NodeList cheap = (NodeList) xpath.evaluate("//book[price < 8]/title", doc, XPathConstants.NODESET);
            int cheapCount = cheap.getLength();                                                    // 2
            show("cheapCount", cheapCount);
        }
        {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("inventory.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();
            Double asNumber = (Double) xpath.evaluate("count(//book)", doc, XPathConstants.NUMBER);   // 3.0
            show("asNumber", asNumber);
            String missing = xpath.evaluate("//book/edition", doc);                                  // ""
            show("missing", missing);
            String firstAuthor = xpath.evaluate("//book[2]/author", doc);                            // "Larry Niven"
            show("firstAuthor", firstAuthor);
            Boolean anyBook = (Boolean) xpath.evaluate("//book", doc, XPathConstants.BOOLEAN);         // true
            show("anyBook", anyBook);
        }
        {
            Document doc = load(Path.of("inventory.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression titlesAfter2001 = xpath.compile("//book[@year > 2001]/title/text()");
            NodeList nodes = (NodeList) titlesAfter2001.evaluate(doc, XPathConstants.NODESET);
            int matches = nodes.getLength();                         // 1
            show("matches", matches);
            String title = nodes.item(0).getNodeValue();             // "Burning Tower"
            show("title", title);
        }
        {
            Document doc = load(Path.of("inventory.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();
            Map<String, Double> prices = new LinkedHashMap<>();
            for (Node book : xpath.evaluateExpression("//book", doc, XPathNodes.class)) {
                prices.put(xpath.evaluate("title", book), xpath.evaluateExpression("price", book, Double.class));
            }
            Map<String, Double> result = prices;                     // {Snow Crash=14.95, Burning Tower=5.99, Zodiac=7.5}
            show("result", result);
        }
        {
            Document doc = load(Path.of("inventory.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();

            List<String> after2001 = texts(xpath, "//book[@year > 2001]/title", doc);          // [Burning Tower]
            show("after2001", after2001);
            List<String> before2001 = texts(xpath, "//book[@year < 2001]/title", doc);         // [Snow Crash, Zodiac]
            show("before2001", before2001);
            List<String> cheap = texts(xpath, "//book[price < 8]/title", doc);                 // [Burning Tower, Zodiac]
            show("cheap", cheap);
            List<String> costly = texts(xpath, "//book[price > 8]/title", doc);                // [Snow Crash]
            show("costly", costly);
            List<String> firstBook = texts(xpath, "//book[1]/title", doc);                     // [Snow Crash]
            show("firstBook", firstBook);
            List<String> lastBook = texts(xpath, "//book[last()]/title", doc);                 // [Zodiac]
            show("lastBook", lastBook);
            List<String> authors = texts(xpath, "//book/author", doc);                         // [Neal Stephenson, Larry Niven, Jerry Pournelle, Neal Stephenson]
            show("authors", authors);
            Integer titleCount = xpath.evaluateExpression("count(//book/title)", doc, Integer.class);   // 3
            show("titleCount", titleCount);
            List<String> byNeal = texts(xpath, "//book[starts-with(author, 'Neal')]/title", doc);     // [Snow Crash, Zodiac]
            show("byNeal", byNeal);
            List<String> byNiven = texts(xpath, "//book[contains(author, 'Niven')]/title", doc);      // [Burning Tower]
            show("byNiven", byNiven);
            List<String> byStephenson = texts(xpath, "//book[author = 'Neal Stephenson']/title", doc);   // [Snow Crash, Zodiac]
            show("byStephenson", byStephenson);
            Integer stephensonCount = xpath.evaluateExpression("count(//book[author = 'Neal Stephenson'])", doc, Integer.class);   // 2
            show("stephensonCount", stephensonCount);
            String comment = xpath.evaluate("/inventory/comment()", doc);                      // "Prices in USD"
            show("comment", comment);
        }
        {
            Document doc = load(Path.of("inventory.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();
            List<String> firstOnly = texts(xpath, "//book[contains(author, 'Pournelle')]/title", doc);       // []
            show("firstOnly", firstOnly);
            List<String> anyAuthor = texts(xpath, "//book[author[contains(., 'Pournelle')]]/title", doc);   // [Burning Tower]
            show("anyAuthor", anyAuthor);
        }
        {
            String nsXml = """
            <inventory xmlns="http://example.com/inventory">
            <book><title>Snow Crash</title></book>
            </inventory>""";
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document nsDoc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(nsXml)));
            XPath xpath = XPathFactory.newInstance().newXPath();

            String withoutPrefix = xpath.evaluate("//book/title", nsDoc);                      // ""
            show("withoutPrefix", withoutPrefix);
            xpath.setNamespaceContext(new NamespaceContext() {
                public String getNamespaceURI(String prefix) {
                    return "inv".equals(prefix) ? "http://example.com/inventory" : XMLConstants.NULL_NS_URI;
                }
                public String getPrefix(String uri) { return null; }
                public Iterator<String> getPrefixes(String uri) { return Collections.emptyIterator(); }
            });
            String withPrefix = xpath.evaluate("//inv:book/inv:title", nsDoc);                 // "Snow Crash"
            show("withPrefix", withPrefix);
            String byLocalName = xpath.evaluate("//*[local-name()='title']", nsDoc);           // "Snow Crash"
            show("byLocalName", byLocalName);
        }
        {
            Document doc = load(Path.of("inventory.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();
            Map<String, Object> params = new HashMap<>();
            xpath.setXPathVariableResolver(name -> params.get(name.getLocalPart()));
            XPathExpression byAuthor = xpath.compile("//book[author = $author]/title");

            params.put("author", "Neal Stephenson");
            int found = byAuthor.evaluateExpression(doc, XPathNodes.class).size();            // 2
            show("found", found);
            params.put("author", "' or '1'='1");
            int injected = byAuthor.evaluateExpression(doc, XPathNodes.class).size();         // 0
            show("injected", injected);
        }
        {
            ThreadLocal<XPathExpression> firstPrice = ThreadLocal.withInitial(() -> {
                try {
                    return XPathFactory.newInstance().newXPath().compile("number(//book[1]/price)");
                } catch (XPathExpressionException e) {
                    throw new IllegalStateException(e);
                }
            });
            Document doc = load(Path.of("inventory.xml"));
            Double price = (Double) firstPrice.get().evaluate(doc, XPathConstants.NUMBER);   // 14.95
            show("price", price);
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
