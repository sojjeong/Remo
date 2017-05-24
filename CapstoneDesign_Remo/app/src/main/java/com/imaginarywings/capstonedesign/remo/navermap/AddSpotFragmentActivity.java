package com.imaginarywings.capstonedesign.remo.navermap;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.imaginarywings.capstonedesign.remo.R;
import com.nhn.android.maps.NMapView;

/**
 * Created by S.JJ on 2017-05-22.
 * 포토스팟 등록창에서 주소 검색 시 표시 될 지도 화면 프래그먼트액티비티
 */

public class AddSpotFragmentActivity extends FragmentActivity {

    //현재 클래스명 얻어오기
    private final String TAG = getClass().getSimpleName();

    //네이버 맵
    public NMapView mMapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragmentactivity_add_spot);

        mMapView = (NMapView)findViewById(R.id.id_AddSpotMapView);

        // initialize map view
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();
    }
}
