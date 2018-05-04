package com.ziroom.bsrd.techplatform.exception;

/**
 * 图片存储平台交互中发生的异常
 */
@SuppressWarnings("serial")
public class StorageException extends RuntimeException {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
