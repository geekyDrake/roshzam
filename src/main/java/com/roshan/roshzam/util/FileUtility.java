package com.roshan.roshzam.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtility {
    private static final Logger logger = LoggerFactory.getLogger(FileUtility.class);
    public static File convertMultipartFileToFile(MultipartFile multipartFile, String filename) throws IOException {
        File tempFile = File.createTempFile("upload_", "_" + filename);
        multipartFile.transferTo(tempFile);

        // cleans up on JVM shutdown - backup in case the program exits earlier than expected
        tempFile.deleteOnExit();
        return tempFile;
    }

    public static void deleteTempFile(final String pathToFile){
        try{
            Path path = Paths.get(pathToFile);
            Files.deleteIfExists(path);
        } catch(Exception e) {
            logger.error(String.format("Failed to delete file at %s", pathToFile));
        }

    }
}
