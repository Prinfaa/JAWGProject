package com.jinganweigu.RoadWayFire.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.jinganweigu.RoadWayFire.Adapters.EZCameraListAdapter;
import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseActivity;
import com.jinganweigu.RoadWayFire.BeseClassUtils.MyApplication;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.EZUIkitUtils.EZUtils;
import com.jinganweigu.RoadWayFire.ToolsUtils.EZUIkitUtils.SelectCameraDialog;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.ToastUtil;
import com.videogo.constant.IntentConsts;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.EZDeviceInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jinganweigu.RoadWayFire.Fragments.VideoFragment.REQUEST_CODE;

public class EZVideoGroupActivity extends BaseActivity {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.fl_title)
    FrameLayout flTitle;
    @BindView(R.id.gv_group_video)
    PullToRefreshGridView gvGroupVideo;
    private EZDeviceInfo mEZDeviceInfo;
    private MyAdatpter adatpter;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_ezvideo_group);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        mEZDeviceInfo = getIntent().getParcelableExtra("group");
        topName.setText("分组视频");

    }

    @Override
    public void initData() {
        adatpter=new MyAdatpter();
        gvGroupVideo.setAdapter(adatpter);

    }

    @Override
    public void initEvents() {

    }





    @OnClick(R.id.back_btn)
    public void onViewClicked() {


        finish();


    }



    class MyAdatpter extends BaseAdapter {
        @Override
        public int getCount() {
            if (mEZDeviceInfo == null || mEZDeviceInfo.getCameraInfoList() == null || mEZDeviceInfo.getCameraInfoList().size() <= 0){
                return 0;
            }
            return mEZDeviceInfo.getCameraInfoList().size();
        }

        @Override
        public Object getItem(int position) {
            return mEZDeviceInfo.getCameraInfoList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           ViewHolder holder = null;
            if(convertView == null) {
                convertView = LayoutInflater.from(EZVideoGroupActivity.this).inflate(R.layout.cameralist_small_item, null);
                holder = new ViewHolder();
                holder.iconIv = (ImageView) convertView.findViewById(R.id.item_icon);
                holder.playBtn = (ImageView) convertView.findViewById(R.id.item_play_btn);

                holder.offlineBtn = (ImageView) convertView.findViewById(R.id.item_offline);
                holder.cameraNameTv = (TextView) convertView.findViewById(R.id.camera_name_tv);
                holder.offlineBgBtn = (ImageView) convertView.findViewById(R.id.offline_bg);
                holder.itemIconArea = convertView.findViewById(R.id.item_icon_area);
                holder.devicePicBtn = (ImageButton) convertView.findViewById(R.id.tab_devicepicture_btn);
                holder.deviceVideoBtn = (ImageButton) convertView.findViewById(R.id.tab_devicevideo_btn);
                holder.deviceDefenceRl = convertView.findViewById(R.id.tab_devicedefence_rl);
                holder.deviceDefenceBtn = (ImageButton) convertView.findViewById(R.id.tab_devicedefence_btn);



//                holder.mCameraNoTV = (TextView) convertView.findViewById(R.id.text_camerano);
//                holder.mCameraNameTV = (TextView) convertView.findViewById(R.id.text_camera_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            EZCameraInfo ezCameraInfo = mEZDeviceInfo.getCameraInfoList().get(position);
            if (ezCameraInfo != null){

//                String imageUrl = ezCameraInfo.getCameraCover();
//                if(!TextUtils.isEmpty(imageUrl)) {
//                    Glide.with(mContext).load(imageUrl).placeholder(R.mipmap.device_other).into(holder.iconIv);
//                }

                holder.iconIv.setImageResource(R.mipmap.ipc);
                holder.cameraNameTv.setText(TextUtils.isEmpty(ezCameraInfo.getCameraName())?"未命名":ezCameraInfo.getCameraName());
//                holder.mCameraNoTV.setText(String.valueOf(ezCameraInfo.getCameraNo()));
//                holder.mCameraNameTV.setText(TextUtils.isEmpty(ezCameraInfo.getCameraName())?"未命名":ezCameraInfo.getCameraName());
            }

            holder.playBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(ezCameraInfo != null){

//                        Intent intent = new Intent(EZVideoGroupActivity.this, EZRealPlayActivity.class);
//                        intent.putExtra(IntentConsts.EXTRA_CAMERA_INFO, ezCameraInfo);
//                        intent.putExtra(IntentConsts.EXTRA_DEVICE_INFO, mEZDeviceInfo);
//                        startActivityForResult(intent, REQUEST_CODE);


                        Intent intent = new Intent(EZVideoGroupActivity.this, MapVideoActivity.class);

                        intent.putExtra("deviceSerial",ezCameraInfo.getDeviceSerial());
                        intent.putExtra("deviceName",ezCameraInfo.getCameraName());
                        intent.putExtra("cameraNo", ezCameraInfo.getCameraNo());

                        startActivity(intent);

                    }

                }
            });

            return convertView;
        }
    }

    class ViewHolder{
        public ImageView iconIv;

        public ImageView playBtn;

        public ImageView offlineBtn;

        public TextView cameraNameTv;





        public View itemIconArea;

        public ImageView offlineBgBtn;


        public ImageButton devicePicBtn;

        public ImageButton deviceVideoBtn;

        public View deviceDefenceRl;
        public ImageButton deviceDefenceBtn;
    }





}
