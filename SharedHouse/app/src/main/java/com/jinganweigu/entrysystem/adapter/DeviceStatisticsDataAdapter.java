package com.jinganweigu.entrysystem.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.entry.DeviceStatisticsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/20 0020.
 */

public class DeviceStatisticsDataAdapter extends BaseAdapter {

    private Context context;
    private List<DeviceStatisticsBean.ResultBean> lists;
    private int currentType;
    private ViewHolder mHolder;
    private int SHALLOW_TYPE=0;
    private int DARK_TYPE=1;

    public DeviceStatisticsDataAdapter(Context context, List<DeviceStatisticsBean.ResultBean> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        currentType=getItemViewType(position);


        if (convertView == null) {

            convertView = View.inflate(context, R.layout.item_device_statistics, null);

            if(currentType==0){

                convertView.setBackgroundColor(Color.parseColor("#aecdfb"));

            }else{

                convertView.setBackgroundColor(Color.parseColor("#cde1fc"));

            }


            mHolder=new ViewHolder(convertView);

            convertView.setTag(mHolder);

        }else{

            mHolder= (ViewHolder) convertView.getTag();

        }

        mHolder.tvDeviceName.setText(lists.get(position).getMitu_type());//设备名称
        mHolder.tvStock.setText(lists.get(position).getInventory());//库存
        mHolder.tvHalfDevice.setText(lists.get(position).getArrsemi_finished());//半成品
        mHolder.tvWaitInstall.setText(lists.get(position).getUninstall());//带安装
        mHolder.tvInstall.setText(lists.get(position).getArrinstall());//安装
        mHolder.tvDeviceReturn.setText(lists.get(position).getArrreturn());//退货



        return convertView;
    }



    @Override
    public int getItemViewType(int position) {
        if (position%2==0) {

            return SHALLOW_TYPE;

        } else {

            return DARK_TYPE;

        }
    }

    static class ViewHolder {
        @BindView(R.id.tv_device_name)
        TextView tvDeviceName;
        @BindView(R.id.tv_stock)
        TextView tvStock;
        @BindView(R.id.tv_half_device)
        TextView tvHalfDevice;
        @BindView(R.id.tv_wait_install)
        TextView tvWaitInstall;
        @BindView(R.id.tv_install)
        TextView tvInstall;
        @BindView(R.id.tv_device_return)
        TextView tvDeviceReturn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
