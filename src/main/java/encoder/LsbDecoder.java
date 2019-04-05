package encoder;


import image.EncodedImage;

import java.awt.image.BufferedImage;

public class LsbDecoder implements Decoder {

    @Override
    public byte[] decode(BufferedImage image) {
        EncodedImage img = new EncodedImage(image);
//        final byte[] bytes = new byte[(img.area * 3) / 8];

//        img.pixels().boxed()
//        for (int i = 0; i < img.area; i++) {
//            int pixel = img.get(i);
//            int r = Pixel.getRed(pixel);
//            int g = Pixel.getGreen(pixel);
//            int b = Pixel.getBlue(pixel);
//
//            ByteHelper.setBitAtIndex(bytes, i, r % 2 == 0);
//            ByteHelper.setBitAtIndex(bytes, i+1, g % 2 == 0);
//            ByteHelper.setBitAtIndex(bytes, i+2, b % 2 == 0);
//        }
//
//        return bytes;

        return img.bytes2();
    }


}
