package com.company.examples.chapter2;

import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * A simple example of AES in ECB mode.
 */
public class ECBShortExample
{
    public static void main(String[] args)
        throws Exception
    {
        byte[] keyBytes = Hex.decode("000102030405060708090a0b0c0d0e0f");

        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

        byte[] input = Hex.decode("a0b1a2a3a4a5a6a7a0a1a2a3a4a5a6a7");
       // byte[] input = Hex.decode("a0b1a2a3a4a5a6a7a0a1a2a3a4a5a6a7");
      /*  byte[] input = {0b01010000, 0b01010000, 0b01010000, 0b01010000,
                0b01010000, 0b01010000, 0b01010000, 0b01010000, 0b01010000,
                0b01010000, 0b01010000, 0b01010000, 0b01010000, 0b01010000, 0b01010000, 0b01010000};*/

        System.out.println("input    : " + Hex.toHexString(input));
        System.out.println(input.length);

        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] output = cipher.doFinal(input);

        System.out.println("encrypted: " + Hex.toHexString(output));

        cipher.init(Cipher.DECRYPT_MODE, key);

        System.out.println("decrypted: "
                            + Hex.toHexString(cipher.doFinal(output)));
    }
}
