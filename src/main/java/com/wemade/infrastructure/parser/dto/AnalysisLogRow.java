package com.wemade.infrastructure.parser.dto;

public record AnalysisLogRow(
        String timeGeneratedUtc,
        String clientIp,
        String httpMethod,
        String requestUri,
        String userAgent,
        int httpStatus,
        String httpVersion,
        long receivedBytes,
        long sentBytes,
        long clientResponseTime,
        String sslProtocol,
        String originalRequestUriWithArgs
) {
}
