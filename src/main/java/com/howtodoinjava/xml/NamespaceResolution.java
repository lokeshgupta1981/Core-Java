package com.howtodoinjava.xml;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class NamespaceResolution {

  public static void main(String[] args) throws Exception {

    //Want to read all book names from XML
    ArrayList<String> bookNames = new ArrayList<String>();

    //Parse XML file
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(new FileInputStream(new File("sample.xml")));

    //Get XPath expression
    XPathFactory xpathfactory = XPathFactory.newInstance();
    XPath xpath = xpathfactory.newXPath();
    xpath.setNamespaceContext(new NamespaceResolver(doc));
    XPathExpression expr = xpath.compile("//ns2:bookStore/ns2:book/ns2:name/text()");

    //Search XPath expression
    Object result = expr.evaluate(doc, XPathConstants.NODESET);

    //Iterate over results and fetch book names
    NodeList nodes = (NodeList) result;
    for (int i = 0; i < nodes.getLength(); i++) {
      bookNames.add(nodes.item(i).getNodeValue());
    }

    //Verify book names
    System.out.println(bookNames);
  }
}

class NamespaceResolver implements NamespaceContext {

  //Store the source document to search the namespaces
  private Document sourceDocument;

  public NamespaceResolver(Document document) {
    sourceDocument = document;
  }

  //The lookup for the namespace uris is delegated to the stored document.
  public String getNamespaceURI(String prefix) {
    if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
      return sourceDocument.lookupNamespaceURI(null);
    } else {
      return sourceDocument.lookupNamespaceURI(prefix);
    }
  }

  public String getPrefix(String namespaceURI) {
    return sourceDocument.lookupPrefix(namespaceURI);
  }

  @SuppressWarnings("rawtypes")
  public Iterator getPrefixes(String namespaceURI) {
    return null;
  }
}
