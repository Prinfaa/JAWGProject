package com.example.dell_pc.unitpad.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell_pc.unitpad.R;
import com.example.dell_pc.unitpad.item.DocumentPicItem;
import com.example.dell_pc.unitpad.tools.downImageTask;


import java.util.HashMap;
import java.util.List;
import java.util.Map;



//import com.example.dell_pc.learnfragment.R;
//import com.example.dell_pc.learnfragment.downImageTask;


public class DocumentPicAdapter extends BaseAdapter {

    private Context context;
    private List<DocumentPicItem> documentPicList;

    public DocumentPicAdapter(Context context, List<DocumentPicItem> documentPicList) {
        this.context = context;
        this.documentPicList = documentPicList;
    }

    @Override
    public int getCount() {
        return documentPicList.size();
    }

    @Override
    public DocumentPicItem getItem(int position) {
        return documentPicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private Map<Integer, View> viewMap = new HashMap<Integer, View>();

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        convertView = this.viewMap.get(position);
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.pic_item, null);
        }

        holder.mPhoto = (ImageView) convertView.findViewById(R.id.ivPic);

        DocumentPicItem documentPicItem = documentPicList.get(position);
        TextView tvPage = (TextView) convertView.findViewById(R.id.tvPage);
        tvPage.setText("第" + String.valueOf(position + 1) + "页");


        convertView.setTag(holder);
        String pic_url = documentPicItem.getPicUrl();
        if (!holder.mPhoto.isDrawingCacheEnabled()) {
            holder.mPhoto.setTag(pic_url);
            new downImageTask().execute(holder.mPhoto);
            holder.mPhoto.setDrawingCacheEnabled(true);
            viewMap.put(position, convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //HttpUtils.setPicBitmap(holder.mPhoto, pic_url);

        return convertView;
    }

    public class ViewHolder {
        public ImageView mPhoto;
    }

}
