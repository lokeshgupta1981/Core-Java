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
 * Examples for the tutorial "Java XPath Find Nodes by Attribute Value (With Examples)".
 * https://howtodoinjava.com/java/xml/xpath-attribute-evaluate/
 */
public class XpathFindNodesByAttributeValue {
    static Document parseSecurely(Path file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        return factory.newDocumentBuilder().parse(file.toFile());
    }
    static List<String> roomNumbers(XPath xpath, Document doc, String expression) throws XPathExpressionException {
        List<String> numbers = new ArrayList<>();
        for (Node node : xpath.evaluateExpression(expression, doc, XPathNodes.class)) {
            numbers.add(((Element) node).getAttribute("number"));
        }
        return numbers;
    }
    public static void main(String[] args) throws Exception {
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            Document doc = factory.newDocumentBuilder().parse(new File("rooms.xml"));

            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathNodes rooms = xpath.evaluateExpression("//room[@type='double' and @status='free']", doc, XPathNodes.class);
            int found = rooms.size();                                         // 1
            show("found", found);
            String number = ((Element) rooms.get(0)).getAttribute("number");  // "205"
            show("number", number);
        }
        {
            Document doc = parseSecurely(Path.of("rooms.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();

            List<String> free = roomNumbers(xpath, doc, "//room[@status='free']");        // [101, 205, 310]
            show("free", free);
            List<String> notFree = roomNumbers(xpath, doc, "//room[@status!='free']");    // [102, 311]
            show("notFree", notFree);
            List<String> none = roomNumbers(xpath, doc, "//room[@status='closed']");      // []
            show("none", none);
        }
        {
            Document doc = parseSecurely(Path.of("rooms.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();

            Node room = xpath.evaluateExpression("//room[@number='205']", doc, Node.class);
            String view = ((Element) room).getAttribute("view");                                 // "lake"
            show("view", view);
            Node unknown = xpath.evaluateExpression("//room[@number='999']", doc, Node.class);   // null
            show("unknown", unknown);
        }
        {
            Document doc = parseSecurely(Path.of("rooms.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();

            List<String> cheap = roomNumbers(xpath, doc, "//room[@price < 130]");          // [101, 102]
            show("cheap", cheap);
            List<String> upper = roomNumbers(xpath, doc, "//room[@floor >= 2]");           // [205, 310, 311]
            show("upper", upper);
            List<String> asNumber = roomNumbers(xpath, doc, "//room[@price = 260]");       // [310]
            show("asNumber", asNumber);
            List<String> asString = roomNumbers(xpath, doc, "//room[@price = '260']");     // []
            show("asString", asString);
        }
        {
            Document doc = parseSecurely(Path.of("rooms.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();

            List<String> thirdFloor = roomNumbers(xpath, doc, "//room[starts-with(@number, '3')]");   // [310, 311]
            show("thirdFloor", thirdFloor);
            List<String> withZero = roomNumbers(xpath, doc, "//room[contains(@number, '0')]");        // [101, 102, 205, 310]
            show("withZero", withZero);
            String lastTwo = "//room[substring(@number, string-length(@number) - 1) = '01']";
            List<String> endsWith01 = roomNumbers(xpath, doc, lastTwo);                                // [101]
            show("endsWith01", endsWith01);
        }
        {
            Document doc = parseSecurely(Path.of("rooms.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();

            List<String> withView = roomNumbers(xpath, doc, "//room[@view]");                                  // [205, 310]
            show("withView", withView);
            List<String> noView = roomNumbers(xpath, doc, "//room[not(@view)]");                               // [101, 102, 311]
            show("noView", noView);
            List<String> singleOrSuite = roomNumbers(xpath, doc, "//room[@type='single' or @type='suite']");   // [101, 310]
            show("singleOrSuite", singleOrSuite);
            List<String> freeTop = roomNumbers(xpath, doc, "//room[@floor=3][@status='free']");                // [310]
            show("freeTop", freeTop);
        }
        {
            Document doc = parseSecurely(Path.of("rooms.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();

            String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            String lower = "abcdefghijklmnopqrstuvwxyz";
            String expression = "//room[translate(@type, '" + upper + "', '" + lower + "') = 'suite']";
            List<String> suites = roomNumbers(xpath, doc, expression);                  // [310, 311]
            show("suites", suites);
        }
        {
            Document doc = parseSecurely(Path.of("rooms.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();

            String typed = "x' or '1'='1";
            List<String> leaked = roomNumbers(xpath, doc, "//room[@type='" + typed + "']");   // [101, 102, 205, 310, 311]
            show("leaked", leaked);
        }
        {
            Document doc = parseSecurely(Path.of("rooms.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();

            Map<String, Object> params = new HashMap<>();
            xpath.setXPathVariableResolver(name -> params.get(name.getLocalPart()));
            XPathExpression byType = xpath.compile("//room[@type=$type and @status='free']");

            params.put("type", "x' or '1'='1");
            int attack = byType.evaluateExpression(doc, XPathNodes.class).size();     // 0
            show("attack", attack);
            params.put("type", "double");
            int doubles = byType.evaluateExpression(doc, XPathNodes.class).size();    // 1
            show("doubles", doubles);
        }
        {
            Document doc = parseSecurely(Path.of("rooms.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();

            Pattern threeDigits = Pattern.compile("[23]\\d1");
            List<String> matched = roomNumbers(xpath, doc, "//room").stream()
                    .filter(n -> threeDigits.matcher(n).matches())
                    .toList();
            List<String> result = matched;                                     // [311]
            show("result", result);
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
