package com.howtodoinjava.xml;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Generates a large recipes XML file and compares the heap used by DOM with SAX and StAX, which
 * read the same file as a stream. Numbers are rough and depend on the JVM.
 *
 * <p>Run from the project root: mvn -q compile exec:java
 * -Dexec.mainClass=com.howtodoinjava.xml.DomMemoryExample
 */
public class DomMemoryExample {

  public static void main(String[] args) throws Exception {
    Path file = Files.createTempFile("recipes-large", ".xml");
    try (BufferedWriter w = Files.newBufferedWriter(file)) {
      w.write("<recipes>\n");
      for (int i = 0; i < 200_000; i++) {
        w.write("  <recipe id=\"" + i + "\" cuisine=\"italian\">\n"
            + "    <name>Pasta " + i + "</name>\n"
            + "    <minutes>20</minutes>\n"
            + "    <ingredients>\n"
            + "      <ingredient qty=\"200g\">spaghetti</ingredient>\n"
            + "      <ingredient qty=\"2\">tomato</ingredient>\n"
            + "    </ingredients>\n"
            + "  </recipe>\n");
      }
      w.write("</recipes>\n");
    }
    System.out.printf("file size: %d MB%n", Files.size(file) / (1024 * 1024));

    long before = usedHeap();
    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file.toFile());
    long afterDom = usedHeap();
    System.out.printf("DOM:  %d recipes, heap held by the Document: %d MB%n",
        doc.getElementsByTagName("recipe").getLength(), (afterDom - before) / (1024 * 1024));
    doc = null;

    long saxStart = usedHeap();
    int[] count = {0};
    long[] peak = {0};
    SAXParserFactory.newInstance().newSAXParser().parse(file.toFile(), new DefaultHandler() {
      @Override
      public void startElement(String uri, String local, String qName, Attributes attrs) {
        if (qName.equals("recipe") && ++count[0] % 50_000 == 0) {
          peak[0] = Math.max(peak[0], usedHeap() - saxStart);
        }
      }
    });
    System.out.printf("SAX:  %d recipes, heap held while reading: %d KB%n",
        count[0], Math.max(0, peak[0]) / 1024);

    long staxStart = usedHeap();
    int staxCount = 0;
    long staxPeak = 0;
    try (InputStream in = Files.newInputStream(file)) {
      XMLStreamReader r = XMLInputFactory.newInstance().createXMLStreamReader(in);
      while (r.hasNext()) {
        if (r.next() == XMLStreamConstants.START_ELEMENT && r.getLocalName().equals("recipe")
            && ++staxCount % 50_000 == 0) {
          staxPeak = Math.max(staxPeak, usedHeap() - staxStart);
        }
      }
    }
    System.out.printf("StAX: %d recipes, heap held while reading: %d KB%n",
        staxCount, Math.max(0, staxPeak) / 1024);

    Files.delete(file);
  }

  static long usedHeap() {
    Runtime rt = Runtime.getRuntime();
    for (int i = 0; i < 3; i++) {
      System.gc();
    }
    return rt.totalMemory() - rt.freeMemory();
  }
}
