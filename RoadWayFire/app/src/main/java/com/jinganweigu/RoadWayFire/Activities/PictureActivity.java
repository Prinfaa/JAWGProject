package com.jinganweigu.RoadWayFire.Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.Adapters.PictureAdapter;
import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseActivity;
import com.jinganweigu.RoadWayFire.BeseClassUtils.MyApplication;
import com.jinganweigu.RoadWayFire.Entries.OneMonthAllPicture;
import com.jinganweigu.RoadWayFire.Interfaces.Mycontants;
import com.jinganweigu.RoadWayFire.Interfaces.Url;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.Sptools;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.ToastUtil;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.HttpTool;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.SMObjectCallBack;
import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PictureActivity extends BaseActivity {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.gv_picture)
    GridView gvPicture;
    private List<OneMonthAllPicture.ResultBean> pictureList=new ArrayList<>();
    private PictureAdapter adapter;
    private String time,cameraNo,token,company_id;



    @Override
    public void initViews() {

        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);

        time=getIntent().getStringExtra("time");
        cameraNo=getIntent().getStringExtra("cameraNo");
        token= Sptools.getString(this, Mycontants.API_TOKEN,"");
        company_id=Sptools.getString(this,Mycontants.COMPANY_ID,"");

        if(!TextUtils.isEmpty(time)){

            topName.setText(time);

        }

    }

    @Override
    public void initData() {

        if(TextUtils.isEmpty(time)){

            ToastUtil.showToast("没有获取到时间",this);

        }else if(TextUtils.isEmpty(cameraNo)){
            ToastUtil.showToast("没有获取到序列号",this);

        }else if(TextUtils.isEmpty(token)){

            ToastUtil.showToast("TOKEN错误",this);
        }else if(TextUtils.isEmpty(company_id)){

            ToastUtil.showToast("COMPANYID错误",this);
        }else{

            getAllPicture(token,time,company_id,cameraNo);

        }

    }

    @Override
    public void initEvents() {

    }


    @OnClick(R.id.back_btn)
    public void onViewClicked() {

        finish();

    }

    //获取图片接口

    private void getAllPicture(String token,String times,String company_id, String camera_id){

        RequestParams params=new RequestParams();
        params.addBodyParameter("api_token",token);
        params.addBodyParameter("times",times);
        params.addBodyParameter("company_id",company_id);
        params.addBodyParameter("camera_id",camera_id);

        HttpTool.getInstance(this).httpWithParams(Url.GET_PIRCTURE_FROM_MONTH, params, new SMObjectCallBack<OneMonthAllPicture>() {

            @Override
            public void onSuccess(OneMonthAllPicture oneMonthAllPicture) {

                if(oneMonthAllPicture.getCode()==200){

                    pictureList=oneMonthAllPicture.getResult();
                    adapter=new PictureAdapter(pictureList,PictureActivity.this);
                    gvPicture.setAdapter(adapter);


                }else{

                    ToastUtil.showToast(oneMonthAllPicture.getMsg(),PictureActivity.this);

                }

            }

            @Override
            public void onError(int error, String msg) {

            }
        });






    }



}
