package com.albertech.demo.rocker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;



/**
 * Created by Albert on 2017/6/29.
 */
public class AimerView extends View implements RockerView.OnRockListener {

    private float mSpeed = 30f;

    private Path mPath = new Path();
    private RectF mRect = new RectF();
    private Paint mPaint1;
    private Paint mPaint2;

    private int mWidth;
    private int mHeight;
    private int mRadius;
    private int mCenterX;
    private int mCenterY;

    public AimerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void init() {
        initPaint();
        initDimensions();
    }

    private void initPaint() {
        mPaint1 = new Paint();
        mPaint1.setDither(true);
        mPaint1.setAntiAlias(true);
        mPaint1.setFilterBitmap(true);
        mPaint1.setStrokeWidth(2);
        mPaint1.setColor(Color.GREEN);
        mPaint1.setStyle(Paint.Style.STROKE);
        mPaint2 = new Paint(mPaint1);
        mPaint2.setColor(Color.BLACK);
        mPaint2.setStyle(Paint.Style.FILL);
    }

    private void initDimensions() {
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mRadius = Math.min(mWidth, mHeight) / 6;
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;
        refreshDimensions();
    }

    private void refreshDimensions() {
        mRect.left = mCenterX - mRadius;
        mRect.top = mCenterY - mRadius;
        mRect.right = mCenterX + mRadius;
        mRect.bottom = mCenterY + mRadius;
        calcPath();
    }

    private void calcPath() {
        mPath.reset();
        mPath.moveTo(0, 0);
        mPath.lineTo(0, mHeight);
        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(mWidth, 0);
        mPath.lineTo(0, 0);
        mPath.addArc(mRect, -90f, 360f);
        mPath.close();
    }

    private void updateCenter(int x, int y) {
        mCenterX = mCenterX + x < 0 ? 0 : (mCenterX + x > mWidth ? mWidth : mCenterX + x);
        mCenterY = mCenterY + y < 0 ? 0 : (mCenterY + y > mHeight ? mHeight : mCenterY + y);
        refreshDimensions();
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint2);
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint1);
        canvas.drawLine(mCenterX - mRadius, mCenterY, mCenterX + mRadius, mCenterY, mPaint1);
        canvas.drawLine(mCenterX, mCenterY - mRadius, mCenterX, mCenterY + mRadius, mPaint1);
    }

    @Override
    public void onRock(float ratioX, float ratioY) {
        updateCenter((int) (ratioX * mSpeed), (int) (ratioY * mSpeed));
    }

    @Override
    public void onReset() {

    }
}
