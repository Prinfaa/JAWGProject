package com.example.dell_pc.qr_code_patrol.adpter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dell_pc.qr_code_patrol.PatrolHome;
import com.example.dell_pc.qr_code_patrol.R;
import com.example.dell_pc.qr_code_patrol.item.CheckedItem;

import java.util.List;


public class CheckInfoAdapter extends BaseAdapter {

    private Context context;
    private List<CheckedItem> checkInfoList;


    public CheckInfoAdapter(Context context, List<CheckedItem> checkInfoList){
        this.context = context;
        this.checkInfoList = checkInfoList;
    }
    @Override
    public int getCount() {
        return checkInfoList.size();
    }

    @Override
    public CheckedItem getItem(int position) {
        return checkInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.checked_item_main_page,null);
        }
        //Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/HYI_TXDXJ.ttf");
        TextView tvFac = (TextView) convertView.findViewById(R.id.tv_fac);
        TextView tvPosition = (TextView) convertView.findViewById(R.id.tv_position);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tv_time);
        TextView tvStatue = (TextView) convertView.findViewById(R.id.tv_statue);
        //tvFac.setTypeface(tf);
        CheckedItem item = checkInfoList.get(position);
        if(PatrolHome.getStatue()==0){
            tvFac.setTextColor(0XFF038DAF);
            tvTime.setText(item.getPosition());
            tvTime.setTextColor(0XFF4488FF);
            tvPosition.setText("巡检时间：" + item.getDateTime());
            tvPosition.setTextColor(0X88000000);
        }else if(PatrolHome.getStatue()==1){
            tvFac.setTextColor(0X88FF0000);
            tvTime.setText("");
            tvPosition.setText(item.getPosition());
            tvPosition.setTextColor(0XFF4488FF);


        }
        tvFac.setText(item.getFac());
        if(item.getStatue().equals("1")){
            tvStatue.setVisibility(View.VISIBLE);
        }else {
            tvStatue.setVisibility(View.INVISIBLE);
        }


        if (position % 2 == 0) {
            convertView.setBackgroundColor(0xFFFFFFFF);
        } else {
            convertView.setBackgroundColor(0xFFF7F7F7);
        }

        return convertView;
    }


}
