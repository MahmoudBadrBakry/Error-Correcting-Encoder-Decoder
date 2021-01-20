package correcter;

import java.io.*;
import java.util.Random;

public class Send {
    private static final Random random = new Random();


    public static void transmission(File fileEncoded, File fileReceived) throws IOException{
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileEncoded));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileReceived));
        bos.write(simulateErrors(bis.readAllBytes()));
        bis.close();
        bos.close();
    }

    private static byte[] simulateErrors(byte[] encodedBytes) {
        byte[] erroredBytes = new byte[encodedBytes.length];
        for (int i = 0; i < encodedBytes.length; i++) {
            erroredBytes[i] = applyRandomErrorToByte(encodedBytes[i]);
        }
        return erroredBytes;
    }

    private static byte applyRandomErrorToByte(byte encodedByte) {
        return (byte) (encodedByte ^ getRandomError());
    }

    private static byte getRandomError() {
        return (byte) (1 << random.nextInt(8));
    }
}
