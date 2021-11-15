package com.calendar;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.Date;
import java.util.LinkedList;

public class CalendarDateView extends ViewPager {

    private static final String TAG = "CalendarDateView";
    final int[] ymd = CalendarUtil.getYMD(new Date());
    //    final int[] ywd = CalendarUtil.getYWD(new Date());
    SparseArray<CalendarView> calendarViewSparseArray = new SparseArray<>();
    ViewChangeListener viewChangeListener;
    private CalendarTopViewChangeListener calendarTopViewChangeListener;
    private CalendarView.OnItemClickListener onItemClickListener;
    private LinkedList<CalendarView> calendarViewList = new LinkedList<>();
    //TODO  2017/4/14
    private int row = 6;
    private CalendarAdapter calendarAdapter;
    private int calendarOpenItemHeight = 0;
    private volatile boolean isHorizontalScrolled = false;
    private OnPageChangedListener onPageChangedListener;
    private int positionCache;

    public CalendarDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setAdapter(CalendarAdapter calendarAdapter) {
        this.calendarAdapter = calendarAdapter;
        setCurrentItem(2500, false);
        getAdapter().notifyDataSetChanged();
    }

    public void setOnItemClickListener(CalendarView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnPageChangedListener(OnPageChangedListener onPageChangedListener) {
        this.onPageChangedListener = onPageChangedListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int calendarHeight = 0;
        if (getAdapter() != null) {
            CalendarView view = (CalendarView) getChildAt(0);
            if (view != null) {
                calendarHeight = view.getMeasuredHeight();
                calendarOpenItemHeight = view.getOpenItemHeight();
            }
        }
        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(calendarHeight, MeasureSpec.EXACTLY));
    }

    private void init() {

        setAdapter(new PagerAdapter() {
            private boolean firstInit = true;

            @Override
            public int getCount() {
                return 5000;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                Log.d(TAG, "setPrimaryItem position ; " + position);
                if (firstInit) {
                    initCalendarData(calendarViewSparseArray.get(position));
                    firstInit = false;
                }
                refreshLeftAndRightCalendarData(position);
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                Log.d(TAG, "instantiateItem position ; " + position);
                CalendarView calendarView;

                if (!calendarViewList.isEmpty()) {
                    calendarView = calendarViewList.removeFirst();
                    calendarView.setFullScreen(calendarTopViewChangeListener.isFullScreen());
                } else {
                    calendarView = new CalendarView(container.getContext(), row, calendarTopViewChangeListener.isFullScreen());
                }
                calendarView.setOnItemClickListener(onItemClickListener);
                calendarView.setAdapter(calendarAdapter);

                container.addView(calendarView);
                calendarViewSparseArray.put(position, calendarView);
                return calendarView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                Log.d(TAG, "destroyItem position ; " + position);
                container.removeView((View) object);
                calendarViewList.addLast((CalendarView) object);
                calendarViewSparseArray.remove(position);
            }

        });

        setOnPageChangeListener(new SimpleOnPageChangeListener() {
            private int cachePosition = 0;

            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {
                isHorizontalScrolled = !(positionOffset == 0 && positionOffsetPixels == 0);
                Log.d(TAG, "onPageScrolled " + isHorizontalScrolled + " position " + position);
                final CalendarView view = calendarViewSparseArray.get(position);

                if (!isHorizontalScrolled && cachePosition != position) {
                    cachePosition = position;
                    if (onPageChangedListener != null) {
                        onPageChangedListener.instantPageChanged(view.getSelectCalendarInfo());
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "onPageScrolled " + isHorizontalScrolled + " position " + position + " bean " + view.getSelectCalendarInfo().toString());
                            if (isHorizontalScrolled) {
                                return;
                            }
                            if (onPageChangedListener != null) {
                                onPageChangedListener.delayPageChanged(view.getSelectChildView(), view.getSelectPosition(),
                                        view.getSelectCalendarInfo(), calendarTopViewChangeListener.isFullScreen());
                            }
                        }
                    }, 400);
                }
            }
        });
    }

    public void setToday() {
        initCalendarData(calendarViewSparseArray.get(getCurrentItem()));
        refreshLeftAndRightCalendarData(getCurrentItem());
    }

    private void initCalendarData(CalendarView calendarView) {
        if (calendarTopViewChangeListener.isFold()) {
            calendarView.setData(CalendarFactory.getMonthOfDayList(ymd[0], ymd[1]),
                    CalendarUtil.getYear(ymd[0], ymd[1]),
                    CalendarUtil.getMonth(ymd[0], ymd[1]),
                    ymd[2],
                    calendarTopViewChangeListener.isFullScreen());
        } else {
            calendarView.setData(CalendarFactory.getMonthOfDayList(ymd[0], ymd[1]),
                    CalendarUtil.getYear(ymd[0], ymd[1]),
                    CalendarUtil.getMonth(ymd[0], ymd[1]),
                    ymd[2],
                    calendarTopViewChangeListener.isFullScreen());
        }
    }

    int[] getCurrentSelectPosition() {
        CalendarView view = calendarViewSparseArray.get(getCurrentItem());
        if (view == null) {
            view = (CalendarView) getChildAt(0);
        }
        if (view != null) {
            return view.getSelectItemRect();
        }
        return new int[4];
    }

    public boolean isHorizontalScrolled() {
        return isHorizontalScrolled;
    }

    int getOpenItemHeight() {
        return calendarOpenItemHeight;
    }

    public void setCalendarTopViewChangeListener(CalendarTopViewChangeListener listener) {
        calendarTopViewChangeListener = listener;
    }

    void viewChangeWithScrollDistance(int distance) {
        Log.d(TAG, "itemViewChangeWithScrollDistance " + calendarViewSparseArray.size());
        calendarViewSparseArray.get(getCurrentItem()).itemViewChangeWithScrollDistance(distance);
    }

    void refreshLeftAndRightCalendarData(int position) {
        Log.d(TAG, "refreshLeftAndRightCalendarData " + calendarTopViewChangeListener.isFullScreen() + " position " + position);
        CalendarView currentCalendarView = calendarViewSparseArray.get(getCurrentItem());
        currentCalendarView.setFullScreen(calendarTopViewChangeListener.isFullScreen());

        if (position != -1) {
            if (position == positionCache) {
                return;
            } else {
                positionCache = position;
            }
        }
        CalendarView previousCalendarView = calendarViewSparseArray.get(getCurrentItem() - 1);
        CalendarView nextCalendarView = calendarViewSparseArray.get(getCurrentItem() + 1);

        if (previousCalendarView != null) {
            previousCalendarView.setDay(CalendarUtil.adjustDay(currentCalendarView.getMouth() - 1, currentCalendarView.getDay()));
            changeCalendarType(previousCalendarView);
        }
        if (nextCalendarView != null) {
            nextCalendarView.setDay(CalendarUtil.adjustDay(currentCalendarView.getMouth() + 1, currentCalendarView.getDay()));
            changeCalendarType(nextCalendarView);
        }

        if (previousCalendarView != null) {
            if (calendarTopViewChangeListener.isFold()) {
                previousCalendarView.setData(CalendarFactory.getMonthOfDayList(currentCalendarView.getYear(), currentCalendarView.getMouth(), currentCalendarView.getDay() - 7),
                        CalendarUtil.getYear(currentCalendarView.getYear(), currentCalendarView.getMouth(), currentCalendarView.getDay() - 7),
                        CalendarUtil.getMonth(currentCalendarView.getYear(), currentCalendarView.getMouth(), currentCalendarView.getDay() - 7),
                        CalendarUtil.getDay(currentCalendarView.getYear(), currentCalendarView.getMouth(), currentCalendarView.getDay() - 7),
                        calendarTopViewChangeListener.isFullScreen());
            } else {
                previousCalendarView.setData(CalendarFactory.getMonthOfDayList(currentCalendarView.getYear(), currentCalendarView.getMouth() - 1),
                        CalendarUtil.getYear(currentCalendarView.getYear(), currentCalendarView.getMouth() - 1),
                        CalendarUtil.getMonth(currentCalendarView.getYear(), currentCalendarView.getMouth() - 1),
                        CalendarUtil.adjustDay(currentCalendarView.getMouth() - 1, currentCalendarView.getDay()),
                        calendarTopViewChangeListener.isFullScreen());
            }
        }
        if (nextCalendarView != null) {
            if (calendarTopViewChangeListener.isFold()) {
                nextCalendarView.setData(CalendarFactory.getMonthOfDayList(currentCalendarView.getYear(), currentCalendarView.getMouth(), currentCalendarView.getDay() + 7),
                        CalendarUtil.getYear(currentCalendarView.getYear(), currentCalendarView.getMouth(), currentCalendarView.getDay() + 7),
                        CalendarUtil.getMonth(currentCalendarView.getYear(), currentCalendarView.getMouth(), currentCalendarView.getDay() + 7),
                        CalendarUtil.getDay(currentCalendarView.getYear(), currentCalendarView.getMouth(), currentCalendarView.getDay() + 7),
                        calendarTopViewChangeListener.isFullScreen());
            } else {
                nextCalendarView.setData(CalendarFactory.getMonthOfDayList(currentCalendarView.getYear(), currentCalendarView.getMouth() + 1),
                        CalendarUtil.getYear(currentCalendarView.getYear(), currentCalendarView.getMouth() + 1),
                        CalendarUtil.getMonth(currentCalendarView.getYear(), currentCalendarView.getMouth() + 1),
                        CalendarUtil.adjustDay(currentCalendarView.getMouth() + 1, currentCalendarView.getDay()),
                        calendarTopViewChangeListener.isFullScreen());
            }
        }
    }

    private void changeCalendarType(CalendarView calendarView) {
        if (calendarTopViewChangeListener.isFold()) {
            if (viewChangeListener != null) {
                for (int i = 0; i < calendarView.getChildCount(); i++) {
                    viewChangeListener.fold(calendarView.getChildAt(i));
                }
            }
        } else if (calendarTopViewChangeListener.isOpen()) {
            calendarView.setFullScreen(false);
            calendarView.changeLayout();
            if (viewChangeListener != null) {
                for (int i = 0; i < calendarView.getChildCount(); i++) {
                    viewChangeListener.open(calendarView.getChildAt(i));
                }
            }
        }
        if (calendarTopViewChangeListener.isFullScreen()) {
            calendarView.setFullScreen(true);
            calendarView.changeLayout();
            if (viewChangeListener != null) {
                for (int i = 0; i < calendarView.getChildCount(); i++) {
                    viewChangeListener.fullScreen(calendarView.getChildAt(i));
                }
            }
        }
    }

    public void notifyDataChanged() {
        CalendarView previousCalendarView = calendarViewSparseArray.get(getCurrentItem() - 1);
        CalendarView currentCalendarView = calendarViewSparseArray.get(getCurrentItem());
        CalendarView nextCalendarView = calendarViewSparseArray.get(getCurrentItem() + 1);
        if (currentCalendarView != null)
            currentCalendarView.freshData();
        if (previousCalendarView != null)
            previousCalendarView.freshData();
        if (nextCalendarView != null)
            nextCalendarView.freshData();
    }

    public void setViewChangeListener(ViewChangeListener viewChangeListener) {
        this.viewChangeListener = viewChangeListener;
    }

    public interface ViewChangeListener {
        void fold(View view);

        void open(View view);

        void fullScreen(View view);
    }

    public interface OnPageChangedListener {
        void delayPageChanged(View view, int position, CalendarInfo bean, boolean isFullScreen);

        void instantPageChanged(CalendarInfo bean);
    }


}
