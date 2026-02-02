package com.wemade.domain;

import java.util.UUID;

public class Analysis {
    private final String id;
    private final long createdAt;
    private final AnalysisStatistics statistics = new AnalysisStatistics();
    private final ParseReport parseReport = new ParseReport();

    private Analysis(String id, long createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public long getCreatedAt() { return createdAt; }

    public AnalysisStatistics getStatistics() { return statistics; }
    public ParseReport getParseReport() { return parseReport; }

    public static Analysis create() {
        return new Analysis(UUID.randomUUID().toString(), System.currentTimeMillis());
    }
}
