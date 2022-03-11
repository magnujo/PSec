package com.company.exercise.pwmanager;

import org.apache.commons.lang3.SerializationUtils;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class PasswordProtector {
    String passwordFileName;
    Cipher cipher;
    byte[] keyBytes = Hex.decode("000102030405060708090a0b0c0d0e0f");
    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
    String ivString = "9f741fdb5d8845bdb48a94394e84f8a3";
    byte[] iv =  Hex.decode(ivString);
    String algoName = "AES";
    String tranformation = algoName + "/CBC/PKCS5Padding";


    // initializa Cipher-object
    PasswordProtector(String passwordFileName) {
        this.passwordFileName = passwordFileName;
    }

    // load() and decrypt()
    PasswordTable load() {

        PasswordTable pt;
        byte[] encrypted = library.FileUtil.readAllBytes(passwordFileName + "." + ivString);
        if (encrypted.length == 0) { pt = new PasswordTable();}
        else {
            byte[] serialized = decrypt(encrypted);
            pt = (PasswordTable) SerializationUtils.deserialize(serialized);
        }

        return pt;
    }


    byte[] decrypt(byte[] input) {

        byte[] output = new byte[0];

        try {
            Cipher cipher = Cipher.getInstance(tranformation, "BC");
            SecretKeySpec key = new SecretKeySpec(keyBytes, algoName);
            cipher.init(Cipher.DECRYPT_MODE, key);
            output = cipher.doFinal(input);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }

    void byte[] encrypt(byte [] input) {

        byte[] output = new byte[0];

        try {
            Cipher cipher = Cipher.getInstance(tranformation, "BC");
            SecretKeySpec key = new SecretKeySpec(keyBytes, algoName);
            cipher.init(Cipher.ENCRYPT_MODE, key);
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
        library.FileUtil.write(algoName, passwordFileName, encrypted, ivString);
    }
}
