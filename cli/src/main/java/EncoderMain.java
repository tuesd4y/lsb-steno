import encoder.Encoder;
import encoder.LsbEncoder;
import encoder.MessageTooLongException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;

public class EncoderMain {
    public static void main(String[] args) throws IOException, MessageTooLongException {
        final BufferedImage image = ImageIO.read(EncoderMain.class.getResourceAsStream("/testImage.jpg"));
        final Encoder encoder = new LsbEncoder();
        final BufferedImage newImage = encoder.encode("Baum", image);

        ImageIO.write(newImage, "png", new File("testResult.png"));

    }
}
