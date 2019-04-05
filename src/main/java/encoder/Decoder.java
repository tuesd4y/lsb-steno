package encoder;

import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.nio.charset.Charset;

public interface Decoder {
    byte[] decode(BufferedImage image);

    byte[] decode(BufferedImage image, Charset charset);
    byte[] decode(BufferedImage image, char delimiter);
    byte[] decode(BufferedImage image, Charset charset, char delimiter);
}
