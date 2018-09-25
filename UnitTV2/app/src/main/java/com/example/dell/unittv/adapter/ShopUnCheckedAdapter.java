package com.example.dell.unittv.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dell.unittv.R;
import com.example.dell.unittv.item.ShopUnCheckedItem;

import java.util.List;


public class ShopUnCheckedAdapter extends BaseAdapter {

    private Context context;
    private List<ShopUnCheckedItem> shopUnCheckedItemList;

    public ShopUnCheckedAdapter(Context context, List<ShopUnCheckedItem> shopUnCheckedItemList) {
        this.context = context;
        this.shopUnCheckedItemList = shopUnCheckedItemList;
    }

    @Override
    public int getCount() {
        return shopUnCheckedItemList.size();
    }

    @Override
    public ShopUnCheckedItem getItem(int position) {
        return shopUnCheckedItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.shop_unchecked_item, null);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);

        ShopUnCheckedItem shopUnCheckedItem = shopUnCheckedItemList.get(position);
        tvName.setText(shopUnCheckedItem.getShopName());
        return convertView;
    }


}
