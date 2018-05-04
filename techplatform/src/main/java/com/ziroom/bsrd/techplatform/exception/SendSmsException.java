package com.ziroom.bsrd.techplatform.exception;

/**
 * 发送短信异常
 */
@SuppressWarnings("serial")
public class SendSmsException extends RuntimeException {

    public SendSmsException(String message) {
        super(message);
    }

    public SendSmsException(String message, Throwable cause) {
        super(message, cause);
    }
}
