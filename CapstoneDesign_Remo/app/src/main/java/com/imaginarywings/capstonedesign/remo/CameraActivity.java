package com.imaginarywings.capstonedesign.remo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.imaginarywings.capstonedesign.remo.Consts.DEFAULT_URL;

public class CameraActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.camera_layout) FrameLayout mCameraLayout;
    @BindView(R.id.camera_preview) CameraView mPreview;
    @BindView(R.id.camera_guide) ImageView mGuideImage;

    @BindView(R.id.capture_btn) ImageButton mCaptureBtn;
    @BindView(R.id.switch_btn) ImageButton mSwitchBtn;
    @BindView(R.id.gallery_btn) ImageButton mGalleryBtn;

    private String mGuideUrl;
    private boolean mInitialized = false;
    private Bitmap mGuideBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        mGuideUrl = getIntent().getStringExtra("image");
        if (getIntent() != null && mGuideUrl != null) {
            // 화면 가로 크기에 따라 세로 크기 변경
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;

            // 화면에 보여지는 FrameLayout의 크기 변경
            ViewGroup.LayoutParams cameraLayoutParams = mCameraLayout.getLayoutParams();
            cameraLayoutParams.height = width;
            mCameraLayout.setLayoutParams(cameraLayoutParams);

            // 프리뷰 크기변경
            ViewGroup.LayoutParams previewLayoutParams = mPreview.getLayoutParams();
            previewLayoutParams.height = width;
            mPreview.setLayoutParams(previewLayoutParams);

            // 카메라가 사용가능할때 캡쳐가 가능하도록 한다.
            updateButtons(false);

            mPreview.setCameraListener(new CameraListener() {
                @Override
                public void onCameraOpened() {
                    new Handler(getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mInitialized) {
                                mGuideImage.setImageBitmap(mGuideBitmap);
                            } else {
                                Glide.with(CameraActivity.this)
                                        .load(DEFAULT_URL + mGuideUrl)
                                        .asBitmap()
                                        .thumbnail(0.1f)
                                        .listener(new RequestListener<String, Bitmap>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                mGuideBitmap = resource;
                                                return false;
                                            }
                                        })
                                        .into(mGuideImage);
                            }
                        }
                    }, 1000);

                    updateButtons(true);

                    super.onCameraOpened();
                }

                @Override
                public void onCameraClosed() {
                    new Handler(getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(CameraActivity.this)
                                    .load(R.drawable.black)
                                    .thumbnail(0.1f)
                                    .into(mGuideImage);
                        }
                    });
                    updateButtons(false);

                    super.onCameraClosed();
                }

                @Override
                public void onPictureTaken(byte[] jpeg) {
                    super.onPictureTaken(jpeg);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length, options);
                    String filename = System.currentTimeMillis() + ".jpg";
                    new SaveTask(bitmap, "REMO", filename, new OnPictureSavedListener() {
                        @Override
                        public void onPictureSaved(Uri uri) {
                            mGuideImage.setImageBitmap(mGuideBitmap);
                            Log.i(TAG, "저장된 사진 경로: " + uri.toString());
                            updateButtons(true);
                        }
                    }).execute();
                }
            });
        } else {
            Toast.makeText(this, "올바른 데이터가 아닙니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPreview.start();
    }

    @Override
    protected void onPause() {
        mPreview.stop();
        super.onPause();
    }

    @OnClick(R.id.switch_btn)
    public void switchBtnClick() {
        updateButtons(false);
        mPreview.toggleFacing();
    }

    @OnClick(R.id.capture_btn)
    public void captureBtnClick() {
        updateButtons(false);
        Glide.with( CameraActivity.this)
                .load(R.drawable.black)
                .thumbnail(0.1f)
                .into(mGuideImage);
        mPreview.captureImage();
    }

    @OnClick(R.id.gallery_btn)
    public void galleryBtnClick() {
        updateButtons(false);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivity(intent);
    }

    /**
     * 카메라 변경, 캡쳐 등은 카메라가 오픈되있을때만 사용가능하도록 한다.
     *
     * @param enabled 여부
     */
    private void updateButtons(final boolean enabled) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSwitchBtn.setEnabled(enabled);
                mCaptureBtn.setEnabled(enabled);
                mGalleryBtn.setEnabled(enabled);
            }
        });
    }

    /**
     * 아래 클래스는 사진을 저장할때 백그라운드에서 저장하게 한다.
     * 캡쳐버튼을 클릭하면 가이드라인을 검은색으로 만들고 caputeImage()메소드가 실행된다
     * doInBackground()의 saveImage() 메소드가 실행되고 파일이 정상적으로 저장되면
     * onPictureSaved()리스너를 호출해서 다시 가이드라인으로 변경한다.
     */
    private class SaveTask extends AsyncTask<Void, Void, Void> {
        private final Bitmap mBitmap;
        private final String mFolderName;
        private final String mFileName;
        private final OnPictureSavedListener mListener;
        private final Handler mHandler;

        public SaveTask(Bitmap bitmap, final String folderName, final String fileName,
                        final OnPictureSavedListener listener) {
            mBitmap = bitmap;
            mFolderName = folderName;
            mFileName = fileName;
            mListener = listener;
            mHandler = new Handler(getMainLooper());
        }

        @Override
        protected Void doInBackground(final Void... params) {
            saveImage(mFolderName, mFileName, mBitmap);
            return null;
        }

        private void saveImage(final String folderName, final String fileName, final Bitmap image) {
            File path = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file = new File(path, folderName + "/" + fileName);
            try {
                file.getParentFile().mkdirs();
                image.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(file));
                MediaScannerConnection.scanFile(getApplicationContext(),
                        new String[]{
                                file.toString()
                        }, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(final String path, final Uri uri) {
                                if (mListener != null) {
                                    mHandler.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            mListener.onPictureSaved(uri);
                                        }
                                    });
                                }
                            }
                        });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnPictureSavedListener {
        void onPictureSaved(Uri uri);
    }
}
