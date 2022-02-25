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
        }
        else outFile = plaintextFileName;
        try {
            Files.write(Paths.get(outFile), output);
            System.out.println(Paths.get(outFile));
            System.out.println("writing...");
        } catch (Exception e) { e.printStackTrace(); }
    }

    /*public static String getIV(String transformation, String plaintextFileName){
        String outFile = "";
        String iv = "";
        String[] parts = transformation.split("/");
        if (parts.length == 3 && parts[0].equals("AES")) {
            outFile = plaintextFileName + ".aes";
            System.out.println("Aes");
        }
        else outFile = plaintextFileName;
        //return outFile.split(".");
    }*/
}