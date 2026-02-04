package com.wemade.common.exception;

public class RestClientException extends CustomException{
    public RestClientException(ErrorCode errorCode) {super(errorCode);}
    public RestClientException(ErrorCode errorCode, String message) {super(errorCode, message);}
    public RestClientException(ErrorCode errorCode, String message, Throwable cause) {super(errorCode, message, cause);}
}
