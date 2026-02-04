package com.wemade.infrastructure.parser.dto;

public record ParsedLogLine(String ip, String path, String statusCode) {}

