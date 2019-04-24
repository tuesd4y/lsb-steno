package me.tuesd4y.lsb.encoder;


import me.tuesd4y.lsb.image.EncodedImage;

import java.awt.image.BufferedImage;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class LsbDecoder implements Decoder {

    @Override
    public byte[] decode(BufferedImage image) {
        return decode(image, StandardCharsets.UTF_8);
    }

//    @Override
    byte[] decode(BufferedImage image, Charset charset) {
        return decode(image, charset, ';');
    }

//    @Override
    byte[] decode(BufferedImage image, char delimiter) {
        return decode(image, StandardCharsets.UTF_8, delimiter);
    }

//    @Override
    byte[] decode(BufferedImage image, Charset charset, char delimiter) {
        EncodedImage img = new EncodedImage(image);
        img.setEncodingCharset(charset);
        img.setMetaDataDelimiter(delimiter);
        return img.bytes();
    }
}
