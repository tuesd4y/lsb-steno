package image;

import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class EncodedImage implements Iterable<Integer> {
    private final int width, height;
    private final BufferedImage image;
    public int area;

    private static int c = 0;


    public EncodedImage(BufferedImage image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.area = width * height;
    }

//    private static Integer counting(Boolean ignored) {
//        return c++ / 8;
//    }

    public static byte booleansToByte(List<Boolean> values) {
        byte b = 0b0000_0000;
        if (values.get(values.size() - 1)) {
            b |= 0b1;
        }
        for (int i = values.size() - 2; i >= 0; i--) {
            Boolean value = values.get(i);
            b <<= 1;
            if (value) {
                b |= 0b1;
            }
        }

        return b;
    }

    public int get(int i) {
//        System.out.printf("Currently getting %d,%d\n", i % width, i / width);
        return image.getRGB(i % width, i / width);
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < area;
            }

            @Override
            public Integer next() {
                int pixel = get(i);
                i++;
                return pixel;
            }
        };
    }

//    public IntStreamEx stream() {
//        return IntStreamEx.range(0, this.area - 1).map(this::get);
//    }

//    public IntStreamEx colorValues() {
//        return stream().flatMap(pixel -> IntStream.of(Pixel.getRed(pixel), Pixel.getGreen(pixel), Pixel.getBlue(pixel)));
//    }

//    public Stream<Boolean> bitValues() {
//        return colorValues().mapToObj(color -> (color & 0b1) == 0b1);
//    }

//    public byte[] bytes() {
//        c = 0;
//        final Collection<List<Boolean>> bitLists = colorValues().mapToObj(color -> (color & 0b1) == 0b1)
//                .groupingBy(EncodedImage::counting)
//                .values();
//        final byte[] bytes = new byte[bitLists.size()];
//        final Iterator<List<Boolean>> iterator = bitLists.iterator();
//        for (int i = 0; i < bitLists.size(); i++) {
//            bytes[i] = booleansToByte(iterator.next());
//        }
//        return bytes;
//    }

    public byte[] bytes() {
        final boolean[] bits = new boolean[area * 3];
        for (int i = 0; i < bits.length; i++) {
            int color = getColor(i);
            // check if last bit is a 1
            bits[i] = (color & 0b1) == 0b1;
        }

        byte[] encodedData = null;
        int index = 0;
        int dataEnd = bits.length / 8;

        StringBuilder info = new StringBuilder();

        boolean isInfo = true;

        // fixme: maybe we should handle the last case separately
        for (int i = 0; i < dataEnd; i++) {
            int x = i * 8;
            byte currentByte = booleansToByte(Arrays.asList(bits[x], bits[x + 1], bits[x + 2], bits[x + 3], bits[x + 4], bits[x + 5], bits[x + 6], bits[x + 7]));

            if (isInfo) {
                // todo: make this variable and not hardcoded...
                if (currentByte == StandardCharsets.UTF_8.encode(";").get()) {
                    isInfo = false;
                    final String infoString = info.toString();
                    final int dataLength = Integer.parseInt(infoString);
                    dataEnd = infoString.length() + 1 + dataLength;
                    encodedData = new byte[dataLength];
                } else {
                    info.appendCodePoint(currentByte);
                }
            } else {
                encodedData[index++] = currentByte;
            }
        }
        return encodedData;
    }

    int getColor(int i) {
        int c = get(i / 3);

        switch (i % 3) {
            case 0:
                return Pixel.getRed(c);
            case 1:
                return Pixel.getGreen(c);
            default:
                return Pixel.getBlue(c);
        }
    }
}