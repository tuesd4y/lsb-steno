package me.tuesd4y.lsb.compression;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SteganoTestApp {

	private final static String fs = System.getProperty("file.separator"); // "/" or "\" depending on system
	private final static Path pathIn = Paths.get( "in");
	private final static Path pathOut = Paths.get("out");

	public static void main(String[] args) {

		String fileInName;

		// TestFiles anpassen
//		fileInName = pathIn + fs + "testFileIn.txt";
//		fileInName = pathIn + fs + "d3dcompiler_46.dll";
//		fileInName = pathIn + fs + "2.jpg"; // no me.tuesd4y.lsb.compression.compression possible
		// fileInName = pathIn + fs + "AAAABCABCABCD.txt";
		fileInName = pathIn + fs + "test.zip";
		
		FileHandler fh = new FileHandler();
		
		// read file to byte[]
		File fileIn = new File(fileInName);
		byte[] data = fh.fileToByteArray(fileIn);

		// encode LZW
		LZWEncoder en = new LZWEncoder(data);
		System.out.println("before encoding: " + en.getData().length);
		en.encode();
		System.out.println("after encoding: " + en.getData().length + " (ratio: " + en.getCompRatio() + ")");
		data = en.getData();

		// decode LZW
		LZWDecoder de = new LZWDecoder(data);

		de.decode();
		System.out.println("after decoding: " + de.getData().length);
		data = de.getData();

		// write byte[] to file
		fh.byteArrayToFile(data, pathOut);
	}

}
