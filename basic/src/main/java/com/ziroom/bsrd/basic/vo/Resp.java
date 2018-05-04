package com.ziroom.bsrd.basic.vo;

import com.google.common.collect.Maps;
import com.ziroom.bsrd.basic.constant.ErrorCode;

import java.io.Serializable;
import java.util.Map;

/**
 * api的响应对象
 */
public class Resp<T> implements Serializable {

    private static final String CODE_SUCCESS = "20000";
    private static final String MESSAGE_SUCCESS = "success";

    /**
     * 状态码
     */
    private String code;
    /**
     * 状态信息
     */
    private String message;

    /**
     * 数据对象
     */
    private T data;

    private Resp(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return CODE_SUCCESS.equals(this.getCode());
    }

    public static Resp success(Object data) {
        return new Resp(CODE_SUCCESS, MESSAGE_SUCCESS, data);
    }

    public static Resp success(String key, Object value) {
        Map<String, Object> data = Maps.newHashMap();
        data.put(key, value);
        return new Resp(CODE_SUCCESS, MESSAGE_SUCCESS, data);
    }


    public static Resp successWithMsg(String msg) {
        return new Resp(CODE_SUCCESS, msg, "success");
    }

    public static Resp success() {
        return new Resp(CODE_SUCCESS, MESSAGE_SUCCESS, "success");
    }

    public static Resp error(ErrorCode errorCode, String... message) {
        return new Resp(errorCode.getCode(), String.format(errorCode.getMessage(), message), "error");
    }

    public static Resp error(String code, String message) {
        return new Resp(code, message, "error");
    }

    public static Resp error(ErrorCode errorCode, Map<String, Object> data) {
        return new Resp(errorCode.getCode(), errorCode.getMessage(), data);
    }

    public static Resp error(ErrorCode code) {
        return new Resp(code.getCode(), code.getMessage(), "error");
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Resp{");
        sb.append("code='").append(code).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
