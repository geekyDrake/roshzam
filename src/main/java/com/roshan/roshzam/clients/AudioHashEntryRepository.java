package com.roshan.roshzam.clients;

import com.roshan.roshzam.domain.models.dto.AudioHashEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AudioHashEntryRepository extends JpaRepository<AudioHashEntry, Long> {}
