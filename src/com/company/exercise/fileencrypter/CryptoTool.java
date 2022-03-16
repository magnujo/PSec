package com.company.exercise.fileencrypter;

import org.bouncycastle.util.encoders.Hex;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class CryptoTool {

    String algorithm;
    byte[] keyBytes = Hex.decode("000102030405060708090a0b0c0d0e0f");
    byte[] iv;

    CryptoTool() {
        SecureRandom secureRandom = null;
        try {
            secureRandom = SecureRandom.getInstance("DEFAULT", "BC");
        }
        catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        this.iv = new byte[16];
        assert secureRandom != null;
        secureRandom.nextBytes(iv);
    }

    public void encryptFile(String filepath, String algorithm) {
        this.algorithm = algorithm.toUpperCase();
        try {
            // reading
            byte [] input = library.FileUtil.readAllBytes(filepath);
            // encrypting
            Cipher cipher = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding", "BC");
            SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] output = cipher.doFinal(input);
            // writing
            library.FileUtil.write(filepath + "." + algorithm, output);
            System.out.println("Slut");
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void decryptFile(String filepath, String algorithm) {

        this.algorithm = algorithm.toUpperCase();

        try {
            // Reading
            System.out.println(filepath);
            byte[] input = library.FileUtil.readAllBytes(filepath);
            System.out.println(input);
            // Decrypting
            Cipher cipher = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding", "BC");
            SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] output = cipher.doFinal(input);
            // Writing (removes the .AES from the filepath)
            library.FileUtil.write(filepath.substring(0, filepath.length() - algorithm.length()) + "test", output);
        } catch (Exception e) { e.printStackTrace();
        }
    }

    void computeSHA256(String filepath){
        try {
            // reading plaintext file
            byte [] input = library.FileUtil.readAllBytes(filepath);

            // computing hash value of plaintext
            MessageDigest digest = MessageDigest.getInstance("SHA-256", "BC");
            byte[] hashValue = digest.digest(input);
            System.out.println("Hashvalue: " + Hex.toHexString(hashValue));

            // writing hash value to file
            String hashFileName = filepath;
            library.FileUtil.write("sha256", hashFileName, hashValue);
        } catch (Exception e) { e.printStackTrace(); }
    }

    void verifySHA256(String filepath){
        try {
            /*// Reading ciphertext file
            ..
            // getting key
            ..
            // Decrypting
            ..
            byte[] plaintext = cipher.doFinal(ciphertext);*/

            // to begin with, you may use original plaintext

            // Verifying hash
            byte[] storedHashValue = library.FileUtil.readAllBytes(filepath + ".sha256");
            MessageDigest digest = MessageDigest.getInstance("SHA-256", "BC");
            digest.update(library.FileUtil.readAllBytes(filepath));
            byte[] computedHashValue = digest.digest();
            if (MessageDigest.isEqual(computedHashValue, storedHashValue)) {
                System.out.println("Hash values are equal");
            } else {
                System.out.println("Hash values are not equal");
                System.exit(1);
            }
        }
        catch (Exception e) { e.printStackTrace(); }
    }

}
