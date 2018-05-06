package com.cheng.application.techplatform;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.cheng.application.client.HttpClientUtils;
import com.cheng.application.techplatform.exception.SendSmsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 发送短信到消息平台<br>
 *
 * @author zhoutao
 * @thread-safe
 */
public class SmsSendClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(SmsSendClient.class);
    private HttpClientUtils httpClientUtils;
    private String smsServerUrl;
    private String token;
    private final Lock lock = new ReentrantLock();

    public SmsSendClient() {
        smsServerUrl = "http://message.t.ziroom.com";// 默认测试地址
        token = "YRQ4NqJ5Ra2nYuMGRmcSlQ";// 默认token
    }

    /**
     * @param to      - 收信人，多个用逗号隔开
     * @param content - 短信内容
     * @return 错误消息 如果发送成功 则返回null
     * @throws IllegalArgumentException
     * @throws SendSmsException
     */
    public String sendSms(String to, String content) throws SendSmsException {
        /**
         * 前置校验
         */
        Preconditions.checkArgument(!Strings.isNullOrEmpty(to), "Param to must be not null and empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(content), "Param content must be not null and empty");
        Preconditions.checkArgument(httpClientUtils != null, "Filed httpClientUtils must be set");

        /**
         * 发送
         */
        LinkedHashMap<String, String> params = Maps.newLinkedHashMap();
        params.put("to", to);
        params.put("content", content);
        boolean result = false;
        /**
         * 统计邮件送达率
         */
        long start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("[短信发送统计]");
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        sb.append(",发送者:" + ste.getMethodName());
        sb.append(",接收人:" + to);
        try {
            String resText = null;
            this.lock.lock();
            try {
                params.put("token", token);
                resText = httpClientUtils.httpPostJson(smsServerUrl + "/api/sms/send", JSON.toJSONString(params));
            } finally {
                this.lock.unlock();
            }
            @SuppressWarnings("unchecked")
            Map<String, String> resMap = JSON.parseObject(resText, Map.class);
            String responseCode = resMap.get("response_code");
            if ("0".equals(responseCode)) {
                result = true;
                sb.append(",发送结果:成功");
                return null;
            } else {
                String errorInfo = resMap.get("error_info");
                sb.append(",发送结果:失败," + errorInfo);
                return errorInfo;
            }
        } catch (Exception ex) {
            sb.append(",发送结果:失败,Exception " + ex.getMessage());
            LOGGER.error("SmsSendClient.sendSms error.", ex);
            throw new SendSmsException("SmsSendClient.sendSms error.", ex);
        } finally {
            sb.append(",发送时长:" + (System.currentTimeMillis() - start));
            LOGGER.info(sb.toString());
        }
    }

    public synchronized void setHttpClientUtils(HttpClientUtils httpClientUtils) {
        this.httpClientUtils = httpClientUtils;
    }

    public synchronized void setSmsServerUrl(String smsServerUrl) {
        this.smsServerUrl = smsServerUrl;
    }

    public synchronized void setToken(String token) {
        this.token = token;
    }

}
