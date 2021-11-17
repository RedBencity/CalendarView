package com.calendar;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CalendarView extends ViewGroup {

    private static final String TAG = "CalendarView";
    private int selectPosition = -1;
    private CalendarAdapter adapter;
    private int row = 6;
    private final int column = 7;
    private int openItemHeight;
    private List<CalendarInfo> calendarInfoList;
    private boolean isFullScreen = false;
    private int mouth;
    private int year;
    private int day;
    private int differenceItemValue;
    private OnItemClickListener onItemClickListener;


    public CalendarView(final Context context, int row, boolean isFullScreen) {
        super(context);
        this.row = row;
        this.isFullScreen = isFullScreen;
        Log.d(TAG, "CalendarView init three params");
    }

    public CalendarView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        Log.d(TAG, "CalendarView init");
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    int getOpenItemHeight() {
        return openItemHeight;
    }

    void setAdapter(CalendarAdapter adapter) {
        this.adapter = adapter;
    }

    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
//        Log.d(TAG, "setFullScreen fullScreen : " + fullScreen);
    }

    void setData(final List<CalendarInfo> calendarInfoList, final int year, final int mouth, final int day, final boolean isFullScreen) {
        Log.d(TAG, "setData  ; year : " + year + " mouth : " + mouth  + " day : " + day);
        this.year = year;
        this.mouth = mouth;
        this.day = day;
        this.calendarInfoList = calendarInfoList;
        this.isFullScreen = isFullScreen;

        long begin = System.currentTimeMillis();
        selectPosition = -1;
        setItem(calendarInfoList, mouth, day, isFullScreen);
        Log.d(TAG, "setData time :" + (System.currentTimeMillis() - begin));
    }

    void freshData() {
        if (calendarInfoList != null) {
            setItem(calendarInfoList, mouth, day, isFullScreen);
        }
    }

    public int getYear() {
        return year;
    }

    public int getMouth() {
        return mouth;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    private void setItem(List<CalendarInfo> calendarInfoList, int mouth, int day, boolean isFullScreen) {
//        Log.d(TAG, "setItem  ; " + " mouth : " + mouth + " day : " + day);
        if (adapter == null) {
            throw new RuntimeException("adapter is null,please set adapter");
        }
//        Log.d(TAG, "setItem  ; " + " ---------------------- ");
        for (int i = 0; i < calendarInfoList.size(); i++) {
            CalendarInfo calendarInfo = calendarInfoList.get(i);
            View view = getChildAt(i);
            final View childView = adapter.getView(view, this, calendarInfo, isFullScreen);
            if (view == null || view != childView) {
//                Log.d(TAG, "setItem  ; " + " addViewInLayout ");
                addViewInLayout(childView, i, childView.getLayoutParams(), true);
            }
//            Log.d(TAG, "mouth : " + mouth + " day : " + day + " calendarInfo.getMouth() : " + calendarInfo.getMouth());
            if (selectPosition == -1 && calendarInfo.getMouth() == mouth && calendarInfo.getDay() == day) {
                selectPosition = i;
//                Log.d(TAG, "setItem  ; " + " selectPosition : " + selectPosition);
            }

            childView.setSelected(selectPosition == i);
            setItemClick(childView, i, calendarInfo);
        }
//        Log.d(TAG, "setItem  ; " + " ---------------------- ");
        if (selectPosition == -1) {
            for (int i = 0; i < calendarInfoList.size(); i++) {
                CalendarInfo calendarInfo = calendarInfoList.get(i);
                View view = getChildAt(i);
                final View childView = adapter.getView(view, this, calendarInfo, isFullScreen);
                if (calendarInfo.getMouth() == mouth && calendarInfo.getDay() == 1) {
                    selectPosition = i;
                    childView.setSelected(true);
                    setItemClick(childView, i, calendarInfo);
                }
            }
        }
    }

    View getSelectChildView() {
        return getChildAt(selectPosition);
    }

    int getSelectPosition() {
        return selectPosition;
    }

    CalendarInfo getSelectCalendarInfo() {
        return calendarInfoList.get(selectPosition);
    }

    void printDate() {
        Log.d(TAG, " year : " + year + " mouth : " + mouth  + " day : " + day);
    }

    void setItemClick(final View view, final int position, final CalendarInfo bean) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectPosition != -1) {
                    getChildAt(selectPosition).setSelected(false);
                    getChildAt(position).setSelected(true);
                }
                selectPosition = position;
                Log.d(TAG, " year : " + bean.getYear() + " mouth : " + bean.getMouth() + " week : " + bean.getWeek() + " day : " + bean.getDay());
//                year = bean.getYear();
//                mouth = bean.getMouth();
//                week = bean.getWeek();
                day = bean.getDay();

                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, position, bean, isFullScreen);
                }
            }
        });
    }

    int[] getSelectItemRect() {
        Rect rect = new Rect();
        try {
            getChildAt(selectPosition).getHitRect(rect);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new int[]{rect.left, rect.top, rect.right, rect.top};
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY));
        int fullScreenHeight = MeasureSpec.getSize(MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.EXACTLY));
        //FIXME ListView 的HeaderView高18dp扣去阴影空白为15dp           +0002437
        fullScreenHeight -= getResources().getDisplayMetrics().density * 15;
//        Log.d(TAG, "onMeasure" + fullScreenHeight);
        int fullScreenItemHeight = fullScreenHeight / row;
        int itemWidth = parentWidth / column;
        openItemHeight = (int) (getResources().getDimension(R.dimen.calendar_item_view_height));

        differenceItemValue = fullScreenItemHeight - openItemHeight;

        setMeasuredDimension(parentWidth, fullScreenHeight);

        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.measure(MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(fullScreenItemHeight, MeasureSpec.EXACTLY));
        }

//        Log.i(TAG, "onMeasure() called with: openItemHeight = [" + fullScreenItemHeight + "], itemWidth = [" + itemWidth + "]");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout" + " isFullScreen : " + isFullScreen);
        View view;
        for (int i = 0; i < getChildCount(); i++) {
            view = getChildAt(i);
            int cc = i % column;
            int cr = i / column;
            int itemWidth = view.getMeasuredWidth();
            int itemHeight = view.getMeasuredHeight();
            if (i < column) {
                l = cc * itemWidth;
                t = cr * itemHeight;
                r = l + itemWidth;
                b = t + itemHeight;
            } else {
                l = cc * itemWidth;
                if (isFullScreen) {
                    t = cr * itemHeight;
                } else {
                    t = cr * (itemHeight - differenceItemValue);
                }
                r = l + itemWidth;
                b = t + itemHeight;
            }
            view.layout(l, t, r, b);
        }
    }

    void itemViewChangeWithScrollDistance(int distance) {
        distance = calculateScrollDistance(getChildAt(column).getTop() - getChildAt(0).getBottom(), distance, -differenceItemValue, 0);
//        System.out.println("move distance " + distance + " top " + getChildAt(column).getTop() + " bottom " + getChildAt(0).getBottom());
        for (int i = column; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.offsetTopAndBottom(distance * (i / column));
        }

        float itemScrollDistanceScale = (float) (-(getChildAt(column).getTop() - getChildAt(0).getBottom()) * 1.0 / differenceItemValue);
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            adapter.setAnimationWhenItemViewScroll(childView, itemScrollDistanceScale);
        }
    }

    void changeLayout() {
        View view;
        int l, t, r, b;
        for (int i = 0; i < getChildCount(); i++) {
            view = getChildAt(i);
            int cc = i % column;
            int cr = i / column;
            int itemWidth = view.getMeasuredWidth();
            int itemHeight = view.getMeasuredHeight();
            if (i < column) {
                l = cc * itemWidth;
                t = cr * itemHeight;
                r = l + itemWidth;
                b = t + itemHeight;
            } else {
                l = cc * itemWidth;
                if (isFullScreen) {
                    t = cr * itemHeight;
                } else {
                    t = cr * (itemHeight - differenceItemValue);
                }
                r = l + itemWidth;
                b = t + itemHeight;
            }
            view.layout(l, t, r, b);
        }
    }

    private int calculateScrollDistance(int top, int dY, int minValue, int maxValue) {
        if (top + dY < minValue) {
            return minValue - top;
        }

        if (top + dY > maxValue) {
            return maxValue - top;
        }
        return dY;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, CalendarInfo bean, boolean isFullScreen);
    }

}
