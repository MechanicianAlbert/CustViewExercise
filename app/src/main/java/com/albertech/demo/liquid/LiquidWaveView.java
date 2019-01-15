package com.albertech.demo.liquid;

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
public class LiquidWaveView extends View implements ViewTreeObserver.OnGlobalLayoutListener {

    // config params
    private InputSourceFeature[] mInputSourceFeatures;
    private boolean mIsPhaseIncreaseAuto;

    // screen params
    private int mWidth;
    private int mCenterVerticalHeight;

    // input info
    private float mPeakHeight = 15;

    // reuseable instances
    private Paint mPaint;

    // anim
    private boolean mPhaseShifting;
    private Runnable mPhaseUpdater = new Runnable() {
        @Override
        public void run() {
            updateInputPhase();
            invalidate();
            if (ViewCompat.isAttachedToWindow(LiquidWaveView.this) && mPhaseShifting) {
                postDelayed(this, 10);
            }
        }
    };


    public LiquidWaveView(Context context) {
        this(context, null);
    }

    public LiquidWaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LiquidWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        initInput();
        setDecoration(null);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    private void initInput() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setFilterBitmap(true);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    public void setDecoration(IWaveDecorator decorator) {
        if (decorator == null) {
            setDecoration(createDefaultDecoration());
        } else {
            mInputSourceFeatures = decorator.getInputSourceFeatures();
            mIsPhaseIncreaseAuto = decorator.isPhaseIncreaseAuto();
        }
    }

    private IWaveDecorator createDefaultDecoration() {
        return new LiquidDecorator();
    }

    private void onAttachedToWindowExtra() {
        updateLayoutParams();
        updateGradients();
        checkPhaseNeedUpdate();
    }

    private void updateLayoutParams() {
        mWidth = getWidth();
        mCenterVerticalHeight = getHeight() / 2;
    }

    private void updateGradients() {
        if (mInputSourceFeatures != null) {
            for (int i = 0; i < mInputSourceFeatures.length; i++) {
                mInputSourceFeatures[i].gradient = new LinearGradient(0, 0, mWidth, 0, mInputSourceFeatures[i].colors, mInputSourceFeatures[i].positions, Shader.TileMode.MIRROR);
            }
        }
    }

    private void checkPhaseNeedUpdate() {
        if (mIsPhaseIncreaseAuto) {
            post(mPhaseUpdater);
        }
    }

    private Path mPath = new Path();

    private void drawWave(Canvas canvas) {
        for (int i = 0; i < mInputSourceFeatures.length; i++) {
            mPaint.setShader(mInputSourceFeatures[i].gradient);
            mPath.reset();
            mPath.moveTo(0, calcY(0, i));
            for (int x = 0; x < mWidth; x++) {
                if (x % 4 == 0) {
                    mPath.lineTo(x, calcY(x, i));
                }
            }
            mPath.lineTo(mWidth, 2 * mCenterVerticalHeight);
            mPath.lineTo(0, 2 * mCenterVerticalHeight);
            mPath.close();
            canvas.drawPath(mPath, mPaint);
        }
    }

    private float calcY(float x, int index) {
        InputSourceFeature isf = mInputSourceFeatures[index];
        float y = (float) (mPeakHeight * Math.sin(isf.peakCount * 0.01f * (x + isf.phase) + isf.cycleOffsetY) + (1.5f * mCenterVerticalHeight) + isf.baseOffeetY);
        return y;
    }

    private void updateInputPhase() {
        for (int i = 0; i < mInputSourceFeatures.length; i++) {
            mInputSourceFeatures[i].phase += (mInputSourceFeatures[i].phaseSpeed);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawWave(canvas);
    }

    @Override
    public void onGlobalLayout() {
        onAttachedToWindowExtra();
    }

    public void startPhaseShifting() {
        mPhaseShifting = true;
        post(mPhaseUpdater);
    }

    public void stopPhaseShifting() {
        mPhaseShifting = false;
    }

    public void input(float volume) {
        float nextHeight = volume / 512f * mCenterVerticalHeight;
        mPeakHeight = Math.min(Math.max(15, nextHeight), mCenterVerticalHeight);
    }
}