package com.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CalendarDateView calendarDateView;
    private GeographyPresenter geographyPresenter;
    private static String TAG = "MainActivity";
    private HashMap<String, Integer> holidayMaps;
    private ListHeaderView headerView;
    private LinearLayout woEmptyLL;
    private ExpandableListView listView;
    private CalendarLayout calendarLayout;
    private RelativeLayout emptyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendarDateView = findViewById(R.id.calendar_date_view);
        geographyPresenter = new GeographyPresenter();
        holidayMaps = new HashMap<>();
        calendarLayout = findViewById(R.id.calendar_layout);

        listView = findViewById(R.id.list);
        headerView = new ListHeaderView(this);
        headerView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.calendar_china_date)));
        headerView.setBackgroundColor(getResources().getColor(R.color.calendar_list_header_bg));
        listView.addHeaderView(headerView);

        emptyLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.view_listview_empty, null);
        woEmptyLL = emptyLayout.findViewById(R.id.view_empty_ll);
        initCalendar();
    }

    private void initCalendar() {
        calendarDateView.setAdapter(new CalendarAdapter() {
            private int blue = getResources().getColor(R.color.calendar_blue);
            private int grey = getResources().getColor(R.color.calendar_grey);
            private int black = getResources().getColor(R.color.calendar_black);
            private int green = getResources().getColor(R.color.calendar_green);

            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarInfo bean, boolean isFullScreen) {
                ViewHolder viewHolder;
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_calendar_view, null);
                    viewHolder.chinaText = convertView.findViewById(R.id.china_day);
                    viewHolder.text = convertView.findViewById(R.id.day);
                    viewHolder.weatherIv = convertView.findViewById(R.id.calendar_weather_image);
                    viewHolder.redSpotView = convertView.findViewById(R.id.red_spot);
                    viewHolder.one = convertView.findViewById(R.id.one);
                    viewHolder.two = convertView.findViewById(R.id.two);
                    viewHolder.three = convertView.findViewById(R.id.three);
                    viewHolder.holiday = convertView.findViewById(R.id.holiday);
                    viewHolder.chinaText.setTextCenter(true);
                    viewHolder.text.setTextCenter(true);
                    viewHolder.text.setTextSize((int) getResources().getDimension(R.dimen.calendar_date));
                    viewHolder.chinaText.setTextSize((int) getResources().getDimension(R.dimen.calendar_china_date));
                    viewHolder.one.setTextSize((int) getResources().getDimension(R.dimen.calendar_text));
                    viewHolder.two.setTextSize((int) getResources().getDimension(R.dimen.calendar_text));
                    viewHolder.three.setTextSize((int) getResources().getDimension(R.dimen.calendar_text));
                    viewHolder.one.setTextColor(getResources().getColor(R.color.black));
                    viewHolder.two.setTextColor(getResources().getColor(R.color.black));
                    viewHolder.three.setTextColor(getResources().getColor(R.color.black));
                    viewHolder.one.setBackgroundColor(blue);
                    viewHolder.two.setBackgroundColor(blue);
                    viewHolder.three.setBackgroundColor(blue);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                String day = bean.getYear() + "-"
                        + (bean.getMouth() < 10 ? "0" + bean.getMouth() : bean.getMouth()) + "-"
                        + (bean.getDay() < 10 ? "0" + bean.getDay() : bean.getDay());
                if (bean.getDay() % 7 != 1) {
                    viewHolder.weatherIv.setBitmap(geographyPresenter.selectWeatherIcon(1));
                } else {
                    viewHolder.weatherIv.setBitmap(-1);
                }

                viewHolder.chinaText.setText(bean.getChinaDay());
                viewHolder.text.setText("" + bean.getDay());
                viewHolder.one.setText("");
                viewHolder.two.setText("");
                viewHolder.three.setText("");
                viewHolder.redSpotView.setDraw(false);
                if (bean.getMouthFlag() != 0) {
                    viewHolder.text.setTextColor(grey);
                    viewHolder.chinaText.setTextColor(grey);

                } else {
                    viewHolder.text.setTextColor(black);
                    viewHolder.chinaText.setTextColor(black);
                }

                if ((bean.getDayOfTheWeek() == 1 || bean.getDayOfTheWeek() == 7) && bean.getMouthFlag() == 0) {
                    viewHolder.text.setTextColor(green);
                    viewHolder.chinaText.setTextColor(green);
                }

                List<String> stringList = new ArrayList<>();
                if (stringList != null) {
                    ArrayList<String> names = new ArrayList<>();
                    names.add("测试文本测试文本测试文本");
                    for (int i = 0; i < stringList.size(); i++) {
                        names.add("test test");
                    }
                    if (names.size() > 0) {
                        if (names.size() >= 1 && names.get(0) != null && !TextUtils.isEmpty(names.get(0))) {
                            viewHolder.one.setText(names.get(0));
                        }
                        if (names.size() >= 2 && names.get(1) != null && !TextUtils.isEmpty(names.get(1))) {
                            viewHolder.two.setText(names.get(1));
                        }
                        if (names.size() >= 3 && names.get(2) != null && !TextUtils.isEmpty(names.get(2))) {
                            viewHolder.three.setText(names.get(2));
                        }
                    }
                    if (stringList.size() > 0) {
                        viewHolder.redSpotView.setDraw(true);
                    }

                    if (stringList.size() <= 2) {
                        viewHolder.redSpotView.setMaxAlpha(20);
                    } else if (stringList.size() > 2 && stringList.size() <= 5) {
                        viewHolder.redSpotView.setMaxAlpha(30);
                    } else if (stringList.size() > 5 && stringList.size() <= 10) {
                        viewHolder.redSpotView.setMaxAlpha(40);
                    } else if (stringList.size() > 10 && stringList.size() <= 15) {
                        viewHolder.redSpotView.setMaxAlpha(50);
                    } else if (stringList.size() > 15 && stringList.size() <= 20) {
                        viewHolder.redSpotView.setMaxAlpha(60);
                    } else if (stringList.size() > 20 && stringList.size() <= 30) {
                        viewHolder.redSpotView.setMaxAlpha(70);
                    } else if (stringList.size() > 30 && stringList.size() <= 50) {
                        viewHolder.redSpotView.setMaxAlpha(80);
                    } else if (stringList.size() > 50) {
                        viewHolder.redSpotView.setMaxAlpha(100);
                    }
                }

                if (!isFullScreen) {
                    viewHolder.one.changeAlpha(0);
                    viewHolder.two.changeAlpha(0);
                    viewHolder.three.changeAlpha(0);
                    viewHolder.chinaText.changeAlpha(0);
                    viewHolder.weatherIv.changeAlpha(0);
                } else {
                    viewHolder.one.changeAlpha(1);
                    viewHolder.two.changeAlpha(1);
                    viewHolder.three.changeAlpha(1);
                    viewHolder.chinaText.changeAlpha(1);
                    viewHolder.redSpotView.changeAlpha(0);
                    viewHolder.weatherIv.changeAlpha(1);
                }

                String date = bean.getYear() + "/" + bean.getMouth() + "/" + bean.getDay();
                if (holidayMaps.get(date) == null) {
                    viewHolder.holiday.setVisibility(View.GONE);
                    return convertView;
                }
                if (holidayMaps.get(date) == 1 || holidayMaps.get(date) == 2) {
                    viewHolder.holiday.setVisibility(View.VISIBLE);
                    if (bean.getMouthFlag() != 0) {
                        viewHolder.text.setTextColor(grey);
                        viewHolder.chinaText.setTextColor(grey);
                    } else {
                        viewHolder.text.setTextColor(green);
                        viewHolder.chinaText.setTextColor(green);
                    }
                } else if (holidayMaps.get(date) == 0) {
                    viewHolder.holiday.setVisibility(View.GONE);
                    if (bean.getMouthFlag() != 0) {
                        viewHolder.text.setTextColor(grey);
                        viewHolder.chinaText.setTextColor(grey);
                    } else {
                        viewHolder.text.setTextColor(black);
                        viewHolder.chinaText.setTextColor(black);
                    }
                } else {
                    viewHolder.holiday.setVisibility(View.GONE);
                }
                return convertView;
            }

            @Override
            public void setAnimationWhenItemViewScroll(View itemView, float scrollDistanceScale) {
                ViewHolder viewHolder = (ViewHolder) itemView.getTag();
                Log.d(TAG, "scrollDistanceScale: " + scrollDistanceScale);
                float alphaScale = 1 - scrollDistanceScale;
                viewHolder.one.changeAlpha(alphaScale);
                viewHolder.two.changeAlpha(alphaScale);
                viewHolder.three.changeAlpha(alphaScale);
                viewHolder.chinaText.changeAlpha(alphaScale);
                viewHolder.redSpotView.changeAlpha(scrollDistanceScale);
                viewHolder.weatherIv.changeAlpha(alphaScale);
            }
        });

        calendarDateView.setOnPageChangedListener(new CalendarDateView.OnPageChangedListener() {
            @Override
            public void delayPageChanged(View view, int position, final CalendarInfo bean,
                                         boolean isFullScreen) {
            }

            @Override
            public void instantPageChanged(CalendarInfo bean) {
            }
        });

        calendarDateView.setViewChangeListener(new CalendarDateView.ViewChangeListener() {
            private ViewHolder viewHolder;

            private void init(View itemView) {
                viewHolder = (ViewHolder) itemView.getTag();
            }

            @Override
            public void fold(View itemView) {
                headerView.setLineStyle(ListHeaderView.LineStyle.DOWN);
                init(itemView);
                viewHolder.one.changeAlpha(0);
                viewHolder.two.changeAlpha(0);
                viewHolder.three.changeAlpha(0);
                viewHolder.chinaText.changeAlpha(0);
                viewHolder.redSpotView.changeAlpha(1);
                viewHolder.weatherIv.changeAlpha(0);
                woEmptyLL.offsetTopAndBottom((int) (getResources().getDimension(R.dimen.h130) - (woEmptyLL.getTop() - listView.getTop())));
            }

            @Override
            public void open(View itemView) {
                headerView.setLineStyle(ListHeaderView.LineStyle.MIDDLE);
                init(itemView);
                viewHolder.one.changeAlpha(0);
                viewHolder.two.changeAlpha(0);
                viewHolder.three.changeAlpha(0);
                viewHolder.chinaText.changeAlpha(0);
                viewHolder.redSpotView.changeAlpha(1);
                viewHolder.weatherIv.changeAlpha(0);
                woEmptyLL.offsetTopAndBottom(-(int) getResources().getDimension(R.dimen.h100) - (woEmptyLL.getTop() - listView.getTop()));
            }

            @Override
            public void fullScreen(View itemView) {
                headerView.setLineStyle(ListHeaderView.LineStyle.UP);
                init(itemView);
                viewHolder.one.changeAlpha(1);
                viewHolder.two.changeAlpha(1);
                viewHolder.three.changeAlpha(1);
                viewHolder.chinaText.changeAlpha(1);
                viewHolder.redSpotView.changeAlpha(0);
                viewHolder.weatherIv.changeAlpha(1);

            }
        });

        calendarDateView.setOnItemClickListener((view, position, bean, isFullScreen) -> {
        });
        final int maxDistance = -(int) getResources().getDimension(R.dimen.h80);
        final int mixDistance = 0;
        calendarLayout.setScrollListener(new CalendarLayout.ScrollListener() {
            private int scrollDistanceBetweenFO = 0;

            @Override
            public void scrollListener(int distance) {
                distance = -distance;
                if (scrollDistanceBetweenFO + distance <= maxDistance) {
                    distance = maxDistance - scrollDistanceBetweenFO;
                } else if (scrollDistanceBetweenFO + distance >= mixDistance) {
                    distance = mixDistance - scrollDistanceBetweenFO;
                }
                scrollDistanceBetweenFO += distance;
                Log.d(TAG, "scrollDistanceBetweenFO: " + scrollDistanceBetweenFO + " listView.getTop(): " + listView.getTop());
                woEmptyLL.offsetTopAndBottom(distance);
            }
        });
    }

    static class ViewHolder {
        CalendarTextView one;
        CalendarTextView two;
        CalendarTextView three;
        RedSpotView redSpotView;
        CalendarTextView chinaText;
        CalendarTextView text;
        ImageView holiday;
        CalendarImageView weatherIv;
    }
}