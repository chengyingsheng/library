package com.cheng.application.log;

import com.cheng.application.core.ApplicationEnvironment;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * 系统统一日志接口，提供常用的日志方法 ，如果需要扩展需要继承该类 <br>
 * 目前只统一记录错误日志
 *
 * @author chengys4
 * @version 0.1
 */
public class ApplicationLogger {
    private static Logger LOGGER = LoggerFactory.getLogger(ApplicationLogger.class);

    private static ThreadLocal<String> requestId = new ThreadLocal<>();

    public static void setRequestId(String requestId) {
        ApplicationLogger.requestId.set(requestId);
    }

    public static void removeRequestId() {
        ApplicationLogger.requestId.remove();
    }

    /**
     * 记录系统错误日志
     *
     * @param errorMsg
     * @param throwable
     */
    public static void error(String errorMsg, Throwable throwable) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getLogClassName()).append("_").append(errorMsg);

        if (throwable == null) {
            LOGGER.error(stringBuilder.toString());
        } else {
            LOGGER.error(stringBuilder.toString(), throwable);
        }
    }

    /**
     * 记录普通日志
     *
     * @param infomsg 日志说明
     */
    public static void info(String infomsg) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getLogClassName()).append(" ").append(infomsg);
        LOGGER.info(stringBuilder.toString());
    }

    public static void info(String infomsg, Object... arguments){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getLogClassName()).append(" ").append(infomsg);
        LOGGER.info(stringBuilder.toString(),arguments);
    }

    /**
     * 记录接口访问日志
     *
     * @param infomsg 日志说明
     */
    public static void info_api(String infomsg) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[").append(getLogId()).append("]").append(" ").append(infomsg);
        LOGGER.info(stringBuilder.toString());
    }

    private static String getLogClassName() {
        StackTraceElement loggerToUse = Thread.currentThread().getStackTrace()[3];
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[").append(getLogId()).append("] ");

        if (ApplicationEnvironment.getSession() != null) {
            stringBuilder.append(ApplicationEnvironment.getEmpCode()).append(" ");
        }
        stringBuilder.append(loggerToUse.getClassName());
        return stringBuilder.toString();
    }

    public static String getLogId() {
        String id = requestId.get();
        if (StringUtils.isBlank(id)) {
            id = UUID.randomUUID().toString();
            id = id.replace("-", "");
            requestId.set(id);
        }
        return id;
    }

}
