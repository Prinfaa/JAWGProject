package com.jinganweigu.entrysystem.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.entry.ReturnProjectDetailBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectDeviceDeticalAdapter extends BaseAdapter {

    private Context context;
    private List<ReturnProjectDetailBean.ResultBean.BadBean> badBeanList;
    private List<ReturnProjectDetailBean.ResultBean.NiceBean> niceBeanList;
    private int mTag = 0;// 0 是正常退货  1是损坏退货
    private ViewHolder mHolder;

    public ProjectDeviceDeticalAdapter(Context context, List<ReturnProjectDetailBean.ResultBean.BadBean> badBeanList, int mTag) {
        this.context = context;
        this.badBeanList = badBeanList;
        this.mTag = mTag;
    }

    public ProjectDeviceDeticalAdapter(Context context, List<ReturnProjectDetailBean.ResultBean.NiceBean> niceBeanList) {
        this.context = context;
        this.niceBeanList = niceBeanList;
        this.mTag = mTag;
    }

    @Override
    public int getCount() {

        if (mTag == 0) {

            return niceBeanList.size();
        } else {
            return badBeanList.size();
        }


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

            convertView = View.inflate(context, R.layout.item_return_back_project, null);

            mHolder = new ViewHolder(convertView);

            convertView.setTag(mHolder);

        } else {

            mHolder = (ViewHolder) convertView.getTag();

        }

        if (mTag == 0) {
            mHolder.tvDeviceId.setText(niceBeanList.get(position).getChip_IMEI());
            mHolder.tvDeviceType.setText(niceBeanList.get(position).getMitu_type());
            mHolder.tvTime.setText(niceBeanList.get(position).getReturn_time_nice());

        } else {

            mHolder.tvDeviceId.setText(badBeanList.get(position).getChip_IMEI());
            mHolder.tvDeviceType.setText(badBeanList.get(position).getMitu_type());
            mHolder.tvTime.setText(badBeanList.get(position).getReturn_time_bad());


        }


        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_device_id)
        TextView tvDeviceId;
        @BindView(R.id.tv_device_type)
        TextView tvDeviceType;
        @BindView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
