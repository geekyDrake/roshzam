package com.roshan.roshzam.endpoints;

import com.roshan.roshzam.services.FingerPrintingService;
import com.roshan.roshzam.services.JpaDatabaseService;
import com.roshan.roshzam.services.QueryMatchingService;
import com.roshan.roshzam.util.FileUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private FingerPrintingService fingerPrintingService;
    @Autowired
    private JpaDatabaseService jpaDatabaseService;
    @Autowired
    private QueryMatchingService queryMatchingService;


    private final static Logger logger = LoggerFactory.getLogger(RoshzamEndpointController.class);

    @PostMapping("/roshzam/upload-song")
    public CompletionStage<HttpStatus> uploadSongWithMetadata(@RequestParam("file") MultipartFile file) {
        try {
            // Remove spaces for File location
            String originalFilename = file.getOriginalFilename();
            String sanitisedFilename = originalFilename.replaceAll("\\s+", "_");

            System.out.printf("Request received for %s \n", originalFilename);
            // Convert to File
            File convertedFile = FileUtility.convertMultipartFileToFile(file, sanitisedFilename);

            return fingerPrintingService.storeAudioFingerPrint(convertedFile, originalFilename);

        } catch (IOException e) {
            logger.error(String.format("Song upload failed. Name: %s, content type: %s. Message: %s \n",
                    file.getOriginalFilename(), file.getContentType(), e.getMessage()));
            return CompletableFuture.completedStage(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/roshzam/query-song")
    public String querySong(@RequestParam("file") MultipartFile file) {
        System.out.printf("Request received for %s \n", file.getOriginalFilename());
        final File audioSnippet;
        try {
            audioSnippet = FileUtility.convertMultipartFileToFile(file, file.getOriginalFilename());

        } catch (IOException e) {
            logger.error(String.format("File format conversion failed for %s \n", file.getOriginalFilename()));
            return "Query matching failed";
        }
        return queryMatchingService.matchSnippetToSong(audioSnippet);
    }

    @GetMapping("/roshzam/test-db")
    public String testAudioHashDB() {
        System.out.println("DB test request received");
        return jpaDatabaseService.testGetAudioHashes().toString();
    }
}
