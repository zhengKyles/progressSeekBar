package com.kyle.gradientseekbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.kyle.gradientseekbar.utils.DensityUtil;

import static com.kyle.gradientseekbar.MySeekBarLine.childHeight;
import static com.kyle.gradientseekbar.MySeekBarLine.childWidth;
import static com.kyle.gradientseekbar.MySeekBarLine.grayColor;

/**
 * Created by Kyle on 2018/9/25.
 */

public class MySeekBarCircle extends View {
    private Paint mPaintCircle;
    private Paint mPaintCircleOut;

    private int circleRadius;
    private int circleOutRadius;

    public MySeekBarCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaintCircle = new Paint();
        mPaintCircle.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintCircle.setColor(grayColor);

        mPaintCircleOut = new Paint();
        mPaintCircleOut.setStyle(Paint.Style.STROKE);
        mPaintCircleOut.setColor(grayColor);
        mPaintCircleOut.setStrokeWidth(2);
        circleOutRadius = (int) (childWidth / 2) + DensityUtil.dp2px(2);
        circleRadius = (int) (childWidth / 2);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(childWidth / 2, childHeight / 2, circleRadius, mPaintCircle);
        canvas.drawCircle(childWidth / 2, childHeight / 2, circleOutRadius, mPaintCircleOut);
    }

    public void changeColor(int color) {
        mPaintCircle.setColor(color);
        mPaintCircleOut.setColor(color);
        invalidate();
    }

    public void setCircleOutRadius(int circleOutRadius) {
        this.circleOutRadius = this.circleRadius + circleOutRadius;
    }
}
