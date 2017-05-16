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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.imaginarywings.capstonedesign.remo.AddSpotActivity;
import com.imaginarywings.capstonedesign.remo.R;
import com.nhn.android.maps.maplib.NGeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * FragmentActivity extends Activity(최상위)
 * 프래그먼트 지도를 표시해 줄 프래그먼트 전용 액티비티
 */

public class FragmentMapActivity extends FragmentActivity {

    Animation FabOpen, FabClose, FabRClockwise, FabRanticlockWise;
    private FloatingActionButton fabMain, fabAddSpot, fabMySpot, fabMylocation;
    boolean isOpen = false;

    public Fragment1 PhotospotMap;

    //다른 액티비티에서 FragmentMapActivity 함수를 사용하기 위한 스태틱 변수
    public static Context mContext;

    public double longitude;
    public double latitude;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.framents);

        //------------------------------------------------------------
        //2017.05.17. 00:12 테스트 - 동작 완료
        Fragment1 fragment1 = new Fragment1();
        fragment1.setArguments(new Bundle());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment1, fragment1);
        //------------------------------------------------------------

        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabRClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        FabRanticlockWise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

        fabMain = (FloatingActionButton)findViewById(R.id.fab_main);
        fabAddSpot = (FloatingActionButton)findViewById(R.id.fab_addspot);
        fabMySpot = (FloatingActionButton)findViewById(R.id.fab_myspot);
        fabMylocation = (FloatingActionButton)findViewById(R.id.fab_mylocation);

        fabMain.setOnClickListener(clickListener);
        fabAddSpot.setOnClickListener(clickListener);
        fabMySpot.setOnClickListener(clickListener);
        fabMylocation.setOnClickListener(clickListener);

        mContext = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId())
            {
                case R.id.fab_main :
                    if (!isOpen)
                    {
                        //프래그먼트1에 있는 함수 호출을 위해 먼저 생성함.
                        PhotospotMap = (Fragment1) getSupportFragmentManager().findFragmentById(R.id.fragment1);

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
                    Toast.makeText(FragmentMapActivity.this, "포토스팟 등록창", Toast.LENGTH_SHORT).show();
                    Intent intent_addspot = new Intent(getApplicationContext(), AddSpotActivity.class);
                    startActivity(intent_addspot);

                    break;
                }

                case R.id.fab_mylocation :
                {
                    Toast.makeText(FragmentMapActivity.this, "현재 위치", Toast.LENGTH_SHORT).show();
                    PhotospotMap.startMyLocation();

                    //축척 재 설정 해줘야함 - 함수 찾아보기

                    NGeoPoint address = PhotospotMap.checkMyLocationInfo();

                    longitude = address.getLongitude();
                    latitude = address.getLatitude();

                    String Address = CovertAddress(mContext, latitude, longitude);
                    Toast.makeText(getApplicationContext(), Address, Toast.LENGTH_SHORT).show();

                    break;
                }
            }
        }
    };

    //위도, 경도를 주소로 변환하는 함수 - Geocorder 이용
    public static String CovertAddress(Context mContext, double latitude, double longitude)
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

    //위도 경도 반환 함수
    public NGeoPoint getAddress()
    {
        NGeoPoint Point = PhotospotMap.checkMyLocationInfo();
        return Point;
    }
}
