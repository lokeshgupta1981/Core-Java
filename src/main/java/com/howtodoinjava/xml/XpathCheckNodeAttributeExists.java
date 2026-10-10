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
 * Examples for the tutorial "XPath: Check if a Node or Attribute Exists in Java".
 * https://howtodoinjava.com/java/xml/xpath-check-if-xml-tag-exists/
 */
public class XpathCheckNodeAttributeExists {
    static boolean exists(XPath xpath, Object context, String path) throws XPathExpressionException {
        return xpath.evaluateExpression("boolean(" + path + ")", context, Boolean.class);
    }
    public static void main(String[] args) throws Exception {
        {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("employees.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();
            boolean hasEmployee = xpath.evaluateExpression("boolean(//employee)", doc, Boolean.class);               // true
            show("hasEmployee", hasEmployee);
            boolean hasIdAttribute = xpath.evaluateExpression("boolean(//employee/@id)", doc, Boolean.class);        // true
            show("hasIdAttribute", hasIdAttribute);
            boolean hasManager = xpath.evaluateExpression("boolean(//employee/manager)", doc, Boolean.class);        // false
            show("hasManager", hasManager);
            boolean brianHasStatus = xpath.evaluateExpression("boolean(//employee[@id='2']/@status)", doc, Boolean.class);   // false
            show("brianHasStatus", brianHasStatus);
        }
        {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("employees.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();

            Boolean viaBoolean = (Boolean) xpath.evaluate("//employee/@id", doc, XPathConstants.BOOLEAN);   // true
            show("viaBoolean", viaBoolean);
            NodeList ids = (NodeList) xpath.evaluate("//employee/@id", doc, XPathConstants.NODESET);
            boolean viaNodeSet = ids.getLength() > 0;                                                       // true
            show("viaNodeSet", viaNodeSet);
            Node manager = (Node) xpath.evaluate("//employee/manager", doc, XPathConstants.NODE);
            boolean viaNode = manager != null;                                                              // false
            show("viaNode", viaNode);
            Integer idCount = xpath.evaluateExpression("count(//employee/@id)", doc, Integer.class);        // 2
            show("idCount", idCount);
        }
        {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("employees.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();

            boolean anyStatus = xpath.evaluateExpression("boolean(//employee/@status)", doc, Boolean.class);           // true
            show("anyStatus", anyStatus);
            boolean firstHasStatus = xpath.evaluateExpression("boolean(//employee[1]/@status)", doc, Boolean.class);   // true
            show("firstHasStatus", firstHasStatus);
            boolean activeExists = xpath.evaluateExpression("boolean(//employee[@status='active'])", doc, Boolean.class);   // true
            show("activeExists", activeExists);
            boolean id3Exists = xpath.evaluateExpression("boolean(//employee[@id='3'])", doc, Boolean.class);          // false
            show("id3Exists", id3Exists);
            String withoutStatus = xpath.evaluate("//employee[not(@status)]/firstName", doc);                         // "Brian"
            show("withoutStatus", withoutStatus);
        }
        {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("employees.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();

            String emptyValue = xpath.evaluate("//employee[1]/middleName", doc);                                       // ""
            show("emptyValue", emptyValue);
            String missingValue = xpath.evaluate("//employee[2]/middleName", doc);                                     // ""
            show("missingValue", missingValue);
            boolean emptyExists = xpath.evaluateExpression("boolean(//employee[1]/middleName)", doc, Boolean.class);    // true
            show("emptyExists", emptyExists);
            boolean missingExists = xpath.evaluateExpression("boolean(//employee[2]/middleName)", doc, Boolean.class);  // false
            show("missingExists", missingExists);
            boolean hasText = xpath.evaluateExpression("boolean(//employee[1]/middleName[normalize-space()])", doc, Boolean.class);   // false
            show("hasText", hasText);
        }
        {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("employees.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();
            List<String> withMiddleName = new ArrayList<>();
            for (Node employee : xpath.evaluateExpression("//employee", doc, XPathNodes.class)) {
                if (exists(xpath, employee, "middleName")) {
                    withMiddleName.add(xpath.evaluate("firstName", employee));
                }
            }
            List<String> result = withMiddleName;                                     // [Lokesh]
            show("result", result);
            boolean deptName = exists(xpath, doc, "//employee[2]/department/name");   // true
            show("deptName", deptName);
        }
        {
            String response = """
            <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
            <soap:Body>
            <soap:Fault><faultstring>Card expired</faultstring></soap:Fault>
            </soap:Body>
            </soap:Envelope>""";
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document soap = factory.newDocumentBuilder().parse(new InputSource(new StringReader(response)));
            XPath xpath = XPathFactory.newInstance().newXPath();

            boolean plainPath = exists(xpath, soap, "//Fault");                                // false
            show("plainPath", plainPath);
            boolean isFault = exists(xpath, soap, "//*[local-name()='Fault']");                // true
            show("isFault", isFault);
            String reason = xpath.evaluate("//*[local-name()='faultstring']", soap);           // "Card expired"
            show("reason", reason);
        }
        {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("employees.xml"));
            Element first = (Element) doc.getElementsByTagName("employee").item(0);
            Element second = (Element) doc.getElementsByTagName("employee").item(1);

            boolean firstStatus = first.hasAttribute("status");                          // true
            show("firstStatus", firstStatus);
            boolean secondStatus = second.hasAttribute("status");                        // false
            show("secondStatus", secondStatus);
            boolean anyManager = doc.getElementsByTagName("manager").getLength() > 0;    // false
            show("anyManager", anyManager);
        }
        {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("employees.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();
            try { Boolean result = xpath.evaluateExpression("exists(//employee)", doc, Boolean.class); show("result", result); } catch (Throwable _t) { System.out.println("result -> " + _t); }
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
