import encoder.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LsbTest {
    public static void main(String[] args) throws IOException, MessageTooLongException {
        final Decoder decoder = new LsbDecoder();
        final Encoder encoder = new LsbEncoder();

        final BufferedImage image = ImageIO.read(LsbTest.class.getResourceAsStream("/WhatsApp Image 2019-03-27 at 15.37.19.jpeg"));
        final BufferedImage encoded = encoder.encode("Testtext", image);
        final File tempFile = File.createTempFile("nothing_", ".png");
        ImageIO.write(encoded, "png", tempFile);
        System.out.println(tempFile.getAbsoluteFile());

        final byte[] decoded = decoder.decode(encoded);
        final String decodedString = new String(decoded, StandardCharsets.UTF_8);
        System.out.println(decodedString);
    }
}
