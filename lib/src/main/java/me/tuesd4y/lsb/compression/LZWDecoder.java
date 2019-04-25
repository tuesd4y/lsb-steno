package me.tuesd4y.lsb.compression;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class LZWDecoder {
	
	private byte[] data;
	private Map<Integer, String> decodeTable;
	private final String EMPTY = "";

	LZWDecoder(byte[] data) {
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}
	
	private void createDecodeTable() {
		decodeTable = new TreeMap<>();
		for (int i = 0; i < 256; i++) {
			decodeTable.put(i, String.format("%02x", i));
		}
	}
	
	public void decode() {
		if (data[0] == 0) { // if there was no me.tuesd4y.lsb.compression.compression
			data = Arrays.copyOfRange(data, 1, data.length);
		} else {
			createDecodeTable();
			decodeComp();
		}

	}
	
	public void decodeComp() {

		BitInputStream in = null;
		ByteArrayOutputStream out = null;

		try {

			in = new BitInputStream(data);
			out = new ByteArrayOutputStream();

			String prefix = EMPTY, muster;

			int nBits = in.readNBits(8); // read bits per chunk from encoded file

			int k = decodeTable.size();

			int i = in.readNBits(nBits); // first chunk
			out.write(Integer.valueOf(decodeTable.get(i), 16) - 128);
			prefix = decodeTable.get(i);
			while ((i = in.readNBits(nBits)) > -1) { // while (!EOF) read LZW-code

				if (decodeTable.containsKey(i)) {
					muster = decodeTable.get(i);
					for (int j = 0; j < muster.length(); j = j + 2) { // write muster
						out.write(Integer.valueOf(muster.substring(j, j + 2), 16) - 128);
					}
					decodeTable.put(k++, prefix + muster.substring(0, 2));
					prefix = muster;

				} else {
					muster = prefix + prefix.substring(0, 2);
					for (int j = 0; j < muster.length(); j = j + 2) { // write muster
						out.write(Integer.valueOf(muster.substring(j, j + 2), 16) - 128);
					}
					decodeTable.put(k++, muster);
					prefix = muster;
				}

			}
			data = out.toByteArray();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
			} catch (Exception ignore) {
			}

		}
	}

	public static File decodeToFile(final byte[] data, Path outPath) throws IOException {
		final FileHandler fh = new FileHandler();
		final LZWDecoder de = new LZWDecoder(data);

		de.decode();
//		System.out.println("after decoding: " + de.getData().length);
		final byte[] result = de.getData();

		File f = fh.byteArrayToFile(result, outPath);
		return f;
	}
}
