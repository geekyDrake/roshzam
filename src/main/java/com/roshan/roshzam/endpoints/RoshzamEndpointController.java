package com.roshan.roshzam.endpoints;

import com.roshan.roshzam.services.FingerPrintingService;
import com.roshan.roshzam.services.JpaDatabaseService;
import com.roshan.roshzam.util.FileUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RestController
public class RoshzamEndpointController {

    private final FingerPrintingService fingerPrintingService;
    private final JpaDatabaseService jpaDatabaseService;

    public RoshzamEndpointController(
            final FingerPrintingService fingerPrintingService,
            final JpaDatabaseService jpaDatabaseService
            ) {
        this.fingerPrintingService = fingerPrintingService;
        this.jpaDatabaseService = jpaDatabaseService;
    }

    private final static Logger logger = LoggerFactory.getLogger(RoshzamEndpointController.class);

    @PostMapping("/roshzam/upload-song")
    public CompletionStage<HttpStatus> uploadSongWithMetadata(@RequestParam("file") MultipartFile file) {
        try {
            // Remove spaces for File location
            String originalFilename = file.getOriginalFilename();
            String sanitisedFilename = originalFilename.replaceAll("\\s+", "_");

            // Convert to File
            File convertedFile = FileUtility.convertMultipartFileToFile(file, sanitisedFilename);

            return fingerPrintingService.storeAudioFingerPrint(convertedFile, originalFilename);

        } catch (IOException e) {
            logger.error(String.format("Song upload failed. Name: %s, content type: %s. Message: %s",
                    file.getOriginalFilename(), file.getContentType(), e.getMessage()));
            return CompletableFuture.completedStage(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/roshzam/test-db")
    public String testAudioHashDB(@RequestParam(value = "useNewRepo", defaultValue = "true") boolean useNewRepo) {
        return useNewRepo ? jpaDatabaseService.testGetAudioHashes().toString()
                : jpaDatabaseService.testGetAudioHashesFromOldDb().toString();
    }
}
