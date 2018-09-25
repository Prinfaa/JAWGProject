package com.jinganweigu.entrysystem.activities;


import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.common.BaseActivity;
import com.jinganweigu.entrysystem.utils.MyApplication;

public class ForgetPasswordActivity extends BaseActivity {


    @Override
    public void initViews() {
        setContentView(R.layout.activity_forget_password);
        MyApplication.getInstance().addActivity(this);

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvents() {

    }
}
