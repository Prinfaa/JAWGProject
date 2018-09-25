package com.jinganweigu.entrysystem.activities;


import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.common.BaseActivity;
import com.jinganweigu.entrysystem.utils.MyApplication;

public class RegisterActivity extends BaseActivity {

    @Override
    public void initViews() {
        setContentView(R.layout.activity_register);

//        colorSetting.setStatusBar(this,true,false);

        MyApplication.getInstance().addActivity(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvents() {

    }







}
