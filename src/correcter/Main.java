package correcter;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class Main {

    private static final File fileSend = new File("send.txt");
    private static final File fileEncoded = new File("encoded.txt");
    private static final File fileReceived = new File("received.txt");
    private static final File fileDecoded = new File("decoded.txt");

    public static void main(String[] args) throws IOException {
        System.out.print("Write a mode: ");
        String mode = new Scanner(System.in).next();
        switch (mode) {
            case "encode":
                Encode.encodeFile(fileSend, fileEncoded);
                System.out.println("\n" + fileSend.getName() + ":");
                System.out.println("text view: " + getTextString(fileSend));
                System.out.println("hex view: " + getHexString(fileSend));
                System.out.println("bin view: " + getBinaryString(fileSend));
                System.out.println("\n" + fileEncoded.getName() + ":");
                System.out.println("hex view: " + getHexString(fileEncoded));
                System.out.println("bin view: " + getBinaryString(fileEncoded));
                break;
            case "send":
                Send.transmission(fileEncoded, fileReceived);
                System.out.println("\n" + fileEncoded.getName() + ":");
                System.out.println("hex view: " + getHexString(fileEncoded));
                System.out.println("bin view: " + getBinaryString(fileEncoded));
                System.out.println("\n" + fileReceived.getName() + ":");
                System.out.println("hex view: " + getHexString(fileReceived));
                System.out.println("bin view: " + getBinaryString(fileReceived));
                break;
            case "decode":
                Decode.decodeFile(fileReceived, fileDecoded);
                System.out.println("\n" + fileReceived.getName() + ":");
                System.out.println("hex view: " + getHexString(fileReceived));
                System.out.println("bin view: " + getBinaryString(fileReceived));
                System.out.println("\n" + fileDecoded.getName() + ":");
                System.out.println("text view: " + getTextString(fileDecoded));
                System.out.println("hex view: " + getHexString(fileDecoded));
                System.out.println("bin view: " + getBinaryString(fileDecoded));
                break;
            case "exit":
            case "quit":
                return;
            default:
                System.out.println("Unknown operation, try again.\n");
                break;
        }
    }

    private static String getBinaryString(File fileSend) throws IOException {
        StringBuilder out = new StringBuilder("");
        for (byte byteOfFile : Files.readAllBytes(fileSend.toPath())) {
            out.append(String.format("%8s ", intToBinaryString(Byte.toUnsignedInt(byteOfFile))));
        }
        return out.toString();
    }

    private static String intToBinaryString(int unsignedInt) {
        StringBuilder binaryString = new StringBuilder(8);
        for(int i = 7; i >= 0; i--) {
            binaryString.append((unsignedInt >> i) & 1);
        }
        return binaryString.toString();
    }

    private static String getHexString(File fileSend) throws IOException {
        StringBuilder out = new StringBuilder("");
        for (byte byteOfFile : Files.readAllBytes(fileSend.toPath())) {
            out.append(String.format("%2s", Integer.toHexString(Byte.toUnsignedInt(byteOfFile)).replaceAll("\\s", "0")));
            out.append(' ');
        }
        return out.toString();
    }

    private static String getTextString(File fileSend) throws IOException {
        return Files.readString(fileSend.toPath());
    }
}