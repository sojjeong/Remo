package com.imaginarywings.capstonedesign.remo.navermap;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.imaginarywings.capstonedesign.remo.R;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
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

    //포토스팟 다이얼로그 창 데이터 해시 테이블?
    private static final String TAG_SPOT_DETAIL_DIALOG = "SpotDetailDialog";

    private static final int REQUEST_LOCATION_ENABLE = 717;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMapContext = new NMapContext(super.getActivity());
        mMapContext.onCreate();
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
     * 마커 추가하기
     */
    private void testPOIdataOverlay() {

        // Markers for POI item
        int markerId = NMapPOIflagType.PIN;

        // set POI data
        NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
        poiData.beginPOIdata(2);
        NMapPOIitem item = poiData.addPOIitem(127.0630205, 37.5091300, "Pizza 777-111", markerId, 0);
        item.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
        poiData.addPOIitem(127.061, 37.51, "Pizza 123-456", markerId, 0);
        poiData.endPOIdata();

        // create POI data overlay
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);

        // set event listener to the overlay
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

        // select an item
        poiDataOverlay.selectPOIitem(0, true);

        // show all POI data
        //poiDataOverlay.showAllPOIdata(0);
    }

    /* POI data State Change Listener*/
    private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {

        @Override
        public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onCalloutClick: title=" + item.getTitle());
            }

            // [[TEMP]] handle a click event of the callout
            Toast.makeText(getActivity(), "onCalloutClick: " + item.getTitle(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            if (DEBUG) {
                if (item != null) {
                    Log.i(LOG_TAG, "onFocusChanged: " + item.toString());
                } else {
                    Log.i(LOG_TAG, "onFocusChanged: ");
                }
            }
        }
    };

    /*
    private void testFloatingPOIdataOverlay() {
        // Markers for POI item
        int marker1 = NMapPOIflagType.PIN;

        // set POI data
        NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
        poiData.beginPOIdata(1);
        NMapPOIitem item = poiData.addPOIitem(null, "Touch & Drag to Move", marker1, 0);
        if (item != null) {
            // initialize location to the center of the map view.
            item.setPoint(mMapController.getMapCenter());
            // set floating mode
            item.setFloatingMode(NMapPOIitem.FLOATING_TOUCH | NMapPOIitem.FLOATING_DRAG);
            // show right button on callout
            item.setRightButton(true);

            mFloatingPOIitem = item;
        }
        poiData.endPOIdata();

        // create POI data overlay
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        if (poiDataOverlay != null) {
            poiDataOverlay.setOnFloatingItemChangeListener(onPOIdataFloatingItemChangeListener);

            // set event listener to the overlay
            poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

            poiDataOverlay.selectPOIitem(0, false);

            mFloatingPOIdataOverlay = poiDataOverlay;
        }
    }
    */

    /* NMapDataProvider Listener */
    /*
    private final NMapActivity.OnDataProviderListener onDataProviderListener = new NMapActivity.OnDataProviderListener() {

        @Override
        public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {

            if (DEBUG) {
                Log.i(LOG_TAG, "onReverseGeocoderResponse: placeMark="
                        + ((placeMark != null) ? placeMark.toString() : null));
            }

            if (errInfo != null) {
                Log.e(LOG_TAG, "Failed to findPlacemarkAtLocation: error=" + errInfo.toString());

                Toast.makeText(NMapViewer.this, errInfo.toString(), Toast.LENGTH_LONG).show();
                return;
            }

            if (mFloatingPOIitem != null && mFloatingPOIdataOverlay != null) {
                mFloatingPOIdataOverlay.deselectFocusedPOIitem();

                if (placeMark != null) {
                    mFloatingPOIitem.setTitle(placeMark.toString());
                }
                mFloatingPOIdataOverlay.selectPOIitemBy(mFloatingPOIitem.getId(), false);
            }
        }

    };
    */
}
