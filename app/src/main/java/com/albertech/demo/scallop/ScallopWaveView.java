package com.albertech.demo.scallop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;



/**
 * Created by Albert on 2017/2/9.
 */
public class ScallopWaveView extends View implements ViewTreeObserver.OnGlobalLayoutListener {

    // config params
    private IWaveDecorator mDecorator;
    private InputSourceFeature[] mInputSourceFeatures;
    private boolean mIsPhaseIncreaseAuto;

    // screen params
    private int mWidth;
    private int mCenterVerticalHeight;

    // input info
    private float mPeakHeight;
    private boolean mIsUpdatingPhase;

    // reuseable instances
    private Paint mPaint;
    private PointF mStartPoint = new PointF();
    private PointF mEndPoint = new PointF();
    private Runnable mPhaseUpdater = new Runnable() {
        @Override
        public void run() {
            updateInputPhase();
            invalidate();
            if (ViewCompat.isAttachedToWindow(ScallopWaveView.this)) {
                postDelayed(this, 30);
            }
        }
    };


    public ScallopWaveView(Context context) {
        this(context, null);
    }

    public ScallopWaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScallopWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setFilterBitmap(true);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    public void setDecoration(IWaveDecorator decorator) {
        if (decorator == null) {
            setDecoration(createDefaultDecoration());
        } else {
            mDecorator = decorator;
            mInputSourceFeatures = decorator.getInputSourceFeatures();
            mIsPhaseIncreaseAuto = decorator.isPhaseIncreaseAuto();
        }
    }

    private IWaveDecorator createDefaultDecoration() {
        return new ScallopDecorator();
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
            for (InputSourceFeature mInputSourceFeature : mInputSourceFeatures) {
                mInputSourceFeature.gradient = new LinearGradient(0, 0, mWidth, 0, mInputSourceFeature.colors, mInputSourceFeature.positions, Shader.TileMode.MIRROR);
            }
        }
    }

    private void checkPhaseNeedUpdate() {
        if (mIsPhaseIncreaseAuto && !mIsUpdatingPhase) {
            mIsUpdatingPhase = true;
            post(mPhaseUpdater);
        }
    }

    private void drawWave(Canvas canvas) {
        mPaint.setStrokeWidth(mWidth / 1000f);
        for (int i = 0; i < mInputSourceFeatures.length; i++) {
            mPaint.setShader(mInputSourceFeatures[i].gradient);
            for (int x = -100; x < mWidth + 100; x++) {
                if (x % (mWidth / 150) == 0) {
                    calcLinePoint(x, calcCenterY(i, x), calcPeakHeight(i), calcDynamicRotate(i, x));
                    canvas.drawLine(mStartPoint.x, mStartPoint.y, mEndPoint.x, mEndPoint.y, mPaint);
                }
            }
        }
    }

    private float calcCenterY(int inputSourceIndex, int x) {
        return mCenterVerticalHeight * (1 + mInputSourceFeatures[inputSourceIndex].baseOffeetY + mInputSourceFeatures[inputSourceIndex].cycleOffsetY * (float) Math.sin(calcDynamicRotate(inputSourceIndex, x)));
    }

    private float calcPeakHeight(int inputSourceIndex) {
        return (mPeakHeight / 100) * (0.1f * calcHalfCycle(inputSourceIndex));
    }

    private float calcHalfCycle(int inputSourceIndex) {
        return mWidth / mInputSourceFeatures[inputSourceIndex].peakCount;
    }

    private float calcDynamicRotate(int inputSourceIndex, float x) {
        return calcStaticRotate(inputSourceIndex, x + 10000 * mInputSourceFeatures[inputSourceIndex].phase);
    }

    private float calcStaticRotate(int inputSourceIndex, float x) {
        return mInputSourceFeatures[inputSourceIndex].peakCount * x / mWidth * (float) Math.PI;
    }

    private void calcLinePoint(float x, float y, float peakHeight, float rotate) {
        float offsetX = (float) Math.cos(rotate) * peakHeight;
        float offsetY = (float) Math.sin(rotate) * peakHeight;
        mStartPoint.x = x - offsetX;
        mStartPoint.y = y - offsetY;
        mEndPoint.x = x + offsetX;
        mEndPoint.y = y + offsetY;
    }

    private void updateInputPhase() {
        for (InputSourceFeature mInputSourceFeature : mInputSourceFeatures) {
            mInputSourceFeature.phase += (mInputSourceFeature.phaseSpeed);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawWave(canvas);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        setDecoration(mDecorator);
    }

    @Override
    public void onGlobalLayout() {
        onAttachedToWindowExtra();
    }

    public void input(float volume) {
        mPeakHeight = volume;
    }
}