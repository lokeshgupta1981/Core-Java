package com.howtodoinjava.iae;

import java.util.Base64;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;

/**
 * Safe versions of JDK calls that throw IllegalArgumentException on bad input.
 */
public final class SafeInputs {

  private SafeInputs() {
  }

  /** Integer.parseInt() throws NumberFormatException, a subclass of IllegalArgumentException. */
  public static OptionalInt parseQuantity(String text) {
    if (text == null) {
      return OptionalInt.empty();
    }
    try {
      return OptionalInt.of(Integer.parseInt(text.strip()));
    } catch (NumberFormatException e) {
      return OptionalInt.empty();
    }
  }

  /** UUID.fromString() throws IllegalArgumentException for a malformed string. */
  public static Optional<UUID> parseUuid(String text) {
    if (text == null) {
      return Optional.empty();
    }
    try {
      return Optional.of(UUID.fromString(text.strip()));
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }

  /** Base64 decoders throw IllegalArgumentException for characters outside the alphabet. */
  public static Optional<byte[]> decodeBase64(String text) {
    if (text == null) {
      return Optional.empty();
    }
    try {
      return Optional.of(Base64.getDecoder().decode(text.strip()));
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }

  /** A negative initial capacity throws IllegalArgumentException, so clamp it to zero. */
  public static int safeCapacity(int requested) {
    return Math.max(0, requested);
  }
}
