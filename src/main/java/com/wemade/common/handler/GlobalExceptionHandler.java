package com.wemade.common.handler;

import com.wemade.common.exception.CustomException;
import com.wemade.common.exception.ErrorCode;
import com.wemade.common.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(NOT_FOUND)
                .body(new ErrorResponse(errorCode.name(), e.getMessage()));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustom(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(BAD_REQUEST)
                .body(new ErrorResponse(errorCode.name(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception e) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("INTERNAL_SERVER_ERROR", "unexpected server error"));
    }
}
