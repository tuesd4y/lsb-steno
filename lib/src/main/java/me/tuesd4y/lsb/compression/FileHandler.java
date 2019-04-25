package me.tuesd4y.lsb.compression;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;

public class FileHandler {

    private final static String fs = System.getProperty("file.separator"); // "/" or "\" depending on system
    private final static byte DELIMITER = '='; // fullFileName + DELIMITER + fileData > byte[]

    public byte[] fileToByteArray(File file) {

        byte[] fileData = new byte[((int) file.length())];

        try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
            in.readFully(fileData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return concatenateByteArrays(file.getName().getBytes(), DELIMITER, fileData);

    }

    public File byteArrayToFile(byte[] array, Path pathOut) {

        FileOutputStream out = null;

        File outFile = null;
        try {

            // get file name and extension
            int i = 0;
            StringBuilder sb = new StringBuilder();
            while (array[i] != DELIMITER) {
                sb.append((char) array[i]);
                i++;
            }

            outFile = createFile(sb.toString(), pathOut);
            out = new FileOutputStream(outFile);
            out.write(Arrays.copyOfRange(array, i + 1, array.length));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (Exception ignore) {
            }
        }

        return outFile;
    }

    private File createFile(String fullFileName, Path pathOut) {
        // create newer File in pathOut
        File file = null;

        if (pathOut == null) {
            file = new File(fullFileName);
        } else {
            file = new File(pathOut + fs + fullFileName);
        }

        int i = 1;

        if (!file.exists()) {
            if (file.getParentFile() != null)
                file.getParentFile().mkdirs();
        }


        StringBuilder sb = new StringBuilder();
        int dot = fullFileName.lastIndexOf('.');
        if (pathOut != null) {
            sb.append(pathOut);
            sb.append(fs);
        }
        sb.append(fullFileName, 0, dot);
//        sb.append("(").append(i++).append(")");
        sb.append(fullFileName.substring(dot));
        file = new File(sb.toString());


        return file;
    }

    private byte[] concatenateByteArrays(byte[] arr1, byte delimitter, byte[] arr2) {
        // concatenate two or more byte arrays

        ByteArrayOutputStream concat = new ByteArrayOutputStream();
        for (byte b : arr1) {
            concat.write(b);
        }
        concat.write(delimitter);

        for (byte b : arr2) {
            concat.write(b);
        }
        return concat.toByteArray();
    }

    // not used
    public static ByteArrayInputStream arrayToByteArrayInputStream(byte[] array) {
        return new ByteArrayInputStream(array);
    }

    public static byte[] byteArrayOutputStreamToArray(ByteArrayOutputStream out) {
        return out.toByteArray();
    }

}
