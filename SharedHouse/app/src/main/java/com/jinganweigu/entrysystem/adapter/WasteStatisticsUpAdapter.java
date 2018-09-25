package com.jinganweigu.entrysystem.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.entry.WasteDeviceBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WasteStatisticsUpAdapter extends BaseAdapter {

    private Context context;
    private List<WasteDeviceBean.ResultBean.TotalBean> list;
    private ViewHolder holder;

    public WasteStatisticsUpAdapter(Context context, List<WasteDeviceBean.ResultBean.TotalBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {

            convertView = View.inflate(context, R.layout.item_waste_staistic_device_type, null);

            holder=new ViewHolder(convertView);

        }else {

            holder= (ViewHolder) convertView.getTag();
        }

        holder.tvDeviceNum.setText(list.get(position).getQuantity());
        holder.tvDeviceType.setText(list.get(position).getMitu_type());

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_device_type)
        TextView tvDeviceType;
        @BindView(R.id.tv_device_num)
        TextView tvDeviceNum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
