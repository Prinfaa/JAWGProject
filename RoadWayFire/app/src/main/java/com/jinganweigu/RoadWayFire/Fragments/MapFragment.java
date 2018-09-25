package com.jinganweigu.RoadWayFire.Fragments;

import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.jinganweigu.RoadWayFire.Activities.MapVideoActivity;
import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseFragment2;
import com.jinganweigu.RoadWayFire.Entries.EZVideoDeviceInfo;
import com.jinganweigu.RoadWayFire.Entries.EventModel;
import com.jinganweigu.RoadWayFire.Interfaces.Mycontants;
import com.jinganweigu.RoadWayFire.Interfaces.Url;
import com.jinganweigu.RoadWayFire.R;
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
import butterknife.Unbinder;

import static android.content.Context.SENSOR_SERVICE;

public class MapFragment extends BaseFragment2 implements SensorEventListener {
    @BindView(R.id.bmapView)
    MapView mMapView;
    @BindView(R.id.btn_arrow)
    ImageView ivLocation;
    Unbinder unbinder;
    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.tv_save)
    TextView tvSave;
    BaiduMap mBaiduMap;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private BDLocation mLocation;
    private List<EZVideoDeviceInfo.ResultBean> deviceList = new ArrayList<>();


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.fragment_map, null);

        unbinder = ButterKnife.bind(this, view);
        topName.setText("地图");
        backBtn.setVisibility(View.GONE);

        String company_id = Sptools.getString(getActivity(), Mycontants.COMPANY_ID, "");
        String token = Sptools.getString(getActivity(), Mycontants.API_TOKEN, "");

        if (TextUtils.isEmpty(company_id)) {

            ToastUtil.showToast("单位id为空", getActivity());

        } else if (TextUtils.isEmpty(token)) {

            ToastUtil.showToast("Token为空", getActivity());

        } else {

            getDeviceInfo(company_id, token);

        }


        return view;
    }

    @Override
    public void initData() {
        mBaiduMap = mMapView.getMap();
        mSensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker));
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.overlook(0);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(getActivity());
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
        // 隐藏缩放控件
        //  mMapView.showZoomControls(false);

        //定位当前
        ivLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mLocation != null) {

                    LatLng ll = new LatLng(mLocation.getLatitude(),
                            mLocation.getLongitude());
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(18.0f);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                }

            }
        });


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

    private void navigateTo(BDLocation location) {
        // 按照经纬度确定地图位置
        if (isFirstLoc) {
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            // 移动到某经纬度
            mBaiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomBy(5f);
            // 放大
            mBaiduMap.animateMapStatus(update);

            isFirstLoc = false;
        }
        // 显示个人位置图标
        MyLocationData.Builder builder = new MyLocationData.Builder();
        builder.latitude(location.getLatitude());
        builder.longitude(location.getLongitude());
        MyLocationData data = builder.build();
        mBaiduMap.setMyLocationData(data);
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

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


    /**
     * 添加事件覆盖物
     */
    private void addOverlay(final List<EZVideoDeviceInfo.ResultBean> eventModels) {

        mBaiduMap.clear();

        for (int i = 0; i < eventModels.size(); i++) {
            BitmapDescriptor bd = null;//覆盖物样式

            if (eventModels.get(i).getStatus().equals("离线")) {

                bd = BitmapDescriptorFactory.fromResource(R.mipmap.cover_orange);

            } else if (eventModels.get(i).getStatus().equals("正常")) {

                bd = BitmapDescriptorFactory.fromResource(R.mipmap.cover_light_green);

            } else if (eventModels.get(i).getStatus().equals("报警")) {

                bd = BitmapDescriptorFactory.fromResource(R.mipmap.cover_red);

            }


            LatLng latLng = new LatLng(Double.valueOf(eventModels.get(i).getLat()), Double.valueOf(eventModels.get(i).getLng()));

            OverlayOptions options = new MarkerOptions()
                    .position(latLng)
                    .icon(bd)
                    .zIndex(9)
                    .draggable(true);//为marker添加拖拽
            Marker marker = (Marker) mBaiduMap.addOverlay(options);
            Bundle bundle = new Bundle();
            bundle.putString("deviceName", eventModels.get(i).getCamera_name());
            bundle.putString("deviceStatus", eventModels.get(i).getStatus());
            bundle.putString("deviceAddress", eventModels.get(i).getPosition());
            bundle.putString("deviceNum", eventModels.get(i).getDev_number());
            marker.setExtraInfo(bundle);

            mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    View marketdao = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_cover_mark, null, false);

                    Button deal = (Button) marketdao.findViewById(R.id.btn_check_deal);
                    ImageView channel = (ImageView) marketdao.findViewById(R.id.iv_close);

                    TextView tvDeviceName = (TextView) marketdao.findViewById(R.id.tv_device_name);
                    TextView tvDeviceState = (TextView) marketdao.findViewById(R.id.tv_device_state);
                    TextView tvDeviceAddress = (TextView) marketdao.findViewById(R.id.tv_device_address);

                    String name = marker.getExtraInfo().getString("deviceName");
                    String status = marker.getExtraInfo().getString("deviceStatus");
                    String address = marker.getExtraInfo().getString("deviceAddress");
                    String deviceNum=marker.getExtraInfo().getString("deviceNum");



                    tvDeviceAddress.setText("设备位置:" + address);
                    tvDeviceName.setText("设备名称:" + name);
                    tvDeviceState.setText("设备状态:" + status);

                    deal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent=new Intent(getActivity(),MapVideoActivity.class);
                            intent.putExtra("deviceSerial",deviceNum);
                            intent.putExtra("cameraNo",1);
                            intent.putExtra("deviceName",name);
                            intent.putExtra("position",address);
                            intent.putExtra("status",status);
                            startActivity(intent);
//                            intentMethod.startMethodWithStringInt(getActivity(), MapVideoActivity.class,"deviceSerial",deviceNum,"cameraNo",1);

                        }
                    });


                    channel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mBaiduMap.hideInfoWindow();
                        }
                    });
                    Point p = mBaiduMap.getProjection().toScreenLocation(marker.getPosition());
                    p.x = p.x + 280;//设置x轴偏移量
                    p.y = p.y + 600;//设置y轴偏移量
                    LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);

                    InfoWindow in = new InfoWindow(marketdao, llInfo, -50);
                    mBaiduMap.showInfoWindow(in);

                    return false;
                }
            });


        }


    }

    /**
     * 获取设备列表
     * @param company_id
     * @param token
     */
    private void getDeviceInfo(String company_id, String token) {


        RequestParams params = new RequestParams();

        params.addBodyParameter("company_id", company_id);
        params.addBodyParameter("api_token", token);

        HttpTool.getInstance(getActivity()).httpWithParams(Url.COVER_EZvIDEO_DEVICE_INFO, params, new SMObjectCallBack<EZVideoDeviceInfo>() {

            @Override
            public void onSuccess(EZVideoDeviceInfo ezVideoDeviceInfo) {

                if (ezVideoDeviceInfo.getCode() == 200) {

                    deviceList = ezVideoDeviceInfo.getResult();

                    addOverlay(deviceList);


                } else {

                    ToastUtil.showToast("获取数据失败", getActivity());
                }

            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }


}
