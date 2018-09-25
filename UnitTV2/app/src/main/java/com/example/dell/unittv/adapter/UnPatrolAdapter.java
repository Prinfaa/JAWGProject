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
import com.example.dell.unittv.item.UnPatrolItem;

import java.util.List;


public class UnPatrolAdapter extends BaseAdapter {

    private Context context;
    private List<UnPatrolItem> unPatrolList;


    public UnPatrolAdapter(Context context, List<UnPatrolItem> unPatrolList){
        this.context = context;
        this.unPatrolList = unPatrolList;
    }



    @Override
    public int getCount() {
        return unPatrolList.size();
    }

    @Override
    public UnPatrolItem getItem(int position) {
        return unPatrolList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.unpatrol_item,null);
        }
        TextView tvPosition = (TextView) convertView.findViewById(R.id.tvPosition);

        UnPatrolItem unPatrolItem = unPatrolList.get(position);
        tvPosition.setText(unPatrolItem.getPosition());
        return convertView;
    }


}
