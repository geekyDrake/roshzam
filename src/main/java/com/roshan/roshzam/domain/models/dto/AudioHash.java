package com.roshan.roshzam.domain.models.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class AudioHash {

    // Note that this is a hex string version of the hash (byte[])
    @Id
    private String hash;

    protected AudioHash() {}

    public AudioHash(final String hexHash){
        this.hash = hexHash;
    }

    @OneToMany(mappedBy= "audioHash", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AudioHashEntry> entries = new ArrayList<>();

    public void addEntry(AudioHashEntry entry) {
        entry.setAudioHash(this);
        entries.add(entry);
    }

    @Override
    public String toString() {
        return String.format(
                "AudioHashEntry[hash=%s, %s]",
                this.hash, this.entries.toString()
        );
    }
}
