package com.example.dell_pc.qr_code_patrol.adpter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell_pc.qr_code_patrol.R;
import com.example.dell_pc.qr_code_patrol.ShopProblem;
import com.example.dell_pc.qr_code_patrol.item.ShopProblemNotDealItem;
import com.example.dell_pc.qr_code_patrol.tools.TimeManager;
import com.example.dell_pc.qr_code_patrol.tools.downImageTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


//import com.example.dell_pc.learnfragment.R;
//import com.example.dell_pc.learnfragment.downImageTask;


public class ShopProblemNotDealAdapter extends BaseAdapter {

    private Context context;
    private List<ShopProblemNotDealItem> shopProblemNotDealList;
    private String shopID;
    public ShopProblemNotDealAdapter(Context context, List<ShopProblemNotDealItem> shopProblemNotDealList) {
        this.context = context;
        shopID = ShopProblem.shopID;
        this.shopProblemNotDealList = shopProblemNotDealList;
    }

    @Override
    public int getCount() {
        return shopProblemNotDealList.size();
    }

    @Override
    public ShopProblemNotDealItem getItem(int position) {
        return shopProblemNotDealList.get(position);
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

            convertView = LayoutInflater.from(context).inflate(R.layout.shop_problem_item, null);
        }



        TextView tvShop = (TextView) convertView.findViewById(R.id.tvShop);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        TextView tvProblem = (TextView) convertView.findViewById(R.id.tvProblem);


        holder.mPhoto = (ImageView) convertView.findViewById(R.id.ivPic);

        ShopProblemNotDealItem shopProblemNotDealItem = shopProblemNotDealList.get(position);


//        SpannableStringBuilder span = new SpannableStringBuilder("缩进");
//        span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 2,
//                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        String span = "\u3000";

        convertView.setTag(holder);
        tvShop.setText(span + shopProblemNotDealItem.getShopName());
        tvTime.setText(span + TimeManager.getStrTime2(shopProblemNotDealItem.getTimestamp()));
        tvProblem.setText(span + shopProblemNotDealItem.getProblem());
        String pic_url = shopProblemNotDealItem.getImageName();
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
