package com.calendar;

import android.view.View;
import android.view.ViewGroup;

public interface CalendarAdapter {
    View getView(View convertView, ViewGroup parentView, CalendarInfo bean, boolean isFullScreen);

    void setAnimationWhenItemViewScroll(View itemView, float scrollDistanceScale);
}
