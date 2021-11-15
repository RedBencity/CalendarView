package com.calendar;

class GeographyPresenter {
    private static final String TAG = "CalendarPresenter";
    GeographyPresenter() {
    }


    int selectWeatherIcon(int id) {
        //对应数字需要去心知天气网上查找
         int resId;
        if (id <= 3) {
            resId = R.mipmap.weather_sunny;
        } else if (id <= 8) {
            resId = R.mipmap.weather_cloudy;
        } else if (id == 9) {
            resId = R.mipmap.weather_overcast;
        } else if (id <= 12) {
            resId = R.mipmap.weather_heavy_rain;
        } else if (id == 13) {
            resId = R.mipmap.weather_light_rain;
        } else if (id == 14) {
            resId = R.mipmap.weather_moderate_rain;
        } else if (id <= 19) {
            resId = R.mipmap.weather_heavy_rain;
        } else if (id == 20) {
            resId = R.mipmap.weather_sleet;
        } else if (id == 21) {
            resId = R.mipmap.weather_heavy_snow;
        } else if (id == 22) {
            resId = R.mipmap.weather_light_snow;
        } else if (id == 23) {
            resId = R.mipmap.weather_moderate_snow;
        } else if (id <= 25) {
            resId = R.mipmap.weather_heavy_snow;
        } else if (id <= 29) {
            resId = R.mipmap.weather_sandstorm;
        } else if (id == 30) {
            resId = R.mipmap.weather_foggy;
        } else if (id == 31) {
            resId = R.mipmap.weather_haze;
        } else if (id == 32) {
            resId = R.mipmap.weather_windy;
        } else if (id <= 36) {
            resId = R.mipmap.weather_blustery;
        } else {
            resId = R.mipmap.weather_sunny;
        }
        return resId;
    }
}
