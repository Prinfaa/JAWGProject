package com.jinganweigu.RoadWayFire.Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.Adapters.ImageShowAdapter;
import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseActivity;
import com.jinganweigu.RoadWayFire.BeseClassUtils.MyApplication;
import com.jinganweigu.RoadWayFire.Entries.ImagePathBean;
import com.jinganweigu.RoadWayFire.Interfaces.Mycontants;
import com.jinganweigu.RoadWayFire.Interfaces.Url;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.Sptools;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.ToastUtil;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.HttpTool;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.SMObjectCallBack;
import com.jinganweigu.RoadWayFire.Views.CommonProgressDialog;
import com.lidroid.xutils.http.RequestParams;
import com.lzy.imagepicker.view.ViewPagerFixed;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowPictureActivity extends BaseActivity implements ImageShowAdapter.OnPicturePositionChangeListner {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.viewpager)
    ViewPagerFixed viewpager;
    List<ImagePathBean.ResultBean> items = new ArrayList<>();
    ImageShowAdapter adapter;
    String WarningNum, token;
    @BindView(R.id.tv_new_position)
    TextView tvNewPosition;
    private CommonProgressDialog dialog;

    @Override
    public void initViews() {

        setContentView(R.layout.activity_show_picture);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        topName.setText("图片");
        dialog = new CommonProgressDialog(this, "正在加载...");
        WarningNum = getIntent().getStringExtra("warningnum");

        token = Sptools.getString(this, Mycontants.API_TOKEN, "");

        if (TextUtils.isEmpty(token)) {

            ToastUtil.showToast("TOKEN错误", this);

        } else if (TextUtils.isEmpty(WarningNum)) {

            ToastUtil.showToast("WarningNum错误", this);

        } else {

            getData(token, WarningNum);

        }

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvents() {

    }


    private void getData(String token, String warningNum) {

        dialog.show();
        RequestParams params = new RequestParams();

        params.addBodyParameter("api_token", token);
        params.addBodyParameter("warning_num", warningNum);


        HttpTool.getInstance(this).httpWithParams(Url.GET_ALARM_EVENT_IMAGE, params, new SMObjectCallBack<ImagePathBean>() {

            @Override
            public void onSuccess(ImagePathBean imagePathBean) {

                dialog.dismiss();
                if (imagePathBean.getCode() == 200) {

                    items.clear();

                    items.addAll(imagePathBean.getResult());
                    adapter = new ImageShowAdapter(items, ShowPictureActivity.this);
                    viewpager.setAdapter(adapter);

                }


            }

            @Override
            public void onError(int error, String msg) {

                dialog.dismiss();

            }
        });


    }


    @OnClick(R.id.back_btn)
    public void onViewClicked() {

        finish();

    }

    @Override
    public void OnPicturePositionChange(int position) {


        Log.e("abc", "OnPicturePositionChange: ====>???"+position );

        tvNewPosition.setText((position+1)+"/"+items.size());



    }


}
