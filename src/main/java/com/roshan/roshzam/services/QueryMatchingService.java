package com.roshan.roshzam.services;

import com.roshan.roshzam.domain.querys.SongCount;
import io.honerlaw.audio.fingerprint.hash.peak.HashedPeak;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class QueryMatchingService {

    @Autowired
    FingerPrintingService fingerPrintingService;
    @Autowired
    JpaDatabaseService jpaDatabaseService;

    public String matchSnippetToSongNaive(final File audioSnippet) {
        List<SongCount> result = getHashMatchesCountOrdered(audioSnippet);
        return result.isEmpty() ? "" : result.get(0).getName();
    }

    public String matchSnippetToSongHistogram(final File audioSnippet) {
        List<SongCount> result = getHashMatchesCountOrdered(audioSnippet);

        // Choose between results within 20% of top song
        long topValue = result.getFirst().getCount();
        result = result.stream()
                .filter(song -> song.getCount() > (topValue * 0.8))
                .toList();
        System.out.println("Output from top 20% count");
        result.forEach(s ->
                System.out.println(s.getName() + " " + s.getCount()));
        // If no other song has matches within 20% of top result, return top result
        if (result.size() == 1) {
            return result.getFirst().getName();
        }

        // Get timestamps for each file
        List<FileTimestamps> timestamps = result.stream()
                .map(song -> new FileTimestamps(song.getName(), jpaDatabaseService.queryDbForTimestamps(song.getName())))
                .toList();

        // Return the file with the least variance
        return timestamps.stream()
                .map(fileTimestamps -> {
                    DescriptiveStatistics stats = new DescriptiveStatistics();
                    fileTimestamps.timestamps().forEach(stats::addValue);
                    return new FileStatistics(fileTimestamps.filename(), stats);
                })
                .sorted((a, b) -> Double.compare(b.statistics().getVariance(), a.statistics().getVariance()))
                .toList()
                .stream().peek(d -> System.out.println("Output from timestamp variance: " + d.filename() + " " + d.statistics().getVariance())).toList()
                .getFirst()
                .filename();
    }

    private List<SongCount> getHashMatchesCountOrdered(final File audioSnippet) {
        /*
        - get hashes
        - search DB for matches
        - For each filename entry, count number of matches
        - Return list ordered by highest matches first
         */
        List<String> hashesToMatch = getHashesToMatch(audioSnippet);
        List<SongCount> result = jpaDatabaseService.queryDbForMatches(hashesToMatch);
        System.out.println("Output from DB directly");
        result.forEach(s ->
                System.out.println(s.getName() + " " + s.getCount()));
        // Return list ordered by highest match count - could do this in the SQL query but this makes double sure
        result.sort((a, b) -> {
                    var c = a.getCount() - b.getCount();
                    return c == 0 ? 0 : (c < 1 ? 1 : -1);
                }
        );
        System.out.println("Output from sorted DB");
        result.forEach(s ->
                System.out.println(s.getName() + " " + s.getCount()));
        return result;
    }

    private List<String> getHashesToMatch(File audioSnippet) {
        return Stream.of(fingerPrintingService.getFingerPrint(audioSnippet))
                .map(HashedPeak::getHashAsHex)
                .toList();
    }
}

record FileTimestamps(String filename, List<Long> timestamps) {
}

record FileStatistics(String filename, DescriptiveStatistics statistics) {
}