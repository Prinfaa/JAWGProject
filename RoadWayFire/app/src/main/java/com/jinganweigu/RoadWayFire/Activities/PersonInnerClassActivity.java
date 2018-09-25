package com.jinganweigu.RoadWayFire.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.Adapters.PersonInnerClassAdapter;
import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseActivity;
import com.jinganweigu.RoadWayFire.BeseClassUtils.MyApplication;
import com.jinganweigu.RoadWayFire.Entries.BaseModel;
import com.jinganweigu.RoadWayFire.Entries.InnerClassPerson;
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

public class PersonInnerClassActivity extends BaseActivity {

    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.tv_class_and_time)
    TextView tvClassAndTime;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.et_phone_num)
    EditText etPhoneNum;
    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.gv_name)
    GridView gvName;
    private List<InnerClassPerson.ResultBean> personList = new ArrayList<>();
    String cla_stoptime, cla_starttime, token, company_id,classes_id,class_name;
    PersonInnerClassAdapter adapter;
    public final static int REQUESTCODE = 1; // 返回的结果码

    @Override
    public void initViews() {

        setContentView(R.layout.activity_person_inner_class);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        cla_starttime = getIntent().getStringExtra("cla_starttime");
        cla_stoptime = getIntent().getStringExtra("cla_stoptime");
        classes_id = getIntent().getStringExtra("classes_id");
        class_name = getIntent().getStringExtra("class_name");

        token = Sptools.getString(this, Mycontants.API_TOKEN, "");
        company_id = Sptools.getString(this, Mycontants.COMPANY_ID, "");

    }

    @Override
    public void initData() {

        if (TextUtils.isEmpty(token)) {

            ToastUtil.showToast("TOKEN错误", this);

        } else if (TextUtils.isEmpty(company_id)) {
            ToastUtil.showToast("COMPANDID错误", this);
        } else if (TextUtils.isEmpty(cla_starttime)) {

            ToastUtil.showToast("开始时间为空", this);

        } else if (TextUtils.isEmpty(cla_stoptime)) {

            ToastUtil.showToast("结束时间为空", this);

        } else if (TextUtils.isEmpty(classes_id)) {
            ToastUtil.showToast("CLASSESID为空", this);

        } else if (TextUtils.isEmpty(class_name)) {

            ToastUtil.showToast("CLASSESID为空", this);

        } else {

            tvClassAndTime.setText(class_name+"  "+cla_starttime+"--"+cla_stoptime);
            getPersonData(token, company_id, cla_starttime, cla_stoptime);



        }


    }

    @Override
    public void initEvents() {

        gvName.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                BaseUtils.showAlertDialog2(PersonInnerClassActivity.this, "删除成员", "确定删除吗？", new BaseUtils.OnConfirm() {
                    @Override
                    public void onConfirm() {

                        delatePersonData(company_id,token,personList.get(position).getName(),position);
                    }
                });




                return false;
            }
        });


    }


    @OnClick({R.id.back_btn, R.id.btn_add, R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:

                finish();

                break;
            case R.id.btn_add:

                Intent intent = new Intent(PersonInnerClassActivity.this,
                        ContactsPersonActivity.class);
                startActivityForResult(intent, REQUESTCODE);

                break;
            case R.id.btn_sure:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==ContactsPersonActivity.GET_RESULT_CODE&&requestCode==REQUESTCODE){

            String phone=data.getStringExtra("phone");


            if(!TextUtils.isEmpty(phone)){

             insertPersonData(token,company_id,phone,cla_starttime,cla_stoptime,classes_id);

            }
        }





    }
    //获取班次中人员
    private void getPersonData(String token, String company_id, String cla_starttime, String cla_stoptime) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("api_token", token);
        params.addBodyParameter("company_id", company_id);
        params.addBodyParameter("cla_starttime", cla_starttime);
        params.addBodyParameter("cla_stoptime", cla_stoptime);


        HttpTool.getInstance(this).httpWithParams(Url.GET_CLASS_INNER_PERSON, params, new SMObjectCallBack<InnerClassPerson>() {

            @Override
            public void onSuccess(InnerClassPerson innerClassPerson) {

                if (innerClassPerson.getCode() == 200) {

                    personList = innerClassPerson.getResult();
                    adapter = new PersonInnerClassAdapter(personList, PersonInnerClassActivity.this);
                    gvName.setAdapter(adapter);

                } else {

                    ToastUtil.showToast(innerClassPerson.getMsg(), PersonInnerClassActivity.this);

                }

            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }
    //为班次中添加人员
    private void insertPersonData(String token,String company_id,String phone,String cla_starttime,String cla_stoptime,String classes_id){

        RequestParams params=new RequestParams();

        params.addBodyParameter("api_token",token);
        params.addBodyParameter("company_id",company_id);
        params.addBodyParameter("phone",phone);
        params.addBodyParameter("cla_starttime",cla_starttime);
        params.addBodyParameter("cla_stoptime",cla_stoptime);
        params.addBodyParameter("classes_id", classes_id);


        Log.e("abc", "insertPersonData: =api_token==>"+token+"==company_id=>"+company_id+"==phone==>"+phone+"==cla_starttime==>"+cla_starttime +"==cla_stoptime==>"+cla_stoptime+"==classes_id==>"+classes_id);

        HttpTool.getInstance(this).httpWithParams(Url.ADD_USER_IN_CLA, params, new SMObjectCallBack<BaseModel>() {

            @Override
            public void onSuccess(BaseModel baseModel) {

                if(baseModel.getCode()==200){

                    getPersonData(token,company_id,cla_starttime,cla_stoptime);

                }else{

                    ToastUtil.showToast(baseModel.getMsg(),PersonInnerClassActivity.this);

                }


            }

            @Override
            public void onError(int error, String msg) {

            }
        });





    }

    //删除班次中的人员
    private void delatePersonData(String company_id,String token,String name,int position){

        RequestParams params=new RequestParams();
        params.addBodyParameter("company_id",company_id);
        params.addBodyParameter("name",name);
        params.addBodyParameter("api_token",token);

        HttpTool.getInstance(this).httpWithParams(Url.DELATE_USER_IN_CLA, params, new SMObjectCallBack<BaseModel>() {

            @Override
            public void onSuccess(BaseModel baseModel) {

                if(baseModel.getCode()==200){

                    personList.remove(position);

                    adapter.notifyDataSetChanged();

                }else{


                    ToastUtil.showToast(baseModel.getMsg(),PersonInnerClassActivity.this);

                }




            }

            @Override
            public void onError(int error, String msg) {

            }
        });





    }

}
