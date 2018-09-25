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
import com.example.dell_pc.unitpad.item.CheckedItem;
import com.example.dell_pc.unitpad.tools.TimeManager;

import java.util.List;


public class Checked1Adapter extends BaseAdapter {

    private Context context;
    private List<CheckedItem> checkedList;


    public Checked1Adapter(Context context, List<CheckedItem> checkedList){
        this.context = context;
        this.checkedList = checkedList;
    }
    @Override
    public int getCount() {
        return checkedList.size();
    }

    @Override
    public CheckedItem getItem(int position) {
        return checkedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.checked1_item,null);
        }
        TextView tvPosition = (TextView) convertView.findViewById(R.id.tvPosition);
        TextView tvFac = (TextView) convertView.findViewById(R.id.tvFac);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        TextView tvStatue = (TextView) convertView.findViewById(R.id.tvStatue);


        CheckedItem checkedItem = checkedList.get(position);
        tvPosition.setText(checkedItem.getPosition());
        tvFac.setText(checkedItem.getFac());
        tvTime.setText(TimeManager.getStrTime2(checkedItem.getDateTime()));
        String strStatue;
        if(!checkedItem.getProblemID().equals("")){
            strStatue = "不正常";
//            tvStatue.setTextColor(0xFFFF0000);
        }else {
            strStatue = "正常";
//            tvStatue.setTextColor(0xFF038DAF);
        }
        tvStatue.setText(strStatue);

//        if (position % 2 == 1) {
//            convertView.setBackgroundColor(0xFFFFFFFF);
//        } else {
//            convertView.setBackgroundColor(0xFFF7F7F7);
//        }

        return convertView;
    }
    public  void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }
    private int  selectItem=0;


}
