package encoder;


import image.EncodedImage;

import java.awt.image.BufferedImage;

public class LsbDecoder implements Decoder {

    @Override
    public byte[] decode(BufferedImage image) {
        EncodedImage img = new EncodedImage(image);
        return img.bytes();
    }


}
