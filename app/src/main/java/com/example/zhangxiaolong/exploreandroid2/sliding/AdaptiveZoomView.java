package com.example.zhangxiaolong.exploreandroid2.sliding;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Scroller;

/**
 * 先不考虑自动  zoom的问题
 * <p>
 * Created by zhangxiaolong on 2017/4/26.
 */

public class AdaptiveZoomView extends View {
    private static final String TAG = AdaptiveZoomView.class.getSimpleName();

    //最初始的左边界
    private int mInitLeft = -1;
    //最初始的顶边界
    private int mInitTop = -1;
    //临界值设置为20像素吧，如果X或者Y的差值超过了这个值。则需要关闭，否则要回到原来的地方
    private static final int THRESHOLD_DIFF = 80;
    private Scroller mScroller;
    //是否在播放动画中
    private boolean mAnimating = false;
    private long postDelay = 16;//10毫秒来一次
    //应该是10dp，暂时定为30像素
    private static final float mScaleLimit = 30;
    private static final int SHORT_DURATION = 200;
    private static final int LONG_DURATION = 800;

    public AdaptiveZoomView(Context context) {
        super(context);
    }

    public AdaptiveZoomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AdaptiveZoomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AdaptiveZoomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public int getInitTop() {
        return mInitTop;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (getHeight() != 0 && getWidth() != 0) {
            if (mInitLeft < 0) {
                mInitLeft = getLeft();
            }
            if (mInitTop < 0) {
                mInitTop = getTop();
            }
        }
    }

    //手势的拖拉调用
    @Override
    public void offsetTopAndBottom(int offset) {
        if (!inAnimating()) {
            if(offset == 0){
                return;
            }
            //手势的滑动需要scale
            superOffsetTopAndBottom(offset, true);
            setPivot(BOTTOM_CENTER);
        }
    }

    private void superOffsetTopAndBottom(int offset, boolean needScale) {
        super.offsetTopAndBottom(offset);
        if(needScale && offset != 0) {
            setScale(Math.abs(getTop() - mInitTop));
        }
    }

    private void superOffsetLeftAndRight(int offset, boolean needScale) {
        super.offsetLeftAndRight(offset);
        if(needScale && offset != 0) {
            setScale(Math.abs(getLeft() - mInitLeft));
        }
    }

    private void setScale(int diff) {
        Log.e(TAG, "diff:" + diff);
        float factor;
        if (diff == 0) {
            factor = 1f;
        } else if (diff > mScaleLimit) {
            factor = 0.96f;
        } else {
            float scale = diff / mScaleLimit;
            factor = scale * 0.96f + (1 - scale) * 1;
        }
        setScaleX(factor);
        setScaleY(factor);
    }

    //播放动画的时候调用
    private void offsetTopAndBottomInner(int offset, boolean needScale) {
        superOffsetTopAndBottom(offset, needScale);
        if (mScroller != null && !mScroller.isFinished()) {
            if(needScale) {
                postDelayed(mScrollerFlingVerticalRunnable, postDelay);
            } else {
                postDelayed(mScrollerAnimationVerticalRunnable, postDelay);
            }
        } else {
            setAnimating(false);
        }
    }

    //手势的拖拉调用
    @Override
    public void offsetLeftAndRight(int offset) {
        if (!inAnimating()) {
            //手势拖拉的情况下需要scale
            if(offset == 0){
                return;
            }
            superOffsetLeftAndRight(offset, true);
            if(getLeft() > mInitLeft){
                //在右边
                setPivot(RIGHT_CENTER);
            } else {
                //在左边
                setPivot(LEFT_CENTER);
            }
        }
    }

    //播放动画的时候调用
    private void offsetLeftAndRightInner(int offset, boolean needScale) {
        superOffsetLeftAndRight(offset, needScale);
        if (mScroller != null && !mScroller.isFinished()) {
            postDelayed(mScrollerHorizontalRunnable, postDelay);
        } else {
            setAnimating(false);
        }
    }

    /**
     * 这个是自动进入动画自动退出动画的runnable，特点是不需要scale
     */
    private Runnable mScrollerAnimationVerticalRunnable = new Runnable() {
        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {
                Log.e(TAG, "原来的top: " + getTop());
                Log.e(TAG, "mScoller.getCurrY()  :" + mScroller.getCurrY());
                offsetTopAndBottomInner(mScroller.getCurrY() - getTop(), false);
                Log.e(TAG, "原来的top: " + getTop());
            } else {
                Log.e(TAG, "setAnimating(false)");
                setAnimating(false);
            }
        }
    };

    /**
     * 这个是fling残留的动画，特点是需要scale
     */
    private Runnable mScrollerFlingVerticalRunnable = new Runnable() {
        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {
                Log.e(TAG, "原来的top: " + getTop());
                Log.e(TAG, "mScoller.getCurrY()  :" + mScroller.getCurrY());
                offsetTopAndBottomInner(mScroller.getCurrY() - getTop(), true);
                Log.e(TAG, "原来的top: " + getTop());
            } else {
                Log.e(TAG, "setAnimating(false)");
                setAnimating(false);
            }
        }
    };



    private Runnable mScrollerHorizontalRunnable = new Runnable() {
        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {
                Log.e(TAG, "原来的left: " + getLeft());
                Log.e(TAG, "mScoller.getCurrX()  :" + mScroller.getCurrX());
                //左右滑动只有可能是手势滑动，所以一定要scale
                offsetLeftAndRightInner(mScroller.getCurrX() - getLeft(), true);
                Log.e(TAG, "后来的left: " + getLeft());
            } else {
                Log.e(TAG, "setAnimating(false)");
                setAnimating(false);
            }
        }
    };

    public boolean inAnimating() {
        return mAnimating;
    }

    private void setAnimating(boolean animation) {
        mAnimating = animation;
    }

    /**
     * 动画的方式开始
     */
    public void animationOpen(int parentHeight) {
        if (inAnimating()) {
            return;
        }
        //先offset到刚好消失的位置，然后再慢慢上升起来
        superOffsetLeftAndRight(mInitLeft - getLeft(), true);
        superOffsetTopAndBottom(parentHeight - getTop(), true);

        setAnimating(true);
        mScroller = new Scroller(getContext(), new MInterpolator());
        mScroller.startScroll(getLeft(), getTop(), 0, mInitTop - getTop(), LONG_DURATION);
        postDelayed(mScrollerAnimationVerticalRunnable, postDelay);
    }

    private Scroller setAndGetCommonScroller() {
        mScroller = new Scroller(getContext(), PathInterpolatorCompat.create(0.4f, 0.8f, 0.74f, 1f));
        return mScroller;
    }

    /**
     * 动画的方式结束
     */
    public void animationClose(int parentHeight) {
        if (inAnimating()) {
            return;
        }
        //先回到最开始的位置
        superOffsetLeftAndRight(mInitLeft - getLeft(), true);
        superOffsetTopAndBottom(mInitTop - getTop(), true);

        setAnimating(true);
        setAndGetCommonScroller().startScroll(getLeft(), getTop(), 0, parentHeight - getTop(), SHORT_DURATION);
        postDelayed(mScrollerAnimationVerticalRunnable, postDelay);
    }

    private static final int RIGHT_CENTER = 1;
    private static final int LEFT_CENTER = 2;
    private static final int BOTTOM_CENTER = 3;
    private int currentPivotLocation = -1;

    private void setPivot(int location){
//        if(currentPivotLocation == location){
//            return;
//        }
//        currentPivotLocation = location;
//        if(location == RIGHT_CENTER){
//            setPivotX(getWidth());
//            setPivotY(getHeight() / 2);
//        } else if(location == LEFT_CENTER){
//            setPivotX(0);
//            setPivotY(getHeight() / 2);
//        } else if(location == BOTTOM_CENTER){
//            setPivotX(getWidth() / 2);
//            setPivotY(getHeight());
//        }
    }

    public void startFling(int parentWidth, int parentHeight) {
        if (inAnimating()) {
            return;
        }

        int xDiff = Math.abs(getLeft() - mInitLeft);
        int yDiff = Math.abs(getTop() - mInitTop);
        if (xDiff > 0) {
            //水平方向进行了滑动
            if (xDiff > THRESHOLD_DIFF) {
                //水平关闭
                if (getLeft() > mInitLeft) {
                    //向右滑动关闭
                    Log.e(TAG, "向右滑动关闭");
                    setAndGetCommonScroller().startScroll(getLeft(), getTop(), parentWidth - getLeft(), 0, SHORT_DURATION);
                    setPivot(RIGHT_CENTER);
                    Log.e(TAG, "setAnimating(true)");
                    setAnimating(true);
                } else {
                    //向左滑动关闭
                    Log.e(TAG, "向左滑动关闭");
                    setAndGetCommonScroller().startScroll(getLeft(), getTop(), -getWidth() - getLeft(), SHORT_DURATION);
                    setPivot(LEFT_CENTER);
                    Log.e(TAG, "setAnimating(true)");
                    setAnimating(true);
                }
            } else {
                //水平回到原来的位置
                Log.e(TAG, "水平回到原来的位置");
                setAndGetCommonScroller().startScroll(getLeft(), getTop(), mInitLeft - getLeft(), SHORT_DURATION);
                if(getLeft() > mInitLeft){
                    //从右边退回去
                    setPivot(RIGHT_CENTER);
                } else {
                    //从左边退回去
                    setPivot(LEFT_CENTER);
                }
                Log.e(TAG, "setAnimating(true)");
                setAnimating(true);
            }

            postDelayed(mScrollerHorizontalRunnable, postDelay);
        } else if (yDiff > 0) {
            //垂直方向进行了滑动
            if (yDiff > THRESHOLD_DIFF) {
                //垂直关闭
                if (getTop() <= mInitTop) {
                    //向上滑动关闭
                    Log.e(TAG, "向上滑动关闭（没有实现）");
                } else {
                    //向下滑动关闭
                    Log.e(TAG, "向下滑动关闭");
                    setAndGetCommonScroller().startScroll(getLeft(), getTop(), 0, parentHeight - getTop(), SHORT_DURATION);
                    Log.e(TAG, "setAnimating(true)");
                    setAnimating(true);
                }
            } else {
                //垂直回到原来的位置
                Log.e(TAG, "垂直回到原来的位置");
                setAndGetCommonScroller().startScroll(getLeft(), getTop(), 0, mInitTop - getTop(), SHORT_DURATION);
                //从底部退回去
                setPivot(BOTTOM_CENTER);
                Log.e(TAG, "setAnimating(true)");
                setAnimating(true);
            }

            postDelayed(mScrollerFlingVerticalRunnable, postDelay);
        }
    }
}
