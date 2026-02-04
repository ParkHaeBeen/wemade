package com.wemade.common.exception;

public enum ErrorCode {

    FILE_REQUIRED("file is required"),
    FILE_SIZE_EXCEEDED("file too large (max 50MB)"),

    PARSE_LINE_EXCEEDED("too many lines (max 200k lines)"),
    PARSE_FAILED("failed to parse line"),
    PARSE_INVALID_FORMAT("invalid format"),

    NOT_FOUND("not found");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
