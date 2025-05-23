package com.roshan.roshzam.services;

import com.roshan.roshzam.clients.AudioHashRepository;
import com.roshan.roshzam.clients.OldAudioHashRepository;
import com.roshan.roshzam.clients.TestRepository;
import com.roshan.roshzam.domain.models.HashDataPointHolder;
import com.roshan.roshzam.domain.models.dto.AudioHash;
import com.roshan.roshzam.domain.models.dto.AudioHashEntry;
import com.roshan.roshzam.domain.models.dto.OldAudioHashRecord;
import com.roshan.roshzam.domain.models.dto.TestRecord;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

// Responsibility: Save and retrieve entries from DBs
@Service
public class JpaDatabaseService {

    private final AudioHashRepository audioHashRepository;
    private final TestRepository testRepository;
    private final OldAudioHashRepository oldAudioHashRepository;

    // Use JPA with the H2 in-memory database. See pom.xml
    public JpaDatabaseService(
            AudioHashRepository audioHashRepository,
            TestRepository testRepository,
            OldAudioHashRepository oldAudioHashRepository
    ) {
        this.audioHashRepository = audioHashRepository;
        this.testRepository = testRepository;
        this.oldAudioHashRepository = oldAudioHashRepository;
    }

    @Transactional
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

    // Flawed because multiple entries with the same hash cannot be stored
    @Deprecated
    public Void addAudioHashEntriesToOldDB(List<HashDataPointHolder> holderList) {
        var entries = holderList.stream()
                .map( entry -> new OldAudioHashRecord(entry.hexHash(), entry.timestamp(), entry.filename())).toList();
        oldAudioHashRepository.saveAll(entries);
        return null;
    }

    // ============== TEST METHODS ===================

    public List<AudioHash> testGetAudioHashes() {
        final int startingEntry = 50;
        var entries = audioHashRepository.findAll().stream().toList();
        return entries.subList(Math.min(startingEntry, entries.size()), Math.min(startingEntry + 30, entries.size()));
    }

    public List<OldAudioHashRecord> testGetAudioHashesFromOldDb() {
        var entries = StreamSupport.stream(oldAudioHashRepository.findAll().spliterator(), false).toList();
        return entries.subList(0, Math.min(50, entries.size()));
    }

    public String addTestRecord(final String request) {
        testRepository.save(new TestRecord(request));
        return String.format("Record[%s] added to database", request);
    }

    public String getAllTestRecords() {
        final StringBuilder sb = new StringBuilder("Here is a list of all records in test DB: \n");
        testRepository.findAll().forEach(record -> {
            var s = record.toString() + "\n";
            sb.append(s);
        });
        return sb.toString();
    }
}
