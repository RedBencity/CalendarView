package com.calendar;


public class CalendarInfo {

    private int year;
    private int mouth;
    private int week;
    private int day;
    private int dayOfTheWeek;

    //-1,0,1
    private int mouthFlag;

    //显示
    private String chinaMonth;
    private String chinaDay;

    public CalendarInfo(int year, int moth, int day) {
        this.year = year;
        this.mouth = moth;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMouth() {
        return mouth;
    }

    public void setMouth(int mouth) {
        this.mouth = mouth;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(int dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public int getMouthFlag() {
        return mouthFlag;
    }

    public void setMouthFlag(int mouthFlag) {
        this.mouthFlag = mouthFlag;
    }

    public String getChinaMonth() {
        return chinaMonth;
    }

    public void setChinaMonth(String chinaMonth) {
        this.chinaMonth = chinaMonth;
    }

    public String getChinaDay() {
        return chinaDay;
    }

    public void setChinaDay(String chinaDay) {
        this.chinaDay = chinaDay;
    }

    public String getDisplayWeek() {
        String s = "";
        switch (dayOfTheWeek) {
            case 1:
                s = "星期日";
                break;
            case 2:
                s = "星期一";
                break;
            case 3:
                s = "星期二";
                break;
            case 4:
                s = "星期三";
                break;
            case 5:
                s = "星期四";
                break;
            case 6:
                s = "星期五";
                break;
            case 7:
                s = "星期六";
                break;

        }
        return s;
    }

    public String ymdStr() {
        return year + "-" + mouth + "-" + day;
    }

    @Override
    public String toString() {
//        String s=year+"/"+mouth+"/"+day+"\t"+getDisplayWeek()+"\t农历"+":"+chinaMonth+"/"+chinaDay;
        String s = year + "/" + mouth + "/" + day;
        return s;
    }
}