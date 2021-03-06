package com.company.exercise.fileencrypter;

import com.company.exercise.FileEncrypterMVC.FileUtil;
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
                byte[] input = FileUtil.readAllBytes(filepath);

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
                FileUtil.write(filepath + "." + algorithm, ciphertext_iv);
                System.out.println("Slut");


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("encrypting using Key: " + Arrays.toString(key.getEncoded()));
            System.out.println("IV: " + Arrays.toString(iv));
            try {

                // reading
                byte[] input = FileUtil.readAllBytes(filepath);
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
                FileUtil.write(filepath + "." + algorithm, ciphertext_iv);
                System.out.println("Slut");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void decryptFile(String filepath, String algorithm, boolean hashing, SecretKeySpec key) {

        this.algorithm = algorithm.toUpperCase();

        if (hashing) {
            System.out.println("Decrypting and dehashing using Key: " + Arrays.toString(key.getEncoded()));
            try {
                // Reading
                System.out.println(filepath);
                byte[] input = FileUtil.readAllBytes(filepath);
                System.out.println(input);
                byte[] iv = Arrays.copyOfRange(input, input.length - ivLength, input.length);
                byte[] ciphertext = Arrays.copyOfRange(input, 0, input.length - ivLength);
                System.out.println("IV: " + Arrays.toString(iv));

                //Tampering test
                input[56] = 10;

                // Decrypting
                Cipher cipher = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding", "BC");
                //SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);
                cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
                byte[] output = cipher.doFinal(ciphertext);

                System.out.println("Decryptet text length: " + output.length);

                //Splitting the hash value from the plaintext
                byte[] hashValue = Arrays.copyOfRange(output, output.length - 32, output.length);
                byte[] plainText = Arrays.copyOfRange(output, 0, output.length - 32);
                System.out.println("Hash length from file: " + hashValue.length);
                System.out.println("Hash value from file: " + Arrays.toString(hashValue));

                // Checking the integrity before writing
                verifySHA256(hashValue, plainText);
                FileUtil.write(filepath.substring(0, filepath.length() - algorithm.length()) + "test", output);

            } catch (Exception e) { e.printStackTrace();}
        }

        else {
            System.out.println("Decrypting using Key: " + Arrays.toString(key.getEncoded()));
            try {

            // Reading
            System.out.println(filepath);
            byte[] input = FileUtil.readAllBytes(filepath);
            System.out.println(input);
            byte[] iv = Arrays.copyOfRange(input, input.length - ivLength, input.length);
            byte[] ciphertext = Arrays.copyOfRange(input, 0, input.length - ivLength);
            System.out.println("IV: " + Arrays.toString(iv));

            // Decrypting
            Cipher cipher = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding", "BC");
            //SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] output = cipher.doFinal(ciphertext);

            // Writing (removes the .AES from the filepath)
            FileUtil.write(filepath.substring(0, filepath.length() - algorithm.length()) + "test", output);
            } catch (Exception e) { e.printStackTrace();}
        }
    }

    void computeSHA256(String filepath){
        try {
            // reading plaintext file
            byte [] input = FileUtil.readAllBytes(filepath);

            // computing hash value of plaintext
            MessageDigest digest = MessageDigest.getInstance("SHA-256", "BC");
            byte[] hashValue = digest.digest(input);
            System.out.println("Hashvalue: " + Hex.toHexString(hashValue));

            // writing hash value to file
            String hashFileName = filepath;
            FileUtil.write("sha256", hashFileName, hashValue);
        } catch (Exception e) { e.printStackTrace(); }
    }

    void verifySHA256(String filepath){
        try {
            // Verifying hash
            byte[] storedHashValue = FileUtil.readAllBytes(filepath + ".sha256");
            MessageDigest digest = MessageDigest.getInstance("SHA-256", "BC");
            digest.update(FileUtil.readAllBytes(filepath));
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
