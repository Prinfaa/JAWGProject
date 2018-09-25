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
import com.example.dell_pc.microstationreceivealarm.item.ElectricItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ElectricAdapter extends BaseAdapter {

    private Context context;
    private List<ElectricItem> ElectricList;

    private Map<Integer, View> viewMap = new HashMap<Integer, View>();


    public ElectricAdapter(Context context, List<ElectricItem> ElectricList){
        this.context = context;
        this.ElectricList = ElectricList;
    }
    @Override
    public int getCount() {
        return ElectricList.size();
    }

    @Override
    public ElectricItem getItem(int position) {
        return ElectricList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.electric_item,null);
        }

        final ElectricItem ElectricItem = ElectricList.get(position);

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvPosition = (TextView) convertView.findViewById(R.id.tvPosition);
        TextView tvValue = (TextView) convertView.findViewById(R.id.tvStatue);

        tvName.setText(ElectricItem.getConstructionName());
        tvPosition.setText(ElectricItem.getPosition());
        long now = System.currentTimeMillis()/1000;
        if((now-Long.parseLong(ElectricItem.getLastTime()))>3600){
            tvValue.setTextColor(0XFFCC6600);
            tvValue.setText("离线");
        }else {
            double current = Double.parseDouble(ElectricItem.getCurrent());
            double maxCurrent = Double.parseDouble(ElectricItem.getMaxCurrent());
            double temp = Double.parseDouble(ElectricItem.getTemp());
            double maxTemp = Double.parseDouble(ElectricItem.getMaxTemp());
            if(current<maxCurrent && temp<maxTemp){
                tvValue.setTextColor(0XFF333333);
                //tvValue.setText(ElectricItem.getValue() + "Mpa");
                tvValue.setText("正常");

            }else {
                tvValue.setTextColor(0XFFFF0000);
                //tvValue.setText(ElectricItem.getValue() + "Mpa");
                tvValue.setText("异常");
            }
        }




        return convertView;
    }


}
