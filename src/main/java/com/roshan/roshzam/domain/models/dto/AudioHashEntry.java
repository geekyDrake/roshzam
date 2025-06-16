package com.roshan.roshzam.domain.models.dto;

import jakarta.persistence.*;
import lombok.Setter;

@Setter
@Entity
public class AudioHashEntry {
    @Id
    @GeneratedValue
    private Long id;

    private Long timestamp;
    private String filename;

    protected AudioHashEntry(){}

    public AudioHashEntry(final Long timestamp, final String filename){
        this.timestamp = timestamp;
        this.filename = filename;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hash_id")
    private AudioHash audioHash;

    @Override
    public String toString() {
        return String.format(
                "AudioHashEntry[timeStamp=%d, fileName=%s]",
                this.timestamp, this.filename
        );
    }
}
