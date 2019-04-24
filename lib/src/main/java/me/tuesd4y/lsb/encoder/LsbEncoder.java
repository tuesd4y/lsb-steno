package me.tuesd4y.lsb.encoder;

import me.tuesd4y.lsb.image.Pixel;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static me.tuesd4y.lsb.encoder.ByteHelper.setLsb;

public class LsbEncoder implements Encoder {

//    @Override
    BufferedImage encode(byte[] data, Charset charset, BufferedImage image) throws MessageTooLongException {
        final BufferedImage copy = copyImage(image);
        writeIntoImage(data, charset, copy);
        return copy;
    }

//    @Override
    public BufferedImage encode(byte[] data, BufferedImage image) throws MessageTooLongException {
        return encode(data, StandardCharsets.UTF_8, image);
    }

//    @Override
    public BufferedImage encode(String text, Charset charset, BufferedImage image) throws MessageTooLongException {
        return encode(text.getBytes(charset), charset, image);
    }

    @Override
    public BufferedImage encode(String text, BufferedImage image) throws MessageTooLongException {
        return encode(text, StandardCharsets.UTF_8, image);
    }

    private void writeIntoImage(byte[] data, Charset charset, BufferedImage bi) throws MessageTooLongException {
        // compute how much bytes we need to encode the data
        final String sizeString = String.format("%d;", data.length);
        final byte[] info = sizeString.getBytes(charset);

        final int width = bi.getWidth();
        final int height = bi.getHeight();

        final int maxSize = width * height * 3 / 8;
        final int messageSize = data.length + info.length;

        if (maxSize < messageSize) {
            throw new MessageTooLongException(
                    String.format("Trying to store %d bytes when only %d are available", messageSize, maxSize));
        }

        // copy all the bytes to write into one array
        final byte[] allBytes = new byte[messageSize];
        System.arraycopy(info, 0, allBytes, 0, info.length);
        System.arraycopy(data, 0, allBytes, info.length, data.length);

        outside:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                final int i = 3 * (y * width + x);
                final int pixel = bi.getRGB(x, y);
                bi.setRGB(x, y, writeDataToPixel(allBytes, i, pixel));
                if (i / 8 >= allBytes.length) {
                    break outside;
                }
            }
        }
    }

    int writeDataToPixel(byte[] data, int i, int pixel) {
        boolean lsbR = false;
        boolean lsbG = false;
        boolean lsbB = false;

        try {
            lsbR = ByteHelper.getBitAtIndex(data, i);
            lsbG = ByteHelper.getBitAtIndex(data, i + 1);
            lsbB = ByteHelper.getBitAtIndex(data, i + 2);
        } catch (ArrayIndexOutOfBoundsException e) {
            // i is now not in the array anymore
            // we can ignore this, since we handle this in the caller method anyways...
        }

        int r = Pixel.getRed(pixel);
        int g = Pixel.getGreen(pixel);
        int b = Pixel.getBlue(pixel);
        int alpha = Pixel.getAlpha(pixel);

        return Pixel.generateRGBAPixel(setLsb(r, lsbR), setLsb(g, lsbG), setLsb(b, lsbB), alpha);
    }

    private BufferedImage copyImage(BufferedImage source) {
        ColorModel cm = source.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = source.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
