package com.cheng.application.techplatform.exception;

/**
 * FTP exception
 */
@SuppressWarnings("serial")
public class FtpException extends RuntimeException {

    public FtpException(String message) {
        super(message);
    }

    public FtpException(String message, Throwable cause) {
        super(message, cause);
    }
}
