package com.cheng.application.techplatform;

/**
 * 存储平台响应对象
 *
 * @author zhoutao
 * @version 0.0.1
 * @since 2015/11/24
 */
public class StorageResponse {
    /**
     * 服务端机器名称
     */
    private String serverMachineName;
    /**
     * 服务端ip地址
     */
    private String serverIp;
    /**
     * 服务器响应时间戳
     */
    private String serverCurrentTime;
    /**
     * 服务返回编码
     */
    private String responseCode;
    /**
     * 服务器响应信息
     */
    private String messageInfo;
    /**
     * 操作错误信息
     */
    private String errorInfo;
    /**
     * 文件资源uuid
     */
    private String uuid;
    /**
     * 文件名,原始名字,如'IMG_001.jpg'
     */
    private String originFilename;
    /**
     * fastdfs的完整url,如'http://image.ziroom.com/g1/M00/00/3A/rBAN3lZ72oWAYM9sAAE9rAQ-9bY512.jpg'
     */
    private String url;
    /**
     * 域名,如'image.ziroom.com'
     */
    private String domain;
    /**
     * url中不包含域名和后缀部分,如'/g1/M00/00/3A/rBAN3lZ72oWAYM9sAAE9rAQ-9bY512'
     */
    private String urlBase;
    /**
     * 文件后缀,如'.jpg'
     */
    private String urlExt;
    /**
     * 标签
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

    public String getServerMachineName() {
        return serverMachineName;
    }

    public void setServerMachineName(String serverMachineName) {
        this.serverMachineName = serverMachineName;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerCurrentTime() {
        return serverCurrentTime;
    }

    public void setServerCurrentTime(String serverCurrentTime) {
        this.serverCurrentTime = serverCurrentTime;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(String messageInfo) {
        this.messageInfo = messageInfo;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOriginFilename() {
        return originFilename;
    }

    public void setOriginFilename(String originFilename) {
        this.originFilename = originFilename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUrlBase() {
        return urlBase;
    }

    public void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }

    public String getUrlExt() {
        return urlExt;
    }

    public void setUrlExt(String urlExt) {
        this.urlExt = urlExt;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("StorageResponse [serverMachineName=");
        builder.append(serverMachineName);
        builder.append(", serverIp=");
        builder.append(serverIp);
        builder.append(", serverCurrentTime=");
        builder.append(serverCurrentTime);
        builder.append(", responseCode=");
        builder.append(responseCode);
        builder.append(", messageInfo=");
        builder.append(messageInfo);
        builder.append(", errorInfo=");
        builder.append(errorInfo);
        builder.append(", uuid=");
        builder.append(uuid);
        builder.append(", originFilename=");
        builder.append(originFilename);
        builder.append(", url=");
        builder.append(url);
        builder.append(", domain=");
        builder.append(domain);
        builder.append(", urlBase=");
        builder.append(urlBase);
        builder.append(", urlExt=");
        builder.append(urlExt);
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
