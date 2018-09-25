package com.jinganweigu.entrysystem.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.entry.EnterStaisticsBean;
import com.jinganweigu.entrysystem.entry.OutIntoStatisticBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/3 0003.
 */

public class EnterStatisticAdapter extends BaseAdapter {

    private Context context;
    private ViewHolder mviewHolder;
    private List<OutIntoStatisticBean.ResultBean> lists;
    private int SHALLOW_TYPE=0;
    private int DARK_TYPE=1;

    private int currentType;


    public EnterStatisticAdapter(Context context, List<OutIntoStatisticBean.ResultBean> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        currentType=getItemViewType(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mpchart_enter_statistic, null);
            if(currentType==0){

                convertView.setBackgroundColor(Color.parseColor("#aecdfb"));

            }else{

                convertView.setBackgroundColor(Color.parseColor("#cde1fc"));

            }
            mviewHolder=new ViewHolder(convertView);


            convertView.setTag(mviewHolder);


        }else{

            mviewHolder= (ViewHolder) convertView.getTag();

        }
        mviewHolder.tvIn.setText(lists.get(position).getWarehouse_in()+"");
        mviewHolder.tvOut.setText(lists.get(position).getWarehouse_out()+"");
        mviewHolder.tvReturn.setText(lists.get(position).getWarehouse_return()+"");

        mviewHolder.tvName.setText(lists.get(position).getMitu_type());



        return convertView;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position%2==0) {

            return SHALLOW_TYPE;

        } else {

            return DARK_TYPE;

        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    static class ViewHolder {
        @BindView(R.id.tv_in)
        TextView tvIn;
        @BindView(R.id.tv_out)
        TextView tvOut;
        @BindView(R.id.tv_return)
        TextView tvReturn;
        @BindView(R.id.name)
        TextView tvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
