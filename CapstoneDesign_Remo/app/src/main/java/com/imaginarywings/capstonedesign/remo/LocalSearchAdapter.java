package com.imaginarywings.capstonedesign.remo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imaginarywings.capstonedesign.remo.model.LocalSearchItemModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rimi on 2017. 4. 24..
 * Copyright (c) 2017 UserInsight Corp.
 */

public class LocalSearchAdapter extends RecyclerView.Adapter<LocalSearchAdapter.LocalSearchHolder> {
    private final String TAG = getClass().getSimpleName();

    private Context mContext;
    private List<LocalSearchItemModel> mDataList;

    public LocalSearchAdapter(Context context) {
        mContext = context;
        mDataList = new ArrayList<>();
    }

    @Override
    public LocalSearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_local_search, parent, false);
        return new LocalSearchHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocalSearchHolder holder, int position) {
        final LocalSearchItemModel data = mDataList.get(position);

        holder.mTitleTv.setText(data.getTitle());
        if (data.getRoadAddress() == null || data.getRoadAddress().equals("")) {
            holder.mAddressTv.setText(data.getAddress());
        } else {
            holder.mAddressTv.setText(data.getRoadAddress());
        }
        holder.mCategoryTv.setText(data.getCategory());

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: " + data.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void resetDataList(List<LocalSearchItemModel> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    class LocalSearchHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lsch_item_layout) LinearLayout mLayout;
        @BindView(R.id.lsch_item_title_tv) TextView mTitleTv;
        @BindView(R.id.lsch_item_category_tv) TextView mCategoryTv;
        @BindView(R.id.lsch_item_address_tv) TextView mAddressTv;

        public LocalSearchHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
