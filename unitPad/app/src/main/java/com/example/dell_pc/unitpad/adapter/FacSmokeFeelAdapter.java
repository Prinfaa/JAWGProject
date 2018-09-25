package com.example.dell_pc.unitpad.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dell_pc.unitpad.R;
import com.example.dell_pc.unitpad.item.DeviceItem;
import com.example.dell_pc.unitpad.tools.TimeManager;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacSmokeFeelAdapter extends BaseAdapter {

    private Context context;
    private List<DeviceItem> facList;

    public FacSmokeFeelAdapter(Context context, List<DeviceItem> facList) {
        this.context = context;
        this.facList = facList;
    }


    @Override
    public int getCount() {
        return facList.size();
    }

    @Override
    public DeviceItem getItem(int position) {
        return facList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.fac_chose_item, null);
            holder. tvFac = (TextView) convertView.findViewById(R.id.tvFac);
            holder. tvPosition = (TextView) convertView.findViewById(R.id.tvPosition);
            holder. tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            holder. tvValue = (TextView) convertView.findViewById(R.id.tvValue);
            holder. tvUnit = (TextView) convertView.findViewById(R.id.tvUnit);
            holder.tvStatue = (TextView) convertView.findViewById(R.id.tvStatue);
            convertView.setTag(holder);
        }else{

            holder= (ViewHolder) convertView.getTag();

        }

        DeviceItem facItem = facList.get(position);
        String typeID = facItem.getTypeID();
        String state = facItem.getState();
        boolean online = facItem.isOnline();
        holder. tvFac.setText(facItem.getType());
        holder. tvPosition.setText(facItem.getPosition());
        if (!facItem.getLastTimestamp().equals("")) {
            holder.  tvTime.setText(facItem.getLastTimestamp());
        } else {
            holder.  tvTime.setText("");
        }

        holder.  tvValue.setText("/");
        if ( state.equals("正常")) {
            holder.tvStatue.setTextColor(0XFF009966);
            holder.tvStatue.setText("正常");
            holder.  tvValue.setTextColor(0XFF009966);
        }else {
            holder.tvStatue.setText(state);
            holder.tvStatue.setTextColor(0XFFFF6600);
            return convertView;
        }


        if (position % 2 == 0) {
            convertView.setBackgroundColor(0xFFFFFFFF);
        } else {
            convertView.setBackgroundColor(0xFFF7F7F7);
        }

        return convertView;
    }

    public class ViewHolder {
        public TextView tvStatue;
        TextView tvFac ;
        TextView tvPosition ;
        TextView tvTime ;
        TextView tvValue ;
        TextView tvUnit;
    }


}