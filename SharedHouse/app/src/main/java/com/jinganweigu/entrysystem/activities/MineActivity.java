package com.jinganweigu.entrysystem.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.common.BaseActivity;
import com.jinganweigu.entrysystem.utils.MyApplication;
import com.jinganweigu.entrysystem.utils.Mycontants;
import com.jinganweigu.entrysystem.utils.Sptools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MineActivity extends BaseActivity {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_ware_house)
    TextView tvWareHouse;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.btn_login_out)
    Button btnLoginOut;

    String name, warehouse, company;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_mine);
        ButterKnife.bind(this);
        topName.setText("个人信息");
        tvSave.setVisibility(View.GONE);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary), true);
        MyApplication.getInstance().addActivity(this);
        warehouse = Sptools.getString(this, Mycontants.WAREHOUSE_NAME, "");
        company = Sptools.getString(this, Mycontants.COMPANY_NAME, "");
        name = Sptools.getString(this, Mycontants.REAL_NAME, "");


    }

    @Override
    public void initData() {

        if (!TextUtils.isEmpty(warehouse)) {

            tvWareHouse.setText(warehouse);
        }

        if (!TextUtils.isEmpty(company)) {

            tvCompanyName.setText(company);
        }

        if (!TextUtils.isEmpty(name)) {

            tvName.setText(name);
        }


    }

    @Override
    public void initEvents() {

    }


    @OnClick({R.id.back_btn, R.id.btn_login_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:

                intentMethod.rebackMethod(this);

                break;
            case R.id.btn_login_out:

                MyApplication.getInstance().exit();

                intentMethod.startMethodWithInt(this,LoginActivity.class,"back",1);


                break;
        }
    }
}
