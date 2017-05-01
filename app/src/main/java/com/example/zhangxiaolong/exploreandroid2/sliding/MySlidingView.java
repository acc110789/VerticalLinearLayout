package com.example.zhangxiaolong.exploreandroid2.sliding;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.example.zhangxiaolong.exploreandroid2.R;

/**
 * Created by zhangxiaolong on 2017/4/25.
 */

public class MySlidingView extends FrameLayout{

    private static final String TAG = MySlidingView.class.getSimpleName();

    private ViewDragHelper mDragHelper;
    private static final int VERTICAL = 0;
    private static final int HORIZONTAL = 1;
    private int mDirection = HORIZONTAL;

    public MySlidingView(Context context) {
        super(context);
        init();
    }

    public MySlidingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MySlidingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MySlidingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        if(mDragHelper == null){
            mDragHelper = ViewDragHelper.create(this,1f,new MSlidingViewCallback());
        }
    }

    private boolean mIntercepted = false;
    private float mActionDownX = 0;
    private float mActionDownY = 0;
    //是否应该忽略本次触摸事件，如果在actionDown的时候
    // 发现目标View正在播放动画，则应当忽略本次事件
    private boolean mIgnoreCurrentMotionEvents = false;


    private void reset(MotionEvent ev){
        mIntercepted = false;
        mActionDownX = ev.getX();
        mActionDownY = ev.getY();
        mIgnoreCurrentMotionEvents = false;
        AdaptiveZoomView child = getAdativeZoomChild();
        if(child != null) {
            mIgnoreCurrentMotionEvents = child.inAnimating();
        }
    }

    private void computeDirection(MotionEvent ev){
        float diffX = Math.abs(ev.getX() - mActionDownX);
        float diffY = Math.abs(ev.getY() - mActionDownY);
        if(diffX >= diffY){
            mDirection = HORIZONTAL;
        } else {
            mDirection = VERTICAL;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getActionMasked();
        if(action == MotionEvent.ACTION_DOWN){
            reset(ev);
        }
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }
        boolean intercept = mDragHelper.shouldInterceptTouchEvent(ev);
        if(intercept && !mIntercepted){
            mIntercepted = true;
            //计算方向,水平滑动或者垂直滑动
            computeDirection(ev);
        }
        return intercept;
    }

    private AdaptiveZoomView getAdativeZoomChild(){
        View result = findViewById(R.id.clickable_view);
        return (AdaptiveZoomView) result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(!mIgnoreCurrentMotionEvents) {
            mDragHelper.processTouchEvent(ev);
            if (ev.getActionMasked() == MotionEvent.ACTION_UP) {
                AdaptiveZoomView child = getAdativeZoomChild();
                if (child != null) {
                    //这里先不考虑滑动的速度，实际上如果滑动的速度超过了一个临界值，也是需要消失的
                    Log.e(TAG, "child startFling");
                    child.startFling(getWidth(), getHeight());
                }
            }
        }
        return true;
    }

    public class MSlidingViewCallback extends ViewDragHelper.Callback {

        @Override
        public int getViewHorizontalDragRange(View child) {
            return 10;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return 10;
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            AdaptiveZoomView adaptiveZoomView;
            if(child instanceof AdaptiveZoomView){
                adaptiveZoomView = (AdaptiveZoomView) child;
                return adaptiveZoomView.getId() == R.id.clickable_view &&
                        !adaptiveZoomView.inAnimating();
            }
            return false;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if(child instanceof AdaptiveZoomView && mDirection == HORIZONTAL) {
                return left;
            } else {
                //其它情况下不变
                return child.getLeft();
            }
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if(child instanceof AdaptiveZoomView && mDirection == VERTICAL) {
                AdaptiveZoomView adaptiveZoomView = (AdaptiveZoomView) child;
                if(top <= adaptiveZoomView.getInitTop()){
                    //不能向上滑动
                    return adaptiveZoomView.getInitTop();
                } else {
                    return top;
                }
            } else {
                return child.getTop();
            }
        }
    }
}
