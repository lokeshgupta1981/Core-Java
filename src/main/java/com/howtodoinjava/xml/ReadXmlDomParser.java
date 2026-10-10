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
import java.math.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import javax.xml.xpath.*;
import javax.xml.XMLConstants;

/**
 * Examples for the tutorial "Read XML File in Java using DOM Parser (with Examples)".
 * https://howtodoinjava.com/java/xml/read-xml-dom-parser-example/
 */
public class ReadXmlDomParser {
    static DocumentBuilder newBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        return factory.newDocumentBuilder();
    }
    static List<Element> childElements(Element parent) {
        List<Element> result = new ArrayList<>();
        for (Node n = parent.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                result.add((Element) n);
            }
        }
        return result;
    }
    static String text(Element parent, String tag) {
        Node node = parent.getElementsByTagName(tag).item(0);
        return node == null ? null : node.getTextContent().strip();
    }
    static void printElement(Element element, int depth) {
        StringBuilder line = new StringBuilder("  ".repeat(depth)).append(element.getTagName());
        NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            line.append(" @").append(attrs.item(i).getNodeName())
                    .append('=').append(attrs.item(i).getNodeValue());
        }
        List<Element> children = childElements(element);
        if (children.isEmpty()) {
            line.append(" = ").append(element.getTextContent().strip());
        }
        System.out.println(line);
        for (Element child : children) {
            printElement(child, depth + 1);
        }
    }
    static record Ingredient(String name, String qty) {}

    static record Recipe(int id, String cuisine, String name, int minutes, List<Ingredient> ingredients) {}
    public static void main(String[] args) throws Exception {
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);  // blocks XXE
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(new File("recipes.xml"));
            doc.getDocumentElement().normalize();

            String root = doc.getDocumentElement().getNodeName();    // recipes
            show("root", root);
            NodeList recipes = doc.getElementsByTagName("recipe");   // 2 elements
            show("recipes", recipes);

            for (int i = 0; i < recipes.getLength(); i++) {
                Element recipe = (Element) recipes.item(i);
                String id = recipe.getAttribute("id");                                        // "1"
                String name = recipe.getElementsByTagName("name").item(0).getTextContent();   // "Pasta"
                String minutes = recipe.getElementsByTagName("minutes").item(0).getTextContent();
                System.out.println(id + " " + name + " " + minutes);
            }
        }
        {
            Document doc = newBuilder().parse(new File("recipes.xml"));
            Element pasta = (Element) doc.getElementsByTagName("recipe").item(0);
            NodeList children = pasta.getChildNodes();
            int count = children.getLength();    // 7
            show("count", count);
        }
        {
            Document doc = newBuilder().parse(new File("recipes.xml"));
            Element pasta = (Element) doc.getElementsByTagName("recipe").item(0);
            int elementCount = childElements(pasta).size();   // 3
            show("elementCount", elementCount);
        }
        {
            Document doc = newBuilder().parse(new InputSource(new StringReader("<name>Pasta</name>")));
            Element name = doc.getDocumentElement();
            int before = name.getChildNodes().getLength();          // 1
            show("before", before);

            name.appendChild(doc.createTextNode(" Bake"));
            int afterAppend = name.getChildNodes().getLength();     // 2
            show("afterAppend", afterAppend);
            String first = name.getFirstChild().getNodeValue();     // Pasta
            show("first", first);

            doc.normalize();
            int afterNormalize = name.getChildNodes().getLength();  // 1
            show("afterNormalize", afterNormalize);
            String merged = name.getFirstChild().getNodeValue();    // Pasta Bake
            show("merged", merged);
        }
        {
            Document doc = newBuilder().parse(new File("recipes.xml"));
            Element name = (Element) doc.getElementsByTagName("name").item(0);
            String content = name.getTextContent();                  // Pasta
            show("content", content);
            String elementValue = name.getNodeValue();               // null
            show("elementValue", elementValue);
            String textValue = name.getFirstChild().getNodeValue();  // Pasta
            show("textValue", textValue);
        }
        {
            Document doc = newBuilder().parse(new File("recipes.xml"));
            Element pasta = (Element) doc.getElementsByTagName("recipe").item(0);

            String idText = pasta.getAttribute("id");             // "1"
            show("idText", idText);
            String spicy = pasta.getAttribute("spicy");           // "" (missing attribute)
            show("spicy", spicy);
            boolean hasSpicy = pasta.hasAttribute("spicy");       // false
            show("hasSpicy", hasSpicy);
            Attr spicyNode = pasta.getAttributeNode("spicy");     // null
            show("spicyNode", spicyNode);
            int id = Integer.parseInt(pasta.getAttribute("id"));  // 1
            show("id", id);
        }
        {
            Document doc = newBuilder().parse(new File("recipes.xml"));
            Element pasta = (Element) doc.getElementsByTagName("recipe").item(0);
            NamedNodeMap attrs = pasta.getAttributes();
            for (int i = 0; i < attrs.getLength(); i++) {
                Node attr = attrs.item(i);
                System.out.println(attr.getNodeName() + " = " + attr.getNodeValue());
            }
        }
        {
            Document doc = newBuilder().parse(new File("recipes.xml"));
            int all = doc.getElementsByTagName("ingredient").getLength();   // 4 (both recipes)
            show("all", all);

            Element dal = (Element) doc.getElementsByTagName("recipe").item(1);
            NodeList ingredients = dal.getElementsByTagName("ingredient");
            int dalCount = ingredients.getLength();                         // 2 (only Dal)
            show("dalCount", dalCount);

            for (int i = 0; i < ingredients.getLength(); i++) {
                Element ing = (Element) ingredients.item(i);
                System.out.println(ing.getAttribute("qty") + " " + ing.getTextContent());
            }
        }
        {
            Document doc = newBuilder().parse(new File("recipes.xml"));
            Element dal = (Element) doc.getElementsByTagName("recipe").item(1);
            String dalName = text(dal, "name");     // "Dal"
            show("dalName", dalName);
            String rating = text(dal, "rating");    // null
            show("rating", rating);
        }
        {
            Document doc = newBuilder().parse(new File("recipes.xml"));
            printElement(doc.getDocumentElement(), 0);
        }
        {
            Document doc = newBuilder().parse(new File("recipes.xml"));
            List<Recipe> result = new ArrayList<>();
            NodeList nodes = doc.getElementsByTagName("recipe");

            for (int i = 0; i < nodes.getLength(); i++) {
                Element r = (Element) nodes.item(i);

                List<Ingredient> ingredients = new ArrayList<>();
                NodeList ingNodes = r.getElementsByTagName("ingredient");
                for (int j = 0; j < ingNodes.getLength(); j++) {
                    Element ing = (Element) ingNodes.item(j);
                    ingredients.add(new Ingredient(ing.getTextContent().strip(), ing.getAttribute("qty")));
                }

                result.add(new Recipe(
                Integer.parseInt(r.getAttribute("id")),
                r.getAttribute("cuisine"),
                text(r, "name"),
                Integer.parseInt(text(r, "minutes")),
                List.copyOf(ingredients)));
            }

            List<String> quick = result.stream().filter(r -> r.minutes() < 30).map(Recipe::name).toList();   // [Pasta]
            show("quick", quick);
        }
        {
            String xml = """
            <recipe id="3" cuisine="mexican">
            <name>Tacos</name>
            <minutes>15</minutes>
            </recipe>""";

            Document doc = newBuilder().parse(new InputSource(new StringReader(xml)));
            String cuisine = doc.getDocumentElement().getAttribute("cuisine");   // "mexican"
            show("cuisine", cuisine);
        }
        {
            String xml = "<recipe id=\"3\"><name>Tacos</name></recipe>";
            try { Document wrong = newBuilder().parse(xml); show("wrong", wrong); } catch (Throwable _t) { System.out.println("wrong -> " + _t); }
        }
        {
            try (InputStream in = Files.newInputStream(Path.of("recipes.xml"))) {
                Document doc = newBuilder().parse(in);
                int count = doc.getElementsByTagName("recipe").getLength();   // 2
            }
        }
        {
            Document doc = newBuilder().parse(new File("recipes.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();

            String dalName = xpath.evaluate("/recipes/recipe[@id='2']/name", doc);    // "Dal"
            show("dalName", dalName);
            String quick = xpath.evaluate("//recipe[minutes < 30]/name", doc);        // "Pasta"
            show("quick", quick);
            String qty = xpath.evaluate("//ingredient[.='lentils']/@qty", doc);       // "1 cup"
            show("qty", qty);
            String countText = xpath.evaluate("count(//ingredient)", doc);            // "4"
            show("countText", countText);
            Double count = xpath.evaluateExpression("count(//ingredient)", doc, Double.class);   // 4.0
            show("count", count);

            NodeList names = (NodeList) xpath.evaluate("//recipe/name", doc, XPathConstants.NODESET);
            int nameCount = names.getLength();                                        // 2
            show("nameCount", nameCount);
        }
        {
            String xml = """
            <!DOCTYPE recipe [
            <!ENTITY secret SYSTEM "file:///etc/recipes/secret.txt">
            ]>
            <recipe><name>&secret;</name></recipe>""";

            Document unsafe = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(new InputSource(new StringReader(xml)));
            String leaked = unsafe.getDocumentElement().getTextContent();   // "db.password=tiger"
            show("leaked", leaked);

            try { Document safe = newBuilder().parse(new InputSource(new StringReader(xml))); show("safe", safe); } catch (Throwable _t) { System.out.println("safe -> " + _t); }
        }
        {
            String xml = """
            <r:recipe xmlns:r="https://example.com/recipes" id="1"/>""";
            Document doc = newBuilder().parse(new InputSource(new StringReader(xml)));

            int plain = doc.getElementsByTagName("recipe").getLength();       // 0
            show("plain", plain);
            int prefixed = doc.getElementsByTagName("r:recipe").getLength();  // 1
            show("prefixed", prefixed);
        }
        {
            String xml = """
            <r:recipe xmlns:r="https://example.com/recipes" id="1"/>""";
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setNamespaceAware(true);
            Document doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));

            NodeList recipes = doc.getElementsByTagNameNS("https://example.com/recipes", "recipe");
            int nsCount = recipes.getLength();                           // 1
            show("nsCount", nsCount);
            String local = ((Element) recipes.item(0)).getLocalName();   // recipe
            show("local", local);
            String tag = ((Element) recipes.item(0)).getTagName();       // r:recipe
            show("tag", tag);
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
