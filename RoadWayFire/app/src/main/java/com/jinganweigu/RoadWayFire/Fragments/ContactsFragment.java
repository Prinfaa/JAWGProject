package com.jinganweigu.RoadWayFire.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.Activities.ContactPersonInformationActivity;
import com.jinganweigu.RoadWayFire.Adapters.ContactsAdapter;
import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseFragment2;
import com.jinganweigu.RoadWayFire.Entries.Contacts;
import com.jinganweigu.RoadWayFire.Interfaces.Mycontants;
import com.jinganweigu.RoadWayFire.Interfaces.OnRefreshListener;
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
import butterknife.Unbinder;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ContactsFragment extends BaseFragment2 {

    Unbinder unbinder;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.et_search_contant)
    EditText etSearchContant;
    @BindView(R.id.ll_content_search)
    LinearLayout llContentSearch;
    @BindView(R.id.lv_contact)
    RefreshListView lvContact;
    @BindView(R.id.top_name)
    TextView topName;
    private ContactsAdapter adapter;

    private List<Contacts.ResultBean> contactsList = new ArrayList<>();
    private String company_id;
    private String id;
    private String token;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        unbinder = ButterKnife.bind(this, view);
        topName.setText("通讯录");
        backBtn.setVisibility(View.GONE);
        etSearchContant.setCursorVisible(false);
        etSearchContant.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                etSearchContant.setCursorVisible(true);
                return false;
            }
        });


        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llContentSearch.getVisibility() == View.GONE) {

                    llContentSearch.setVisibility(View.VISIBLE);

                } else {

                    llContentSearch.setVisibility(View.GONE);

                }


            }
        });

        initEdit();


        return view;

    }


    /**
     * 回车键改为搜索
     */
    private void initEdit() {

        etSearchContant.setOnKeyListener(new View.OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 先隐藏键盘
                    ((InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getActivity().getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    String searchContext = etSearchContant.getText().toString().trim();
                    if (TextUtils.isEmpty(searchContext)) {

                        ToastUtil.showToast("输入框为空，请输入", getActivity());

                    } else {
                        //调用搜索方法
                        if (TextUtils.isEmpty(company_id)) {
                            ToastUtil.showToast("单位ID错误", getActivity());
                        } else if (TextUtils.isEmpty(id)) {
                            ToastUtil.showToast("用户ID错误", getActivity());
                        } else if (TextUtils.isEmpty(token)) {
                            ToastUtil.showToast("TOKEN错误", getActivity());
                        } else if (TextUtils.isEmpty(searchContext)) {
                            ToastUtil.showToast("请输入搜索内容", getActivity());
                        } else {
                            getSearchContactsName(token, company_id, id, searchContext);
                        }
                    }
                }
                return false;
            }
        });
    }


    @Override
    public void initData() {
        company_id = Sptools.getString(getActivity(), Mycontants.COMPANY_ID, "");
        id = Sptools.getString(getActivity(), Mycontants.USER_ID, "");
        token = Sptools.getString(getActivity(), Mycontants.API_TOKEN, "");


        if (TextUtils.isEmpty(company_id)) {
            ToastUtil.showToast("单位ID错误", getActivity());
        } else if (TextUtils.isEmpty(id)) {
            ToastUtil.showToast("用户ID错误", getActivity());
        } else if (TextUtils.isEmpty(token)) {
            ToastUtil.showToast("TOKEN错误", getActivity());
        } else {
            getdata(token, company_id, id);

        }

        lvContact.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void downPullRefresh() {

                String name = etSearchContant.getText().toString().trim();
                if (TextUtils.isEmpty(company_id)) {
                    ToastUtil.showToast("单位ID错误", getActivity());
                } else if (TextUtils.isEmpty(id)) {
                    ToastUtil.showToast("用户ID错误", getActivity());
                } else if (TextUtils.isEmpty(token)) {
                    ToastUtil.showToast("TOKEN错误", getActivity());
                } else if (TextUtils.isEmpty(name)) {
                    getdata(token, company_id, id);
                } else {
                    getSearchContactsName(token, company_id, id, name);
                }
            }
        });


        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                intentMethod.startMethodWith2String(getActivity(), ContactPersonInformationActivity.class, "name", contactsList.get(position - 1).getName(), "phone", contactsList.get(position - 1).getPhone());

            }
        });
    }


    private void getdata(String token, String company_id, String id) {

        RequestParams params = new RequestParams();

        params.addBodyParameter("company_id", company_id);
        params.addBodyParameter("api_token", token);
        params.addBodyParameter("user_id", id);

        HttpTool.getInstance(getActivity()).httpWithParams(Url.GET_CONTACTS_LIST, params, new SMObjectCallBack<Contacts>() {

            @Override
            public void onSuccess(Contacts contacts) {

                if (contacts.getCode() == 200) {

                    contactsList = contacts.getResult();

                    adapter = new ContactsAdapter(getActivity(), contactsList);

                    lvContact.setAdapter(adapter);

                } else {

                    ToastUtil.showToast(contacts.getMsg(), getActivity());
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


        HttpTool.getInstance(getActivity()).httpWithParams(Url.CONTACTS_LIST_SEARCH, params, new SMObjectCallBack<Contacts>() {

            @Override
            public void onSuccess(Contacts data) {


                if (data.getCode() == 200) {

                    contactsList.clear();
                    contactsList = data.getResult();
                    adapter = new ContactsAdapter(getActivity(), contactsList);
                    lvContact.setAdapter(adapter);
                } else {
                    ToastUtil.showToast(data.getMsg(), getActivity());
                }
                lvContact.hideHeaderView();

            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
