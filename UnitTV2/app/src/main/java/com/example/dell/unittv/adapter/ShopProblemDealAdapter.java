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
import com.example.dell.unittv.item.ShopProblemDealedItem;
import com.example.dell.unittv.tools.TimeManager;
import com.example.dell.unittv.tools.downImageTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


//import com.example.dell_pc.learnfragment.R;
//import com.example.dell_pc.learnfragment.downImageTask;


public class ShopProblemDealAdapter extends BaseAdapter {

    private Context context;
    private List<ShopProblemDealedItem> shopProblemDealList;
    private String shopID;
    public ShopProblemDealAdapter(Context context, List<ShopProblemDealedItem> shopProblemDealList) {
        this.context = context;
//        shopID = ShopProblem.shopID;
        this.shopProblemDealList = shopProblemDealList;
    }

    @Override
    public int getCount() {
        return shopProblemDealList.size();
    }

    @Override
    public ShopProblemDealedItem getItem(int position) {
        return shopProblemDealList.get(position);
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

            convertView = LayoutInflater.from(context).inflate(R.layout.shop_problem_deal_item, null);
        }



        TextView tvShop = (TextView) convertView.findViewById(R.id.tvShop);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        TextView tvProblem = (TextView) convertView.findViewById(R.id.tvProblem);
        TextView tvDealTime = (TextView) convertView.findViewById(R.id.tvDealTime);


        holder.ivProblem = (ImageView) convertView.findViewById(R.id.ivProblemPic);
        holder.ivDeal = (ImageView) convertView.findViewById(R.id.ivDealPic);

        ShopProblemDealedItem shopProblemDealedItem = shopProblemDealList.get(position);


        String span = "\u3000";

        convertView.setTag(holder);
        tvShop.setText(span + shopProblemDealedItem.getShopName());
        tvTime.setText(span + TimeManager.getStrTime2(shopProblemDealedItem.getTimestamp()));
        tvProblem.setText(span + shopProblemDealedItem.getProblem());
        tvDealTime.setText(span + TimeManager.getStrTime2(shopProblemDealedItem.getDealTimestamp()));
        String problem_pic_url = shopProblemDealedItem.getImageName();
        String deal_pic_url = shopProblemDealedItem.getDealPic();
        if (!holder.ivProblem.isDrawingCacheEnabled()) {
            holder.ivProblem.setTag(problem_pic_url);
            holder.ivDeal.setTag(deal_pic_url);
            new downImageTask().execute(holder.ivProblem);
            new downImageTask().execute(holder.ivDeal);
            holder.ivProblem.setDrawingCacheEnabled(true);
            holder.ivDeal.setDrawingCacheEnabled(true);
            viewMap.put(position, convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    public class ViewHolder {
        public ImageView ivProblem;
        public ImageView ivDeal;
    }

}
