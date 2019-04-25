package me.tuesd4y.lsb.compression;

import java.io.*;

public class BitInputStream extends InputStream {
	// a class that reads from an InputStream bitwise
	
	private ByteArrayInputStream in;
	private int bitsLeft = 0;
	private int buffer = 0;
	private boolean eof = false;

	private static final int BYTE = 8;

	public BitInputStream(byte[] data) {
		in = new ByteArrayInputStream(data);
	}

	public int readNBits(final int n) throws IOException {
		// reads n bits from the stream
		
		int result = 0;
		for (int i = 1; i <= n; i++) {
			result += read() << (n - i);
		}

		if (eof) // only works for n >= BYTE
			return -1;

		return result;

	}

	@Override
	public int read() throws IOException {
		// reads the next bit
		if (bitsLeft == 0) {// if buffer empty
			getNextByte();
		}
		return (buffer >> (bitsLeft-- - 1)) & 1;
	}

	public void getNextByte() throws IOException {
		// reads the next byte to the buffer
		if (in.available() > 0) {
			byte[] b = new byte[1];
			in.read(b);
			buffer = b[0];
			bitsLeft = BYTE;
		} else {
			eof = true;
		}

	}
	
	@Override
	public void close() throws IOException {
		in.close();
	}
	
	@Override
	public int available() throws IOException {
		return in.available() * BYTE + bitsLeft;
	}

	@Override
	protected void finalize() throws IOException {
		in.close();
	}

}
