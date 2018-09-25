package com.example.dell_pc.qr_code_patrol.adpter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dell_pc.qr_code_patrol.R;
import com.example.dell_pc.qr_code_patrol.item.PatrolItem;
import com.example.dell_pc.qr_code_patrol.item.PatroledItem;
import com.example.dell_pc.qr_code_patrol.tools.TimeManager;

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

        PatroledItem patroledItem = patroledList.get(patroledList.size() - position - 1);
        String times = patroledItem.getTimes();
        tvPosition.setText(patroledItem.getPosition());
        tvTimes.setText(times+"次");

        return convertView;
    }



}
