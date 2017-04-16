package com.example.zhangxiaolong.exploreandroid2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 不考虑weight的LinearLayout
 * Created by zhangxiaolong on 2017/4/9.
 */

public class VerticalLinearLayout extends ViewGroup {

    public VerticalLinearLayout(Context context) {
        super(context);
    }

    public VerticalLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public VerticalLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = 0;
        int usedHeight = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child == null || child.getVisibility() == View.GONE) {
                continue;
            }
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, usedHeight);

            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
            usedHeight += child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
        }
        int totalWidth = maxWidth + getPaddingLeft() + getPaddingRight();
        totalWidth = Math.max(totalWidth, getSuggestedMinimumWidth());

        int totalHeight = usedHeight + getPaddingBottom() + getPaddingTop();
        totalHeight = Math.max(totalHeight, getSuggestedMinimumHeight());

        int widthSizeAndState = resolveSizeAndState(totalWidth, widthMeasureSpec, 0);
        int heightSizeAndState = resolveSizeAndState(totalHeight, heightMeasureSpec, 0);
        setMeasuredDimension(widthSizeAndState, heightSizeAndState);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childTop = getPaddingTop();

        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child == null || child.getVisibility() == View.GONE) {
                continue;
            }
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            childTop += lp.topMargin;

            int childLeft = lp.leftMargin + getPaddingLeft();

            child.layout(childLeft, childTop, childLeft + child.getMeasuredWidth(), childTop + child.getMeasuredHeight());

            childTop += child.getMeasuredHeight() + lp.bottomMargin;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        LayoutParams lp = super.generateDefaultLayoutParams();
        return this.generateLayoutParams(lp);
    }
}
