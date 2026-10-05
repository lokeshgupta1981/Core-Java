package com.howtodoinjava.xml;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * Shows an XXE (XML External Entity) attack against a default DocumentBuilderFactory and the
 * factory settings that block the attack.
 *
 * <p>Run from the project root: mvn -q compile exec:java
 * -Dexec.mainClass=com.howtodoinjava.xml.DomXxeExample
 */
public class DomXxeExample {

  public static void main(String[] args) throws Exception {
    Path secret = Files.createTempFile("secret", ".txt");
    Files.writeString(secret, "db.password=tiger");

    String xml = """
        <!DOCTYPE recipe [
          <!ENTITY secret SYSTEM "%s">
        ]>
        <recipe><name>&secret;</name></recipe>""".formatted(secret.toUri());
    System.out.println(xml);

    System.out.println("== default factory");
    run(DocumentBuilderFactory.newInstance(), xml);

    System.out.println("== disallow-doctype-decl");
    DocumentBuilderFactory noDoctype = DocumentBuilderFactory.newInstance();
    noDoctype.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    run(noDoctype, xml);

    System.out.println("== FEATURE_SECURE_PROCESSING");
    DocumentBuilderFactory fsp = DocumentBuilderFactory.newInstance();
    fsp.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    run(fsp, xml);

    System.out.println("== ACCESS_EXTERNAL_DTD empty");
    DocumentBuilderFactory noExternal = DocumentBuilderFactory.newInstance();
    noExternal.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    noExternal.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    run(noExternal, xml);

    System.out.println("== external-general-entities false");
    DocumentBuilderFactory noEntities = DocumentBuilderFactory.newInstance();
    noEntities.setFeature("http://xml.org/sax/features/external-general-entities", false);
    noEntities.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    noEntities.setExpandEntityReferences(false);
    run(noEntities, xml);

    System.out.println("== billion laughs, default factory");
    StringBuilder bomb = new StringBuilder("<!DOCTYPE r [\n<!ENTITY a0 \"lol\">\n");
    for (int i = 1; i <= 10; i++) {
      bomb.append("<!ENTITY a").append(i).append(" \"")
          .append(("&a" + (i - 1) + ";").repeat(10)).append("\">\n");
    }
    bomb.append("]>\n<r>&a10;</r>");
    run(DocumentBuilderFactory.newInstance(), bomb.toString());

    Files.delete(secret);
  }

  static void run(DocumentBuilderFactory factory, String xml) {
    try {
      Document doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
      String text = doc.getDocumentElement().getTextContent();
      System.out.println("parsed, text = [" + (text.length() > 60 ? text.length() + " chars" : text)
          + "]");
    } catch (Exception e) {
      System.out.println(e.getClass().getName() + ": " + e.getMessage());
    }
  }
}
