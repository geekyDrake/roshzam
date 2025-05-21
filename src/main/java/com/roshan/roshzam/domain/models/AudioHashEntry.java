package com.roshan.roshzam.domain.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class AudioHashEntry {
    @Id
    private String hexHash;
    private int timeStamp;
    private String fileName;

    protected AudioHashEntry() {}

    public AudioHashEntry(String hexHash, int peakOneTime, String fileName){
        this.hexHash = hexHash;
        this.timeStamp = peakOneTime;
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return String.format(
                "AudioHashEntry[hexHash=%s, timeStamp=%d, fileName=%s]",
                hexHash, timeStamp, fileName
        );
    }
}
