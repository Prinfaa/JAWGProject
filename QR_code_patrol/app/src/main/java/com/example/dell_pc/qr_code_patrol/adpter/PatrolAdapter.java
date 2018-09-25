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
import com.example.dell_pc.qr_code_patrol.item.CheckedItem;
import com.example.dell_pc.qr_code_patrol.item.PatrolItem;
import com.example.dell_pc.qr_code_patrol.tools.TimeManager;

import java.util.List;


public class PatrolAdapter extends BaseAdapter {

    private Context context;
    private List<PatrolItem> patrolList;


    public PatrolAdapter(Context context, List<PatrolItem> patrolList){
        this.context = context;
        this.patrolList = patrolList;
    }
    @Override
    public int getCount() {
        return patrolList.size();
    }

    @Override
    public PatrolItem getItem(int position) {
        return patrolList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.my_patrol_item,null);
        }
        TextView tvLeft = (TextView) convertView.findViewById(R.id.tvLeft);
        TextView tvRight = (TextView) convertView.findViewById(R.id.tvRight);
        LinearLayout llUp = (LinearLayout) convertView.findViewById(R.id.llUp);
        LinearLayout llDown = (LinearLayout) convertView.findViewById(R.id.llDown);

        PatrolItem patrolItem = patrolList.get(patrolList.size() - position - 1);
        String patrolDateTime = TimeManager.getStrTime2(patrolItem.getDateTime());
        tvLeft.setText("");
        tvRight.setText("");
        if ((patrolList.size() - position - 1) % 2 == 1) {
            tvLeft.setText(patrolDateTime + "\n\r" + patrolItem.getPosition());
//            tvRight.setVisibility(View.INVISIBLE);
            //convertView.setBackgroundColor(0xFFFFFFFF);
        } else {
            tvRight.setText(patrolDateTime + "\n\r" + patrolItem.getPosition());
//            tvLeft.setVisibility(View.INVISIBLE);
//            convertView.setBackgroundColor(0xFFF7F7F7);
        }

        if(patrolList.size() == 1){
            llUp.setVisibility(View.INVISIBLE);
            llDown.setVisibility(View.INVISIBLE);
        }else {
            if(position == 0){
                llDown.setVisibility(View.VISIBLE);
                llUp.setVisibility(View.INVISIBLE);
            }else if(position == patrolList.size()-1){
                llUp.setVisibility(View.VISIBLE);
                llDown.setVisibility(View.INVISIBLE);
            }else {
                llUp.setVisibility(View.VISIBLE);
                llDown.setVisibility(View.VISIBLE);
            }
        }


        return convertView;
    }
    public  void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }
    private int  selectItem=0;


}
