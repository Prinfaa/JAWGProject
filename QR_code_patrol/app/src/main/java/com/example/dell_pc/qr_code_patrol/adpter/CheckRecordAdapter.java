package com.example.dell_pc.qr_code_patrol.adpter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell_pc.qr_code_patrol.CheckedCard;
import com.example.dell_pc.qr_code_patrol.R;
import com.example.dell_pc.qr_code_patrol.item.CheckRecordItem;
import com.example.dell_pc.qr_code_patrol.tools.HttpUtils;
import com.example.dell_pc.qr_code_patrol.tools.downImageTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class CheckRecordAdapter extends BaseAdapter {

    private Context context;
    private List<CheckRecordItem> checkRecordList;

    private Map<Integer, View> viewMap = new HashMap<Integer, View>();


    public CheckRecordAdapter(Context context, List<CheckRecordItem> checkRecordList){
        this.context = context;
        this.checkRecordList = checkRecordList;
    }
    @Override
    public int getCount() {
        return checkRecordList.size();
    }

    @Override
    public CheckedCard getItem(int position) {
        return checkRecordList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.check_record_item,null);
        }
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        holder.mPhoto = (ImageView) convertView.findViewById(R.id.ivCheck);
        convertView.setBackgroundResource(R.drawable.bg112);



        CheckRecordItem checkRecordItem = checkRecordList.get(position);
        tvTime.setText(checkRecordItem.getTime());

//System.out.println(picUrl);
        convertView.setTag(holder);
        String picUrl = checkRecordItem.getPicUrl();
        if (!holder.mPhoto.isDrawingCacheEnabled()) {
            holder.mPhoto.setTag(checkRecordItem.getPicUrl());
            new downImageTask().execute(holder.mPhoto);
            holder.mPhoto.setDrawingCacheEnabled(true);
            viewMap.put(position, convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HttpUtils.setPicBitmap(holder.mPhoto, picUrl);

        return convertView;
    }

    public class ViewHolder {
        public TextView mNameText;
        public ImageView mPhoto;
    }

}
