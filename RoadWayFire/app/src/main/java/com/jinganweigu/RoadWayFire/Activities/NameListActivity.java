package com.jinganweigu.RoadWayFire.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.Adapters.NameGridViewCheckAdapter;
import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseActivity;
import com.jinganweigu.RoadWayFire.BeseClassUtils.MyApplication;
import com.jinganweigu.RoadWayFire.Entries.Contacts;
import com.jinganweigu.RoadWayFire.Entries.MessageBackValue;
import com.jinganweigu.RoadWayFire.Interfaces.Mycontants;
import com.jinganweigu.RoadWayFire.Interfaces.Url;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.Sptools;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.ToastUtil;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.HttpTool;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.SMObjectCallBack;
import com.lidroid.xutils.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NameListActivity extends BaseActivity {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.et_search_contant)
    EditText etSearchContant;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.gv_name)
    GridView gvName;
    private List<Contacts.ResultBean> contactsList = new ArrayList<>();
    private NameGridViewCheckAdapter adapter;
    private String warningNum;
    List<Contacts.ResultBean> chooseContact = new ArrayList<>();

    List<String> cidList = new ArrayList<>();
    List<String> userIdList = new ArrayList<>();
    private String company_id;
    private String id;
    private String token;


    @Override
    public void initViews() {

        setContentView(R.layout.activity_name_list);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        topName.setText("选择");
        tvSave.setVisibility(View.VISIBLE);
        tvSave.setText("转发");
        warningNum = getIntent().getStringExtra("wariningNum");
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

        gvName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                NameGridViewCheckAdapter.ViewHolder holder = (NameGridViewCheckAdapter.ViewHolder) arg1.getTag();
                holder.cb.toggle();
                NameGridViewCheckAdapter.getIsSelected().put(arg2, holder.cb.isChecked());

            }
        });

    }


    @OnClick({R.id.back_btn, R.id.tv_save, R.id.ll_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.tv_save:
                submit();
                break;
            case R.id.ll_search:

                String name = etSearchContant.getText().toString().trim();
                if (TextUtils.isEmpty(company_id)) {
                    ToastUtil.showToast("单位ID错误", this);
                } else if (TextUtils.isEmpty(id)) {
                    ToastUtil.showToast("用户ID错误", this);
                } else if (TextUtils.isEmpty(token)) {
                    ToastUtil.showToast("TOKEN错误", this);
                } else if (TextUtils.isEmpty(name)) {
                    ToastUtil.showToast("请输入搜索内容", this);
                } else {
                    getSearchContactsName(token, company_id, id, name);
                }


                break;
        }
    }


    /**
     * 提交
     */
    private void submit() {
        chooseContact.clear();
        cidList.clear();
        userIdList.clear();
        for (int i = 0; i < adapter.getIsSelected().size(); i++) {
            if (adapter.getIsSelected().get(i)) {
                chooseContact.add(contactsList.get(i));
                cidList.add(contactsList.get(i).getCid());
                userIdList.add(contactsList.get(i).getUser_id());
            }
        }

        if (chooseContact.size() == 0) {

            ToastUtil.showToast("请选择接收人", NameListActivity.this);

        } else {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < chooseContact.size(); i++) {

                sb.append(chooseContact.get(i).getName() + "  ");

            }

            String token = Sptools.getString(this, Mycontants.API_TOKEN, "");
            String UserId = Sptools.getString(this, Mycontants.USER_ID, "");


            if (cidList.size() == 0) {
                ToastUtil.showToast("CID为空", this);
            } else if (userIdList.size() == 0) {
                ToastUtil.showToast("USERID列表为空", this);
            } else if (TextUtils.isEmpty(warningNum)) {
                ToastUtil.showToast("warningnum为空", this);
            } else if (TextUtils.isEmpty(UserId)) {
                ToastUtil.showToast("UserId为空", this);
            } else if (TextUtils.isEmpty(token)) {
                ToastUtil.showToast("TOKEN为空", this);
            } else {

                showContactsAlertDialog(NameListActivity.this, sb.toString(), cidList, userIdList, warningNum, UserId, token);

            }
        }
    }


    /**
     * 发送短信名单确认dialog
     */

    public void showContactsAlertDialog(Context context, String message, List<String> cidList, List<String> userIdList, String warningNum, String userId, String Token) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.dialogNoBg);
        View view = View
                .inflate(context, R.layout.dialog_choose_contacts_view, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView tvName = (TextView) view
                .findViewById(R.id.tv_name_list);//姓名列表
        EditText leaveMessage = (EditText) view
                .findViewById(R.id.et_leave_message);//留言
        Button comfirm = (Button) view
                .findViewById(R.id.btn_send);//发送
        Button channel = (Button) view
                .findViewById(R.id.btn_channel);//取消
        tvName.setText(message);
        //取消或确定按钮监听事件处理
        final AlertDialog dialog = builder.create();


        dialog.setCanceledOnTouchOutside(false);//取消点击外界消失
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                HttpTool.isDeleteUser = true;
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.show();
//        dialog.getWindow().setLayout(view.getWidth(), view.getHeight());
        channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = leaveMessage.getText().toString().trim();

                if (!TextUtils.isEmpty(message)) {

                    sendMessage(context, cidList, userIdList, Token, message, warningNum, userId);

                    dialog.dismiss();
                } else {

                    ToastUtil.showToast("请输入留言", context);

                }


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

                    contactsList = contacts.getResult();

                    adapter = new NameGridViewCheckAdapter((ArrayList<Contacts.ResultBean>) contactsList, NameListActivity.this);

                    gvName.setAdapter(adapter);


                } else {

                    ToastUtil.showToast(contacts.getMsg(), NameListActivity.this);

                }


            }

            @Override
            public void onError(int error, String msg) {

            }
        });

    }

    private void sendMessage(Context context, List<String> cidList, List<String> userIdList, String token, String leave_message, String warning_num, String sendUser) {

        JSONArray cidArray = new JSONArray();
        for (int i = 0; i < cidList.size(); i++) {

            try {
                cidArray.put(i, cidList.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        JSONArray userIdArray = new JSONArray();
        for (int i = 0; i < userIdList.size(); i++) {

            try {
                userIdArray.put(i, userIdList.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }



        RequestParams params = new RequestParams();
        params.addBodyParameter("api_token", token);
        params.addBodyParameter("cid", cidArray.toString());
        params.addBodyParameter("rece_user", userIdArray.toString());
        params.addBodyParameter("leave_msg", leave_message);
        params.addBodyParameter("warning_num", warning_num);
        params.addBodyParameter("send_user", sendUser);

        HttpTool.getInstance(context).httpWithParams(Url.SEND_MESSAGE_BACK_VALUE, params, new SMObjectCallBack<MessageBackValue>() {

            @Override
            public void onSuccess(MessageBackValue messageBackValue) {


                if (messageBackValue.getCode() == 200) {
                }

                if (messageBackValue.getResult().getState().toLowerCase().equals("ok")) {

                    ToastUtil.showToast("转发成功", context);

                    finish();
                }
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

                    adapter = new NameGridViewCheckAdapter((ArrayList<Contacts.ResultBean>) contactsList, NameListActivity.this);

                    gvName.setAdapter(adapter);

                } else {


                    ToastUtil.showToast(data.getMsg(), NameListActivity.this);

                }


            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }
}