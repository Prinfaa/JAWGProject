package com.example.dell_pc.microstationreceivealarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.dell_pc.microstationreceivealarm.adapter.FireCarAdapter;
import com.example.dell_pc.microstationreceivealarm.item.CarItem;
import com.example.dell_pc.microstationreceivealarm.item.FireEngineItem;
import com.example.dell_pc.microstationreceivealarm.tools.HttpUtils;
import com.example.dell_pc.microstationreceivealarm.tools.MapTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell-pc on 2017/7/28.
 */
public class FireCar extends Activity implements View.OnClickListener {


    private String communityID;
    private MapView mMapView = null;
    private BaiduMap baiduMap = null;
    private MapTools mapTools = new MapTools();
//    private List<CarItem> fireCarList = new ArrayList<>();
    private List<FireEngineItem> fireEngineItems = new ArrayList<>();

    private ListView lvFireCar;
    private FireCarAdapter fireCarAdapter;
    private ImageView ivPackUp;
    private TextView tvListBtn;
    private TextView tvNormalMap, tvSatelliteMap;
    private boolean isNormalMap = true;
    private LinearLayout llListBtn;
    private View lastView = null;
    private LinearLayout llFireCarList;
    private LinearLayout llInfo;
    private TextView tvCarName, tvMaterial, tvWeight, tvLevel, tvDriver, tvMaster, tvEquipment;
    private Button btnShowTrack;
    private Button btnCarPackUp;
    private ImageView ivDriverPhone, ivMasterPhone;
    private FrameLayout flCar;
    private int nCarSelect;
    private Button btnTrackShow;
    private FrameLayout flMap;


    private boolean isFirst = true;
    private GetCarsInfoThread getCarsInfoThread = new GetCarsInfoThread();

    private String URL_GET_CARS_INFO = "http://dndzl.cn/daping/fire_car.php?unit_id=328";

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);

        initData();

        initView();
        getCarsInfoThread.start();

    }

    private void initData() {
        communityID = "13";
        SDKInitializer.initialize(getApplicationContext());

    }


    private void initView() {
        setContentView(R.layout.fire_car);

        ivPackUp = (ImageView) findViewById(R.id.ivPackUp);
        ivPackUp.setOnClickListener(this);

        tvListBtn = (TextView) findViewById(R.id.tvListBtn);
        tvListBtn.setOnClickListener(this);

        tvNormalMap = (TextView) findViewById(R.id.tvNormalMap);
        tvSatelliteMap = (TextView) findViewById(R.id.tvSatelliteMap);
        tvNormalMap.setOnClickListener(this);
        tvSatelliteMap.setOnClickListener(this);

        llListBtn = (LinearLayout) findViewById(R.id.llListBtn);

        llFireCarList = (LinearLayout) findViewById(R.id.llFireCarList);
        lvFireCar = (ListView) findViewById(R.id.lvFireCar);
//        fireCarAdapter = new FireCarAdapter(this, fireCarList);
        fireCarAdapter = new FireCarAdapter(this, fireEngineItems);
        lvFireCar.setAdapter(fireCarAdapter);

        tvCarName = (TextView) findViewById(R.id.tvCarName);
        tvMaterial = (TextView) findViewById(R.id.tvMaterial);
        tvWeight = (TextView) findViewById(R.id.tvWeight);
        tvLevel = (TextView) findViewById(R.id.tvLevel);
        tvMaster = (TextView) findViewById(R.id.tvMaster);
        tvDriver = (TextView) findViewById(R.id.tvDriver);
        tvEquipment = (TextView) findViewById(R.id.tvEquipment);
        ivMasterPhone = (ImageView) findViewById(R.id.ivMasterPhone);
        ivDriverPhone = (ImageView) findViewById(R.id.ivDriverPhone);
        btnCarPackUp = (Button) findViewById(R.id.btnCarPackUp);
        flCar = (FrameLayout) findViewById(R.id.flCar);
        flMap = (FrameLayout) findViewById(R.id.flMap);
        //btnShowTrack = (Button) findViewById(R.id.btnShowTrack);
        btnCarPackUp.setOnClickListener(this);
        ivMasterPhone.setOnClickListener(this);
        ivDriverPhone.setOnClickListener(this);

        btnTrackShow = (Button) findViewById(R.id.btnTrackShow);
        btnTrackShow.setOnClickListener(this);


        lvFireCar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lastView != null) {
                    lastView.setBackgroundColor(0XCCFFFFFF);
                }
                nCarSelect = position;
                flCar.setVisibility(View.VISIBLE);
                view.setBackgroundColor(0X22FF4422);
                lastView = view;
                showFireCarInfo(position);

            }
        });

        showMap();


    }

    private void showMap() {
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mMapView.getMap();

        //卫星地图
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mapTools.setCenterAndZoom(baiduMap, "36.7228967912", "116.9715909885", 18);
        MapStatus mapStatus = new MapStatus.Builder().overlook(-180).build();
        MapStatusUpdate msu = MapStatusUpdateFactory.newMapStatus(mapStatus);
        baiduMap.setMapStatus(msu);
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int i = marker.getZIndex();
                packUp();
                showFireCarInfo(i);
                return false;
            }
        });

    }


    private void showFireCarPosition(int i) {
        String lon = fireEngineItems.get(i).getLongitude();
        String lat = fireEngineItems.get(i).getLatitude();
        mapTools.setCenterAndZoom(baiduMap, lat, lon, 19);
    }

    boolean isGet = true;

    class GetCarsInfoThread extends Thread {
        public void run() {
            try {
                while (isGet) {
                    HttpUtils.getJSON(URL_GET_CARS_INFO, getCarsHandler);
                    sleep(3000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private Handler getCarsHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String jsonData = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
//                    String id = object.getString("carID");
//                    String name = object.getString("carName");
//                    String materialType = object.getString("materialType");
//                    String weight = object.getString("weight");
//                    String driver = object.getString("driver");
//                    String driverPhone = object.getString("driverPhone");
//                    String master = object.getString("master");
//                    String masterPhone = object.getString("masterPhone");
//                    String equipment = object.getString("equipment");
//                    String picUrl = object.getString("pic");
//                    String level = object.getString("level");
//                    String lat = object.getString("latitude");
//                    String lon = object.getString("longitude");

                    String name = object.getString("name");
                    String longitude = object.getString("longitude");
                    String latitude = object.getString("latitude");
                    String level = object.getString("level");
                    String pic = object.getString("pic");
                    String material_type = object.getString("material_type");
                    String driver = object.getString("driver");
                    String driver_phone = object.getString("driver_phone");
                    String master = object.getString("master");
                    String master_phone = object.getString("master_phone");
                    String equipment = object.getString("equipment");
                    String longx = object.getString("long");
                    String lati = object.getString("lati");

                    if (isFirst == true) {

                        FireEngineItem item=new FireEngineItem();

                        item.setName(name);
                        item.setLongitude(longitude);
                        item.setLatitude(latitude);
                        item.setLevel(level);
                        item.setPic(pic);
                        item.setMaterial_type(material_type);
                        item.setDriver(driver);
                        item.setDriver_phone(driver_phone);
                        item.setMaster(master);
                        item.setMaster_phone(master_phone);
                        item.setEquipment(equipment);
                        item.setLongX(longx);
                        item.setLati(lati);


//                        fireCarList.add(new CarItem(id, name, materialType, weight, driver, driverPhone, master, masterPhone, equipment, picUrl, level, lat, lon, null));
//                        addCarMarkers(i, fireCarList.get(i).getLat(), fireCarList.get(i).getLon());

                        fireEngineItems.add(item);
                        addCarMarkers(i, fireEngineItems.get(i).getLatitude(), fireEngineItems.get(i).getLongitude());

                    } else {
//                        fireCarList.get(i).setLevel(level);
//                        fireCarList.get(i).setLat(lat);
//                        fireCarList.get(i).setLon(lon);
//                        fireEngineItems.get(i).setLevel(level);
//                        fireEngineItems.get(i).setLat(lat);
//                        fireEngineItems.get(i).setLon(lon);
                        if (llFireCarList.getVisibility() == View.VISIBLE) {
                            showFireCarInfo(nCarSelect);
                        }
//                        updateCarsPosition(i, fireCarList.get(i).getLat(), fireCarList.get(i).getLon());
                        updateCarsPosition(i, fireEngineItems.get(i).getLati(), fireEngineItems.get(i).getLongX());
                    }
                }
                isFirst = false;
                fireCarAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    private void addCarMarkers(int i, String lat, String lon) {
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.fire_car_icon1);
        //定义Maker坐标点
        LatLng point = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));

        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap)
                .animateType(MarkerOptions.MarkerAnimateType.grow);
        //在地图上添加Marker，并显示

        Marker marker = (Marker) baiduMap.addOverlay(option);
        marker.setZIndex(i);
        fireEngineItems.get(i).setMarker(marker);
        showFireCarPosition(i);
    }


    private void updateCarsPosition(int i, String lat, String lon) {
        //移动Maker
        LatLng point = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
        fireEngineItems.get(i).getMarker().setPosition(point);
    }


    private void showFireCarInfo(int id) {
        flCar.setVisibility(View.VISIBLE);
        tvCarName.setText(fireEngineItems.get(id).getName());
        tvMaterial.setText(fireEngineItems.get(id).getMaterial_type());
//        tvWeight.setText(fireCarList.get(id).getWeight() + "吨");
        tvMaster.setText(fireEngineItems.get(id).getMaster());
        tvDriver.setText(fireEngineItems.get(id).getDriver());
        tvEquipment.setText(fireEngineItems.get(id).getEquipment());
        tvLevel.setText(fireEngineItems.get(id).getLevel() + "%");
//        tvCarName.setText(fireCarList.get(id).getName());
//        tvMaterial.setText(fireCarList.get(id).getMaterialType());
//        tvWeight.setText(fireCarList.get(id).getWeight() + "吨");
//        tvMaster.setText(fireCarList.get(id).getMaster());
//        tvDriver.setText(fireCarList.get(id).getDriver());
//        tvEquipment.setText(fireCarList.get(id).getEquipment());
//        tvLevel.setText(fireCarList.get(id).getLevel() + "%");

        int nLevel = Integer.parseInt(fireEngineItems.get(id).getLevel());
        if (nLevel >= 80) {
            tvLevel.setTextColor(0XFF99CCFF);
        } else if (nLevel >= 60) {
            tvLevel.setTextColor(0XFFCCFF99);
        } else if (nLevel >= 40) {
            tvLevel.setTextColor(0XFFFFFF00);
        } else if (nLevel >= 20) {
            tvLevel.setTextColor(0XFFFF9900);
        } else {
            tvLevel.setTextColor(0XFFFF6666);
        }

    }

    private void packUp() {
        llListBtn.setVisibility(View.VISIBLE);
        flCar.setVisibility(View.GONE);
        llFireCarList.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view == ivMasterPhone) {
//            String inputStr = fireCarList.get(nCarSelect).getMasterPhone();
            String inputStr = fireEngineItems.get(nCarSelect).getMaster_phone();
            callPhone(inputStr);
        } else if (view == ivDriverPhone) {
//            String inputStr = fireCarList.get(nCarSelect).getDriverPhone();
            String inputStr = fireEngineItems.get(nCarSelect).getDriver_phone();
            callPhone(inputStr);
        } else if (view == btnShowTrack) {
            startActivity(new Intent(this, TrackMap.class));
        } else if (view == tvListBtn) {
            llListBtn.setVisibility(View.GONE);
            llFireCarList.setVisibility(View.VISIBLE);
        } else if (view == ivPackUp) {
            packUp();
        } else if (view == btnCarPackUp) {
            flCar.setVisibility(View.GONE);
        } else if (view == btnTrackShow) {
            startActivity(new Intent(FireCar.this, TrackMap.class));
        }

    }

    public void callPhone(final String inputStr) {
        if (inputStr.trim().length() != 0) {
            AlertDialog.Builder multiDia = new AlertDialog.Builder(this);
            multiDia.setTitle("您确定拨打电话" + inputStr + "?");
            multiDia.setPositiveButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                }
            });
            multiDia.setNeutralButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                    Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + inputStr));
                    startActivity(phoneIntent);
                }
            });
            multiDia.create().show();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (llFireCarList.getVisibility() == View.VISIBLE || flCar.getVisibility() == View.VISIBLE) {
                packUp();
            } else {
                getCarsInfoThread.interrupt();
                finish();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
