package encoder;
public class ByteHelper {
    public static boolean getBitAtIndex(byte[] data, int i) {
        byte b = data[i / 8];
        int pos = (byte) (i % 8);
        return (b & (0b1 << pos)) == 0b1 << pos;
    }

    public static int switchLsb(boolean shouldSwitch, int b) {
        if (!shouldSwitch) return b;

        else if ((b & 0b1) == 0b1) {
            return b >> 1 << 1;
        } else {
            return b | 0b1;
        }
    }

    public static int setLsb(int origin, boolean newBit) {
        if (newBit) {
            return origin | 0b1;
        } else {
            return origin >> 1 << 1;
        }
    }

    public static void setBitAtIndex(byte[] data, int i, boolean value) {
        if (value) {
            data[i / 8] |= (0b1 << (i % 8));
        } else {
            data[i / 8] &= (0b1111_1111 ^ (0b1 << (i % 8)));
        }
    }
}
