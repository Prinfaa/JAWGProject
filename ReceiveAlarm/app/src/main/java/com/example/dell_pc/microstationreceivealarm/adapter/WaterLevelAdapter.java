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
import com.example.dell_pc.microstationreceivealarm.item.WaterLevelItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WaterLevelAdapter extends BaseAdapter {

    private Context context;
    private List<WaterLevelItem> waterLevelList;

    private Map<Integer, View> viewMap = new HashMap<Integer, View>();


    public WaterLevelAdapter(Context context, List<WaterLevelItem> waterLevelList){
        this.context = context;
        this.waterLevelList = waterLevelList;
    }
    @Override
    public int getCount() {
        return waterLevelList.size();
    }

    @Override
    public WaterLevelItem getItem(int position) {
        return waterLevelList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.water_level_item,null);
        }

        final WaterLevelItem waterLevelItem = waterLevelList.get(position);

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvPosition = (TextView) convertView.findViewById(R.id.tvPosition);
        TextView tvValue = (TextView) convertView.findViewById(R.id.tvValue);

        tvName.setText(waterLevelItem.getConstructionName());
        tvPosition.setText(waterLevelItem.getPosition());
        long now = System.currentTimeMillis()/1000;
        if((now-Long.parseLong(waterLevelItem.getLastTime()))>3600){
            tvValue.setTextColor(0XFFCC6600);
            tvValue.setText("离线");
        }else {
            double level = Double.parseDouble(waterLevelItem.getLevel());
            double maxLevel = Double.parseDouble(waterLevelItem.getMaxLevel());
            double rate = level/maxLevel;
            double referValue = Double.parseDouble(waterLevelItem.getReferValue());
            if(rate >= referValue){
                tvValue.setTextColor(0XFF333333);
                //tvValue.setText(waterLevelItem.getValue() + "Mpa");
                tvValue.setText("正常");

            }else {
                tvValue.setTextColor(0XFFFF0000);
                //tvValue.setText(waterLevelItem.getValue() + "Mpa");
                tvValue.setText("不足");
            }
        }




        return convertView;
    }


}
