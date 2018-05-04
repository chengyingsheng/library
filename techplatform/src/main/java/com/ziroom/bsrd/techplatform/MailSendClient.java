package com.ziroom.bsrd.techplatform;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import com.ziroom.bsrd.client.HttpClientUtils;
import com.ziroom.bsrd.techplatform.exception.SendMailException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 发送邮件到消息平台<br>
 *
 * @author zhoutao
 * @thread-safe
 */
public class MailSendClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(MailSendClient.class);
    private HttpClientUtils httpClientUtils;
    private String mailServerUrl;
    private String token;
    private final Lock lock = new ReentrantLock();

    public MailSendClient() {
        mailServerUrl = "http://message.t.ziroom.com";// 默认测试地址
        token = "DPSK6R4eRem98jyydaSomA";// 默认token
    }

    /**
     * 发送邮件不带附件
     *
     * @param to      - 收件人，多个用逗号隔开
     * @param cc      - 抄送，多个用逗号隔开
     * @param title   - 邮件标题
     * @param content - 邮件内容
     * @return 错误信息 如果发送成功则 返回null
     * @throws IllegalArgumentException
     * @throws SendMailException
     */
    public String sendMail(String to, String cc, String title, String content) throws SendMailException {
        return this.sendMail(to, cc, title, content, null);
    }

    /**
     * 发送邮件带附件
     *
     * @param to      - 收件人，多个用逗号隔开
     * @param cc      - 抄送，多个用逗号隔开
     * @param title   - 邮件标题
     * @param content - 邮件内容
     * @param files   - 附件
     * @return 错误信息 如果发送成功则 返回null
     * @throws IllegalArgumentException
     * @throws SendMailException
     */
    public String sendMail(String to, String cc, String title, String content, List<File> files) throws SendMailException {
        /**
         * 前置校验
         */
        Preconditions.checkArgument(!Strings.isNullOrEmpty(to), "Param to must be not null and empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(title), "Param title must be not null and empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(content), "Param content must be not null and empty");
        Preconditions.checkArgument(httpClientUtils != null, "Filed httpClientUtils must be set");

        /**
         * 发送参数
         */
        LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
        params.put("to", to);
        params.put("cc", cc);
        params.put("title", title);
        params.put("content", content);

        /**
         * 统计邮件送达率
         */
        long start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("[邮件发送统计]");
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        sb.append(",发送者:" + ste.getMethodName());
        sb.append(",接收人:" + to);
        sb.append(",标题:" + title);
        try {
            // 附件
            if (files != null && !files.isEmpty()) {
                List<LinkedHashMap<String, String>> attachments = Lists.newArrayList();
                for (File attachment : files) {
                    ByteSource ByteSource = Files.asByteSource(attachment);
                    byte[] bytes = ByteSource.read();
                    String base64 = Base64.encodeBase64String(bytes);
                    LinkedHashMap<String, String> attachmentParams = Maps.newLinkedHashMap();
                    attachmentParams.put("filename", attachment.getName());
                    attachmentParams.put("base64", base64);
                    // 消息平台支持常用的mimetype自动转换,不需要显示设置,具体支持的mime类型咨询技术平台部
                    attachments.add(attachmentParams);
                }
                params.put("attachments", attachments);
            }
            String resText = null;
            this.lock.lock();
            try {
                params.put("token", token);
                resText = httpClientUtils.httpPostJson(mailServerUrl + "/api/mail/send", JSON.toJSONString(params));
            } finally {
                this.lock.unlock();
            }
            @SuppressWarnings("unchecked")

            Map<String, String> resMap = JSON.parseObject(resText, Map.class);
            String responseCode = resMap.get("response_code");
            if ("0".equals(responseCode)) {
                sb.append(",发送结果:成功");
                return null;
            } else {
                String errorInfo = resMap.get("error_info");
                sb.append(",发送结果:失败," + errorInfo);
                return errorInfo;
            }
        } catch (Exception ex) {
            sb.append(",发送结果:失败,Exception " + ex.getMessage());
            LOGGER.error("MailSendClient.sendMail error.", ex);
            throw new SendMailException("MailSendClient.sendMail error.", ex);
        } finally {
            sb.append(",发送时长:" + (System.currentTimeMillis() - start));
            LOGGER.info(sb.toString());
        }
    }

    public synchronized void setHttpClientUtils(HttpClientUtils httpClientUtils) {
        this.httpClientUtils = httpClientUtils;
    }

    public synchronized void setMailServerUrl(String mailServerUrl) {
        this.mailServerUrl = mailServerUrl;
    }

    public synchronized void setToken(String token) {
        this.token = token;
    }

}
