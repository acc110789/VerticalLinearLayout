package com.example.zhangxiaolong.exploreandroid2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        TextView tx = (TextView) findViewById(R.id.test_url);
//        Pattern pattern = Patterns.WEB_URL;
//        String url = "<a href=\"www.baidu.com\">喔喔喔</a>";
//
//        Matcher matcher = pattern.matcher(url);
    }
}
