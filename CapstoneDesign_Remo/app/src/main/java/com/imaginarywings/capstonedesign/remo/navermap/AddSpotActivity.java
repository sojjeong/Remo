package com.imaginarywings.capstonedesign.remo.navermap;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.imaginarywings.capstonedesign.remo.R;
import com.imaginarywings.capstonedesign.remo.model.PhotoSpotModel;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.FilePart;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.async.http.body.StringPart;
import com.koushikdutta.ion.Ion;
import com.nhn.android.maps.maplib.NGeoPoint;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.imaginarywings.capstonedesign.remo.Consts.API_URL;

public class AddSpotActivity extends AppCompatActivity {

    private final int REQUEST_SELECT_PHOTO = 1004;

    private final String TAG = getClass().getSimpleName();

    public static Context mContext;
    public static boolean mMarkerEnable;
    public PhotoSpotModel mSpotModel;
    public LocationManager locationManager;
    private String mSelectedImagePath;
    public NGeoPoint mSavePoint;

    @BindView(R.id.id_SpotAddress) TextView mtext_SpotAddress;
    @BindView(R.id.btn_AddSpot_Search) Button mbtnSearch;
    @BindView(R.id.id_editTextAddressSearch) EditText mEditText_AddressSearch;
    @BindView(R.id.Btn_AddSpot_Image) ImageButton mbtnAddSpotImage;
    @BindView(R.id.id_ImgView_PhotoSpot) ImageView mPhotospotImage;
    @BindView(R.id.id_imgbtnSaveSpot) ImageButton mSaveSpot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spot);

        ButterKnife.bind(this);

        mContext = this;
        mMarkerEnable = false;
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        //GPS ON/OFF 유무 확인
        boolean isEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        //활성화 되어 있다면 처음 생성할 때 현재 위치에 대한 주소를 기본적인 주소로 지정한다.
        if (isEnable) {

            //FragmentMapActivity 의 함수 호출 방법
            //포토스팟 위도 경도 삽입
            mSavePoint = ((FragmentMapActivity) FragmentMapActivity.mContext).getAddress();

            String Address =
                    ((FragmentMapActivity) FragmentMapActivity.mContext).ConvertAddress(this, mSavePoint.getLatitude(), mSavePoint.getLongitude());

            mtext_SpotAddress.setText(Address);
        } else {

            Toast.makeText(mContext, "GPS기능이 비활성화 되어 있습니다. 기능을 활성화 해주십시오.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_SELECT_PHOTO) {
            if(resultCode == RESULT_OK)
            {
                if(data != null)
                {
                    Uri uri = data.getData();

                    Glide.with(this)
                            .load(uri)
                            .thumbnail(0.1f)
                            .into(mPhotospotImage);

                    mSaveSpot.setEnabled(true);
                    mSelectedImagePath = getPathFromUri(uri);
                }
            }
            else
            {
                mSaveSpot.setEnabled(false);
                mSelectedImagePath = null;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.Btn_AddSpot_Image)
    public void addSpotBtnClick()
    {
        Intent photoPickerIntet = new Intent(Intent.ACTION_PICK);
        photoPickerIntet.setType("image/*");
        startActivityForResult(photoPickerIntet, REQUEST_SELECT_PHOTO);
    }

    //지도 검색
    @OnClick(R.id.btn_AddSpot_Search)
    public void selecteBtnAddSpotSearch() {
        Intent AddSpotMap = new Intent(getApplicationContext(), AddSpotFragmentActivity.class);

        AddSpotFragment fragment = (AddSpotFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_add_spot);

        NGeoPoint nPoint;

        String searchAddress = String.valueOf(mEditText_AddressSearch.getText());

        nPoint = ((FragmentMapActivity) FragmentMapActivity.mContext).ConvertLatLng(searchAddress);
        
        if(nPoint != null) {

            //위도
            String latitude = String.valueOf(nPoint.getLatitude());

            //경도
            String longitude = String.valueOf(nPoint.getLongitude());

            Log.e("위도", latitude);
            Log.e("경도", longitude);

            startActivity(AddSpotMap);
        }
        else
        {
            Toast.makeText(mContext, "위치를 검색할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 포토스팟 등록
     */
    @OnClick(R.id.id_imgbtnSaveSpot)
    public void selectBtnSaveSpot() {
        if(mSelectedImagePath == null)
        {
            Toast.makeText(this, "사진을 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("잠시만 기다려주세요.")
                .setCancelable(false).show();

        //서버 전송을 위한 데이터
        List<Part> parts = new ArrayList<>();

        parts.add(new FilePart("image", new File(mSelectedImagePath)));

        //주소
        if(mtext_SpotAddress.getText() != null)
            parts.add(new StringPart("spot_address", String.valueOf(mtext_SpotAddress.getText())));
        else
            Toast.makeText(this, "주소를 입력해주세요.", Toast.LENGTH_SHORT).show();

        //이름
        parts.add(new StringPart("spot_name", "임시이름"));

        //uuid
        parts.add(new StringPart("user_uuid", "사용자 uuid"));

        //위,경도
        if(mSavePoint != null)
        {
            parts.add(new StringPart("spot_latitude", String.valueOf(mSavePoint.getLatitude())));
            parts.add(new StringPart("spot_longitude", String.valueOf(mSavePoint.getLongitude())));
        }
        else
            Toast.makeText(this, "잘못된 위치입니다. 다시 확인해보세요.", Toast.LENGTH_SHORT).show();

        //포토스팟모델 생성
        mSpotModel = new PhotoSpotModel(1, "TEST", "uuid", "test1", String.valueOf(mtext_SpotAddress.getText()),
                mSelectedImagePath, mSavePoint.getLatitude(), mSavePoint.getLongitude());

        //서버에 데이터 전송
        Ion.with(this)
                .load(API_URL + "/spot")
                .addMultipartParts(parts)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        dialog.cancel();
                        if (e != null) {
                            Toast.makeText(AddSpotActivity.this, "업로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "onCompleted: " + e.getLocalizedMessage());
                        } else {

                            Log.d(TAG, "onCompleted: " + result.toString());
                            int code = result.get("code").getAsInt();
                            if (code != 201) {
                                Toast.makeText(AddSpotActivity.this, "업로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "onCompleted: " + code);
                            } else {
                                Toast.makeText(AddSpotActivity.this, "업로드 성공", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onCompleted: " + result.get("data").toString());
                                setResult(RESULT_OK);

                                mMarkerEnable = true;
                                finish();
                            }
                        }
                    }
                });

    }

    //사진 uri 경로 얻어오기
    public String getPathFromUri(Uri uri){
        Cursor cursor = getContentResolver().query(uri, null, null, null, null );
        cursor.moveToNext();
        String path = cursor.getString( cursor.getColumnIndex( "_data" ) );
        cursor.close();

        return path;
    }

    public PhotoSpotModel getSpotModel()
    {
        return mSpotModel;
    }
}
