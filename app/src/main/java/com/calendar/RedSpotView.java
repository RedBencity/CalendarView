package com.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

//+0002361
public class RedSpotView extends View {
    private Paint paint;
    private boolean isDraw = true;
    private int alpha;
    private int color = 0xcc0000;
    private int maxAlpha = 255;

    public RedSpotView(Context context) {
        super(context);
    }

    public RedSpotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    public RedSpotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isDraw) {
            paint.setColor(color);
            paint.setAlpha(alpha);
            canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2, paint);
        }
    }

    public void changeAlpha(float scale) {
        if (!isDraw) {
            return;
        }
        this.alpha = (int) (scale * maxAlpha);
        invalidate();
    }

    public void setDraw(boolean draw) {
        isDraw = draw;
        invalidate();
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    public void setMaxAlpha(float scale) {
        this.maxAlpha = (int) (scale * 255 / 100);
        this.alpha = (int) (scale * 255 / 100);
        invalidate();
    }
}
