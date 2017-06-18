package com.imaginarywings.capstonedesign.remo.navermap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.imaginarywings.capstonedesign.remo.R;
import com.koushikdutta.async.http.body.Part;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-06-18.
 */

public class MyPhotospotActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myphotospot);

        // 서버에서 받아오는 데이터
        List<Part> parts = new ArrayList<>();

        // UUID
        String uuid = ((FragmentMapActivity) FragmentMapActivity.mContext).mUUID;

    }

}
