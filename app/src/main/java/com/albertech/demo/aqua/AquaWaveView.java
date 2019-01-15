package com.albertech.demo.aqua;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;


/**
 * Created by Albert on 2017/2/9.
 */
public class AquaWaveView extends View implements ViewTreeObserver.OnGlobalLayoutListener {

    // config params
    private InputSourceFeature[] mInputSourceFeatures;
    private int mPeakCount;
    private boolean mIsPhaseIncreaseAuto;
    private float mPhaseSpeed;

    // screen params
    private int mWidth;
    private int mCenterVerticalHeight;
    private float mHalfCycle;

    // reuseable instances
    private Path mPath = new Path();
    private Paint mPaint;
    private int[][] mPeakHeightTable;
    private int mInputCount;
    private float mPhase;
    private float mInputRatio;
    private Runnable mPeakHeightUpdater = new Runnable() {
        @Override
        public void run() {
            if (ViewCompat.isAttachedToWindow(AquaWaveView.this)) {
                updatePeakHeightTable(mInputRatio);
                if (mIsPhaseIncreaseAuto) {
                    mPhase += (mPhaseSpeed * Math.PI * 2);
                    mPhase %= (Math.PI * 2);
                }
                invalidate();
                postDelayed(this, 50);
            }
        }
    };


    public AquaWaveView(Context context) {
        this(context, null);
    }

    public AquaWaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AquaWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        initInput();
        setDecoration(createDefaultDecoration());
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    private void initInput() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setFilterBitmap(true);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    public void setDecoration(IWaveDecorator decorator) {
        if (decorator == null) {
            setDecoration(createDefaultDecoration());
        } else {
            mInputSourceFeatures = decorator.getInputSourceFeatures();
            mPeakCount = decorator.getPeakCount();
            mIsPhaseIncreaseAuto = decorator.isPhaseIncreaseAuto();
            mPhaseSpeed = decorator.getPhaseIncreaseSpeed();
            mPeakHeightTable = new int[mInputSourceFeatures.length][mPeakCount];
        }
    }

    private IWaveDecorator createDefaultDecoration() {
        return new AquaDecorator();
    }

    private void updatePeakHeightTable(float percent) {
        for (int i = 0; i < mPeakHeightTable.length; i++) {
            for (int j = 0; j < mPeakHeightTable[i].length; j++) {
                int index = j + 1 <= mPeakHeightTable[i].length / 2 ? j + 1 : mPeakHeightTable[i].length - j;
                mPeakHeightTable[i][j] = (int) ((((Math.random() * 0.8f + 0.2f) * mInputSourceFeatures[i].maxHeightRatio * mCenterVerticalHeight * 2 * index / mPeakHeightTable[i].length) * percent) + mInputSourceFeatures[i].minHeightRatio * mCenterVerticalHeight);
            }
        }
    }

    private void updateLayoutParams() {
        mWidth = getWidth();
        mCenterVerticalHeight = getHeight() / 2;
        mHalfCycle = (mWidth + 0.0f) / mPeakCount;
        updateGradients();
    }

    private void updateGradients() {
        if (mInputSourceFeatures != null) {
            for (int i = 0; i < mInputSourceFeatures.length; i++) {
                mInputSourceFeatures[i].gradient = new LinearGradient(0, 0, mWidth, 0, mInputSourceFeatures[i].colors, mInputSourceFeatures[i].positions, Shader.TileMode.MIRROR);
            }
        }
    }

    private void drawWave(Canvas canvas) {
        for (int i = 0; i < mInputSourceFeatures.length; i++) {
            mPath.reset();
            mPaint.setStrokeWidth(mInputSourceFeatures[i].strokeWidth);
            mPaint.setShader(mInputSourceFeatures[i].gradient);
            for (int x = 0; x < mWidth; x++) {
                int peakNum = calcPeakNumByX(x);
                float y = calcPointHeight(mPeakHeightTable[i][peakNum], x);

                if (x == 0) {
                    mPath.moveTo(x, mCenterVerticalHeight);
                } else if (x == mWidth) {
                    mPath.lineTo(x, mCenterVerticalHeight);
                } else {
                    mPath.lineTo(x + (i % 2) * mHalfCycle / 2, x + (i % 2) * mHalfCycle / 2 < mWidth - 1 ? y : mCenterVerticalHeight);
                }
            }
            canvas.drawPath(mPath, mPaint);
        }
    }

    private int calcPeakNumByX(int x) {
        int peakNum = (int) (((x + mHalfCycle / Math.PI * mPhase) / mHalfCycle));
        return peakNum < 0 ? mPeakCount - 1 : peakNum < mPeakCount ? peakNum : 0;
    }

    private float calcPointHeight(int amplitude, int x) {
        boolean isStart = x < (mHalfCycle * 2);
        boolean isEnd = mWidth - x < (mHalfCycle * 2);
        float amplitudeRatio = isStart ? x / (mHalfCycle * 2) : isEnd ? (mWidth - x) / (mHalfCycle * 2) : 1;
        return (float) ((amplitudeRatio * amplitude * Math.sin(x / mHalfCycle * Math.PI + mPhase)) + mCenterVerticalHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mPeakHeightTable != null) {
            drawWave(canvas);
        }
    }

    @Override
    public void onGlobalLayout() {
        updateLayoutParams();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        post(mPeakHeightUpdater);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(null);
    }

    public void input(float volume) {
        mInputRatio = volume / 25f;
    }
}