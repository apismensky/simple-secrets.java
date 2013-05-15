package com.github.timshadel.simplesecrets;


import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import java.security.GeneralSecurityException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class PrimitivesTest
{
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


  @Test( expected = IllegalArgumentException.class)
  public void test_derive_sender_hmac_null_master_key()
          throws GeneralSecurityException
  {
    Primitives.derive_sender_hmac(null);
  }

  @Test( expected = IllegalArgumentException.class)
  public void test_derive_sender_hmac_too_short_master_key()
          throws GeneralSecurityException
  {
    Primitives.derive_sender_hmac(hexStringToBytes("bc", 31));
  }

  @Test( expected = IllegalArgumentException.class)
  public void test_derive_sender_hmac_too_long_master_key()
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



  @Test( expected = IllegalArgumentException.class)
  public void test_derive_sender_key_null_master_key()
          throws GeneralSecurityException
  {
    Primitives.derive_sender_key(null);
  }

  @Test( expected = IllegalArgumentException.class)
  public void test_derive_sender_key_too_short_master_key()
          throws GeneralSecurityException
  {
    Primitives.derive_sender_key(hexStringToBytes("33", 31));
  }

  @Test( expected = IllegalArgumentException.class)
  public void test_derive_sender_key_too_long_master_key()
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


  @Test( expected = IllegalArgumentException.class)
  public void test_derive_receiver_hmac_null_master_key()
          throws GeneralSecurityException
  {
    Primitives.derive_receiver_hmac(null);
  }

  @Test( expected = IllegalArgumentException.class)
  public void test_derive_receiver_hmac_too_short_master_key()
          throws GeneralSecurityException
  {
    Primitives.derive_receiver_hmac(hexStringToBytes("bc", 31));
  }

  @Test( expected = IllegalArgumentException.class)
  public void test_derive_receiver_hmac_too_long_master_key()
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



  @Test( expected = IllegalArgumentException.class)
  public void test_derive_receiver_key_null_master_key()
          throws GeneralSecurityException
  {
    Primitives.derive_receiver_key(null);
  }

  @Test( expected = IllegalArgumentException.class)
  public void test_derive_receiver_key_too_short_master_key()
          throws GeneralSecurityException
  {
    Primitives.derive_receiver_key(hexStringToBytes("33", 31));
  }

  @Test( expected = IllegalArgumentException.class)
  public void test_derive_receiver_key_too_long_master_key()
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



  private static final byte[] hexStringToBytes(String string)
  {
    return hexStringToBytes(string, 1);
  }


  private static final byte[] hexStringToBytes(String string, int repeat)
  {
    StringBuilder builder = new StringBuilder();
    for(int i = 0; i < repeat; i++)
      builder.append(string);
    try {
      return new Hex().decode(builder.toString().getBytes());
    } catch (DecoderException e) {
      e.printStackTrace();
      return null;
    }
  }
}