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
import java.net.URI;
import java.net.http.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Examples for the tutorial "WireMock Tutorial: Mock REST APIs in Java with WireMock 3".
 * https://howtodoinjava.com/java/library/mock-rest-api-with-wiremock/
 */
public class WiremockStubRequestMatching {
    static class ParcelClient {

        private final HttpClient http = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        private final String baseUrl;
        private final Duration timeout;

        ParcelClient(String baseUrl, Duration timeout) {
            this.baseUrl = baseUrl;
            this.timeout = timeout;
        }

        String track(String parcelId) throws IOException, InterruptedException {
            HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + "/parcels/" + parcelId))
                    .header("Accept", "application/json")
                    .timeout(timeout)
                    .GET()
                    .build();
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IllegalStateException("Tracking failed with status " + response.statusCode());
            }
            return response.body();
        }
    }
    public static void main(String[] args) throws Exception {
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
