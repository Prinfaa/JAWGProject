package com.jinganweigu.RoadWayFire.Activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseActivity;
import com.jinganweigu.RoadWayFire.Entries.Login;
import com.jinganweigu.RoadWayFire.Interfaces.Mycontants;
import com.jinganweigu.RoadWayFire.Interfaces.Url;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.BaseUtils;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.Sptools;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.ToastUtil;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.HttpTool;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.SMObjectCallBack;
import com.jinganweigu.RoadWayFire.Views.CommonProgressDialog;
import com.lidroid.xutils.http.RequestParams;
import com.xw.repo.xedittext.XEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity {
    private static final int REQUEST_CODE = 0; // 请求码

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;

    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_forget_password)
    Button btnForgetPassword;
    @BindView(R.id.et_username)
    XEditText etUsername;
    @BindView(R.id.et_password)
    XEditText etPassword;
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private CommonProgressDialog dialog;


    @Override
    public void initViews() {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mPermissionsChecker = new PermissionsChecker(this);
        dialog = new CommonProgressDialog(this, "正在登录...");


    }

    @Override
    protected void onResume() {
        super.onResume();

        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }

    }

    @Override
    public void initData() {

        topName.setText("登录");
        backBtn.setVisibility(View.GONE);

        String username = Sptools.getString(this, Mycontants.USER_NAME, "");
        String password = Sptools.getString(this, Mycontants.PASS_WORD, "");

        if (!TextUtils.isEmpty(username)) {

            etUsername.setText(username);

        }
        if (!TextUtils.isEmpty(password)) {

            etPassword.setText(password);
        }


    }

    @Override
    public void initEvents() {




    }

    @OnClick({R.id.btn_login, R.id.btn_forget_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:

                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {

                    ToastUtil.showToast("用户名不能为空", this);

                } else if (TextUtils.isEmpty(password)) {

                    ToastUtil.showToast("密码不能为空", this);

                } else if (!BaseUtils.isMobiles(username)) {

                    ToastUtil.showToast("请输入正确手机号", LoginActivity.this);

                } else if (!BaseUtils.validatePhonePass(password)) {

                    ToastUtil.showToast("密码格式错误", LoginActivity.this);

                } else {

                    loginRoadWayFire(username, password);
                }

                break;
            case R.id.btn_forget_password:

                intentMethod.startMethodTwo(this, ForgetPasswordActivity.class);

                break;
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }


    private void loginRoadWayFire(String username, String Password) {

        dialog.show();

        RequestParams params = new RequestParams();

        params.addBodyParameter("user_name", username);
        params.addBodyParameter("user_password", Password);

        HttpTool.getInstance(this).httpWithParams(Url.LOGIN_IN, params, new SMObjectCallBack<Login>() {

            @Override
            public void onSuccess(Login login) {
                dialog.dismiss();

                if (login.getCode() == 200) {

                    Sptools.putString(LoginActivity.this, Mycontants.COMPANY_ID, login.getResult().getCompany_id());
                    Sptools.putString(LoginActivity.this, Mycontants.USER_ID, login.getResult().getUser_id());
                    Sptools.putString(LoginActivity.this, Mycontants.USER_NAME, username);
                    Sptools.putString(LoginActivity.this, Mycontants.PASS_WORD, Password);
                    Sptools.putString(LoginActivity.this, Mycontants.API_TOKEN, login.getResult().getApi_token());
                    Sptools.putString(LoginActivity.this, Mycontants.EZUIkit_APPKEY, login.getResult().getApp_key());
                    Sptools.putString(LoginActivity.this, Mycontants.EZUIkit_SECRET, login.getResult().getApp_secret());

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("appKey", login.getResult().getApp_key());
                    intent.putExtra("appSecret", login.getResult().getApp_secret());

                    startActivity(intent);
                    finish();
                } else {
                    ToastUtil.showToast(login.getMsg(), LoginActivity.this);
                }

            }

            @Override
            public void onError(int error, String msg) {

                dialog.dismiss();

            }
        });


    }


}
