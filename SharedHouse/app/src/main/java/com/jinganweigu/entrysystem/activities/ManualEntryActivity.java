package com.jinganweigu.entrysystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.activity.ScanCodeAdapter;
import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.adapter.DeviceDeticalAdapter;
import com.jinganweigu.entrysystem.common.BaseActivity;
import com.jinganweigu.entrysystem.entry.BaseBean;
import com.jinganweigu.entrysystem.entry.OutHouseBean;
import com.jinganweigu.entrysystem.entry.ReadyEnterDeviceBean;
import com.jinganweigu.entrysystem.entry.SureOutHouseBean;
import com.jinganweigu.entrysystem.entry.WasteDeviceEnterBean;
import com.jinganweigu.entrysystem.utils.HttpCode;
import com.jinganweigu.entrysystem.utils.MyApplication;
import com.jinganweigu.entrysystem.utils.Mycontants;
import com.jinganweigu.entrysystem.utils.NoDoubleClickListener;
import com.jinganweigu.entrysystem.utils.Sptools;
import com.jinganweigu.entrysystem.utils.ToastUtil;
import com.jinganweigu.entrysystem.utils.Url;
import com.jinganweigu.entrysystem.utils.http.HttpTool;
import com.jinganweigu.entrysystem.utils.http.SMObjectCallBack;
import com.jinganweigu.entrysystem.view.CommonProgressDialog;
import com.lidroid.xutils.http.RequestParams;
import com.xw.repo.xedittext.XEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManualEntryActivity extends BaseActivity {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.fl_title)
    FrameLayout flTitle;
    @BindView(R.id.sure)
    Button sure;
    @BindView(R.id.btn_sure_enter)
    Button btnSureEnter;
    @BindView(R.id.btn_bad_return)
    Button btnBadReturn;
    @BindView(R.id.btn_good_return)
    Button btnGoodReturn;
    @BindView(R.id.ll_return)
    LinearLayout llReturn;
    @BindView(R.id.et_num)
    XEditText etNum;
    @BindView(R.id.lv_deviceId)
    ListView lvDeviceId;
    @BindView(R.id.tv_show_return_project)
    TextView tvShowReturnProject;
    @BindView(R.id.btn_choose_project)
    Button btnChooseProject;
    @BindView(R.id.ed_why_return)
    EditText edWhyReturn;
    @BindView(R.id.ll_return_part)
    LinearLayout llReturnPart;
    @BindView(R.id.view)
    View view;

// 包括手动入库 手动出库 手动退货


    private CommonProgressDialog dialog;

    //    private List<String> IntoDeviceNum = new ArrayList<>();
//    private ScanCodeAdapter IntoAdapter;
    private int a;

    //出库参数

    private String Leader;
    private String project;
    private List<SureOutHouseBean.ResultBean> outLists = new ArrayList<>();
    private DeviceDeticalAdapter adapter;

    private String deviceType;


    @Override
    public void initViews() {
        setContentView(R.layout.activity_manual_entry);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        a = getIntent().getIntExtra("from", 0);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary), true);
        dialog = new CommonProgressDialog(this, "正在加载....");

        etNum.setSeparator("-");//设置分隔符
        etNum.setPattern(new int[]{5, 5, 5});

//        etNum.setOnClickListener(new NoDoubleClickListener() {
//            @Override
//            protected void onNoDoubleClick(View v) {
//
//
//
//            }
//        });


        if (a == 0) {//手动入库

            topName.setText("手动入库");
            tvSave.setVisibility(View.GONE);
            deviceType = getIntent().getStringExtra("deviceType");


        } else if (a == 1) {//手动出库

            topName.setText("手动出库");
            tvSave.setVisibility(View.GONE);
            btnSureEnter.setText("确认出库");
            Leader = getIntent().getStringExtra("receivePeople");
            project = getIntent().getStringExtra("receive_unit");


        } else if (a == 2) {//手动退货

            topName.setText("手动退货");
            tvSave.setVisibility(View.GONE);
            llReturn.setVisibility(View.VISIBLE);
            btnSureEnter.setVisibility(View.GONE);
            llReturnPart.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
        }


//        IntoAdapter = new ScanCodeAdapter(this, IntoDeviceNum, 1);
//        lvDeviceId.setAdapter(IntoAdapter);//手动入库adapter


    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvents() {

    }


    @OnClick({R.id.back_btn, R.id.sure, R.id.btn_sure_enter, R.id.btn_bad_return, R.id.btn_good_return, R.id.btn_choose_project})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:

                intentMethod.rebackMethod(this);

                break;
            case R.id.sure:

//                String name = Sptools.getString(this, Mycontants.REAL_NAME, "");

                String rDeviceId = etNum.getNonSeparatorText().toString().trim();//获取无分割线的数据

                CheckDeviceInformation(rDeviceId);

//
//                if (a == 0) {//手动入库 确认
//
//                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(rDeviceId)) {
//
//                        if (deviceType.equals("成品入库")) {
////
//
//
//
//                        } else if (deviceType.equals("废品入库")) {
//
////                            WasteManualIntoHouse(rDeviceId, name);
//
//
//                        }
//                    } else {
//
//                        ToastUtil.showToast("参数不完整", this);
//
//                    }
//
//                } else if (a == 1) {//手动出库  确认按钮
//
//                    String cDeviceId = etNum.getNonSeparatorText().toString().trim();//获取无分割线的数据
//
//                    if (!TextUtils.isEmpty(cDeviceId)) {
//
//                        SureManualOutHouse(cDeviceId);
//
//                    } else {
//
//                        ToastUtil.showToast("请输入设备号", this);
//
//                    }
//
//                } else {
//
//                    String cDeviceId = etNum.getNonSeparatorText().toString().trim();//获取无分割线的数据
//
//                    if (!TextUtils.isEmpty(cDeviceId)) {
//
//                        SureManualOutHouse(cDeviceId);
//
//                    } else {
//
//                        ToastUtil.showToast("请输入设备号", this);
//
//                    }
//
//
//                }

                break;
            case R.id.btn_sure_enter://确定入库、确定出库

                if (a == 0) {//确定入库

                    String num = etNum.getNonSeparatorText().toString().trim();//获取无分割线的数据

                    String cname = Sptools.getString(this, Mycontants.REAL_NAME, "");

                    if (TextUtils.isEmpty(num) || TextUtils.isEmpty(cname)) {

                        ToastUtil.showToast("参数不完整", this);
                    } else {

                        if (deviceType.equals("成品入库")) {

                            GoodManualIntoHouse(num, cname);

                        } else if (deviceType.equals("新品入库")) {

                            upLoadNewDeviceIntoHouse(num, cname);

                        } else if (deviceType.equals("废品入库")) {

                            WasteManualIntoHouse(num, cname);

                        }

                    }
                } else if (a == 1) {//确定出库

                    String num = etNum.getNonSeparatorText().toString().trim();//获取无分割线的数据

                    String cname = Sptools.getString(this, Mycontants.REAL_NAME, "");

                    if (TextUtils.isEmpty(num)) {

                        ToastUtil.showToast("请输入设备号", this);

                    } else if (TextUtils.isEmpty(cname)) {

                        ToastUtil.showToast("姓名为空", this);

                    } else if (TextUtils.isEmpty(Leader)) {

                        ToastUtil.showToast("没有领用人", this);
                    } else if (TextUtils.isEmpty(project)) {

                        ToastUtil.showToast("没有领用单位", this);
                    } else {

                        ManualOutHouse(num, cname, Leader, project);

                    }

                }

                break;

            case R.id.btn_bad_return://损坏退货

                String bnum = etNum.getNonSeparatorText().toString().trim();//获取无分割线的数据
                String bname = Sptools.getString(this, Mycontants.REAL_NAME, "");
                String breason = edWhyReturn.getText().toString().trim();

                if (TextUtils.isEmpty(bnum)) {

                    ToastUtil.showToast("请输入设备号", ManualEntryActivity.this);

                } else if (TextUtils.isEmpty(bname)) {

                    ToastUtil.showToast("没有处理人", ManualEntryActivity.this);
                } else if (TextUtils.isEmpty(breason)) {

                    ToastUtil.showToast("请输入退货原因", ManualEntryActivity.this);

                } else {

                    BadReturn(bnum, bname, breason);

                }
                break;
            case R.id.btn_good_return://正常退货
                String gnum = etNum.getNonSeparatorText().toString().trim();//获取无分割线的数据
                String gname = Sptools.getString(this, Mycontants.REAL_NAME, "");
                String greason = edWhyReturn.getText().toString().trim();

                if (TextUtils.isEmpty(gnum)) {

                    ToastUtil.showToast("请输入设备号", ManualEntryActivity.this);
                } else if (TextUtils.isEmpty(gname)) {

                    ToastUtil.showToast("没有处理人", ManualEntryActivity.this);
                } else if (TextUtils.isEmpty(greason)) {

                    ToastUtil.showToast("请输入退货原因", ManualEntryActivity.this);

                } else {

                    GoodReturn(gnum, gname, greason);

                }


                break;


            case R.id.btn_choose_project://选择退货项目

                break;

        }
    }

    //成品手动入库 确认入库
    private void GoodManualIntoHouse(String DeviceID, String Name) {

        dialog.show();
        RequestParams params = new RequestParams();


        params.addBodyParameter("rtu_id", DeviceID);
        params.addBodyParameter("in_mitu_peo", Name);

        HttpTool.getInstance(this).httpWithParams(Url.ENTER_HOUSE_URL, params, new SMObjectCallBack<ReadyEnterDeviceBean>() {

            @Override
            public void onSuccess(ReadyEnterDeviceBean Bean) {

                Log.e("getCode", "onSuccess: ====>" + Bean.getCode());

                if (Bean.getCode() == HttpCode.REQUEST_SUCCESS) {//操作成功

//                    IntoDeviceNum.clear();

                    dialog.dismiss();

                    ToastUtil.showToast(Bean.getMsg(), ManualEntryActivity.this);


                    intentMethod.startMethodWithStringInt(ManualEntryActivity.this, DeviceEnterExamineActivity.class, "deviceType", "成品入库", "from", 0);//0是入库，1是出库

                    finish();

//                    CheckDeviceInformation();

//                    IntoDeviceNum.add("设备号：" + Bean.getResult().getRtu_id());
//                    IntoDeviceNum.add("设备类型号：" + Bean.getResult().getRtu_type());
//                    IntoDeviceNum.add("设备类型：" + Bean.getResult().getMitu_type());
//                    IntoDeviceNum.add("入库人：" + Bean.getResult().getIn_mitu_peo());
//                    IntoDeviceNum.add("仓库号：" + Bean.getResult().getWarehouse_id());
//                    IntoDeviceNum.add("仓库名：" + Bean.getResult().getWarehouse_name());
//
////
//                    IntoAdapter.notifyDataSetChanged();

//                    tvDeviceInformation.setText("设备号："+Bean.getResult().getRtu_id()+"\n"+"设备类型号："+Bean.getResult().getRtu_type()+"\n"+"设备类型："+
//                            Bean.getResult().getMitu_type()+"\n"+"入库人："+Bean.getResult().getIn_mitu_peo()+"\n"+"仓库号："+Bean.getResult().getWarehouse_id()+"\n"+
//                    "仓库名："+Bean.getResult().getWarehouse_name());


                } else if (Bean.getCode() == HttpCode.CODE_ERROR) {//操作失败
                    dialog.dismiss();
                    ToastUtil.showToast(Bean.getMsg(), ManualEntryActivity.this);


                } else if (Bean.getCode() == HttpCode.NO_DEVICE) {//没有该设备
                    dialog.dismiss();
                    ToastUtil.showToast(Bean.getMsg(), ManualEntryActivity.this);

                } else if (Bean.getCode() == HttpCode.FINISH_PRODUCT_INTO_HOUSE) {//该成品已入库
                    dialog.dismiss();
                    ToastUtil.showToast(Bean.getMsg(), ManualEntryActivity.this);

                } else if (Bean.getCode() == HttpCode.THE_FINISHED_PRODUCT_INTO_HOUSE_SUCCESS) {//该成品已预入库
                    dialog.dismiss();
                    ToastUtil.showToast(Bean.getMsg(), ManualEntryActivity.this);

                } else {

                    dialog.dismiss();

                    ToastUtil.showToast(Bean.getMsg(), ManualEntryActivity.this);


                }


            }

            @Override
            public void onError(int error, String msg) {


                dialog.dismiss();


            }
        });


    }


    // 手动 新品入库接口
    private void upLoadNewDeviceIntoHouse(String DeviceID, String Name) {
        dialog.show();
        RequestParams params = new RequestParams();

        params.addBodyParameter("rtu_id", DeviceID);
        params.addBodyParameter("in_mitu_peo", Name);


        HttpTool.getInstance(this).httpWithParams(Url.NEW_DEVICE_ENTER_HOUSE_URL, params, new SMObjectCallBack<ReadyEnterDeviceBean>() {

            @Override
            public void onSuccess(ReadyEnterDeviceBean Bean) {

                if (Bean.getCode() == HttpCode.REQUEST_SUCCESS) {//操作成功

                    dialog.dismiss();

                    ToastUtil.showToast(Bean.getMsg(), ManualEntryActivity.this);


                    intentMethod.startMethodWithStringInt(ManualEntryActivity.this, DeviceEnterExamineActivity.class, "deviceType", "新品入库", "from", 0);//0是入库，1是出库

                    finish();


                } else {

                    ToastUtil.showToast(Bean.getMsg(), ManualEntryActivity.this);

                    dialog.dismiss();

                }


            }

            @Override
            public void onError(int error, String msg) {


                dialog.dismiss();


            }
        });


    }


    //废品手动入库 sure
    private void WasteManualIntoHouse(String deviceId, String name) {

        dialog.show();

        RequestParams params = new RequestParams();

        params.addBodyParameter("rtu_id", deviceId);
        params.addBodyParameter("waste_peo", name);

        HttpTool.getInstance(this).httpWithParams(Url.BAD_ENTER_HOUSE_URL, params, new SMObjectCallBack<WasteDeviceEnterBean>() {


            @Override
            public void onSuccess(WasteDeviceEnterBean baseBean) {

                if (baseBean.getCode() == HttpCode.REQUEST_SUCCESS) {

                    dialog.dismiss();

                    ToastUtil.showToast(baseBean.getMsg(), ManualEntryActivity.this);

//                    IntoDeviceNum.clear();
//
//                    IntoDeviceNum.add("设备号：" + baseBean.getResult().getRtu_id());
//                    IntoDeviceNum.add("设备类型号：" + baseBean.getResult().getRtu_type());
//                    IntoDeviceNum.add("设备类型：" + baseBean.getResult().getMitu_type());
//                    IntoDeviceNum.add("入库人：" + baseBean.getResult().getIn_mitu_peo());
//                    IntoDeviceNum.add("仓库号：" + baseBean.getResult().getWarehouse_id());
//                    IntoDeviceNum.add("仓库名：" + baseBean.getResult().getWarehouse_name());
//                    IntoAdapter.notifyDataSetChanged();

//                    DeviceNum.add(baseBean.getResult().getRtu_id());

//                    adapter.notifyDataSetChanged();

//                    handler.restartPreviewAndDecode();//恢复扫描


                } else {


                    ToastUtil.showToast(baseBean.getMsg(), ManualEntryActivity.this);


                }


            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }

    //手动出库 sure
    private void SureManualOutHouse(String DeviceId) {

        dialog.show();

        RequestParams params = new RequestParams();

        params.addBodyParameter("rtu_id", DeviceId);

        HttpTool.getInstance(this).httpWithParams(Url.SURE_OUT_HOUSE, params, new SMObjectCallBack<SureOutHouseBean>() {


            @Override
            public void onSuccess(SureOutHouseBean sureOutHouseBean) {

                dialog.dismiss();

                if (sureOutHouseBean.getCode() == HttpCode.REQUEST_SUCCESS) {

//                    outLists = sureOutHouseBean.getResult();

//                    IntoDeviceNum.clear();

//                    for (int i = 0; i < outLists.size(); i++) {
//                        IntoDeviceNum.add(outLists.get(i).getFirst_line() + ":" + outLists.get(i).getSecond_line());
//                    }
//
//
//                    IntoAdapter.notifyDataSetChanged();


                } else {

                    dialog.dismiss();

//                    IntoDeviceNum.clear();

//                    IntoAdapter.notifyDataSetChanged();
                    ToastUtil.showToast(sureOutHouseBean.getMsg(), ManualEntryActivity.this);


                }


            }

            @Override
            public void onError(int error, String msg) {

//                IntoDeviceNum.clear();
//                IntoAdapter.notifyDataSetChanged();

                dialog.dismiss();

            }
        });


    }

    //手动出库
    private void ManualOutHouse(String DeviceID, String Name, String LeaderName, String project) {

        RequestParams params = new RequestParams();

        params.addBodyParameter("rtu_id", DeviceID);
        params.addBodyParameter("out_people", Name);
        params.addBodyParameter("receive_people", LeaderName);
        params.addBodyParameter("receive_unit", project);

        HttpTool.getInstance(this).httpWithParams(Url.DEVICE_OUT_HOUSE_URL, params, new SMObjectCallBack<OutHouseBean>() {


            @Override
            public void onSuccess(OutHouseBean outHouseBean) {

                ToastUtil.showToast(outHouseBean.getMsg(), ManualEntryActivity.this);

                Intent intent = new Intent(ManualEntryActivity.this, DeviceOutExamineActivity.class);
                startActivity(intent);
                finish();


//                finish();

            }

            @Override
            public void onError(int error, String msg) {

                Log.e("error", "onError: ===>出错了");

            }
        });


    }


    //正常退货
    private void GoodReturn(String deviceId, String name, String reason) {

        dialog.show();
        RequestParams params = new RequestParams();
        params.addBodyParameter("rtu_id", deviceId);
        params.addBodyParameter("return_peo_nice", name);
        params.addBodyParameter("return_reason", reason);

        HttpTool.getInstance(this).httpWithParams(Url.GOOD_DEVICE_RETURN, params, new SMObjectCallBack<BaseBean>() {

            @Override
            public void onSuccess(BaseBean baseBean) {

                dialog.dismiss();

                if (baseBean.getCode() == HttpCode.REQUEST_SUCCESS) {

                    ToastUtil.showToast(baseBean.getMsg(), ManualEntryActivity.this);
                    finish();


                } else {

                    ToastUtil.showToast(baseBean.getMsg(), ManualEntryActivity.this);

                }

            }

            @Override
            public void onError(int error, String msg) {

                dialog.dismiss();
                ToastUtil.showToast("网络不给力呀哈！！！", ManualEntryActivity.this);


            }
        });


    }

    //损坏退货
    private void BadReturn(String deviceId, String name, String reason) {

        dialog.show();
        RequestParams params = new RequestParams();
        params.addBodyParameter("rtu_id", deviceId);
        params.addBodyParameter("return_peo_bad", name);
        params.addBodyParameter("return_reason", reason);

        HttpTool.getInstance(this).httpWithParams(Url.BAD_DEVICE_RETURN, params, new SMObjectCallBack<BaseBean>() {

            @Override
            public void onSuccess(BaseBean baseBean) {

                dialog.dismiss();

                if (baseBean.getCode() == HttpCode.REQUEST_SUCCESS) {

                    ToastUtil.showToast(baseBean.getMsg(), ManualEntryActivity.this);
                    finish();


                } else {

                    ToastUtil.showToast(baseBean.getMsg(), ManualEntryActivity.this);

                }

            }

            @Override
            public void onError(int error, String msg) {

                dialog.dismiss();
                ToastUtil.showToast("网络不给力呀哈！！！", ManualEntryActivity.this);

            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    //  点击 sure 查询该设备信息
    private void CheckDeviceInformation(String DeviceId) {

        dialog.show();

        RequestParams params = new RequestParams();

        params.addBodyParameter("rtu_id", DeviceId);

        HttpTool.getInstance(this).httpWithParams(Url.SURE_OUT_HOUSE, params, new SMObjectCallBack<SureOutHouseBean>() {

            @Override
            public void onSuccess(SureOutHouseBean sureOutHouseBean) {

                dialog.dismiss();

                outLists.clear();

                if (sureOutHouseBean.getCode() == HttpCode.REQUEST_SUCCESS) {

                    outLists = sureOutHouseBean.getResult();

                    adapter = new DeviceDeticalAdapter(outLists, ManualEntryActivity.this);

                    lvDeviceId.setAdapter(adapter);

                } else {

                    dialog.dismiss();
                    ToastUtil.showToast(sureOutHouseBean.getMsg(), ManualEntryActivity.this);

                }

            }

            @Override
            public void onError(int error, String msg) {

                dialog.dismiss();

            }
        });


    }

}
