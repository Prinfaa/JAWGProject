package com.example.dell_pc.microstationreceivealarm.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dell_pc.microstationreceivealarm.R;
import com.example.dell_pc.microstationreceivealarm.item.WaterPressItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WaterPressAdapter extends BaseAdapter {

    private Context context;
    private List<WaterPressItem> waterPressList;

    private Map<Integer, View> viewMap = new HashMap<Integer, View>();


    public WaterPressAdapter(Context context, List<WaterPressItem> waterPressList){
        this.context = context;
        this.waterPressList = waterPressList;
    }
    @Override
    public int getCount() {
        return waterPressList.size();
    }

    @Override
    public WaterPressItem getItem(int position) {
        return waterPressList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = this.viewMap.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.water_press_item,null);
        }

        final WaterPressItem waterPressItem = waterPressList.get(position);

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvPosition = (TextView) convertView.findViewById(R.id.tvPosition);
        TextView tvValue = (TextView) convertView.findViewById(R.id.tvValue);

        tvName.setText(waterPressItem.getConstructionName());
        tvPosition.setText(waterPressItem.getPosition());
        long now = System.currentTimeMillis()/1000;
        if((now-Long.parseLong(waterPressItem.getLastTime()))>3600){
            tvValue.setTextColor(0XFFCC6600);
            tvValue.setText("离线");
        }else {
            double value = Double.parseDouble(waterPressItem.getValue());
            double referValue = Double.parseDouble(waterPressItem.getReferValue());
            if(value >= referValue){
                tvValue.setTextColor(0XFF333333);
                tvValue.setText("正常");

            }else {
                tvValue.setTextColor(0XFFFF0000);
                tvValue.setText("欠压");
            }
        }
        return convertView;
    }


}
