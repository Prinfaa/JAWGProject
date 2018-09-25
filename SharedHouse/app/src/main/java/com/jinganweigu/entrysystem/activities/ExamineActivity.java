package com.jinganweigu.entrysystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.common.BaseActivity;
import com.jinganweigu.entrysystem.entry.ExamineDeviceNumBean;
import com.jinganweigu.entrysystem.utils.HttpCode;
import com.jinganweigu.entrysystem.utils.Mycontants;
import com.jinganweigu.entrysystem.utils.Sptools;
import com.jinganweigu.entrysystem.utils.ToastUtil;
import com.jinganweigu.entrysystem.utils.Url;
import com.jinganweigu.entrysystem.utils.http.HttpTool;
import com.jinganweigu.entrysystem.utils.http.SMObjectCallBack;
import com.lidroid.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExamineActivity extends BaseActivity {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.tv_good_into_house_examine_num)
    TextView tvGoodIntoHouseExamineNum;
    @BindView(R.id.ll_good)
    LinearLayout llGood;
    @BindView(R.id.tv_waste_into_house_examine_num)
    TextView tvWasteIntoHouseExamineNum;
    @BindView(R.id.ll_waste)
    LinearLayout llWaste;
    @BindView(R.id.tv_out_house_examine_num)
    TextView tvOutHouseExamineNum;
    @BindView(R.id.ll_out)
    LinearLayout llOut;

    @Override
    public void initViews() {

        setContentView(R.layout.activity_examine);
        ButterKnife.bind(this);
        tvSave.setVisibility(View.GONE);
        topName.setText("审核");
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary), true);


    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvents() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        String name = Sptools.getString(this, Mycontants.REAL_NAME, "");

        if (!TextUtils.isEmpty(name)) {

            getdata(name);

        }

    }

    @OnClick({R.id.back_btn, R.id.ll_good, R.id.ll_waste, R.id.ll_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:

                intentMethod.rebackMethod(this);

                break;
            case R.id.ll_good:

                intentMethod.startMethodWithStringInt(this, DeviceEnterExamineActivity.class, "deviceType", "成品入库", "from", 0);//0是入库，1是出库

                break;
            case R.id.ll_waste:

                intentMethod.startMethodWithStringInt(this, DeviceEnterExamineActivity.class, "deviceType", "废品入库", "from", 0);//0是入库，1是出库

                break;
            case R.id.ll_out:


//                intentMethod.startMethodWithInt(this, DeviceOutExamineActivity.class, "from", 1);//0是入库，1是出库
                Intent intent = new Intent(this, DeviceOutExamineActivity.class);
                startActivity(intent);

                break;
        }
    }


    private void getdata(String name) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("people", name);

        HttpTool.getInstance(this).httpWithParams(Url.EXAMINE_ALL_DEVICE_NUM, params, new SMObjectCallBack<ExamineDeviceNumBean>() {
            @Override
            public void onSuccess(ExamineDeviceNumBean examineDeviceNumBean) {

                if (examineDeviceNumBean.getCode() == HttpCode.REQUEST_SUCCESS) {

                    int in = Integer.valueOf(examineDeviceNumBean.getResult().get(0).getIn_advance());
                    int waste = Integer.valueOf(examineDeviceNumBean.getResult().get(0).getWaste_advance());
                    int out = Integer.valueOf(examineDeviceNumBean.getResult().get(0).getOut_advance());
                    if (in > 0) {
                        tvGoodIntoHouseExamineNum.setTextColor(getResources().getColor(R.color.big_red));
                        tvGoodIntoHouseExamineNum.setText(examineDeviceNumBean.getResult().get(0).getIn_advance());

                    } else {

                        tvGoodIntoHouseExamineNum.setText(examineDeviceNumBean.getResult().get(0).getIn_advance());

                    }

                    if (waste > 0) {
                        tvWasteIntoHouseExamineNum.setTextColor(getResources().getColor(R.color.big_red));
                        tvWasteIntoHouseExamineNum.setText(examineDeviceNumBean.getResult().get(0).getWaste_advance());

                    } else {
                        tvWasteIntoHouseExamineNum.setText(examineDeviceNumBean.getResult().get(0).getWaste_advance());
                    }

                    if (out > 0) {
                        tvOutHouseExamineNum.setTextColor(getResources().getColor(R.color.big_red));
                        tvOutHouseExamineNum.setText(examineDeviceNumBean.getResult().get(0).getOut_advance());

                    } else {
                        tvOutHouseExamineNum.setText(examineDeviceNumBean.getResult().get(0).getOut_advance());
                    }


                } else {


                    ToastUtil.showToast(examineDeviceNumBean.getMsg(), ExamineActivity.this);

                }


            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }

}
