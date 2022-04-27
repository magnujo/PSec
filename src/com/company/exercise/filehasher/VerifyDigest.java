package com.company.exercise.filehasher;

import com.company.exercise.FileEncrypterMVC.FileUtil;

import java.security.MessageDigest;

public class VerifyDigest {
    public static void main(String[] args) {
        VerifyDigest vd = new VerifyDigest();
        vd.verify();
    }
    String dir = "src/com/company/exercise/files";
    String mr = "VeryConfidential";
    String plaintextFileName = dir + "/" + mr + "." + "pdf";
    //String testfile = dir + "/" + mr + "." + "test" + "." + "pdf";
    void verify(){
        try {
            /*// Reading ciphertext file
            ..
            // getting key
            ..
            // Decrypting
            ..
            byte[] plaintext = cipher.doFinal(ciphertext);*/

            // to begin with, you may use original plaintext

            // Verifying hash
            byte[] storedHashValue
                    = FileUtil.readAllBytes(plaintextFileName + ".sha256");
            MessageDigest digest = MessageDigest.getInstance("SHA-256", "BC");
            digest.update(FileUtil.readAllBytes(plaintextFileName));
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

}
