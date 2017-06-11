package com.imaginarywings.capstonedesign.remo.navermap;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.imaginarywings.capstonedesign.remo.R;
import com.nhn.android.maps.maplib.NGeoPoint;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by S.JJ on 2017-05-22.
 * 포토스팟 등록창에서 주소 검색 시 표시 될 지도 화면 프래그먼트액티비티
 */

public class AddSpotFragmentActivity extends FragmentActivity {

    //현재 클래스명 얻어오기
    private final String TAG = getClass().getSimpleName();

    public LocationManager locationManager;

    @BindView(R.id.id_CenterAddress) TextView text_CenterAddress;

    public AddSpotFragment AddSpotFragment;

    public static Context mContext;

    public double latitude;     //위도
    public double longitude;    //경도

    public String mCenterAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmentactivity_add_spot);

        mContext = this;

        ButterKnife.bind(AddSpotFragmentActivity.this);

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
