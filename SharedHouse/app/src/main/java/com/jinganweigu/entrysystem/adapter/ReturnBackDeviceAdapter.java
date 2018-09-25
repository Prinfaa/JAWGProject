package com.jinganweigu.entrysystem.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.activities.ReturnProjectDeticalActivity;
import com.jinganweigu.entrysystem.entry.ReturnBackDeviceBean;
import com.jinganweigu.entrysystem.utils.LoadImageUtil;
import com.jinganweigu.entrysystem.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReturnBackDeviceAdapter extends BaseAdapter {

    private Context context;
    private List<ReturnBackDeviceBean.ResultBean.ParticularBean> list;
    private ViewHolder holder;

    public ReturnBackDeviceAdapter(Context context, List<ReturnBackDeviceBean.ResultBean.ParticularBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = View.inflate(context, R.layout.item_return_back_things, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }
        holder.tvBadReturn.setText(list.get(position).getDamage());
        holder.tvGoodReturn.setText(list.get(position).getNormal());
        holder.tvReturnProject.setText(list.get(position).getReceive_unit());

        holder.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(list.get(position).getReceive_unit())) {

                    LoadImageUtil.IntentMethod.getInstance().startMethodWithString(context, ReturnProjectDeticalActivity.class, "project", list.get(position).getReceive_unit());

                } else {

                    ToastUtil.showToast("项目名称为空", context);

                }


            }
        });


        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.tv_return_project)
        TextView tvReturnProject;
        @BindView(R.id.tv_good_return)
        TextView tvGoodReturn;
        @BindView(R.id.tv_bad_return)
        TextView tvBadReturn;
        @BindView(R.id.ll_root)
        LinearLayout llRoot;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
