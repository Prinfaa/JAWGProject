package com.jinganweigu.RoadWayFire.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.Activities.AboutUsActivity;
import com.jinganweigu.RoadWayFire.Activities.ChangePasswordActivity;
import com.jinganweigu.RoadWayFire.Activities.GeneralStatisticActivity;
import com.jinganweigu.RoadWayFire.Activities.ManagerActivity;
import com.jinganweigu.RoadWayFire.Activities.PersonInformationActivity;
import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseFragment2;
import com.jinganweigu.RoadWayFire.Entries.PersonInformatication;
import com.jinganweigu.RoadWayFire.Interfaces.Mycontants;
import com.jinganweigu.RoadWayFire.Interfaces.Url;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.Sptools;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.ToastUtil;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.HttpTool;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.SMObjectCallBack;
import com.jinganweigu.RoadWayFire.ToolsUtils.ImageUtil.LoadImageUtil;
import com.lidroid.xutils.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PersonFragment extends BaseFragment2 {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_nike_name)
    TextView tvNikeName;
    @BindView(R.id.tv_ture_name)
    TextView tvTureName;
    @BindView(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @BindView(R.id.tv_work_time)
    TextView tvWorkTime;
    @BindView(R.id.ll_person_info)
    LinearLayout llPersonInfo;
    @BindView(R.id.ll_statistic)
    LinearLayout llStatistic;
    @BindView(R.id.ll_person_manager)
    LinearLayout llPersonManager;
    @BindView(R.id.ll_about_us)
    LinearLayout llAboutUs;
    @BindView(R.id.ll_change_password)
    LinearLayout llChangePassword;
    Unbinder unbinder;
    String imagePath,name,phoneNum;
    private String token;
    private String rece_user;


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.fragment_person, container, false);
        unbinder = ButterKnife.bind(this, view);
        topName.setText("个人中心");
        backBtn.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void initData() {

        token = Sptools.getString(getActivity(), Mycontants.API_TOKEN, "");
        rece_user = Sptools.getString(getActivity(), Mycontants.USER_ID, "");
        if (TextUtils.isEmpty(token)) {
            ToastUtil.showToast("TOKEN错误", getActivity());
        } else if (TextUtils.isEmpty(rece_user)) {
            ToastUtil.showToast("用户ID错误", getActivity());
        } else {
            getInformation(token, rece_user);
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();

        }else{  // 在最前端显示 相当于调用了onResume();

            if (TextUtils.isEmpty(token)) {
                ToastUtil.showToast("TOKEN错误", getActivity());
            } else if (TextUtils.isEmpty(rece_user)) {
                ToastUtil.showToast("用户ID错误", getActivity());
            } else {
                getInformation(token, rece_user);
            }


        }
    }


    private void getInformation(String token, String id) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("api_token", token);
        params.addBodyParameter("user_id", id);
        HttpTool.getInstance(getActivity()).httpWithParams(Url.GET_PERSON_INFORMATION, params, new SMObjectCallBack<PersonInformatication>() {
            @Override
            public void onSuccess(PersonInformatication personInformatication) {

                if (personInformatication.getCode() == 200) {

                    tvNikeName.setText(personInformatication.getResult().getName());
                    name=personInformatication.getResult().getName();
                    tvPhoneNum.setText(personInformatication.getResult().getPhone());
                    phoneNum=personInformatication.getResult().getPhone();
                    LoadImageUtil.ShowRoundCornerImg(getActivity(), personInformatication.getResult().getUser_head(), ivPhoto);
                    imagePath=personInformatication.getResult().getUser_head();
                    tvTureName.setText(personInformatication.getResult().getName());
                    tvWorkTime.setText("工作时间  "+personInformatication.getResult().getCla_starttime()+"--"+personInformatication.getResult().getCla_stoptime());

                } else {

                    ToastUtil.showToast(personInformatication.getMsg(), getActivity());

                }

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

    @OnClick({R.id.ll_person_info, R.id.ll_statistic, R.id.ll_person_manager, R.id.ll_about_us, R.id.ll_change_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_person_info:

                Intent intent=new Intent(getActivity(),PersonInformationActivity.class);

                if(!TextUtils.isEmpty(name)){
                    intent.putExtra("name",name);
                }
                if(!TextUtils.isEmpty(phoneNum)){
                    intent.putExtra("phoneNum",phoneNum);
                }
                if(!TextUtils.isEmpty(imagePath)){
                    intent.putExtra("imagePath",imagePath);
                }
                getActivity().startActivity(intent);

//                intentMethod.startMethodTwo(getActivity(), PersonInformationActivity.class);

                break;

            case R.id.ll_statistic:

                intentMethod.startMethodTwo(getActivity(), GeneralStatisticActivity.class);

                break;

            case R.id.ll_person_manager:

                intentMethod.startMethodTwo(getActivity(), ManagerActivity.class);

                break;

            case R.id.ll_about_us:

                intentMethod.startMethodTwo(getActivity(), AboutUsActivity.class);

                break;

            case R.id.ll_change_password:


                intentMethod.startMethodTwo(getActivity(), ChangePasswordActivity.class);

                break;

        }

    }


}
