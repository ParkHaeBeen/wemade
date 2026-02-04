package com.wemade.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

public record AnalysisReadResponse(
        String analysisId,
        LocalDateTime createdDateTime,
        long totalRequests,
        double success2xxRatio,
        double redirect3xxRatio,
        double client4xxRatio,
        double server5xxRatio,
        List<AnalysisTopItem> topPaths,
        List<AnalysisTopItem> topStatusCodes,
        List<AnalysisTopItem> topIps
) {

}
