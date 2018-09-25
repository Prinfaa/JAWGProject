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
import com.example.dell_pc.microstationreceivealarm.item.AlarmKeyItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AlarmKeyAdapter extends BaseAdapter {

    private Context context;
    private List<AlarmKeyItem> alarmKeyList;

    private Map<Integer, View> viewMap = new HashMap<Integer, View>();


    public AlarmKeyAdapter(Context context, List<AlarmKeyItem> alarmKeyList){
        this.context = context;
        this.alarmKeyList = alarmKeyList;
    }
    @Override
    public int getCount() {
        return alarmKeyList.size();
    }

    @Override
    public AlarmKeyItem getItem(int position) {
        return alarmKeyList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.alarm_key_item,null);
        }

        final AlarmKeyItem alarmKeyItem = alarmKeyList.get(position);

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvStatue = (TextView) convertView.findViewById(R.id.tvStatue);

        tvName.setText(alarmKeyItem.getName());
        long now = System.currentTimeMillis()/1000;
        if((now-Long.parseLong(alarmKeyItem.getLastTime()))>3600){
            tvStatue.setTextColor(0XFFCC6600);
            tvStatue.setText("离线");
        }else {
            tvStatue.setTextColor(0XFF333333);
            tvStatue.setText("正常");
        }

        return convertView;
    }


}
