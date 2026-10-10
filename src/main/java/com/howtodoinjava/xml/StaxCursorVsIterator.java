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
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import javax.xml.namespace.QName;

/**
 * Examples for the tutorial "Java StAX Parser: Read XML with Cursor and Iterator APIs".
 * https://howtodoinjava.com/java/xml/read-xml-stax-parser-cursor-iterator/
 */
public class StaxCursorVsIterator {
    static record Song(int id, String title, String artist, int seconds) {}
    static List<Song> readWithCursor(Path file) throws IOException, XMLStreamException {
        List<Song> songs = new ArrayList<>();
        try (InputStream in = Files.newInputStream(file)) {
            XMLStreamReader reader = secureFactory().createXMLStreamReader(in);
            try {
                int id = 0, seconds = 0;
                String title = null, artist = null;
                while (reader.hasNext()) {
                    int event = reader.next();
                    if (event == XMLStreamConstants.START_ELEMENT) {
                        switch (reader.getLocalName()) {
                            case "song" -> id = Integer.parseInt(reader.getAttributeValue(null, "id"));
                            case "title" -> title = reader.getElementText();
                            case "artist" -> artist = reader.getElementText();
                            case "seconds" -> seconds = Integer.parseInt(reader.getElementText().strip());
                            default -> { }
                        }
                    } else if (event == XMLStreamConstants.END_ELEMENT
                            && reader.getLocalName().equals("song")) {
                        songs.add(new Song(id, title, artist, seconds));
                    }
                }
            } finally {
                reader.close();
            }
        }
        return songs;
    }
    static List<Song> readWithIterator(Path file) throws IOException, XMLStreamException {
        List<Song> songs = new ArrayList<>();
        QName idName = new QName("id");
        try (InputStream in = Files.newInputStream(file)) {
            XMLEventReader reader = secureFactory().createXMLEventReader(in);
            try {
                int id = 0, seconds = 0;
                String title = null, artist = null;
                while (reader.hasNext()) {
                    XMLEvent event = reader.nextEvent();
                    if (event.isStartElement()) {
                        StartElement start = event.asStartElement();
                        switch (start.getName().getLocalPart()) {
                            case "song" -> id = Integer.parseInt(start.getAttributeByName(idName).getValue());
                            case "title" -> title = reader.getElementText();
                            case "artist" -> artist = reader.getElementText();
                            case "seconds" -> seconds = Integer.parseInt(reader.getElementText().strip());
                            default -> { }
                        }
                    } else if (event.isEndElement()
                            && event.asEndElement().getName().getLocalPart().equals("song")) {
                        songs.add(new Song(id, title, artist, seconds));
                    }
                }
            } finally {
                reader.close();
            }
        }
        return songs;
    }
    static XMLInputFactory secureFactory() {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        return factory;
    }
    static String textOf(XMLInputFactory factory, String xml) throws XMLStreamException {
        XMLStreamReader reader = factory.createXMLStreamReader(new StringReader(xml));
        StringBuilder text = new StringBuilder();
        while (reader.hasNext()) {
            if (reader.next() == XMLStreamConstants.CHARACTERS) {
                text.append(reader.getText());
            }
        }
        reader.close();
        return text.toString();
    }
    static Optional<String> titleOf(Path file, String wantedId) throws IOException, XMLStreamException {
        try (InputStream in = Files.newInputStream(file)) {
            XMLStreamReader reader = secureFactory().createXMLStreamReader(in);
            try {
                while (reader.hasNext()) {
                    if (reader.next() == XMLStreamConstants.START_ELEMENT
                            && reader.getLocalName().equals("song")
                            && wantedId.equals(reader.getAttributeValue(null, "id"))) {
                        reader.nextTag();                         // moves to <title>
                        return Optional.of(reader.getElementText());
                    }
                }
                return Optional.empty();
            } finally {
                reader.close();
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);

            List<String> titles = new ArrayList<>();
            try (InputStream in = Files.newInputStream(Path.of("playlist.xml"))) {
                XMLStreamReader reader = factory.createXMLStreamReader(in);
                while (reader.hasNext()) {
                    if (reader.next() == XMLStreamConstants.START_ELEMENT
                            && reader.getLocalName().equals("title")) {
                        titles.add(reader.getElementText());
                    }
                }
                reader.close();
            }
            int count = titles.size();                    // 3
            show("count", count);
            String first = titles.getFirst();             // "Morning Run"
            show("first", first);
        }
        {
            List<Song> songs = readWithCursor(Path.of("playlist.xml"));
            int count = songs.size();                                          // 3
            show("count", count);
            Song first = songs.getFirst();                                     // Song[id=1, title=Morning Run, artist=Lena, seconds=215]
            show("first", first);
            int totalSeconds = songs.stream().mapToInt(Song::seconds).sum();   // 642
            show("totalSeconds", totalSeconds);
        }
        {
            XMLStreamReader reader = secureFactory().createXMLStreamReader(new StringReader("<song>Late Train</song>"));
            int firstEvent = reader.next();                     // 1 (START_ELEMENT)
            show("firstEvent", firstEvent);
            int secondEvent = reader.next();                    // 4 (CHARACTERS)
            show("secondEvent", secondEvent);
            String text = reader.getText();                     // "Late Train"
            show("text", text);
            try { String name = reader.getLocalName(); show("name", name); } catch (Throwable _t) { System.out.println("name -> " + _t); }
        }
        {
            List<Song> songs = readWithIterator(Path.of("playlist.xml"));
            int count = songs.size();                     // 3
            show("count", count);
            String lastTitle = songs.getLast().title();   // "Blue Sky"
            show("lastTitle", lastTitle);
        }
        {
            List<String> ids = new ArrayList<>();
            try (InputStream in = Files.newInputStream(Path.of("playlist.xml"))) {
                XMLInputFactory factory = secureFactory();
                XMLEventReader songsOnly = factory.createFilteredReader(factory.createXMLEventReader(in),
                e -> e.isStartElement() && e.asStartElement().getName().getLocalPart().equals("song"));
                while (songsOnly.hasNext()) {
                    StartElement song = songsOnly.nextEvent().asStartElement();
                    ids.add(song.getAttributeByName(new QName("id")).getValue());
                }
                songsOnly.close();
            }
            String allIds = String.join(",", ids);        // "1,2,3"
            show("allIds", allIds);
        }
        {
            String untrusted = """
            <?xml version="1.0"?>
            <!DOCTYPE song [<!ENTITY x SYSTEM "file:secret.txt">]>
            <song>&x;</song>""";
            String leaked = textOf(XMLInputFactory.newInstance(), untrusted);   // the content of secret.txt
            show("leaked", leaked);
            try { String blocked = textOf(secureFactory(), untrusted); show("blocked", blocked); } catch (Throwable _t) { System.out.println("blocked -> " + _t); }
        }
        {
            Optional<String> second = titleOf(Path.of("playlist.xml"), "2");    // Optional[Late Train]
            show("second", second);
            Optional<String> missing = titleOf(Path.of("playlist.xml"), "9");   // Optional.empty
            show("missing", missing);
        }
        {
            StringWriter out = new StringWriter();
            XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(out);
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("playlist");
            writer.writeAttribute("name", "Gym");
            writer.writeStartElement("song");
            writer.writeAttribute("id", "4");
            writer.writeStartElement("title");
            writer.writeCharacters("Rock & Roll");
            writer.writeEndElement();
            writer.writeEndDocument();                    // closes <song> and <playlist>
            writer.close();
            String xml = out.toString();                  // <?xml version="1.0" encoding="UTF-8"?><playlist name="Gym"><song id="4"><title>Rock &amp; Roll</title></song></playlist>
            show("xml", xml);
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
