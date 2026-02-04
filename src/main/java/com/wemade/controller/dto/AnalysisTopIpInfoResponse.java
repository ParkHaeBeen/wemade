package com.wemade.controller.dto;

public record AnalysisTopIpInfoResponse(
        String key,
        long count,
        String country,
        String region,
        String city,
        String asn,
        String asDomain
) {
}
