package com.jinganweigu.RoadWayFire.Activities;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseActivity;
import com.jinganweigu.RoadWayFire.BeseClassUtils.MyApplication;
import com.jinganweigu.RoadWayFire.Entries.Code;
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

public class ForgetPasswordActivity extends BaseActivity {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.et_enter_phone_code_password)
    EditText etEnterPhoneCodePassword;
    @BindView(R.id.btn_get_code)
    Button btnGetCode;

    @Override
    public void initViews() {

        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        topName.setText("忘记密码");

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvents() {

    }


    @OnClick({R.id.back_btn, R.id.btn_get_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:

                finish();

                break;
            case R.id.btn_get_code:

                String phone = etEnterPhoneCodePassword.getText().toString().trim();

                if (TextUtils.isEmpty(phone)) {

                    ToastUtil.showToast("手机号不能为空", ForgetPasswordActivity.this);

                }
                if (!BaseUtils.isMobiles(phone)) {

                    ToastUtil.showToast("请输入正确手机号", ForgetPasswordActivity.this);

                } else {

                    sendMessage(phone);

                }


                break;
        }
    }


    //发送短信请求

    private void sendMessage(String phoneNum) {

        RequestParams params = new RequestParams();

        params.addBodyParameter("phone", phoneNum);

        HttpTool.getInstance(this).httpWithParams(Url.FORGET_PASSSWORD_GET_CODE, params, new SMObjectCallBack<Code>() {
            @Override
            public void onSuccess(Code code) {

                if (code.getCode() == 200) {

//                    BaseUtils.startCaptureTimeCount(60000, 1000, btnGetCode);//这是点击发送验证码之后倒计时
                    intentMethod.startMethodWith2String(ForgetPasswordActivity.this, InPutCodeActivity.class, "code", "" + code.getResult().getNum(),"phone",phoneNum);

                    finish();
                }

            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }

}
