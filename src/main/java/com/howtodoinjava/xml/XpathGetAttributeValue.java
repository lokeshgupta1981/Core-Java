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
 * Examples for the tutorial "Java XPath Get Attribute Value (String, List, Number)".
 * https://howtodoinjava.com/java/xml/xpath-get-attribute-value-xml/
 */
public class XpathGetAttributeValue {
    static Document loadXml(Path file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        return factory.newDocumentBuilder().parse(file.toFile());
    }
    static record Song(String id, String genre, int seconds) {}
    public static void main(String[] args) throws Exception {
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            Document doc = factory.newDocumentBuilder().parse(new File("playlist.xml"));

            XPath xpath = XPathFactory.newInstance().newXPath();
            String genre = xpath.evaluate("/playlist/song[@id='s2']/@genre", doc);   // "jazz"
            show("genre", genre);
            String name = xpath.evaluate("/playlist/@name", doc);                    // "Road trip"
            show("name", name);
        }
        {
            Document doc = loadXml(Path.of("playlist.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();

            String first = xpath.evaluate("//song/@genre", doc);                 // "rock"
            show("first", first);
            String missing = xpath.evaluate("//song[@id='s9']/@genre", doc);     // ""
            show("missing", missing);
            String noAttr = xpath.evaluate("//song[@id='s1']/@explicit", doc);   // ""
            show("noAttr", noAttr);
            boolean exists = (Boolean) xpath.evaluate("boolean(//song[@id='s1']/@explicit)", doc, XPathConstants.BOOLEAN);   // false
            show("exists", exists);
        }
        {
            Document doc = loadXml(Path.of("playlist.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();

            XPathNodes nodes = xpath.evaluateExpression("//song/@id", doc, XPathNodes.class);
            List<String> ids = new ArrayList<>();
            for (Node node : nodes) {
                ids.add(node.getNodeValue());
            }
            List<String> allIds = ids;                 // [s1, s2, s3]
            show("allIds", allIds);
            int count = nodes.size();                  // 3
            show("count", count);
        }
        {
            Document doc = loadXml(Path.of("playlist.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();

            NodeList list = (NodeList) xpath.evaluate("//song/@genre", doc, XPathConstants.NODESET);
            List<String> genres = new ArrayList<>();
            for (int i = 0; i < list.getLength(); i++) {
                genres.add(list.item(i).getNodeValue());
            }
            List<String> allGenres = genres;           // [rock, jazz, rock]
            show("allGenres", allGenres);
        }
        {
            Document doc = loadXml(Path.of("playlist.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();

            Integer seconds = xpath.evaluateExpression("//song[@id='s2']/@seconds", doc, Integer.class);   // 340
            show("seconds", seconds);
            Double total = xpath.evaluateExpression("sum(//song/@seconds)", doc, Double.class);            // 753.0
            show("total", total);
            Double rockSongs = xpath.evaluateExpression("count(//song[@genre='rock'])", doc, Double.class); // 2.0
            show("rockSongs", rockSongs);
            Double none = xpath.evaluateExpression("//song[@id='s9']/@seconds", doc, Double.class);        // NaN
            show("none", none);
        }
        {
            Document doc = loadXml(Path.of("playlist.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();

            Integer hidden = xpath.evaluateExpression("//song[@id='s9']/@seconds", doc, Integer.class);    // 0
            show("hidden", hidden);
        }
        {
            Document doc = loadXml(Path.of("playlist.xml"));
            XPath xpath = XPathFactory.newInstance().newXPath();

            List<Song> songs = new ArrayList<>();
            for (Node node : xpath.evaluateExpression("//song", doc, XPathNodes.class)) {
                Element song = (Element) node;
                songs.add(new Song(
                xpath.evaluate("@id", song),
                song.getAttribute("genre"),
                Integer.parseInt(song.getAttribute("seconds"))));
            }
            Song second = songs.get(1);                // Song[id=s2, genre=jazz, seconds=340]
            show("second", second);
        }
        {
            Document doc = loadXml(Path.of("playlist.xml"));
            XPathExpression genreOf = XPathFactory.newInstance().newXPath().compile("@genre");

            NodeList songs = doc.getElementsByTagName("song");
            String firstGenre = genreOf.evaluate(songs.item(0));     // "rock"
            show("firstGenre", firstGenre);
            String secondGenre = genreOf.evaluate(songs.item(1));    // "jazz"
            show("secondGenre", secondGenre);
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
