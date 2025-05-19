package com.roshan.roshzam.services;

import com.roshan.roshzam.clients.TestRepository;
import com.roshan.roshzam.domain.models.TestRecord;
import org.springframework.stereotype.Service;

@Service
public class JpaDatabaseService {
    private final TestRepository repository;

    public JpaDatabaseService(TestRepository repository) {
        this.repository = repository;
    }

    public String addTestRecord(final String request) {
        repository.save(new TestRecord(request));
        return String.format("Record[%s] added to database", request);
    }

    public String getAllTestRecords() {
        final StringBuilder sb = new StringBuilder("Here is a list of all records in test DB: \n");
        repository.findAll().forEach(record -> {
            var s = record.toString() + "\n";
            sb.append(s);
        });
        return sb.toString();
    }
}
