package me.tuesd4y.lsb.image;

import java.awt.image.BufferedImage;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class EncodedImage implements Iterable<Integer> {

    private Charset encodingCharset = StandardCharsets.UTF_8;
    private byte metaDataDelimiter = encodingCharset.encode(CharBuffer.wrap(";")).get();

    public Charset getEncodingCharset() {
        return encodingCharset;
    }

    public void setEncodingCharset(Charset encodingCharset) {
        this.encodingCharset = encodingCharset;
    }

    public byte getMetaDataDelimiter() {
        return metaDataDelimiter;
    }

    public void setMetaDataDelimiter(byte metaDataDelimiter) {
        if (Character.isDigit(metaDataDelimiter)) {
            throw new IllegalArgumentException("Unable to set digit as delimiter, this would confuse the decoding algorithm...");
        }
        this.metaDataDelimiter = metaDataDelimiter;
    }

    public void setMetaDataDelimiter(char delimiter) {
        this.metaDataDelimiter = encodingCharset.encode(CharBuffer.wrap(String.valueOf(delimiter))).get();
    }

    private final int width, height;
    private final BufferedImage image;
    private int area;

    public EncodedImage(BufferedImage image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.area = width * height;
    }

    static byte booleansToByte(List<Boolean> values) {
        byte b = 0b0000_0000;
        if (values.get(values.size() - 1)) {
            b |= 0b1;
        }
        for (int i = values.size() - 2; i >= 0; i--) {
            Boolean value = values.get(i);
            b <<= 1;
            if (value) {
                b |= 0b1;
            }
        }

        return b;
    }

    public int get(int i) {
        return image.getRGB(i % width, i / width);
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < area;
            }

            @Override
            public Integer next() {
                int pixel = get(i);
                i++;
                return pixel;
            }
        };
    }

    public byte[] bytes() {
        final boolean[] bits = new boolean[area * 3];
        for (int i = 0; i < bits.length; i++) {
            int color = getColor(i);

            // check if last bit is a 1
            bits[i] = (color & 0b1) == 0b1;
        }

        byte[] encodedData = null;
        int index = 0;
        int dataEnd = bits.length / 8;

        StringBuilder info = new StringBuilder();

        boolean isInfo = true;

        // go through each eight-bit group
        for (int i = 0; i < dataEnd; i++) {
            int x = i * 8;
            byte currentByte = booleansToByte(Arrays.asList(bits[x], bits[x + 1], bits[x + 2], bits[x + 3], bits[x + 4], bits[x + 5], bits[x + 6], bits[x + 7]));

            if (isInfo) {
                // todo: make this variable and not hardcoded...
                if (currentByte == metaDataDelimiter) {
                    isInfo = false;
                    final String infoString = info.toString();
                    final int dataLength = Integer.parseInt(infoString);
                    dataEnd = infoString.length() + 1 + dataLength;
                    encodedData = new byte[dataLength];
                } else {
                    info.appendCodePoint(currentByte);
                }
            } else {
                encodedData[index++] = currentByte;
            }
        }
        return encodedData;
    }

    int getColor(int i) {
        int c = get(i / 3);

        switch (i % 3) {
            case 0:
                return Pixel.getRed(c);
            case 1:
                return Pixel.getGreen(c);
            default:
                return Pixel.getBlue(c);
        }
    }
}