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
import com.example.dell.unittv.item.UnCheckItem;

import java.util.List;


public class UnCheckAdapter extends BaseAdapter {

    private Context context;
    private List<UnCheckItem> unCheckList;


    public UnCheckAdapter(Context context, List<UnCheckItem> unCheckList){
        this.context = context;
        this.unCheckList = unCheckList;
    }
    @Override
    public int getCount() {
        return unCheckList.size();
    }

    @Override
    public UnCheckItem getItem(int position) {
        return unCheckList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.uncheck_item,null);
        }
        TextView tvPosition = (TextView) convertView.findViewById(R.id.tvPosition);
        TextView tvFac = (TextView) convertView.findViewById(R.id.tvFac);

        UnCheckItem unCheckItem = unCheckList.get(position);
        tvPosition.setText(unCheckItem.getPosition());
        tvFac.setText(unCheckItem.getFac());
        return convertView;
    }


}
