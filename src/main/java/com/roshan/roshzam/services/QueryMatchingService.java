package com.roshan.roshzam.services;

import com.roshan.roshzam.domain.querys.SongCount;
import io.honerlaw.audio.fingerprint.hash.peak.HashedPeak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

@Service
public class QueryMatchingService {

    @Autowired
    FingerPrintingService fingerPrintingService;
    @Autowired
    JpaDatabaseService jpaDatabaseService;

    public String matchSnippetToSong(final File audioSnippet) {
        /*
        - get hashes
        - search DB for matches
        - For each filename entry, count number of matches
        - Return highest number of matches
         */
        List<String> hashesToMatch = Stream.of(fingerPrintingService.getFingerPrint(audioSnippet))
                .map(HashedPeak::getHashAsHex)
                .toList();
        List<SongCount> result = jpaDatabaseService.queryDbForMatches(hashesToMatch);
        result.sort((a,b) -> {
                    var c = a.getCount() - b.getCount();
                    return c == 0 ? 0 : (c < 1 ? -1 : 1);
                }
        );
        return result.isEmpty() ? "" : result.get(0).getName();
    }
}
