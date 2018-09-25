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
import com.example.dell.unittv.item.DeviceItem;
import com.example.dell.unittv.tools.TimeManager;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FacAdapter extends BaseAdapter {

    private Context context;
    private Map<Integer, View> viewMap = new HashMap<Integer, View>();
    private List<DeviceItem> facList;

    public FacAdapter(Context context, List<DeviceItem> facList){
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

        ViewHolder holder = new ViewHolder();
        convertView = this.viewMap.get(position);

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.fac_chose_item,null);
        }
        TextView tvFac = (TextView) convertView.findViewById(R.id.tvFac);
        TextView tvPosition = (TextView) convertView.findViewById(R.id.tvPosition);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        TextView tvValue = (TextView) convertView.findViewById(R.id.tvValue);
        TextView tvUnit = (TextView) convertView.findViewById(R.id.tvUnit);
        holder.tvStatue = (TextView) convertView.findViewById(R.id.tvStatue);

        DeviceItem facItem = facList.get(position);
        String typeID = facItem.getTypeID();
        String state = facItem.getState();
        boolean online = facItem.isOnline();
        tvFac.setText(facItem.getType());
        tvPosition.setText(facItem.getPosition());
        if(!facItem.getLastTimestamp().equals("")){
            tvTime.setText(TimeManager.getStrTime4(facItem.getLastTimestamp()));
        }else {
            tvTime.setText("");
        }
        String value = "";

        if(!facItem.getValue().equals("")){
            float valueF = Float.valueOf(facItem.getValue());
            if(typeID.equals("1")||typeID.equals("2")||typeID.equals("7")){
                tvUnit.setText("Mpa");
                DecimalFormat fnum = new DecimalFormat("##0.000");
                value = fnum.format(valueF);

            }else if(typeID.equals("3")||typeID.equals("4")){
                tvUnit.setText("%");
                value = String.valueOf((int)valueF);
            }else if(typeID.equals("13")||typeID.equals("8")||typeID.equals("12")){
                tvUnit.setText("A");
                value = String.valueOf((int)valueF);
            }else {

                tvUnit.setText("");

            }

        }else {
            tvUnit.setText("");
        }





        tvValue.setText(value);
        if(online == false){
            tvTime.setText(TimeManager.getStrTime1(facItem.getLastTimestamp()));
            tvValue.setText("");
            tvUnit.setText("");
            holder.tvStatue.setText("离线");
            holder.tvStatue.setTextColor(0XFFFF6600);
            return convertView;
        }
        if (state.equals("alarm")){
            tvValue.setTextColor(0XFFCE0F0F);
            holder.tvStatue.setTextColor(0XFFCE0F0F);
            holder.tvStatue.setText("异常");
        }else if(state.equals("normal")){
            holder.tvStatue.setTextColor(0XFF88CFFF);
            holder.tvStatue.setText("正常");
            tvValue.setTextColor(0XFF88CFFF);
        }

        if (position % 2 == 0) {
            convertView.setBackgroundColor(0x00FFFFFF);
        } else {
            convertView.setBackgroundColor(0x11F7F7F7);
        }

        return convertView;
    }

    public class ViewHolder {
        public TextView tvStatue;
    }



}
