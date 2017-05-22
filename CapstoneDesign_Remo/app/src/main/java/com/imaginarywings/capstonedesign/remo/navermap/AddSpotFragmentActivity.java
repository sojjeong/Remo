package com.imaginarywings.capstonedesign.remo.navermap;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.imaginarywings.capstonedesign.remo.R;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapView;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;

/**
 * Created by S.JJ on 2017-05-22.
 * 포토스팟 등록창에서 주소 검색 시 표시 될 지도 화면 프래그먼트액티비티
 */

public class AddSpotFragmentActivity extends FragmentActivity {

    //네이버맵 클라이언트 아이디(API키)
    private final String CLIENT_ID = "xQ50GyWn_EU3eQE4A1sL";

    //현재 클래스명 얻어오기
    private final String TAG = getClass().getSimpleName();

    //네이버 맵
    public NMapView mMapView;

    //지도 위에 표시되는 오버레이 객체를 관리
    public NMapOverlayManager mOverlayManager;

    //오버레이의 리소스를 제공하기 위한 객체
    public NMapViewerResourceProvider mMapViewerResourceProvider;

    public NMapContext mMapContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragmentactivity_add_spot);

    }
}
