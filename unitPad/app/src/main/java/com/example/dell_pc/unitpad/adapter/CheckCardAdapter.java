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
import com.example.dell_pc.unitpad.item.CheckCardItem;

import java.util.List;


//import com.example.dell_pc.learnfragment.CheckedCard;
//import com.example.dell_pc.learnfragment.R;


public class CheckCardAdapter extends BaseAdapter {

    private Context context;
    private List<CheckCardItem> checkedCardList;


    public CheckCardAdapter(Context context, List<CheckCardItem> checkedCardList){
        this.context = context;
        this.checkedCardList = checkedCardList;
    }
    @Override
    public int getCount() {
        return checkedCardList.size();
    }

    @Override
    public CheckCardItem getItem(int position) {
        return checkedCardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.checked_card_item,null);
        }
        TextView Position = (TextView) convertView.findViewById(R.id.tvPosition);
        TextView CardNo = (TextView) convertView.findViewById(R.id.tvCardNo);
        TextView Fac = (TextView) convertView.findViewById(R.id.tvFac);


        CheckCardItem checkCardItem = checkedCardList.get(position);
        Position.setText(checkCardItem.getPosition());
        CardNo.setText(checkCardItem.getCardNo());
        Fac.setText(checkCardItem.getFac());

        if (position == selectItem) {
            convertView.setBackgroundResource(R.drawable.bg112);
        }
        else {
            convertView.setBackgroundResource(R.drawable.bg113);
        }

        return convertView;
    }
    public  void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }
    private int  selectItem=0;
}
