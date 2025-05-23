package com.roshan.roshzam.domain.models.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class TestRecord {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String testString;

    protected TestRecord() {}

    public TestRecord(String testString) {
        this.testString = testString;
    }

    @Override
    public String toString() {
        return String.format(
                "TestRecord[id=%d, testString=%s]",
                id, testString
        );
    }
}
