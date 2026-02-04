package com.wemade.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnalysisStatistics {
    private long totalRequests;
    private long success2xx;
    private long redirect3xx;
    private long clientError4xx;
    private long serverError5xx;

    private final Map<String, Long> ipCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> pathCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> statusCounts = new ConcurrentHashMap<>();

    public void incrementIp(String value) {
        if (value == null || value.isBlank()) return;
        ipCounts.merge(value.trim(), 1L, Long::sum);
    }

    public void incrementPath(String value) {
        if (value == null || value.isBlank()) return;
        pathCounts.merge(value.trim(), 1L, Long::sum);
    }

    public void incrementStatus(String value) {
        if (value == null || value.isBlank()) return;
        statusCounts.merge(value, 1L, Long::sum);
    }

    public void incrementStatusGroup(String statusCode) {
        int group = Integer.parseInt(statusCode) / 100;
        if (group == 2) success2xx++;
        else if (group == 3) redirect3xx++;
        else if (group == 4) clientError4xx++;
        else if (group == 5) serverError5xx++;
    }

    public void setTotalRequests(long totalRequests) {
        this.totalRequests = totalRequests;
    }

    public long getTotalRequests() { return totalRequests; }

    public long getSuccess2xx() { return success2xx; }
    public long getRedirect3xx() { return redirect3xx; }
    public long getClientError4xx() { return clientError4xx; }
    public long getServerError5xx() { return serverError5xx; }

    public Map<String, Long> getIpCounts() { return ipCounts; }
    public Map<String, Long> getPathCounts() { return pathCounts; }
    public Map<String, Long> getStatusCounts() { return statusCounts; }
}