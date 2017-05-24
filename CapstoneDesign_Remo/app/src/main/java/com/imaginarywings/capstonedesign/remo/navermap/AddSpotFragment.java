package com.imaginarywings.capstonedesign.remo.navermap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imaginarywings.capstonedesign.remo.R;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapView;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;

/**
 * Created by S.JJ on 2017-05-22.
 * 포토스팟 등록창에서 주소 검색 후 상세 위치 추가를 위한 프래그먼트창
 */

public class AddSpotFragment extends NMapFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_spot, container, false);
    }
}
