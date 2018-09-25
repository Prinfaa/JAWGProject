package com.jinganweigu.entrysystem.activities;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.common.BaseActivity;
import com.jinganweigu.entrysystem.entry.BaseBean;
import com.jinganweigu.entrysystem.entry.ReadyEnterHouseListBean;
import com.jinganweigu.entrysystem.entry.WasteDeviceEnterListBean;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceEnterExamineActivity extends BaseActivity {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.fl_title)
    FrameLayout flTitle;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.btn_sure)
    Button btnSure;
    private List<ReadyEnterHouseListBean.ResultBean> listDevice = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 2000:


                    break;

                default:

                    break;


            }


        }
    };

    private int from;
    private String deviceType;

    private String enterType;

    private MyAdapter adapter;

    SwipeListView content;

    private CommonProgressDialog dialog;

    @Override
    public void initViews() {

        setContentView(R.layout.activity_device_enter);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        topName.setText("设备入库");
        tvSave.setVisibility(View.GONE);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary), true);
        dialog = new CommonProgressDialog(this, "正在加载....");

        from = getIntent().getIntExtra("from", 0);

        deviceType = getIntent().getStringExtra("deviceType");

//        tvNum.setText("100000");


        if (from == 0) {

            topName.setText(deviceType);

        }

        if (deviceType.equals("成品入库") || deviceType.equals("新品入库")) {
            GoodDeviceGetOutHouseList();
        } else if (deviceType.equals("废品入库")) {

            WasteDeviceGetOutHouseList();

        }


    }

    @Override
    public void initData() {
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setColorSchemeColors(Color.RED);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        content = (SwipeListView) findViewById(R.id.listview);


    }

    @Override
    public void initEvents() {

    }


    @OnClick({R.id.back_btn, R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:

                intentMethod.rebackMethod(this);


                break;
            case R.id.btn_sure:

                String name = Sptools.getString(this, Mycontants.REAL_NAME, "");

                if (!TextUtils.isEmpty(name)) {

                    if (deviceType.equals("成品入库") || deviceType.equals("新品入库")) {

                        MakeSureGoodDeviceIntoHouse(name);
                    } else if (deviceType.equals("废品入库")) {

                        MakeSureBadDeviceIntoHouse(name);

                    }

                } else {

                    ToastUtil.showToast("用户名错误", DeviceEnterExamineActivity.this);

                }


                break;
        }
    }


    class MyAdapter extends BaseAdapter {

        List<ReadyEnterHouseListBean.ResultBean> lists = new ArrayList<>();

        int deviceStatus = 0;// 0 是正常入库 1 是废品入库

        Handler handler;

        public MyAdapter(List<ReadyEnterHouseListBean.ResultBean> lists, int deviceStatus, Handler handler) {
            this.lists = lists;
            this.deviceStatus = deviceStatus;
            this.handler = handler;
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(DeviceEnterExamineActivity.this).inflate(R.layout.item_listview, parent, false);
                holder = new Holder(convertView, position);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            holder.tvID.setText(lists.get(position).getChip_IMEI());
            holder.tvIDType.setText(lists.get(position).getStat_id());
            holder.tvDeviceType.setText(lists.get(position).getRtu_type());

            if (deviceStatus != 1) {

                int a = Integer.valueOf(lists.get(position).getReturn_num());

                if (a > 0) {
                    holder.tvScendEnter.setVisibility(View.VISIBLE);
                } else {

                    holder.tvScendEnter.setVisibility(View.GONE);

                }


            }


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


            String name = Sptools.getString(DeviceEnterExamineActivity.this, Mycontants.REAL_NAME, "");

            if (!TextUtils.isEmpty(name) && deviceStatus == 0) {//正常预入库删除


                GooddelateOneEntry(name, lists.get(position).getChip_IMEI(), position);

                handler.sendEmptyMessage(2000);
            } else if (!TextUtils.isEmpty(name) && deviceStatus == 1) {//废品入库删除


                WastedelateOneEntry(name, lists.get(position).getChip_IMEI(), position);
                handler.sendEmptyMessage(2000);

            }

        }

    }


    //成品入库删除接口
    private void GooddelateOneEntry(String name, String DeviceId, final int position) {


        final RequestParams params = new RequestParams();

        params.addBodyParameter("in_mitu_peo", name);
        params.addBodyParameter("chip_IMEI", DeviceId);
        HttpTool.getInstance(DeviceEnterExamineActivity.this).httpWithParams(Url.GOOD_ENTER_HOUSE_DELAGE_ONE_ENTRY, params, new SMObjectCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean baseBean) {

                if (baseBean.getCode() == HttpCode.REQUEST_SUCCESS) {

                    listDevice.remove(position);

//                    tvNum.setText(listDevice.size());
                    tvNum.setText(listDevice.size() + "");
//                    handler.sendEmptyMessage(2222);

                    adapter.notifyDataSetChanged();

                    ToastUtil.showToast(baseBean.getMsg(), DeviceEnterExamineActivity.this);

                } else {

                    ToastUtil.showToast(baseBean.getMsg(), DeviceEnterExamineActivity.this);

                }


            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }

    //废品入库删除接口
    private void WastedelateOneEntry(String name, String DeviceId, final int position) {


        final RequestParams params = new RequestParams();
        params.addBodyParameter("waste_peo", name);
        params.addBodyParameter("chip_IMEI", DeviceId);
        HttpTool.getInstance(DeviceEnterExamineActivity.this).httpWithParams(Url.WASTE_ENTER_HOUSE_DELAGE_ONE_ENTRY, params, new SMObjectCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean baseBean) {

                if (baseBean.getCode() == HttpCode.REQUEST_SUCCESS) {

                    listDevice.remove(position);

//                    tvNum.setText(listDevice.size());
                    tvNum.setText(listDevice.size() + "");
//                    handler.sendEmptyMessage(2222);
                    adapter.notifyDataSetChanged();

                    ToastUtil.showToast(baseBean.getMsg(), DeviceEnterExamineActivity.this);

                } else {

                    ToastUtil.showToast(baseBean.getMsg(), DeviceEnterExamineActivity.this);

                }


            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }

    //成品入库列表接口
    private void GoodDeviceGetOutHouseList() {
        dialog.show();
        String name = Sptools.getString(this, Mycontants.REAL_NAME, "");

        if (!TextUtils.isEmpty(name)) {


            RequestParams params = new RequestParams();
            params.addBodyParameter("in_mitu_peo", name);
            HttpTool.getInstance(this).httpWithParams(Url.GOOD_ENTER_HOUSE_EXAMINE_LIST, params, new SMObjectCallBack<ReadyEnterHouseListBean>() {

                @Override
                public void onSuccess(ReadyEnterHouseListBean Bean) {

                    dialog.dismiss();

                    Log.e("codecode", "onSuccess: ===>" + Bean.getCode());

                    if (Bean.getCode() == HttpCode.REQUEST_SUCCESS) {//成功

                        listDevice.clear();
                        listDevice = Bean.getResult();
                        tvNum.setText(listDevice.size() + "");
                        adapter = new MyAdapter(listDevice, 0, handler);
                        content.setAdapter(adapter);


                    } else if (Bean.getCode() == HttpCode.PLAN_INTO_HOUSE_EXAMINE_NULL) {// 预入库审核为空

                        ToastUtil.showToast(Bean.getMsg(), DeviceEnterExamineActivity.this);

                    }

                }

                @Override
                public void onError(int error, String msg) {

                    dialog.dismiss();

                }
            });

        }


    }

    //废品入库列表接口

    private void WasteDeviceGetOutHouseList() {

        dialog.show();

        String name = Sptools.getString(this, Mycontants.REAL_NAME, "");

        if (!TextUtils.isEmpty(name)) {


            RequestParams params = new RequestParams();
            params.addBodyParameter("waste_peo", name);

            HttpTool.getInstance(this).httpWithParams(Url.WASTE_ENTER_HOUSE_EXAMINE_LIST, params, new SMObjectCallBack<WasteDeviceEnterListBean>() {

                @Override
                public void onSuccess(WasteDeviceEnterListBean Bean) {

                    dialog.dismiss();

                    if (Bean.getCode() == HttpCode.REQUEST_SUCCESS) {//成功

                        listDevice.clear();
//                        listDevice = Bean.getResult();
                        for (int i = 0; i < Bean.getResult().size(); i++) {

                            ReadyEnterHouseListBean.ResultBean resultBean = new ReadyEnterHouseListBean.ResultBean();

                            resultBean.setChip_IMEI(Bean.getResult().get(i).getChip_IMEI());
                            resultBean.setRtu_type(Bean.getResult().get(i).getRtu_type());
                            resultBean.setStat_id(Bean.getResult().get(i).getStat_id());
                            listDevice.add(resultBean);

                        }
                        tvNum.setText(listDevice.size() + "");
                        adapter = new MyAdapter(listDevice, 1, handler);
                        content.setAdapter(adapter);


                    } else if (Bean.getCode() == HttpCode.PLAN_INTO_HOUSE_EXAMINE_NULL) {// 预入库审核为空

                        ToastUtil.showToast(Bean.getMsg(), DeviceEnterExamineActivity.this);

                    }

                }

                @Override
                public void onError(int error, String msg) {

                    dialog.dismiss();

                }
            });

        }


    }

    //确定成品入库接口
    private void MakeSureGoodDeviceIntoHouse(String name) {

        JSONArray jsonArray = new JSONArray();


        for (int i = 0; i < listDevice.size(); i++) {

            try {
                jsonArray.put(i, listDevice.get(i).getChip_IMEI());
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        dialog.show();
        RequestParams params = new RequestParams();
        params.addBodyParameter("in_mitu_peo", name);
        params.addBodyParameter("rtu_id", jsonArray.toString());
        HttpTool.getInstance(this).httpWithParams(Url.MAKE_SURE_READY_DEVICE_CHANE_INTO, params, new SMObjectCallBack<BaseBean>() {

            @Override
            public void onSuccess(BaseBean baseBean) {

                dialog.dismiss();

                if (baseBean.getCode() == HttpCode.REQUEST_SUCCESS) {

                    adapter.notifyDataSetChanged();

                    ToastUtil.showToast(baseBean.getMsg(), DeviceEnterExamineActivity.this);
                    finish();

                } else {

                    ToastUtil.showToast(baseBean.getMsg(), DeviceEnterExamineActivity.this);

                }


            }

            @Override
            public void onError(int error, String msg) {

                dialog.dismiss();
                ToastUtil.showToast("网络不给力呀哈！！！", DeviceEnterExamineActivity.this);

            }
        });


    }
    //确定废品入库接口

    private void MakeSureBadDeviceIntoHouse(String name) {

        RequestParams params = new RequestParams();

        params.addBodyParameter("waste_peo", name);

        HttpTool.getInstance(DeviceEnterExamineActivity.this).httpWithParams(Url.WASTE_DEVICE_ENTER_HOUSE_SURN, params, new SMObjectCallBack<BaseBean>() {

            @Override
            public void onSuccess(BaseBean baseBean) {

                if (HttpCode.REQUEST_SUCCESS == baseBean.getCode()) {


                    ToastUtil.showToast(baseBean.getMsg(), DeviceEnterExamineActivity.this);

                    finish();

                } else {

                    ToastUtil.showToast(baseBean.getMsg(), DeviceEnterExamineActivity.this);

                }

            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }


}
