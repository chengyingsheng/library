package com.ziroom.bsrd.datatimes;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public final class TimeUtil {

    static final Logger logger = LoggerFactory.getLogger(TimeUtil.class);

	/* ------------------------- with millis ------------------------- */

    public static final long second = 1000;
    public static final long minute = 60 * second;
    public static final long hour = 60 * minute;
    public static final long day = 24 * hour;

	/* ------------------------- format/parse ------------------------- */

    public static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
    public static final DateTimeFormatter compactDateFormatter = DateTimeFormat.forPattern("yyyyMMdd");
    public static final DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    public static long parseDate(String text) {
        return dateFormatter.parseMillis(text);
    }

    public static long parseCompactDate(String text) {
        return compactDateFormatter.parseMillis(text);
    }

    public static long parseTime(String text) {
        return timeFormatter.parseMillis(text);
    }

    public static String formatDate(Date date) {
        return formatDate(date == null ? 0 : date.getTime());
    }

    public static String formatDate(long millis) {
        return millis <= 0 ? "" : dateFormatter.print(millis);
    }

    public static String formatCompactDate(Date date) {
        return formatCompactDate(date == null ? 0 : date.getTime());
    }

    public static String formatCompactDate(long millis) {
        return millis <= 0 ? "" : compactDateFormatter.print(millis);
    }

    public static String formatTime(Date date) {
        return formatTime(date == null ? 0 : date.getTime());
    }

    public static String formatTime(long millis) {
        return millis <= 0 ? "" : timeFormatter.print(millis);
    }

}
