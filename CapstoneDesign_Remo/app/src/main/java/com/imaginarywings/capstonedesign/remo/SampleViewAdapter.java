package com.imaginarywings.capstonedesign.remo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.imaginarywings.capstonedesign.remo.model.SampleImageModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.imaginarywings.capstonedesign.remo.Consts.DEFAULT_URL;

/**
 * Created by rimi on 2017. 6. 13..
 * Copyright (c) 2017 UserInsight Corp.
 */

public class SampleViewAdapter extends RecyclerView.Adapter<SampleViewAdapter.SampleViewHolder>{
    private final String TAG = getClass().getSimpleName();

    private Context mContext;
    private List<SampleImageModel> mImageList;

    public SampleViewAdapter(Context context) {
        this.mContext = context;
        this.mImageList = new ArrayList<>();
    }

    @Override
    public SampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_sample_image, parent, false);
        return new SampleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SampleViewHolder holder, int position) {
        final SampleImageModel model = mImageList.get(position);

        String fullImageUrl = DEFAULT_URL + model.getSampleUrl();
        Log.d(TAG, "onBindViewHolder: " + fullImageUrl);
        Glide.with(mContext)
                .load(fullImageUrl)
                .thumbnail(0.1f)
                .into(holder.mImageView);

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(mContext, CameraActivity.class);
                cameraIntent.putExtra("image", model.getGuideUrl());
                mContext.startActivity(cameraIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    public void addImage(SampleImageModel model) {
        Log.d(TAG, "addImage: " + mImageList.size());
        mImageList.add(model);
        notifyDataSetChanged();
    }

    public class SampleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sample_image) ImageView mImageView;
        public SampleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
