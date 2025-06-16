package com.roshan.roshzam.clients;

import com.roshan.roshzam.domain.models.dto.AudioHashEntry;
import com.roshan.roshzam.domain.querys.SongCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AudioHashEntryRepository extends JpaRepository<AudioHashEntry, Long> {

    @Query(
            "SELECT e.filename AS name, COUNT(e) AS count " +
            "FROM AudioHashEntry e " +
            "WHERE e.audioHash.hash IN :hashes " +
            "GROUP BY e.filename"
    )
    List<SongCount> countByFilenameForHashes(@Param("hashes") List<String> hashes);
}
