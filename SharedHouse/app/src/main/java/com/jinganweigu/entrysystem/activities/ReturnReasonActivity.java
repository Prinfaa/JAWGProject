package com.jinganweigu.entrysystem.activities;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.activity.ScanCodeAdapter;
import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.common.BaseActivity;
import com.jinganweigu.entrysystem.entry.BaseBean;
import com.jinganweigu.entrysystem.entry.SureOutHouseBean;
import com.jinganweigu.entrysystem.utils.HttpCode;
import com.jinganweigu.entrysystem.utils.Mycontants;
import com.jinganweigu.entrysystem.utils.Sptools;
import com.jinganweigu.entrysystem.utils.ToastUtil;
import com.jinganweigu.entrysystem.utils.Url;
import com.jinganweigu.entrysystem.utils.http.HttpTool;
import com.jinganweigu.entrysystem.utils.http.SMObjectCallBack;
import com.jinganweigu.entrysystem.view.CommonProgressDialog;
import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReturnReasonActivity extends BaseActivity {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.fl_title)
    FrameLayout flTitle;
    @BindView(R.id.lv_device_information)
    ListView lvDeviceInformation;
    @BindView(R.id.tv_show_return_project)
    TextView tvShowReturnProject;
    @BindView(R.id.btn_choose_project)
    Button btnChooseProject;
    @BindView(R.id.ed_why_return)
    EditText edWhyReturn;
    @BindView(R.id.btn_sure)
    Button btnSure;
    private CommonProgressDialog dialog;
    private List<String> DeviceNum = new ArrayList<>();
    private ScanCodeAdapter adapter;
    private String deviceId;
    private int returnThing;

    @Override
    public void initViews() {

        setContentView(R.layout.activity_return_reason);
        ButterKnife.bind(this);
        tvSave.setVisibility(View.GONE);
        topName.setText("退货");
        dialog = new CommonProgressDialog(this, "正在载入....");
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary), true);

    }

    @Override
    public void initData() {

        deviceId = getIntent().getStringExtra("deviceId");
        returnThing = getIntent().getIntExtra("ReturnThing", 0);

//        Log.e("loge", "initData: ===>"+deviceId+"returnThing====>"+returnThing );


        if (!TextUtils.isEmpty(deviceId)) {

            CheckOutReturnDevice(deviceId);

        }

        adapter = new ScanCodeAdapter(this, DeviceNum, 3);//只要不是0 item的textview色值为黑色

        lvDeviceInformation.setAdapter(adapter);

    }

    @Override
    public void initEvents() {

    }


    @OnClick({R.id.back_btn, R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:

                intentMethod.rebackMethod(this);

                break;
            case R.id.btn_sure:

                String reason = edWhyReturn.getText().toString().trim();
                String name = Sptools.getString(ReturnReasonActivity.this, Mycontants.REAL_NAME, "");
                if (TextUtils.isEmpty(deviceId)) {

                    ToastUtil.showToast("设备id为空", ReturnReasonActivity.this);
                } else if (TextUtils.isEmpty(reason)) {

                    ToastUtil.showToast("请输入退货原因", ReturnReasonActivity.this);
                } else if (TextUtils.isEmpty(name)) {

                    ToastUtil.showToast("退货人为空", ReturnReasonActivity.this);
                } else {

                    if (returnThing == 0) {//正常退货

                        GoodReturn(deviceId, name, reason);

                    } else if (returnThing == 1) {//损坏退货

                        BadReturn(deviceId, name, reason);

                    }

                }


                break;
        }
    }

    //查询出退货设备

    private void CheckOutReturnDevice(String DeviceId) {


        dialog.show();

        RequestParams params = new RequestParams();

        params.addBodyParameter("rtu_id", DeviceId);

        HttpTool.getInstance(this).httpWithParams(Url.SURE_OUT_HOUSE, params, new SMObjectCallBack<SureOutHouseBean>() {


            @Override
            public void onSuccess(SureOutHouseBean sureOutHouseBean) {

                dialog.dismiss();

                if (sureOutHouseBean.getCode() == HttpCode.REQUEST_SUCCESS) {


                    for (int i = 0; i < sureOutHouseBean.getResult().size(); i++) {

                        DeviceNum.add(sureOutHouseBean.getResult().get(i).getFirst_line() + ":" + sureOutHouseBean.getResult().get(i).getSecond_line());

                    }


                    adapter.notifyDataSetChanged();
//                    list = sureOutHouseBean.getResult();

//                    detailadapter = new DeviceDeticalAdapter(list, CaptureActivity.this);

//                    xListView.setAdapter(adapter);


                } else {

                    dialog.dismiss();


                    ToastUtil.showToast(sureOutHouseBean.getMsg(), ReturnReasonActivity.this);


                }


            }

            @Override
            public void onError(int error, String msg) {


                dialog.dismiss();

            }
        });


    }


    //正常退货
    private void GoodReturn(final String deviceId, String name, String returnReason) {

        dialog.show();
        RequestParams params = new RequestParams();
        params.addBodyParameter("rtu_id", deviceId);
        params.addBodyParameter("return_peo_nice", name);
        params.addBodyParameter("return_reason", returnReason);

        HttpTool.getInstance(this).httpWithParams(Url.GOOD_DEVICE_RETURN, params, new SMObjectCallBack<BaseBean>() {

            @Override
            public void onSuccess(BaseBean baseBean) {

                dialog.dismiss();

                if (baseBean.getCode() == HttpCode.REQUEST_SUCCESS) {

                    ToastUtil.showToast(baseBean.getMsg(), ReturnReasonActivity.this);
//
//                    DeviceNum.add(deviceId);
//
//                    adapter.notifyDataSetChanged();

                    finish();


                } else {

                    ToastUtil.showToast(baseBean.getMsg(), ReturnReasonActivity.this);

                }

            }

            @Override
            public void onError(int error, String msg) {

                dialog.dismiss();
                ToastUtil.showToast("网络不给力呀哈！！！", ReturnReasonActivity.this);

            }


        });


    }

    //损坏退货
    private void BadReturn(String deviceId, String name, String returnReason) {

        dialog.show();
        RequestParams params = new RequestParams();
        params.addBodyParameter("rtu_id", deviceId);
        params.addBodyParameter("return_peo_bad", name);
        params.addBodyParameter("return_reason", returnReason);

        HttpTool.getInstance(this).httpWithParams(Url.BAD_DEVICE_RETURN, params, new SMObjectCallBack<BaseBean>() {

            @Override
            public void onSuccess(BaseBean baseBean) {

                dialog.dismiss();

                if (baseBean.getCode() == HttpCode.REQUEST_SUCCESS) {

                    ToastUtil.showToast(baseBean.getMsg(), ReturnReasonActivity.this);
                    finish();

                } else {

                    ToastUtil.showToast(baseBean.getMsg(), ReturnReasonActivity.this);

                }

            }

            @Override
            public void onError(int error, String msg) {

                dialog.dismiss();
                ToastUtil.showToast("网络不给力呀哈！！！", ReturnReasonActivity.this);

            }
        });


    }


}
