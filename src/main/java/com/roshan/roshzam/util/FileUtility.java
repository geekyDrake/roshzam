package com.roshan.roshzam.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUtility {
    private static final Logger logger = LoggerFactory.getLogger(FileUtility.class);
    public static File convertMultipartFileToFile(final MultipartFile multipartFile, final String filename) throws IOException {
        File tempFile = File.createTempFile("upload_", "_" + filename);
        multipartFile.transferTo(tempFile);

        // cleans up on JVM shutdown - backup in case the program exits earlier than expected
        tempFile.deleteOnExit();
        return tempFile;
    }

    public static File copyToTempFile(final File incomingFile, final String safeName) throws IOException {
        // Copy to system temp folder with new name
        Path targetPath = Paths.get(System.getProperty("java.io.tmpdir"), safeName);
        Files.copy(incomingFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        File tempFile = targetPath.toFile();

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
