package com.example.dell_pc.microstationreceivealarm.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dell_pc.microstationreceivealarm.R;
import com.example.dell_pc.microstationreceivealarm.item.CarItem;
import com.example.dell_pc.microstationreceivealarm.item.FireEngineItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FireCarAdapter extends BaseAdapter {

    private Context context;
//    private List<CarItem> carList;
    private List<FireEngineItem> carList;

    private Map<Integer, View> viewMap = new HashMap<Integer, View>();


    public FireCarAdapter(Context context, List<FireEngineItem> carList){
        this.context = context;
        this.carList = carList;
    }
    @Override
    public int getCount() {
        return carList.size();
    }

    @Override
    public FireEngineItem getItem(int position) {
        return carList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.fire_car_item,null);
        }

        final FireEngineItem carItem = carList.get(position);

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvLevel = (TextView) convertView.findViewById(R.id.tvLevel);

        tvName.setText(carItem.getName());
        tvLevel.setText(carItem.getLevel() + "%");

        int nLevel = Integer.parseInt(carItem.getLevel());

        if (nLevel >= 80) {
            tvLevel.setTextColor(Color.BLUE);
        } else if (nLevel >= 60) {
            tvLevel.setTextColor(Color.GREEN);
        } else if (nLevel >= 40) {
            tvLevel.setTextColor(0XFF996600);
        } else if (nLevel >= 20) {
            tvLevel.setTextColor(0XFFFF6600);
        } else {
            tvLevel.setTextColor(Color.RED);
        }


        return convertView;
    }


}
