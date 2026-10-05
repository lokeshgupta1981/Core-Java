package com.howtodoinjava.java25;

import com.howtodoinjava.java25.language.EBook;
import com.howtodoinjava.java25.language.ModuleImports;
import com.howtodoinjava.java25.language.OldStyleEBook;
import com.howtodoinjava.java25.language.PrimitivePatterns;
import com.howtodoinjava.java25.libraries.BookCatalog;
import com.howtodoinjava.java25.libraries.BookLookup;
import com.howtodoinjava.java25.libraries.KeyDerivation;
import com.howtodoinjava.java25.libraries.LibraryContext;
import com.howtodoinjava.java25.libraries.PemKeys;
import com.howtodoinjava.java25.libraries.VectorMath;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import javax.crypto.SecretKey;

/** Prints the output of every example in the article. */
public class AllFeatures {

  public static void main(String[] args) throws Exception {
    System.out.println("Java " + Runtime.version());

    System.out.println("== JEP 513 flexible constructor bodies");
    EBook dune = new EBook("Dune", 412, " epub ");
    System.out.println(dune.summary());
    System.out.println(new OldStyleEBook("Dune", 412, "epub").summary());
    try {
      new EBook("Emma", 0, "pdf");
    } catch (IllegalArgumentException e) {
      System.out.println("IllegalArgumentException: " + e.getMessage());
    }

    System.out.println("== JEP 511 module import declarations");
    System.out.println(ModuleImports.pagesByTitle(List.of("Ulysses", "Dune", "Emma")));
    System.out.println(ModuleImports.catalogFile());

    System.out.println("== JEP 506 scoped values");
    LibraryContext ctx = new LibraryContext();
    ctx.borrow("Lokesh", "Dune");
    ctx.log().forEach(System.out::println);
    System.out.println("discount: " + LibraryContext.memberDiscount());
    System.out.println("outside: " + LibraryContext.currentMemberOrGuest());
    try {
      LibraryContext.MEMBER.get();
    } catch (java.util.NoSuchElementException e) {
      System.out.println("NoSuchElementException: " + e.getMessage());
    }

    System.out.println("== JEP 510 key derivation");
    byte[] secret = "shared-secret".getBytes(StandardCharsets.UTF_8);
    byte[] salt = "library-salt".getBytes(StandardCharsets.UTF_8);
    SecretKey k1 = KeyDerivation.deriveAesKey(secret, salt, "loan-records");
    SecretKey k2 = KeyDerivation.deriveAesKey(secret, salt, "loan-records");
    SecretKey k3 = KeyDerivation.deriveAesKey(secret, salt, "member-emails");
    System.out.println(k1.getAlgorithm() + " " + k1.getEncoded().length * 8 + " bits " + KeyDerivation.hex(k1));
    System.out.println("same inputs, same key: " + Arrays.equals(k1.getEncoded(), k2.getEncoded()));
    System.out.println("other purpose, same key: " + Arrays.equals(k1.getEncoded(), k3.getEncoded()));

    System.out.println("== JEP 502 stable values (preview)");
    BookCatalog catalog = new BookCatalog();
    System.out.println("loaded before: " + catalog.isLoaded());
    System.out.println(catalog.pages().get("Dune") + " " + catalog.pages().get("Emma"));
    System.out.println("loads: " + catalog.loadCount() + ", loaded: " + catalog.isLoaded());
    System.out.println(catalog.shelves());
    String first = catalog.shelves().get(0);
    System.out.println(first + " -> " + catalog.shelves());

    System.out.println("== JEP 505 structured concurrency (preview)");
    System.out.println(new BookLookup(Duration.ofMillis(150), false).offer("Dune"));
    try {
      new BookLookup(Duration.ofMillis(50), true).offer("Dune");
    } catch (StructuredTaskScope.FailedException e) {
      System.out.println("FailedException caused by " + e.getCause());
    }
    try {
      new BookLookup(Duration.ofSeconds(5), false).offerWithTimeout("Dune", Duration.ofMillis(300));
    } catch (StructuredTaskScope.TimeoutException e) {
      System.out.println("TimeoutException after 300 ms");
    }
    System.out.println("subtask sees: " + new BookLookup(Duration.ZERO, false).memberSeenBySubtask());

    System.out.println("== JEP 507 primitive patterns (preview)");
    System.out.println(PrimitivePatterns.size(0) + ", " + PrimitivePatterns.size(80) + ", "
        + PrimitivePatterns.size(412) + ", " + PrimitivePatterns.size(1225));
    System.out.println(PrimitivePatterns.rating(5.0) + ", " + PrimitivePatterns.rating(4.2) + ", " + PrimitivePatterns.rating(2.5));
    System.out.println(PrimitivePatterns.availability(true) + ", " + PrimitivePatterns.availability(false));
    System.out.println(PrimitivePatterns.toIntSafely(150_000L) + ", " + PrimitivePatterns.toIntSafely(3_000_000_000L));
    System.out.println(PrimitivePatterns.fitsInByte(100) + " " + PrimitivePatterns.fitsInByte(300));

    System.out.println("== JEP 470 PEM encodings (preview)");
    KeyPair pair = PemKeys.newKeyPair();
    String pem = PemKeys.toPem(pair.getPublic());
    System.out.print(pem.endsWith("\n") ? pem : pem + "\n");
    System.out.println("round trip equal: " + PemKeys.fromPem(pem).equals(pair.getPublic()));
    String encrypted = PemKeys.toEncryptedPem(pair.getPrivate(), "s3cret".toCharArray());
    System.out.println(encrypted.lines().findFirst().orElseThrow());
    System.out.println("private key restored: "
        + Arrays.equals(PemKeys.fromEncryptedPem(encrypted, "s3cret".toCharArray()).getEncoded(), pair.getPrivate().getEncoded()));

    System.out.println("== JEP 508 Vector API (incubator)");
    float[] totals = VectorMath.multiply(new float[] {5, 3, 8, 2, 7}, new float[] {2, 4, 1, 3, 2});
    System.out.println("lanes: " + VectorMath.lanes() + ", totals: " + Arrays.toString(totals));
  }
}
