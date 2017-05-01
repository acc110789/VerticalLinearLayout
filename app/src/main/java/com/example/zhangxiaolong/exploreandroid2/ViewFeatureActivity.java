package com.example.zhangxiaolong.exploreandroid2;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by zhangxiaolong on 2017/5/1.
 */

public class ViewFeatureActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_feature);

        final View back = findViewById(R.id.back_view);
        final View target = findViewById(R.id.target_view);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Rect rect = new Rect();
                rect.set(back.getLeft(),back.getTop(),back.getRight(),back.getBottom());
                log("back",rect);

                rect.set(target.getLeft(),target.getTop(),target.getRight(),target.getBottom());
                log("target",rect);

                target.requestLayout();
                target.invalidate();
            }
        },1000);
    }

    private void log(String tag,Rect rect){
        Log.e(tag,"left :" + rect.left);
        Log.e(tag,"top :" + rect.top);
        Log.e(tag,"right :" + rect.right);
        Log.e(tag,"bottom :" + rect.bottom);
    }
}
