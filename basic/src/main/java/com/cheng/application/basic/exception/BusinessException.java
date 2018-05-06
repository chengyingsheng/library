package com.cheng.application.basic.exception;

import com.cheng.application.basic.constant.ErrorCode;
import com.google.common.base.MoreObjects;

/**
 * 服务异常
 * provider统一使用此异常，通过不同的code和message区分不同的错误
 * code和message @see ErrorCode
 *
 * @Author zhoutao
 * @Date 2016/8/24
 */
public class BusinessException extends RuntimeException {
    private String code;

    public BusinessException() {
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.code = errorCode.getCode();
    }

    /**
     * 追加格式化的消息%s
     * ErrorCode PARAM_REQUIRED = new ErrorCode("40001", "必填参数【%s】为空");
     *
     * @param errorCode
     * @param appendMessage
     */
    public BusinessException(ErrorCode errorCode, String appendMessage) {
        super(String.format(errorCode.getMessage(), appendMessage == null ? "" : appendMessage));
        this.code = errorCode.getCode();
    }

    public BusinessException(String errorCode, String appendMessage) {
        super(appendMessage);
        this.code = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String appendMessage, Throwable cause) {
        super(String.format(errorCode.getMessage(), appendMessage == null ? "" : appendMessage), cause);
        this.code = errorCode.getCode();
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("code", code)
                .add("message", super.getMessage())
                .toString();
    }
}
