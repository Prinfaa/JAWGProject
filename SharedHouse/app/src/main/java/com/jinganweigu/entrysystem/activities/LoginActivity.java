package com.jinganweigu.entrysystem.activities;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.githang.statusbar.StatusBarCompat;
import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.common.BaseActivity;
import com.jinganweigu.entrysystem.entry.LoginBean;
import com.jinganweigu.entrysystem.utils.BaseUtils;
import com.jinganweigu.entrysystem.utils.HttpCode;
import com.jinganweigu.entrysystem.utils.MobileInfoUtil;
import com.jinganweigu.entrysystem.utils.MyApplication;
import com.jinganweigu.entrysystem.utils.Mycontants;
import com.jinganweigu.entrysystem.utils.PermissionsChecker;
import com.jinganweigu.entrysystem.utils.Sptools;
import com.jinganweigu.entrysystem.utils.ToastUtil;
import com.jinganweigu.entrysystem.utils.Url;
import com.jinganweigu.entrysystem.utils.http.HttpTool;
import com.jinganweigu.entrysystem.utils.http.SMObjectCallBack;
import com.jinganweigu.entrysystem.view.CommonProgressDialog;
import com.lidroid.xutils.http.RequestParams;

import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {


    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.ed_phone_num)
    EditText edPhoneNum;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_forget_pwd)
    Button btnForgetPwd;
    @BindView(R.id.ed_password)
    EditText edPassword;
    private CommonProgressDialog dialog;
    private String phoneNum, Password, imei;

    private static final int REQUEST_CODE = 0; // 请求码

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.CAMERA,
            Manifest.permission.VIBRATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
//            Manifest.permission.REQUEST_INSTALL_PACKAGES



    };



    private PermissionsChecker mPermissionsChecker; // 权限检测器





    @Override
    public void initViews() {

        setContentView(R.layout.activity_login);
        mPermissionsChecker = new PermissionsChecker(this);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white), true);


        ButterKnife.bind(this);
//        MyApplication.getInstance().addActivity(this);
        dialog = new CommonProgressDialog(this, "正在登录..");
        imei = MobileInfoUtil.getIMEI(this);

//        StatusColorSetting.getInstance().setStatusBar(this,false,true);
//        boolean a=StatusColorSetting.setMeizuStatusBarDarkIcon(this,true);

        try {
            Object win = getWindow();
            Class<?> cls = win.getClass();
            Method method = cls.getDeclaredMethod("setStatusBarIconDark", boolean.class);
            method.invoke(win, true);
        } catch (Exception e) {
            Log.v("ff", "statusBarIconDark,e=" + e);
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(getResources().getColor(android.R.color.white));
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }


//        Log.e("statuscolor", "initViews: ===>"+a );

        int code = getIntent().getIntExtra("back", 0);


        if (code == 1) {

            edPassword.setText("");
            edPhoneNum.setText("");

        } else {
            String name = Sptools.getString(this, Mycontants.LOGIN_NAME, "");
            String password = Sptools.getString(this, Mycontants.LOGIN_PASSWORD, "");


            if (TextUtils.isEmpty(name)) {

                return;
            } else if (TextUtils.isEmpty(password)) {
                return;

            } else {

                edPhoneNum.setText(name);
                edPassword.setText(password);

                login(name, password, imei);

            }
        }
    }

    @Override
    public void initData() {


//                LoadImageUtil.ShowGlideRoundImage(LoginActivity.this,"https://www.baidu.com/img/bdlogo.png",ivImage);


//        HttpTool.getInstance(this).http(Url.URL_ALIPAY_APPPAY, new SMObjectCallBack<EnterStaisticsBean>() {
//
//
//
//            @Override
//            public void onSuccess(EnterStaisticsBean enterStaistics) {
//
//
//
//            }
//
//            @Override
//            public void onError(int error, String msg) {
//
//            }
//        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }

    }

    @Override
    public void initEvents() {


    }


    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }



    @OnClick(R.id.btn_login)
    public void onViewClicked() {

        phoneNum = edPhoneNum.getText().toString().trim();
        Password = edPassword.getText().toString().trim();


        if (TextUtils.isEmpty(phoneNum)) {
            ToastUtil.showToast("请输入正确的手机号", mContext);
        } else if (TextUtils.isEmpty(imei)) {
            ToastUtil.showToast("无法获取IMEI", mContext);
        } else if (TextUtils.isEmpty(Password)) {
            ToastUtil.showToast("请输入正确的密码", mContext);
        } else {
            login(phoneNum, Password, imei);
        }


    }


    private void login(final String phoneNum, final String Password, String imei) {


        dialog.show();//显示进度条

        RequestParams params = new RequestParams();
        params.addBodyParameter("user_name", phoneNum);
        params.addBodyParameter("user_password", Password);
        params.addBodyParameter("phone_imei", imei);

        HttpTool.getInstance(this).httpWithParams(Url.URL_LOGIN, params, new SMObjectCallBack<LoginBean>() {
            @Override
            public void onSuccess(LoginBean data) {


                if (data.getCode() == HttpCode.REQUEST_SUCCESS) {//登陆成功

                    ToastUtil.showToast(data.getMsg(), LoginActivity.this);

                    Sptools.putString(LoginActivity.this, Mycontants.LOGIN_NAME, phoneNum);
                    Sptools.putString(LoginActivity.this, Mycontants.LOGIN_PASSWORD, Password);
                    Sptools.putString(LoginActivity.this, Mycontants.WAREHOUSE_NAME, data.getResult().getWarehouse_name());
                    Sptools.putString(LoginActivity.this, Mycontants.COMPANY_NAME, data.getResult().getCompany_name());
                    Sptools.putString(LoginActivity.this, Mycontants.REAL_NAME, data.getResult().getReal_name());
                    Sptools.putString(LoginActivity.this, Mycontants.MAX_ROW, data.getResult().getMax_row());

                    Sptools.putString(LoginActivity.this, Mycontants.COLOR_IN, data.getResult().getColour_in());
                    Sptools.putString(LoginActivity.this, Mycontants.COLOR_OUT, data.getResult().getColour_out());
                    Sptools.putString(LoginActivity.this, Mycontants.COLOR_RETURN, data.getResult().getColour_return());
                    intentMethod.startMethodOne(LoginActivity.this, MainActivity.class);

                    dialog.dismiss();
                } else if (data.getCode() == HttpCode.BIND_PHONE_SUCCESS) {//绑定手机成功

                    dialog.dismiss();
                    ToastUtil.showToast(data.getMsg(), LoginActivity.this);

                } else if (data.getCode() == HttpCode.USER_NAME_INCORRECT) {//账号与绑定手机不匹配

                    dialog.dismiss();
                    ToastUtil.showToast(data.getMsg(), LoginActivity.this);
                } else if (data.getCode() == HttpCode.USERNAME_OR_PASSWORD_ERROR) {//账号或密码错误

                    dialog.dismiss();
                    ToastUtil.showToast(data.getMsg(), LoginActivity.this);
                } else if (data.getCode() == HttpCode.IMEI_ERROR) {//记录IMEI失败

                    dialog.dismiss();
                    ToastUtil.showToast(data.getMsg(), LoginActivity.this);

                }

//                if (data.getResultCode() == 0) {
//
//                    String userId = data.getData().getUserId() + "";
//                    String userName = data.getData().getNickname();
//                    String icon = data.getData().getHeadImg();
//                    Sptools.putBoolean(mContext, Mycontants.Is_other_Login, false);//是不是第三方登录
//                    Sptools.putBoolean(mContext, Mycontants.IF_LOGIN, true);
//                    Sptools.putString(mContext, Mycontants.USER_ID, userId);
//                    Sptools.putString(mContext, Mycontants.USER_NAME, userName);
//                    Sptools.putString(mContext, Mycontants.USER_HEAD_URL, icon);
//                    Sptools.putString(mContext, Mycontants.LOGIN_NAME, phoneNum);
//                    Sptools.putString(mContext, Mycontants.LOGIN_PASSWORD, password);
//                    hasPayPassword(Sptools.getString(mContext, Mycontants.LOGIN_NAME, null));//是否设置了支付密码
//                    //带数据返回个人中心
//                    if (mMineFragmentListener != null) {
//                        mMineFragmentListener.OnMineFragment();
//                    }
//                    if (mLonginForSettingListener != null) {
//                        mLonginForSettingListener.OnLonginForSetting();
//                    }
//
//
//                    intentMethod.rebackMethod(LoginActivity.this);
//
//                }
//
//                ToastUtil.showToast(data.getMsg(), mContext);

            }

            @Override
            public void onError(int error, String msg) {

                dialog.dismiss();
//                ToastUtil.showToast(msg, mContext);
            }
        });


    }


}
