package fileencrypter;

import org.bouncycastle.util.encoders.Hex;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;



public class EncryptFileECB {
    public static void main(String[] args) {
        EncryptFileECB e = new EncryptFileECB();
        e.encrypt();
    }
    String dir = "files";
    String plaintextFileName = dir + "/" + "VeryConfidential.pdf";
    byte[] keyBytes = Hex.decode("000102030405060708090a0b0c0d0e0f");
    void encrypt() {
        try {
// reading
            byte [] input = library.FileUtil.readAllBytes(plaintextFileName);
// encrypting
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "BC");
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] output = cipher.doFinal(input);
// writing
            library.FileUtil.write("AES/ECB/PKCS5Padding", plaintextFileName, output);
            System.out.println("Slut");
        } catch (Exception e) { e.printStackTrace(); }
    }

}
