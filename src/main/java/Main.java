import encoder.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) throws IOException, MessageTooLongException {
        final Decoder decoder = new LsbDecoder();
        final Encoder encoder = new LsbEncoder();

        final BufferedImage image = ImageIO.read(Main.class.getResourceAsStream("/WhatsApp Image 2019-03-27 at 15.37.19.jpeg"));
        final byte[] data = "hello my name is chris!".getBytes(StandardCharsets.UTF_8);
        final BufferedImage encoded = encoder.encode(data, image);
        final File tempFile = File.createTempFile("nothing_", ".jpg");
        ImageIO.write(encoded, "jpg", tempFile);
        System.out.println(tempFile.getAbsoluteFile());

        final byte[] decoded = decoder.decode(encoded);
        final String decodedString = new String(decoded, StandardCharsets.UTF_8);
        System.out.println(decodedString);
    }
}
