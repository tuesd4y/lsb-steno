package me.tuesd4y.lsb.cli.old;

import me.tuesd4y.lsb.encoder.Decoder;
import me.tuesd4y.lsb.encoder.LsbDecoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DecoderMain {
    public static void main(String[] args) throws IOException {
        final BufferedImage encodedImage = ImageIO.read(new File("testResult.png"));
        final Decoder decoder = new LsbDecoder();
        final byte[] decodedBytes = decoder.decode(encodedImage);
        System.out.println(new String(decodedBytes));
    }
}
