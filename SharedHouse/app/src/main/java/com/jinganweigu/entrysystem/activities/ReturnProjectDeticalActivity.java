package com.jinganweigu.entrysystem.activities;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.adapter.ProjectDeviceDeticalAdapter;
import com.jinganweigu.entrysystem.common.BaseActivity;
import com.jinganweigu.entrysystem.entry.ReturnProjectDetailBean;
import com.jinganweigu.entrysystem.utils.HttpCode;
import com.jinganweigu.entrysystem.utils.Url;
import com.jinganweigu.entrysystem.utils.http.HttpTool;
import com.jinganweigu.entrysystem.utils.http.SMObjectCallBack;
import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReturnProjectDeticalActivity extends BaseActivity {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.fl_title)
    FrameLayout flTitle;
    @BindView(R.id.tv_project_name)
    TextView tvProjectName;
    @BindView(R.id.tv_return_all)
    TextView tvReturnAll;
    @BindView(R.id.lv_good_device)
    ListView lvGoodDevice;
    @BindView(R.id.tv_good_return_all)
    TextView tvGoodReturnAll;
    @BindView(R.id.lv_bad_device)
    ListView lvBadDevice;
    @BindView(R.id.tv_bad_return_all)
    TextView tvBadReturnAll;

    private String project;

    private List<ReturnProjectDetailBean.ResultBean.NiceBean> niceBeanList = new ArrayList<>();
    private List<ReturnProjectDetailBean.ResultBean.BadBean> badBeanList = new ArrayList<>();

    private ProjectDeviceDeticalAdapter nadapter;
    private ProjectDeviceDeticalAdapter badapter;


    @Override
    public void initViews() {
        setContentView(R.layout.activity_return_project_detical);
        ButterKnife.bind(this);
        tvSave.setVisibility(View.GONE);
        topName.setText("退货统计");
        project = getIntent().getStringExtra("project");
//        Log.e("loge", "initViews: ===>>>" + project);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary), true);
        tvProjectName.setText(project);

        if (!TextUtils.isEmpty(project)) {

            getData(project);

        } else {

            Log.e("loge", "initViews: ===>参数没有传过来");

        }


    }

    @Override
    public void initData() {


    }

    @Override
    public void initEvents() {

    }


    @OnClick(R.id.back_btn)
    public void onViewClicked() {

        intentMethod.rebackMethod(this);


    }

    private void getData(String project) {

        RequestParams params = new RequestParams();

        params.addBodyParameter("receive_unit", project);

        HttpTool.getInstance(this).httpWithParams(Url.GET_STATISTICS_RETURN_BACK_DATA_DETAIL, params, new SMObjectCallBack<ReturnProjectDetailBean>() {

            @Override
            public void onSuccess(ReturnProjectDetailBean returnProjectDetailBean) {

                if (returnProjectDetailBean.getCode() == HttpCode.REQUEST_SUCCESS) {

                    if (returnProjectDetailBean.getResult().getNice() != null) {

                        niceBeanList = returnProjectDetailBean.getResult().getNice();
                    }
                    if (returnProjectDetailBean.getResult().getBad() != null) {

                        badBeanList = returnProjectDetailBean.getResult().getBad();
                    }

                    int DeviceAll = niceBeanList.size() + badBeanList.size();
//                    Log.e("loge", "onSuccess: ==niceBeanList==>"+niceBeanList.size()+" ==badBeanList==>"+badBeanList.size() );
                    tvReturnAll.setText("退货总计：" + DeviceAll);

                    tvGoodReturnAll.setText("总计：" + niceBeanList.size());

                    tvBadReturnAll.setText("总计：" + badBeanList.size());

                    nadapter = new ProjectDeviceDeticalAdapter(ReturnProjectDeticalActivity.this, niceBeanList);

                    lvGoodDevice.setAdapter(nadapter);

                    badapter = new ProjectDeviceDeticalAdapter(ReturnProjectDeticalActivity.this, badBeanList, 1);

                    lvBadDevice.setAdapter(badapter);
                }


            }

            @Override
            public void onError(int error, String msg) {


            }
        });


    }


}
