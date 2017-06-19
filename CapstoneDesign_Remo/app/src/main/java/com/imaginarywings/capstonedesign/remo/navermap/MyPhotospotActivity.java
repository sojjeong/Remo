package com.imaginarywings.capstonedesign.remo.navermap;

import android.os.Bundle;
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
import com.imaginarywings.capstonedesign.remo.model.PhotoSpotModel;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.Part;
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
    static final ArrayList LIST_MENU = new ArrayList();

    @BindView(R.id.id_lv_spotlist) ListView spotListView;
    ArrayAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myphotospot);

        ButterKnife.bind(this);

        //포토스팟 목록 리스트뷰
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU);
        spotListView.setAdapter(adapter);

        // UUID
        String uuid = ((FragmentMapActivity) FragmentMapActivity.mContext).mUUID;

        // UUID를 기준으로 나의 포토스팟 목록 가져오기
        getMyPhotospotList(uuid);

        spotListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get TextView's Text.
                String strText = (String) parent.getItemAtPosition(position) ;

                Toast.makeText(MyPhotospotActivity.this, strText, Toast.LENGTH_LONG).show();
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

                                LIST_MENU.add(spot_address);
                            }
                        }
                    }
                });
    }
}
