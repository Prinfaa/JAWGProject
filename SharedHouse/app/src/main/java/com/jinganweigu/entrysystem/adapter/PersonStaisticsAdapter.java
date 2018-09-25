package com.jinganweigu.entrysystem.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.entry.PersonStatisticsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/24 0024.
 */

public class PersonStaisticsAdapter extends BaseAdapter {

    private List<PersonStatisticsBean.ResultBean.FirstBean> data;
    private Context context;
    private ViewHolder mViewHolder;

    public PersonStaisticsAdapter(List<PersonStatisticsBean.ResultBean.FirstBean> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = View.inflate(context, R.layout.item_person_statistics_data, null);

            mViewHolder = new ViewHolder(convertView);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.tvName.setText(data.get(position).getWorker_name());
        mViewHolder.tvAll.setText("领用总计：" + data.get(position).getAll());
        mViewHolder.tvInstall.setText("安装 " + data.get(position).getInstall());
        mViewHolder.tvReturn.setText("退货 " + data.get(position).getSend_back());
        mViewHolder.tvUninstall.setText("待安装 " + data.get(position).getUninstall());


        return convertView;

    }

    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_all)
        TextView tvAll;
        @BindView(R.id.tv_install)
        TextView tvInstall;
        @BindView(R.id.tv_uninstall)
        TextView tvUninstall;
        @BindView(R.id.tv_return)
        TextView tvReturn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
