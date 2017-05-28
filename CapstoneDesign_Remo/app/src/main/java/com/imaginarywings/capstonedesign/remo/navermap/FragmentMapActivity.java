/*
 * Copyright 2016 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.imaginarywings.capstonedesign.remo.navermap;


import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SearchView;
import android.widget.Toast;

import com.imaginarywings.capstonedesign.remo.R;
import com.nhn.android.maps.maplib.NGeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * FragmentActivity extends Activity(최상위)
 * 프래그먼트 지도를 표시해 줄 프래그먼트 전용 액티비티
 */

public class FragmentMapActivity extends FragmentActivity {

    private final String TAG = getClass().getSimpleName();
    private final String CLIENT_ID = "xQ50GyWn_EU3eQE4A1sL";
    private final String CLIENT_SCERET = "3dKV3kxpZb";

    Animation FabOpen, FabClose, FabRClockwise, FabRanticlockWise;
    private FloatingActionButton fabMain, fabAddSpot, fabMySpot, fabMylocation;
    boolean isOpen = false;

    public Fragment1 PhotospotMap;

    //다른 액티비티에서 FragmentMapActivity 함수를 사용하기 위한 스태틱 변수
    public static Context mContext;

    public double latitude;     //위도
    public double longitude;    //경도

    //포토스팟 검색 SearchView
    @BindView(R.id.id_SpotSearch) SearchView mSearchView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.framents);

        mContext = this;

        ButterKnife.bind(FragmentMapActivity.this);

        //------------------------------------------------------------
        Fragment1 fragment1 = new Fragment1();
        fragment1.setArguments(new Bundle());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment1, fragment1);
        //------------------------------------------------------------

        //fab 애니메이션
        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabRClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        FabRanticlockWise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

        //id 연결
        fabMain = (FloatingActionButton)findViewById(R.id.fab_main);
        fabAddSpot = (FloatingActionButton)findViewById(R.id.fab_addspot);
        fabMySpot = (FloatingActionButton)findViewById(R.id.fab_myspot);
        fabMylocation = (FloatingActionButton)findViewById(R.id.fab_mylocation);

        //리스너 설치
        fabMain.setOnClickListener(clickListener);
        fabAddSpot.setOnClickListener(clickListener);
        fabMySpot.setOnClickListener(clickListener);
        fabMylocation.setOnClickListener(clickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //프래그먼트1에 있는 함수 호출을 위해 먼저 생성함.
        PhotospotMap = (Fragment1) getSupportFragmentManager().findFragmentById(R.id.fragment1);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    /**
     * 포토스팟 지도 검색 창 버튼 클릭
     */
    @OnClick(R.id.btn_MapSearch)
    public void btnSearchClick(){

        mSearchView.clearFocus();
        String Query = mSearchView.getQuery().toString();

        if(Query != null)
        {
            NGeoPoint point = ConvertLatLng(Query);

            if(point != null) {
                PhotospotMap.mMapController.animateTo(point);
            }
            else
            {
                Toast.makeText(mContext, "검색한 위치를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        //현재 위치 탐색 종료
        PhotospotMap.mMapLocationManager.disableMyLocation();
    }

    //fab 애니메이션 및 이벤트
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId())
            {
                case R.id.fab_main :
                    if (!isOpen)
                    {
                        fabMySpot.startAnimation(FabOpen);
                        fabAddSpot.startAnimation(FabOpen);
                        fabMylocation.startAnimation(FabOpen);

                        fabMain.startAnimation(FabRClockwise);

                        fabMySpot.setClickable(true);
                        fabAddSpot.setClickable(true);
                        fabMylocation.setClickable(true);
                        isOpen = true;
                    }
                    else
                    {
                        fabMySpot.startAnimation(FabClose);
                        fabAddSpot.startAnimation(FabClose);
                        fabMylocation.startAnimation(FabClose);

                        fabMain.startAnimation(FabRanticlockWise);

                        fabMySpot.setClickable(false);
                        fabAddSpot.setClickable(false);
                        fabMylocation.setClickable(false);
                        isOpen = false;
                    }

                    break;

                case R.id.fab_addspot :
                {
                    //Toast.makeText(FragmentMapActivity.this, "포토스팟 등록창", Toast.LENGTH_SHORT).show();
                    Intent intent_addspot = new Intent(getApplicationContext(), AddSpotActivity.class);
                    startActivity(intent_addspot);

                    break;
                }

                case R.id.fab_mylocation :
                {
                    //Toast.makeText(FragmentMapActivity.this, "현재 위치", Toast.LENGTH_SHORT).show();
                    PhotospotMap.startMyLocation();

                    //확대나 축소 했을 경우 이 버튼을 눌렀을 시 축척을 알맞은 값으로 재 설정 해줘야함 - 함수 찾아보기
                    NGeoPoint point = PhotospotMap.checkMyLocationInfo();

                    if(point != null) {
                        longitude = point.getLongitude();
                        latitude = point.getLatitude();

                        String Address = ConvertAddress(mContext, latitude, longitude);
                        Toast.makeText(getApplicationContext(), Address, Toast.LENGTH_SHORT).show();
                    }

                    break;
                }
            }
        }
    };

    //위도 경도 반환 함수
    public NGeoPoint getAddress()
    {
        NGeoPoint Point = PhotospotMap.checkMyLocationInfo();
        return Point;
    }

    //위도, 경도를 주소로 변환하는 함수(Geocorder)
    public static String ConvertAddress(Context mContext, double latitude, double longitude)
    {
        String nowAddress = "";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);

        List<Address> addressList = null;
        try{
            if (geocoder != null)
            {
                addressList = geocoder.getFromLocation(latitude, longitude, 1);

                if(addressList != null && addressList.size() > 0)
                {
                    String currentLocationAddress = addressList.get(0).getAddressLine(0).toString();
                    nowAddress = currentLocationAddress;
                }
            }
        }
        catch( IOException e )
        {
            Toast.makeText(mContext, "주소를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }

        return nowAddress;
    }

    //주소를 위도, 경도로 변환하는 함수(Geocorder)
    public NGeoPoint ConvertLatLng(String location) {

        if(location == null)
            return null;

        Geocoder geocorder = new Geocoder(mContext, Locale.KOREA);
        Address addr;
        NGeoPoint point = null;

        try{
            List<Address> list = geocorder.getFromLocationName(location, 1);

            if(list.size() > 0)
            {
                addr = list.get(0);
                double lat = addr.getLatitude();
                double lng = addr.getLongitude();

                //Ngeopoint 생성은 경도, 위도 순으로 넣는다.
                point = new NGeoPoint(lng, lat);

                Log.d(TAG, "주소로부터 취득한 위도 : " + lat + "," +  "경도 : " + lng);
            }
        }
        catch(IOException e)
        {
            Toast.makeText(mContext, "주소를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return point;
    }
}
