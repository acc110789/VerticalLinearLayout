package com.example.zhangxiaolong.exploreandroid2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zhangxiaolong on 2017/4/19.
 */

public class GestureActivity extends AppCompatActivity {
    private static final String TAG = GestureActivity.class.getSimpleName();

    private View mSlideView;
    private GestureDetector mDetector;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gesture_activity);

        mSlideView = findViewById(R.id.slide_view);
        mDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                log("onDown");
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                log("onShowPress");
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                log("onSingleTapUp");
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                log("onScroll");
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                log("onLongPress");
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                log("onFling");
                return false;
            }
        });

        mDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                log("onSingleTapConfirmed");
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                log("onDoubleTap");
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                log("onDoubleTapEvent");
                return false;
            }
        });

        mSlideView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    private void log(String content){
        Log.e(TAG,content);
    }

}
