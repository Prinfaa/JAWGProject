package com.jinganweigu.RoadWayFire.Activities;


import android.content.Intent;
import android.graphics.Color;
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
import com.jinganweigu.RoadWayFire.Interfaces.Mycontants;
import com.jinganweigu.RoadWayFire.Interfaces.Url;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.BaseUtils;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.Sptools;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.ToastUtil;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.HttpTool;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.SMObjectCallBack;
import com.lidroid.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseActivity {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.et_old_password)
    EditText etOldPassword;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.et_sure_new_password)
    EditText etSureNewPassword;
    @BindView(R.id.id_forget_old_password)
    Button idForgetOldPassword;

    @Override
    public void initViews() {

        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        topName.setText("修改密码");
        tvSave.setVisibility(View.VISIBLE);
        tvSave.setTextColor(Color.parseColor("#009688"));
        tvUserName.setText(Sptools.getString(this, Mycontants.USER_NAME, ""));

    }

    @Override
    public void initData() {


    }

    @Override
    public void initEvents() {


    }


    @OnClick({R.id.back_btn, R.id.tv_save, R.id.id_forget_old_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:

                finish();

                break;
            case R.id.tv_save:
                String olderPassword = etOldPassword.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                String surePassword = etSureNewPassword.getText().toString().trim();
                String token = Sptools.getString(this, Mycontants.API_TOKEN, "");
                String company_id = Sptools.getString(this, Mycontants.COMPANY_ID, "");
                String user_id = Sptools.getString(this, Mycontants.USER_ID, "");

                if (TextUtils.isEmpty(olderPassword)) {

                    ToastUtil.showToast("旧密码不能为空", ChangePasswordActivity.this);

                } else if (TextUtils.isEmpty(newPassword)) {

                    ToastUtil.showToast("新密码不能为空", ChangePasswordActivity.this);
                } else if (TextUtils.isEmpty(token)) {

                    ToastUtil.showToast("TOKEN错误", ChangePasswordActivity.this);
                } else if (TextUtils.isEmpty(company_id)) {

                    ToastUtil.showToast("COMPANYID错误", ChangePasswordActivity.this);
                } else if (TextUtils.isEmpty(user_id)) {

                    ToastUtil.showToast("USERID错误", ChangePasswordActivity.this);

                } else if (TextUtils.isEmpty(surePassword)) {

                    ToastUtil.showToast("确认密码不能为空", ChangePasswordActivity.this);

                } else if (!surePassword.equals(newPassword)) {

                    ToastUtil.showToast("密码不一致", ChangePasswordActivity.this);

                }else if(!BaseUtils.validatePhonePass(newPassword)){

                    ToastUtil.showToast("密码格式错误",ChangePasswordActivity.this);

                } else {

                    updatePassword(token, company_id, user_id, olderPassword, surePassword);

                }


                break;
            case R.id.id_forget_old_password:

                Intent intent = new Intent();
                intent.setAction(MainActivity.REFURSH_MESSAGE_COUNT);
                intent.putExtra("refrush", "finished");
                sendBroadcast(intent);

                intentMethod.startMethodOne(this, LoginActivity.class);

                break;
        }
    }


    private void updatePassword(String token, String company_id, String user_id, String old_password, String user_password) {

        RequestParams params = new RequestParams();

        params.addBodyParameter("api_token", token);
        params.addBodyParameter("company_id", company_id);
        params.addBodyParameter("user_id", user_id);
        params.addBodyParameter("old_password", old_password);
        params.addBodyParameter("user_password", user_password);

        HttpTool.getInstance(this).httpWithParams(Url.UPDATE_PASSWORD, params, new SMObjectCallBack<BaseModel>() {

            @Override
            public void onSuccess(BaseModel baseModel) {

                if (baseModel.getCode() == 200) {

                    ToastUtil.showToast("修改成功", ChangePasswordActivity.this);

                    finish();
                } else {

                    ToastUtil.showToast(baseModel.getMsg(), ChangePasswordActivity.this);

                }

            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }
}
