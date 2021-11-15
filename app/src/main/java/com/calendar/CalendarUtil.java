package com.calendar;

import android.annotation.TargetApi;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

@TargetApi(14)
public class CalendarUtil {

    //获取一月的第一天是星期几
    public static int getDayOfWeek(int y, int mouth, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(y, mouth - 1, day);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    //获取一月最大天数
    public static int getDayOfMonth(int year, int mouth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, mouth - 1, 1);
        int dateOfMonth = cal.getActualMaximum(Calendar.DATE);
        return dateOfMonth;
    }

    public static int[] getYMD(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return new int[]{cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE)};
    }

    @Deprecated
    public static int[] getYWD(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Log.d("aaaaaa", cal.get(Calendar.YEAR) + " " + (cal.get(Calendar.MONTH) + 1) + " " + cal.get(Calendar.WEEK_OF_YEAR) + " " + cal.get(Calendar.DATE));
        return new int[]{cal.get(Calendar.YEAR), cal.get(Calendar.WEEK_OF_YEAR), cal.get(Calendar.DATE)};
    }

    @Deprecated
    public static int getMouthByWeek(int year, int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        return calendar.get(Calendar.MONTH) + 1;
    }

    @Deprecated
    public static int getYearByWeek(int year, int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        return calendar.get(Calendar.YEAR);
    }

    @Deprecated
    public static int resetWeek(int year, int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    @Deprecated
    public static int getDayByWeek(int year, int week) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, week);
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getMonth(int year, int mouth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, mouth - 1);
        cal.set(Calendar.DAY_OF_MONTH, 28);
        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getYear(int year, int mouth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, mouth - 1);
        return cal.get(Calendar.YEAR);
    }

    @Deprecated
    public static int getWeek(int year, int mouth, int day) {
        if (day == 31 && (mouth == 4 || mouth == 6 || mouth == 9 || mouth == 11)) {
            day = 30;
        } else if (day == 31 && mouth == 2) {
            day = 28;
        }
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, mouth - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    public static int adjustDay(int mouth, int day) {
        if (day == 31 && (mouth == 4 || mouth == 6 || mouth == 9 || mouth == 11)) {
            day = 30;
        } else if (day == 31 && mouth == 2) {
            day = 28;
        }
        return day;
    }

    public static int getYear(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getDay(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
}
