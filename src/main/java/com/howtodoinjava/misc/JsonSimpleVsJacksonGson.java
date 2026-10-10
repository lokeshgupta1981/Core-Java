package com.howtodoinjava.misc;

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
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Examples for the tutorial "JSON.simple: Read and Write JSON in Java (and Alternatives)".
 * https://howtodoinjava.com/java/library/json-simple-read-write-json-examples/
 */
public class JsonSimpleVsJacksonGson {
    @SuppressWarnings("unchecked")
    static JSONObject song(String title, String artist, int seconds) {
        JSONObject song = new JSONObject();
        song.put("title", title);
        song.put("artist", artist);
        song.put("seconds", seconds);
        return song;
    }
    static record Album(String name) {}
    static record Song(String title, String artist, int seconds) {}
    static record Playlist(String name, List<Song> songs) {}
    public static void main(String[] args) throws Exception {
        {
            JSONObject song = new JSONObject();
            song.put("title", "Yesterday");
            song.put("seconds", 125);
            String json = song.toJSONString();               // {"seconds":125,"title":"Yesterday"}
            show("json", json);

            JSONObject parsed = (JSONObject) new JSONParser().parse(json);
            String title = (String) parsed.get("title");     // "Yesterday"
            show("title", title);
            long seconds = (Long) parsed.get("seconds");     // 125
            show("seconds", seconds);
        }
        {
            JSONArray songs = new JSONArray();
            songs.add(song("Bohemian Rhapsody", "Queen", 354));
            songs.add(song("Yesterday", "The Beatles", 125));
            JSONObject playlist = new JSONObject();
            playlist.put("name", "Road trip");
            playlist.put("songs", songs);
            try (Writer out = Files.newBufferedWriter(Path.of("playlist.json"))) {
                playlist.writeJSONString(out);
            }
            long size = Files.size(Path.of("playlist.json"));   // 150 bytes, written on one line
            show("size", size);
        }
        {
            JSONObject root;
            try (Reader in = Files.newBufferedReader(Path.of("playlist.json"))) {
                root = (JSONObject) new JSONParser().parse(in);
            }
            String name = (String) root.get("name");                 // "Road trip"
            show("name", name);
            JSONArray list = (JSONArray) root.get("songs");
            int count = list.size();                                 // 2
            show("count", count);
            JSONObject first = (JSONObject) list.get(0);
            String artist = (String) first.get("artist");            // "Queen"
            show("artist", artist);
            long length = (Long) first.get("seconds");               // 354
            show("length", length);
            Object missing = first.get("album");                     // null
            show("missing", missing);
        }
        {
            String error;
            try {
                new JSONParser().parse("{\"title\": }");
                error = "valid";
            } catch (ParseException e) {
                error = e.getClass().getSimpleName() + ": " + e;
            }
            try { String message = error; show("message", message); } catch (Throwable _t) { System.out.println("message -> " + _t); }
        }
        {
            JSONObject track = (JSONObject) new JSONParser().parse("{\"seconds\":125,\"rating\":4.5}");
            try { Integer wrong = (Integer) track.get("seconds"); show("wrong", wrong); } catch (Throwable _t) { System.out.println("wrong -> " + _t); }
            int seconds = ((Number) track.get("seconds")).intValue();   // 125
            show("seconds", seconds);
            double rating = ((Number) track.get("rating")).doubleValue(); // 4.5
            show("rating", rating);
        }
        {
            JSONObject bad = new JSONObject();
            bad.put("album", new Album("Help!"));
            String invalid = bad.toJSONString();                        // {"album":Album[name=Help!]}
            show("invalid", invalid);
        }
        {
            Object nothing = JSONValue.parse("[1, 2");                 // null, no exception
            show("nothing", nothing);
            String url = JSONValue.toJSONString("https://example.com/a"); // "https:\/\/example.com\/a"
            show("url", url);
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
