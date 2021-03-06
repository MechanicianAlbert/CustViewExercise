package com.albertech.demo.siri;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;



/**
 * A imitation of siri wave
 *
 * @Author Albert
 * @Time 20181231
 */
public class SiriWaveView extends View {

    private final Path PATH_TOP = new Path();
    private final Path PATH_BOTTOM = new Path();
    private final Runnable PHASER = new Runnable() {
        @Override
        public void run() {
            if (ViewCompat.isAttachedToWindow(SiriWaveView.this)) {
                mPhase += mPhaseSpeed;
                mPhase %= Integer.MAX_VALUE / 6;
                invalidate();
                postDelayed(this, 20);
            }
        }
    };
    private final int[] COLORS = new int[]{
            Color.argb(223, 159, 64, 64),//63
            Color.argb(223, 48, 191, 151),//127
            Color.argb(223, 0, 80, 167),//191
    };


    private Paint mPaint;
    private float mWidth;
    private float mCenterHeight;

    private SiriWaveFeature[] mFeatures;
    private float mAmplitude;
    private float mPhase;
    private float mPhaseSpeed;


    public SiriWaveView(Context context) {
        super(context);
    }

    public SiriWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SiriWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        post(PHASER);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(PHASER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mFeatures.length; i++) {
            drawAsFeature(canvas, mFeatures[i]);
        }
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(0, mCenterHeight - 0.2f, mWidth, mCenterHeight + 0.2f, mPaint);
    }


    private void init() {
        initPaint();
        initDimens();
        initFeatures();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
    }

    private void initDimens() {
        mWidth = getMeasuredWidth();
        mCenterHeight = getMeasuredHeight() / 2;
        mAmplitude = mCenterHeight / 2;
    }

    private void initFeatures() {
        mFeatures = new SiriWaveFeature[COLORS.length];

        for (int i = 0; i < COLORS.length; i++) {
            SiriWaveFeature feature = new SiriWaveFeature();
            feature.color = COLORS[i];
//            int length = (int) (Math.random() * 4 + 2);
            int length = 2;
            SiriWaveFeature.SiriWaveDimens[] dimens = new SiriWaveFeature.SiriWaveDimens[length];
//            for (int j = 0; j < length; j++) {
//                dimens[j] = new SiriWaveFeature.SiriWaveDimens((float) Math.random() + 0.3f,
//                        (float) Math.random() * 0.015f + 0.002f,
//                        0,
//                        (float) Math.random() + 2f);
//            }
            dimens[0] = new SiriWaveFeature.SiriWaveDimens((float) Math.random() + 0.3f,
                    (float) Math.random() * 0.015f + 0.004f,
                    0,
                    (float) Math.random() + 2f);
            dimens[1] = new SiriWaveFeature.SiriWaveDimens((float) Math.random() + 0.3f,
                    dimens[0].angularVelocity,
                    0,
                    -dimens[0].phaseVelocity);
            feature.dimens = dimens;
            mFeatures[i] = feature;
        }
    }


    private void drawAsFeature(Canvas canvas, SiriWaveFeature feature) {
        mPaint.setColor(feature.color);

        PATH_TOP.reset();
        PATH_BOTTOM.reset();
        PATH_TOP.moveTo(0, mCenterHeight);
        PATH_BOTTOM.moveTo(0, mCenterHeight);
        for (int x = 0; x < mWidth; x++) {
            float absY = calcVerticalOffset(x, feature.dimens);
            PATH_TOP.lineTo(x, mCenterHeight - absY);
            PATH_BOTTOM.lineTo(x, mCenterHeight + absY);
        }
        PATH_TOP.lineTo(mWidth, mCenterHeight);
        PATH_BOTTOM.lineTo(mWidth, mCenterHeight);
        PATH_TOP.close();
        PATH_BOTTOM.close();

        canvas.drawPath(PATH_TOP, mPaint);
        canvas.drawPath(PATH_BOTTOM, mPaint);
    }

    private float calcVerticalOffset(float x, SiriWaveFeature.SiriWaveDimens[] features) {
        float subSin = mAmplitude;
        for (SiriWaveFeature.SiriWaveDimens feature : features) {
            subSin += calcStandardSin(x + mPhase * feature.phaseVelocity, mAmplitude * feature.amplitude, feature.angularVelocity, feature.originalPhase);
        }
        return Math.max(subSin * calcMute(x), 0);
    }

    private float calcStandardSin(float x, float amplitude, float angularVelocity, float originalPhase) {
        return amplitude * (float) Math.sin(angularVelocity * x + originalPhase);
    }

    private float calcMute(float x) {
        return  (float) (Math.pow(Math.E, - Math.pow(x - mWidth / 2, 2) / (2 * Math.pow(mWidth / 8, 2))) / 6);
    }

    public void input(float volume) {
        float ratio = volume / 256;
        mAmplitude = ratio * mCenterHeight / 2;
        mPhaseSpeed = Math.min((float) Math.pow(ratio, 0.3), 1) * 15;
    }

}
