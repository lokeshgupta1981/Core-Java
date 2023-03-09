package com.howtodoinjava.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ConvertStringToXML {

  public static void main(String[] args) {
    final String xmlStr = "<employees>" +
        " <employee id=\"101\">" +
        "    <name>Lokesh Gupta</name>" +
        "     <title>Author</title>" +
        " </employee>" +
        " <employee id=\"102\">" +
        "    <name>Brian Lara</name>" +
        "     <title>Cricketer</title>" +
        " </employee>" +
        "</employees>";

    //Use method to convert XML string content to XML Document object
    Document doc = convertStringToXMLDocument(xmlStr);

    //Verify XML document is build correctly
    System.out.println("Root Node :" + doc.getFirstChild().getNodeName());

    NodeList nodeList = doc.getElementsByTagName("employee");

    for (int itr = 0; itr < nodeList.getLength(); itr++) {

      Node node = nodeList.item(itr);
      System.out.println("\nNode Name :" + node.getNodeName());
      if (node.getNodeType() == Node.ELEMENT_NODE) {

        Element eElement = (Element) node;
        System.out.println(
            "Name: " + eElement.getElementsByTagName("name").item(0).getTextContent());
        System.out.println(
            "Title: " + eElement.getElementsByTagName("title").item(0).getTextContent());
      }
    }

    //Convert XML to String
    String outputXML = convertXmlDomToString(doc);
    System.out.println("Output string : " + outputXML);

    //Write XML to File
    writeXmlDocumentToFile(doc, "c:/temp/test.xml");
  }

  private static Document convertStringToXMLDocument(String xmlString) {
    //Parser that produces DOM object trees from XML content
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    //API to obtain DOM Document instance
    DocumentBuilder builder = null;
    try {
      //Create DocumentBuilder with default configuration
      builder = factory.newDocumentBuilder();

      //Parse the content to Document object
      Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
      return doc;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String convertXmlDomToString(Document xmlDocument) {
    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer transformer;
    try {
      transformer = tf.newTransformer();

      // Uncomment if you do not require XML declaration
      // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

      //A character stream that collects its output in a string buffer,
      //which can then be used to construct a string.
      StringWriter writer = new StringWriter();

      //transform document to string
      transformer.transform(new DOMSource(xmlDocument), new StreamResult(writer));

      return writer.getBuffer().toString();
    } catch (TransformerException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private static void writeXmlDocumentToFile(Document xmlDocument, String fileName) {
    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer transformer;
    try {
      transformer = tf.newTransformer();

      //Uncomment if you do not require XML declaration
      //transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

      //Write XML to file
      FileOutputStream outStream = new FileOutputStream(new File(fileName));

      transformer.transform(new DOMSource(xmlDocument), new StreamResult(outStream));
    } catch (TransformerException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}