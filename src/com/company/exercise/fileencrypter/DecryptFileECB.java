package fileencrypter;

import org.bouncycastle.util.encoders.Hex;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DecryptFileECB {
    public static void main(String[] args) {
        DecryptFileECB d = new DecryptFileECB();
        d.decrypt();
    }
    String dir = "files";
    String mr = "VeryConfidential";
    String plaintextFileName = dir + "/" + mr + "." + "pdf",
            testFile = dir + "/" + mr + "." + "test" + "." + "pdf";
    byte[] keyBytes = Hex.decode("000102030405060708090a0b0c0d0e0f");
    void decrypt() {
        try {
            // Reading
            byte[] input = library.FileUtil.readAllBytes(
                    plaintextFileName); // Decrypting
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "BC");
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] output = cipher.doFinal(input); // Writing
            library.FileUtil.write("AES/ECB/PKCS5Padding", testFile, output);
        } catch (Exception e) { e.printStackTrace();
        }
    }
}
