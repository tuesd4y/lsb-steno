package image;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class EncodedImageTest {

    @Test
    public void booleansToByteTest() {
        final List<Boolean> booleans = Arrays.asList(true, false, true, false, true, false, true, false);
        final int res = EncodedImage.booleansToByte(booleans);
        assertThat(res, is(0b0101_0101));
    }

    @Test
    public void getColorTest() throws IOException {
        final BufferedImage bi = ImageIO.read(getClass().getResourceAsStream("/image.jpg"));
        EncodedImage image = new EncodedImage(bi);
        int r = image.getColor(0);
        int g = image.getColor(1);
        int b = image.getColor(2);

        int pixel = bi.getRGB(0, 0);
        System.out.println(Integer.toBinaryString(pixel));

        assertThat(r, is(Pixel.getRed(pixel)));
        assertThat(g, is(Pixel.getGreen(pixel)));
        assertThat(b, is(Pixel.getBlue(pixel)));
    }
}
