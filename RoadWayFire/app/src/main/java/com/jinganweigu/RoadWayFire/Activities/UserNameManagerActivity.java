package com.jinganweigu.RoadWayFire.Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.Adapters.ContactsManagerAdapter;
import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseActivity;
import com.jinganweigu.RoadWayFire.BeseClassUtils.MyApplication;
import com.jinganweigu.RoadWayFire.Entries.BaseModel;
import com.jinganweigu.RoadWayFire.Entries.Contacts;
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

public class UserNameManagerActivity extends BaseActivity implements ContactsManagerAdapter.onClickListener {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.et_phone_num)
    EditText etPhoneNum;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.lv_user)
    ListView lvUser;
    private String token;
    private String company_id;
    private List<Contacts.ResultBean> contactsList = new ArrayList<>();
    private ContactsManagerAdapter adapter;


    @Override
    public void initViews() {

        setContentView(R.layout.activity_user_name);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        topName.setText("添加人员");

    }

    @Override
    public void initData() {

        String token = Sptools.getString(this, Mycontants.API_TOKEN, "");
        String user_id = Sptools.getString(this, Mycontants.USER_ID, "");
        String company_id = Sptools.getString(this, Mycontants.COMPANY_ID, "");
        if (TextUtils.isEmpty(token)) {
            ToastUtil.showToast("TOKEN有误", this);
        } else if (TextUtils.isEmpty(user_id)) {
            ToastUtil.showToast("userId有误", this);
        } else if (TextUtils.isEmpty(company_id)) {
            ToastUtil.showToast("COMPANYID有误", this);
        } else {

            getdata(token, company_id, user_id);

        }

    }

    @Override
    public void initEvents() {

    }


    @OnClick({R.id.back_btn, R.id.btn_add, R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_add:

                etName.setText("");
                etPhoneNum.setText("");

                break;
            case R.id.btn_sure:

                token = Sptools.getString(this, Mycontants.API_TOKEN, "");
                company_id = Sptools.getString(this, Mycontants.COMPANY_ID, "");
                String user_id = etPhoneNum.getText().toString().trim();
                String name = etName.getText().toString().trim();

                if (TextUtils.isEmpty(token)) {

                    ToastUtil.showToast("TOKEN有误", this);
                } else if (TextUtils.isEmpty(company_id)) {
                    ToastUtil.showToast("COMPANYID有误", this);

                } else if (TextUtils.isEmpty(user_id)) {
                    ToastUtil.showToast("请输入手机号", this);

                } else if (TextUtils.isEmpty(name)) {
                    ToastUtil.showToast("请填写姓名", this);

                } else {

                    InsertUser(token, user_id, company_id, name);
                }

                break;
        }
    }

    private void InsertUser(String token, String user_name, String company_id, String name) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("api_token", token);
        params.addBodyParameter("user_name", user_name);
        params.addBodyParameter("company_id", company_id);
        params.addBodyParameter("name", name);

        HttpTool.getInstance(this).httpWithParams(Url.INSERT_USER_TO_COMPANY, params, new SMObjectCallBack<BaseModel>() {

            @Override
            public void onSuccess(BaseModel baseModel) {


                if (baseModel.getCode() == 200) {

                    ToastUtil.showToast("添加成功", UserNameManagerActivity.this);

                    getdata(token, company_id, user_name);

                } else {

                    ToastUtil.showToast(baseModel.getMsg(), UserNameManagerActivity.this);

                }


            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }

    private void getdata(String token, String company_id, String id) {

        RequestParams params = new RequestParams();

        params.addBodyParameter("company_id", company_id);
        params.addBodyParameter("api_token", token);
        params.addBodyParameter("user_id", id);

        HttpTool.getInstance(this).httpWithParams(Url.GET_CONTACTS_LIST, params, new SMObjectCallBack<Contacts>() {

            @Override
            public void onSuccess(Contacts contacts) {

                if (contacts.getCode() == 200) {

                    contactsList.clear();

                    contactsList = contacts.getResult();

                    adapter = new ContactsManagerAdapter(UserNameManagerActivity.this, contactsList);

                    lvUser.setAdapter(adapter);

                } else {

                    ToastUtil.showToast(contacts.getMsg(), UserNameManagerActivity.this);
                }

            }

            @Override
            public void onError(int error, String msg) {

            }
        });

    }

    @Override
    public void onClick(int position) {

        BaseUtils.showAlertDialog2(this, "人员管理", "确定删除？", new BaseUtils.OnConfirm() {
            @Override
            public void onConfirm() {

                delateuser(Sptools.getString(UserNameManagerActivity.this, Mycontants.API_TOKEN, ""), contactsList.get(position).getUser_id(), Sptools.getString(UserNameManagerActivity.this, Mycontants.COMPANY_ID, ""), position);
            }
        });



    }

    private void delateuser(String token, String user_id, String company_id, int position) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("user_id", user_id);
        params.addBodyParameter("company_id", company_id);
        params.addBodyParameter("api_token", token);

        HttpTool.getInstance(this).httpWithParams(Url.DELATE_USER_MANAGER, params, new SMObjectCallBack<BaseModel>() {

            @Override
            public void onSuccess(BaseModel baseModel) {

                if (baseModel.getCode() == 200) {

                    contactsList.remove(position);

                    adapter.notifyDataSetChanged();

                    ToastUtil.showToast("删除成功", UserNameManagerActivity.this);

                } else {

                    ToastUtil.showToast(baseModel.getMsg(), UserNameManagerActivity.this);
                }

            }

            @Override
            public void onError(int error, String msg) {

            }
        });

    }


}
