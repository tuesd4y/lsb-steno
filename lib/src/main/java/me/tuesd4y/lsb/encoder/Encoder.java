package me.tuesd4y.lsb.encoder;


import java.awt.image.BufferedImage;

public interface Encoder {
    BufferedImage encode(byte[] data, BufferedImage image) throws MessageTooLongException;

//    BufferedImage encode(String text, Charset charset, BufferedImage image) throws MessageTooLongException;
//
//    BufferedImage encode(byte[] data, Charset charset, BufferedImage image) throws MessageTooLongException;

    BufferedImage encode(String text, BufferedImage image) throws MessageTooLongException;
}
