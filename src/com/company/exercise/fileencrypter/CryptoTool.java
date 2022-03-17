package com.company.exercise.fileencrypter;

import org.bouncycastle.util.encoders.Hex;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Arrays;

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

    public void encryptFile(String filepath, String algorithm, boolean hashing) {
        this.algorithm = algorithm.toUpperCase();
        if (hashing) {
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
                SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);
                cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
                byte[] ciphertext = cipher.doFinal(inputWithHash);
                System.out.println("Ciphertext length with hash: " + ciphertext.length);

                // writing
                library.FileUtil.write(filepath + "." + algorithm, ciphertext);
                System.out.println("Slut");


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {

                // reading
                byte[] input = library.FileUtil.readAllBytes(filepath);
                System.out.println("Plaintext length without hash: " + input.length);


                // encrypting
                Cipher cipher = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding", "BC");
                SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);
                cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
                byte[] output = cipher.doFinal(input);
                System.out.println("Ciphertext length without hash: " + output.length);

                // writing
                library.FileUtil.write(filepath + "." + algorithm, output);
                System.out.println("Slut");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void decryptFile(String filepath, String algorithm, boolean hashing) {

        this.algorithm = algorithm.toUpperCase();

        if (hashing) {
            try {
                // Reading
                System.out.println(filepath);
                byte[] input = library.FileUtil.readAllBytes(filepath);
                System.out.println(input);

                //Tampering test
               // input[56] = 10;

                // Decrypting
                Cipher cipher = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding", "BC");
                SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);
                cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
                byte[] output = cipher.doFinal(input);

                System.out.println("Decryptet text length: " + output.length);

                //Splitting the hash value from the plaintext
                byte[] hashValue = Arrays.copyOfRange(output, output.length - 32, output.length);
                byte[] plainText = Arrays.copyOfRange(output, 0, output.length - 32);
                System.out.println("Hash length from file: " + hashValue.length);
                System.out.println("Hash value from file: " + Arrays.toString(hashValue));

                // Checking the integrity before writing
                verifySHA256(hashValue, plainText);
                library.FileUtil.write(filepath.substring(0, filepath.length() - algorithm.length()) + "test", output);

            } catch (Exception e) { e.printStackTrace();}
        }

        else {
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
            } catch (Exception e) { e.printStackTrace();}
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


    void verifySHA256(byte[] hashvalue, byte[] plaintext){
        try {
            // Verifying hash
            MessageDigest digest = MessageDigest.getInstance("SHA-256", "BC");
            digest.update(plaintext);
            byte[] computedHashValue = digest.digest();

            if (MessageDigest.isEqual(computedHashValue, hashvalue)) {
                System.out.println("Hash values are equal");


            } else {
                System.out.println("Hash values are not equal");

            }
        }
        catch (Exception e) { e.printStackTrace(); }

    }

}
