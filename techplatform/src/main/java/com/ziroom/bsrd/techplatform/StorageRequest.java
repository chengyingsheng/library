package com.ziroom.bsrd.techplatform;

import java.io.InputStream;

/**
 * 存储平台请求对象
 *
 * @author zhoutao
 * @version 0.0.1
 * @since 2015/11/24
 */
public class StorageRequest {

    /**
     * 允许上传的文件类型,多个用,号隔开
     */
    private String allowFileTypes;
    /**
     * 允许上传的文件类型,多个用,号隔开
     */
    private int allowMaxFileSize;
    /**
     * 包含文件的输入流
     */
    private InputStream inputStream;
    /**
     * 请求来源系统。申请资源时的来源key
     */
    private String source;
    /**
     * 文件唯一标示
     */
    private String uuid;
    /**
     * 文件名
     */
    private String filename;
    /**
     * 内容的base64编码
     */
    private String base64;
    /**
     * 文件存储类型。申请资源时的可用存储类型，如内网图片，需要传递ii
     */
    private String type;
    /**
     * 标签（多个用逗号分隔）
     */
    private String tags;
    /**
     * 描述
     */
    private String desc;
    /**
     * 到什么时候有效（过期自动删除），秒级别时间戳。如到2018/12/12 0:0:0过期，则传递1544544000
     */
    private String activeTime;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(String activeTime) {
        this.activeTime = activeTime;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getAllowFileTypes() {
        return allowFileTypes;
    }

    public void setAllowFileTypes(String allowFileTypes) {
        this.allowFileTypes = allowFileTypes;
    }

    public int getAllowMaxFileSize() {
        return allowMaxFileSize;
    }

    public void setAllowMaxFileSize(int allowMaxFileSize) {
        this.allowMaxFileSize = allowMaxFileSize;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("StorageRequest [allowFileTypes=");
        builder.append(allowFileTypes);
        builder.append(", allowMaxFileSize=");
        builder.append(allowMaxFileSize);
        builder.append(", source=");
        builder.append(source);
        builder.append(", uuid=");
        builder.append(uuid);
        builder.append(", filename=");
        builder.append(filename);
        builder.append(", base64=");
        builder.append(base64);
        builder.append(", type=");
        builder.append(type);
        builder.append(", tags=");
        builder.append(tags);
        builder.append(", desc=");
        builder.append(desc);
        builder.append(", activeTime=");
        builder.append(activeTime);
        builder.append("]");
        return builder.toString();
    }
}
