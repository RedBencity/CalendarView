package com.calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

//+0002361
public class CalendarImageView extends View {
    private Paint paint;
    private int alpha = 0xff;
    private Bitmap bitmap;
    private Bitmap bgBitmap;
    private Canvas mCanvas;
    private Rect rect;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;

    public CalendarImageView(Context context) {
        super(context);
        init();
    }

    public CalendarImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CalendarImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        rect = new Rect();
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap == null) {
            return;
        }
        mCanvas.drawColor(0xeeeeee);
        mCanvas.drawBitmap(bitmap, 0, 0, null);
        paint.setAlpha(alpha);
        canvas.setDrawFilter(paintFlagsDrawFilter);
        canvas.drawBitmap(bgBitmap, null, rect, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        rect.left = 0;
        rect.top = 0;
        rect.right = width;
        rect.bottom = height;

    }

    public void changeAlpha(float scale) {
        this.alpha = (int) (0xff * scale);
        invalidate();
    }

    public void setBitmap(int id) {
        if (id == -1) {
            bitmap = null;
            invalidate();
            return;
        }
        bitmap = BitmapFactory.decodeResource(getResources(), id);
        bgBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(bgBitmap);
        invalidate();
    }

}
