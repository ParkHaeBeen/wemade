package com.wemade.common.handler;

public record ErrorResponse(
        String code,
        String message
) {
}
