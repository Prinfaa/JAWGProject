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
import com.jinganweigu.RoadWayFire.Entries.BaseModel;
import com.jinganweigu.RoadWayFire.Interfaces.Url;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.BaseUtils;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.ToastUtil;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.HttpTool;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.SMObjectCallBack;
import com.lidroid.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetPasswordActivity extends BaseActivity {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.et_enter_phone_code_password)
    EditText etEnterPhoneCodePassword;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.btn_submit_code)
    Button btnSubmitCode;
    private String phoneNum;

    @Override
    public void initViews() {

        setContentView(R.layout.activity_set_password);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        phoneNum = getIntent().getStringExtra("phoneNum");


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

                String newPassword = etEnterPhoneCodePassword.getText().toString().trim();
                String surePassword = etNewPassword.getText().toString().trim();

                if (TextUtils.isEmpty(newPassword)) {

                    ToastUtil.showToast("密码不能为空", SetPasswordActivity.this);

                } else if (TextUtils.isEmpty(surePassword)) {

                    ToastUtil.showToast("确认密码不能为空", SetPasswordActivity.this);

                } else if (!surePassword.equals(newPassword)) {

                    ToastUtil.showToast("密码不一致", SetPasswordActivity.this);

                }else if(!BaseUtils.validatePhonePass(newPassword)){

                    ToastUtil.showToast("密码格式错误",SetPasswordActivity.this);

                } else if (TextUtils.isEmpty(phoneNum)) {

                    ToastUtil.showToast("账号不能为空", SetPasswordActivity.this);
                } else {

                    changePassword(phoneNum, newPassword);

                }

                break;
        }
    }


    private void changePassword(String username, String newPassword) {

        RequestParams params = new RequestParams();

        params.addBodyParameter("user_name", username);
        params.addBodyParameter("user_password", newPassword);

        HttpTool.getInstance(this).httpWithParams(Url.SET_PASSWORD_SUBMENT, params, new SMObjectCallBack<BaseModel>() {

            @Override
            public void onSuccess(BaseModel baseModel) {

                if (baseModel.getCode() == 200) {

                    ToastUtil.showToast("修改成功", SetPasswordActivity.this);

                    finish();

                } else {

                    ToastUtil.showToast(baseModel.getMsg(), SetPasswordActivity.this);

                    finish();
                }
            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }


}
