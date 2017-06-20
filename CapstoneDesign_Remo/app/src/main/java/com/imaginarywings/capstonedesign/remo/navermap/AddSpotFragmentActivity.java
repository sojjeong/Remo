package com.imaginarywings.capstonedesign.remo.navermap;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.imaginarywings.capstonedesign.remo.R;
import com.nhn.android.maps.maplib.NGeoPoint;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by S.JJ on 2017-05-22.
 * 포토스팟 등록창에서 주소 검색 시 표시 될 지도 화면 프래그먼트액티비티
 */

public class AddSpotFragmentActivity extends FragmentActivity {

    //현재 클래스명 얻어오기
    private final String TAG = getClass().getSimpleName();

    public LocationManager locationManager;

    @BindView(R.id.id_CenterAddress) TextView text_CenterAddress;
    @BindView(R.id.id_btnSelectAddress) Button btn_SelectAddress;

    public AddSpotFragment AddSpotFragment;

    public static Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmentactivity_add_spot);

        mContext = this;

        ButterKnife.bind(AddSpotFragmentActivity.this);

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.id_btnSelectAddress)
    public void selectAddress()
    {
        if(text_CenterAddress.getText() != null)
        {
            //마커가 위치한 곳의 주소 가져오기
            String centerAddress = String.valueOf(text_CenterAddress.getText());
            ((AddSpotActivity)AddSpotActivity.mContext).mtext_SpotAddress.setText(centerAddress);

            //마커에 해당하는 위,경도 가져오기
            AddSpotFragment = (AddSpotFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_add_spot);
            NGeoPoint Point = AddSpotFragment.mCenterAddress;

            //서버에 저장하기위한 위,경도값을 포토스팟 등록창으로 전달
            ((AddSpotActivity)AddSpotActivity.mContext).mSavePoint = Point;

            //확인
            String latitude = String.valueOf(Point.getLatitude());
            String longitude = String.valueOf(Point.getLongitude());
            Log.e(TAG, latitude);
            Log.e(TAG, longitude);

            //이전 액티비티로 돌아가기 위해 finish
            finish();
        }
    }
}
