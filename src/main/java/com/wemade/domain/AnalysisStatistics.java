package com.wemade.domain;

import java.util.HashMap;
import java.util.Map;

public class AnalysisStatistics {
    private long success2xx;
    private long redirect3xx;
    private long clientError4xx;
    private long serverError5xx;
    private final Map<String, Long> ip = new HashMap<>();
    private final Map<String, Long> path = new HashMap<>();
    private final Map<Integer, Long> status = new HashMap<>();
}
