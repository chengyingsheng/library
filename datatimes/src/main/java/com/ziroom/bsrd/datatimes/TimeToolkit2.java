package com.ziroom.bsrd.datatimes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeToolkit2 {

    public static String YYYY_MM_DD = "yyyy-MM-dd";
    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";


    /**
     * 将时间戳转换为时间 yyyy-MM-dd HH:mm:ss
     */
    public static String stampToDateTime(long stamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        return simpleDateFormat.format(new Date(stamp));
    }

    /**
     * 将时间戳转换为日期 yyyy-MM-dd
     */
    public static String stampToDate(long stamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD);
        return simpleDateFormat.format(new Date(stamp));
    }

    /**
     * 将时间（yyyy-MM-dd HH:mm:ss）转换为时间戳
     */
    public static long dateTimeToStamp(String dateTime) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
            return simpleDateFormat.parse(dateTime).getTime();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将日期（yyyy-MM-dd）转换为时间戳
     *
     * @param date
     * @return
     */
    public static long dateToStamp(String date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD);
            return simpleDateFormat.parse(date).getTime();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定日期的第一条
     *
     * @param orgDate 原始Date
     * @return Date
     */
    public static Date getFirstDayOfMonth(Date orgDate) {
        Calendar calendar = Calendar.getInstance();//获取当前日期
        calendar.setTime(orgDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);//
        //将小时至0
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        calendar.set(Calendar.MINUTE, 0);
        //将秒至0
        calendar.set(Calendar.SECOND, 0);
        //将毫秒至0
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取指定日期所在月的最后一天
     *
     * @param orgDate 原始Date
     * @return Date
     */
    public static Date getLastDayOfMonth(Date orgDate) {
        Calendar calendar = Calendar.getInstance();//获取当前日期
        calendar.setTime(orgDate);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
}
