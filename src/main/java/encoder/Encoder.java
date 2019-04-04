package encoder;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface Encoder {
    BufferedImage encode(byte[] data, BufferedImage image) throws MessageTooLongException;

    default BufferedImage encode(String text, Charset charset, BufferedImage image) throws MessageTooLongException {
        return encode(text.getBytes(charset), image);
    }

    default BufferedImage encode(String text, BufferedImage image) throws MessageTooLongException {
        return encode(text, StandardCharsets.UTF_8, image);
    }
}
