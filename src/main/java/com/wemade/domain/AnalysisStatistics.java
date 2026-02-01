package com.wemade.domain;

import java.util.HashMap;
import java.util.Map;

public class AnalysisStatistics {
    private final Map<String, Long> ip = new HashMap<>();
    private final Map<String, Long> path = new HashMap<>();
    private final Map<Integer, Long> status = new HashMap<>();
}
