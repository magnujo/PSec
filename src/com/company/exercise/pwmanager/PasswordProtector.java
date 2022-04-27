package com.company.exercise.pwmanager;

import com.company.exercise.FileEncrypterMVC.FileUtil;
import org.apache.commons.lang3.SerializationUtils;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class PasswordProtector {
    String passwordFilePath;
    Cipher cipher;
    byte[] keyBytes = Hex.decode("000102030405060708090a0b0c0d0e0f");
    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
    String ivString = "9f741fdb5d8845bdb48a94394e84f8a3";
    byte[] iv =  Hex.decode(ivString);
    String algoName = "AES";
    String transformation = algoName + "/CBC/PKCS5Padding";


    // initializer Cipher-object
    PasswordProtector(String passwordFilePath) {
        this.passwordFilePath = passwordFilePath;
    }

    // load() and decrypt()
    public PasswordTable load() {

        PasswordTable pt;
        byte[] encrypted = FileUtil.readAllBytes(passwordFilePath + "." + algoName + "." + ivString);
        if (encrypted.length == 0) {
            System.out.println("Creating new Password Table...");
            pt = new PasswordTable();
        }
        else {
            System.out.println("Decrypting passwordfile...");
            byte[] serialized = decrypt(encrypted);
            pt = SerializationUtils.deserialize(serialized);
        }

        return pt;
    }


    byte[] decrypt(byte[] input) {

        byte[] output = new byte[0];

        try {
            Cipher cipher = Cipher.getInstance(transformation, "BC");
            SecretKeySpec key = new SecretKeySpec(keyBytes, algoName);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            output = cipher.doFinal(input);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }

     byte[] encrypt(byte [] input) {

        byte[] output = new byte[0];

        try {
            Cipher cipher = Cipher.getInstance(transformation, "BC");
            SecretKeySpec key = new SecretKeySpec(keyBytes, algoName);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            output = cipher.doFinal(input);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }

    // store() and encrypt()
    void store(PasswordTable pt) {

        byte[] serialized = SerializationUtils.serialize(pt);
        byte[] encrypted = encrypt(serialized);
        FileUtil.write(passwordFilePath + "." + algoName + "." + ivString, encrypted);
    }
}
