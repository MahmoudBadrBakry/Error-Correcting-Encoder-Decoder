package correcter;

import java.io.*;

class AnotherDecode {

    public static void decodeFile(File in, File out) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(in));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(out));

        bos.write(eightThreeDecode(bis.readAllBytes()));

        bis.close();
        bos.close();
    }

    private static byte[] eightThreeDecode(byte[] raw) {
        byte[] result = new byte[raw.length * 3 / 8]; // Integer division, auto-drop extra trailing zeros.
        int[] buffer = new int[result.length * 8];

        for (int i = 0; i < buffer.length; i++) {
            int bitAtRawSequence = i * 2 + i / 3 * 2;
            int byteAtRaw = bitAtRawSequence / 8;
            int bitAtRawByte = bitAtRawSequence % 8;

            // buffer[] stores binary data in original form.
            // e.g. {0,1,1,0,1,0,0,0} for 'h'.
            int[] corrected = correct(raw[byteAtRaw]);
            buffer[i] = corrected[bitAtRawByte / 2];

            if ((i + 1) % 8 == 0) {
                for (int j = 0; j < 8; j++) {
                    result[i / 8] |= (byte) buffer[i - j] << j;
                }
            }
        }
        System.out.println("anotherDecoded");
        for (int i = 0; i < result.length; i++) {
            System.out.println(Byte.toUnsignedInt(result[i]));
        }
        return result;
    }

    /**
     * Correct binary data using parity checksum.
     * Assume only one bit error in one byte.
     * Do nothing when parity code gets error.
     *
     * @return Corrected bit array in original order. e.g. 1011_1100 -> 0011_11 -> {0,1,1}.
     */
    private static int[] correct(byte b) {
        if (getBit(b, 7) != getBit(b, 6)) {
            return new int[]{
                    getBit(b, 5) ^ getBit(b, 3) ^ getBit(b, 1),
                    getBit(b, 5),
                    getBit(b, 3)
            };
        } else if (getBit(b, 5) != getBit(b, 4)) {
            return new int[]{
                    getBit(b, 7),
                    getBit(b, 7) ^ getBit(b, 3) ^ getBit(b, 1),
                    getBit(b, 3)
            };
        } else if (getBit(b, 3) != getBit(b, 2)) {
            return new int[]{
                    getBit(b, 7),
                    getBit(b, 5),
                    getBit(b, 5) ^ getBit(b, 7) ^ getBit(b, 1)
            };
        } else {
            return new int[]{
                    getBit(b, 7),
                    getBit(b, 5),
                    getBit(b, 3)
            };
        }
    }

    private static int getBit(byte i, int pos) {
        return 1 & (i >> pos);
    }

}
