package com.jinganweigu.RoadWayFire.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseActivity;
import com.jinganweigu.RoadWayFire.BeseClassUtils.MyApplication;
import com.jinganweigu.RoadWayFire.Entries.AlarmTypes;
import com.jinganweigu.RoadWayFire.Entries.BaseModel;
import com.jinganweigu.RoadWayFire.Entries.ListMessage;
import com.jinganweigu.RoadWayFire.Interfaces.Mycontants;
import com.jinganweigu.RoadWayFire.Interfaces.Url;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.Sptools;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.ToastUtil;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.HttpTool;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.SMObjectCallBack;
import com.jinganweigu.RoadWayFire.ToolsUtils.ImageUtil.LoadImageUtil;
import com.jinganweigu.RoadWayFire.Views.HKImageView;
import com.jinganweigu.RoadWayFire.Views.PopupuWindow;
import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlarmInformationDetailActivity extends BaseActivity implements PopupuWindow.PopupuwindowItemClickListener {

    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.tv_alarm_device)
    TextView tvAlarmDevice;
    @BindView(R.id.tv_alarm_address)
    TextView tvAlarmAddress;
    @BindView(R.id.tv_alarm_time_long)
    TextView tvAlarmTimeLong;
    @BindView(R.id.tv_car_info)
    TextView tvCarInfo;
    @BindView(R.id.tv_alarm_time)
    TextView tvAlarmTime;
    @BindView(R.id.iv_alarm_picutre)
    HKImageView ivAlarmPicutre;
    @BindView(R.id.ll_button)
    LinearLayout llButton;
    @BindView(R.id.tv_alarm_type)
    TextView tvAlarmType;
    @BindView(R.id.et_remarks)
    EditText etRemarks;
    @BindView(R.id.ll_manager_alarm)
    LinearLayout llManagerAlarm;
    @BindView(R.id.btn_deal_alarm)
    Button btnDealAlarm;
    @BindView(R.id.btn_send_other)
    Button btnSendOther;
    @BindView(R.id.tv_remarks)
    TextView tvRemarks;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.tv_alarm_person)
    TextView tvAlarmPerson;
    private ListMessage.ResultBean mMessage;
    PopupuWindow pwindow;
    private Display display;
    private List<String> groups = new ArrayList<>();
    private String token;
    private List<AlarmTypes.ResultBean> typesList = new ArrayList<>();

    private String alarmID, from;


    @Override
    public void initViews() {

        setContentView(R.layout.activity_alarm_information_detail);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        token = Sptools.getString(this, Mycontants.API_TOKEN, "");
        topName.setText("报警信息");
        mMessage = (ListMessage.ResultBean) getIntent().getSerializableExtra("entity");
        display = getWindowManager().getDefaultDisplay();
        from = getIntent().getStringExtra("from");

    }

    @Override
    public void initData() {

        tvAlarmDevice.setText("报警设备：" + mMessage.getCamera_name());
        tvAlarmAddress.setText("报警位置：" + mMessage.getPosition());
        tvAlarmTimeLong.setText("报警时长：" + mMessage.getTotal_time());
        tvAlarmDevice.setText("报警设备：" + mMessage.getCamera_name());
//        tvAlarmTime.setText("报警时间：" + BaseUtils.getDateToString(Long.valueOf(mMessage.getStart_time())));
        tvAlarmTime.setText("报警时间：" + mMessage.getStart_time());


        LoadImageUtil.ShowImage(this, mMessage.getPhoto_adress(), ivAlarmPicutre);

        ivAlarmPicutre.setPadding(18, 18, 20, 20);

        if (from.equals("deal")) {
            llButton.setVisibility(View.GONE);
            llManagerAlarm.setVisibility(View.VISIBLE);
            tvAlarmType.setText(mMessage.getWarning_type());
            tvRemarks.setVisibility(View.VISIBLE);
            etRemarks.setVisibility(View.GONE);

            if(!TextUtils.isEmpty(mMessage.getRemark())){
                tvRemarks.setText(mMessage.getRemark());
            }else if(!TextUtils.isEmpty(mMessage.getComment())){

                tvRemarks.setText(mMessage.getComment());

            }else{

                tvRemarks.setText("无留言");

            }

            ivMore.setVisibility(View.GONE);
            tvAlarmPerson.setText("处警人："+mMessage.getDeal_peo());

        } else if (from.equals("undeal")) {
            tvRemarks.setVisibility(View.GONE);
            etRemarks.setVisibility(View.VISIBLE);
            llButton.setVisibility(View.VISIBLE);
            llManagerAlarm.setVisibility(View.GONE);
            tvAlarmPerson.setVisibility(View.GONE);
        }


    }

    @Override
    public void initEvents() {

        ivAlarmPicutre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentMethod.startMethodWithString(AlarmInformationDetailActivity.this, ShowPictureActivity.class, "warningnum", mMessage.getWarning_num());

            }
        });

        //选择处警方式
        tvAlarmType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (groups.size() != 0) {

                    pwindow = new PopupuWindow(AlarmInformationDetailActivity.this, groups, display.getWidth() * 7 / 10, ViewGroup.LayoutParams.WRAP_CONTENT);

                    pwindow.setOnPopupuwindowItemClickListener(AlarmInformationDetailActivity.this);

                    pwindow.showWindow(v);

                } else {

//                    ToastUtil.showToast("暂无数据", AlarmInformationDetailActivity.this);

                }

            }
        });


        tvAlarmDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AlarmInformationDetailActivity.this, MapVideoActivity.class);
                intent.putExtra("deviceSerial", mMessage.getDev_number());
                intent.putExtra("cameraNo", 1);
                intent.putExtra("deviceName", mMessage.getCamera_name());
                startActivity(intent);

            }
        });


    }


    @OnClick({R.id.back_btn, R.id.tv_save, R.id.btn_deal_alarm, R.id.btn_send_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:

                finish();
                break;
            case R.id.tv_save:

                String remarks = etRemarks.getText().toString().trim();
                String warningNum = mMessage.getWarning_num();
                String alarmtypeID = tvAlarmType.getText().toString().trim();
                String deal_peo = Sptools.getString(this, Mycontants.USER_ID, "");

                if (TextUtils.isEmpty(alarmtypeID)) {

                    ToastUtil.showToast("请选择处警类型", AlarmInformationDetailActivity.this);
                } else {
                    upLoadDetailAlarmData(alarmID, token, warningNum, remarks, deal_peo);
                }
                break;

            case R.id.btn_deal_alarm:

                tvSave.setVisibility(View.VISIBLE);
                tvSave.setText("处警");
                llButton.setVisibility(View.GONE);
                llManagerAlarm.setVisibility(View.VISIBLE);
                getAlarmType(token);

                break;
            case R.id.btn_send_other:

                Intent intent = new Intent(this, NameListActivity.class);
                intent.putExtra("wariningNum", mMessage.getWarning_num());
                startActivity(intent);

                break;
        }
    }

    //获得处警方式
    private void getAlarmType(String token) {

        RequestParams params = new RequestParams();

        params.addBodyParameter("api_token", token);

        HttpTool.getInstance(this).httpWithParams(Url.DETAIL_TYPE, params, new SMObjectCallBack<AlarmTypes>() {

            @Override
            public void onSuccess(AlarmTypes alarmTypes) {

                if (alarmTypes.getCode() == 200) {

                    typesList = alarmTypes.getResult();

                    groups.clear();

                    for (int i = 0; i < typesList.size(); i++) {

                        groups.add(typesList.get(i).getType());

                    }

                } else {

//                    ToastUtil.showToast(alarmTypes.getMsg(), AlarmInformationDetailActivity.this);
                }
            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }

    //提交处警方式
    private void upLoadDetailAlarmData(String warningType, String token, String Warning_num, String remark, String deal_peo) {

        RequestParams params = new RequestParams();

        params.addBodyParameter("warning_type", warningType);
        params.addBodyParameter("remark", remark);
        params.addBodyParameter("warning_num", Warning_num);
        params.addBodyParameter("api_token", token);
        params.addBodyParameter("deal_peo", deal_peo);


        HttpTool.getInstance(this).httpWithParams(Url.DRTAIL_ALARM_MESSAGE, params, new SMObjectCallBack<BaseModel>() {

            @Override
            public void onSuccess(BaseModel baseModel) {

                if (baseModel.getCode() == 200) {

                    ToastUtil.showToast("提交成功", AlarmInformationDetailActivity.this);
                    finish();
                }

            }

            @Override
            public void onError(int error, String msg) {

            }
        });

    }

    @Override
    public void OnPopupuwindowClick(int postition) {

        tvAlarmType.setText(groups.get(postition));
        alarmID = typesList.get(postition).getType_id();

        showSoftInputFromWindow(this,etRemarks);
    }
    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

}
