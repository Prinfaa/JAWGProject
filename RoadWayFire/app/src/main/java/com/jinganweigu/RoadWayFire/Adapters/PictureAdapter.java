package com.jinganweigu.RoadWayFire.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.Activities.AlarmInformationDetailActivity;
import com.jinganweigu.RoadWayFire.Activities.ShowPictureActivity;
import com.jinganweigu.RoadWayFire.Entries.OneMonthAllPicture;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.BaseUtils;
import com.jinganweigu.RoadWayFire.ToolsUtils.ImageUtil.LoadImageUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PictureAdapter extends BaseAdapter {

    private List<OneMonthAllPicture.ResultBean> pictureList;
    private Context context;
    private ViewHolder mViewHolder;

    public PictureAdapter(List<OneMonthAllPicture.ResultBean> pictureList, Context context) {
        this.pictureList = pictureList;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = View.inflate(context, R.layout.item_picture_of_month, null);

            mViewHolder=new ViewHolder(convertView);

            convertView.setTag(mViewHolder);

        }else{

            mViewHolder= (ViewHolder) convertView.getTag();

        }
        LoadImageUtil.ShowImage(context,pictureList.get(position).getPhoto_adress(),mViewHolder.ivPicture);
        mViewHolder.tvName.setText(BaseUtils.getDateToString(Long.valueOf(pictureList.get(position).getTimestamp())));

        mViewHolder.ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadImageUtil.IntentMethod.getInstance().startMethodWithString(context, ShowPictureActivity.class, "warningnum", pictureList.get(position).getWarning_num());
            }
        });
        return convertView;
    }

    @Override
    public int getCount() {
        return pictureList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        @BindView(R.id.iv_picture)
        ImageView ivPicture;
        @BindView(R.id.tv_name)
        TextView tvName;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
