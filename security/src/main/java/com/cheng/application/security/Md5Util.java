package com.cheng.application.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 注意，此类中，String转byte[]统一使用utf-8编码。如需要gbk编码计算md5，请自转，并使用md5(byte[])接口
 */
public class Md5Util {

    /**
     * @return byte[16]
     */
    public static byte[] md5(byte[] bytes) {
        if (bytes == null) throw new IllegalArgumentException();
        try {
            return MessageDigest.getInstance("MD5").digest(bytes);
        } catch (Exception e) {
            return null;
        }
    }

    private static final char[] HEX_CHARS_LOWER = "0123456789abcdef".toCharArray();
    private static final char[] HEX_CHARS_UPPER = "0123456789ABCDEF".toCharArray();

    private static String asHex(byte[] bytes, boolean lowerCase) {
        if (bytes == null || bytes.length != 16)
            throw new IllegalArgumentException();

        char[] template = lowerCase ? HEX_CHARS_LOWER : HEX_CHARS_UPPER;
        char chars[] = new char[32];
        for (int i = 0; i < chars.length; i = i + 2) {
            byte b = bytes[i / 2];
            chars[i] = template[(b >>> 4) & 0xf];
            chars[i + 1] = template[b & 0xf];
        }
        return new String(chars);
    }


    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c',
            'd', 'e', 'f'};

    public static String encode(File file) {
        FileInputStream in = null;
        MessageDigest md5 = null;
        try {
            in = new FileInputStream(file);
            FileChannel ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return toHex(md5.digest());
    }

    public static String encode(String arg) {
        if (arg == null) {
            arg = "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(arg.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toHex(md5.digest());
    }

    private static String toHex(byte[] bytes) {
        StringBuffer str = new StringBuffer(32);
        for (byte b : bytes) {
            str.append(hexDigits[(b & 0xf0) >> 4]);
            str.append(hexDigits[(b & 0x0f)]);
        }
        return str.toString();
    }

    /**
     * MD5方法
     *
     * @param text 明文
     * @param key  密钥
     * @return 密文
     * @throws UnsupportedEncodingException
     * @throws Exception
     */
    public static String encode(String text, String key) throws UnsupportedEncodingException {
        byte[] bytes = (text + key).getBytes("UTF-8");

        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        messageDigest.update(bytes);
        bytes = messageDigest.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] & 0xff) < 0x10) {
                sb.append("0");
            }

            sb.append(Long.toString(bytes[i] & 0xff, 16));
        }

        return sb.toString().toLowerCase();
    }

    /**
     * MD5验证方法
     *
     * @param text 明文
     * @param key  密钥
     * @param md5  密文
     * @return true/false
     * @throws Exception
     */
    public static boolean verify(String text, String key, String md5) throws Exception {
        String md5Text = encode(text, key);
        if (md5Text.equalsIgnoreCase(md5)) {
            return true;
        } else {
            return false;
        }
    }
}
