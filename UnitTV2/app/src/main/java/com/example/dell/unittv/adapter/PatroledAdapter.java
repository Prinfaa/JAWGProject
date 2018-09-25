package com.example.dell.unittv.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dell.unittv.R;
import com.example.dell.unittv.item.PatroledItem;
import com.example.dell.unittv.tools.TimeManager;

import java.util.List;


public class PatroledAdapter extends BaseAdapter {

    private Context context;
    private List<PatroledItem> patroledList;


    public PatroledAdapter(Context context, List<PatroledItem> patroledList){
        this.context = context;
        this.patroledList = patroledList;
    }
    @Override
    public int getCount() {
        return patroledList.size();
    }

    @Override
    public PatroledItem getItem(int position) {
        return patroledList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.patroled_item,null);
        }
        TextView tvPosition = (TextView) convertView.findViewById(R.id.tvPosition);
        TextView tvTimes = (TextView) convertView.findViewById(R.id.tvTimes);

        PatroledItem patroledItem = patroledList.get(position);
        String times = patroledItem.getTimes();
        tvPosition.setText(patroledItem.getPosition());
        tvTimes.setText(TimeManager.getStrTime5(times));

        return convertView;
    }



}
