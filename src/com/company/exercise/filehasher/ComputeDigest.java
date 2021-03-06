package com.company.exercise.filehasher;

import com.company.exercise.FileEncrypterMVC.FileUtil;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;

public class ComputeDigest {
    public static void main(String[] args) {
        ComputeDigest cd = new ComputeDigest();
        cd.hash();
    }
    String dir = "src/com/company/exercise/files";
    String mr = "VeryConfidential";
    String plaintextFileName = dir + "/" + mr + "." + "pdf";
    //String testfile = dir + "/" + mr + "." + "test" + "." + "pdf";
    void hash(){
        try {
            // reading plaintext file
            byte [] input = FileUtil.readAllBytes(plaintextFileName);

            // computing hash value of plaintext
            //MessageDigest digest = MessageDigest.getInstance("SHA-256", "BC");
            Cipher digest = Cipher.getInstance("SHA-256", "BC");

            byte[] hashValue = digest.doFinal(input);
            System.out.println("Hashvalue: " + Hex.toHexString(hashValue));

            // writing hash value to file
            String hashFileName = plaintextFileName;
            FileUtil.write("sha256", hashFileName, hashValue);
        } catch (Exception e) { e.printStackTrace(); }
    }

}
