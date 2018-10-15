package com.kyle.gradientseekbar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.kyle.gradientseekbar.utils.DensityUtil;


/**
 * Created by Kyle on 2018/9/25.
 */

public class MySeekBarLine extends ViewGroup {
    //下面是默认值
    private int DEFAULT_TXT_COLOR = Color.parseColor("#333333");//默认文字颜色
    private float DEFAULT_TXT_SIZE = DensityUtil.dp2px(14);//默认文字大小
    private float DEFAULT_MARGIN_TOP = DensityUtil.dp2px(20);//默认margin top
    private float DEFAULT_MARGIN_LEFT = DensityUtil.dp2px(20);//默认margin left
    private float DEFAULT_MARGIN_RIGHT = DensityUtil.dp2px(0);//默认margin right
    private float DEFAULT_TXT_MARGIN = DensityUtil.dp2px(10);//默认文件和线条间隔
    private static float DEFAULT_CHILD_CIRCLE_WIDTH = DensityUtil.dp2px(20);
    private static float DEFAULT_CHILD_CIRCLE_HEIGHT = DensityUtil.dp2px(20);
    private int DEFAULT_START_COLOR = Color.parseColor("#FFB401");
    private int DEFAULT_END_COLOR = Color.parseColor("#68D581");
    private static int DEFAULT_GRAY_COLOR = Color.parseColor("#D8D8D8");//默认线条颜色
    private float DEFAULT_LINE_HEIGHT = DensityUtil.dp2px(3);//默认线条高度
    private int DEFAULT_MAX_COUNT = 5;//默认圆形数量
    private int DEFAULT_MAX_UNIT = 60;//默认单位最大值
    private float DEFAULT_LINE_OVER_LENGTH = DensityUtil.dp2px(30);//默认线条超出长度 比如最大值为60，超出则默认为:60以上
    //下面是使用的值
    private int txtColor = DEFAULT_TXT_COLOR;//文字颜色
    private float txtSize = DEFAULT_TXT_SIZE;//文字颜色
    private float marginTop = DEFAULT_MARGIN_TOP;
    private float marginLeft = DEFAULT_MARGIN_LEFT;
    private float marginRight = DEFAULT_MARGIN_RIGHT;
    private float txtMargin = DEFAULT_TXT_MARGIN;//文字和线条间隔 单位dp
    public static float childWidth = DEFAULT_CHILD_CIRCLE_WIDTH;//子view的宽
    public static float childHeight = DEFAULT_CHILD_CIRCLE_HEIGHT;//子view的高
    private int startColor = DEFAULT_START_COLOR;//起始颜色
    private int endColor = DEFAULT_END_COLOR;//结束颜色
    public static int grayColor = DEFAULT_GRAY_COLOR;//默认灰色
    private int lineWidth;//布局宽度
    private float lineHeight = DEFAULT_LINE_HEIGHT;//线条高度
    private int maxCount = DEFAULT_MAX_COUNT;//总child数目
    private int maxUnit = DEFAULT_MAX_UNIT;//最多60分钟
    private float lineOverLength = DEFAULT_LINE_OVER_LENGTH;

    //下面是固定值  不可设置
    private Paint mPaint;//带颜色线条
    private Paint mPaintGray;//默认颜色线条
    private Paint mPaintTxt;//文字
    private float colorLineWidth;//选中的线条的长度
    private float totalGrayLine;//线条最大长度
    private float totalCircleLine;//第一个球和最后一个球的间距
    private float progress = 0;
    public OnProgressChangeListener onProgressChangeListener;


    public MySeekBarLine(Context context) {
        super(context);
    }

    public MySeekBarLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MySeekBarLine);
        int count = array.getIndexCount();
        for (int i = 0; i < count; i++) {
            int a = array.getIndex(i);
            if (a == R.styleable.MySeekBarLine_txtColor) {
                txtColor = array.getColor(a, DEFAULT_TXT_COLOR);
            } else if (a == R.styleable.MySeekBarLine_txtSize) {
                txtSize = array.getDimension(a, DEFAULT_TXT_SIZE);
            } else if (a == R.styleable.MySeekBarLine_viewMarginTop) {
                marginTop = array.getDimension(a, DEFAULT_MARGIN_TOP);
            } else if (a == R.styleable.MySeekBarLine_viewMarginLeft) {
                marginLeft = array.getDimension(a, DEFAULT_MARGIN_LEFT);
            } else if (a == R.styleable.MySeekBarLine_viewMarginRight) {
                marginRight = array.getDimension(a, DEFAULT_MARGIN_RIGHT);
            } else if (a == R.styleable.MySeekBarLine_txtMargin) {
                txtMargin = array.getDimension(a, DEFAULT_TXT_MARGIN);
            } else if (a == R.styleable.MySeekBarLine_childRadius) {
                childWidth = array.getDimension(a, DEFAULT_CHILD_CIRCLE_WIDTH);
                childHeight = array.getDimension(a, DEFAULT_CHILD_CIRCLE_HEIGHT);
            } else if (a == R.styleable.MySeekBarLine_startColor) {
                startColor = array.getColor(a, DEFAULT_START_COLOR);
            } else if (a == R.styleable.MySeekBarLine_endColor) {
                endColor = array.getColor(a, DEFAULT_END_COLOR);
            } else if (a == R.styleable.MySeekBarLine_grayColor) {
                grayColor = array.getColor(a, DEFAULT_GRAY_COLOR);
            } else if (a == R.styleable.MySeekBarLine_lineHeight) {
                lineHeight = array.getDimension(a, DEFAULT_LINE_HEIGHT);
            } else if (a == R.styleable.MySeekBarLine_maxCount) {
                maxCount = array.getInteger(a, DEFAULT_MAX_COUNT);
            } else if (a == R.styleable.MySeekBarLine_maxUnit) {
                maxUnit = array.getInteger(a, DEFAULT_MAX_UNIT);
            } else if (a == R.styleable.MySeekBarLine_lineOverLength) {
                lineOverLength = array.getDimension(a, DEFAULT_LINE_OVER_LENGTH);
            }
        }
        array.recycle();
        setClipChildren(false);
        setWillNotDraw(false);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(lineHeight);

        mPaintTxt = new Paint();
        mPaintTxt.setColor(txtColor);
        mPaintTxt.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintTxt.setAntiAlias(true);
        mPaintTxt.setTextSize(txtSize);

        mPaintGray = new Paint();
        mPaintGray.setAntiAlias(true);
        mPaintGray.setColor(grayColor);
        mPaintGray.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintGray.setStrokeWidth(lineHeight);
        for (int i = 0; i < maxCount; i++) {
            MySeekBarCircle circle = new MySeekBarCircle(context, attrs);
            ViewGroup.LayoutParams params = new LayoutParams((int) childWidth, (int) childHeight);
            circle.setLayoutParams(params);
            addView(circle, i, params);
            final int finalI = i;
            circle.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeChildColorInIndex(finalI);
                    changeLineColor(finalI);
                }
            });

        }

    }

    /***
     * 选定某个childView位置改变之前和之后的颜色(之前为彩色，之后为灰色)
     * @param index
     */
    private void changeChildColorInIndex(int index) {
        for (int j = index; j >= 0; j--) {
            float radio = (float) j / maxCount;
            MySeekBarCircle view = (MySeekBarCircle) getChildAt(j);
            if(view!=null)
            view.changeColor(getColor(radio));
        }
        for (int k = index + 1; k < maxCount; k++) {
            MySeekBarCircle view = (MySeekBarCircle) getChildAt(k);
            if(view!=null)
                view.changeColor(grayColor);
        }
    }

    /***
     * 当点击childView时，根据所点击的店 改变线条颜色
     * @param index
     */
    private void changeLineColor(int index) {
        colorLineWidth = totalCircleLine / (maxCount - 1) * index;
        invalidate();
    }

    /***
     * 当手指触摸线条时，根据触摸的x改变颜色
     * @param width
     */
    private void changeLineColor(float width) {
        colorLineWidth = width > totalGrayLine ? totalGrayLine : width;
        int overIndex = (int) (width / totalCircleLine * (maxCount - 1));
        changeChildColorInIndex(overIndex);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            MySeekBarCircle child = ((MySeekBarCircle) getChildAt(i));
            child.layout((int) (marginLeft + (totalCircleLine / (maxCount - 1) * i) - childWidth / 2), (int) (marginTop - childWidth / 2), (int) (marginLeft + (totalCircleLine / (maxCount - 1) * i) - childWidth / 2 + childWidth), (int) (marginTop - childWidth / 2 + childWidth));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(marginLeft, marginTop, lineWidth - marginRight, marginTop, mPaintGray);
        canvas.drawLine(marginLeft, marginTop, marginLeft + colorLineWidth, marginTop, mPaint);
        if (onProgressChangeListener != null) {
            progress = colorLineWidth / totalCircleLine;
            onProgressChangeListener.onChange(progress);
        }

        int count = getChildCount();

        float txtWidth = 0;
        for (int i = 0; i < count; i++) {
            String txt = maxUnit / (count - 1) * i + "";
            txtWidth = mPaintTxt.measureText(txt, 0, txt.length());
            canvas.drawText(txt, marginLeft + (totalCircleLine / (count - 1) * i) - txtWidth / 2, marginTop + childHeight + txtMargin, mPaintTxt);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        lineWidth = MeasureSpec.getSize(widthMeasureSpec);
        totalGrayLine = lineWidth - marginLeft - marginRight;
        totalCircleLine = totalGrayLine - lineOverLength;
        @SuppressLint("DrawAllocation") LinearGradient shader = new LinearGradient(0, 0, totalGrayLine, lineHeight, new int[]{startColor, endColor}, null,
                Shader.TileMode.CLAMP);
        mPaint.setShader(shader);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //获取某一个百分比间的颜色,radio取值[0,1]
    public int getColor(float radio) {
        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);

        int red = (int) (redStart + ((redEnd - redStart) * radio + 0.5));
        int greed = (int) (greenStart + ((greenEnd - greenStart) * radio + 0.5));
        int blue = (int) (blueStart + ((blueEnd - blueStart) * radio + 0.5));
        return Color.argb(255, red, greed, blue);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    public interface OnProgressChangeListener {
        void onChange(float progress);
    }

    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }

    public float getProgress() {
        {
            return progress;
        }
    }

    public void setProgress(float progress) {
        this.progress = progress;
        colorLineWidth = totalCircleLine * progress;
        invalidate();
    }

    private float firstX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() <= marginLeft) return false;
                firstX = event.getX();
                changeLineColor(firstX - marginLeft);
                break;
            case MotionEvent.ACTION_MOVE:
                if (firstX == 0) return false;
                float moveX = event.getX();
                changeLineColor(moveX - marginLeft);
                break;
        }
        return true;
    }
}
