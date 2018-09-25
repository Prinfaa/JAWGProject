package com.example.dell_pc.unitpad;

import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.dell_pc.unitpad.item.BankLocationInformation;
import com.example.dell_pc.unitpad.tools.HttpUtils;
import com.example.dell_pc.unitpad.view.CommonProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CompanyOnMapActivity extends AppCompatActivity implements SensorEventListener {

    private ListView listView;
    private DrawerLayout drawerLayout;
    private BaiduMap mBaiduMap;
    private MapView mMapView;
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private BDLocation mLocation;
    private String GET_BANK_LOCATION_INFORMATION = "http://dndzl.cn/push/sub_branch.php?bloc=";//北京银行所有的支行信息
    private List<BankLocationInformation.ResultBean> bankList = new ArrayList<>();

    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;
    CommonProgressDialog dialog;

    private String bloc;
//    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_on_map);
        bloc=getIntent().getStringExtra("bloc");

        initView();

        dialog=new CommonProgressDialog(this,"数据加载中....");
        HttpUtils.getJSON(GET_BANK_LOCATION_INFORMATION, BankInfoHandler);

    }

    private void initView() {
        listView = (ListView) findViewById(R.id.v4_listview);
        drawerLayout = (DrawerLayout) findViewById(R.id.v4_drawerlayout);
        mMapView = (MapView) findViewById(R.id.bmapView);

        initMapData();
    }

    private void initMapData() {
        mBaiduMap = mMapView.getMap();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker));
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.overlook(0);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }

        //地图上比例尺
        mMapView.showScaleControl(false);

        findViewById(R.id.btn_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });


    }


    private void initDate() {
        final List<String> list = new ArrayList<String>();
        list.clear();

        for (int i = 0; i < bankList.size(); i++) {

            list.add(bankList.get(i).getCom_name());

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                textView.setText(list.get(position));
                Intent intent = new Intent();
                intent.setClass(CompanyOnMapActivity.this, MainActivity.class);
                intent.putExtra("unitID", bankList.get(position).getCompany_id());
                intent.putExtra("unitName", bankList.get(position).getCom_name());
                startActivity(intent);

                dialog.show();

            }
        });

    }

    private void showDrawerLayout() {
        if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.openDrawer(Gravity.LEFT);
        } else {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            mLocation = location;
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }


    /**
     * 添加事件覆盖物
     */
    private void addOverlay(final List<BankLocationInformation.ResultBean> eventModels) {

        mBaiduMap.clear();

        for (int i = 0; i < eventModels.size(); i++) {
            BitmapDescriptor bd = null;//覆盖物样式


            bd = BitmapDescriptorFactory.fromResource(R.mipmap.company_coverly);

            LatLng latLng = new LatLng(Double.valueOf(eventModels.get(i).getCom_lati()), Double.valueOf(eventModels.get(i).getCom_long()));

            OverlayOptions options = new MarkerOptions()
                    .position(latLng)
                    .icon(bd)
                    .zIndex(9)
                    .draggable(true);//为marker添加拖拽
            Marker marker = (Marker) mBaiduMap.addOverlay(options);
            Bundle bundle = new Bundle();
            bundle.putString("CompanyName", eventModels.get(i).getCom_name());
            bundle.putString("companyID", eventModels.get(i).getCompany_id());

            marker.setExtraInfo(bundle);

            mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(final Marker marker) {

                    View marketdao = LayoutInflater.from(CompanyOnMapActivity.this).inflate(R.layout.dialog_cover_mark, null, false);

                    Button deal = (Button) marketdao.findViewById(R.id.btn_check_deal);
                    ImageButton channel = (ImageButton) marketdao.findViewById(R.id.iv_close);

                    TextView tvDeviceName = (TextView) marketdao.findViewById(R.id.tv_company_name);

                    final String name = marker.getExtraInfo().getString("CompanyName");
                    final String id = marker.getExtraInfo().getString("companyID");
                    tvDeviceName.setText(name);

                    deal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.putExtra("unitID", id);
                            intent.putExtra("unitName", name);
                            intent.setClass(CompanyOnMapActivity.this, MainActivity.class);
                            startActivity(intent);



                        }
                    });


                    channel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mBaiduMap.hideInfoWindow();
                        }
                    });
                    Point p = mBaiduMap.getProjection().toScreenLocation(marker.getPosition());
                    p.x = p.x - 220;//设置x轴偏移量
                    p.y = p.y - 30;//设置y轴偏移量
                    LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);

                    InfoWindow in = new InfoWindow(marketdao, llInfo, -50);
                    mBaiduMap.showInfoWindow(in);

                    return false;
                }
            });


        }


    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();


    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    public void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();

       dialog.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocClient.stop();
        // 关闭定位图层

        if (mBaiduMap != null) {

            mBaiduMap.setMyLocationEnabled(false);
        }
        if (mMapView != null) {

            mMapView.onDestroy();
            mMapView = null;

        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        double x = event.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public Handler BankInfoHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            String data = (String) msg.obj;

            try {
                JSONObject jbData = new JSONObject(data);

                int code = jbData.getInt("code");

                if (code == 200) {

                    bankList.clear();

                    JSONArray jsarry = jbData.getJSONArray("result");

                    for (int i = 0; i < jsarry.length(); i++) {

                        BankLocationInformation.ResultBean bean = new BankLocationInformation.ResultBean();

                        JSONObject ObjBean = jsarry.getJSONObject(i);

                        bean.setCom_lati(ObjBean.getString("com_lati"));
                        bean.setCom_long(ObjBean.getString("com_long"));
                        bean.setCom_name(ObjBean.getString("com_name"));
                        bean.setCompany_id(ObjBean.getString("company_id"));

                        bankList.add(bean);
                    }

                    if (bankList.size() > 0) {

                        initDate();
                        addOverlay(bankList);

                    }


                } else {

                    Toast.makeText(CompanyOnMapActivity.this, "无数据", Toast.LENGTH_SHORT).show();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };



    //回退键控制
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            myHandler.sendEmptyMessageDelayed(0, 2000);
        } else {

            finish();
            System.exit(0);
        }
    }

}
