package com.mycompany.myapp.utils;

import org.apache.commons.io.IOUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {
    private static final String PREFIX = "stream2file";
    private static final String SUFFIX = ".tmp";
    private static final String PATH = "src/main/webapp/assets/images/";

    public static File stream2file (InputStream in) throws IOException {
        final File tempFile = File.createTempFile(PREFIX, SUFFIX);
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return tempFile;
    }

    public static String saveImage(String fileName, String format, byte[] albumImageData) throws IOException {
        String fullPath = format != null ? PATH + fileName + "." + format : PATH + fileName + ".jpg";
        try (FileOutputStream fos = new FileOutputStream(fullPath)) {
            fos.write(albumImageData);

            return fullPath.replace("src/main/webapp/", "");
        }
    }
}
