package me.tuesd4y.lsb.compression;

import java.io.*;

public class BitOutputStream extends OutputStream {
	// a class that writes to an OutputStream bitwise
	
	private ByteArrayOutputStream out;
	private int buffer;
	private int digits; // digits in buffer

	private static final int BYTE = 8;

	public BitOutputStream() {
		out = new ByteArrayOutputStream();
		buffer = 0;
		digits = 0;
	}

	public void write(int b, int nDigits) throws IOException {
		// write the integer b as a nDigits length binary
		int index = nDigits - 1;
		for (int i = 0; i < nDigits; i++) {
			write((b >> index--) & 1);
		}
	}	
	
	@Override
	public void write(int bit) throws IOException {
		// write a single bit to the buffer and the buffer to the stream
		if (bit < 0 || bit > 1)
			throw new IllegalArgumentException();
		buffer += bit << (7 - digits++);
		if (digits == BYTE)
			flush();

	}

	@Override
	public void flush() {
		// write the (full) buffer to the stream
		out.write(buffer);
		buffer = 0;
		digits = 0;
	}

	@Override
	public void close() throws IOException {
		if (digits > 0) {
			flush();
		}
		out.close();
	}

	@Override
	protected void finalize() throws IOException {
		close();
	}

	public byte[] toByteArray() {
		return out.toByteArray();
	}

}
