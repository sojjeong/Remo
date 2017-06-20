package com.imaginarywings.capstonedesign.remo.navermap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.imaginarywings.capstonedesign.remo.MySpotAdapter;
import com.imaginarywings.capstonedesign.remo.R;
import com.imaginarywings.capstonedesign.remo.model.PhotoSpotModel;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.imaginarywings.capstonedesign.remo.Consts.API_URL;

/**
 * Created by Administrator on 2017-06-18.
 */

public class MyPhotospotActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    public static final String TAG_SPOT_DETAIL_DIALOG = "SpotDetailDialog";

    @BindView(R.id.id_lv_spotlist) ListView mSpotListView;

    private MySpotAdapter mSpotAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myphotospot);

        ButterKnife.bind(this);

        mSpotAdapter = new MySpotAdapter(this);
        mSpotListView.setAdapter(mSpotAdapter);

        // UUID
        String uuid = FragmentMapActivity.mUUID;

        // UUID를 기준으로 나의 포토스팟 목록 가져오기
        getMyPhotospotList(uuid);
    }

    private void getMyPhotospotList(final String uuid) {
        Ion.with(this)
                .load(API_URL + "/spots")
                .addQuery("user_uuid", uuid)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            Toast.makeText(MyPhotospotActivity.this, "포토스팟 목록을 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, result.toString());

                            if(!result.get("code").toString().equals("404"))
                            {
                                JsonArray array = result.get("data").getAsJsonArray();

                                List<PhotoSpotModel> newList = new ArrayList<>();

                                for (int i = 0; i < array.size(); ++i) {
                                    JsonObject object = array.get(i).getAsJsonObject();

                                    String spot_id = object.get("spot_id").getAsString();
                                    String spot_url = API_URL + "/" + object.get("spot_image_url").getAsString();
                                    String spot_latitude = object.get("spot_latitude").getAsString();
                                    String spot_longitude = object.get("spot_longitude").getAsString();
                                    String spot_address = object.get("spot_address").getAsString();
                                    String user_uuid = object.get("user_uuid").getAsString();
                                    String spot_datetime = object.get("spot_datetime").getAsString();

                                    int id = Integer.valueOf(spot_id);
                                    double latitude = Double.valueOf(spot_latitude);
                                    double longitude = Double.valueOf(spot_longitude);

                                    PhotoSpotModel newSpot = new PhotoSpotModel(
                                            id, "TYPE", user_uuid, "SUBJECT", spot_address, spot_url, latitude, longitude);

                                    newSpot.setDateTime(spot_datetime);

                                    newList.add(newSpot);
                                }

                                mSpotAdapter.updateList(newList);
                            }
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
