package com.cheng.application.datatimes;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeToolkit {
    public static final String dateFormat_yyyyMMdd = "yyyyMMdd";
    public static final String dateFormat_HHmmss = "HHmmss";
    public static final String dateFormat_date = "yyyy-MM-dd";
    public static final String dateFormat_yyy年MM月dd日 = "yyyy年MM月dd日";
    public static final String dateFormat_ts = "yyyy-MM-dd HH:mm:ss";
    public static final String dateFormat_ts1 = "yyyyMMddHHmmss";
    public static final String dateFormat_datetime = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String dateFormat_HH时mm分 = "HH时mm分";
    public static final String dateFormat_HH时mm分ss秒 = "HH时mm分ss秒";

    public TimeToolkit() {
    }

    public static int getGapSeconds(Date beginTime, Date endTime) {
        if (beginTime != null && endTime != null) {
            Long beginLong = Long.valueOf(beginTime.getTime());
            Long endLong = Long.valueOf(endTime.getTime());
            Long result = Long.valueOf((endLong.longValue() - beginLong.longValue()) / 1000L);
            return result.intValue();
        } else {
            return 0;
        }
    }

    public static synchronized int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(1);
    }

    public static synchronized Date formatStrToDate(String str) throws ParseException {
        if (str != null && str.trim().length() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(str);
            return date;
        } else {
            return null;
        }
    }

    public static long gettime() {
        Date d = new Date();
        long time = d.getTime();
        d = null;
        return time;
    }

    public static String[] convertWeekByDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int dayWeek = cal.get(7);
        if (1 == dayWeek) {
            cal.add(5, -1);
        }

        cal.setFirstDayOfWeek(2);
        int day = cal.get(7);
        cal.add(5, cal.getFirstDayOfWeek() - day);
        String imptimeBegin = sdf.format(cal.getTime());
        cal.add(5, 6);
        String imptimeEnd = sdf.format(cal.getTime());
        return new String[]{imptimeBegin, imptimeEnd};
    }

    public static String getCurrentDate(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String Clogintime = format.format(new Date());
        return Clogintime;
    }

    public static String getCurrentDate(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String Clogintime = format.format(date);
        return Clogintime;
    }

    public static String getCurrentHour(String dateStr) {
        try {
            SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date bdate = e.parse(dateStr);
            SimpleDateFormat format = new SimpleDateFormat("HH");
            String Clogintime = format.format(bdate);
            return Clogintime;
        } catch (Exception var5) {
            return "";
        }
    }

    public static String getCurrentDate() {
        return getCurrentDate("yyyy-MM-dd");
    }

    public static long getCurrentDateLong() {
        return Long.valueOf(getCurrentDate("yyyyMMdd"));
    }

    public static String getCurrentTs() {
        return getCurrentDate("yyyy-MM-dd HH:mm:ss");
    }

    public static long getCurrentTsLong() {
        return Long.valueOf(getCurrentDate("yyyyMMddHHmmss"));
    }

    public static String getCurrentTs(String dateFormat) {
        return getCurrentDate(dateFormat);
    }

    public static String getCurrentTs(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String Clogintime = format.format(date);
        return Clogintime;
    }


    public static String secondToCurrentTs(String timestampString) {
        if (StringUtils.isBlank(timestampString)) {
            return getCurrentTs();
        } else {
            Long timestamp = Long.valueOf(Long.parseLong(timestampString));
            return secondToCurrentTs(timestamp.longValue());
        }
    }

    public static String secondToCurrentTs(long secondtime) {
        return getCurrentTs((new Date(secondtime * 1000L)));
    }

    public static long currentTsToSecond() {
        long time = (new Date()).getTime();
        return time / 1000L;
    }

    public static long currentTsToSecond(String timestamp) {
        Date date = null;

        try {
            date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(timestamp);
        } catch (ParseException e) {
            throw new RuntimeException("所给日期格式不是  yyyy-MM-dd HH:mm:ss");
        }

        return date.getTime() / 1000L;
    }

    public static String getNextDay(int days) {
        return getNextDay((String) null, days);
    }

    public static String getNextDay(String datastr, int days) {
        return getNextDay("yyyy-MM-dd", datastr, days);
    }

    public static Date getNextMonth(Date data, int mouths) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.add(Calendar.MONTH, mouths);
        return c.getTime();
    }

    public static Date getNextDays(Date data, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    public static String getNextDay(String dateformat, String datastr, int days) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(dateformat);
        if (StringUtils.isEmpty(datastr)) {
            c.setTime(new Date());
        } else {
            try {
                c.setTime(format.parse(datastr));
            } catch (ParseException var6) {
                throw new RuntimeException("日期格式 不符合" + dateformat + " 格式");
            }
        }

        c.add(5, days);
        String Clogintime = format.format(c.getTime());
        return Clogintime;
    }

    public static String getNextMinute(int minute) {
        return getNextMinute((String) null, minute);
    }

    public static String getNextMinute(String datastr, int minute) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isEmpty(datastr)) {
            c.setTime(new Date());
        } else {
            try {
                c.setTime(format.parse(datastr));
            } catch (ParseException var5) {
                throw new RuntimeException("日期格式 不符合 yyyy-MM-dd HH:mm:ss 格式");
            }
        }

        c.add(12, minute);
        String Clogintime = format.format(c.getTime());
        return Clogintime;
    }

    public static String getNextMouth(String datastr, int days, int mouths) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isEmpty(datastr)) {
            c.setTime(new Date());
        } else {
            try {
                c.setTime(format.parse(datastr));
            } catch (ParseException var6) {
                throw new RuntimeException("日期格式 不符合 yyyy-MM-dd 格式");
            }
        }

        c.add(5, days);
        c.add(2, mouths);
        String Clogintime = format.format(c.getTime());
        return Clogintime;
    }

    public static Date getNextMouth(Date data, int days, int mouths) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.add(5, days);
        c.add(2, mouths);
        return c.getTime();
    }

    public static Date getNextYear(Date data, int year, int mouths, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.add(Calendar.YEAR, year);
        c.add(Calendar.MONTH, mouths);
        c.add(Calendar.DATE, days);// 日期减1
        return c.getTime();
    }

    public static Date getNextMouth(Date data, int mouths) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.add(Calendar.MONTH, mouths);
        return c.getTime();
    }

    @Deprecated
    public static String getNextMouth(int days, int mouths) throws ParseException {
        return getNextMouth((String) null, days, mouths);
    }

    /**
     * 计算两个日期的相差天数
     *
     * @param begindate 开始日期 （小）
     * @param enddate   结束日期 （大）
     * @return
     */
    public static int daysBetween(String begindate, String enddate) {
        try {
            SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(e.parse(begindate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(e.parse(enddate));
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / 86400000L;
            return Integer.parseInt(String.valueOf(between_days));
        } catch (ParseException var10) {
            throw new RuntimeException("日期格式 不符合 yyyy-MM-dd 格式");
        }
    }

    /**
     * 计算两个日期的相差天数
     *
     * @param begindate 开始日期 （小）
     * @param enddate   结束日期 （大）
     * @return
     */
    public static int daysBetween(Date begindate, Date enddate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(begindate);
        long time1 = cal.getTimeInMillis();

        cal.setTime(enddate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / 86400000L;
        return Integer.parseInt(String.valueOf(between_days));
    }

    public static int daysBetween2(Date begindate, Date enddate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(begindate);
        long time1 = cal.getTimeInMillis();

        cal.setTime(enddate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / 86400000L;
        return Integer.parseInt(String.valueOf(between_days)) + 1;
    }

    /**
     * 两个时间相差的分钟数，如果 begindatetime>enddatetime，否则返回负整数
     *
     * @param begindatetime "yyyy-MM-dd HH:mm:ss" 形式的时间字符串
     * @param enddatetime   "yyyy-MM-dd HH:mm:ss" 形式的时间字符串
     * @return
     */
    public static int subdatetimereturnmin(String begindatetime, String enddatetime) {
        try {
            SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.setTime(e.parse(begindatetime));
            long time1 = cal.getTimeInMillis();
            cal.setTime(e.parse(enddatetime));
            long time2 = cal.getTimeInMillis();
            long between_days = (time1 - time2) / 60000L;
            return Integer.parseInt(String.valueOf(between_days));
        } catch (ParseException var10) {
            throw new RuntimeException("日期格式 不符合 yyyy-MM-dd HH:mm:ss 格式");
        }
    }

    /**
     * 两个时间相差的分钟数，如果 currentdate>passDate则返回正整数，否则返回负整数
     *
     * @param currentdate
     * @param passDate
     * @return
     */
    public static int subdatetimereturnmin(Date currentdate, Date passDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentdate);
        long currentdatetime = cal.getTimeInMillis();
        cal.setTime(passDate);
        long passDatetime = cal.getTimeInMillis();
        long between_days = (currentdatetime - passDatetime) / 60000L;
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 两个时间相差的天数，如果 currentdate>passDate则返回正整数，否则返回负整数
     *
     * @param currentdate
     * @param passDate
     * @return
     */
    public static int subdatetimereturdays(Date currentdate, Date passDate) {
        return subdatetimereturnmin(currentdate, passDate) / 60 / 24;
    }

    public static int compareDateOfStr(String begindate, String enddate) {
        int i = daysBetween(begindate, enddate);
        return i > 0 ? 1 : (i == 0 ? 0 : (i < 0 ? -1 : i));
    }

    public static String getLastDayOfMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(new Date());
        cDay.set(5, cDay.getActualMaximum(5));
        return format.format(cDay.getTime());
    }

    public static String getFirstDayOfMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(new Date());
        cDay.set(5, cDay.getActualMinimum(5));
        return format.format(cDay.getTime());
    }

    public static String getFirstDayOfMonth(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        cDay.set(5, cDay.getActualMinimum(5));
        return format.format(cDay.getTime());
    }

    public static String getLastDayOfMonth(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        cDay.set(5, cDay.getActualMaximum(5));
        return format.format(cDay.getTime());
    }

    public static String getLastDayOfMonth(String datastr) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat_date);
        Calendar cDay = Calendar.getInstance();
        try {
            cDay.setTime(format.parse(datastr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cDay.set(Calendar.DAY_OF_MONTH,
                cDay.getActualMaximum(Calendar.DAY_OF_MONTH));
        return format.format(cDay.getTime());

    }

    public static String getDateDay(String date) {
        String ss = date.substring(8, 10);
        return ss.startsWith("0") ? ss.substring(1) : ss;
    }

    public static boolean isBettwenTime(String sourcetime, String begintime, String endtime) {
        try {
            SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.setTime(e.parse(begintime));
            long time1 = cal.getTimeInMillis();
            cal.setTime(e.parse(endtime));
            long time2 = cal.getTimeInMillis();
            cal.setTime(e.parse(sourcetime));
            long time3 = cal.getTimeInMillis();
            return time3 - time1 > 0L && time3 - time2 < 0L;
        } catch (ParseException var11) {
            throw new RuntimeException("日期格式 不符合 yyyy-MM-dd HH:mm:ss  格式");
        }
    }

    public static boolean isBettwenTime(String begintime, String endtime) {
        try {
            SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.setTime(e.parse(begintime));
            long time1 = cal.getTimeInMillis();
            cal.setTime(e.parse(endtime));
            long time2 = cal.getTimeInMillis();
            cal.setTime(e.parse(getCurrentTs()));
            long time3 = cal.getTimeInMillis();
            return time3 - time1 > 0L && time3 - time2 < 0L;
        } catch (ParseException var10) {
            throw new RuntimeException("日期格式 不符合 yyyy-MM-dd HH:mm:ss  格式");
        }
    }


    public static boolean isWeekend() {
        return isWeekend((new Date())) == 7 || isWeekend((new Date())) == 1;
    }

    public static boolean isWeekend(String dateStr) {
        try {
            SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd");
            Date bdate = e.parse(dateStr);
            return isWeekend(bdate) == 7 || isWeekend(bdate) == 1;
        } catch (Exception var3) {
            return false;
        }
    }

    private static int isWeekend(Date bdate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(bdate);
        return cal.get(7) == 7 ? 7 : (cal.get(7) == 1 ? 1 : -9999);
    }

    public static String getFirstWeekdayFromWeekend() {
        return getFirstWeekdayFromWeekend((new Date()));
    }

    public static String getFirstWeekdayFromWeekend(String dateStr) {
        try {
            SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd");
            Date bdate = e.parse(dateStr);
            return getFirstWeekdayFromWeekend(bdate);
        } catch (Exception var3) {
            throw new IllegalStateException("日期格式错误");
        }
    }

    public static String getFirstWeekdayFromWeekend(Date date) {
        int d = isWeekend(date);
        if (d == 1) {
            return getNextDay(getCurrentDate(date, "yyyy-MM-dd"), 1);
        } else if (d == 7) {
            return getNextDay(getCurrentDate(date, "yyyy-MM-dd"), 2);
        } else {
            throw new IllegalStateException("非法日期");
        }
    }

    public static Date parseDate(String datestr) {
        try {
            SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd");
            Date bdate = e.parse(datestr);
            return bdate;
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseTs(String datestr) {
        try {
            SimpleDateFormat e = new SimpleDateFormat(TimeToolkit.dateFormat_ts);
            Date bdate = e.parse(datestr);
            return bdate;
        } catch (Exception e) {
            return null;
        }
    }


    public static String parseDate(Timestamp timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String Clogintime = format.format(timestamp);
        return Clogintime;
    }

    public static String parseStr(Date datestr) {
        try {
            SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd");
            return e.format(datestr);
        } catch (Exception e) {
            return null;
        }
    }

    public static String parseTsStr(Date datestr) {
        try {
            SimpleDateFormat e = new SimpleDateFormat(dateFormat_ts);
            return e.format(datestr);
        } catch (Exception e) {
            return null;
        }
    }

    public static String parseStr(Date datestr, String format) {
        try {
            SimpleDateFormat e = new SimpleDateFormat(format);
            return e.format(datestr);
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer getMonth(String date) {
        if (null == date) {
            return null;
        }
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(parseDate(date));
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static Integer getDay(String date) {
        if (null == date) {
            return null;
        }
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(parseDate(date));
        return calendar.get(Calendar.DATE);
    }

    public static Integer getMonth(Date date) {
        if (null == date) {
            return null;
        }
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * Java将Unix时间戳 毫秒 转换成指定格式日期字符串
     *
     * @param timestampString 时间戳 如："1473048265000";
     * @param formats         要格式化的格式 默认："yyyy-MM-dd HH:mm:ss";
     * @return 返回结果 如："2016-09-05 16:06:42";
     */
    public static String TimeStamp2Date(String timestampString, String formats) {
        if (StringUtils.isBlank(formats)) {
            formats = dateFormat_ts;
        }
        Long timestamp = Long.parseLong(timestampString);
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }

    public static String TimeStamp2Date(String timestamp) {
        return TimeStamp2Date(timestamp, null);
    }

}
