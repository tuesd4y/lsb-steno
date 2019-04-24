package me.tuesd4y.lsb.cli.newer;

import me.tuesd4y.lsb.compression.LZWDecoder;
import me.tuesd4y.lsb.encoder.LsbDecoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class LsbDecoderCli {
    public static void main(String[] args) throws IOException {

        final Scanner in = new Scanner(System.in);
        final PrintStream out = System.out;

        out.println("Which image do you want to decode?");
        String input;
        while ((input = in.nextLine()) == null || input.isEmpty() || Files.notExists(Paths.get(input)))
            out.println("Can't find file, please try again");

        final Path imagePath = Paths.get(input);
        final File imageFile = imagePath.toFile();
        final BufferedImage imageToDecode = ImageIO.read(imageFile);
        final LsbDecoder lsbDecoder = new LsbDecoder();

        final byte[] data = lsbDecoder.decode(imageToDecode);

        out.println("Does the image contain a file? [y/anything else]");
        input = in.nextLine();
        if ("y".equals(input)) {
            final File f = LZWDecoder.decodeToFile(data, imagePath.getParent());
            out.println("Decoded file to :" + f.getPath());
        } else {
            out.println("Ok, so text it is! The result as text is:");
            out.println(new String(data, StandardCharsets.UTF_8));
        }
    }
}