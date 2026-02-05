package com.wemade.service.mapper;

import com.wemade.controller.dto.AnalysisReadResponse;
import com.wemade.controller.dto.AnalysisTopIpInfoResponse;
import com.wemade.controller.dto.AnalysisTopItem;
import com.wemade.domain.Analysis;
import com.wemade.domain.AnalysisStatistics;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class AnalysisReadMapper {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    public AnalysisReadResponse mapper(
            Analysis analysis,
            List<AnalysisTopItem> topPaths,
            List<AnalysisTopItem> topStatusCodes,
            List<AnalysisTopIpInfoResponse> topIpInfos
    ) {
        AnalysisStatistics statistics = analysis.getStatistics();
        long total = statistics.getTotalRequests();

        return new AnalysisReadResponse(
                analysis.getId(),
                toLocalDateTime(analysis.getCreatedAt()),
                total,
                ratio(statistics.getSuccess2xx(), total),
                ratio(statistics.getRedirect3xx(), total),
                ratio(statistics.getClientError4xx(), total),
                ratio(statistics.getServerError5xx(), total),
                topPaths,
                topStatusCodes,
                topIpInfos
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
}