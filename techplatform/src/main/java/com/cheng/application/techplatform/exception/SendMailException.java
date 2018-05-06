package com.cheng.application.techplatform.exception;

/**
 * 发送邮件异常
 */
@SuppressWarnings("serial")
public class SendMailException extends RuntimeException {

    public SendMailException(String message) {
        super(message);
    }

    public SendMailException(String message, Throwable cause) {
        super(message, cause);
    }
}
