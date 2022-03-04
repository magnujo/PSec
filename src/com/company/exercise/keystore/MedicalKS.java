package com.company.exercise.keystore;

import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.sql.SQLOutput;
import java.util.Scanner;

public class MedicalKS {

    static String dir = "src/com/company/exercise/keystore";
    static String storeFileName = dir + "/" + "keystore.bks";
    static char[] storePW,
                  secretKeyPW;

    public static void main(String[] args) {
        KeyStore ks = createKeyStore();
        generateAndAddKey(ks);
        store(ks, storeFileName);
        getKey(storeFileName);
    }

    public static KeyStore createKeyStore() {
        KeyStore store = null;
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter new password for secretkey:");
            secretKeyPW = scanner.nextLine().toCharArray();
            System.out.print("Enter new password for store:");
            storePW = scanner.nextLine().toCharArray();
            store = KeyStore.getInstance("BKS", "BC");
            store.load(null, null);
        } catch (Exception e) { e.printStackTrace(); }
        return store;
    }

    public static void generateAndAddKey(KeyStore store) {
        try {
            // generating random bytes
            SecureRandom secureRandom
                    = SecureRandom.getInstance("DEFAULT", "BC");
            byte[] keyBytes = new byte[16];
            secureRandom.nextBytes(keyBytes);
            // generating key
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            // adding key to keystore
            KeyStore.SecretKeyEntry entry
                    = new KeyStore.SecretKeyEntry(key);
            KeyStore.ProtectionParameter protection
                    = new KeyStore.PasswordProtection(secretKeyPW);
            store.setEntry("key", entry, protection);

        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void store(KeyStore store, String path) {
        try {
            FileOutputStream fOut
                    = new FileOutputStream(storeFileName);
            store.store(fOut, storePW);
            fOut.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static KeyStore load(String path) {
        KeyStore ks = null;
        try {
        // step (1)
            ks = KeyStore.getInstance("BKS", "BC");
        // step (2)
            Scanner scanner = new Scanner(System.in);
            System.out.print("Please type password to access the store:");
            char[] pw = scanner.nextLine().toCharArray();
            FileInputStream fis
                    = new FileInputStream(storeFileName);
            ks.load(fis, pw);
            fis.close();
        } catch (Exception e) { e.printStackTrace(); }
        return ks;
    }

    public static SecretKeySpec getKey(String path) {
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
