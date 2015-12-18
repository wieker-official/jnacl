package com.neilalexander.jnacl.crypto.auth;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static com.neilalexander.jnacl.NaCl.asHex;
import static org.fest.assertions.Assertions.assertThat;

public class hmacsha256Test {

  private static byte[] secret = "0cba66066896ffb51e92bc3c36ffa627".getBytes();
  private static byte[] message = "a secret message".getBytes();
  private Random simpleRandom;

  @BeforeMethod
  public void setUp() throws Exception {
    simpleRandom = new Random(System.currentTimeMillis());
  }

  @Test
  public void happy_path() throws Exception {
    byte[] buf = new byte[32];
    hmacsha256.crypto_auth(buf, message, message.length, secret);

    assertThat(asHex(buf)).isEqualTo("04e1a5d1edd8585c7c5acd6e487f336a8ed50de2ddb6946dad8ee26bce6dd54c");
  }

  /**
   * "Test Case AUTH256-4" from RFC 4868 (source: cnacl/tests/auth2.c)
   */
  @Test
  public void auth2() {
    byte[] key = new byte[]{
        0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08
        , 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10
        , 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18
        , 0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f, 0x20
    };

    byte[] c = new byte[]{
        (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd
        , (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd
        , (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd
        , (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd
        , (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd
        , (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd
        , (byte) 0xcd, (byte) 0xcd
    };

    byte[] a = new byte[32];

    hmacsha256.crypto_auth(a, c, c.length, key);

    assertThat(a).isEqualTo(new byte[]{
        (byte) 0x37, (byte) 0x2e, (byte) 0xfc, (byte) 0xf9, (byte) 0xb4, (byte) 0x0b, (byte) 0x35, (byte) 0xc2
        , (byte) 0x11, (byte) 0x5b, (byte) 0x13, (byte) 0x46, (byte) 0x90, (byte) 0x3d, (byte) 0x2e, (byte) 0xf4
        , (byte) 0x2f, (byte) 0xce, (byte) 0xd4, (byte) 0x6f, (byte) 0x08, (byte) 0x46, (byte) 0xe7, (byte) 0x25
        , (byte) 0x7b, (byte) 0xb1, (byte) 0x56, (byte) 0xd3, (byte) 0xd7, (byte) 0xb3, (byte) 0x0d, (byte) 0x3f
    });
  }

  /**
   * "Test Case AUTH256-4" from RFC 4868 (source: cnacl/tests/auth3.c)
   */
  @Test
  public void auth3() {
    byte[] key = new byte[]{
        (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08
        , (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f, (byte) 0x10
        , (byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15, (byte) 0x16, (byte) 0x17, (byte) 0x18
        , (byte) 0x19, (byte) 0x1a, (byte) 0x1b, (byte) 0x1c, (byte) 0x1d, (byte) 0x1e, (byte) 0x1f, (byte) 0x20
    };

    byte[] c = new byte[]{
        (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd
        , (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd
        , (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd
        , (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd
        , (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd
        , (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd, (byte) 0xcd
        , (byte) 0xcd, (byte) 0xcd
    };

    byte[] a = new byte[]{
        (byte) 0x37, (byte) 0x2e, (byte) 0xfc, (byte) 0xf9, (byte) 0xb4, (byte) 0x0b, (byte) 0x35, (byte) 0xc2
        , (byte) 0x11, (byte) 0x5b, (byte) 0x13, (byte) 0x46, (byte) 0x90, (byte) 0x3d, (byte) 0x2e, (byte) 0xf4
        , (byte) 0x2f, (byte) 0xce, (byte) 0xd4, (byte) 0x6f, (byte) 0x08, (byte) 0x46, (byte) 0xe7, (byte) 0x25
        , (byte) 0x7b, (byte) 0xb1, (byte) 0x56, (byte) 0xd3, (byte) 0xd7, (byte) 0xb3, (byte) 0x0d, (byte) 0x3f
    };

    int valid = hmacsha256.crypto_auth_verify(a, c, c.length, key);
    assertThat(valid).isEqualTo(0);
  }

  @Test
  public void compare_with_standard_java_runtime_hmac_implementation_when_size_zero() throws InvalidKeyException, NoSuchAlgorithmException {
    byte[] key = new byte[]{
        0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08
        , 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10
        , 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18
        , 0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f, 0x20
    };

    Mac jreMac = createMacFromJavaxCryptoProvider(key);

    byte[] buf = new byte[32];
    hmacsha256.crypto_auth(buf, new byte[]{}, 0, key);

    assertThat(buf).isEqualTo(jreMac.doFinal());
  }

  @Test(invocationCount = 1000)
  public void compare_with_standard_java_runtime_hmac_implementation_with_varying_buffer_sizes() throws InvalidKeyException, NoSuchAlgorithmException {
    byte[] key = new byte[]{
        0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08
        , 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10
        , 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18
        , 0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f, 0x20
    };

    byte[] testData = createTestData();

    Mac jreMac = createMacFromJavaxCryptoProvider(key);
    jreMac.update(testData);

    byte[] buf = new byte[32];
    hmacsha256.crypto_auth(buf, testData, testData.length, key);

    assertThat(buf).isEqualTo(jreMac.doFinal());
  }

  private byte[] createTestData() {
    int smallest_prime_number_with_5_digits = 10007;
    int size = simpleRandom.nextInt(smallest_prime_number_with_5_digits);
    byte[] testData = new byte[size];
    simpleRandom.nextBytes(testData);
    return testData;
  }

  private Mac createMacFromJavaxCryptoProvider(byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    SecretKeySpec secret_key = new SecretKeySpec(key, "HmacSHA256");
    sha256_HMAC.init(secret_key);
    return sha256_HMAC;
  }

}