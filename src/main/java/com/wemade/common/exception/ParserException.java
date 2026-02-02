package com.wemade.common.exception;

public class ParserException extends CustomException {
    public ParserException(ErrorCode errorCode) { super(errorCode); }
    public ParserException(ErrorCode errorCode, String message) { super(errorCode, message); }
    public ParserException(ErrorCode errorCode, String message, Throwable cause) { super(errorCode, message, cause); }
}
