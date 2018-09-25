package com.jinganweigu.RoadWayFire.Activities;


import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseActivity;
import com.jinganweigu.RoadWayFire.BeseClassUtils.MyApplication;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.widget.RefreshListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IPCManagerActivity extends BaseActivity {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.lv_ipc)
    RefreshListView lvIpc;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_ipc);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        topName.setText("摄像头权限管理");
    }

    @Override
    public void initData() {

    }
    
    @Override
    public void initEvents() {

    }

    @OnClick(R.id.back_btn)
    public void onViewClicked() {

        finish();

    }
}
