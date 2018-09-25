package com.jinganweigu.RoadWayFire.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseActivity;
import com.jinganweigu.RoadWayFire.BeseClassUtils.MyApplication;
import com.jinganweigu.RoadWayFire.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManagerActivity extends BaseActivity {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.ll_username_manager)
    LinearLayout llUsernameManager;
    @BindView(R.id.ll_class_manager)
    LinearLayout llClassManager;
    @BindView(R.id.ll_ipc_manager)
    LinearLayout llIpcManager;

    @Override
    public void initViews() {

        setContentView(R.layout.activity_manager);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        topName.setText("人员管理");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvents() {

    }


    @OnClick({R.id.back_btn, R.id.ll_username_manager, R.id.ll_class_manager, R.id.ll_ipc_manager})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:

                finish();

                break;
            case R.id.ll_username_manager:

                intentMethod.startMethodTwo(this, UserNameManagerActivity.class);

                break;
            case R.id.ll_class_manager:

                intentMethod.startMethodTwo(this, ClassesManageActivity.class);

                break;
            case R.id.ll_ipc_manager:

                intentMethod.startMethodTwo(this, IPCManagerActivity.class);

                break;
        }
    }

}
