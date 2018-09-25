package com.jinganweigu.entrysystem.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.common.BaseActivity;
import com.jinganweigu.entrysystem.entry.BaseBean;
import com.jinganweigu.entrysystem.entry.OutHouseListBean;
import com.jinganweigu.entrysystem.entry.ReadyEnterHouseListBean;
import com.jinganweigu.entrysystem.utils.HttpCode;
import com.jinganweigu.entrysystem.utils.MyApplication;
import com.jinganweigu.entrysystem.utils.Mycontants;
import com.jinganweigu.entrysystem.utils.Sptools;
import com.jinganweigu.entrysystem.utils.ToastUtil;
import com.jinganweigu.entrysystem.utils.Url;
import com.jinganweigu.entrysystem.utils.http.HttpTool;
import com.jinganweigu.entrysystem.utils.http.SMObjectCallBack;
import com.jinganweigu.entrysystem.view.CommonProgressDialog;
import com.lidroid.xutils.http.RequestParams;
import com.listview.SwipeListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceOutExamineActivity extends BaseActivity {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.fl_title)
    FrameLayout flTitle;
    @BindView(R.id.listview)
    SwipeListView listview;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.btn_sure)
    Button btnSure;

    private CommonProgressDialog dialog;
    private String name;
    List<OutHouseListBean.ResultBean> list=new ArrayList<>();
    private MyAdapter adapter;


    @Override
    public void initViews() {
        setContentView(R.layout.activity_device_out_examine);
        ButterKnife.bind(this);

        MyApplication.getInstance().addActivity(this);
        topName.setText("设备出库");
        tvSave.setVisibility(View.GONE);
        name = Sptools.getString(this, Mycontants.REAL_NAME, "");
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary), true);
        dialog = new CommonProgressDialog(this, "正在加载....");

        GetOutHouseList(name);

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvents() {

    }


    @OnClick({R.id.back_btn, R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:

                intentMethod.rebackMethod(DeviceOutExamineActivity.this);

                break;
            case R.id.btn_sure:


                if (!TextUtils.isEmpty(name)) {

                    MakeSureOutHouse(name);

                }


                break;
        }
    }

    //出库列表接口
    private void GetOutHouseList(String name) {

        dialog.show();
        RequestParams params = new RequestParams();

        params.addBodyParameter("out_people", name);

        HttpTool.getInstance(this).httpWithParams(Url.DEVICE_OUT_HOUSE_LIST_URL, params, new SMObjectCallBack<OutHouseListBean>() {
            @Override
            public void onSuccess(OutHouseListBean outHouseListBean) {

                dialog.dismiss();

                list.clear();
                if (outHouseListBean.getCode() == HttpCode.REQUEST_SUCCESS) {

                    list = outHouseListBean.getResult();

                    tvNum.setText(""+list.size());

                    adapter = new MyAdapter();

                    listview.setAdapter(adapter);


                } else {

                    dialog.dismiss();
                    ToastUtil.showToast(outHouseListBean.getMsg(), DeviceOutExamineActivity.this);


                }


            }

            @Override
            public void onError(int error, String msg) {
                dialog.dismiss();
            }
        });


    }

    //确定出库
    private void MakeSureOutHouse(final String name) {


        dialog.show();

        RequestParams params = new RequestParams();

        params.addBodyParameter("out_people", name);

        HttpTool.getInstance(this).httpWithParams(Url.SURE_OUT_HOUSE_URL, params, new SMObjectCallBack<BaseBean>() {


            @Override
            public void onSuccess(BaseBean baseBean) {


                dialog.dismiss();

//                    if(baseBean.getCode()== HttpCode.REQUEST_SUCCESS){}

                ToastUtil.showToast(baseBean.getMsg(), DeviceOutExamineActivity.this);

//                adapter.notifyDataSetChanged();

//

                finish();



            }

            @Override
            public void onError(int error, String msg) {

                dialog.dismiss();

                GetOutHouseList(name);

            }
        });


    }


    class MyAdapter extends BaseAdapter {

//        List<OutHouseListBean.ResultBean> lists;
//
////        int deviceStatus = 0;// 0 是正常入库 1 是废品入库
//
//        public MyAdapter(List<OutHouseListBean.ResultBean> lists) {
//            this.lists = lists;
//        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(DeviceOutExamineActivity.this).inflate(R.layout.item_listview, parent, false);
                holder = new Holder(convertView, position);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            holder.tvID.setText(list.get(position).getChip_IMEI());
            holder.tvIDType.setText(list.get(position).getStat_id());
            holder.tvDeviceType.setText(list.get(position).getRtu_type());
            holder.tvScendEnter.setVisibility(View.GONE);
            return convertView;
        }

        class Holder {
            private TextView tvID, tvIDType, tvDeviceType, tvScendEnter;

            Holder(View main, final int position) {
                tvID = (TextView) main.findViewById(R.id.tv_device_id);//id号
                tvIDType = (TextView) main.findViewById(R.id.tv_id_type);//id类型
                tvDeviceType = (TextView) main.findViewById(R.id.tv_device_type);//设备类型
                tvScendEnter = (TextView) main.findViewById(R.id.tv_second_enter);//是否二次入库

                main.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete(position);
                    }
                });
            }
        }

        public void delete(int position) {


            String name = Sptools.getString(DeviceOutExamineActivity.this, Mycontants.REAL_NAME, "");

            String imei = list.get(position).getChip_IMEI();

            if (!TextUtils.isEmpty(name)) {

                OutHouseDelate(name, imei,position);


            } else {

                Log.e("loge", "delete: ==>没有领用人");

            }


        }

    }


    private void OutHouseDelate(String name, String imei, final int position) {

        RequestParams params = new RequestParams();

        params.addBodyParameter("out_people", name);
        params.addBodyParameter("chip_IMEI", imei);

        HttpTool.getInstance(this).httpWithParams(Url.DEVICE_OUT_OUT_LIST_DELATE, params, new SMObjectCallBack<BaseBean>() {

            @Override
            public void onSuccess(BaseBean baseBean) {

                ToastUtil.showToast(baseBean.getMsg(), DeviceOutExamineActivity.this);

                list.remove(position);

                adapter.notifyDataSetChanged();

                tvNum.setText(""+list.size());

            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }


}
