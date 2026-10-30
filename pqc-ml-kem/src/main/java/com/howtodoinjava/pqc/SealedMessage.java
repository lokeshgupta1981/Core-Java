package com.howtodoinjava.pqc;

public record SealedMessage(byte[] encapsulation, byte[] iv, byte[] ciphertext) {
}
