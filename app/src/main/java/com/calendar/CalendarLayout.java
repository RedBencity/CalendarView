package com.calendar;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import androidx.core.widget.ScrollerCompat;
import static com.calendar.CalendarLayout.OpenType.FOLD;
import static com.calendar.CalendarLayout.OpenType.FULL_SCREEN;
import static com.calendar.CalendarLayout.OpenType.OPEN;

public class CalendarLayout extends FrameLayout {

    private static final String TAG = "CalendarLayout";
    private static final Interpolator sInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };
    int oldY = 0;
    private CalendarDateView calendarDateView;
    private ViewGroup view2;
    private OpenType openType = OpenType.FOLD;
    private boolean isSliding = false;
    private int openHeight;
    private int foldHeight;
    private int bottomViewDistanceFromTopViewHeight;
    private int maxOpenDistance;
    private int maxFullScreenDistance;
    private int fullScreenHeight;
    private ScrollerCompat mScroller;
    private float oY, oX;
    private boolean isClickBottomViewArea = false;
    private ScrollDirection scrollDirection = ScrollDirection.NO;

    public CalendarLayout(Context context) {
        super(context);
        init();
    }

    public CalendarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mScroller = ScrollerCompat.create(getContext(), sInterpolator);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        calendarDateView = (CalendarDateView) getChildAt(0);
        view2 = (ViewGroup) getChildAt(1);

        this.calendarDateView.setCalendarTopViewChangeListener(new CalendarTopViewChangeListener() {

            @Override
            public boolean isFullScreen() {
                return openType == FULL_SCREEN;
            }

            @Override
            public boolean isFold() {
                return openType == FOLD;
            }

            @Override
            public boolean isOpen() {
                return openType == OPEN;
            }

        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        fullScreenHeight = MeasureSpec.getSize(heightMeasureSpec);
        //FIXME ListView 的HeaderView高18dp扣去阴影空白为15dp  +0002437
        fullScreenHeight -= getResources().getDisplayMetrics().density * 15;
        Log.d(TAG, "onMeasure " + fullScreenHeight + "  " + openType.getName());
        foldHeight = calendarDateView.getOpenItemHeight();
        openHeight = calendarDateView.getOpenItemHeight() * 6;
        maxOpenDistance = openHeight - foldHeight;
        maxFullScreenDistance = fullScreenHeight - openHeight;
        view2.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec)
                - calendarDateView.getOpenItemHeight(), MeasureSpec.EXACTLY));

        switch (openType) {
            case FOLD:
                bottomViewDistanceFromTopViewHeight = foldHeight;
                break;
            case OPEN:
                bottomViewDistanceFromTopViewHeight = openHeight;
                break;
            case FULL_SCREEN:
                bottomViewDistanceFromTopViewHeight = fullScreenHeight;
            default:
                break;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout" + "  " + openType.getName());
        super.onLayout(changed, left, top, right, bottom);
        view2.offsetTopAndBottom(bottomViewDistanceFromTopViewHeight);
        int[] selectRct = getSelectItemRect();
        if (openType == FOLD) {
            calendarDateView.offsetTopAndBottom(-selectRct[1]);
            if (layoutListener != null) {
                layoutListener.fold();
            }
        } else if (openType == OPEN) {
            calendarDateView.offsetTopAndBottom(0);
            if (layoutListener != null) {
                layoutListener.open();
            }
        } else if (openType == FULL_SCREEN) {
            calendarDateView.offsetTopAndBottom(0);
            if (layoutListener != null) {
                layoutListener.fullScreen();
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isFlag = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oY = ev.getY();
                oX = ev.getX();
                isClickBottomViewArea = isClickViewArea(view2, ev);

                freshOpenType();
                Log.d(TAG, "onInterceptTouchEvent ACTION_DOWN " + " open type : " + openType.getName()
                        + ", top " + view2.getTop() + ", open height " + openHeight + ", full screen height " + fullScreenHeight);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onInterceptTouchEvent ACTION_MOVE ");
                freshOpenType();

                float y = ev.getY();
                float x = ev.getX();

                float xOffset = x - oX;
                float yOffset = y - oY;

                if (Math.abs(yOffset) > 5 && Math.abs(yOffset) > Math.abs(xOffset)) {
                    isFlag = true;
                    if (yOffset > 0) {
                        scrollDirection = ScrollDirection.DOWN;
                    } else if (yOffset < 0) {
                        scrollDirection = ScrollDirection.UP;
                    } else {
                        scrollDirection = ScrollDirection.NO;
                    }
                    if (isClickBottomViewArea) {
                        boolean isBottomViewScroll = isBottomViewScroll(view2);
                        oX = x;
                        oY = y;

                        if (yOffset > 0) {
                            if (openType == FULL_SCREEN) {
                                return false;
                            } else if (openType == OPEN) {
                                return true;
                            } else if (openType == FOLD) {
                                return !isBottomViewScroll;
                            }
                        } else {
                            if (openType == FULL_SCREEN) {
                                return true;
                            } else if (openType == OPEN) {
                                return true;
                            } else if (openType == FOLD) {
                                return false;
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                scrollDirection = ScrollDirection.NO;
                break;
        }
        return isSliding || isFlag;
    }

    private boolean isBottomViewScroll(ViewGroup view2) {
        View firstChildView = view2.getChildAt(0);
        if (firstChildView == null) {
            return false;
        }
        if (view2 instanceof ListView) {
            AbsListView list = (AbsListView) view2;
            if (firstChildView.getTop() != 0) {
                return true;
            } else {
                if (list.getPositionForView(firstChildView) != 0) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isClickViewArea(View view, MotionEvent ev) {
        Rect rect = new Rect();
        view.getHitRect(rect);
        boolean isClick = rect.contains((int) ev.getX(), (int) ev.getY());
//        Log.d(TAG, "isClickViewArea() called with: isClick = [" + isClick + "]");
        return isClick;
    }

    private void freshOpenType() {
        Log.d(TAG, "freshOpenType view.getTop2 : " + view2.getTop() + " openHeight : " + openHeight + " fullScreenHeight : " + fullScreenHeight);
        if (view2.getTop() < openHeight) {
            openType = FOLD;
        } else if (view2.getTop() >= openHeight && view2.getTop() < fullScreenHeight) {
            openType = OPEN;
        } else {
            openType = FULL_SCREEN;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        processTouchEvent(event);
        return true;
    }

    float ooY;

    public void processTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float cY = event.getY();
                if (scrollListener != null) {
                        scrollListener.scrollListener((int) (cY - ooY));
                }
                ooY = cY;
                if (isSliding) {
                    return;
                }
                int dY = (int) (cY - oY);

                if (dY > 0) {
                    scrollDirection = ScrollDirection.DOWN;
                } else if (dY < 0) {
                    scrollDirection = ScrollDirection.UP;
                } else {
                    scrollDirection = ScrollDirection.NO;
                }
                if (dY == 0) {
                    return;
                }
                oY = cY;
                move(dY);
                break;
            case MotionEvent.ACTION_UP:

                Log.d(TAG, "processTouchEvent ACTION_UP " + " open type : " + openType.getName()
                        + " , scroll direction : " + scrollDirection.getName());
                adjustBottomViewPosition();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
    }

    private void open() {
        Log.d(TAG, "scroll open() ");
        startScroll(view2.getTop(), openHeight);
    }

    private void fold() {
        Log.d(TAG, "scroll fold() ");
        startScroll(view2.getTop(), openHeight - maxOpenDistance);
    }

    private void foldScreenToOpen() {
        Log.d(TAG, "scroll foldScreenToOpen() ");
        startScroll(view2.getTop(), fullScreenHeight - maxFullScreenDistance);
    }

    private void fullScreen() {
        Log.d(TAG, "scroll fullScreen() ");
        startScroll(view2.getTop(), fullScreenHeight);
    }

    private void startScroll(int startY, int endY) {
        Log.d(TAG, "startScroll() -------------- (endY - startY) : " + (endY - startY) + " startY : " + startY + " end : " + endY);

        float distance = endY - startY;
        float t = distance / maxOpenDistance * 500;

        mScroller.startScroll(0, 0, 0, endY - startY, (int) Math.abs(t));
        postInvalidate();
    }

    private int[] getSelectItemRect() {
        return calendarDateView.getCurrentSelectPosition();
    }

    private void move(int dY) {
        Log.d(TAG, "move() openType.getName :" + openType.getName());
        Log.d(TAG, "move() dY " + dY);
        if (dY == 0) {
            return;
        }
        int[] selectRect = getSelectItemRect();
        int itemHeight = calendarDateView.getOpenItemHeight();
        int dY1 = calculateScrollDistance(calendarDateView.getTop(), dY, -selectRect[1], 0);
        if (calendarDateView.getTop() >= 0 && openType == OPEN && scrollDirection == ScrollDirection.DOWN) {
            dY1 = 0;
        } else if (calendarDateView.getTop() >= 0 && view2.getTop() >= openHeight && scrollDirection == ScrollDirection.UP) {
            dY1 = 0;
        } else if (calendarDateView.getTop() >= 0 && view2.getTop() > openHeight) {
            dY1 = 0;
        }
        //TODO red 17/10/30 最好统一
        int row = 5;
        int dY2 = 0;
        if (openType == FOLD) {
            dY2 = calculateScrollDistance(view2.getTop() - openHeight, dY, -(openHeight - itemHeight), 0);
        } else if (openType == OPEN) {
            if (view2.getTop() >= openHeight && dY > 0) {
                int itemScrollY = (dY + row) / row;
                calendarDateView.viewChangeWithScrollDistance(itemScrollY);
                dY2 = calculateScrollDistance(view2.getTop() - fullScreenHeight, itemScrollY * 6, -(fullScreenHeight - openHeight), 0);
            } else if (view2.getTop() > openHeight && dY < 0) {
                dY2 = calculateScrollDistance(view2.getTop() - fullScreenHeight, dY, -(fullScreenHeight - openHeight), 0);
                int itemScrollY = ((dY2) / row) == 0 ? -1 : ((dY2) / row);
                calendarDateView.viewChangeWithScrollDistance(itemScrollY);
            } else if (view2.getTop() <= openHeight && dY < 0) {
                dY2 = calculateScrollDistance(view2.getTop() - openHeight, dY, -(openHeight - itemHeight), 0);
            } else if (view2.getTop() <= openHeight && dY >= 0) {
                dY2 = calculateScrollDistance(view2.getTop() - openHeight, dY, -(openHeight - itemHeight), 0);
            }
        } else if (openType == FULL_SCREEN) {
            if (dY < 0) {
                dY2 = calculateScrollDistance(view2.getTop() - fullScreenHeight, dY, -(fullScreenHeight - openHeight), 0);
                int itemScrollY = ((dY2) / row) == 0 ? -1 : ((dY2) / row);
                calendarDateView.viewChangeWithScrollDistance(itemScrollY);
            } else if (dY >= 0) {
                dY2 = calculateScrollDistance(view2.getTop() - fullScreenHeight, dY, -(fullScreenHeight - openHeight), 0);
                int itemScrollY = ((dY2) / row) == 0 ? 1 : ((dY2) / row);
                calendarDateView.viewChangeWithScrollDistance(itemScrollY);
            }
        }

        if (dY1 != 0) {
            calendarDateView.offsetTopAndBottom(dY1);
        }
        view2.offsetTopAndBottom(dY2);
        Log.d(TAG, "move() view2.getTop : " + view2.getTop());
    }

    private int calculateScrollDistance(int top, int dY, int minValue, int maxValue) {
//        Log.d(TAG, "calculateScrollDistance " + "open type : " + openType.getName()
//                + " top : " + top + " dY: " + dY + " minValue: " + minValue + " maxValue : " + maxValue);
        if (top + dY < minValue) {
            return minValue - top;
        }

        if (top + dY > maxValue) {
            return maxValue - top;
        }
        return dY;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            isSliding = true;
            int cy = mScroller.getCurrY();
            int dy = cy - oldY;
            Log.d(TAG, "computeScroll computeScrollOffset " + " dy : " + dy);
            if (scrollListener != null) {
                    scrollListener.scrollListener(dy);
            }
            if (dy > 0) {
                scrollDirection = ScrollDirection.DOWN;
            } else if (dy < 0) {
                scrollDirection = ScrollDirection.UP;
            } else {
                scrollDirection = ScrollDirection.NO;
            }
            move(dy);
            oldY = cy;
            invalidate();
        } else {
            Log.d(TAG, "computeScroll computeScrollOffset false");
            scrollDirection = ScrollDirection.NO;
            oldY = 0;
            isSliding = false;
            freshOpenType();
            //#2622 HBC
            if (view2.getTop() != foldHeight && view2.getTop() != openHeight && view2.getTop() != fullScreenHeight) {
                adjustBottomViewPosition();
            }
            Log.d(TAG, "computeScroll computeScrollOffset -----------------------");
            if (!calendarDateView.isHorizontalScrolled()) {
                calendarDateView.refreshLeftAndRightCalendarData(-1);
            }
        }
        Log.d(TAG, "computeScroll " + "open type : " + openType.getName());
    }

    private void adjustBottomViewPosition() {
        int line1 = (openHeight - foldHeight) / 2;
        int line2 = (fullScreenHeight + openHeight) / 2;
        if (view2.getTop() >= foldHeight && view2.getTop() <= line1) {
            fold();
        } else if (view2.getTop() > line1 && view2.getTop() <= openHeight) {
            open();
        } else if (view2.getTop() > openHeight && view2.getTop() < line2) {
            foldScreenToOpen();
        } else if (view2.getTop() >= line2 && view2.getTop() <= fullScreenHeight) {
            fullScreen();
        }
    }

    enum OpenType {
        FOLD("fold"), OPEN("open"), FULL_SCREEN("full_screen");
        private String name;

        OpenType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private enum ScrollDirection {
        DOWN("down"), UP("up"), NO("no");
        private String name;

        ScrollDirection(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private ScrollListener scrollListener;

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public interface ScrollListener {
        void scrollListener(int distance);
    }

    private LayoutListener layoutListener;

    public void setLayoutListener(LayoutListener layoutListener) {
        this.layoutListener = layoutListener;
    }

    public interface LayoutListener {
        void fold();

        void open();

        void fullScreen();
    }
}
