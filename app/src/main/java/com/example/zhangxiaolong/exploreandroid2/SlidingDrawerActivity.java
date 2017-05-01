package com.example.zhangxiaolong.exploreandroid2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by zhangxiaolong on 2017/4/21.
 */

public class SlidingDrawerActivity extends AppCompatActivity {
    Button mCloseButton;
    TextView mOpenButton;
    MultiDirectionSlidingDrawer mDrawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_drawer_activity);

        mCloseButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v )
            {
                mDrawer.animateClose();
            }
        });

        mOpenButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick( View v )
            {
                if( !mDrawer.isOpened() )
                    mDrawer.animateOpen();
            }
        });
    }

    @Override
    public void onContentChanged()
    {
        super.onContentChanged();
        mCloseButton = (Button) findViewById( R.id.button_close );
        mOpenButton = (TextView) findViewById( R.id.button_open );
        mDrawer = (MultiDirectionSlidingDrawer) findViewById( R.id.drawer );
    }
}
