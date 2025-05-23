package com.roshan.roshzam.clients;

import com.roshan.roshzam.domain.models.dto.OldAudioHashRecord;
import org.springframework.data.repository.CrudRepository;

public interface OldAudioHashRepository extends CrudRepository<OldAudioHashRecord, String> {
    OldAudioHashRecord findByHexHash(String hexHash);
}
