package encoder;

import image.EncodedImage;
import image.Pixel;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;

public class LsbDecoderTest {
    LsbDecoder decoder = new LsbDecoder();

    @Test
    public void testDecode() throws IOException {
        final BufferedImage encodedImage = ImageIO.read(new File("/Users/dev/Desktop/img2.png"));
//        final byte[] decodingResult = decoder.decode(encodedImage);
//        for (int i = 0; i < decodingResult.length; i++) {
////            System.out.println(Integer.toBinaryString(decodingResult[i]));
//            System.out.println(decodingResult[i]);
//        }
        final EncodedImage e = new EncodedImage(encodedImage);
        e.bitValues().limit(8).forEach(System.out::println);

        int rgb = encodedImage.getRGB(0, 0);
//        int r = Pixel.getRed(rgb);
//        int g = Pixel.getGreen(rgb);
//        int b = Pixel.getBlue(rgb);
//
//        System.out.println(Integer.toBinaryString(r));
//        System.out.println(Integer.toBinaryString(g));
//        System.out.println(Integer.toBinaryString(b));

        LsbEncoderTest.printPixel(rgb);
        LsbEncoderTest.printPixel(encodedImage.getRGB(0,1));

        final byte first = e.bytes2()[0];
        System.out.println(first);
    }
}
