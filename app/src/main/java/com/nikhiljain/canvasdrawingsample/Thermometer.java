package com.nikhiljain.canvasdrawingsample;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Thermometer extends View {
    private Paint mInnerCirclePaint;
    private int mInnerRadius;
    private int mThermometerColor = Color.RED;
    private Bitmap bitmap;
    private int left;
    private int top;
    private int innerCircleCenter;
    private int circleHeight;
    private int lineEndY;
    private int lineStartY;

    public Thermometer(Context context) {
        this(context, null);
    }

    public Thermometer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Thermometer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (attrs != null) {

            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Thermometer, defStyle, 0);

            mThermometerColor = a.getColor(R.styleable.Thermometer_therm_color, mThermometerColor);

            a.recycle();
        }

        init();
    }

    private void init() {
        mInnerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerCirclePaint.setColor(mThermometerColor);
        mInnerCirclePaint.setStyle(Paint.Style.FILL);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.thermometer_container);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // init bitmap
        int scaledHeight;
        int scaledWidth;
        int width = getWidth();
        int height = getHeight();
        if (width > height) {
            scaledHeight = (int) (height * 0.98);
            scaledWidth = scaledHeight * bitmap.getWidth() / bitmap.getHeight();
        } else {
            scaledWidth = (int) (width * 0.98);
            scaledHeight = scaledWidth * bitmap.getHeight() / bitmap.getWidth();
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);

        mInnerRadius = bitmap.getWidth() / 8;

        mInnerCirclePaint.setStrokeWidth((int)(bitmap.getWidth() / 10));
        left = (getWidth() - bitmap.getWidth()) / 2;
        top = (getHeight() - bitmap.getHeight()) / 2;
        innerCircleCenter = (left + left + bitmap.getWidth() + (Math.min(width, height) / 72)) / 2;
        circleHeight = (top + bitmap.getHeight()) - (int)(bitmap.getHeight() / 4.6f);
        lineStartY = ((int)(bitmap.getHeight() / 4.6f) + top);
        lineEndY = (top + bitmap.getHeight()) - (int)(bitmap.getHeight() / 4f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawThermometer(canvas);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //takes care of paddingTop and paddingBottom
        int paddingY = getPaddingBottom() + getPaddingTop();

        //get height and width
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        height += paddingY;

        setMeasuredDimension(width, height);
    }

    private void drawThermometer(Canvas canvas) {
        canvas.drawCircle(innerCircleCenter, circleHeight, mInnerRadius, mInnerCirclePaint);
        canvas.drawLine(innerCircleCenter, lineStartY, innerCircleCenter, lineEndY, mInnerCirclePaint);
        canvas.drawBitmap(bitmap, left, top, new Paint());
    }

    public void setThermometerColor(int thermometerColor) {
        this.mThermometerColor = thermometerColor;
        mInnerCirclePaint.setColor(mThermometerColor);
        invalidate();
    }
}