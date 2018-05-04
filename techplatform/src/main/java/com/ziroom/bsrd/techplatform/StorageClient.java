package com.ziroom.bsrd.techplatform;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.ziroom.bsrd.basic.FileTypeUtils;
import com.ziroom.bsrd.client.HttpClientUtils;
import com.ziroom.bsrd.techplatform.exception.MaxUploadSizeLimitException;
import com.ziroom.bsrd.techplatform.exception.NotAllowedUploadTypeException;
import com.ziroom.bsrd.techplatform.exception.StorageException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 存储平台客户端
 *
 * @author zhoutao
 * @version 0.0.1
 * @thread-safe
 * @since 2015/11/24
 */
public class StorageClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(StorageClient.class);
    /**
     * 读上传文件流使用的bufsize
     */
    private static final int BUF_SIZE = 0x1000; // 4K
    /**
     * 默认最大只能上传5M
     */
    private static final int MAX_UPLOAD_SIZE = 5 * 1024 * 1024; // 5M
    private HttpClientUtils httpClientUtils;
    private String storageServerUrl;
    private final Lock lock = new ReentrantLock();

    public StorageClient() {
        storageServerUrl = "http://storage.t.ziroom.com";// 默认测试地址
    }

    /**
     * 上传文件
     *
     * @param storageRequest
     * @throws NotAllowedUploadTypeException - 不允许上传的文件类型异常
     * @throws MaxUploadSizeLimitException   - 超过最大大小限制异常
     * @throws IllegalArgumentException      - 参数异常
     * @throws StorageException              - 服务端内部异常
     */
    public StorageResponse upload(StorageRequest storageRequest) throws StorageException, NotAllowedUploadTypeException, MaxUploadSizeLimitException {
        /**
         * 前置校验
         */
        Preconditions.checkArgument(storageRequest != null, "Param storageRequest must be not null");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(storageRequest.getSource()), "Param storageRequest.source must be not null and empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(storageRequest.getFilename()), "Param storageRequest.fileName must be not null and empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(storageRequest.getType()), "Param storageRequest.type must be not null and empty");
        Preconditions.checkArgument(httpClientUtils != null, "Filed httpClientUtils must be set");

        /**
         * 得到代表文件内容的base64
         */
        try {
            storageRequest.setBase64(this.getBase64(storageRequest));
        } catch (IOException e) {
            LOGGER.error("StorageClient.upload getBase64 error.", e);
            throw new StorageException(e.getMessage(), e);
        }

        /**
         * 上传参数
         */
        LinkedHashMap<String, String> params = Maps.newLinkedHashMap();
        params.put("source", storageRequest.getSource());
        params.put("filename", storageRequest.getFilename());
        params.put("type", storageRequest.getType());
        params.put("tags", storageRequest.getTags());
        params.put("desc", storageRequest.getDesc());
        params.put("active_time", storageRequest.getActiveTime());
        params.put("base64", storageRequest.getBase64());
        /**
         * 统计上传过程
         */
        long start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("[文件上传统计]");
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        sb.append(",上传者:" + ste.getMethodName());
        try {
            String resText = null;
            this.lock.lock();
            try {
                resText = httpClientUtils.httpPostJson(storageServerUrl + "/api/file/upload", JSON.toJSONString(params));
            } finally {
                this.lock.unlock();
            }
            StorageResponse storageResponse = responseJsonStr2Obj(resText);
            sb.append(",上传结果:" + storageResponse.toString());

            String responseCode = storageResponse.getResponseCode();
            if (!"0".equals(responseCode)) {
                String errorInfo = storageResponse.getErrorInfo();
                throw new StorageException("上传失败," + errorInfo);
            }
            return storageResponse;
        } catch (StorageException ex) {
            throw ex;
        } catch (Exception ex) {
            sb.append(",上传结果:失败,Exception " + ex.getMessage());
            LOGGER.error("StorageClient.upload error.", ex);
            throw new StorageException(ex.getMessage(), ex);
        } finally {
            sb.append(",上传时长:" + (System.currentTimeMillis() - start));
            LOGGER.info(sb.toString());
        }
    }

    /**
     * 查询文件 默认超时时间 20 秒
     *
     * @param uuid   - 图片标识,@see StorageRequest.uuid
     * @param source - 来源系统,@see StorageRequest.source
     * @throws IllegalArgumentException - 参数异常
     * @throws StorageException         - 服务端内部异常
     */
    public StorageResponse query(String uuid, String source) throws StorageException {
        /**
         * 前置校验
         */
        Preconditions.checkArgument(!Strings.isNullOrEmpty(uuid), "Param uuid must be not null and empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(source), "Param source must be not null and empty");
        Preconditions.checkArgument(httpClientUtils != null, "Filed httpClientUtils must be set");

        /**
         * 上传参数
         */
        LinkedHashMap<String, String> params = Maps.newLinkedHashMap();
        params.put("source", source);
        params.put("uuid", uuid);
        /**
         * 统计上传过程
         */
        long start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("[文件查询统计]");
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        sb.append(",查询者:" + ste.getMethodName());
        try {
            String resText = null;
            this.lock.lock();
            try {
                resText = httpClientUtils.httpPostJson(storageServerUrl + "/api/file/info", JSON.toJSONString(params), 20000);
            } finally {
                this.lock.unlock();
            }
            StorageResponse storageResponse = responseJsonStr2Obj(resText);
            sb.append(",查询结果:" + storageResponse.toString());

            String responseCode = storageResponse.getResponseCode();
            if (!"0".equals(responseCode)) {
                String errorInfo = storageResponse.getErrorInfo();
                throw new StorageException("查询失败," + errorInfo);
            }
            return storageResponse;
        } catch (StorageException ex) {
            throw ex;
        } catch (Exception ex) {
            sb.append(",查询结果:失败,Exception " + ex.getMessage());
            LOGGER.error("StorageClient.upload error.", ex);
            throw new StorageException(ex.getMessage(), ex);
        } finally {
            sb.append(",查询时长:" + (System.currentTimeMillis() - start));
            LOGGER.info(sb.toString());
        }
    }

    /**
     * 删除文件
     *
     * @param uuid   - 图片标识,@see StorageRequest.uuid
     * @param source - 来源系统,@see StorageRequest.source
     * @throws IllegalArgumentException - 参数异常
     * @throws StorageException         - 服务端内部异常
     */
    public void delete(String uuid, String source) throws StorageException {
        /**
         * 前置校验
         */
        Preconditions.checkArgument(!Strings.isNullOrEmpty(uuid), "Param uuid must be not null and empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(source), "Param source must be not null and empty");
        Preconditions.checkArgument(httpClientUtils != null, "Filed httpClientUtils must be set");

        /**
         * 上传参数
         */
        LinkedHashMap<String, String> params = Maps.newLinkedHashMap();
        params.put("source", source);
        params.put("uuid", uuid);
        /**
         * 统计上传过程
         */
        long start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("[文件删除统计]");
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        sb.append(",删除者:" + ste.getMethodName());
        try {
            String resText = null;
            this.lock.lock();
            try {
                resText = httpClientUtils.httpPostJson(storageServerUrl + "/api/file/delete", JSON.toJSONString(params));
            } finally {
                this.lock.unlock();
            }
            sb.append(",删除结果:" + resText);

            Map resMap = JSON.parseObject(resText, Map.class);
            String responseCode = (String) resMap.get("response_code");
            if (!"0".equals(responseCode)) {
                String errorInfo = (String) resMap.get("error_info");
                throw new StorageException("删除失败," + errorInfo);
            }
        } catch (StorageException ex) {
            throw ex;
        } catch (Exception ex) {
            sb.append(",删除结果:失败,Exception " + ex.getMessage());
            LOGGER.error("StorageClient.delete error.", ex);
            throw new StorageException(ex.getMessage(), ex);
        } finally {
            sb.append(",删除时长:" + (System.currentTimeMillis() - start));
            LOGGER.info(sb.toString());
        }
    }

    @SuppressWarnings({"rawtypes"})
    private StorageResponse responseJsonStr2Obj(String json) {
        Map resMap = JSON.parseObject(json, Map.class);
        StorageResponse storageResponse = new StorageResponse();
        storageResponse.setServerMachineName((String) resMap.get("server_machine_name"));
        storageResponse.setServerIp((String) resMap.get("server_ip"));
        storageResponse.setServerCurrentTime((String) resMap.get("server_current_time"));
        storageResponse.setResponseCode((String) resMap.get("response_code"));
        storageResponse.setMessageInfo((String) resMap.get("message_info"));
        storageResponse.setErrorInfo((String) resMap.get("error_info"));
        Map fileMap = (Map) resMap.get("file");
        storageResponse.setUuid((String) fileMap.get("uuid"));
        storageResponse.setOriginFilename((String) fileMap.get("original_filename"));
        storageResponse.setUrl((String) fileMap.get("url"));
        storageResponse.setDomain((String) fileMap.get("domain"));
        storageResponse.setUrlBase((String) fileMap.get("url_base"));
        storageResponse.setUrlExt((String) fileMap.get("url_ext"));
        Object tags = fileMap.get("tags");
        if (tags != null) {
            storageResponse.setTags(tags.toString());
        }
        storageResponse.setDesc((String) fileMap.get("desc"));
        storageResponse.setActiveTime((String) fileMap.get("active_time"));
        return storageResponse;
    }

    public synchronized void setHttpClientUtils(HttpClientUtils httpClientUtils) {
        this.httpClientUtils = httpClientUtils;
    }

    public synchronized void setStorageServerUrl(String storageServerUrl) {
        this.storageServerUrl = storageServerUrl;
    }

    /**
     * 内部使用方法 - 获取要上传给存储平台的文件内容
     *
     * @param storageRequest
     * @return
     * @throws IOException
     * @throws NotAllowedUploadTypeException
     * @throws MaxUploadSizeLimitException
     */
    private String getBase64(StorageRequest storageRequest) throws IOException, NotAllowedUploadTypeException, MaxUploadSizeLimitException {
        String base64 = storageRequest.getBase64();
        String allowFileTypes = storageRequest.getAllowFileTypes();
        InputStream inputStream = storageRequest.getInputStream();
        /**
         * 前置校验 base64和inputStream只能传一个
         */
        if (!Strings.isNullOrEmpty(base64)) {
            // 如果传了base64,关闭流
            if (inputStream != null) {
                inputStream.close();
            }
            return base64;
        }
        // 通过文件流获取图片内容
        if (inputStream == null) {
            throw new IllegalArgumentException("base64和inputStream必须设置一个才能上传");
        }
        try {
            // 允许上传的文件类型,只在通过文件流方式上传时校验
            if (Strings.isNullOrEmpty(allowFileTypes)) {
                throw new IllegalArgumentException("allowFileTypes必须设置,如jpg,png");
            }
        /*
         * 获取文件类型
	     */
            byte[] fileTypeBytes = new byte[4];
            inputStream.read(fileTypeBytes);
            String fileType = FileTypeUtils.getFileType(fileTypeBytes);
            if (allowFileTypes.toLowerCase().indexOf(fileType) == -1) {
                throw new NotAllowedUploadTypeException("只允许上传" + allowFileTypes + "类型的文件,当前实际类型为:" + fileType);
            }

            /**
             * 字节输出流,无须关闭
             */
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write(fileTypeBytes);

            /**
             * 获取文件其他内容
             */
            byte[] buf = new byte[BUF_SIZE];
            long total = 4;
            // 设置最大大小
            int maxSize = storageRequest.getAllowMaxFileSize() <= 0 ? MAX_UPLOAD_SIZE : storageRequest.getAllowMaxFileSize();
            while (true) {
                if (total >= maxSize) {
                    throw new MaxUploadSizeLimitException("超过了上传的文件最大" + maxSize + "的限制");
                }
                int r = inputStream.read(buf);
                if (r == -1) {
                    break;
                }
                out.write(buf, 0, r);
                total += r;
            }
            byte[] fileContentBytes = out.toByteArray();
            // 使用和存储平台版本一致的org.apache.commons.codec.binary.Base64 1.9版本
            return Base64.encodeBase64String(fileContentBytes);
        } finally {
            inputStream.close();
        }
    }

    public static void main(String[] args) {
        StorageClient storageClient = new StorageClient();
        HttpClientUtils httpClientUtils = new HttpClientUtils();
        storageClient.setHttpClientUtils(httpClientUtils);

        try {
            StorageRequest storageRequest = new StorageRequest();
            storageRequest.setAllowFileTypes("jpg,png");
            storageRequest.setSource("corebusiness.config");
            storageRequest.setFilename("首次支付流程.jpg");
            storageRequest.setType("ei");
            FileInputStream fis = new FileInputStream("f:/首次支付流程.jpg");
            storageRequest.setInputStream(fis);
            System.out.println(storageClient.upload(storageRequest));

        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }
}
