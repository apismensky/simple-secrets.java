package com.timshadel.simplesecrets;


import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.msgpack.template.Template;
import org.msgpack.type.Value;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

import static org.junit.Assert.*;
import static org.msgpack.template.Templates.*;


@RunWith(PowerMockRunner.class)
public class PrimitivesTest
{
  private static final byte[] DATA = hexStringToBytes("11", 25);
  private static final byte[] KEY = hexStringToBytes("cd", 32);
  private static final byte[] IV = hexStringToBytes("ab", 16);


  @Test
  public void constructor()
  {
    Object object = new Primitives();
    assertNotNull(object);
  }


  @Test
  public void test_nonce()
  {
    byte[] nonce = Primitives.nonce();
    assertEquals("Nonce is 16 bytes", 16, nonce.length);

    byte[] another_nonce = Primitives.nonce();
    assertFalse("Nonce is not repeated", Arrays.equals(nonce, another_nonce));
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_derive_sender_hmac_null_master_key()
          throws GeneralSecurityException
  {
    Primitives.derive_sender_hmac(null);
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_derive_sender_hmac_master_key_too_short()
          throws GeneralSecurityException
  {
    Primitives.derive_sender_hmac(hexStringToBytes("bc", 31));
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_derive_sender_hmac_master_key_too_long()
          throws GeneralSecurityException
  {
    Primitives.derive_sender_hmac(hexStringToBytes("bc", 33));
  }


  @Test
  public void test_derive_sender_hmac()
          throws GeneralSecurityException
  {
    byte[] master_key = hexStringToBytes("bc", 32);
    byte[] expected = hexStringToBytes("1e2e2725f135463f05c268ffd1c1687dbc9dd7da65405697471052236b3b3088");

    byte[] result = Primitives.derive_sender_hmac(master_key);
    assertTrue("Derives sender HMAC key", Arrays.equals(expected, result));
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_derive_sender_key_null_master_key()
          throws GeneralSecurityException
  {
    Primitives.derive_sender_key(null);
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_derive_sender_key_master_key_too_short()
          throws GeneralSecurityException
  {
    Primitives.derive_sender_key(hexStringToBytes("33", 31));
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_derive_sender_key_master_key_too_long()
          throws GeneralSecurityException
  {
    Primitives.derive_sender_key(hexStringToBytes("33", 33));
  }


  @Test
  public void test_derive_sender_key()
          throws GeneralSecurityException
  {
    byte[] master_key = hexStringToBytes("bc", 32);
    byte[] expected = hexStringToBytes("327b5f32d7ff0beeb0a7224166186e5f1fc2ba681092214a25b1465d1f17d837");

    byte[] result = Primitives.derive_sender_key(master_key);
    assertTrue("Derives sender key", Arrays.equals(expected, result));
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_derive_receiver_hmac_null_master_key()
          throws GeneralSecurityException
  {
    Primitives.derive_receiver_hmac(null);
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_derive_receiver_hmac_master_key_too_short()
          throws GeneralSecurityException
  {
    Primitives.derive_receiver_hmac(hexStringToBytes("bc", 31));
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_derive_receiver_hmac_master_key_too_long()
          throws GeneralSecurityException
  {
    Primitives.derive_receiver_hmac(hexStringToBytes("bc", 33));
  }


  @Test
  public void test_derive_receiver_hmac()
          throws GeneralSecurityException
  {
    byte[] master_key = hexStringToBytes("bc", 32);
    byte[] expected = hexStringToBytes("375f52dff2a263f2d0e0df11d252d25ba18b2f9abae1f0cbf299bab8d8c4904d");

    byte[] result = Primitives.derive_receiver_hmac(master_key);
    assertTrue("Derives receiver HMAC key", Arrays.equals(expected, result));
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_derive_receiver_key_null_master_key()
          throws GeneralSecurityException
  {
    Primitives.derive_receiver_key(null);
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_derive_receiver_key_master_key_too_short()
          throws GeneralSecurityException
  {
    Primitives.derive_receiver_key(hexStringToBytes("33", 31));
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_derive_receiver_key_master_key_too_long()
          throws GeneralSecurityException
  {
    Primitives.derive_receiver_key(hexStringToBytes("33", 33));
  }


  @Test
  public void test_derive_receiver_key()
          throws GeneralSecurityException
  {
    byte[] master_key = hexStringToBytes("bc", 32);
    byte[] expected = hexStringToBytes("c7e2a9660369f243aed71b0de0c49ee69719d20261778fdf39991a456566ef22");

    byte[] result = Primitives.derive_receiver_key(master_key);
    assertTrue("Derives receiver key", Arrays.equals(expected, result));
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_encrypt_null_binary()
          throws GeneralSecurityException
  {
    Primitives.encrypt(null, KEY);
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_encrypt_null_key()
          throws GeneralSecurityException
  {
    Primitives.encrypt(hexStringToBytes("11", 25), null);
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_encrypt_key_too_short()
          throws GeneralSecurityException
  {
    Primitives.encrypt(hexStringToBytes("11", 25), hexStringToBytes("cd", 31));
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_encrypt_key_too_long()
          throws GeneralSecurityException
  {
    Primitives.encrypt(hexStringToBytes("11", 25), hexStringToBytes("cd", 33));
  }


  @Test
  public void test_encrypt()
          throws GeneralSecurityException
  {
    byte[] result = Primitives.encrypt(DATA, KEY);
    assertEquals(48, result.length);
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_decrypt_null_binary()
          throws GeneralSecurityException
  {
    Primitives.decrypt(null, KEY, IV);
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_decrypt_null_key()
          throws GeneralSecurityException
  {
    Primitives.decrypt(DATA, null, IV);
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_decrypt_key_too_short()
          throws GeneralSecurityException
  {
    Primitives.decrypt(DATA, hexStringToBytes("cd", 31), IV);
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_decrypt_key_too_long()
          throws GeneralSecurityException
  {
    Primitives.decrypt(DATA, hexStringToBytes("cd", 33), IV);
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_decrypt_null_iv()
          throws GeneralSecurityException
  {
    Primitives.decrypt(DATA, KEY, null);
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_decrypt_iv_too_short()
          throws GeneralSecurityException
  {
    Primitives.decrypt(DATA, KEY, hexStringToBytes("ab", 15));
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_decrypt_iv_too_long()
          throws GeneralSecurityException
  {
    Primitives.decrypt(DATA, KEY, hexStringToBytes("ab", 17));
  }


  @Test
  public void test_decrypt()
          throws GeneralSecurityException
  {
    byte[] ciphertext = hexStringToBytes("f8c6db482b00b25b122e2dc8c50c52db8dbd58a796fcaed6d926e87bb227dfbb");
    byte[] iv = hexStringToBytes("3f05e3a3fb9cdb198f498174002965ac");

    byte[] plaintext = Primitives.decrypt(ciphertext, KEY, iv);
    assertTrue("Decrypts the data", Arrays.equals(DATA, plaintext));
  }


  @Test
  public void test_encrypt_and_decrypt()
          throws GeneralSecurityException
  {
    byte[] encrypted = Primitives.encrypt(DATA, KEY);

    byte[] iv = Arrays.copyOfRange(encrypted, 0, 16);
    byte[] ciphertext = Arrays.copyOfRange(encrypted, 16, encrypted.length);
    byte[] plaintext = Primitives.decrypt(ciphertext, KEY, iv);

    assertTrue("Encrypts and decrypts the data", Arrays.equals(DATA, plaintext));
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_identify_null_binary()
          throws GeneralSecurityException
  {
    Primitives.identify(null);
  }


  @Test
  public void test_identify()
          throws GeneralSecurityException
  {
    byte[] identity = Primitives.identify(KEY);
    assertTrue(Arrays.equals(hexStringToBytes("b097da5683f1"),identity));
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_mac_null_binary()
          throws GeneralSecurityException
  {
    Primitives.mac(null, KEY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_mac_null_hmac_key()
          throws GeneralSecurityException
  {
    Primitives.mac(DATA, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_mac_hmac_key_too_short()
          throws GeneralSecurityException
  {
    Primitives.mac(DATA, hexStringToBytes("9f", 31));
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_mac_hmac_key_too_long()
          throws GeneralSecurityException
  {
    Primitives.mac(DATA, hexStringToBytes("9f", 33));
  }

  @Test
  public void test_mac()
          throws GeneralSecurityException
  {
    byte[] expected = hexStringToBytes("adf1793fdef44c54a2c01513c0c7e4e71411600410edbde61558db12d0a01c65");
    assertTrue(Arrays.equals(expected, Primitives.mac(DATA, hexStringToBytes("9f", 32))));
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_compare_null_a()
  {
    Primitives.compare(null, hexStringToBytes("11"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_compare_null_b()
  {
    Primitives.compare(hexStringToBytes("11"), null);
  }

  @Test
  public void test_compare()
  {
    byte[] a = hexStringToBytes("11");
    byte[] b = hexStringToBytes("12");
    byte[] c = hexStringToBytes("11");
    byte[] d = hexStringToBytes("1111");

    assertTrue(Primitives.compare(a, a));
    assertFalse(Primitives.compare(a, b));
    assertTrue(Primitives.compare(a, c));
    assertFalse(Primitives.compare(a, d));
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_binify_null_string()
  {
    Primitives.binify(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_binify_invalid_string()
  {
    Primitives.binify("I'm not a base64 string.");
  }

  @Test
  public void test_binify()
  {
    byte[] expected = hexStringToBytes("69b71d");
    assertTrue(Arrays.equals(expected, Primitives.binify("abcd")));
  }

  @Test
  public void test_binify_needing_padding()
  {
    byte[] expected = Base64.decodeBase64("YWJjZGU=");
    assertTrue(Arrays.equals(expected, Primitives.binify("YWJjZGU")));
  }


  @Test(expected = IllegalArgumentException.class)
  public void test_stringify_null_binary()
  {
    Primitives.stringify(null);
  }

  @Test
  public void test_stringify()
  {
    assertEquals("MjIyMjIyMjIyMg",Primitives.stringify(hexStringToBytes("32",10)));
  }

  @Test
  public void test_stringify_remove_padding()
  {
    assertEquals("YWJjZGU", Primitives.stringify(hexStringToBytes("6162636465")));
  }


  @Test
  public void test_serialize()
          throws IOException
  {
    byte[] binary = Primitives.serialize("abcd");
    byte[] expected = new byte[]{ -92, 97, 98, 99, 100 };
    assertTrue(Arrays.equals(expected, binary));
  }


  @Test( expected = IllegalArgumentException.class)
  public void test_deserialize_null_binary()
          throws IOException
  {
    Primitives.deserialize(null, String.class);
  }

  @Test( expected = IllegalArgumentException.class)
  public void test_deserialize_null_class()
          throws IOException
  {
    byte[] binary = new byte[]{ -92, 97, 98, 99, 100 };
    Primitives.deserialize(binary, (Class)null);
  }

  @Test( expected = IllegalArgumentException.class)
  public void test_deserialize_null_template()
          throws IOException
  {
    byte[] binary = new byte[]{ -92, 97, 98, 99, 100 };
    Primitives.deserialize(binary, (Template)null);
  }

  @Test
  public void test_deserialize_with_class()
          throws IOException
  {
    byte[] binary = new byte[]{ -92, 97, 98, 99, 100 };
    Object object = Primitives.deserialize(binary, String.class);
    assertTrue(object instanceof String);
    assertEquals("abcd", object);
  }

  @Test
  public void test_deserialize_with_template()
          throws IOException
  {
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("id", 12345);
    data.put("name", "John Doe");
    List<String> roles = new ArrayList<String>();
    roles.add("admin");
    roles.add("user");
    data.put("roles", roles);

    byte[] binary = Primitives.serialize(data);

    Map<String, Value> map = Primitives.deserialize(binary, tMap(TString, TValue));
    assertEquals(12345, map.get("id").asIntegerValue().getInt());
    assertEquals("John Doe", map.get("name").asRawValue().getString());
    List<Value> list = (List<Value>)map.get("roles");
    assertEquals(2, list.size());
    assertEquals("admin", list.get(0).asRawValue().getString());
    assertEquals("user", list.get(1).asRawValue().getString());
  }


  @Test
  public void test_zero_null_binaries()
  {
    Primitives.zero(null);
  }

  @Test
  public void test_zero()
  {
    byte[] array1 = "123".getBytes();
    byte[] array2 = "456".getBytes();
    byte[] array3 = null;
    byte[] array4 = "789".getBytes();

    byte[] expected = new byte[]{ 0x00, 0x00, 0x00 };
    Primitives.zero(array1, array2, array3, array4);
    assertTrue(Arrays.equals(expected, array1));
    assertTrue(Arrays.equals(expected, array2));
    assertNull(array3);
    assertTrue(Arrays.equals(expected, array4));
  }


  // Helper methods


  private static final byte[] hexStringToBytes(String string)
  {
    return hexStringToBytes(string, 1);
  }


  private static final byte[] hexStringToBytes(String string, int repeat)
  {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < repeat; i++)
      builder.append(string);
    try {
      return new Hex().decode(builder.toString().getBytes());
    }
    catch (DecoderException e) {
      e.printStackTrace();
      return null;
    }
  }
}