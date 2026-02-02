package com.wemade.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParseReport {
    private long totalCount = 0;
    private long errorCount = 0;
    private final List<String> samples = new ArrayList<>();

    public void addLine() {
        totalCount++;
    }

    public void addError(String rawLine) {
        errorCount++;
        if (rawLine != null) {
            samples.add(rawLine);
        }
    }

    public long getTotalCount() {
        return totalCount;
    }

    public long getErrorCount() {
        return errorCount;
    }

    public List<String> getSamples() {
        return Collections.unmodifiableList(samples);
    }
}
