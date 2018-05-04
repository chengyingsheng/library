package com.ziroom.bsrd.techplatform.exception;

/**
 * 不允许上传的文件大小异常
 */
@SuppressWarnings("serial")
public class MaxUploadSizeLimitException extends Exception {

    public MaxUploadSizeLimitException(String message) {
        super(message);
    }

    public MaxUploadSizeLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}
