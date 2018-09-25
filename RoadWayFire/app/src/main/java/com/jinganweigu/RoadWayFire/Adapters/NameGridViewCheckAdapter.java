package com.jinganweigu.RoadWayFire.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.Entries.Contacts;
import com.jinganweigu.RoadWayFire.R;

import java.util.ArrayList;
import java.util.HashMap;

public class NameGridViewCheckAdapter extends BaseAdapter{

    private ArrayList<Contacts.ResultBean> list;
    private static HashMap<Integer,Boolean> isSelected;
    private Context context;
    private LayoutInflater inflater = null;
    public NameGridViewCheckAdapter(ArrayList<Contacts.ResultBean> list, Context context) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        isSelected = new HashMap<Integer, Boolean>();
        initDate();
    }
    private void initDate(){
        for(int i=0; i<list.size();i++) {
            getIsSelected().put(i,false);
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.gridviewitem, null);
            holder.cb = (CheckBox) convertView.findViewById(R.id.item_cb);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.cb.setText(list.get(position).getName());
//        holder.name.setText(list.get(position).getName());
        holder.cb.setChecked(getIsSelected().get(position));
        return convertView;
    }
    public static class ViewHolder{
        public CheckBox cb;

    }
    public static HashMap<Integer,Boolean> getIsSelected() {
        return isSelected;
    }


}
