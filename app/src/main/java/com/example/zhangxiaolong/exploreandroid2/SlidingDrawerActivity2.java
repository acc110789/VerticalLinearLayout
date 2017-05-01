package com.example.zhangxiaolong.exploreandroid2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.example.zhangxiaolong.exploreandroid2.sliding.AdaptiveZoomView;

/**
 * Created by zhangxiaolong on 2017/4/25.
 */

public class SlidingDrawerActivity2 extends AppCompatActivity {
    private static final int LAYOUT_ID = R.id.layout_id_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_drawer_activity_2);

        View tmp = findViewById(LAYOUT_ID);
        tmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG","bb");
            }
        });

        final View parent = findViewById(R.id.sliding_parent);

        final AdaptiveZoomView view = (AdaptiveZoomView) findViewById(R.id.clickable_view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("aaa","click");
            }
        });

        View view1 = findViewById(R.id.click_to_reset);
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.requestLayout();
                view.invalidate();
            }
        });

        final View view2 = findViewById(R.id.click_to_show_animation);
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);

                //先让其消失,一秒之后播放出现的动画
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.VISIBLE);
                        final ViewTreeObserver observer = view.getViewTreeObserver();
                        ViewTreeObserver.OnPreDrawListener listener = new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                //当前这一帧
                                observer.removeOnPreDrawListener(this);
                                view.animationOpen(parent.getHeight());
                                return false;
                            }
                        };
                        observer.addOnPreDrawListener(listener);
                    }
                },300L);
            }
        });

        View view3 = findViewById(R.id.click_to_close_animation);
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.animationClose(parent.getHeight());
                    }
                },300L);
            }
        });
    }
}
