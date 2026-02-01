package com.wemade.domain;

public class Analysis {
    private final String id;
    private final long createdAt;
    private final AnalysisStatistics statistics = new AnalysisStatistics();
    private final ParseReport parseReport;

    private long totalRequests;
    private long success2xx;
    private long redirect3xx;
    private long clientError4xx;
    private long serverError5xx;

    public Analysis(String id, long createdAt, int sampleLimit) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id must not be blank");
        if (createdAt <= 0) throw new IllegalArgumentException("createdAt must be epoch millis");
        this.id = id;
        this.createdAt = createdAt;
        this.parseReport = new ParseReport(sampleLimit);
    }

    public String getId() { return id; }
    public long getCreatedAt() { return createdAt; }

    public AnalysisStatistics getStatistics() { return statistics; }
    public ParseReport getParseReport() { return parseReport; }

    public long getTotalRequests() { return totalRequests; }
    public long getSuccess2xx() { return success2xx; }
    public long getRedirect3xx() { return redirect3xx; }
    public long getClientError4xx() { return clientError4xx; }
    public long getServerError5xx() { return serverError5xx; }
}
