package com.example.zhangxiaolong.exploreandroid2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxiaolong on 2017/4/20.
 */

public class ListViewHeightActivity extends AppCompatActivity {
    private static final String TAG = ListViewHeightActivity.class.getSimpleName();

    private ListView mListView;
    private MView mView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_height_activity);
        mListView = (ListView) findViewById(R.id.m_list_view);
        mView = (MView) findViewById(R.id.my_view);

        final List<String> list = new ArrayList<>();

        for(int i = 0;i< 50;i++) {
            list.add("aaaaa");
        }

        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                    convertView = inflater.inflate(R.layout.list_item,parent,false);
                }
                TextView textView = (TextView) convertView.findViewById(R.id.text_content);
                textView.setText((String) getItem(position));
                return convertView;
            }
        });

        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mListView.getMeasuredHeight() <= 0){
                    mListView.measure(mView.mWidthMeasureSpec,mView.mHeightMeasureSpec);
                }
                Log.e(TAG,"mListView.getMeasuredHeight() : " + mListView.getMeasuredHeight());
            }
        },1000);

    }
}
