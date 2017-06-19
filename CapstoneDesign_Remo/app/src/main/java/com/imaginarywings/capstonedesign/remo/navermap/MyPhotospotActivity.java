package com.imaginarywings.capstonedesign.remo.navermap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.imaginarywings.capstonedesign.remo.R;
import com.imaginarywings.capstonedesign.remo.SpotDetailDialog;
import com.imaginarywings.capstonedesign.remo.model.PhotoSpotModel;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.imaginarywings.capstonedesign.remo.Consts.API_URL;

/**
 * Created by Administrator on 2017-06-18.
 */

public class MyPhotospotActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private static final String TAG_SPOT_DETAIL_DIALOG = "SpotDetailDialog";
    private  PhotoSpotModel mSpotModel;

    static final ArrayList<String> LIST_MENU = new ArrayList();

    @BindView(R.id.id_lv_spotlist) ListView mSpotListView;
    ArrayAdapter mAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myphotospot);

        ButterKnife.bind(this);

        //포토스팟 목록 리스트뷰
        mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU);
        mSpotListView.setAdapter(mAdapter);

        // UUID
        String uuid = ((FragmentMapActivity) FragmentMapActivity.mContext).mUUID;

        // UUID를 기준으로 나의 포토스팟 목록 가져오기
        getMyPhotospotList(uuid);

        // 리스트뷰를 눌렀을 때 이벤트 처리
        mSpotListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String strText = (String) parent.getItemAtPosition(position);

                Ion.with(MyPhotospotActivity.this)
                        .load (API_URL + "/spots")
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                JsonArray array = result.get("data").getAsJsonArray();

                                for(int i = 0; i < array.size(); ++i)
                                {
                                    JsonObject object = (JsonObject)array.get(i);

                                    String spot_url = API_URL+ "/" + object.get("spot_image_url").getAsString();
                                    String spot_id = object.get("spot_id").getAsString();
                                    String spot_latitude = object.get("spot_latitude").getAsString();
                                    String spot_longitude = object.get("spot_longitude").getAsString();
                                    String spot_address = object.get("spot_address").getAsString();
                                    String user_uuid = object.get("user_uuid").getAsString();

                                    int id = Integer.valueOf(spot_id).intValue();
                                    double latitude = Double.valueOf(spot_latitude).doubleValue();
                                    double longitude = Double.valueOf(spot_longitude).doubleValue();

                                    if(strText.equals(spot_address)) {
                                        mSpotModel = new PhotoSpotModel(id, "type", user_uuid, "subject", spot_address, spot_url, latitude, longitude);

                                        FragmentManager manager = getSupportFragmentManager();
                                        Fragment frag = manager.findFragmentByTag(TAG_SPOT_DETAIL_DIALOG);
                                        if (frag != null) {
                                            manager.beginTransaction().remove(frag).commit();
                                        }

                                        //포토스팟을 눌렀을 때 나오는 팝업창
                                        SpotDetailDialog dialog = new SpotDetailDialog();
                                        Bundle data = new Bundle();
                                        data.putParcelable("detail", mSpotModel);
                                        dialog.setArguments(data);
                                        dialog.show(manager, TAG_SPOT_DETAIL_DIALOG);

                                    }
                                }
                            }
                        });
            }
        });
    }

    private void getMyPhotospotList(final String uuid)
    {
        Ion.with(this)
                .load(API_URL + "/spots")
                .addQuery("user_uuid", uuid)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if(e != null)
                        {
                            Toast.makeText(MyPhotospotActivity.this, "포토스팟 목록을 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Log.e(TAG, result.toString());
                            JsonArray array = result.get("data").getAsJsonArray();

                            for(int i = 0; i < array.size(); ++i)
                            {
                                JsonObject object = (JsonObject)array.get(i);

                                String spot_url = API_URL+ "/" + object.get("spot_image_url").getAsString();
                                String spot_id = object.get("spot_id").getAsString();
                                String spot_latitude = object.get("spot_latitude").getAsString();
                                String spot_longitude = object.get("spot_longitude").getAsString();
                                String spot_address = object.get("spot_address").getAsString();
                                String user_uuid = object.get("user_uuid").getAsString();

                                int id = Integer.valueOf(spot_id).intValue();
                                double latitude = Double.valueOf(spot_latitude).doubleValue();
                                double longitude = Double.valueOf(spot_longitude).doubleValue();

                                mAdapter.add(spot_address);
                            }
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LIST_MENU.clear();
    }
}
