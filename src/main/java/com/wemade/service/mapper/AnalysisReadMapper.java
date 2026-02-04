package com.wemade.service.mapper;

import com.wemade.controller.dto.AnalysisReadResponse;
import com.wemade.controller.dto.AnalysisTopItem;
import com.wemade.domain.Analysis;
import com.wemade.domain.AnalysisStatistics;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Component
public class AnalysisReadMapper {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    public AnalysisReadResponse mapper(Analysis analysis, int topN) {
        AnalysisStatistics statistics = analysis.getStatistics();
        long total = statistics.getTotalRequests();

        long success2xx = statistics.getSuccess2xx();
        long redirect3xx = statistics.getRedirect3xx();
        long error4xx = statistics.getClientError4xx();
        long error5xx = statistics.getServerError5xx();

        return new AnalysisReadResponse(
                analysis.getId(),
                toLocalDateTime(analysis.getCreatedAt()),
                total,
                ratio(success2xx, total),
                ratio(redirect3xx, total),
                ratio(error4xx, total),
                ratio(error5xx, total),
                topNStringKey(statistics.getPathCounts(), topN),
                topNIntKey(statistics.getStatusCounts(), topN),
                topNStringKey(statistics.getIpCounts(), topN)
        );
    }

    private LocalDateTime toLocalDateTime(long epochMillis) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(epochMillis),
                KST
        );
    }

    private double ratio(long value, long total) {
        if (total <= 0) return 0.0;
        return (value * 100.0) / total;
    }

    private List<AnalysisTopItem> topNStringKey(Map<String, Long> counts, int n) {
        return counts.entrySet().stream()
                .sorted((left, right) -> Long.compare(right.getValue(), left.getValue()))
                .limit(n)
                .map(entry -> new AnalysisTopItem(entry.getKey(), entry.getValue()))
                .toList();
    }

    private List<AnalysisTopItem> topNIntKey(Map<Integer, Long> counts, int n) {
        return counts.entrySet().stream()
                .sorted((left, right) -> Long.compare(right.getValue(), left.getValue()))
                .limit(n)
                .map(entry -> new AnalysisTopItem(String.valueOf(entry.getKey()), entry.getValue()))
                .toList();
    }
}