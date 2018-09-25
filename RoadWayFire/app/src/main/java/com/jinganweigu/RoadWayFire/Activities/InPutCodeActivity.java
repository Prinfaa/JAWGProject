package com.jinganweigu.RoadWayFire.Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseActivity;
import com.jinganweigu.RoadWayFire.BeseClassUtils.MyApplication;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InPutCodeActivity extends BaseActivity {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.et_enter_phone_code_password)
    EditText etEnterPhoneCodePassword;
    @BindView(R.id.btn_submit_code)
    Button btnSubmitCode;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private String phone, code;

    @Override
    public void initViews() {

        setContentView(R.layout.activity_input_code);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        code = getIntent().getStringExtra("code");
        phone = getIntent().getStringExtra("phone");
        tvTitle.setText("验证短信已经发送到" + phone);

    }

    @Override
    public void initData() {


    }

    @Override
    public void initEvents() {

    }

    @OnClick({R.id.back_btn, R.id.btn_submit_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:

                finish();

                break;
            case R.id.btn_submit_code:

                String eCode = etEnterPhoneCodePassword.getText().toString().trim();

                if (TextUtils.isEmpty(eCode)) {

                    ToastUtil.showToast("验证码不能为空", InPutCodeActivity.this);

                } else if (eCode.equals(code)) {

                    intentMethod.startMethodWithString(this, SetPasswordActivity.class, "phoneNum", phone);

                    finish();
                } else {

                    ToastUtil.showToast("验证码错误", InPutCodeActivity.this);
                }

                break;
        }
    }


}
