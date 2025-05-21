package com.roshan.roshzam.services;

import com.roshan.roshzam.clients.AudioHashRepository;
import com.roshan.roshzam.clients.TestRepository;
import com.roshan.roshzam.domain.models.AudioHashEntry;
import com.roshan.roshzam.domain.models.TestRecord;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class JpaDatabaseService {
    private final TestRepository testRepository;
    private final AudioHashRepository repository;

    // Use JPA with the H2 in-memory database. See pom.xml
    public JpaDatabaseService(
            TestRepository testRepository,
            AudioHashRepository repository
    ) {
        this.testRepository = testRepository;
        this.repository = repository;
    }

    public CompletionStage<Void> addAudioHashEntries(List<AudioHashEntry> entries) {
        return CompletableFuture.runAsync(() -> repository.saveAll(entries));
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

    public List<AudioHashEntry> testGetAudioHashes() {
        var entries = StreamSupport.stream(repository.findAll().spliterator(), false).toList();
        return entries.subList(0, Math.min(50, entries.size()));
    }
}
