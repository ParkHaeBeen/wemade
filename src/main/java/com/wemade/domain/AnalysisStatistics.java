package com.wemade.domain;

import java.util.HashMap;
import java.util.Map;

public class AnalysisStatistics {
    private long totalRequests;
    private long success2xx;
    private long redirect3xx;
    private long clientError4xx;
    private long serverError5xx;
    private final Map<String, Long> ip = new HashMap<>();
    private final Map<String, Long> path = new HashMap<>();
    private final Map<Integer, Long> status = new HashMap<>();

    public void incrementIp(String value) {
        if (value == null || value.isBlank()) return;
        ip.merge(value, 1L, Long::sum);
    }

    public void incrementPath(String value) {
        if (value == null || value.isBlank()) return;
        path.merge(value, 1L, Long::sum);
    }

    public void incrementStatus(int value) {
        status.merge(value, 1L, Long::sum);
    }

    public void incrementStatusGroup(int statusCode) {
        int group = statusCode / 100;
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

    public Map<String, Long> getIp() { return ip; }
    public Map<String, Long> getPath() { return path; }
    public Map<Integer, Long> getStatus() { return status; }
}
