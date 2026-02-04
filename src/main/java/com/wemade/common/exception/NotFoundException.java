package com.wemade.common.exception;

public class NotFoundException extends CustomException{
    public NotFoundException(ErrorCode errorCode) { super(errorCode); }
    public NotFoundException(ErrorCode errorCode, String message) { super(errorCode, message); }
    public NotFoundException(ErrorCode errorCode, String message, Throwable cause) { super(errorCode, message, cause); }
}
