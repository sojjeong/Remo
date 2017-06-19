package com.imaginarywings.capstonedesign.remo;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2017-06-19.
 */

public class TutorialActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        ViewPager viewPager = (ViewPager) findViewById(R.id.tutorial);
        viewPager.setAdapter(new CustomPagerAdapter(this));
    }
}