package com.company.ClientServerChat;

import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class CryptoTool {

    String algorithm;
    byte[] keyBytes = Hex.decode("000102030405060708090a0b0c0d0e0f");
    byte[] iv;
    int ivLength;
    RSAPublicKey rsaPublicKey;
    RSAPrivateKey rsaPrivateKey;
    KeyPairGenerator generator;
    KeyPair keyPair;
    SecretKeySpec aesSymKey;
    byte[] encryptedSymKey;
    public PublicKey pubkey;

    CryptoTool() {
        generateIv();
        generateAsymmetricKeyPair();
        aesSymKey = generateSymmetricKey();
        byte[] rsa = rsaPublicKey.getEncoded();
        encryptedSymKey = encryptKey(aesSymKey, rsaPublicKey);
    }

    private void generateIv(){
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

    private void generateAsymmetricKeyPair(){
        //Generation assymetric RSA keys
        try {
            generator = KeyPairGenerator.getInstance("RSA", "BC");
        } catch (Exception e) {
            e.printStackTrace();
        }
        generator.initialize(3072);
        keyPair = generator.generateKeyPair();
        pubkey = keyPair.getPublic();
        rsaPublicKey = (RSAPublicKey) pubkey;
        rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
    }

    private SecretKeySpec generateSymmetricKey() {
        SecretKeySpec key = null;
        try {
            // generating random bytes
            SecureRandom secureRandom = SecureRandom.getInstance("DEFAULT", "BC");
            byte[] keyBytes = new byte[16];
            secureRandom.nextBytes(keyBytes);
            // generating key
            key = new SecretKeySpec(keyBytes, "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }

    public static PublicKey decodeRSAKey(byte[] encoded) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory f = KeyFactory.getInstance("RSA");
        return f.generatePublic(new X509EncodedKeySpec(encoded));
    }

    public static PrivateKey decodeAESKey(byte[] encoded) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory f = KeyFactory.getInstance("AES");
        return f.generatePrivate(new X509EncodedKeySpec(encoded));
    }


    public void addKeyToKeyStore(String KeyPW, String alias, SecretKeySpec key, KeyStore ks){
        KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(key);
        KeyStore.ProtectionParameter protection = new KeyStore.PasswordProtection(KeyPW.toCharArray());
        try {
            ks.setEntry(alias, entry, protection);
            System.out.println("Stored key as: " + ks.getKey(alias, KeyPW.toCharArray()).toString());
        }catch (Exception e) {e.printStackTrace();}
    }

    //encrypt a symmetric key using RSA.
    public byte[] encryptKey(SecretKeySpec aesSymKey, RSAPublicKey rsaPublicKey) {
        byte[] wrappedKey = new byte[0];
        try {
            Cipher cipher = Cipher.getInstance("RSA/NONE/OAEPwithSHA256andMGF1Padding", "BC");
            cipher.init(Cipher.WRAP_MODE, rsaPublicKey);
            wrappedKey = cipher.wrap(aesSymKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wrappedKey;
    }

    public RSAPrivateKey getPrivKey() {
        return rsaPrivateKey;
    }

    public RSAPublicKey getPubKey() {
        return rsaPublicKey;
    }

    public SecretKeySpec getSymKey() {
        return aesSymKey;
    }

    public byte[] getEncryptedSymKey() {
        return encryptedSymKey;
    }

    //decrypt a symmetric key using RSA.
    public SecretKeySpec decryptKey(byte[] wrappedKey, RSAPrivateKey rsaPrivateKey) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA/NONE/OAEPwithSHA256andMGF1Padding", "BC");
        cipher.init(Cipher.UNWRAP_MODE, rsaPrivateKey);
        return (SecretKeySpec)cipher.unwrap(wrappedKey, aesSymKey.getAlgorithm(), Cipher.SECRET_KEY);
    }

    //problem with iv? should i use the iv i get from the other party?
    public byte[] encryptMessage(String msg, boolean hashing) {
        if (hashing) {
            System.out.println("encrypting and hashing using Key: " + Arrays.toString(aesSymKey.getEncoded()));
            System.out.println("IV: " + Arrays.toString(iv));
            try {
                // reading plaintext file
                byte[] input = msg.getBytes(StandardCharsets.UTF_8);

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
                Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding", "BC");
                //SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);
                cipher.init(Cipher.ENCRYPT_MODE, aesSymKey, new IvParameterSpec(iv));
                byte[] ciphertext = cipher.doFinal(inputWithHash);
                System.out.println("Ciphertext length with hash: " + ciphertext.length);

                byte[] ciphertext_iv = new byte[ciphertext.length + iv.length];

                System.arraycopy(ciphertext, 0, ciphertext_iv, 0, ciphertext.length);
                System.arraycopy(iv, 0, ciphertext_iv, ciphertext.length, iv.length);

                // writing
                System.out.println("Slut");
                return ciphertext_iv;

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("encrypting using Key: " + Arrays.toString(aesSymKey.getEncoded()));
            System.out.println("IV: " + Arrays.toString(iv));
            try {

                // reading
                byte[] input = msg.getBytes(StandardCharsets.UTF_8);
                System.out.println("Plaintext length without hash: " + input.length);


                // encrypting
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
                //SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);
                cipher.init(Cipher.ENCRYPT_MODE, aesSymKey, new IvParameterSpec(iv));
                System.out.println("Encrypting using key: " + aesSymKey.toString());
                byte[] ciphertext = cipher.doFinal(input);
                System.out.println("Ciphertext length without hash: " + ciphertext.length);

                byte[] ciphertext_iv = new byte[ciphertext.length + iv.length];
                System.arraycopy(ciphertext, 0, ciphertext_iv, 0, ciphertext.length);
                System.arraycopy(iv, 0, ciphertext_iv, ciphertext.length, iv.length);

                // writing
                System.out.println("Slut");
                return ciphertext_iv;


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    return "ERROR".getBytes(StandardCharsets.UTF_8);
    }

    public String decryptMessage(byte[] msg, boolean hashing) {

        if (hashing) {
            System.out.println("Decrypting and dehashing using Key: " + Arrays.toString(aesSymKey.getEncoded()));
            try {
                // Reading
                System.out.println(Arrays.toString(msg));
                byte[] input = msg;
                byte[] iv = Arrays.copyOfRange(input, input.length - ivLength, input.length);
                byte[] ciphertext = Arrays.copyOfRange(input, 0, input.length - ivLength);
                System.out.println("IV: " + Arrays.toString(iv));

                //Tampering test
                // input[56] = 10;

                // Decrypting
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
                //SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);
                cipher.init(Cipher.DECRYPT_MODE, aesSymKey, new IvParameterSpec(iv));
                byte[] output = cipher.doFinal(ciphertext);

                System.out.println("Decryptet text length: " + output.length);

                //Splitting the hash value from the plaintext
                byte[] hashValue = Arrays.copyOfRange(output, output.length - 32, output.length);
                byte[] plainText = Arrays.copyOfRange(output, 0, output.length - 32);
                System.out.println("Hash length from file: " + hashValue.length);
                System.out.println("Hash value from file: " + Arrays.toString(hashValue));

                // Checking the integrity before writing
                boolean verification = verifySHA256(hashValue, plainText);

                if (verification) {
                    return new String(plainText);
                }
                    else return "Message has been tampered";
            } catch (Exception e) { e.printStackTrace();}
        }

        else {
            System.out.println("Decrypting using Key: " + Arrays.toString(aesSymKey.getEncoded()));
            try {

                // Reading
                byte[] input = msg;
                System.out.println(Arrays.toString(input));
                byte[] iv = Arrays.copyOfRange(input, input.length - ivLength, input.length);
                byte[] ciphertext = Arrays.copyOfRange(input, 0, input.length - ivLength);
                System.out.println("IV: " + Arrays.toString(iv));

                // Decrypting
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
                //SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);
                cipher.init(Cipher.DECRYPT_MODE, aesSymKey, new IvParameterSpec(iv));
                byte[] output = cipher.doFinal(ciphertext);

                // Writing (removes the .AES from the filepath)
                return new String(output);
            } catch (Exception e) { e.printStackTrace();}
        }

    return "Error: There was an issue receiving a message from the sender";
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
                System.out.println("Encrypting using key: " + key);
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

    public void decryptFile(String filepath, String algorithm, boolean hashing, SecretKeySpec key) {

        this.algorithm = algorithm.toUpperCase();

        if (hashing) {
            System.out.println("Decrypting and dehashing using Key: " + Arrays.toString(key.getEncoded()));
            try {
                // Reading
                System.out.println(filepath);
                byte[] input = library.FileUtil.readAllBytes(filepath);
                System.out.println(input);
                byte[] iv = Arrays.copyOfRange(input, input.length - ivLength, input.length);
                byte[] ciphertext = Arrays.copyOfRange(input, 0, input.length - ivLength);
                System.out.println("IV: " + Arrays.toString(iv));

                //Tampering test
                // input[56] = 10;

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
                library.FileUtil.write(filepath.substring(0, filepath.length() - algorithm.length()) + "test", output);

            } catch (Exception e) { e.printStackTrace();}
        }

        else {
            System.out.println("Decrypting using Key: " + Arrays.toString(key.getEncoded()));
            try {

                // Reading
                System.out.println(filepath);
                byte[] input = library.FileUtil.readAllBytes(filepath);
                System.out.println(Arrays.toString(input));
                byte[] iv = Arrays.copyOfRange(input, input.length - ivLength, input.length);
                byte[] ciphertext = Arrays.copyOfRange(input, 0, input.length - ivLength);
                System.out.println("IV: " + Arrays.toString(iv));

                // Decrypting
                Cipher cipher = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding", "BC");
                //SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);
                cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
                byte[] output = cipher.doFinal(ciphertext);

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

    public boolean verifySHA256(byte[] hashvalue, byte[] plaintext){
        try {
            // Verifying hash
            MessageDigest digest = MessageDigest.getInstance("SHA-256", "BC");
            digest.update(plaintext);
            byte[] computedHashValue = digest.digest();

            if (MessageDigest.isEqual(computedHashValue, hashvalue)) {
                System.out.println("Hash values are equal");
                return true;

            } else {
                System.out.println("Hash values are not equal");
                return false;
            }
        }
        catch (Exception e) { e.printStackTrace(); }

        System.out.println("Verification error");
        return false;
    }
}
