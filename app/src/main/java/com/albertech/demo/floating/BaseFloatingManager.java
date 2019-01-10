package com.albertech.demo.floating;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;




public abstract class BaseFloatingManager implements OnTouchListener, OnAttachStateChangeListener {

    protected final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Runnable SHOW = new Runnable() {
        @Override
        public void run() {
            try {
                if (!ViewCompat.isAttachedToWindow(mRoot) && mWm != null) {
                    updateDisplayParams();
                    mLp.x = mTargetX == -100 ? (int) (getMaxX() * mDefaultPosition[0]) : mTargetX;
                    mLp.y = mTargetY == -100 ? (int) (getMaxY() * mDefaultPosition[1]) : mTargetY;
                    mWm.addView(mRoot, mLp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable DISSMISS = new Runnable() {
        @Override
        public void run() {
            try {
                if (mWm != null && mRoot != null && mRoot.getParent() != null) {
                    interruptAttractAnim();
                    mWm.removeView(mRoot);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable MOVE = new Runnable() {
        @Override
        public void run() {
            if (mWm != null) {
                mWm.updateViewLayout(mRoot, mLp);
            }
        }
    };


    private int mClickDuration;
    private int mAttractDuration;
    private boolean mHorizontalAttractable;
    private boolean mVerticalAttractable;
    private float[] mDefaultPosition = new float[2];


    private Context mContext;
    private WindowManager mWm;
    private WindowManager.LayoutParams mLp;
    private ViewGroup mRoot;

    private int mScreenWidth;
    private int mScreenHeight;
    private int mStatusBarHeight;
    private int mTargetX = -100;
    private int mTargetY = -100;
    private int mLastMoveX;
    private int mLastMoveY;
    private int mTouchUpX;
    private int mTouchUpY;
    private long mDownTime;
    private ValueAnimator mAttractAnimator;
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            executeAttract((Float) valueAnimator.getAnimatedValue());
        }
    };



    public BaseFloatingManager(Context context) {
        this(context, 150, 800, true, false, 1f, 0.6f);
    }

    public BaseFloatingManager(Context context, int clickDuration, int attractDuration, boolean horizontalAttractable, boolean verticalAttractable, float defaultX, float defaultY) {
        mContext = context;
        mClickDuration = clickDuration;
        mAttractDuration = attractDuration;
        mHorizontalAttractable = horizontalAttractable;
        mVerticalAttractable = verticalAttractable;
        mDefaultPosition[0] = defaultX;
        mDefaultPosition[1] = defaultY;
        initAttractAnim();
        initWindow();
        updateRootView(onCreateRootView(mContext));
    }

    private void initWindow() {
        mWm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mLp = new LayoutParams();
        mLp.type = LayoutParams.TYPE_PHONE;
        mLp.format = PixelFormat.RGBA_8888;
        mLp.width = LayoutParams.WRAP_CONTENT;
        mLp.height = LayoutParams.WRAP_CONTENT;
        mLp.gravity = Gravity.LEFT | Gravity.TOP;
        mLp.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
    }

    private void initAttractAnim() {
        mAttractAnimator = ValueAnimator.ofFloat(0, 1);
        mAttractAnimator.setDuration(mAttractDuration);
        mAttractAnimator.addUpdateListener(mAnimatorUpdateListener);
    }

    private void executeAttract(float ratio) {
        try {
            mLp.x = (int) (ratio * (mTargetX - mTouchUpX)) + mTouchUpX;
            mLp.y = (int) (ratio * (mTargetY - mTouchUpY)) + mTouchUpY;
            mWm.updateViewLayout(mRoot, mLp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void updateDisplayParams() {
        if (mWm != null) {
            DisplayMetrics dm = new DisplayMetrics();
            mWm.getDefaultDisplay().getMetrics(dm);
            mScreenWidth = dm.widthPixels;
            mScreenHeight = dm.heightPixels;
            int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
            mStatusBarHeight = resourceId > 0 ? mContext.getResources().getDimensionPixelSize(resourceId) : 0;
        }
    }

    private void updateCurrentPositionAndTargetPosition(int diffX, int diffY) {
        if (mWm != null) {
            mLp.x += diffX;
            mLp.y += diffY;
            mWm.updateViewLayout(mRoot, mLp);
            mTargetX = calcTargetX();
            mTargetY = calcTargetY();
        }
    }

    private int getCurrentCenterX() {
        return mLp.x + mRoot.getMeasuredWidth() / 2;
    }

    private int getCurrentCenterY() {
        return mLp.y + mRoot.getMeasuredHeight() / 2;
    }

    private boolean nearLeft() {
        return getCurrentCenterX() < mScreenWidth / 2;
    }

    private boolean nearTop() {
        return getCurrentCenterY() < mScreenHeight / 2 + mStatusBarHeight;
    }

    private int getMaxX() {
        return mScreenWidth - mRoot.getMeasuredWidth();
    }

    private int getMaxY() {
        return mScreenHeight - mStatusBarHeight - mRoot.getMeasuredHeight();
    }

    private int calcTargetX() {
        return mHorizontalAttractable ? (nearLeft() ? 0 : getMaxX()) : mLp.x;
    }

    private int calcTargetY() {
        return mVerticalAttractable ? (nearTop() ? 0 : getMaxY()) : mLp.y;
    }

    private void interruptAttractAnim() {
        if (mAttractAnimator != null) {
            mAttractAnimator.cancel();
        }
    }

    @Override
    public boolean onTouch(final View view, MotionEvent motionEvent) {
        try {
            int x = (int) (motionEvent.getRawX());
            int y = (int) (motionEvent.getRawY());
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    interruptAttractAnim();
                    mDownTime = System.currentTimeMillis();
                    mLastMoveX = x;
                    mLastMoveY = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    updateCurrentPositionAndTargetPosition(x - mLastMoveX, y - mLastMoveY);
                    mLastMoveX = x;
                    mLastMoveY = y;
                    break;
                case MotionEvent.ACTION_UP:
                    try {
                        if (System.currentTimeMillis() - mDownTime < mClickDuration) {
                            onRootClick(mRoot);
                        }
                        mTouchUpX = mLp.x;
                        mTouchUpY = mLp.y;
                        mAttractAnimator.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
            return System.currentTimeMillis() - mDownTime < mClickDuration;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onViewAttachedToWindow(View v) {
        onShow(v);
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        onDismiss(v);
    }

    public void updateRootView(@NonNull ViewGroup root) {
        if (mRoot != null) {
            mRoot.setOnTouchListener(null);
            mRoot.removeOnAttachStateChangeListener(this);
            dismiss();
        }
        mRoot = root;
        mRoot.measure(0, 0);
        mRoot.setOnTouchListener(this);
        mRoot.addOnAttachStateChangeListener(this);
    }

    public void show() {
        mHandler.post(SHOW);
    }

    public void dismiss() {
        mHandler.post(DISSMISS);
    }

    public void moveBy(int x, int y) {
        if (ViewCompat.isAttachedToWindow(mRoot)) {
            mLp.x += x;
            mLp.y += y;
            mLp.x = Math.min(Math.max(0, mLp.x), mScreenWidth - mRoot.getMeasuredWidth() / 2);
            mLp.y = Math.min(Math.max(0, mLp.y), mScreenHeight - mRoot.getMeasuredHeight() / 2);
            mHandler.post(MOVE);
        }
    }

    public void moveTo(int x, int y) {
        if (ViewCompat.isAttachedToWindow(mRoot)) {
            mLp.x = Math.min(Math.max(0, x), mScreenWidth - mRoot.getMeasuredWidth() / 2);
            mLp.y = Math.min(Math.max(0, y), mScreenHeight - mRoot.getMeasuredHeight() / 2);
            mHandler.post(MOVE);
        }
    }

    protected Context getContext() {
        return mContext;
    }

    protected void onRootClick(View root) {

    }

    protected void onShow(View root) {

    }

    protected void onDismiss(View root) {

    }

    protected abstract ViewGroup onCreateRootView(Context context);

}
