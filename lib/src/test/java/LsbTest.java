import encoder.*;
import org.junit.Ignore;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LsbTest {
    public static void main(String[] args) throws IOException, MessageTooLongException {
        final Decoder decoder = new LsbDecoder();
        final Encoder encoder = new LsbEncoder();

        final BufferedImage image = ImageIO.read(LsbTest.class.getResourceAsStream("/image.jpg"));
        final BufferedImage encoded = encoder.encode("Testtext", image);
        final File tempFile = File.createTempFile("nothing_", ".png");
        ImageIO.write(encoded, "png", tempFile);
        System.out.println(tempFile.getAbsoluteFile());

        final byte[] decoded = decoder.decode(encoded);
        final String decodedString = new String(decoded, StandardCharsets.UTF_8);
        System.out.println(decodedString);
    }

    @Ignore
    @Test
    public void encodeDecodeWithDifferentCharset() throws IOException, MessageTooLongException {
        //fixme currently not working...
//        final Decoder decoder = new LsbDecoder();
//        final Encoder encoder = new LsbEncoder();
//
//        final String textToDecode = "TestAbcDe";
//        final Charset charset = StandardCharsets.UTF_8;
//
//        final BufferedImage image = ImageIO.read(LsbTest.class.getResourceAsStream("/image.jpg"));
//        final BufferedImage encoded = encoder.encode(textToDecode, charset, image);
//        final File tempFile = File.createTempFile("nothing_", ".png");
//        ImageIO.write(encoded, "png", tempFile);
//
//        final byte[] result = decoder.decode(image, charset);
//
//        assertThat(result, is(charset.encode(textToDecode)));
    }
}
