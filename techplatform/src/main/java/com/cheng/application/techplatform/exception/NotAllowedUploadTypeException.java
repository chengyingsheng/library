package com.cheng.application.techplatform.exception;

/**
 * 不允许上传的文件类型异常
 */
@SuppressWarnings("serial")
public class NotAllowedUploadTypeException extends Exception {

    public NotAllowedUploadTypeException(String message) {
        super(message);
    }

    public NotAllowedUploadTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
