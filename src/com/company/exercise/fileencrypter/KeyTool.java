package com.company.exercise.fileencrypter;

import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Scanner;

public class KeyTool {
    ArrayList<char[]> KeyPasswords;
    char[] storePW;
    String storePath = "src/com/company/exercise/fileencrypter/keystore.bks";


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
        KeyStore store = null;
        try {
            store = KeyStore.getInstance("BKS", "BC");
            store.load(null, null);
        } catch (Exception e) { e.printStackTrace(); }
        store(store);
    }

    public void generateAndAddKey(KeyStore store) {
        try {
            System.out.print("Enter new password for new secretkey:");
            // generating random bytes
            SecureRandom secureRandom = SecureRandom.getInstance("DEFAULT", "BC");
            byte[] keyBytes = new byte[16];
            secureRandom.nextBytes(keyBytes);
            // generating key
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            // adding key to keystore
            KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(key);
            KeyStore.ProtectionParameter protection = new KeyStore.PasswordProtection(KeyPasswords.get(0));
            store.setEntry("key", entry, protection);

        } catch (Exception e) { e.printStackTrace(); }
    }

    public void store(KeyStore store) {
        try {
            FileOutputStream fOut = new FileOutputStream(storePath);
            store.store(fOut, storePW);
            fOut.close();
        } catch (Exception e) { e.printStackTrace(); }
    }


    public KeyStore load(String storePW) {
        KeyStore ks = null;
        try {
            // step (1)
            ks = KeyStore.getInstance("BKS", "BC");
            // step (2)
            char[] pw = storePW.toCharArray();
            FileInputStream fis = new FileInputStream(storePath);
            try {
                ks.load(fis, pw);
            }
            catch (Exception e) {
                if (e instanceof IOException) {
                    System.out.println("Wrong password");
                }
                else {
                    e.printStackTrace();
                }
            }
            fis.close();
        }
        catch (Exception e) {e.printStackTrace();}
        return ks;

    }

    public boolean checkStorePW(String storePW) {
        KeyStore ks = null;
        try {
            // step (1)
            ks = KeyStore.getInstance("BKS", "BC");
            // step (2)
            char[] pw = storePW.toCharArray();
            FileInputStream fis = new FileInputStream(storePath);
            try {
                ks.load(fis, pw);
            }
            catch (Exception e) {
                if (e instanceof IOException) {
                    System.out.println("Wrong password");
                    return false;
                }
                else {
                    e.printStackTrace();
                }
            }
            fis.close();
        }
        catch (Exception e) {e.printStackTrace();}
        return true;

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
