package me.tuesd4y.lsb.cli.newer;

import me.tuesd4y.lsb.compression.LZWEncoder;
import me.tuesd4y.lsb.encoder.LsbEncoder;
import me.tuesd4y.lsb.encoder.MessageTooLongException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class LsbEncoderCli {
    public static void main(String[] args) throws IOException {
        final Scanner in = new Scanner(System.in);
        final PrintStream out = System.out;

        out.println("Which image do you want to read?");
        String input;
        while ((input = in.nextLine()) == null || input.isEmpty() || Files.notExists(Paths.get(input))) {
            out.println("Can't find file, please try again");
        }

        final File imageFile = Paths.get(input).toFile();
        final BufferedImage imageToEncode = ImageIO.read(imageFile);
        final LsbEncoder lsbEncoder = new LsbEncoder();
        byte[] dataToEncode;

        out.println("Do you want to encode a file into the image? [y/anything else]");
        input = in.nextLine();
        if ("y".equals(input)) {
            // should encode a file
            out.println("Which file would you like to encode?");
            while ((input = in.nextLine()) == null || input.isEmpty() || Files.notExists(Paths.get(input))) {
                out.println("Can't find file, please try again");
            }
            final File dataFile = Paths.get(input).toFile();
            out.println("Will now encode " + dataFile.getPath());

            dataToEncode = LZWEncoder.encodeFile(dataFile);
        } else {
            out.println("Ok, so text it is! Which text would you like to encode into the image?");
            final String textToEncode = in.nextLine();
            dataToEncode = textToEncode.getBytes(StandardCharsets.UTF_8);
        }

        try {
            final BufferedImage newImage = lsbEncoder.encode(dataToEncode, imageToEncode);
            final File newFile = imageFile.getParentFile().toPath().resolve("encoded_" + imageFile.getName()).toFile();
            if (!newFile.exists())
                newFile.createNewFile();

            ImageIO.write(newImage, "png", newFile);
            out.println("wrote newer image to " + newFile);
        } catch (MessageTooLongException e) {
            e.printStackTrace();
        }
    }
}