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
import com.example.dell.unittv.item.MonthItem;

import java.util.List;


public class MonthAdapter extends BaseAdapter {

    private Context context;
    private List<MonthItem> monthItemList;


    public MonthAdapter(Context context, List<MonthItem> monthItemList) {
        this.context = context;
        this.monthItemList = monthItemList;
    }

    @Override
    public int getCount() {
        return monthItemList.size();
    }

    @Override
    public MonthItem getItem(int position) {
        return monthItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.month_item, null);
        }
        TextView tvMonth = (TextView) convertView.findViewById(R.id.tvMonth);

        MonthItem monthItem = monthItemList.get(position);

        tvMonth.setText(monthItem.getMonth());


        if(position%2==0){
            convertView.setBackgroundColor(0xFFFFFFFF);
        }
        else
        {
            convertView.setBackgroundColor(0xFFF7F7F7);
        }
        return convertView;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    private int selectItem = 0;
}
