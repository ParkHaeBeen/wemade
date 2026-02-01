package com.wemade.domain;

import java.util.ArrayList;
import java.util.List;

public class ParseReport {
    private long totalCount = 0;
    private long errorCount = 0;
    private final int sampleLimit;
    private final List<String> samples = new ArrayList<>();

    public ParseReport(int sampleLimit) {
        this.sampleLimit = sampleLimit;
    }

    public long getTotalCount() { return totalCount; }
    public long getErrorCount() { return errorCount; }
    public int getSampleLimit() { return sampleLimit; }
}
