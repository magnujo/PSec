package library;

import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
    public static byte[] readAllBytes(String plaintextFileName) {
        byte[] bytesRead = {};
        try {
            bytesRead = Files.readAllBytes(Paths.get(plaintextFileName));
        } catch (Exception e) {}
        return bytesRead; // returns {} if file does not exist
    }

    public static void write(String transformation,
                             String plaintextFileName, byte[] output) {
        String outFile = "";
        String[] parts = transformation.split("/");
        if (parts.length == 3 && parts[0].equals("AES")) {
            outFile = plaintextFileName + ".aes";
            System.out.println("Aes");
        } else {
            System.out.println("Noaes");
            outFile = plaintextFileName + ".notaes";
        }
        try {
            Files.write(Paths.get(outFile), output);
            System.out.println("writing...");
        } catch (Exception e) { e.printStackTrace(); }
    }
}