package com.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import static android.R.attr.textSize;

import androidx.annotation.Nullable;

//+0002361
public class ListHeaderView extends View {
    private TextPaint paint;
    private Paint shadowPaint;
    private float density;
    private LineStyle lineStyle;

    public ListHeaderView(Context context) {
        super(context);
        init();
    }

    public ListHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        lineStyle = LineStyle.DOWN;
        density = getResources().getDisplayMetrics().density;
        paint = new TextPaint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);
        paint.setTextSize(getResources().getDisplayMetrics().density * textSize);
        shadowPaint = new Paint();
//        setLayerType(LAYER_TYPE_SOFTWARE, null);
//        shadowPaint.setShadowLayer(getPxByDp(3), 0, getPxByDp(1), 0xe5cccccc);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        shadowPaint.setColor(0xeeeeee);
        canvas.drawRect(0, 0, getWidth(), getHeight() - getPxByDp(3), shadowPaint);

        paint.setColor(0xff8a8a8a);

        if (lineStyle == LineStyle.DOWN) {
            canvas.drawLine(getWidth() / 2 - getPxByDp(7), getPxByDp(4), getWidth() / 2, getPxByDp(6), paint);
            canvas.drawLine(getWidth() / 2, getPxByDp(6), getWidth() / 2 + getPxByDp(7), getPxByDp(4), paint);

            canvas.drawLine(getWidth() / 2 - getPxByDp(7), getPxByDp(7), getWidth() / 2, getPxByDp(9), paint);
            canvas.drawLine(getWidth() / 2, getPxByDp(9), getWidth() / 2 + getPxByDp(7), getPxByDp(7), paint);
        } else if (lineStyle == LineStyle.MIDDLE) {
            canvas.drawLine(getWidth() / 2 - getPxByDp(7), getPxByDp(6), getWidth() / 2 + getPxByDp(7), getPxByDp(6), paint);
            canvas.drawLine(getWidth() / 2 - getPxByDp(7), getPxByDp(9), getWidth() / 2 + getPxByDp(7), getPxByDp(9), paint);
        } else {
            canvas.drawLine(getWidth() / 2 - getPxByDp(7), getPxByDp(6), getWidth() / 2, getPxByDp(4), paint);
            canvas.drawLine(getWidth() / 2, getPxByDp(4), getWidth() / 2 + getPxByDp(7), getPxByDp(6), paint);

            canvas.drawLine(getWidth() / 2 - getPxByDp(7), getPxByDp(9), getWidth() / 2, getPxByDp(7), paint);
            canvas.drawLine(getWidth() / 2, getPxByDp(7), getWidth() / 2 + getPxByDp(7), getPxByDp(9), paint);
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = getPxByDp(MeasureSpec.getSize(heightMeasureSpec));
        setMeasuredDimension(width, height);
    }

    private int getPxByDp(int dp) {
        return (int) (dp * density);
    }

    public void setLineStyle(LineStyle lineStyle) {
        this.lineStyle = lineStyle;
        invalidate();
    }

    public enum LineStyle {
        UP, DOWN, MIDDLE
    }
}
