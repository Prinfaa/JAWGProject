package com.jinganweigu.entrysystem.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.entry.ProjectStatisticListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/23 0023.
 */

public class ProjectDeviceStatisticAdapter extends BaseAdapter {
    private Context context;
    private List<ProjectStatisticListBean.ResultBean> lists;
    private ViewHolder mViewHolder;


    public ProjectDeviceStatisticAdapter(Context context, List<ProjectStatisticListBean.ResultBean> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
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

            convertView = View.inflate(context, R.layout.item_project_staistics_list, null);

            mViewHolder = new ViewHolder(convertView);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

      mViewHolder.tvCompanyName.setText(lists.get(position).getCom_name());
      mViewHolder.tvDeviceAll.setText("设备总计："+lists.get(position).getAll());
      mViewHolder.tvDeviceAlarm.setText("一键报警："+lists.get(position).getAlarm());
      mViewHolder.tvDeviceFlame.setText("火焰探测："+lists.get(position).getFlame());
      mViewHolder.tvDeviceWater.setText("水压水位："+lists.get(position).getWater());
      mViewHolder.tvDeviceCarAlarm.setText("车载报警："+lists.get(position).getVehicular_unit());
      mViewHolder.tvElectricChack.setText("电气检测："+lists.get(position).getGas());





        return convertView;

    }

    static class ViewHolder {
        @BindView(R.id.tv_company_name)
        TextView tvCompanyName;
        @BindView(R.id.tv_device_all)
        TextView tvDeviceAll;
        @BindView(R.id.tv_device_alarm)
        TextView tvDeviceAlarm;
        @BindView(R.id.tv_device_flame)
        TextView tvDeviceFlame;
        @BindView(R.id.tv_device_water)
        TextView tvDeviceWater;
        @BindView(R.id.tv_device_car_alarm)
        TextView tvDeviceCarAlarm;
        @BindView(R.id.tv_electric_chack)
        TextView tvElectricChack;
        @BindView(R.id.tv_name_and_time)
        TextView tvNameAndTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
