package com.cheng.application.techplatform;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.cheng.application.techplatform.exception.FtpException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 基于commons-net-3.4封装ftp server的访问<br>
 * 建议一个实例对应一个ftp server
 *
 * @author zhoutao
 * @thread-no-safe
 */
public class FtpClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(FtpClient.class);

    /**
     * 默认上传超时时间12秒
     */
    public static final int DEFAULT_UPLOAD_TIMEOUT = 12000;
    /**
     * 默认链接建立上传超时时间2秒
     */
    public static final int DEFAULT_CONNECT_TIMEOUT = 2000;
    /**
     * 默认编码,取决与ftp server使用的编码
     */
    public static final String DEFAULT_ENCODEING = "UTF-8";

    private FTPClient ftpClient;

    private String serverAddress;
    private int serverPort;
    private String userName;
    private String password;

    public FtpClient() {
        /**
         * 默认测试server
         */
        serverAddress = "123.57.32.150";
        serverPort = 0;
        userName = "ftptest";
        password = "123456";

        ftpClient = new FTPClient();
    }

    /**
     * 连接FTP服务器
     */
    public void connect() {
        try {
            if (!ftpClient.isConnected()) {
                ftpClient.configure(getFTPClientConfig());
                ftpClient.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);

                if (serverPort > 0) {
                    ftpClient.connect(serverAddress, serverPort);
                } else {
                    ftpClient.connect(serverAddress);
                }

                // 判断服务器返回值，验证是否已经连接上
                if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                    ftpClient.login(userName, password);
                }
                // 文件类型,默认是ASCII
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                // 设置被动模式
                ftpClient.enterLocalPassiveMode();
                ftpClient.setConnectTimeout(2000);
                ftpClient.setBufferSize(1024);
            }
        } catch (Exception e) {
            disconnect();
            LOGGER.error("FtpClient.connect error", e);
            throw new FtpException("connect error.", e);
        }
    }

    /**
     * 配置FTP连接参数
     *
     * @return
     * @throws Exception
     */
    private FTPClientConfig getFTPClientConfig() throws Exception {
        String systemKey = FTPClientConfig.SYST_UNIX;
        String serverLanguageCode = "zh";
        FTPClientConfig conf = new FTPClientConfig(systemKey);
        conf.setServerLanguageCode(serverLanguageCode);
        conf.setDefaultDateFormatStr("yyyy-MM-dd");
        return conf;
    }

    /**
     * 关闭FTP连接
     */
    public void disconnect() {
        try {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            LOGGER.error("FtpClient.disconnect error", e);
            throw new FtpException("disconnect error.", e);
        }
    }

    /***
     * 删除Ftp文件
     *
     * @param remoteFilePath 本地文件
     */
    public void delete(String remoteFilePath) {
        delete(remoteFilePath, DEFAULT_ENCODEING);
    }

    /***
     * 删除Ftp文件
     */
    public void delete(String remoteFilePath, String encoding) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(remoteFilePath), "Param remoteFilePath must be not null and empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(encoding), "Param encoding must be not null and empty");

        long start = System.currentTimeMillis();

        StringBuffer sb = new StringBuffer();
        sb.append("[ftp文件删除统计]");
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        sb.append(",删除者:" + ste.getMethodName());
        sb.append(",file:" + remoteFilePath);
        try {
            connect();

            /**
             * ftp协议规定使用iso8859编码
             */
            remoteFilePath = new String(remoteFilePath.getBytes(encoding), "iso-8859-1");

            if (!ftpClient.deleteFile(remoteFilePath)) {
                throw new FtpException("delete error,ftp server ruturn false");
            }
            sb.append(",结果:Success");
        } catch (FtpException e) {
            sb.append(",结果:Fail[" + e.getMessage() + "]");
            disconnect();
            LOGGER.error("FtpClient.delete error", e);
            throw e;
        } catch (Exception e) {
            sb.append(",结果:Fail[" + e.getMessage() + "]");
            disconnect();
            LOGGER.error("FtpClient.delete error", e);
            throw new FtpException("delete error.", e);
        } finally {
            sb.append(",耗时[" + (System.currentTimeMillis() - start) + "毫秒]");
            LOGGER.info(sb.toString());
        }
    }

    /***
     * 上传Ftp文件
     *
     * @param file              本地文件
     * @param remoteUpLoadePath - 应该以/结束
     */
    public void upload(File file, String remoteUpLoadePath) {
        upload(file, remoteUpLoadePath, DEFAULT_UPLOAD_TIMEOUT, DEFAULT_ENCODEING);
    }

    /***
     * 上传Ftp文件
     *
     * @param file              本地文件
     * @param remoteUpLoadePath - 应该以/结束
     * @param uploadTimeout     - 应该以/结束
     * @param encoding          - 应该以/结束
     */
    public void upload(File file, String remoteUpLoadePath, int uploadTimeout, String encoding) {
        Preconditions.checkNotNull(file);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(remoteUpLoadePath), "Param romotUpLoadePath must be not null and empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(encoding), "Param encoding must be not null and empty");

        BufferedInputStream bis = null;
        FileInputStream fis = null;
        long start = System.currentTimeMillis();

        StringBuffer sb = new StringBuffer();
        sb.append("[ftp上传统计]");
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        sb.append(",上传者:" + ste.getMethodName());
        sb.append(",file:" + remoteUpLoadePath + file.getName());
        try {
            connect();

            /**
             * ftp协议规定使用iso8859编码
             */
            remoteUpLoadePath = new String(remoteUpLoadePath.getBytes(encoding), "iso-8859-1");
            String fileName = file.getName();
            fileName = new String(fileName.getBytes(encoding), "iso-8859-1");

            if (!ftpClient.changeWorkingDirectory(remoteUpLoadePath)) {
                if (!ftpClient.makeDirectory(remoteUpLoadePath)) {
                    throw new FtpException("无法创建远端指定的目录[" + remoteUpLoadePath + "]");
                }
            }

            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);

            if (uploadTimeout <= 0) {
                uploadTimeout = DEFAULT_UPLOAD_TIMEOUT;
            }
            ftpClient.setDataTimeout(uploadTimeout);

            if (!ftpClient.storeFile(fileName, bis)) {
                throw new FtpException("upload error,ftp server ruturn false");
            }
            sb.append(",结果:Success");
        } catch (FtpException e) {
            sb.append(",结果:Fail[" + e.getMessage() + "]");
            disconnect();
            LOGGER.error("FtpClient.upload error", e);
            throw e;
        } catch (Exception e) {
            sb.append(",结果:Fail[" + e.getMessage() + "]");
            disconnect();
            LOGGER.error("FtpClient.upload error", e);
            throw new FtpException("upload error.", e);
        } finally {
            try {
                if (fis != null)
                    fis.close();
                if (bis != null)
                    bis.close();
            } catch (IOException e) {
            }
            sb.append(",耗时[" + (System.currentTimeMillis() - start) + "毫秒]");
            LOGGER.info(sb.toString());
        }
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

}
