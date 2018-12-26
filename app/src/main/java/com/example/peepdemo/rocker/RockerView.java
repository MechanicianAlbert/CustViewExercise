package com.example.peepdemo.rocker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by A on 2017/6/28.
 */

public class RockerView extends View {

    private int mCircleColor = Color.parseColor("#FF777777");
    private int mLineColor = Color.parseColor("#FF000000");
    private int mBallColor = Color.parseColor("#FFFF0000");
    private Paint mPaint;

    private int mCircleCenterX;
    private int mCircleCenterY;
    private int mCircleRadius;
    private float mBallRadius;
    private float mBallCenterX;
    private float mBallCenterY;
    private float mRangeRadius;
    private int mHandleWidth;

    private float mTouchOffset;
    private float mTouchOffsetX;
    private float mTouchOffsetY;
    private float mTouchRatio;
    private float mBallOffsetX;
    private float mBallOffsetY;
    private float mBallRatioX;
    private float mBallRatioY;

    private boolean mOutput;
    private Set<OnRockListener> mOnRockListenerSet = new HashSet<>();
    private Runnable mOutputRunnable = new Runnable() {
        @Override
        public void run() {
            if (!mOutput) {
                return;
            }
            for (OnRockListener onRockListener : mOnRockListenerSet) {
                onRockListener.onRock(mBallRatioX, mBallRatioY);
            }
            postDelayed(this, 500);
        }
    };

    public RockerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {

    }

    private void init() {
        initPaint();
        initDimensions();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    private void initDimensions() {
        mCircleCenterX = getMeasuredWidth() / 2;
        mCircleCenterY = getMeasuredHeight() / 2;
        mCircleRadius = Math.min(mCircleCenterX, mCircleCenterY) * 2 / 3;
        mBallCenterX = mCircleCenterX;
        mBallCenterY = mCircleCenterY;
        mBallRadius = mCircleRadius / 2;
        mHandleWidth = (int) (mBallRadius / 2);
//        mRangeRadius = mCircleRadius - mBallRadius;
        mRangeRadius = mCircleRadius;
        mPaint.setStrokeWidth(mHandleWidth);
    }

    private void calcOffsetAndRatio(float x, float y) {
        mTouchOffsetX = x - mCircleCenterX;
        mTouchOffsetY = y - mCircleCenterY;
        mTouchOffset = (float) Math.pow(Math.pow(mTouchOffsetX, 2) + Math.pow(mTouchOffsetY, 2), 0.5);
        mTouchRatio = mTouchOffset / mRangeRadius;
        mBallCenterX = mTouchOffsetX / Math.max(mTouchRatio, 1) + mCircleCenterX;
        mBallCenterY = mTouchOffsetY / Math.max(mTouchRatio, 1) + mCircleCenterY;
        mBallOffsetX = mBallCenterX - mCircleCenterX;
        mBallOffsetY = mBallCenterY - mCircleCenterY;
        mBallRatioX = mBallOffsetX / mRangeRadius;
        mBallRatioY = mBallOffsetY / mRangeRadius;
    }

    private void startOutput() {
        post(mOutputRunnable);
    }

    public void addOnRockListener(OnRockListener onRockListener) {
        mOnRockListenerSet.add(onRockListener);
    }

    public void removeOnRockListener(OnRockListener onRockListener) {
        mOnRockListenerSet.remove(onRockListener);
    }

    public void clearOnRockListener() {
        mOnRockListenerSet.clear();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mCircleColor);
        canvas.drawCircle(mCircleCenterX, mCircleCenterY, mCircleRadius, mPaint);
        mPaint.setColor(mLineColor);
        canvas.drawCircle((mBallCenterX + mCircleCenterX * 2) / 3, (mBallCenterY + mCircleCenterY * 2) / 3, mHandleWidth / 2, mPaint);
        canvas.drawLine((mBallCenterX + mCircleCenterX * 2) / 3, (mBallCenterY + mCircleCenterY * 2) / 3, mBallCenterX, mBallCenterY, mPaint);
        mPaint.setColor(mBallColor);
        canvas.drawCircle(mBallCenterX, mBallCenterY, mBallRadius, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x;
        float y;
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mOutput = false;
            x = mCircleCenterX;
            y = mCircleCenterY;
            for (OnRockListener onRockListener : mOnRockListenerSet) {
                onRockListener.onReset();
            }
        } else {
            mOutput = true;
            x = event.getX();
            y = event.getY();
        }
        calcOffsetAndRatio(x, y);
        startOutput();
        postInvalidate();
        return super.onTouchEvent(event);
    }

    public interface OnRockListener {
        void onRock(float ratioX, float ratioY);
        void onReset();
    }
}
