package com.roshan.roshzam.clients;

import com.roshan.roshzam.domain.models.dto.AudioHash;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AudioHashRepository extends JpaRepository<AudioHash, String> {}
