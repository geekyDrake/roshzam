package com.roshan.roshzam.services;

import com.roshan.roshzam.domain.models.HashDataPointHolder;
import com.roshan.roshzam.util.FileUtility;
import io.honerlaw.audio.fingerprint.AudioFile;
import io.honerlaw.audio.fingerprint.hash.peak.HashedPeak;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

// Responsibility: Extract fingerprint AND Store (should refactor the Store part out into some other service)
@Service
public class FingerPrintingService {

    private JpaDatabaseService dbService;

    public FingerPrintingService(JpaDatabaseService dbService) {
        this.dbService = dbService;
    }

    // Convenience method - default to new repo
    public CompletionStage<HttpStatus> storeAudioFingerPrint(final File incomingFile, final String filename) {
        return storeAudioFingerPrint(incomingFile, filename, true);
    }

    public CompletionStage<HttpStatus> storeAudioFingerPrint(final File incomingFile, final String filename, final boolean useNewRepository) {

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
                .thenApply(entries -> {
                    // Can't run operation async yet - transactional context issue flushes the DB before I can use it
                    // Future improvement is to get the async version running
                        if (useNewRepository) {
                            dbService.saveHashEntriesToDb(entries);
                        } else {
                            dbService.addAudioHashEntriesToOldDB(entries);
                        }
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

    private List<HashDataPointHolder> prepareTableEntries(final HashedPeak[] rawHashes, final String filename) {
        return Arrays.stream(rawHashes).parallel()
                .map(hashedPeak ->
                        new HashDataPointHolder(
                                hashedPeak.getHashAsHex(),
                                hashedPeak.getPeakOne().getTime(),
                                filename)
                ).toList();
    }
}
