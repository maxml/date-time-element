package com.maxml.datetime.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by maxml on 15.08.16.
 */
public class DateFormatter {

    // YYYY-MM-DD HH:MM:SS:MS format (e.g. 2016-07-27 15:48:06.158)
    private static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    // TODO: send here
    private static String UI_REQUEST_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS z";

    public static String toString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        return formatter.format(date);
    }

    public static Date toDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toUiString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(UI_REQUEST_DATE_PATTERN);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        return formatter.format(date);
    }

    public static Date fromUIStringToDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(UI_REQUEST_DATE_PATTERN);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Calendar toUiCalendar(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fromUIStringToDate(date));
        return calendar;
    }

    public static boolean compare(String start, String end) {
        return toDate(start).before(toDate(end));
    }

    public static int[] toArray(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(toDate(date));

        return new int[]{calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND),
                calendar.get(Calendar.MILLISECOND)
        };
    }
}
