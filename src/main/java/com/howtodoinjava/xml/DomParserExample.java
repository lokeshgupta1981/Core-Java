package com.howtodoinjava.xml;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathNodes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Reads recipes.xml with the JDK DOM parser: text, attributes, nested elements, whitespace text
 * nodes, normalize(), records, XPath, and parsing from a String or an InputStream.
 *
 * <p>Run from the project root: mvn -q compile exec:java
 * -Dexec.mainClass=com.howtodoinjava.xml.DomParserExample
 */
public class DomParserExample {

  record Ingredient(String name, String qty) {}

  record Recipe(int id, String cuisine, String name, int minutes, List<Ingredient> ingredients) {}

  static final String FILE = "src/main/resources/xml/recipes.xml";

  public static void main(String[] args) throws Exception {
    quickAnswer();
    childNodesAndWhitespace();
    normalizeDemo();
    textContentVsNodeValue();
    attributes();
    nestedElements();
    printTree();
    readIntoRecords();
    xpath();
    fromStringAndStream();
    namespaces();
  }

  /** A factory with DOCTYPE declarations disallowed (blocks XXE, see DomXxeExample). */
  static DocumentBuilder newBuilder() throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    return factory.newDocumentBuilder();
  }

  static void quickAnswer() throws Exception {
    System.out.println("== quick answer");
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    DocumentBuilder builder = factory.newDocumentBuilder();

    Document doc = builder.parse(new File(FILE));
    doc.getDocumentElement().normalize();

    System.out.println(doc.getDocumentElement().getNodeName());
    NodeList recipes = doc.getElementsByTagName("recipe");
    System.out.println(recipes.getLength());
    for (int i = 0; i < recipes.getLength(); i++) {
      Element recipe = (Element) recipes.item(i);
      String id = recipe.getAttribute("id");
      String name = recipe.getElementsByTagName("name").item(0).getTextContent();
      String minutes = recipe.getElementsByTagName("minutes").item(0).getTextContent();
      System.out.println(id + " " + name + " " + minutes);
    }
  }

  static void childNodesAndWhitespace() throws Exception {
    System.out.println("== child nodes");
    Document doc = newBuilder().parse(new File(FILE));
    Element pasta = (Element) doc.getElementsByTagName("recipe").item(0);

    NodeList children = pasta.getChildNodes();
    System.out.println("getChildNodes().getLength() = " + children.getLength());
    for (int i = 0; i < children.getLength(); i++) {
      Node child = children.item(i);
      String type = child.getNodeType() == Node.ELEMENT_NODE ? "ELEMENT" : "TEXT";
      String value = child.getNodeType() == Node.TEXT_NODE
          ? child.getNodeValue().replace("\n", "\\n") : child.getNodeName();
      System.out.println(i + " " + type + " [" + value + "]");
    }

    System.out.println("elements only = " + childElements(pasta).size());

    // setIgnoringElementContentWhitespace() needs a DTD; without one nothing changes
    DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
    f.setIgnoringElementContentWhitespace(true);
    Document doc2 = f.newDocumentBuilder().parse(new File(FILE));
    System.out.println("ignoringWhitespace, child nodes = "
        + doc2.getElementsByTagName("recipe").item(0).getChildNodes().getLength());
  }

  /** Direct child elements only, skipping whitespace text nodes and comments. */
  static List<Element> childElements(Element parent) {
    List<Element> result = new ArrayList<>();
    for (Node n = parent.getFirstChild(); n != null; n = n.getNextSibling()) {
      if (n.getNodeType() == Node.ELEMENT_NODE) {
        result.add((Element) n);
      }
    }
    return result;
  }

  static void normalizeDemo() throws Exception {
    System.out.println("== normalize");
    Document doc = newBuilder().parse(new InputSource(new StringReader("<name>Pasta</name>")));
    Element name = doc.getDocumentElement();
    System.out.println("after parse: " + name.getChildNodes().getLength());

    name.appendChild(doc.createTextNode(" Bake"));
    System.out.println("after appendChild: " + name.getChildNodes().getLength());
    System.out.println("first text: " + name.getFirstChild().getNodeValue());

    doc.normalize();
    System.out.println("after normalize: " + name.getChildNodes().getLength());
    System.out.println("first text: " + name.getFirstChild().getNodeValue());

    // normalize() keeps whitespace-only text nodes
    Document recipes = newBuilder().parse(new File(FILE));
    recipes.getDocumentElement().normalize();
    System.out.println("recipe child nodes after normalize: "
        + recipes.getElementsByTagName("recipe").item(0).getChildNodes().getLength());
  }

  static void textContentVsNodeValue() throws Exception {
    System.out.println("== getTextContent vs getNodeValue");
    Document doc = newBuilder().parse(new File(FILE));
    Element name = (Element) doc.getElementsByTagName("name").item(0);
    System.out.println("name.getTextContent() = " + name.getTextContent());
    System.out.println("name.getNodeValue() = " + name.getNodeValue());
    System.out.println("name.getFirstChild().getNodeValue() = "
        + name.getFirstChild().getNodeValue());

    Element ingredients = (Element) doc.getElementsByTagName("ingredients").item(0);
    System.out.println("ingredients.getTextContent() = ["
        + ingredients.getTextContent().replace("\n", "\\n") + "]");
    System.out.println("strip+split = " + String.join(",",
        ingredients.getTextContent().strip().split("\\s+")));

    Element padded = newBuilder().parse(new InputSource(new StringReader(
        "<name>\n  Pasta\n</name>"))).getDocumentElement();
    System.out.println("padded.getTextContent() = [" + padded.getTextContent().replace("\n", "\\n")
        + "], strip() = [" + padded.getTextContent().strip() + "]");

    Element empty = newBuilder().parse(new InputSource(new StringReader("<name/>")))
        .getDocumentElement();
    System.out.println("empty.getTextContent() = [" + empty.getTextContent() + "]");
    System.out.println("empty.getFirstChild() = " + empty.getFirstChild());
  }

  static void attributes() throws Exception {
    System.out.println("== attributes");
    Document doc = newBuilder().parse(new File(FILE));
    Element pasta = (Element) doc.getElementsByTagName("recipe").item(0);

    System.out.println("getAttribute(\"id\") = " + pasta.getAttribute("id"));
    System.out.println("getAttribute(\"spicy\") = [" + pasta.getAttribute("spicy") + "]");
    System.out.println("hasAttribute(\"spicy\") = " + pasta.hasAttribute("spicy"));
    System.out.println("getAttributeNode(\"spicy\") = " + pasta.getAttributeNode("spicy"));
    int id = Integer.parseInt(pasta.getAttribute("id"));
    System.out.println("id = " + id);

    NamedNodeMap attrs = pasta.getAttributes();
    for (int i = 0; i < attrs.getLength(); i++) {
      Node attr = attrs.item(i);
      System.out.println(attr.getNodeName() + " = " + attr.getNodeValue());
    }
  }

  static void nestedElements() throws Exception {
    System.out.println("== nested");
    Document doc = newBuilder().parse(new File(FILE));
    System.out.println("doc ingredient count = " + doc.getElementsByTagName("ingredient").getLength());

    Element dal = (Element) doc.getElementsByTagName("recipe").item(1);
    NodeList ingredients = dal.getElementsByTagName("ingredient");
    System.out.println("dal ingredient count = " + ingredients.getLength());
    for (int i = 0; i < ingredients.getLength(); i++) {
      Element ing = (Element) ingredients.item(i);
      System.out.println(ing.getAttribute("qty") + " " + ing.getTextContent());
    }

    System.out.println("text(dal, name) = " + text(dal, "name"));
    System.out.println("text(dal, rating) = " + text(dal, "rating"));
    System.out.println("missing tag item(0) = " + dal.getElementsByTagName("rating").item(0));
    try {
      dal.getElementsByTagName("rating").item(0).getTextContent();
    } catch (NullPointerException e) {
      System.out.println("NullPointerException: " + e.getMessage());
    }
  }

  static void printTree() throws Exception {
    System.out.println("== tree");
    Document doc = newBuilder().parse(new File(FILE));
    printElement(doc.getDocumentElement(), 0);
  }

  /** Walks every element, printing attributes and leaf text, indented by depth. */
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

  static List<Recipe> readRecipes(Document doc) {
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
    return result;
  }

  /** Text of the first descendant with the tag, or null when the tag is missing. */
  static String text(Element parent, String tag) {
    Node node = parent.getElementsByTagName(tag).item(0);
    return node == null ? null : node.getTextContent().strip();
  }

  static void readIntoRecords() throws Exception {
    System.out.println("== records");
    Document doc = newBuilder().parse(new File(FILE));
    List<Recipe> recipes = readRecipes(doc);
    recipes.forEach(System.out::println);
    System.out.println("quick = " + recipes.stream()
        .filter(r -> r.minutes() < 30).map(Recipe::name).toList());
  }

  static void xpath() throws Exception {
    System.out.println("== xpath");
    Document doc = newBuilder().parse(new File(FILE));
    XPath xpath = XPathFactory.newInstance().newXPath();

    System.out.println(xpath.evaluate("/recipes/recipe[@id='2']/name", doc));
    System.out.println(xpath.evaluate("//recipe[minutes < 30]/name", doc));
    System.out.println(xpath.evaluate("count(//ingredient)", doc));
    Double count = xpath.evaluateExpression("count(//ingredient)", doc, Double.class);
    System.out.println(count);
    System.out.println(xpath.evaluate("//ingredient[.='lentils']/@qty", doc));

    NodeList names = (NodeList) xpath.evaluate("//recipe/name", doc, XPathConstants.NODESET);
    System.out.println("NODESET length = " + names.getLength());

    XPathNodes nodes = xpath.evaluateExpression("//ingredient", doc, XPathNodes.class);
    for (Node n : nodes) {
      System.out.print(n.getTextContent() + " ");
    }
    System.out.println();
  }

  static void fromStringAndStream() throws Exception {
    System.out.println("== string and stream");
    String xml = """
        <recipe id="3" cuisine="mexican">
          <name>Tacos</name>
          <minutes>15</minutes>
        </recipe>""";

    DocumentBuilder builder = newBuilder();
    Document fromString = builder.parse(new InputSource(new StringReader(xml)));
    System.out.println(fromString.getDocumentElement().getAttribute("cuisine"));

    try (InputStream in = DomParserExample.class.getResourceAsStream("/xml/recipes.xml")) {
      Document fromStream = builder.parse(in);
      System.out.println(fromStream.getElementsByTagName("recipe").getLength());
    }

    try {
      builder.parse(xml);
    } catch (Exception e) {
      System.out.println(e.getClass().getName() + ": " + e.getMessage());
    }
  }

  static void namespaces() throws Exception {
    System.out.println("== namespaces");
    String xml = """
        <r:recipes xmlns:r="https://example.com/recipes">
          <r:recipe id="1"><r:name>Pasta</r:name></r:recipe>
        </r:recipes>""";

    Document plain = newBuilder().parse(new InputSource(new StringReader(xml)));
    System.out.println("plain recipe = " + plain.getElementsByTagName("recipe").getLength());
    System.out.println("plain r:recipe = " + plain.getElementsByTagName("r:recipe").getLength());
    System.out.println("case Recipe = " + newBuilder().parse(new File(FILE))
        .getElementsByTagName("Recipe").getLength());

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    Document aware = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
    NodeList recipes = aware.getElementsByTagNameNS("https://example.com/recipes", "recipe");
    System.out.println("aware NS recipe = " + recipes.getLength());
    System.out.println("aware any-NS name = "
        + aware.getElementsByTagNameNS("*", "name").item(0).getTextContent());
    Element first = (Element) recipes.item(0);
    System.out.println("localName = " + first.getLocalName() + ", tagName = " + first.getTagName());
  }
}
