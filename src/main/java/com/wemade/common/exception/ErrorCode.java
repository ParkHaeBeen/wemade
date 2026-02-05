package com.wemade.common.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ErrorCode {

    FILE_REQUIRED(BAD_REQUEST, "file is required"),
    FILE_SIZE_EXCEEDED(PAYLOAD_TOO_LARGE, "file too large (max 50MB)"),

    PARSE_LINE_EXCEEDED(PAYLOAD_TOO_LARGE, "too many lines (max 200k lines)"),
    PARSE_FAILED(UNPROCESSABLE_ENTITY, "failed to parse line"),
    PARSE_INVALID_FORMAT(BAD_REQUEST, "invalid format"),

    NOT_FOUND(HttpStatus.NOT_FOUND, "not found"),

    REST_CLIENT_ERROR(BAD_GATEWAY, "failed to connect to remote server");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}