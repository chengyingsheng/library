package com.ziroom.bsrd.http;


public class HttpResult {

    private boolean isSucc;
    private Object sourceStatusCode;
    private String msg;
    private String data;

    private HttpResult(boolean isSucc, String msg) {
        this.isSucc = isSucc;
        this.msg = msg;
    }

    private HttpResult(boolean isSucc, String msg, String data) {
        this.isSucc = isSucc;
        this.msg = msg;
        this.data = data;
    }

    public static HttpResult ofErr(String msg) {
        return new HttpResult(false, msg);
    }

    public static HttpResult ofFail(String msg) {
        return new HttpResult(false, msg);
    }

    public static HttpResult ofSucc(String msg, String data) {
        return new HttpResult(true, msg, data);
    }

    public static HttpResult of(Exception e) {
        return ofErr(e.getMessage());
    }

    public Object getSourceStatusCode() {
        return sourceStatusCode;
    }

    public void setSourceStatusCode(Object sourceStatusCode) {
        this.sourceStatusCode = sourceStatusCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isSucc() {
        return isSucc;
    }

    public void setIsSucc(boolean isSucc) {
        this.isSucc = isSucc;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
