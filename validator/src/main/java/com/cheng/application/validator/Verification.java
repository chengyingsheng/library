package com.cheng.application.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Verification {


    /**
     * 判断是否是正整数（大于0的整数）
     *
     * @param obj
     * @return
     */
    public static boolean isSignlessnumber(Object obj) {
        if (!Verification.isEmpty(obj)) {
            Pattern p = Pattern.compile("[1-9]+[0-9]*");
            Matcher m = p.matcher(obj.toString().trim());
            return m.matches();
        }
        return false;
    }

    /**
     * 判断是否是整数 （包括负整数）
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^\\d+$|-\\d+$"); // 就是判断是否为整数
        Matcher m = pattern.matcher(str);
        return m.matches();
    }

    /**
     * 判断是否是小数
     *
     * @param obj
     * @return
     */
    public static boolean isDouble(Object obj) {
        if (Verification.isEmpty(obj)) {
            return false;
        }
        Pattern pattern = Pattern.compile("\\d+\\.\\d+$|-\\d+\\.\\d+$");//判断是否为小数
        Matcher matcher = pattern.matcher(obj.toString().trim());
        return matcher.matches();
    }

    /**
     * 第一个字符为非数字
     */
    public static boolean isNotNumberStart(Object obj) {
        if (!Verification.isEmpty(obj)) {
            Pattern p = Pattern.compile("[^0-9](.)*");//非数字开头
            Matcher m = p.matcher(obj.toString().trim());
            return m.matches();
        }
        return false;
    }

    /**
     * 是否包含特殊字符
     */
    public static boolean isContainSpeHtml(Object obj, String[] spe) {
        if (!Verification.isEmpty(obj)) {
            for (int i = 0; i < spe.length; i++) {
                String s = spe[i];
                if (obj.toString().contains(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断对象是否为空，判断字符串为空串，判断集合和map为空，
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是email
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0) {
            return false;
        }
        return email.matches("^[\\w\\-\\.]+@[\\w\\-\\.]+(\\.\\w+)+$");
    }

    /**
     * 判断是否是手机格式
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        if (mobile == null || mobile.trim().length() == 0) {
            return false;
        }
        return mobile.length() == 11 && mobile.matches("^1\\d{10}$");
    }

}
