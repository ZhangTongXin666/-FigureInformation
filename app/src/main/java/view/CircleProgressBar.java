/*
 * Copyright (c) 2015. TedYin
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import com.example.kys_31.figureinformation.R;

/**
 * 圆形进度条
 * Created by ted on 3/24/15.
 */
public class CircleProgressBar extends View {

    private static final float MAX_PROGRESS = 100f;
    private static final float MIN_PROGRESS = 0;
    private static final String TAG = "CircleProgressBar";
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private Context mContext;

    private int mCpbWholeBackgroundColor = Color.parseColor("#ffeeeaff");
    private int mCpbBackgroundColor = Color.parseColor("#7df5f5f5");
    private int mCpbForegroundColor = Color.parseColor("#ffffff");
    private int mCpbProgressTextColor;
    private int mCpbStrokeWidth = 10;// default stroke width
    private int mCpbStartAngle = -90;// default 12 o'clock
    private int mCpbMaxAngle = 360;// default complete circle
    private boolean mCpbNeedAnim = true;// default start anim
    private boolean mCpbNeedShowText = true;// default show text

    private RectF mForegroundRectF, mBackgroundRectF;
    private Paint mCpbWholeBackgroundPaint;
    private Paint mCpbBackgroundPaint;
    private Paint mCpbForegroundPaint;
    private Paint mCpbTextPaint;
    private int mAngleStep = 0;
    private int mMinWidth;
    private int mCurrentProgress = 100;
    private AnimRunnable mAnimRunnable;

    public CircleProgressBar(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        parseAttributes(attrs);
        init();
    }

    private void parseAttributes(AttributeSet attrs) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        mCpbWholeBackgroundColor = a.getColor(R.styleable.CircleProgressBar_cpbWholeBackgroundColor, mCpbWholeBackgroundColor);
        mCpbForegroundColor = a.getColor(R.styleable.CircleProgressBar_cpbForegroundColor, mCpbForegroundColor);
        mCpbBackgroundColor = a.getColor(R.styleable.CircleProgressBar_cpbBackgroundColor, mCpbBackgroundColor);
        mCpbProgressTextColor = a.getColor(R.styleable.CircleProgressBar_cpbProgressTextColor, mCpbProgressTextColor);
        mCpbStrokeWidth = a.getInt(R.styleable.CircleProgressBar_cpbStrokeWidth, mCpbStrokeWidth);
        mCpbStartAngle = a.getInt(R.styleable.CircleProgressBar_cpbStartAngle, mCpbStartAngle);
        mCpbMaxAngle = a.getInt(R.styleable.CircleProgressBar_cpbMaxAngle, mCpbMaxAngle);
        mCpbNeedShowText = a.getBoolean(R.styleable.CircleProgressBar_cpbNeedShowText, mCpbNeedShowText);
        mCpbNeedAnim = a.getBoolean(R.styleable.CircleProgressBar_cpbNeedAnim, mCpbNeedAnim);

        mCpbNeedAnim = mCpbMaxAngle % 360 == 0 && mCpbNeedAnim;
        mCpbProgressTextColor = mCpbProgressTextColor == 0 ? mCpbForegroundColor : mCpbProgressTextColor;
        a.recycle();
    }

    private void init() {
        initWholeBackgroundPaint();
        initProgressBackgroundPaint();
        initProgressForegroundPaint();
        initTextPaint();
        initRectF();
        initAnim();
    }

    // init progress background paint
    private void initWholeBackgroundPaint() {
        mCpbWholeBackgroundPaint = new Paint();
        mCpbWholeBackgroundPaint.setAntiAlias(true);//设置抗锯齿
        mCpbWholeBackgroundPaint.setColor(mCpbWholeBackgroundColor);
    }

    // init progress background paint
    private void initProgressBackgroundPaint() {
        mCpbBackgroundPaint = new Paint();
        mCpbBackgroundPaint.setAntiAlias(true);
        mCpbBackgroundPaint.setStyle(Paint.Style.STROKE);
        mCpbBackgroundPaint.setStrokeWidth(mCpbStrokeWidth);
        mCpbBackgroundPaint.setColor(mCpbBackgroundColor);
    }

    // init progress foreground paint
    private void initProgressForegroundPaint() {
        mCpbForegroundPaint = new Paint();
        mCpbForegroundPaint.setAntiAlias(true);
        mCpbForegroundPaint.setDither(true);//仿抖动
        mCpbForegroundPaint.setStyle(Paint.Style.STROKE);
        mCpbForegroundPaint.setStrokeCap(Paint.Cap.ROUND);
        mCpbForegroundPaint.setStrokeWidth(mCpbStrokeWidth);
        mCpbForegroundPaint.setColor(mCpbForegroundColor);
    }

    // init text paint
    private void initTextPaint() {
        mCpbTextPaint = new Paint();
        mCpbTextPaint.setAntiAlias(true);
        mCpbTextPaint.setColor(mCpbProgressTextColor);
    }

    // init rectF
    private void initRectF() {
        mBackgroundRectF = new RectF();
        mForegroundRectF = new RectF();
    }

    // init Animation Runnable
    private void initAnim() {
        if (mCpbNeedAnim) {
            mAnimRunnable = new AnimRunnable();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mMinWidth = Math.min(getWidth(), getHeight());
            int rLeft = mCpbStrokeWidth / 2;
            int rTop = mCpbStrokeWidth / 2;
            int rRight = mMinWidth - mCpbStrokeWidth / 2;
            int rBottom = mMinWidth - mCpbStrokeWidth / 2;
            mBackgroundRectF.set(rLeft, rTop, rRight, rBottom);
            mForegroundRectF.set(rLeft, rTop, rRight, rBottom);
        }
        startAnimIfNeed();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCpbWholeBackground(canvas);
        drawCpbBackground(canvas);
        drawCpbForeground(canvas);
        drawProgressText(canvas);
    }


    private void drawCpbWholeBackground(Canvas canvas) {
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 2, mCpbWholeBackgroundPaint);
    }

    private void drawProgressText(Canvas canvas) {
        if (!mCpbNeedShowText) return;
        int textSize = (int) ((mMinWidth - mCpbStrokeWidth * 2) / 2.5);
        mCpbTextPaint.setTextSize(textSize);
        Paint.FontMetrics fm = mCpbTextPaint.getFontMetrics();
        float textHeight = (float) Math.ceil(fm.descent - fm.top);//向上取整
        float textWidth = mCpbTextPaint.measureText("跳过");
        float x = (mMinWidth - textWidth) / 2;
        float y = (mMinWidth - textHeight) / 2 + textSize;
        canvas.drawText("跳过", x, y, mCpbTextPaint);
    }

    private void drawCpbForeground(Canvas canvas) {
        int startAngle = mAngleStep + mCpbStartAngle;
        int temValue = 100 - mCurrentProgress;
        int sweepAngle = (int) ((temValue / MAX_PROGRESS) * mCpbMaxAngle);
        canvas.drawArc(mForegroundRectF, startAngle, sweepAngle, false, mCpbForegroundPaint);
    }

    private void drawCpbBackground(Canvas canvas) {
       canvas.drawArc(mBackgroundRectF, 0, mCpbMaxAngle, false, mCpbBackgroundPaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimRunnable != null) {
            mHandler.removeCallbacks(mAnimRunnable);
        }
    }

    private void startAnimIfNeed() {
        if (mAnimRunnable != null) {
            mHandler.removeCallbacks(mAnimRunnable);
        }

        if (mCpbNeedAnim) {
            mHandler.post(mAnimRunnable);
        }
    }

    private class AnimRunnable implements Runnable {
        @Override
        public void run() {
            if (mCurrentProgress <= MIN_PROGRESS) {
                mCurrentProgress = (int) MIN_PROGRESS;
                invalidateView();
                mHandler.removeCallbacks(this);
            } else {
                invalidateView();
                mHandler.postDelayed(this, 12);
            }
        }

        private void invalidateView() {
            mAngleStep += 2;
            invalidate();
        }

    }

    /**
     * invalidate view
     */
    public void invalidateUi() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    public void setProgress(int progress) {
        this.mCurrentProgress = progress < MIN_PROGRESS ? (int) MIN_PROGRESS : progress; // 倒计时
        if (!mCpbNeedAnim) {
            invalidateUi();
        }
    }
}

