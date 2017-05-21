package com.imaginarywings.capstonedesign.remo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.imaginarywings.capstonedesign.remo.model.LocalSearchModel;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocalSearchActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.lsch_keyword_schv) SearchView mSearchView;
    @BindView(R.id.lsch_recv) RecyclerView mRecyclerView;
    @BindView(R.id.lsch_no_data_tv) TextView mNoDataTv;

    private LocalSearchAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_search);
        ButterKnife.bind(this);

        mAdapter = new LocalSearchAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mSearchView.setIconifiedByDefault(false);
    }

    @OnClick(R.id.lsch_search_btn)
    public void searchBtnClick() {
        mSearchView.clearFocus();
        String query = mSearchView.getQuery().toString();

        Ion.with(this)
                .load("https://openapi.naver.com/v1/search/local.json")
                .addHeader("X-Naver-Client-Id", "0GXChEkI2S9AbF8nurCD")
                .addHeader("X-Naver-Client-Secret", "oLnEameXEL")
                .addQuery("query", query)
                .as(new TypeToken<LocalSearchModel>() {
                })
                .setCallback(new FutureCallback<LocalSearchModel>() {
                    @Override
                    public void onCompleted(Exception e, LocalSearchModel result) {
                        if (e == null) {
                            mAdapter.resetDataList(result.getItems());
                            mNoDataTv.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                        } else if (result.getTotal() == 0) {
                            // 검색 데이터가 없을때
                            mNoDataTv.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        } else {
                            // 검색 오류
                            Toast.makeText(LocalSearchActivity.this, "검색에 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                            mNoDataTv.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                            Log.e(TAG, "searchBtnClick: ", e);
                        }
                    }
                });

    }
}
