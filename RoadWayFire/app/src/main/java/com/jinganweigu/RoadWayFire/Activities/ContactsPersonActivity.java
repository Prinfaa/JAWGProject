package com.jinganweigu.RoadWayFire.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.Adapters.ContactsAdapter;
import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseActivity;
import com.jinganweigu.RoadWayFire.BeseClassUtils.MyApplication;
import com.jinganweigu.RoadWayFire.Entries.Contacts;
import com.jinganweigu.RoadWayFire.Interfaces.Mycontants;
import com.jinganweigu.RoadWayFire.Interfaces.Url;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.Sptools;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.ToastUtil;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.HttpTool;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.SMObjectCallBack;
import com.jinganweigu.RoadWayFire.ToolsUtils.widget.RefreshListView;
import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactsPersonActivity extends BaseActivity {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.et_search_contant)
    EditText etSearchContant;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.lv_contact)
    RefreshListView lvContact;
    private String company_id;
    private String id;
    private String token;

    private ContactsAdapter adapter;
    public static final int GET_RESULT_CODE=2;
    private List<Contacts.ResultBean> contactsList = new ArrayList<>();
    @Override
    public void initViews() {
        setContentView(R.layout.activity_contacts_person);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        topName.setText("通讯录");
    }

    @Override
    public void initData() {
        company_id = Sptools.getString(this, Mycontants.COMPANY_ID, "");
        id = Sptools.getString(this, Mycontants.USER_ID, "");
        token = Sptools.getString(this, Mycontants.API_TOKEN, "");


        if (TextUtils.isEmpty(company_id)) {
            ToastUtil.showToast("单位ID错误", this);
        } else if (TextUtils.isEmpty(id)) {
            ToastUtil.showToast("用户ID错误", this);
        } else if (TextUtils.isEmpty(token)) {
            ToastUtil.showToast("TOKEN错误", this);
        } else {
            getdata(token, company_id, id);

        }

    }

    @Override
    public void initEvents() {

        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent();
                intent.putExtra("phone",contactsList.get(position-1).getPhone());
                setResult(GET_RESULT_CODE,intent);
                finish();


            }
        });


    }



    @OnClick({R.id.back_btn, R.id.ll_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:

                finish();

                break;
            case R.id.ll_search:

                String name = etSearchContant.getText().toString().trim();
                if (TextUtils.isEmpty(company_id)) {
                    ToastUtil.showToast("单位ID错误", ContactsPersonActivity.this);
                } else if (TextUtils.isEmpty(id)) {
                    ToastUtil.showToast("用户ID错误",ContactsPersonActivity.this);
                } else if (TextUtils.isEmpty(token)) {
                    ToastUtil.showToast("TOKEN错误", ContactsPersonActivity.this);
                } else if (TextUtils.isEmpty(name)) {
                    ToastUtil.showToast("请输入搜索内容",ContactsPersonActivity.this);
                } else {
                    getSearchContactsName(token, company_id, id, name);
                }
                break;
        }
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

                    contactsList = contacts.getResult();

                    adapter = new ContactsAdapter(ContactsPersonActivity.this, contactsList);

                    lvContact.setAdapter(adapter);

                } else {

                    ToastUtil.showToast(contacts.getMsg(), ContactsPersonActivity.this);
                }

                lvContact.hideHeaderView();
            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }

    /**
     * 通讯录查询接口
     */
    private void getSearchContactsName(String token, String company_id, String id, String name) {

        RequestParams params = new RequestParams();

        params.addBodyParameter("company_id", company_id);
        params.addBodyParameter("id", id);
        params.addBodyParameter("name", name);
        params.addBodyParameter("api_token", token);

        HttpTool.getInstance(this).httpWithParams(Url.CONTACTS_LIST_SEARCH, params, new SMObjectCallBack<Contacts>() {

            @Override
            public void onSuccess(Contacts data) {

                if (data.getCode() == 200) {

                    contactsList.clear();
                    contactsList = data.getResult();
                    adapter = new ContactsAdapter(ContactsPersonActivity.this, contactsList);
                    lvContact.setAdapter(adapter);
                } else {
                    ToastUtil.showToast(data.getMsg(), ContactsPersonActivity.this);
                }
                lvContact.hideHeaderView();

            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }

}
