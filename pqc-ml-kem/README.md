Source code for the article https://howtodoinjava.com/?p=44418

# Post-Quantum Cryptography in Java: ML-KEM and Hybrid TLS

Runnable examples for ML-KEM key encapsulation with the `javax.crypto.KEM` API (Java 24+), encrypting a message with the shared secret and AES-GCM, the common ML-KEM pitfalls, and hybrid `X25519MLKEM768` key exchange in TLS 1.3 (JDK 27, JEP 527). Every snippet in the article has a JUnit test.

## Versions

- JDK 25 for the ML-KEM examples (tested with OpenJDK 25.0.4.1)
- JDK 27 for the hybrid TLS examples (tested with Amazon Corretto 27.0.1.0.11)
- Maven 3.9.11
- JUnit 6.1.3, AssertJ 3.27.7
- maven-compiler-plugin 3.16.0, maven-surefire-plugin 3.6.0

## Run

```bash
# on JDK 25: ML-KEM tests run, JDK 27 TLS tests are skipped, Java 25 TLS tests run
mvn test

# on JDK 27: ML-KEM tests and the hybrid TLS tests run
JAVA_HOME=/path/to/jdk-27 mvn test

# see the negotiated group in the handshake log of any Java application
java -Djavax.net.debug=ssl:handshake -jar app.jar

# list the enabled named groups, with and without an override
java -XshowSettings:security:tls -version
java -Djdk.tls.namedGroups=x25519,secp256r1 -XshowSettings:security:tls -version
```

## Files

| File | What it shows |
|---|---|
| `SealedMessage` | Record with the encapsulation, IV and ciphertext sent to the receiver |
| `MessageSealer` | `seal()` and `open()`: ML-KEM shared secret used as an AES-256-GCM key |
| `LocalTls` | TLS 1.3 server and client in one JVM with a self-signed certificate; one handshake per call |
| `MlKemTest` | Key generation, parameter sets and sizes, encapsulate and decapsulate, X.509 encoding, pitfalls |
| `HybridTlsTest` | Default named groups, a hybrid-only server, fallback to x25519, Java 25 vs JDK 27 behavior |

`HybridTlsTest` creates its keystore with `keytool` in a temporary folder, so no key material is checked in.
