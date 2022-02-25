package fileencrypter;

import org.bouncycastle.util.encoders.Hex;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DecryptFileECB {
    public static void main(String[] args) {
        DecryptFileECB d = new DecryptFileECB();
        d.decrypt();
    }
    String dir = "src/com/company/exercise/files";
    String mr = "VeryConfidential";
    String plaintextFileName = dir + "/" + mr + "." + "pdf",
            testFile = dir + "/" + mr + "." + "test" + "." + "pdf";
    byte[] keyBytes = Hex.decode("000102030405060708090a0b0c0d0e0f");
    void decrypt() {
        try {
            // Reading
            System.out.println(plaintextFileName);
            byte[] input = library.FileUtil.readAllBytes(
                    plaintextFileName + "." + "aes");
            System.out.println(input);
            // Decrypting
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "BC");
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] output = cipher.doFinal(input);
            // Writing
            //library.FileUtil.write("",testFile, output);
        } catch (Exception e) { e.printStackTrace();
        }
    }
}
