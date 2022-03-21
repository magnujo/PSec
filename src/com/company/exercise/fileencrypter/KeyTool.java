package com.company.exercise.fileencrypter;

import javax.crypto.ExemptionMechanismException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.util.*;

public class KeyTool {
    char[] storePW;
    String storePath = "src/com/company/exercise/fileencrypter/keystore.bks";
    KeyStore ks;

    public boolean fileCheck(){
        File f = new File(storePath);
        if(f.isFile()) {
            return true;// ask for password to enter keystore
        }
        else {
            return  false;   // ask to register password and create keystore
        }
    }

    // should run first time program is run. (Look for file keystore.bks and if not found this should run):
    public void createKeyStore(String storePW) {
        this.storePW = storePW.toCharArray();

        try {
            ks = KeyStore.getInstance("BKS", "BC");
            ks.load(null, null);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void generateAndAddKey(String KeyPW, String alias) {
        try {
            // generating random bytes
            SecureRandom secureRandom = SecureRandom.getInstance("DEFAULT", "BC");
            byte[] keyBytes = new byte[16];
            secureRandom.nextBytes(keyBytes);
            // generating key
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            // adding key to keystore
            KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(key);
            KeyStore.ProtectionParameter protection = new KeyStore.PasswordProtection(KeyPW.toCharArray());
            ks.setEntry(alias, entry, protection);
            System.out.println("Stored key as: " + ks.getKey(alias, KeyPW.toCharArray()).toString());

        } catch (Exception e) { e.printStackTrace(); }
    }

    public void store() {
        System.out.println("Storing...");
        try {
            FileOutputStream fOut = new FileOutputStream(storePath);
            printContent();
            ks.store(fOut, storePW);
            fOut.close();
            System.out.println("Save complete");
        } catch (Exception e) { e.printStackTrace(); }
    }


    public KeyStore load(String storePW) {
        System.out.println("Loading...");

        try {
            // step (1)
            ks = KeyStore.getInstance("BKS", "BC");
            // step (2)
            char[] pw = storePW.toCharArray();
            this.storePW = pw;
            FileInputStream fis = new FileInputStream(storePath);
            ks.load(fis, pw);
            fis.close();
            printContent();

        }
        catch (Exception e) {e.printStackTrace();}

        return ks;

    }

    public void printContent() throws KeyStoreException {
        System.out.println("Password: " + Arrays.toString(storePW));
        System.out.println("Number of keys in store: " + ks.size());
        System.out.println("Key aliase: ");
        for (Iterator<String> i = ks.aliases().asIterator(); i.hasNext(); ) {
            System.out.println(i.next());
        }
    }

    public boolean checkStorePW(String storePW) {
        if (storePW.length()<1){
            return  false;
        }

        KeyStore ks = null;
        try {
            // step (1)
            ks = KeyStore.getInstance("BKS", "BC");
            // step (2)
            char[] pw = storePW.toCharArray();
            FileInputStream fis = new FileInputStream(storePath);
            try {
                ks.load(fis, pw);
            } catch (Exception e) {
                if (e instanceof IOException){
                    return false;
                }
                else { e.printStackTrace();}
            }
            fis.close();
        }
        catch (Exception e) {e.printStackTrace();}
        return true;
    }

    public String getStorePW(){
        return Arrays.toString(storePW);
    }

    public SecretKeySpec getKey(String alias, String pw) {
        SecretKeySpec key = null;
        try {
            //KeyStore ks = load(storePath);
            key = (SecretKeySpec) ks.getKey(alias, pw.toCharArray());
        } catch (Exception e) { e.printStackTrace(); }
        return key;
    }

    public int size(){
        int size = 0;
        try {
            size = ks.size();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return size;
    }


    public Enumeration<String> getKeyAliases() throws KeyStoreException {
    return ks.aliases();
    }

}
