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

import com.example.dell_pc.qr_code_patrol.R;
import com.example.dell_pc.qr_code_patrol.item.ShopNoProblemItem;
import com.example.dell_pc.qr_code_patrol.item.ShopProblemNotDealItem;
import com.example.dell_pc.qr_code_patrol.tools.TimeManager;
import com.example.dell_pc.qr_code_patrol.tools.downImageTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class ShopNoProblemAdapter extends BaseAdapter {

    private Context context;
    private List<ShopNoProblemItem> shopNoProblemList;

    public ShopNoProblemAdapter(Context context, List<ShopNoProblemItem> shopNoProblemList) {
        this.context = context;
        this.shopNoProblemList = shopNoProblemList;
    }

    @Override
    public int getCount() {
        return shopNoProblemList.size();
    }

    @Override
    public ShopNoProblemItem getItem(int position) {
        return shopNoProblemList.get(position);
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

            convertView = LayoutInflater.from(context).inflate(R.layout.shop_no_problem_item, null);
        }
        TextView tvShop = (TextView) convertView.findViewById(R.id.tvShop);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);


        holder.mPhoto = (ImageView) convertView.findViewById(R.id.ivPic);

        ShopNoProblemItem shopNoProblemItem = shopNoProblemList.get(position);


        convertView.setTag(holder);
        String span = "\u3000";

        tvShop.setText(span + shopNoProblemItem.getShopName());
        tvTime.setText(span + TimeManager.getStrTime2(shopNoProblemItem.getTimestamp()));
        String pic_url = shopNoProblemItem.getImageName();
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
