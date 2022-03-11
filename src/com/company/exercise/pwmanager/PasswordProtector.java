package com.company.exercise.pwmanager;

import org.apache.commons.lang3.SerializationUtils;

public class PasswordProtector {
    String passwordFileName;
    Cipher cipher;
    SecretKeySpec key = .. // hardcoded
    byte[] iv = .. // hardcoded

    // initializa Cipher-object
    PasswordProtector(String passwordFileName) {
        this.passwordFileName = passwordFileName;
    }

    // load() and decrypt()
    PasswordTable load() {
        PasswordTable pt;
        byte[] encrypted = library.FileUtil.readAllBytes(
                "AES/CBC/PKCS5Padding",
                passwordFileName + "." + ivString);
        if (encrypted.length == 0) { pt = new PasswordTable();}
        else {
            byte[] serialized = decrypt(encrypted);
            pt = (PasswordTable) SerializationUtils.deserialize(serialized);
        }
        return pt;
    }


    byte[] decrypt(byte[] input) {..}

    // store() and encrypt()
    void store(PasswordTable pt) {
        byte[] serialized = SerializationUtils.serialize(pt);
        byte[] encrypted = encrypt(serialized);
        library.FileUtil.write("AES/CBC/PKCS5Padding",
                passwordFileName, encrypted, ivString);
    }
}
