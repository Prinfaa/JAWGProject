package com.jinganweigu.entrysystem.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.entry.WasteDeviceBean;
import com.jinganweigu.entrysystem.utils.BaseUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WasteStatisticsDownAdapter extends BaseAdapter {
    private Context context;
    private List<WasteDeviceBean.ResultBean.ParticularBean> dList;
    private ViewHolder holder;


    public WasteStatisticsDownAdapter(Context context, List<WasteDeviceBean.ResultBean.ParticularBean> dList) {
        this.context = context;
        this.dList = dList;
    }


    @Override
    public int getCount() {
        return dList.size();
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

            convertView = View.inflate(context, R.layout.item_waste_statistic_device_information, null);
            holder = new ViewHolder(convertView);
        } else {

            holder = (ViewHolder) convertView.getTag();

        }

        holder.tvDeviceId.setText(dList.get(position).getChip_IMEI());

        holder.tvReturnProject.setText(dList.get(position).getReceive_unit() + "");

        holder.tvTime.setText(dList.get(position).getWaste_time());
        holder.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dList.get(position).getReturn_reason()!=null){

                    BaseUtils.showAlertDialog2(context, "退货原因", dList.get(position).getReturn_reason().toString(), new BaseUtils.OnConfirm() {
                        @Override
                        public void onConfirm() {

                        }
                    });



                }else{


                    BaseUtils.showAlertDialog2(context, "退货原因", "退货人未填写", new BaseUtils.OnConfirm() {
                        @Override
                        public void onConfirm() {

                        }
                    });



                }






            }
        });


        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_device_id)
        TextView tvDeviceId;
        @BindView(R.id.tv_return_project)
        TextView tvReturnProject;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.ll_root)
        LinearLayout llRoot;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
