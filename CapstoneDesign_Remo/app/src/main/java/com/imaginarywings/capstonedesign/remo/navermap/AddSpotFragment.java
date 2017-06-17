package com.imaginarywings.capstonedesign.remo.navermap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.imaginarywings.capstonedesign.remo.R;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import butterknife.ButterKnife;

/**
 * Created by S.JJ on 2017-05-22.
 * 포토스팟 등록창에서 주소 검색 후 상세 위치 추가를 위한 프래그먼트창
 */

public class AddSpotFragment extends NMapFragment {

    //네이버맵 클라이언트 아이디(API키)
    private final String CLIENT_ID = "xQ50GyWn_EU3eQE4A1sL";
    private static final boolean DEBUG = false;
    private static final String LOG_TAG = "AddSpotFragment";

    //현재 클래스명 얻어오기
    private final String TAG = getClass().getSimpleName();

    //네이버 맵
    public NMapView mMapView;

    //맵 컨트롤러
    public NMapController mMapController;

    //지도 위에 표시되는 오버레이 객체를 관리
    public NMapOverlayManager mOverlayManager;

    //오버레이의 리소스를 제공하기 위한 객체
    public NMapViewerResourceProvider mMapViewerResourceProvider;

    public NMapContext mMapContext;

    //지도 화면 중심의 위,경도
    public NGeoPoint mCenterAddress;
    public String mStringCenterAddress;

    public static Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMapContext = new NMapContext(super.getActivity());
        mMapContext.onCreate();

        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_spot, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(getActivity());

        mMapView = (NMapView)getView().findViewById(R.id.id_AddSpotMapView);
        mMapView.setClientId(CLIENT_ID);

        mMapContext.setupMapView(mMapView);

        mMapView.setOnMapStateChangeListener(mStateChangeListener);

        //자세한 확대 (수치는 적절히 조정하도록)
        mMapView.setScalingFactor(2.0f);

        //줌인아웃 버튼
        mMapView.setBuiltInZoomControls(true, null);

        //화면 터치 옵션 활성화
        mMapView.setClickable(true);

        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();

        mMapViewerResourceProvider = new NMapViewerResourceProvider(getActivity());
        mOverlayManager = new NMapOverlayManager(getActivity(), mMapView, mMapViewerResourceProvider);

        //NMapView를 생성하면서 자동으로 컨트롤러도 생성되므로 NMapView로부터 얻어온다.
        mMapController = mMapView.getMapController();


    }

    @Override
    public void onStart() {
        super.onStart();
        mMapContext.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapContext.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapContext.onPause();
    }

    @Override
    public void onDestroyView() {
        mMapContext.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mMapView.setOnMapStateChangeListener((NMapView.OnMapStateChangeListener) null);

        super.onDestroy();
    }

    /**
     * 맵 상태변화 리스너
     */
    private NMapView.OnMapStateChangeListener mStateChangeListener = new NMapView.OnMapStateChangeListener() {
        @Override
        public void onMapInitHandler(NMapView nMapView, NMapError nMapError) {

            if(nMapError == null)
            {
                // 초기 위치 설정
                String address = String.valueOf(((AddSpotActivity)AddSpotActivity.mContext).mEditText_AddressSearch.getText());
                NGeoPoint point = ((FragmentMapActivity) FragmentMapActivity.mContext).ConvertLatLng(address);
                mMapController.setMapCenter(point, 11);     //NGeopoint , 축척레벨(1~14)

            } else {
                Toast.makeText(getActivity(), "지도를 초기화하는데 실패하였습니다.\n message: " + nMapError.message, Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }

        //지도 중심 변경 시 호출되며 변경된 중심 좌표가 파라미터로 전달된다.
        @Override
        public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {
            String Latitude = String.valueOf(nGeoPoint.getLatitude());
            String Longitude = String.valueOf(nGeoPoint.getLongitude());

            mCenterAddress = nGeoPoint;

            mStringCenterAddress =
                    ((FragmentMapActivity) FragmentMapActivity.mContext).ConvertAddress(mContext, mCenterAddress.getLatitude(), mCenterAddress.getLongitude());

            Log.d(TAG, Latitude);
            Log.d(TAG, Longitude);
            Log.d(TAG, mStringCenterAddress);


            //지도 센터의 값을 주소로 변환하여 AddSpotFragmentActivity의 주소를 담는 텍스트뷰를 계속 갱신시킨다.
            ((AddSpotFragmentActivity) AddSpotFragmentActivity.mContext).text_CenterAddress.setText(mStringCenterAddress);
        }

        @Override
        public void onMapCenterChangeFine(NMapView nMapView) {

        }

        @Override
        public void onZoomLevelChange(NMapView nMapView, int i) {

        }

        @Override
        public void onAnimationStateChange(NMapView nMapView, int i, int i1) {

        }
    };
}
