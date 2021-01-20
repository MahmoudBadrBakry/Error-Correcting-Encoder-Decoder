package correcter;

import java.io.*;

public class Decode {

    public static void decodeFile(File fileReceived, File fileDecoded) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileReceived));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileDecoded));
        bos.write(decodeStupidSevenFourHamming(bis.readAllBytes()));
        bis.close();
        bos.close();
    }


    private static byte[] decodeStupidSevenFourHamming(byte[] receivedBytes) {
        byte[] decoded = new byte[receivedBytes.length / 2];

        for (int i = 0; i < receivedBytes.length-1; i+=2) {

            byte fixedByteLeft = fixStupidSevenFourHammingByte(receivedBytes[i]);
            byte fixedByteRight = fixStupidSevenFourHammingByte(receivedBytes[i+1]);

            decoded[i/2] = combineTwoStupidSevenFourHammingBytes(fixedByteLeft, fixedByteRight);

        }
        return decoded;
    }

    private static byte combineTwoStupidSevenFourHammingBytes(byte ...fixedBytes) {
        byte combinedByte = 0;
        for (int i = 0; i < 2; i++) {
            combinedByte |= getBit(fixedBytes[i], 5) << (3 + (1-i)*4);
            combinedByte |= getBit(fixedBytes[i], 3) << (2 + (1-i)*4);
            combinedByte |= getBit(fixedBytes[i], 2) << (1 + (1-i)*4);
            combinedByte |= getBit(fixedBytes[i], 1) << ((1-i)*4);
        }

        return combinedByte;
    }

    private static byte fixStupidSevenFourHammingByte(byte sevenFourHammingByte) {
        int p1 = getParity(getBit(sevenFourHammingByte, 5), getBit(sevenFourHammingByte, 3), getBit(sevenFourHammingByte, 1));
        int p2 = getParity(getBit(sevenFourHammingByte, 5), getBit(sevenFourHammingByte, 2), getBit(sevenFourHammingByte, 1));
        int p3 = getParity(getBit(sevenFourHammingByte, 3), getBit(sevenFourHammingByte, 2), getBit(sevenFourHammingByte, 1));

        int receivedP1 = getBit(sevenFourHammingByte, 7);
        int receivedP2 = getBit(sevenFourHammingByte, 6);
        int receivedP3 = getBit(sevenFourHammingByte, 4);

        if(p1 != receivedP1) {
            if(p2 != receivedP2) {
                if(p3 != receivedP3) {
                    sevenFourHammingByte ^= 1 << 1;
                } else {
                    sevenFourHammingByte ^= 1 << 5;
                }
            } else {
                if(p3 != receivedP3) {
                    sevenFourHammingByte ^= 1 << 3;
                }
            }
        } else {
            if(p2 != receivedP2) {
                if(p3 != receivedP3) {
                    sevenFourHammingByte ^= 1 << 2;
                }
            }
        }

        return sevenFourHammingByte;
    }

    private static int getBit(byte i, int pos) {
        return 1 & (i >> pos);
    }

    private static byte getParity(int... args) {
        int parity = 0;
        for (int element : args) {
            parity ^= element;
        }
        return (byte) parity;
    }

}
