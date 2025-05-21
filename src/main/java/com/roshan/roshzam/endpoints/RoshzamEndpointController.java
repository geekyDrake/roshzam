package com.roshan.roshzam.endpoints;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class RoshzamEndpointController {

    @PostMapping("/roshzam/upload-song")
    public void uploadSongWithMetadata(@RequestParam("file") MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            long size = file.getSize();
            byte[] audioBytes = file.getBytes();


        } catch (IOException e) {
            // Replace with LOGGER
            throw new RuntimeException(e);
        }
    }
}
