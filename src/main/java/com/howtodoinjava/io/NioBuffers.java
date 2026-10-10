package com.howtodoinjava.io;

import java.nio.*;
import java.nio.channels.*;
import java.util.zip.*;
import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

/**
 * Examples for the tutorial "Java NIO Buffer Tutorial: ByteBuffer flip(), clear(), compact()".
 * https://howtodoinjava.com/java/nio/java-nio-2-0-working-with-buffers/
 */
public class NioBuffers {
    static long copyFile(Path source, Path target) throws IOException {
        try (FileChannel in = FileChannel.open(source, StandardOpenOption.READ);
        FileChannel out = FileChannel.open(target, StandardOpenOption.WRITE,
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            ByteBuffer buffer = ByteBuffer.allocateDirect(4 * 1024);
            long total = 0;
            while (in.read(buffer) != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    total += out.write(buffer);
                }
                buffer.clear();
            }
            return total;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.put((byte) 'H').put((byte) 'i');
            int filled = buffer.position();                 // 2
            show("filled", filled);
            buffer.flip();                                  // switch to reading
            int limit = buffer.limit();                     // 2
            show("limit", limit);
            byte first = buffer.get();                      // 72, the code of 'H'
            show("first", first);
            buffer.compact();                               // keep the unread 'i'
            int next = buffer.position();                   // 1
            show("next", next);
            buffer.clear();                                 // empty again
            int capacity = buffer.capacity();               // 8
            show("capacity", capacity);
        }
        {
            CharBuffer allocated = CharBuffer.allocate(100);
            char[] myArray = new char[100];
            CharBuffer wrapped = CharBuffer.wrap(myArray);
            CharBuffer partial = CharBuffer.wrap(myArray, 12, 42);
            int position = partial.position();              // 12
            show("position", position);
            int limit = partial.limit();                    // 54
            show("limit", limit);
            int capacity = partial.capacity();              // 100
            show("capacity", capacity);
            CharBuffer text = CharBuffer.wrap("hello");     // read-only view of the String
            show("text", text);
        }
        {
            ByteBuffer direct = ByteBuffer.allocateDirect(16);
            boolean hasArray = direct.hasArray();           // false
            show("hasArray", hasArray);
            try { byte[] bytes = direct.array(); show("bytes", bytes); } catch (Throwable _t) { System.out.println("bytes -> " + _t); }
        }
        {
            ByteBuffer small = ByteBuffer.allocate(2);
            small.put((byte) 1).put((byte) 2);
            try { ByteBuffer full = small.put((byte) 3); show("full", full); } catch (Throwable _t) { System.out.println("full -> " + _t); }
        }
        {
            ByteBuffer small = ByteBuffer.allocate(2);
            try { byte outside = small.get(5); show("outside", outside); } catch (Throwable _t) { System.out.println("outside -> " + _t); }
        }
        {
            CharBuffer buffer = CharBuffer.allocate(10);
            buffer.put('H').put('e').put('l').put('l').put('o');
            int position = buffer.position();               // 5
            show("position", position);
        }
        {
            CharBuffer buffer = CharBuffer.allocate(10);
            buffer.put('H').put('e').put('l').put('l').put('o');
            buffer.put(0, 'M').put('w');
            int position = buffer.position();               // 6
            show("position", position);
            String word = buffer.flip().toString();         // "Mellow"
            show("word", word);
        }
        {
            CharBuffer buffer = CharBuffer.allocate(10);
            buffer.put("Mellow");
            buffer.limit(buffer.position()).position(0);    // same as flip()
            int limit = buffer.limit();                     // 6
            show("limit", limit);
            int position = buffer.position();               // 0
            show("position", position);
        }
        {
            CharBuffer buffer = CharBuffer.wrap("abc");
            CharBuffer twice = buffer.flip().flip();
            int limit = twice.limit();                      // 0
            show("limit", limit);
            try { char c = twice.get(); show("c", c); } catch (Throwable _t) { System.out.println("c -> " + _t); }
        }
        {
            CharBuffer buffer = CharBuffer.allocate(10);
            buffer.put("Mellow").flip();
            StringBuilder out = new StringBuilder();
            while (buffer.hasRemaining()) {
                out.append(buffer.get());
            }
            String drained = out.toString();                // "Mellow"
            show("drained", drained);
            int left = buffer.remaining();                  // 0
            show("left", left);
        }
        {
            ByteBuffer inbox = ByteBuffer.allocate(16);
            inbox.put(new byte[]{2, 'h', 'i', 3, 'y'});     // one full message, one partial
            inbox.flip();
            int length = inbox.get();                       // 2
            show("length", length);
            byte[] text = new byte[length];
            inbox.get(text);
            String message = new String(text, StandardCharsets.US_ASCII);   // "hi"
            show("message", message);
            inbox.compact();
            int kept = inbox.position();                    // 2, keeps 3 and y
            show("kept", kept);
        }
        {
            CharBuffer buffer = CharBuffer.wrap("key=value");
            buffer.position(4).mark();
            char v = buffer.get();                          // 'v'
            show("v", v);
            buffer.reset();
            int back = buffer.position();                   // 4
            show("back", back);
            buffer.clear();
            try { CharBuffer again = buffer.reset(); show("again", again); } catch (Throwable _t) { System.out.println("again -> " + _t); }
        }
        {
            ByteBuffer a = ByteBuffer.wrap(new byte[]{9, 1, 2, 3});
            ByteBuffer b = ByteBuffer.wrap(new byte[]{1, 2, 3});
            a.get();                                        // skip the 9
            boolean same = a.equals(b);                     // true
            show("same", same);
            ByteBuffer c = ByteBuffer.wrap(new byte[]{1, 2, 4});
            int order = b.compareTo(c);                     // -1, b is smaller
            show("order", order);
            int firstDiff = b.mismatch(c);                  // 2
            show("firstDiff", firstDiff);
        }
        {
            CharBuffer buffer = CharBuffer.wrap("abcdefghij");
            char[] bigArray = new char[1000];
            int length = buffer.remaining();                // 10
            show("length", length);
            buffer.get(bigArray, 0, length);
            String copied = new String(bigArray, 0, length);   // "abcdefghij"
            show("copied", copied);
        }
        {
            CharBuffer buffer = CharBuffer.wrap("abcdefghij");
            char[] smallArray = new char[4];
            List<String> chunks = new ArrayList<>();
            while (buffer.hasRemaining()) {
                int length = Math.min(buffer.remaining(), smallArray.length);
                buffer.get(smallArray, 0, length);
                chunks.add(new String(smallArray, 0, length));
            }
            List<String> result = chunks;                   // [abcd, efgh, ij]
            show("result", result);
        }
        {
            ByteBuffer packet = ByteBuffer.allocate(8);
            packet.position(2).put(new byte[]{7, 7, 7});
            packet.put(0, new byte[]{0, 3});                // write the length header last
            int position = packet.position();               // 5
            show("position", position);
            byte header = packet.get(1);                    // 3
            show("header", header);
        }
        {
            ByteBuffer big = ByteBuffer.allocate(4).putInt(1);
            byte firstBig = big.get(0);                     // 0
            show("firstBig", firstBig);
            ByteBuffer little = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(1);
            byte firstLittle = little.get(0);               // 1
            show("firstLittle", firstLittle);
            ByteOrder defaultOrder = ByteBuffer.allocate(4).order();   // BIG_ENDIAN
            show("defaultOrder", defaultOrder);
        }
        {
            ByteBuffer bytes = ByteBuffer.allocate(8).putInt(10).putInt(20).flip();
            IntBuffer ints = bytes.asIntBuffer();
            int count = ints.remaining();                   // 2
            show("count", count);
            int second = ints.get(1);                       // 20
            show("second", second);
        }
        {
            CharBuffer buffer = CharBuffer.allocate(8);
            buffer.put("abcdefgh");
            buffer.position(3).limit(5);
            CharBuffer slice = buffer.slice();
            int sliceCapacity = slice.capacity();           // 2
            show("sliceCapacity", sliceCapacity);
            String sliceText = slice.toString();            // "de"
            show("sliceText", sliceText);
            CharBuffer range = buffer.slice(1, 3);
            String rangeText = range.toString();            // "bcd"
            show("rangeText", rangeText);
        }
        {
            ByteBuffer original = ByteBuffer.allocate(4);
            ByteBuffer readOnly = original.asReadOnlyBuffer();
            original.put(0, (byte) 9);
            byte seen = readOnly.get(0);                    // 9
            show("seen", seen);
            try { ByteBuffer fails = readOnly.put((byte) 1); show("fails", fails); } catch (Throwable _t) { System.out.println("fails -> " + _t); }
        }
        {
            ByteBuffer encoded = StandardCharsets.UTF_8.encode("How to do in Java");
            int byteCount = encoded.remaining();            // 17
            show("byteCount", byteCount);
            String decoded = StandardCharsets.UTF_8.decode(encoded).toString();   // "How to do in Java"
            show("decoded", decoded);
        }
        {
            Path source = Files.write(Files.createTempFile("photo", ".png"), new byte[10_000]);
            Path target = source.resolveSibling("photo-copy.png");
            long copied = copyFile(source, target);         // 10000
            show("copied", copied);
            long size = Files.size(target);                 // 10000
            show("size", size);
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
