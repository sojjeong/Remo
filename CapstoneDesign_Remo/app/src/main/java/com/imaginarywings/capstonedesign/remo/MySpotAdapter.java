package com.imaginarywings.capstonedesign.remo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imaginarywings.capstonedesign.remo.model.PhotoSpotModel;
import com.imaginarywings.capstonedesign.remo.navermap.MyPhotospotActivity;

import java.util.ArrayList;
import java.util.List;

import static com.imaginarywings.capstonedesign.remo.navermap.MyPhotospotActivity.TAG_SPOT_DETAIL_DIALOG;

/**
 * Created by S.JJ on 2017-06-20.
 */

public class MySpotAdapter extends BaseAdapter {
    private final String TAG = getClass().getSimpleName();

    private Context mContext;
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private List<PhotoSpotModel> listViewItemList = new ArrayList<>();

    // ListViewAdapter의 생성자
    public MySpotAdapter(Context context) {
        this.mContext = context;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_my_spot, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.my_spot_layout);
        TextView addressTv = (TextView) convertView.findViewById(R.id.my_spot_address_tv);
        TextView datetimeTv = (TextView) convertView.findViewById(R.id.my_spot_datetime_tv);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final PhotoSpotModel listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: " + listViewItem.getId());

                FragmentManager manager = ((FragmentActivity) mContext).getSupportFragmentManager();
                Fragment frag = manager.findFragmentByTag(MyPhotospotActivity.TAG_SPOT_DETAIL_DIALOG);
                if (frag != null) {
                    manager.beginTransaction().remove(frag).commit();
                }

                //포토스팟을 눌렀을 때 나오는 팝업창
                SpotDetailDialog dialog = new SpotDetailDialog();
                Bundle data = new Bundle();
                data.putParcelable("detail", listViewItem);
                dialog.setArguments(data);
                dialog.show(manager, TAG_SPOT_DETAIL_DIALOG);
            }
        });

        addressTv.setText(listViewItem.getAddress());
        datetimeTv.setText(listViewItem.getDateTime());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    public void updateList(List<PhotoSpotModel> list) {
        listViewItemList.clear();
        listViewItemList.addAll(list);
        notifyDataSetChanged();
    }
}
