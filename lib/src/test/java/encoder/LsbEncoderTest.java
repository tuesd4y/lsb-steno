package encoder;

import image.Pixel;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static matchers.CustomMatchers.endingWith0;
import static matchers.CustomMatchers.endingWith1;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class LsbEncoderTest {

    private LsbEncoder encoder = new LsbEncoder();
    private LsbDecoder decoder = new LsbDecoder();

    @Test
    public void writeDataToPixelTest() {

        int r = 0b1111_1111;
        int g = 0b1111_1111;
        int b = 0b1111_1111;
        int pixel = Pixel.generateRGBAPixel(r, g, b, 0b1111_1111);

        byte[] data = new byte[]{0b0100_0011};

        int newPixel = encoder.writeDataToPixel(data, 0, pixel);
        assertThat(Pixel.getRed(newPixel), is(endingWith1));
        assertThat(Pixel.getGreen(newPixel), is(endingWith1));
        assertThat(Pixel.getBlue(newPixel), is(endingWith0));
    }

    @Test
    public void writeDataToCertainImage() throws IOException, MessageTooLongException {

        final String text = "Testtext";
        final byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        final int length = bytes.length;

        final String sizeString = String.format("%d;", length);
        final byte[] info = sizeString.getBytes(StandardCharsets.UTF_8);

        encoder.encode(bytes, ImageIO.read(getClass().getResourceAsStream("/testImage.jpeg")));
    }

    @Test
    public void writeAndReadImage() throws IOException, MessageTooLongException {
        // defining stuff to encode
        final String text = "Testtextü";

        // do the encoding
        final BufferedImage toEncode = ImageIO.read(getClass().getResourceAsStream("/image.jpg"));
        final BufferedImage result = encoder.encode(text, toEncode);
        final File tempFile = File.createTempFile("test", ".png");
        ImageIO.write(result, "png", tempFile);

        // testing the decoding
        final BufferedImage decodingInput = ImageIO.read(tempFile);
        final byte[] resultBytes = decoder.decode(decodingInput);

        String res = new String(resultBytes);
        assertThat(res, is(text));
    }

    @Test(expected = MessageTooLongException.class)
    public void messageTooLongTest() throws IOException, MessageTooLongException {
        // read image to test
        final BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/img2.png"));

        // calculate maximum data possible to encode and create array with that size
        final int area = img.getWidth() * img.getHeight();
        final int maxBytes = area * 3 / 8;
        final byte[] data = new byte[maxBytes - 1];

        // try to encode too much data
        encoder.encode(data, img);
    }

    @Test
    public void readImageTest() throws IOException {
        printPixel(ImageIO.read(getClass().getResourceAsStream("/img2.png")).getRGB(0, 0));
    }

    @Test
    public void testEncodeDecodeBytes() throws IOException, MessageTooLongException {
        // read image to test
        final BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/img2.png"));

        // create and fill big (but not too big array
        final byte[] data = new byte[33];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) ('A' + i % 26);
        }

        // encode and decode data
        final BufferedImage encoded = encoder.encode(data, img);
        final byte[] decoded = decoder.decode(encoded);

        assertThat(decoded, is(data));
    }

    public static void printPixel(int pixel) {
        final int red = Pixel.getRed(pixel);
        final int green = Pixel.getGreen(pixel);
        final int blue = Pixel.getBlue(pixel);

        System.out.printf("red: %3d - %s\n", red, Integer.toBinaryString(red));
        System.out.printf("green: %3d - %s\n", green, Integer.toBinaryString(green));
        System.out.printf("blue: %3d - %s\n", blue, Integer.toBinaryString(blue));
    }
}