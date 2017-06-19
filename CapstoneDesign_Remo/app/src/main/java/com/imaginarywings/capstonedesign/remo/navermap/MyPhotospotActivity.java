package com.imaginarywings.capstonedesign.remo.navermap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.imaginarywings.capstonedesign.remo.R;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import static com.imaginarywings.capstonedesign.remo.Consts.API_URL;

/**
 * Created by Administrator on 2017-06-18.
 */

public class MyPhotospotActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myphotospot);

        // 서버에서 받아오는 데이터
        List<Part> parts = new ArrayList<>();

        // UUID
        String uuid = ((FragmentMapActivity) FragmentMapActivity.mContext).mUUID;

        getMyPhotospotList(uuid);
    }

    private void getMyPhotospotList(final String uuid)
    {
        Ion.with(this)
                .load(API_URL + "/spots")
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
                            JsonArray array = result.getAsJsonArray("data");
                            for(int i = 0; i < array.size(); i++)
                            {
                                final JsonObject object = array.get(i).getAsJsonObject();

                                String str = object.get("user_uuid").getAsString();

                                if(str.equals(uuid))
                                {
                                    Log.e(TAG, str);
                                }
                            }
                        }
                    }
                });
    }
}
