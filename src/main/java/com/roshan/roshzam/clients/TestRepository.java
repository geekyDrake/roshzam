package com.roshan.roshzam.clients;

import com.roshan.roshzam.domain.models.TestRecord;
import org.springframework.data.repository.CrudRepository;

public interface TestRepository extends CrudRepository<TestRecord, Long> {
    TestRecord findById(long id);
}
