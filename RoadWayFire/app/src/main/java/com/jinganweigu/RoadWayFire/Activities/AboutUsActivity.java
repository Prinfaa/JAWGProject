package com.jinganweigu.RoadWayFire.Activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseActivity;
import com.jinganweigu.RoadWayFire.BeseClassUtils.MyApplication;
import com.jinganweigu.RoadWayFire.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUsActivity extends BaseActivity {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.tv_company_logo)
    ImageView tvCompanyLogo;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_custom_service)
    TextView tvCustomService;
    @BindView(R.id.tv_technology_service)
    TextView tvTechnologyService;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;

    @Override
    public void initViews() {

        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        topName.setText("关于我们 ");




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
