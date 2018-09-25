package com.jinganweigu.entrysystem.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.entry.SureOutHouseBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/19 0019.
 */

public class DeviceDeticalAdapter extends BaseAdapter {

    List<SureOutHouseBean.ResultBean> list;
    Context context;
    ViewHolder mViewHolder;

    public DeviceDeticalAdapter(List<SureOutHouseBean.ResultBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = View.inflate(context, R.layout.item_device_detical_information, null);

            mViewHolder = new ViewHolder(convertView);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.tvDeviceName.setText(list.get(position).getFirst_line()+":");
        mViewHolder.tvDeviceInfo.setText(list.get(position).getSecond_line());


        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_device_name)
        TextView tvDeviceName;
        @BindView(R.id.tv_device_info)
        TextView tvDeviceInfo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
