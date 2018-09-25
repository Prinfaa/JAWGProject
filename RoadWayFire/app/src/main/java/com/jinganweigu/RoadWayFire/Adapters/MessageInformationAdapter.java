package com.jinganweigu.RoadWayFire.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.Entries.ListMessage;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.ImageUtil.LoadImageUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageInformationAdapter extends BaseAdapter {

    private Context context;
    private List<ListMessage.ResultBean> list;
    private ViewHolder mViewHolder;
    private int status=0;// 0 未处警  1 已处警

    public MessageInformationAdapter(Context context, List<ListMessage.ResultBean> list,int status) {
        this.context = context;
        this.list = list;
        this.status=status;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = View.inflate(context, R.layout.item_message_contents, null);

            mViewHolder = new ViewHolder(convertView);

            convertView.setTag(mViewHolder);

        } else {

            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.tvAddress.setText("报警位置：" + list.get(position).getPosition());
        mViewHolder.tvDevice.setText("报警设备：" + list.get(position).getCamera_name());
//        mViewHolder.tvReceiverPerson.setText("接收人：" + list.get(position).getName());
        mViewHolder.tvWarnTime.setText("报警时间：" + list.get(position).getStart_time());


        String type = list.get(position).getType();

        if (type.equals("发出消息")) {
            mViewHolder.tvTitle.setText(list.get(position).getType());
            mViewHolder.tvTitle.setTextColor(Color.parseColor("#259b24"));
            mViewHolder.tvReceiverPerson.setText("接收人：" + list.get(position).getName());
            if(!TextUtils.isEmpty(list.get(position).getComment())) {
                mViewHolder.tvCommend.setText("留言："+list.get(position).getComment());
            }else{
                mViewHolder.tvCommend.setText("留言：无");
            }

        } else if (type.equals("报警消息")) {
            mViewHolder.tvTitle.setText(list.get(position).getType());
            mViewHolder.tvTitle.setTextColor(Color.parseColor("#5a95fd"));
            mViewHolder.tvReceiverPerson.setVisibility(View.INVISIBLE);
            mViewHolder.tvCommend.setVisibility(View.GONE);
        } else if (type.equals("接收消息")) {
            mViewHolder.tvTitle.setText(list.get(position).getType());
            mViewHolder.tvTitle.setTextColor(Color.parseColor("#ff9800"));
            mViewHolder.tvReceiverPerson.setText("发送人：" + list.get(position).getName());
            if(!TextUtils.isEmpty(list.get(position).getComment())) {
                mViewHolder.tvCommend.setText("留言："+list.get(position).getComment());
            }else{
                mViewHolder.tvCommend.setText("留言：无");
            }
        }
        mViewHolder.tvTime.setText(list.get(position).getSend_timestamp());

        if(status==0){

          mViewHolder.tvDealTime.setVisibility(View.GONE);

        }else if(status==1){

            mViewHolder.tvDealTime.setText("处警时间："+list.get(position).getDeal_time());
        }

        LoadImageUtil.ShowImage(context, list.get(position).getPhoto_adress(), mViewHolder.ivCar);


        return convertView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ListMessage.ResultBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    static class ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_device)
        TextView tvDevice;
        @BindView(R.id.tv_receiver_person)
        TextView tvReceiverPerson;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.tv_warn_time)
        TextView tvWarnTime;
        @BindView(R.id.tv_deal_time)
        TextView tvDealTime;
        @BindView(R.id.tv_commend)
        TextView tvCommend;
        @BindView(R.id.iv_car)
        ImageView ivCar;
        @BindView(R.id.tv_deal)
        TextView tvDeal;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.rl_message)
        LinearLayout rlMessage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
