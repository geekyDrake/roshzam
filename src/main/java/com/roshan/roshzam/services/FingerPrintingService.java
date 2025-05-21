package com.roshan.roshzam.services;

import com.roshan.roshzam.domain.models.AudioHashEntry;
import com.roshan.roshzam.util.FileUtility;
import io.honerlaw.audio.fingerprint.AudioFile;
import io.honerlaw.audio.fingerprint.hash.peak.HashedPeak;
import jakarta.persistence.Convert;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Service
public class FingerPrintingService {

    private JpaDatabaseService dbService;

    public FingerPrintingService(JpaDatabaseService dbService){
        this.dbService = dbService;
    }

    public CompletionStage<HttpStatus> storeAudioFingerPrint(final File incomingFile, final String filename) {
        return CompletableFuture.completedStage(incomingFile)
                .thenApply(file -> {
                    try {
                        return getFingerPrint(file);
                    } catch (Exception e) {
                        // Convert to checked exception
                        throw new RuntimeException(e);
                    }
                })
                .thenApply(hashedPeaks -> prepareTableEntries(hashedPeaks, filename))
                .thenApply( entries -> {
                    dbService.addAudioHashEntries(entries);
                    return HttpStatus.OK;
                })
                .exceptionally(e -> {
                    throw new RuntimeException(e);
                });
    }

    private HashedPeak[] getFingerPrint(final File incomingFile) {
        try {
            // Create fingerprint
            final AudioFile audioFile = new AudioFile(incomingFile);
            final HashedPeak[] hashes = audioFile.getFingerPrint().getHashes();

            // Clean up temp files produced by library
            FileUtility.deleteTempFile(audioFile.getWAVFilePath());
            FileUtility.deleteTempFile(incomingFile.getAbsolutePath());

            return hashes;
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }

    private List<AudioHashEntry> prepareTableEntries(final HashedPeak[] rawHashes, final String filename) {
        return Arrays.stream(rawHashes).parallel()
                .map(hashedPeak ->
                        new AudioHashEntry(
                                hashedPeak.getHashAsHex(),
                                hashedPeak.getPeakOne().getTime(),
                                filename)
                ).toList();
    }

}
