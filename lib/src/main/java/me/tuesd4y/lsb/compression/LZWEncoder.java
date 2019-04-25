package me.tuesd4y.lsb.compression;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class LZWEncoder {

    private byte[] data;
    private Map<String, Integer> encodeTable;
    private final String EMPTY = "";
    private float compRatio;

    LZWEncoder(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public float getCompRatio() {
        return compRatio;
    }

    private void createEncodeTable() {
        encodeTable = new TreeMap<>();
        for (int i = 0; i < 256; i++) {
            encodeTable.put(String.format("%02x", i), i);
        }
    }

    public void encode() {
        byte[] orig = data.clone();
        int i = 9;
        while (!encodeToNBits(i)) { // increase i until table size fits
            i++;
        }
        if (orig.length <= data.length) { // if bigger than before
            data = new byte[orig.length + 1];
            System.arraycopy(orig, 0, data, 1, data.length - 1);
        }
        compRatio = (float) orig.length / data.length;
    }

    public boolean encodeToNBits(int nBits) {

        ByteArrayInputStream in = null;
        BitOutputStream out = null;
        int limit = (int) Math.pow(2, nBits);

        try {

            in = new ByteArrayInputStream(data);
            out = new BitOutputStream();

            out.write(nBits, 8); // write chunksize for decoding

            String prefix = EMPTY, muster, suffix;

            byte[] buffer = new byte[1];

            createEncodeTable();
            int k = encodeTable.size();

            while (in.available() > 0) { // while EOF not reached

                in.read(buffer);
                suffix = String.format("%02x", buffer[0] + 128); // next "byte" to char
                muster = prefix + suffix;

                if (encodeTable.containsKey(muster)) { // if muster in codetable
                    prefix = muster;
                } else {
                    encodeTable.put(muster, k++); // add muster to code-table

                    if (k > limit) { // code-table size is too small
                        return false;
                    }

                    out.write(encodeTable.get(prefix), nBits); // write LZW-code of präfix
                    prefix = suffix;
                }
            }

            if (!prefix.isEmpty()) {
                out.write(encodeTable.get(prefix), nBits); // write LZW-code of präfix
            }
            out.flush();
            data = out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (Exception ignore) {
            }
        }
        return true;
    }

    public static byte[] encodeFile(File file) {
        final FileHandler fh = new FileHandler();
        final byte[] bytes = fh.fileToByteArray(file);
        LZWEncoder en = new LZWEncoder(bytes);
        en.encode();
        return en.getData();
    }
}
