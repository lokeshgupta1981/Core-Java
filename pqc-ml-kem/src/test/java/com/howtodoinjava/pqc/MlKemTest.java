package com.howtodoinjava.pqc;

import java.security.AsymmetricKey;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.spec.NamedParameterSpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.AEADBadTagException;
import javax.crypto.DecapsulateException;
import javax.crypto.KEM;
import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MlKemTest {

  @Test
  void whenGeneratingWithFamilyName_thenDefaultParameterSetIsMlKem768() throws Exception {
    KeyPair receiver = KeyPairGenerator.getInstance("ML-KEM").generateKeyPair();

    NamedParameterSpec params = (NamedParameterSpec) ((AsymmetricKey) receiver.getPublic()).getParams();
    assertThat(receiver.getPublic().getAlgorithm()).isEqualTo("ML-KEM");
    assertThat(params.getName()).isEqualTo("ML-KEM-768");
  }

  @Test
  void whenComparingParameterSets_thenSizesGrowWithSecurityLevel() throws Exception {
    for (String set : new String[] {"ML-KEM-512", "ML-KEM-768", "ML-KEM-1024"}) {
      KeyPair keys = KeyPairGenerator.getInstance(set).generateKeyPair();
      KEM.Encapsulator encapsulator = KEM.getInstance("ML-KEM").newEncapsulator(keys.getPublic());
      System.out.printf("%s public key %d bytes, encapsulation %d bytes, secret %d bytes%n",
          set, keys.getPublic().getEncoded().length, encapsulator.encapsulationSize(), encapsulator.secretSize());
    }
    KeyPair keys = KeyPairGenerator.getInstance("ML-KEM-1024").generateKeyPair();
    KEM.Encapsulator encapsulator = KEM.getInstance("ML-KEM").newEncapsulator(keys.getPublic());
    assertThat(encapsulator.encapsulationSize()).isEqualTo(1568);
    assertThat(encapsulator.secretSize()).isEqualTo(32);
  }

  @Test
  void givenReceiverKeyPair_whenEncapsulatingAndDecapsulating_thenBothSidesShareSecret() throws Exception {
    KeyPair receiver = KeyPairGenerator.getInstance("ML-KEM").generateKeyPair();
    KEM kem = KEM.getInstance("ML-KEM");

    KEM.Encapsulated encapsulated = kem.newEncapsulator(receiver.getPublic()).encapsulate();
    byte[] message = encapsulated.encapsulation();

    SecretKey receiverSecret = kem.newDecapsulator(receiver.getPrivate()).decapsulate(message);

    assertThat(message).hasSize(1088);
    assertThat(receiverSecret.getEncoded()).isEqualTo(encapsulated.key().getEncoded()).hasSize(32);
  }

  @Test
  void givenEncodedPublicKey_whenDecoding_thenKeyIsEqual() throws Exception {
    KeyPair receiver = KeyPairGenerator.getInstance("ML-KEM").generateKeyPair();
    byte[] encoded = receiver.getPublic().getEncoded();

    PublicKey decoded = KeyFactory.getInstance("ML-KEM").generatePublic(new X509EncodedKeySpec(encoded));

    assertThat(receiver.getPublic().getFormat()).isEqualTo("X.509");
    assertThat(decoded).isEqualTo(receiver.getPublic());
  }

  @Test
  void givenSharedSecret_whenSealingAndOpening_thenMessageRoundTrips() throws Exception {
    KeyPair receiver = KeyPairGenerator.getInstance("ML-KEM").generateKeyPair();
    MessageSealer sealer = new MessageSealer();

    SealedMessage sealed = sealer.seal(receiver.getPublic(), "Invoice 4711 is paid");

    assertThat(sealer.open(receiver.getPrivate(), sealed)).isEqualTo("Invoice 4711 is paid");
  }

  @Test
  void whenInitializingWithKeySize_thenInvalidParameterException() throws Exception {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("ML-KEM");

    assertThatThrownBy(() -> generator.initialize(768))
        .isInstanceOf(InvalidParameterException.class)
        .hasMessage("keysize not supported");
  }

  @Test
  void whenInitializingWithNamedParameterSpec_thenKeyUsesThatSet() throws Exception {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("ML-KEM");
    generator.initialize(NamedParameterSpec.ML_KEM_1024);

    AsymmetricKey key = (AsymmetricKey) generator.generateKeyPair().getPublic();

    assertThat(((NamedParameterSpec) key.getParams()).getName()).isEqualTo("ML-KEM-1024");
  }

  @Test
  void givenMlKem768Key_whenKemIsPinnedTo512_thenInvalidKeyException() throws Exception {
    KeyPair receiver = KeyPairGenerator.getInstance("ML-KEM-768").generateKeyPair();

    assertThatThrownBy(() -> KEM.getInstance("ML-KEM-512").newEncapsulator(receiver.getPublic()))
        .isInstanceOf(InvalidKeyException.class)
        .hasMessage("Unsupported parameter set name: ML-KEM-768");
  }

  @Test
  void givenTamperedEncapsulation_whenDecapsulating_thenSecretDiffersWithoutException() throws Exception {
    KeyPair receiver = KeyPairGenerator.getInstance("ML-KEM").generateKeyPair();
    KEM kem = KEM.getInstance("ML-KEM");
    KEM.Encapsulated encapsulated = kem.newEncapsulator(receiver.getPublic()).encapsulate();

    byte[] tampered = encapsulated.encapsulation().clone();
    tampered[0] ^= 1;
    SecretKey wrong = kem.newDecapsulator(receiver.getPrivate()).decapsulate(tampered);

    assertThat(wrong.getEncoded()).hasSize(32).isNotEqualTo(encapsulated.key().getEncoded());
  }

  @Test
  void givenTamperedSealedMessage_whenOpening_thenAeadTagCheckFails() throws Exception {
    KeyPair receiver = KeyPairGenerator.getInstance("ML-KEM").generateKeyPair();
    MessageSealer sealer = new MessageSealer();
    SealedMessage sealed = sealer.seal(receiver.getPublic(), "Invoice 4711 is paid");

    byte[] tampered = sealed.encapsulation().clone();
    tampered[0] ^= 1;
    SealedMessage attacked = new SealedMessage(tampered, sealed.iv(), sealed.ciphertext());

    assertThatThrownBy(() -> sealer.open(receiver.getPrivate(), attacked))
        .isInstanceOf(AEADBadTagException.class)
        .hasMessage("Tag mismatch");
  }

  @Test
  void givenTruncatedEncapsulation_whenDecapsulating_thenDecapsulateException() throws Exception {
    KeyPair receiver = KeyPairGenerator.getInstance("ML-KEM").generateKeyPair();

    assertThatThrownBy(() -> KEM.getInstance("ML-KEM").newDecapsulator(receiver.getPrivate()).decapsulate(new byte[100]))
        .isInstanceOf(DecapsulateException.class)
        .hasMessage("Invalid key encapsulation message length");
  }
}
