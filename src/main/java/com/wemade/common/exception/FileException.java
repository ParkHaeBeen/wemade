package com.wemade.common.exception;

public class FileException extends CustomException {
    public FileException(ErrorCode errorCode) { super(errorCode); }
    public FileException(ErrorCode errorCode, String message) { super(errorCode, message); }
    public FileException(ErrorCode errorCode, String message, Throwable cause) { super(errorCode, message, cause); }
}
