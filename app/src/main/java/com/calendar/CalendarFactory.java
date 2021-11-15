package com.calendar;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.calendar.CalendarUtil.getDayOfWeek;
import static com.calendar.CalendarUtil.getWeek;

public class CalendarFactory {

    private static HashMap<String, List<CalendarInfo>> cache = new HashMap<>();

    //获取一月中的集合
    public static List<CalendarInfo> getMonthOfDayList(int y, int m) {

        String key = y + "" + m;
        if (cache.containsKey(key)) {
            List<CalendarInfo> list = cache.get(key);
            if (list == null) {
                cache.remove(key);
            } else {
                return list;
            }
        }

        List<CalendarInfo> list = new ArrayList<>();
        cache.put(key, list);

        //计算出一月第一天是星期几
        int week = getDayOfWeek(y, m, 1);
        int total = CalendarUtil.getDayOfMonth(y, m);

        //根据星期推出前面还有几个显示
        for (int i = week - 1; i > 0; i--) {
            CalendarInfo bean = geCalendarBean(y, m, 1 - i);
            bean.setMouthFlag(-1);
            list.add(bean);
        }

        //获取当月的天数
        for (int i = 0; i < total; i++) {
            CalendarInfo bean = geCalendarBean(y, m, i + 1);
            list.add(bean);
        }

        //为了塞满42个格子，显示多出当月的天数
        for (int i = 0; i < 42 - (week - 1) - total; i++) {
            CalendarInfo bean = geCalendarBean(y, m, total + i + 1);
            bean.setMouthFlag(1);
            list.add(bean);
        }
        return list;
    }

    //获取一月中的集合
    public static List<CalendarInfo> getMonthOfDayList(int y, int m, int day) {

        String key = y + "" + m + " " + day;
        if (cache.containsKey(key)) {
            List<CalendarInfo> list = cache.get(key);
            if (list == null) {
                cache.remove(key);
            } else {
                return list;
            }
        }

        List<CalendarInfo> list = new ArrayList<>();
        cache.put(key, list);
        y = CalendarUtil.getYear(y, m, day);
        m = CalendarUtil.getMonth(y, m, day);

        //计算出一月第一天是星期几
        int week = getDayOfWeek(y, m, 1);
        int total = CalendarUtil.getDayOfMonth(y, m);

        //根据星期推出前面还有几个显示
        for (int i = week - 1; i > 0; i--) {
            CalendarInfo bean = geCalendarBean(y, m, 1 - i);
            bean.setMouthFlag(-1);
            list.add(bean);
        }

        //获取当月的天数
        for (int i = 0; i < total; i++) {
            CalendarInfo bean = geCalendarBean(y, m, i + 1);
            list.add(bean);
        }

        //为了塞满42个格子，显示多出当月的天数
        for (int i = 0; i < 42 - (week - 1) - total; i++) {
            CalendarInfo bean = geCalendarBean(y, m, total + i + 1);
            bean.setMouthFlag(1);
            list.add(bean);
        }
        return list;
    }

    public static CalendarInfo geCalendarBean(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DATE);

        CalendarInfo bean = new CalendarInfo(year, month, day);
        bean.setWeek(getWeek(year, month, day));
        bean.setDayOfTheWeek(getDayOfWeek(year, month, day));
        String[] chinaDate = ChinaDate.getChinaDate(year, month, day);
        bean.setChinaMonth(chinaDate[0]);
        bean.setChinaDay(chinaDate[1]);
        return bean;
    }

    public static void main(String[] args) {
    }


}
