package com.roshan.roshzam.clients;

import com.roshan.roshzam.domain.models.AudioHashEntry;
import org.springframework.data.repository.CrudRepository;

public interface AudioHashRepository extends CrudRepository<AudioHashEntry, String> {
    AudioHashEntry findByHexHash(String hexHash);
}
