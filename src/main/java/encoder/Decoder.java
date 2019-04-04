package encoder;

import java.awt.image.BufferedImage;

public interface Decoder {
    public byte[] decode(BufferedImage image);
}
