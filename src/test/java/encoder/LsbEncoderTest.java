package encoder;

import image.Pixel;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

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

        final boolean[][] writtenData = new boolean[bytes.length + info.length + 1][8];
        AtomicInteger counter = new AtomicInteger();
        encoder.applyAfterWrite = b -> {
            writtenData[counter.get() / 8][counter.get() % 8] = b;
            counter.getAndIncrement();
        };

        encoder.encode(bytes, ImageIO.read(getClass().getResourceAsStream("/testImage.jpeg")));

        Object[] writtenBytes = Arrays.stream(writtenData).map(data -> {
            byte b = 0b0;
            b |= data[7] ? 0b1 : 0b0;
            for (int i = 6; i >= 0; i--) {
                b <<= 1;
                b |= data[i] ? 0b1 : 0b0;
            }

            return b;
        }).toArray();

        for (int i = 0; i < writtenBytes.length; i++) {
            if (i < info.length) {
                assertThat("Problem at position " + i, writtenBytes[i], is(info[i]));
            } else if (i < bytes.length) {
                assertThat("Problem at position " + i, writtenBytes[i], is(bytes[i - info.length]));
            }
        }
    }

    @Test
    public void writeAndReadImage() throws IOException, MessageTooLongException {
        // defining stuff to encode
        final String text = "Testtext";
        final byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        final int length = bytes.length;

        final String sizeString = String.format("%d;", length);
        final byte[] info = sizeString.getBytes(StandardCharsets.UTF_8);

        // do the encoding
        final BufferedImage toEncode = ImageIO.read(getClass().getResourceAsStream("/image.jpg"));
        final BufferedImage result = encoder.encode(text, toEncode);
        ImageIO.write(result, "png", new File("/Users/dev/Desktop/img2.png"));

//        for (int i = 0; i < 11 * 10; i++) {
//            int oldPixel = toEncode.getRGB(i % 10, i / 10);
//            int newPixel = result.getRGB(i % 10, i / 10);
//            int newlyReadPixel = decodingInput.getRGB(i % 10, i / 10);
//
////                p(Pixel.getRed(oldPixel));
////                p(Pixel.getGreen(oldPixel));
////                p(Pixel.getBlue(oldPixel));
//            printPixel(newPixel);
//            assertThat(newPixel, is(newlyReadPixel));
//            System.out.println("---");
//        }
//        System.out.println("abc");

        // testing the decoding
        final BufferedImage decodingInput = ImageIO.read(new File("/Users/dev/Desktop/img2.png"));
        final byte[] resultBytes = decoder.decode(decodingInput);
        for (byte resultByte : resultBytes) {
            System.out.printf("%c", resultByte);
        }
        System.out.println(resultBytes);
        System.out.println(StandardCharsets.UTF_8.decode(ByteBuffer.wrap(resultBytes)));
//        System.out.println(new String(resultBytes, StandardCharsets.UTF_8));
    }

    @Test
    public void readImageTest() throws IOException {

        printPixel(ImageIO.read(new File("/Users/dev/Desktop/img2.png")).getRGB(0, 0));
    }

    private void p(int s) {
        System.out.println(Integer.toBinaryString(s));
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