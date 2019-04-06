package encoder;

import image.EncodedImage;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LsbDecoderTest {
    LsbDecoder decoder = new LsbDecoder();

    @Test
    public void testDecode() throws IOException {
        final BufferedImage encodedImage = ImageIO.read(getClass().getResourceAsStream("/img2.png"));

        final EncodedImage e = new EncodedImage(encodedImage);

        int rgb = encodedImage.getRGB(0, 0);
        LsbEncoderTest.printPixel(rgb);
        LsbEncoderTest.printPixel(encodedImage.getRGB(0, 1));

        System.out.println(new String(e.bytes()));
    }
}