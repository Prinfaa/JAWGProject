package com.example.dell_pc.unitpad.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dell_pc.unitpad.R;
import com.example.dell_pc.unitpad.item.DeviceItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlarmDectorAdapter extends BaseAdapter {

    private Context context;
    private List<DeviceItem> facList;
    private Map<Integer, View> viewMap = new HashMap<Integer, View>();

    public AlarmDectorAdapter(Context context, List<DeviceItem> facList) {
        this.context = context;
        this.facList = facList;
    }


    @Override
    public int getCount() {
        return facList.size();
    }

    @Override
    public DeviceItem getItem(int position) {
        return facList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        convertView = this.viewMap.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fac_chose_item, null);
        }
        TextView tvFac = (TextView) convertView.findViewById(R.id.tvFac);
        TextView tvPosition = (TextView) convertView.findViewById(R.id.tvPosition);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        TextView tvValue = (TextView) convertView.findViewById(R.id.tvValue);
        TextView tvUnit = (TextView) convertView.findViewById(R.id.tvUnit);
        holder.tvStatue = (TextView) convertView.findViewById(R.id.tvStatue);

        DeviceItem facItem = facList.get(position);
        String state = facItem.getState();
        tvFac.setText(facItem.getType());
        tvPosition.setText(facItem.getPosition());
        if (!facItem.getLastTimestamp().equals("")) {
            tvTime.setText(facItem.getLastTimestamp());
        } else {
            tvTime.setText("");
        }





        tvValue.setText(facItem.getValue());
        if ( state.equals("启动")) {
            holder.tvStatue.setTextColor(0XFF009966);
            holder.tvStatue.setText("启动");
            tvValue.setTextColor(0XFF009966);
        }else {
            holder.tvStatue.setText(state);
            holder.tvStatue.setTextColor(0XFFFF6600);
            tvValue.setTextColor(0XFFFF6600);
            return convertView;
        }


        if (position % 2 == 0) {
            convertView.setBackgroundColor(0xFFFFFFFF);
        } else {
            convertView.setBackgroundColor(0xFFF7F7F7);
        }

        return convertView;
    }

    public class ViewHolder {
        public TextView tvStatue;
    }


}