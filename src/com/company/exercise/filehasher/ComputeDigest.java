package com.company.exercise.filehasher;

import org.bouncycastle.util.encoders.Hex;

import java.security.MessageDigest;

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
            byte [] input = library.FileUtil.readAllBytes(plaintextFileName);

            // computing hash value of plaintext
            MessageDigest digest = MessageDigest.getInstance("SHA-256", "BC");
            byte[] hashValue = digest.digest(input);
            System.out.println("Hashvalue: " + Hex.toHexString(hashValue));

            // writing hash value to file
            String hashFileName = plaintextFileName + "." + "sha256";
            library.FileUtil.write("", hashFileName, hashValue);
        } catch (Exception e) { e.printStackTrace(); }
    }

}
