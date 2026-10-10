package com.howtodoinjava.core.basic;

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
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Examples for the tutorial "Little Endian vs Big Endian in Java with ByteBuffer Examples".
 * https://howtodoinjava.com/java/basics/little-endian-big-endian/
 */
public class LittleEndianVsBigEndian {
    static byte[] wavHeader(int sampleRate, short channels, short bitsPerSample) {
        int blockAlign = channels * bitsPerSample / 8;
        return ByteBuffer.allocate(44).order(ByteOrder.LITTLE_ENDIAN)
                .put("RIFF".getBytes(StandardCharsets.US_ASCII)).putInt(36)
                .put("WAVEfmt ".getBytes(StandardCharsets.US_ASCII)).putInt(16)
                .putShort((short) 1).putShort(channels)
                .putInt(sampleRate).putInt(sampleRate * blockAlign)
                .putShort((short) blockAlign).putShort(bitsPerSample)
                .put("data".getBytes(StandardCharsets.US_ASCII)).putInt(0)
                .array();
    }
    static int readSampleRate(Path wavFile) throws IOException {
        try (InputStream in = Files.newInputStream(wavFile)) {
            byte[] header = in.readNBytes(44);
            String riff = new String(header, 0, Math.min(4, header.length), StandardCharsets.US_ASCII);
            if (header.length < 44 || !riff.equals("RIFF")) {
                throw new IOException("Not a WAV file: " + wavFile);
            }
            return ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN).getInt(24);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            int value = 0x12345678;
            HexFormat hex = HexFormat.ofDelimiter(" ");

            String big = hex.formatHex(ByteBuffer.allocate(4).putInt(value).array());                      // "12 34 56 78"
            show("big", big);
            String little = hex.formatHex(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array());   // "78 56 34 12"
            show("little", little);
            ByteOrder cpu = ByteOrder.nativeOrder();                                                       // LITTLE_ENDIAN
            show("cpu", cpu);
        }
        {
            int number = 0x12345678;
            int highByte = number >>> 24;              // 18
            show("highByte", highByte);
            int lowByte = number & 0xFF;               // 120
            show("lowByte", lowByte);
        }
        {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bytes);
            out.writeInt(0x12345678);
            String written = HexFormat.ofDelimiter(" ").formatHex(bytes.toByteArray());   // "12 34 56 78"
            show("written", written);
        }
        {
            byte[] data = {0x78, 0x56, 0x34, 0x12};

            int asBig = ByteBuffer.wrap(data).getInt();                                   // 2018915346
            show("asBig", asBig);
            int asLittle = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getInt(); // 305419896
            show("asLittle", asLittle);
            String check = Integer.toHexString(asLittle);                                 // "12345678"
            show("check", check);
        }
        {
            ByteBuffer source = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            ByteOrder sliced = source.slice().order();                    // BIG_ENDIAN
            show("sliced", sliced);
            ByteOrder view = source.asIntBuffer().order();                // LITTLE_ENDIAN
            show("view", view);
            ByteOrder fixed = source.slice().order(ByteOrder.LITTLE_ENDIAN).order();   // LITTLE_ENDIAN
            show("fixed", fixed);
        }
        {
            int swapped = Integer.reverseBytes(0x12345678);               // 2018915346
            show("swapped", swapped);
            String swappedHex = Integer.toHexString(swapped);             // "78563412"
            show("swappedHex", swappedHex);
            short swappedShort = Short.reverseBytes((short) 0x1234);      // 13330
            show("swappedShort", swappedShort);
        }
        {
            byte[] littleEndianFile = {0x44, (byte) 0xAC, 0x00, 0x00};
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(littleEndianFile));
            int rate = Integer.reverseBytes(in.readInt());                // 44100
            show("rate", rate);
        }
        {
            VarHandle intLittle = MethodHandles.byteArrayViewVarHandle(int[].class, ByteOrder.LITTLE_ENDIAN);
            byte[] frame = {0x78, 0x56, 0x34, 0x12, 0, 0, 0, 0};

            int first = (int) intLittle.get(frame, 0);                    // 305419896
            show("first", first);
            intLittle.set(frame, 4, 44100);
            String frameHex = HexFormat.ofDelimiter(" ").formatHex(frame);   // "78 56 34 12 44 ac 00 00"
            show("frameHex", frameHex);
        }
        {
            byte[] header = wavHeader(44100, (short) 2, (short) 16);
            int sampleRate = ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN).getInt(24);   // 44100
            show("sampleRate", sampleRate);
            short channels = ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN).getShort(22); // 2
            show("channels", channels);
            int wrongRate = ByteBuffer.wrap(header).getInt(24);                                   // 1152122880
            show("wrongRate", wrongRate);

            Path upload = Files.write(Path.of("upload.wav"), wavHeader(48000, (short) 1, (short) 16));
            int uploadRate = readSampleRate(upload);                                              // 48000
            show("uploadRate", uploadRate);
        }
        {
            byte[] packet = {0x00, 0x05, 'h', 'e', 'l', 'l', 'o'};
            DataInputStream socketIn = new DataInputStream(new ByteArrayInputStream(packet));
            int length = socketIn.readUnsignedShort();                    // 5
            show("length", length);
        }
        {
            ByteOrder platform = ByteOrder.nativeOrder();                 // LITTLE_ENDIAN
            show("platform", platform);
            ByteBuffer shared = ByteBuffer.allocateDirect(16).order(ByteOrder.nativeOrder());
            ByteOrder sharedOrder = shared.order();                       // LITTLE_ENDIAN
            show("sharedOrder", sharedOrder);
        }
        {
            byte[] raw = {0x01, 0x00, 0x00, 0x00};
            int one = ByteBuffer.wrap(raw).order(ByteOrder.LITTLE_ENDIAN).getInt();   // 1
            show("one", one);
        }
        {
            String utf16 = HexFormat.ofDelimiter(" ").formatHex("Hi".getBytes(StandardCharsets.UTF_16));      // "fe ff 00 48 00 69"
            show("utf16", utf16);
            String utf16le = HexFormat.ofDelimiter(" ").formatHex("Hi".getBytes(StandardCharsets.UTF_16LE));  // "48 00 69 00"
            show("utf16le", utf16le);
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
