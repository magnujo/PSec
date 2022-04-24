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

    public void encryptFile(String filepath, String algorithm, boolean hashing, SecretKeySpec key) {

        this.algorithm = algorithm.toUpperCase();
        if (hashing) {
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
                //SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);
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

        } else {
            System.out.println("encrypting using Key: " + Arrays.toString(key.getEncoded()));
            System.out.println("IV: " + Arrays.toString(iv));
            try {

                // reading
                byte[] input = library.FileUtil.readAllBytes(filepath);
                System.out.println("Plaintext length without hash: " + input.length);


                // encrypting
                Cipher cipher = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding", "BC");
                //SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);
                cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
                System.out.println("Encrypting using key: " + key.toString());
                byte[] ciphertext = cipher.doFinal(input);
                System.out.println("Ciphertext length without hash: " + ciphertext.length);

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
    }

    public String decryptFile(String filepath, String algorithm, boolean hashing, SecretKeySpec key) {

        this.algorithm = algorithm.toUpperCase();

        if (hashing) {
            System.out.println("Decrypting and dehashing using Key: " + Arrays.toString(key.getEncoded()));
            try {

                // Reading
                System.out.println(filepath);
                byte[] input = library.FileUtil.readAllBytes(filepath);
                System.out.println("Input: " + Arrays.toString(Hex.encode(input)));

                //Tampering test
                //input[1] = 10;

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
                if(verifySHA256(hashValue, plainText)) {
                    library.FileUtil.write(filepath.substring(0, filepath.length() - algorithm.length()) + "test", output);
                    return "FileGood";
                }
                else return "FileChanged";
            } catch (BadPaddingException e) {return "WrongKey";} catch (Exception e) { e.printStackTrace();}
        }

        else {
            System.out.println("Decrypting using Key: " + Arrays.toString(key.getEncoded()));
            try {

            // Reading
            System.out.println(filepath);
            byte[] input = library.FileUtil.readAllBytes(filepath);

            byte[] iv = Arrays.copyOfRange(input, input.length - ivLength, input.length);
            byte[] ciphertext = Arrays.copyOfRange(input, 0, input.length - ivLength);
            System.out.println("IV: " + Arrays.toString(iv));

            // Decrypting
            Cipher cipher = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding", "BC");
            //SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] output = cipher.doFinal(ciphertext);

            // Writing (removes the .AES from the filepath
            library.FileUtil.write(filepath.substring(0, filepath.length() - algorithm.length()) + "test", output);
            return "FileGood";
            } catch (BadPaddingException e) {return "WrongKey";} catch (Exception e) { e.printStackTrace();}
        }
        System.out.println("Errror: program should not reach this point");
        return "WrongKey";
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


    boolean verifySHA256(byte[] hashvalue, byte[] plaintext){
        try {
            // Verifying hash
            MessageDigest digest = MessageDigest.getInstance("SHA-256", "BC");
            //digest.update(plaintext);
            byte[] computedHashValue = digest.digest(plaintext);

            System.out.println("Original hash and checked hash:");
            System.out.println(Hex.toHexString(hashvalue));
            System.out.println(Hex.toHexString(computedHashValue));

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
