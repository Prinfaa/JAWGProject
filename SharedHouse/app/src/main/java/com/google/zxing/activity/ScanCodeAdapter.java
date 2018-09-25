package com.google.zxing.activity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.adapter.GroupAdapter;

import java.util.List;

/**
 * Created by Administrator on 2018/2/28 0028.
 */

public class ScanCodeAdapter extends BaseAdapter {

    private Context context;

    private List<String> list;

    private int from;

    public ScanCodeAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    public ScanCodeAdapter(Context context, List<String> list, int from) {
        this.context = context;
        this.list = list;
        this.from = from;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {


        ViewHolder holder;
        if (convertView==null) {
            convertView= LayoutInflater.from(context).inflate(R.layout.scan_code_item, null);
            holder=new ViewHolder();

            convertView.setTag(holder);

            holder.groupItem=(TextView) convertView.findViewById(R.id.groupItem);

        }
        else{
            holder=(ViewHolder) convertView.getTag();
        }
//        holder.groupItem.setTextColor(Color.BLACK);
        if(from==0){
            holder.groupItem.setText(list.get(position));
        }else{
            holder.groupItem.setTextColor(Color.BLACK);
            holder.groupItem.setText(list.get(position));
        }


        return convertView;
    }

    static class ViewHolder {
        TextView groupItem;
    }

}

