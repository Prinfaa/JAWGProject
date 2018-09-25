package com.jinganweigu.entrysystem.activities;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.adapter.DeviceDeticalAdapter;
import com.jinganweigu.entrysystem.common.BaseActivity;
import com.jinganweigu.entrysystem.entry.SureOutHouseBean;
import com.jinganweigu.entrysystem.utils.HttpCode;
import com.jinganweigu.entrysystem.utils.MyApplication;
import com.jinganweigu.entrysystem.utils.ToastUtil;
import com.jinganweigu.entrysystem.utils.Url;
import com.jinganweigu.entrysystem.utils.http.HttpTool;
import com.jinganweigu.entrysystem.utils.http.SMObjectCallBack;
import com.jinganweigu.entrysystem.view.CommonProgressDialog;
import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceDeticalActivity extends BaseActivity {


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
    @BindView(R.id.lv_device)
    ListView lvDevice;
    CommonProgressDialog dialog;
    DeviceDeticalAdapter adapter;
    List<SureOutHouseBean.ResultBean> list=new ArrayList<>();
    private int from;

    @Override
    public void initViews() {

        setContentView(R.layout.activity_device_detical);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        topName.setText("设备详细信息");
        tvSave.setVisibility(View.GONE);
        dialog=new CommonProgressDialog(this,"正在加载...");
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary), true);
        String num=getIntent().getStringExtra("num");
        if(!TextUtils.isEmpty(num)){

            SureManualOutHouse(num);

        }else{

            ToastUtil.showToast("数据为空",this);

        }





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

    private void SureManualOutHouse(String DeviceId){

        dialog.show();

        RequestParams params=new RequestParams();

        params.addBodyParameter("rtu_id",DeviceId);

        HttpTool.getInstance(this).httpWithParams(Url.SURE_OUT_HOUSE, params,new SMObjectCallBack<SureOutHouseBean>() {


            @Override
            public void onSuccess(SureOutHouseBean sureOutHouseBean) {

                dialog.dismiss();

                if(sureOutHouseBean.getCode()== HttpCode.REQUEST_SUCCESS){

                    list=sureOutHouseBean.getResult();

                    adapter=new DeviceDeticalAdapter(list,DeviceDeticalActivity.this);

                    lvDevice.setAdapter(adapter);



                }else {

                    dialog.dismiss();


                    ToastUtil.showToast(sureOutHouseBean.getMsg(),DeviceDeticalActivity.this);


                }


            }

            @Override
            public void onError(int error, String msg) {



                dialog.dismiss();

            }
        });


    }




}
