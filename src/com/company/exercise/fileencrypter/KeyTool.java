package com.company.exercise.fileencrypter;

import javax.crypto.ExemptionMechanismException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

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

    public void generateAndAddKey(String KeyPW) {
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
            ks.setEntry("key", entry, protection);

        } catch (Exception e) { e.printStackTrace(); }
    }

    public void store() {
        try {
            FileOutputStream fOut = new FileOutputStream(storePath);
            System.out.println("Storing pw as: " + Arrays.toString(storePW));
            ks.store(fOut, storePW);
            fOut.close();
            System.out.println("Save complete");
        } catch (Exception e) { e.printStackTrace(); }
    }


    public KeyStore load(String storePW) {

        try {
            // step (1)
            ks = KeyStore.getInstance("BKS", "BC");
            // step (2)
            char[] pw = storePW.toCharArray();
            this.storePW = pw;
            FileInputStream fis = new FileInputStream(storePath);
            ks.load(fis, pw);
            System.out.println(ks.aliases());
            fis.close();
        }
        catch (Exception e) {e.printStackTrace();}

        return ks;

    }

    public boolean checkStorePW(String storePW) {
        if (storePW.length()<1){
            System.out.println("Hej");
            return  false;
        }
        System.out.println("hejehej");
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

    public SecretKeySpec getKey(String path) {
        SecretKeySpec key = null;
        try {
            KeyStore ks = load(path);
            System.out.print("Please type password to access key: ");
            Scanner scanner = new Scanner(System.in);
            char[] pw = scanner.nextLine().toCharArray();
            key = (SecretKeySpec) ks.getKey("key", pw);
            System.out.println(key);
        } catch (Exception e) { e.printStackTrace(); }
        return key;
    }

}
