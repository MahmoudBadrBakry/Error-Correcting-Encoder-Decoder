package correcter;

import java.io.*;


public class Encode {
    public static void encodeFile(File in, File out) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(in));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(out));

        bos.write(sevenFourHammingEncode(bis.readAllBytes()));

        bis.close();
        bos.close();
    }

    private static byte[] sevenFourHammingEncode(byte[] allBytes) {
        byte encodedBytes[] = new byte[allBytes.length * 2];

        int i = 0;
        for (byte rawByte : allBytes) {
            byte left[] = new byte[4];
            byte right[] = new byte[4];
            split8BytesTo4And4Bits(rawByte, left, right);

            encodedBytes[i++] = generateStupidSevenFourHammingBytes(left);
            encodedBytes[i++] = generateStupidSevenFourHammingBytes(right);
        }

        return encodedBytes;
    }

    private static byte generateStupidSevenFourHammingBytes(byte[] fourBits) {
        byte sevenFourHammingByte = 0;

        int p1 = getParity(fourBits[3], fourBits[2], fourBits[0]);
        int p2 = getParity(fourBits[3], fourBits[1], fourBits[0]);
        int p3 = getParity(fourBits[2], fourBits[1], fourBits[0]);

        sevenFourHammingByte |= p1 << 7;
        sevenFourHammingByte |= p2 << 6;
        sevenFourHammingByte |= fourBits[3] << 5;
        sevenFourHammingByte |= p3 << 4;
        sevenFourHammingByte |= fourBits[2] << 3;
        sevenFourHammingByte |= fourBits[1] << 2;
        sevenFourHammingByte |= fourBits[0] << 1;

        return sevenFourHammingByte;
    }



    private static void split8BytesTo4And4Bits(byte raw, byte[] left, byte[] right) {
        for (int i = 3; i >= 0; i--) {
            left[i] = (byte) getBit(raw, i+4);
            right[i] = (byte) getBit(raw, i);
        }
    }

    private static int getBit(int i, int pos) {
        return 1 & (i >> pos);
    }

    private static int getParity(int... args) {
        int parity = 0;
        for (int element : args) {
            parity ^= element;
        }
        return parity;
    }
}
