package com.example.dell.unittv.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.dell.unittv.R;
import com.example.dell.unittv.item.CheckedItem;
import com.example.dell.unittv.tools.TimeManager;
import com.example.dell.unittv.tools.downImageTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CheckAbnormalAdapter extends BaseAdapter {

    private Context context;
    private List<CheckedItem> checkedList;


    public CheckAbnormalAdapter(Context context, List<CheckedItem> checkedList){
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

    private Map<Integer, View> viewMap = new HashMap<Integer, View>();


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        convertView = this.viewMap.get(position);

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.check_abnormal_item,null);
        }
        convertView.setTag(holder);

        TextView tvPosition = (TextView) convertView.findViewById(R.id.tvPosition);
        TextView tvFac = (TextView) convertView.findViewById(R.id.tvFac);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        TextView tvStatue = (TextView) convertView.findViewById(R.id.tvStatue);
        TextView tvProblem = (TextView) convertView.findViewById(R.id.tvProblem);
        holder.mPhoto = (ImageView) convertView.findViewById(R.id.ivCheck);

        CheckedItem checkedItem = checkedList.get(position);
        tvPosition.setText(checkedItem.getPosition());
        tvFac.setText(checkedItem.getFac());
        tvTime.setText(TimeManager.getStrTime2(checkedItem.getDateTime()));
        String strStatue;
        if(!checkedItem.getProblemID().equals("")){
            strStatue = "不正常";
            tvStatue.setTextColor(0xFFFF0000);
        }else {
            strStatue = "正常";
            tvStatue.setTextColor(0xFF038DAF);
        }
        tvStatue.setText(strStatue);
        tvProblem.setText(checkedItem.getProblem());
        String pic_url = checkedItem.getImageName();

        if (!holder.mPhoto.isDrawingCacheEnabled()) {
            holder.mPhoto.setTag(pic_url);
            new downImageTask().execute(holder.mPhoto);
            holder.mPhoto.setDrawingCacheEnabled(true);
            viewMap.put(position, convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


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

    public class ViewHolder {
        public ImageView mPhoto;
    }

}
