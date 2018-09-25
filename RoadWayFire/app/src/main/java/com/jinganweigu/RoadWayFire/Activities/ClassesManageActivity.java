package com.jinganweigu.RoadWayFire.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.jinganweigu.RoadWayFire.Adapters.ClassManagerAdapter;
import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseActivity;
import com.jinganweigu.RoadWayFire.BeseClassUtils.MyApplication;
import com.jinganweigu.RoadWayFire.Entries.BaseModel;
import com.jinganweigu.RoadWayFire.Entries.ClassManagerList;
import com.jinganweigu.RoadWayFire.Interfaces.Mycontants;
import com.jinganweigu.RoadWayFire.Interfaces.Url;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.BaseUtils;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.Sptools;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.ToastUtil;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.HttpTool;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.SMObjectCallBack;
import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClassesManageActivity extends BaseActivity implements ClassManagerAdapter.OnClassClickListener {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.btn_add)
    Button btnAdd;

    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.lv_classer)
    ListView lvClasser;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_start_time)
    TextView etStartTime;
    @BindView(R.id.et_end_time)
    TextView etEndTime;
    private List<String> dayList = new ArrayList<>();
    private List<String> hourList = new ArrayList<>();
    private List<String> minList = new ArrayList<>();
    private String start_day, stop_day;
    private String start_hour, start_min;
    private String end_hour, end_min;
    private List<ClassManagerList.ResultBean> classList = new ArrayList<>();
    private ClassManagerAdapter adapter;
    private String token;
    private String company_id;

    @Override
    public void initViews() {

        setContentView(R.layout.activity_classes_manage);
        ButterKnife.bind(this);
        topName.setText("班次管理");
        MyApplication.getInstance().addActivity(this);
        dayList.clear();
        hourList.clear();
        minList.clear();
        addDataPicker();
    }

    @Override
    public void initData() {
        token = Sptools.getString(this, Mycontants.API_TOKEN, "");
        company_id = Sptools.getString(this, Mycontants.COMPANY_ID, "");

        if (TextUtils.isEmpty(token)) {
            ToastUtil.showToast("TOKEN错误", this);
        } else if (TextUtils.isEmpty(company_id)) {
            ToastUtil.showToast("COMPANYUD错误", this);
        } else {

            getClassDataList(token, company_id);

        }

    }

    @Override
    public void initEvents() {

        lvClasser.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


//                Log.e("abc", "onItemLo/ngClick: ===》"+classList.get(position).getClasses_name() );

                Intent intent=new Intent(ClassesManageActivity.this,PersonInnerClassActivity.class);
                intent.putExtra("cla_starttime",classList.get(position).getCla_starttime());
                intent.putExtra("cla_stoptime",classList.get(position).getCla_stoptime());
                intent.putExtra("classes_id",classList.get(position).getClasses_id());
                intent.putExtra("class_name",classList.get(position).getClasses_name());

                startActivity(intent);

//                intentMethod.startMethodTwoString(ClassesManageActivity.this,PersonInnerClassActivity.class,"cla_starttime",classList.get(position).getCla_starttime(),"cla_stoptime",classList.get(position).getCla_stoptime());



            }
        });




    }

    @OnClick({R.id.btn_add, R.id.et_start_time, R.id.et_end_time, R.id.btn_sure, R.id.back_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add:

                etName.setText("");

                etStartTime.setText("");

                etEndTime.setText("");

                break;
            case R.id.et_start_time:
                initNoLinkOptionsPicker(etStartTime);
                break;
            case R.id.et_end_time:

                initNoLinkOptionsPicker(etEndTime);

                break;
            case R.id.btn_sure:

                String start = etStartTime.getText().toString().trim().substring(0, 2);
                String end = etEndTime.getText().toString().trim().substring(0, 2);

                if (start.equals("今天")) {
                    start_day = "" + 0;
                } else {
                    start_day = "" + 86400;
                }
                if (end.equals("今天")) {
                    stop_day = "" + 0;
                } else {
                    stop_day = "" + 86400;
                }

                String class_name = etName.getText().toString().trim();
                String cla_starttime = start_hour + ":" + start_min;
                String cla_stoptime = end_hour + ":" + end_min;
                if (TextUtils.isEmpty(start_day)) {
                    ToastUtil.showToast("请选择开始时间", this);
                } else if (TextUtils.isEmpty(stop_day)) {
                    ToastUtil.showToast("请选择结束时间", this);
                } else if (TextUtils.isEmpty(token)) {
                    ToastUtil.showToast("TOKEN错误", this);
                } else if (TextUtils.isEmpty(class_name)) {
                    ToastUtil.showToast("请输入班次名称", this);
                } else if (TextUtils.isEmpty(company_id)) {
                    ToastUtil.showToast("COMPANYUD错误", this);
                } else {
                    AddClassData(token, class_name, company_id, cla_starttime, start_day, cla_stoptime, stop_day);
                }
                break;
            case R.id.back_btn:

                finish();

                break;
        }
    }


    private void addDataPicker() {

        dayList.add("今天");
        dayList.add("明天");

        for (int i = 0; i < 24; i++) {

            if (i <= 9) {

                hourList.add("0" + i + "时");

            } else {

                hourList.add(i + "时");
            }

        }


        for (int i = 0; i < 60; i++) {

            if (i <= 9) {

                minList.add("0" + i + "分");

            } else {

                minList.add(i + "分");
            }


        }


    }

    //添加班次接口
    private void AddClassData(String api_token, String classes_name, String company_id, String cla_starttime, String start_day, String cla_stoptime, String stop_day) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("api_token", api_token);
        params.addBodyParameter("classes_name", classes_name);
        params.addBodyParameter("company_id", company_id);
        params.addBodyParameter("cla_starttime", cla_starttime);
        params.addBodyParameter("start_day", start_day);
        params.addBodyParameter("cla_stoptime", cla_stoptime);
        params.addBodyParameter("stop_day", stop_day);

        HttpTool.getInstance(this).httpWithParams(Url.ADD_CLASS_MANAGER, params, new SMObjectCallBack<BaseModel>() {

            @Override
            public void onSuccess(BaseModel baseModel) {
                if (baseModel.getCode() == 200) {

                    ToastUtil.showToast("班次添加成功", ClassesManageActivity.this);

                }

            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }


    private void initNoLinkOptionsPicker(TextView editText) {// 不联动的多级选项
        OptionsPickerView pvNoLinkOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

                String str = dayList.get(options1) + hourList.get(options2) + minList.get(options3);

                if (editText.getId() == R.id.et_start_time) {

                    start_hour = hourList.get(options2).substring(0, 2);
                    start_min = minList.get(options3).substring(0, 2);
                } else if (editText.getId() == R.id.et_end_time) {
                    end_hour = hourList.get(options2).substring(0, 2);
                    end_min = minList.get(options3).substring(0, 2);
                }
                editText.setText(str);
            }
        })
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
//                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
//
//                        Log.e("abc", "onOptionsSelectChanged: ===>"+str );

                    }
                })
                .build();
        pvNoLinkOptions.setNPicker(dayList, hourList, minList);


        pvNoLinkOptions.show();
    }

    //查询班次
    private void getClassDataList(String token, String company_id) {

        RequestParams params = new RequestParams();

        params.addBodyParameter("api_token", token);
        params.addBodyParameter("company_id", company_id);

        HttpTool.getInstance(this).httpWithParams(Url.GET_CLASS_LIST_MANAGET, params, new SMObjectCallBack<ClassManagerList>() {

            @Override
            public void onSuccess(ClassManagerList classManagerList) {

                if (classManagerList.getCode() == 200) {
                    classList.clear();
                    classList = classManagerList.getResult();
                    adapter = new ClassManagerAdapter(ClassesManageActivity.this, classList);
                    lvClasser.setAdapter(adapter);


                } else {

                    ToastUtil.showToast(classManagerList.getMsg(), ClassesManageActivity.this);

                }


            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }

    @Override
    public void OnClassClick(int position) {

        if (classList.size() > 0) {

            if (TextUtils.isEmpty(token)) {
                ToastUtil.showToast("TOKEN错误", this);
            } else if (TextUtils.isEmpty(company_id)) {
                ToastUtil.showToast("COMPANYUD错误", this);
            } else {

                BaseUtils.showAlertDialog2(ClassesManageActivity.this, "班次管理", "确认删除？", new BaseUtils.OnConfirm() {
                    @Override
                    public void onConfirm() {

                        DelateClassManager(token, company_id, classList.get(position).getClasses_id(), position);

                    }
                });



            }

        }

    }

    private void DelateClassManager(String token, String company_id, String class_id, int position) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("api_token", token);
        params.addBodyParameter("company_id", company_id);
        params.addBodyParameter("classes_id", class_id);

        HttpTool.getInstance(this).httpWithParams(Url.DELATE_CLASS_LIST_ITEM, params, new SMObjectCallBack<BaseModel>() {

            @Override
            public void onSuccess(BaseModel baseModel) {

                if (baseModel.getCode() == 200) {

                    ToastUtil.showToast("删除成功", ClassesManageActivity.this);

                    classList.remove(position);

                    adapter.notifyDataSetChanged();

                } else {

                    ToastUtil.showToast(baseModel.getMsg(), ClassesManageActivity.this);

                }


            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }


}
