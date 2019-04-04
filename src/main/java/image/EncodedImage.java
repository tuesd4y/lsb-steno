package image;

import one.util.streamex.IntStreamEx;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    @SuppressWarnings("unused")
    private static Integer counting(Boolean ignored) {
        return c++ / 8;
    }

    private static byte booleansToByteFunction(List<Boolean> values) {
        if (values.size() != 8) return 0;

        byte b = 0b0000_0000;
        if (values.get(0)) {
            b |= 0b1;
        }
        for (int i = 1; i < values.size(); i++) {
            Boolean value = values.get(i);
            b <<= 1;
            if (value) {
                b |= 0b1;
            }
        }

        // TODO: this is exactly mirrored, wtf..
        return b;
    }

    public int get(int i) {
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

    public IntStreamEx stream() {
        return IntStreamEx.range(0, this.area - 1).map(this::get);
    }

    public IntStreamEx colorValues() {
        return stream().flatMap(pixel -> IntStream.of(Pixel.getRed(pixel), Pixel.getGreen(pixel), Pixel.getBlue(pixel)));
    }

    public Stream<Boolean> bitValues() {
        return colorValues().mapToObj(i -> i % 2 == 0);
    }

    public byte[] bytes() {
        c = 0;
        final Collection<List<Boolean>> bitLists = colorValues().mapToObj(color -> (color & 0b1) == 0b1)
                .groupingBy(EncodedImage::counting)
                .values();
        final byte[] bytes = new byte[bitLists.size()];
        final Iterator<List<Boolean>> iterator = bitLists.iterator();
        for (int i = 0; i < bitLists.size(); i++) {
            bytes[i] = booleansToByteFunction(iterator.next());
        }
        return bytes;
    }
}