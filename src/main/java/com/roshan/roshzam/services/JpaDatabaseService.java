package com.roshan.roshzam.services;

import com.roshan.roshzam.clients.AudioHashRepository;
import com.roshan.roshzam.domain.models.HashDataPointHolder;
import com.roshan.roshzam.domain.models.dto.AudioHash;
import com.roshan.roshzam.domain.models.dto.AudioHashEntry;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

// Responsibility: Save and retrieve entries from DBs
@Service
public class JpaDatabaseService {

    private final AudioHashRepository audioHashRepository;

    // Use JPA with the H2 in-memory database. See pom.xml
    public JpaDatabaseService(
            AudioHashRepository audioHashRepository
    ) {
        this.audioHashRepository = audioHashRepository;
    }

//    @Transactional
    public void saveHashEntriesToDb(List<HashDataPointHolder> entries) {
        entries
            .forEach(entry -> {
                AudioHash hash = audioHashRepository.findById(entry.hexHash()).orElseGet(() ->
                        new AudioHash(entry.hexHash())
                );
                // Creates connection between hash and hashEntry
                hash.addEntry(createAudioHashEntry(entry));

                /*
                Cascade saves all entries (even in the AudioHashEntryRepository)
                without having to explicitly write it
                 */
                audioHashRepository.save(hash);
            });
        System.out.printf("Entries added to DB: %d", entries.size());
    }

    private AudioHashEntry createAudioHashEntry(final HashDataPointHolder input) {
        return new AudioHashEntry(input.timestamp(), input.filename());
    }

    // ============== TEST METHODS ===================

    public List<AudioHash> testGetAudioHashes() {
        final int startingEntry = 50;
        var entries = audioHashRepository.findAll().stream().toList();
        return entries.subList(Math.min(startingEntry, entries.size()), Math.min(startingEntry + 30, entries.size()));
    }
}
