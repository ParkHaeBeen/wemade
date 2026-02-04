package com.wemade.infrastructure.parser.dto;

public record AnalysisLogRow(
        String timeGeneratedUtc,
        String clientIp,
        String httpMethod,
        String requestUri,
        String userAgent,
        String httpStatus,
        String httpVersion,
        String receivedBytes,
        String sentBytes,
        String clientResponseTime,
        String sslProtocol,
        String originalRequestUriWithArgs
) {
}
