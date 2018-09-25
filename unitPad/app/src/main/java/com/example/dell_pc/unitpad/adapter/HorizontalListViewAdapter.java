package com.example.dell_pc.unitpad.adapter;

import android.content.Context;
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

/**
 * Created by dell-pc on 2017/10/28.
 */
public class HorizontalListViewAdapter extends BaseAdapter {
    List<DocumentPicItem> documentPicItemList;
    Context mContext;

    public HorizontalListViewAdapter(Context mContext, List<DocumentPicItem> documentPicItemList) {
        this.documentPicItemList = documentPicItemList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return documentPicItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    private Map<Integer, View> viewMap = new HashMap<Integer, View>();

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();

        convertView = this.viewMap.get(position);


        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.pic, null);
        }

        holder.mPhoto = (ImageView) convertView.findViewById(R.id.ivPic);
        TextView tvPage = (TextView) convertView.findViewById(R.id.tvPage);

        DocumentPicItem documentPicItem = documentPicItemList.get(position);
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
        return convertView;
    }

    public class ViewHolder {
        public ImageView mPhoto;
    }

}

