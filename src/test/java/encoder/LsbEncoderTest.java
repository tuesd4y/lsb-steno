package encoder;

import image.Pixel;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static matchers.CustomMatchers.endingWith0;
import static matchers.CustomMatchers.endingWith1;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class LsbEncoderTest {

    private LsbEncoder encoder = new LsbEncoder();

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

        final boolean[][] writtenData = new boolean[bytes.length + info.length+1][8];
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
            } else if(i < bytes.length) {
                assertThat("Problem at position " + i, writtenBytes[i], is(bytes[i - info.length]));
            }
        }
    }

    @Test
    public void writeAndReadImage() throws IOException, MessageTooLongException {
        final String text = "Testtext";
        final byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        final int length = bytes.length;

        final String sizeString = String.format("%d;", length);
        final byte[] info = sizeString.getBytes(StandardCharsets.UTF_8);

        System.out.println(Integer.toBinaryString(info[0]));
        final BufferedImage toEncode = ImageIO.read(getClass().getResourceAsStream("/image.jpg"));
        final BufferedImage result = encoder.encode(bytes, toEncode);
        ImageIO.write(result, "jpg", new File("/Users/dev/Desktop/img2.jpg"));
        for (int i = 0; i < 11 * 10; i++) {
            int oldPixel = toEncode.getRGB(i % 10, i /10);
            int newPixel = result.getRGB(i % 10, i /10);

//                p(Pixel.getRed(oldPixel));
                p(Pixel.getRed(newPixel));
//                p(Pixel.getGreen(oldPixel));
                p(Pixel.getGreen(newPixel));
//                p(Pixel.getBlue(oldPixel));
                p(Pixel.getBlue(newPixel));
                System.out.println("---");
        }
        System.out.println("abc");
    }

    private void p(int s) {
        System.out.println(Integer.toBinaryString(s));
    }
}