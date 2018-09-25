package com.jinganweigu.RoadWayFire.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.Adapters.ContactsManagerAdapter;
import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseActivity;
import com.jinganweigu.RoadWayFire.BeseClassUtils.MyApplication;
import com.jinganweigu.RoadWayFire.Entries.BaseModel;
import com.jinganweigu.RoadWayFire.Interfaces.Mycontants;
import com.jinganweigu.RoadWayFire.Interfaces.Url;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.Sptools;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.ToastUtil;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.HttpTool;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.SMObjectCallBack;
import com.jinganweigu.RoadWayFire.ToolsUtils.ToolUntils.NameUtils;
import com.lidroid.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactPersonInformationActivity extends BaseActivity{


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.tv_first_name)
    TextView tvFirstName;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_deal_name)
    TextView tvDealName;
    @BindView(R.id.tv_deal_phone_num)
    TextView tvDealPhoneNum;
    @BindView(R.id.btn_call)
    Button btnCall;
    String name,phoneNum;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_contact_person_information);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        name=getIntent().getStringExtra("name");
        phoneNum=getIntent().getStringExtra("phone");



    }

    @Override
    public void initData() {

        if(TextUtils.isEmpty(name)){

            ToastUtil.showToast("姓名为空", this);
        }else if(TextUtils.isEmpty(phoneNum)){

            ToastUtil.showToast("电话号码为空", this);
        }else{

            topName.setText(name);
            tvFirstName.setText(NameUtils.getName(name));
           tvName.setText(name);
           tvDealPhoneNum.setText(phoneNum);
           tvDealName.setText(name);

        }



    }

    @Override
    public void initEvents() {

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phoneNum);
                intent.setData(data);
                startActivity(intent);

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


    }





}
