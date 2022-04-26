package com.company.exercise.FileEncrypterMVC;

import org.bouncycastle.util.encoders.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.awt.desktop.AboutEvent;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 This class contains all the functionality related to cryptography.
 **/
public class CryptoTool {

    String algorithm;
    byte[] keyBytes = Hex.decode("000102030405060708090a0b0c0d0e0f");
    byte[] iv;
    int ivLength;

    CryptoTool() {
        SecureRandom secureRandom = null;
        try {
            secureRandom = SecureRandom.getInstance("DEFAULT", "BC");
        }
        catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        iv = new byte[16];
        assert secureRandom != null;
        secureRandom.nextBytes(iv);
        ivLength = iv.length;
    }

    public void encryptFile(String filepath, String algorithm, SecretKeySpec key) {
        this.algorithm = algorithm.toUpperCase();
        System.out.println("encrypting and hashing using Key: " + Arrays.toString(key.getEncoded()));
        System.out.println("IV: " + Arrays.toString(iv));
        try {
            // reading plaintext file
            byte[] input = library.FileUtil.readAllBytes(filepath);

            // computing hash value of plaintext
            MessageDigest digest = MessageDigest.getInstance("SHA-256", "BC");
            byte[] hashValue = digest.digest(input);
            System.out.println("Hash length: " + hashValue.length);
            System.out.println("Hash value: " + Arrays.toString(hashValue));

            // Appending hash value to input
            byte[] inputWithHash = new byte[input.length + hashValue.length];
            System.arraycopy(input, 0, inputWithHash, 0, input.length);
            System.arraycopy(hashValue, 0, inputWithHash, input.length, hashValue.length);
            System.out.println("Plaintext length with hash: " + inputWithHash.length);

            // encrypting input::hashvalue
            Cipher cipher = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] ciphertext = cipher.doFinal(inputWithHash);
            System.out.println("Ciphertext length with hash: " + ciphertext.length);

            byte[] ciphertext_iv = new byte[ciphertext.length + iv.length];

            System.arraycopy(ciphertext, 0, ciphertext_iv, 0, ciphertext.length);
            System.arraycopy(iv, 0, ciphertext_iv, ciphertext.length, iv.length);

            // writing
            library.FileUtil.write(filepath + "." + algorithm, ciphertext_iv);
            System.out.println("Slut");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String decryptFile(String filepath, String algorithm, SecretKeySpec key, boolean test) {

        this.algorithm = algorithm.toUpperCase();
        System.out.println("Decrypting and dehashing using Key: " + Arrays.toString(key.getEncoded()));
        try {
            // Reading
            System.out.println(filepath);
            byte[] input = library.FileUtil.readAllBytes(filepath);
            System.out.println("Input: " + Arrays.toString(Hex.encode(input)));

            //Tampering test
            if(test) input[1] = 10;
            System.out.println("Input: " + Arrays.toString(Hex.encode(input)));

            System.out.println(input);
            byte[] iv = Arrays.copyOfRange(input, input.length - ivLength, input.length);

            byte[] ciphertext = Arrays.copyOfRange(input, 0, input.length - ivLength);
            System.out.println("IV: " + Arrays.toString(iv));
            System.out.println("IV: " + Arrays.toString(Hex.encode(iv)));
            System.out.println("IV: " + Hex.toHexString(iv));

            System.out.println("Ciphertext: " + Hex.toHexString(ciphertext));

            // Decrypting
            Cipher cipher = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding", "BC");
            //SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] output = cipher.doFinal(ciphertext);
            System.out.println("Decrypted output: " + Hex.toHexString(output));

            System.out.println("Decryptet text length: " + output.length);

            //Splitting the hash value from the plaintext
            byte[] hashValue = Arrays.copyOfRange(output, output.length - 32, output.length);
            byte[] plainText = Arrays.copyOfRange(output, 0, output.length - 32);
            System.out.println("Hash length from file: " + hashValue.length);
            System.out.println("Hash value from file: " + Hex.toHexString(hashValue));

            System.out.println("Plaintext: " + Hex.toHexString(plainText));

            // Checking the integrity before writing
            if(verifySHA256(hashValue, plainText, test)) {
                library.FileUtil.write(filepath.substring(0, filepath.length() - algorithm.length()) + "test", output);
                return "FileGood";
            }
            else return "FileChanged";
        } catch (BadPaddingException e) {return "WrongKey";} catch (Exception e) { e.printStackTrace();}

        System.out.println("Errror: program should not reach this point");
        return "WrongKey";
    }

    boolean verifySHA256(byte[] hashvalue, byte[] plaintext, boolean test){
        try {
            // Verifying hash
            MessageDigest digest = MessageDigest.getInstance("SHA-256", "BC");
            //digest.update(plaintext);
            byte[] computedHashValue = digest.digest(plaintext);


            System.out.println("Original hash:  " + Hex.toHexString(hashvalue));
            System.out.println("Decrypted hash: " + Hex.toHexString(computedHashValue));


            if (MessageDigest.isEqual(computedHashValue, hashvalue)) {
                System.out.println("Hash values are equal");
                return true;


            } else {
                System.out.println("Hash values are not equal");
                return false;
            }
        }
        catch (Exception e) { e.printStackTrace(); }

        System.out.println("Some error happened while verifying hash.");
        return false;

    }

}
