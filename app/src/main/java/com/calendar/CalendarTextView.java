package com.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

//+0002361
public class CalendarTextView extends View {
    private String text = "";
    private TextPaint paint;
    private int textSize = 12;
    private int textColor = 0x8A000000;
    private boolean isTextCenter = false;
    private int backgroundColor = 0x000000;
    private int alpha = 0xff;
    private String changeText = "";
    private boolean isTextSizeChange = false;
    private int backgroundAlpha = 0xff;

    public CalendarTextView(Context context) {
        super(context);
        init();
    }

    public CalendarTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CalendarTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        paint = new TextPaint();
        paint.setColor(textColor);
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (text.length() == 0) {
            return;
        }
        if (backgroundColor != 0x000000) {
            paint.setColor(backgroundColor);
            paint.setAlpha(backgroundAlpha);
            canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        }
        paint.setColor(textColor);
        paint.setAlpha(alpha);
        // 计算Baseline绘制的起点X轴坐标 ，计算方式：画布宽度的一半 - 文字宽度的一半
        int baseX;
        if (isTextCenter) {
            baseX = (int) (canvas.getWidth() / 2 - paint.measureText(text) / 2);
        } else {
            baseX = 0;
        }
        // 计算Baseline绘制的Y坐标 ，计算方式：画布高度的一半 - 文字总高度的一半
        int baseY = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

        if (isTextSizeChange) {
            isTextSizeChange = false;
            int size = (int) (getWidth() / paint.getTextSize());
            if (text.length() != 0 && text.length() > size + 1) {
                changeText = text.substring(0, size) + "......";
            } else {
                changeText = text;
            }
        }
        canvas.drawText(changeText, baseX, baseY, paint);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = textSize + 10;
        setMeasuredDimension(width, height);
    }

    public void changeAlpha(float scale) {
        this.alpha = (int) (138 * scale);
        this.backgroundAlpha = (int) (0xcc * scale);
        invalidate();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    public void setText(String text) {
        this.text = text;
        this.changeText = text;
        invalidate();
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        paint.setTextSize(textSize);
        isTextSizeChange = true;
        invalidate();
    }

    public void setTextCenter(boolean textCenter) {
        isTextCenter = textCenter;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
