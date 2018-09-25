package com.example.dell_pc.microstationreceivealarm;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
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
import com.example.dell_pc.microstationreceivealarm.adapter.WaterLevelAdapter;
import com.example.dell_pc.microstationreceivealarm.adapter.WaterPressAdapter;
import com.example.dell_pc.microstationreceivealarm.item.WaterLevelItem;
import com.example.dell_pc.microstationreceivealarm.item.WaterPressItem;
import com.example.dell_pc.microstationreceivealarm.tools.HttpUtils;
import com.example.dell_pc.microstationreceivealarm.tools.MapTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dell-pc on 2017/7/28.
 */
public class Water extends Activity implements View.OnClickListener {

    private MapView mMapView = null;
    private BaiduMap baiduMap = null;
    private MapTools mapTools = new MapTools();
    private String communityID;
    private List<WaterPressItem> waterPressList = new ArrayList<WaterPressItem>();
    private ListView lvWaterPress;
    private WaterPressAdapter waterPressAdapter;
    private TextView tvAlarmKeyListTitle;
    private ImageView ivPackUp;
    private TextView tvListBtn;
    private Marker[] pressMarkers, levelMarkers;
    private TextView tvNormalMap, tvSatelliteMap;
    private boolean isNormalMap = true;
    private LinearLayout llListBtn, llPie;
    private WebView wvPressPie, wvLevelPie;
    private FrameLayout flMeter;
    private ImageView ivMeter, ivRow, ivOffline;
    private TextView tvValue;
    private Float j;
    private View lastView = null;
    private TextView tvWaterLevelListTitle;
    private ListView lvWaterLevel;
    private List<WaterLevelItem> waterLevelList = new ArrayList<WaterLevelItem>();
    private WaterLevelAdapter waterLevelAdapter;
    private LinearLayout llPressList, llLevelList;
    private TextView tvUnit;
    private LinearLayout llWater;
    private int waterH;
    private boolean isFirstMea = true;
    private FrameLayout flWater;
    private Float levelRate;
    private View viewPressSel, viewLevelSel;
    private TextView tvType, tvUnitName, tvPosition;


    private String URL_GET_WATER_PRESS = "http://jn.xiaofang365.cn/xfwlw/fireCommand/getWaterPress.php?communityID=";
    private String URL_GET_WATER_LEVEL = "http://jn.xiaofang365.cn/xfwlw/fireCommand/getWaterLevel.php?communityID=";

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);

        initData();
        initView();
        HttpUtils.getJSON(URL_GET_WATER_PRESS + communityID, getWaterPressHandler);

    }

    private void initData() {
        communityID = "16";
        SDKInitializer.initialize(getApplicationContext());

    }

    private void initView() {
        setContentView(R.layout.water);
        tvAlarmKeyListTitle = (TextView) findViewById(R.id.tvAlarmKeyListTitle);

        ivPackUp = (ImageView) findViewById(R.id.ivPackUp);
        ivPackUp.setOnClickListener(this);

        tvListBtn = (TextView) findViewById(R.id.tvListBtn);
        tvListBtn.setOnClickListener(this);

        tvNormalMap = (TextView) findViewById(R.id.tvNormalMap);
        tvSatelliteMap = (TextView) findViewById(R.id.tvSatelliteMap);
        tvNormalMap.setOnClickListener(this);
        tvSatelliteMap.setOnClickListener(this);

        llListBtn = (LinearLayout) findViewById(R.id.llListBtn);
        llPie = (LinearLayout) findViewById(R.id.llPie);

        flMeter = (FrameLayout) findViewById(R.id.flMeter);
        ivMeter = (ImageView) findViewById(R.id.ivMeter);
        ivRow = (ImageView) findViewById(R.id.ivRow);
        ivOffline = (ImageView) findViewById(R.id.ivOffline);
        tvValue = (TextView) findViewById(R.id.tvValue);
        tvUnit = (TextView) findViewById(R.id.tvUnit);
        flWater = (FrameLayout) findViewById(R.id.flWater);

        tvType = (TextView) findViewById(R.id.tvType);
        tvUnitName = (TextView) findViewById(R.id.tvUnitName);
        tvPosition = (TextView) findViewById(R.id.tvPosition);

        tvWaterLevelListTitle = (TextView) findViewById(R.id.tvWaterLevelListTitle);

        llPressList = (LinearLayout) findViewById(R.id.llPressList);
        llLevelList = (LinearLayout) findViewById(R.id.llLevelList);

        lvWaterPress = (ListView) findViewById(R.id.lvWaterPress);
        waterPressAdapter = new WaterPressAdapter(this, waterPressList);
        lvWaterPress.setAdapter(waterPressAdapter);

        lvWaterLevel = (ListView) findViewById(R.id.lvWaterLevel);
        waterLevelAdapter = new WaterLevelAdapter(this, waterLevelList);
        lvWaterLevel.setAdapter(waterLevelAdapter);

        llWater = (LinearLayout) findViewById(R.id.llWater);

        lvWaterPress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPressInfo(view, position);
            }
        });

        lvWaterLevel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showLevelInfo(view, position);
            }
        });


        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mMapView.getMap();
        //卫星地图
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mapTools.setCenterAndZoom(baiduMap, "36.7345976613", "117.0448044073", 18);
        MapStatus mapStatus = new MapStatus.Builder().overlook(-180).build();
        MapStatusUpdate msu = MapStatusUpdateFactory.newMapStatus(mapStatus);

        baiduMap.setMapStatus(msu);

        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int i = marker.getZIndex();
                if (marker.getTitle().contains("栓")) {
                    packUp();
                    showPressInfo(null, i);
                } else {
                    packUp();
                    showLevelInfo(null, i);
                }
                return false;
            }
        });


        wvPressPie = (WebView) findViewById(R.id.wvPressPie);
        wvPressPie.loadUrl("http://jn.xiaofang365.cn/xfwlw/fireCommand/blank.php");

        WebSettings webSettingsPress = wvPressPie.getSettings();
        webSettingsPress.setUseWideViewPort(true);
        webSettingsPress.setLoadWithOverviewMode(true);
        webSettingsPress.setJavaScriptEnabled(true);
        webSettingsPress.setDomStorageEnabled(true);
        webSettingsPress.setCacheMode(WebSettings.LOAD_NO_CACHE);

        wvLevelPie = (WebView) findViewById(R.id.wvLevelPie);
        wvLevelPie.loadUrl("http://jn.xiaofang365.cn/xfwlw/fireCommand/blank.php");

        WebSettings webSettingsLevel = wvLevelPie.getSettings();
        webSettingsLevel.setUseWideViewPort(true);
        webSettingsLevel.setLoadWithOverviewMode(true);
        webSettingsLevel.setJavaScriptEnabled(true);
        webSettingsLevel.setDomStorageEnabled(true);
        webSettingsLevel.setCacheMode(WebSettings.LOAD_NO_CACHE);

    }

    private void showPressInfo(View view, int position) {
        tvType.setText(waterPressList.get(position).getType());
        tvUnitName.setText(waterPressList.get(position).getConstructionName());
        tvPosition.setText(waterPressList.get(position).getPosition());
        tvUnit.setText("Mpa");
        flWater.setVisibility(View.GONE);
        if (lastView != null) {
            lastView.setBackgroundColor(0XCCFFFFFF);
        }
        if (view != null) {
            view.setBackgroundColor(0XFFCCFFFF);
            lastView = view;
        }
        String lat = waterPressList.get(position).getLat();
        String lon = waterPressList.get(position).getLon();
        mapTools.setCenterAndZoom(baiduMap, lat, lon, 18);
        llPie.setVisibility(View.GONE);
        flMeter.setVisibility(View.VISIBLE);
        if ((System.currentTimeMillis() / 1000 - Long.parseLong(waterPressList.get(position).getLastTime())) > 3600) {
            ivOffline.setVisibility(View.VISIBLE);
            ivMeter.setVisibility(View.GONE);
            ivRow.setVisibility(View.GONE);
            tvValue.setText("———");
            tvValue.setTextColor(0XFF663300);
            return;
        } else {
            ivOffline.setVisibility(View.GONE);
            ivMeter.setVisibility(View.VISIBLE);
            ivRow.setVisibility(View.VISIBLE);

        }

        if (waterPressList.get(position).getValue() != null) {
            j = Float.valueOf(waterPressList.get(position).getValue());
            new MeterThread().start();
        }
        tvValue.setText(waterPressList.get(position).getValue());
        if ((Double.parseDouble(waterPressList.get(position).getValue())) >= (Double.parseDouble(waterPressList.get(position).getReferValue()))) {
            tvValue.setTextColor(0XFF006699);
        } else {
            tvValue.setTextColor(0XFFFF0000);
        }

    }

    private void showLevelInfo(View view, int position) {
        tvType.setText(waterLevelList.get(position).getType());
        tvUnitName.setText(waterLevelList.get(position).getConstructionName());
        tvPosition.setText(waterLevelList.get(position).getPosition());

        tvUnit.setText("%");
        ivMeter.setVisibility(View.GONE);
        ivRow.setVisibility(View.GONE);
        flMeter.setVisibility(View.VISIBLE);

        if (lastView != null) {
            lastView.setBackgroundColor(0XCCFFFFFF);
        }
        if(view!=null){
            view.setBackgroundColor(0XFFCCFFFF);
            lastView = view;
        }
        String lat = waterLevelList.get(position).getLat();
        String lon = waterLevelList.get(position).getLon();
        mapTools.setCenterAndZoom(baiduMap, lat, lon, 18);
        llPie.setVisibility(View.GONE);
        if ((System.currentTimeMillis() / 1000 - Long.parseLong(waterLevelList.get(position).getLastTime())) > 3600) {
            ivOffline.setVisibility(View.VISIBLE);
            tvValue.setText("———");
            return;
        } else {

            ivOffline.setVisibility(View.GONE);
            flWater.setVisibility(View.VISIBLE);
            ViewTreeObserver vto = llWater.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    llWater.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    if (isFirstMea == true) {
                        waterH = llWater.getHeight();
                        System.out.println("waterH============" + waterH);
                        isFirstMea = false;
                        new LevelThread().start();
                    }
                }
            });

        }

        if (waterLevelList.get(position).getLevel() != null) {
            levelRate = Float.valueOf(waterLevelList.get(position).getLevel()) / Float.valueOf(waterLevelList.get(position).getMaxLevel());
            new LevelThread().start();
        }
        float fRate = Float.parseFloat(waterLevelList.get(position).getLevel()) * 100 / Float.parseFloat(waterLevelList.get(position).getMaxLevel());

        String rate = String.valueOf((int) fRate);
        tvValue.setText(rate);
        if ((fRate >= (100 * Float.parseFloat(waterLevelList.get(position).getReferValue())))) {
            tvValue.setTextColor(0XFF006699);
        } else {
            tvValue.setTextColor(0XFFFF0000);
        }

    }

    class MeterThread extends Thread {
        public void run() {
            try {
                int angle = 0;
                for (int i = 0; i < (j * 300); i++) {
                    angle = (i * 8 / 5) - 30;
                    Message msg = new Message();
                    msg.arg1 = angle;
                    meterHandler.sendMessage(msg);
                    sleep(10);
                }


                int i = 0;
                while (i < 2) {
                    sleep(4000);
                    i++;
                    if (i == 2) {
                        i = 0;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class LevelThread extends Thread {
        public void run() {
            try {
                int j = (int) ((1 - levelRate) * waterH);
                System.out.println("j============" + j);
                System.out.println("levelRate============" + levelRate);

                for (int i = 0; i < j; i++) {
                    Message msg = new Message();
                    msg.arg1 = i;
                    levelHandler.sendMessage(msg);
                    sleep(10);
                }


                int i = 0;
                while (i < 2) {
                    sleep(4000);
                    i++;
                    if (i == 2) {
                        i = 0;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private Handler meterHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int angle = msg.arg1;
            ivRow.setRotation(angle);
        }
    };

    private Handler levelHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int i = msg.arg1;
            FrameLayout.LayoutParams para1;
            para1 = (FrameLayout.LayoutParams) llWater.getLayoutParams();
            para1.height = waterH - i;
            llWater.setLayoutParams(para1);

        }
    };


    private Handler getWaterPressHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            waterPressList.clear();
            pressMarkers = null;
            String jsonData = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                pressMarkers = new Marker[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String id = object.getString("id");
                    String constructionName = object.getString("constructionName");
                    String type = object.getString("type");
                    String position = object.getString("position");
                    String value = object.getString("value");
                    String referValue = object.getString("referValue");
                    String lat = object.getString("lat");
                    String lon = object.getString("lon");
                    String lastTime = object.getString("lastTime");
                    waterPressList.add(new WaterPressItem(id, constructionName, position, type, value, referValue, lat, lon, lastTime));
                    addPressMarker(lat, lon, lastTime, constructionName, i, value, referValue);
                }
                waterPressAdapter.notifyDataSetChanged();
                HttpUtils.getJSON(URL_GET_WATER_LEVEL + communityID, getWaterLevelHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private Handler getWaterLevelHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            waterLevelList.clear();
            levelMarkers = null;
            String jsonData = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                levelMarkers = new Marker[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String id = object.getString("id");
                    String constructionName = object.getString("constructionName");
                    String type = object.getString("type");
                    String position = object.getString("position");
                    String level = object.getString("level");
                    String maxLevel = object.getString("maxLevel");
                    String referValue = object.getString("referValue");
                    String lat = object.getString("lat");
                    String lon = object.getString("lon");
                    String lastTime = object.getString("lastTime");
                    waterLevelList.add(new WaterLevelItem(id, constructionName, position, type, level, maxLevel, referValue, lat, lon, lastTime));
                    addLevelMarker(lat, lon, lastTime, constructionName, i, level, maxLevel, referValue, type);
                }
                waterLevelAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    private void addPressMarker(String lat, String lon, String lastTime, String name, int i, String value, String referValue) {
        BitmapDescriptor bitmap = null;


        //定义Maker坐标点
        LatLng point = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));


        long timeNow = System.currentTimeMillis() / 1000;
        if (timeNow - Long.parseLong(lastTime) < 3600) {
            if ((Double.parseDouble(value)) >= (Double.parseDouble(referValue))) {
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.xiao_huo_shuan_normal);
            } else {
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.xiao_huo_shuan_low);
            }

        } else {
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.xiao_huo_shuan_offline);
        }
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap)
                .animateType(MarkerOptions.MarkerAnimateType.grow);
        //在地图上添加Marker，并显示
        String type = waterPressList.get(i).getType();
        pressMarkers[i] = (Marker) baiduMap.addOverlay(option);
        pressMarkers[i].setTitle(type);
        pressMarkers[i].setZIndex(i);

    }

    private void addLevelMarker(String lat, String lon, String lastTime, String name, int i, String level, String maxLevel, String referValue, String type) {
        BitmapDescriptor bitmap = null;
        //定义Maker坐标点
        LatLng point = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
        long timeNow = System.currentTimeMillis() / 1000;

        if (type.contains("池")) {
            if (timeNow - Long.parseLong(lastTime) < 3600) {
                double rate = (Double.parseDouble(level)) / (Double.parseDouble(maxLevel));
                if (rate >= (Double.parseDouble(referValue))) {
                    bitmap = BitmapDescriptorFactory.fromResource(R.drawable.shui_chi_normal);
                } else {
                    bitmap = BitmapDescriptorFactory.fromResource(R.drawable.shui_chi_low);
                }

            } else {
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.shui_chi_offline);
            }
        } else if (type.contains("箱")) {
            if (timeNow - Long.parseLong(lastTime) < 3600) {
                double rate = (Double.parseDouble(level)) / (Double.parseDouble(maxLevel));
                if (rate >= (Double.parseDouble(referValue))) {
                    bitmap = BitmapDescriptorFactory.fromResource(R.drawable.shui_xiang_normal);
                } else {
                    bitmap = BitmapDescriptorFactory.fromResource(R.drawable.shui_xiang_low);
                }

            } else {
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.shui_xiang_offline);
            }

        }
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap)
                .animateType(MarkerOptions.MarkerAnimateType.grow);
        //在地图上添加Marker，并显示

        pressMarkers[i] = (Marker) baiduMap.addOverlay(option);
        pressMarkers[i].setTitle(name);
        pressMarkers[i].setZIndex(i);

    }

    //收起所有弹出窗口
    private void packUp() {
        ivPackUp.setVisibility(View.GONE);
        llListBtn.setVisibility(View.VISIBLE);
        llPie.setVisibility(View.GONE);
        llPressList.setVisibility(View.GONE);
        llLevelList.setVisibility(View.GONE);
        if (lastView != null) {
            lastView.setBackgroundColor(0XCCFFFFFF);
            lastView = null;
        }
        flMeter.setVisibility(View.GONE);
        wvPressPie.loadUrl("http://jn.xiaofang365.cn/xfwlw/fireCommand/blank.php");
        wvLevelPie.loadUrl("http://jn.xiaofang365.cn/xfwlw/fireCommand/blank.php");
    }


    @Override
    public void onClick(View v) {
        if (v == ivPackUp) {
            packUp();
        } else if (v == tvListBtn) {
            packUp();
            ivPackUp.setVisibility(View.VISIBLE);
            llListBtn.setVisibility(View.GONE);
            llPie.setVisibility(View.VISIBLE);
            wvPressPie.reload();
            wvLevelPie.reload();
            llPressList.setVisibility(View.VISIBLE);
            llLevelList.setVisibility(View.VISIBLE);

            wvLevelPie.loadUrl("http://jn.xiaofang365.cn/xfwlw/fireCommand/waterLevelPie3D.php?communityID=" + communityID);
            wvPressPie.loadUrl("http://jn.xiaofang365.cn/xfwlw/fireCommand/waterPressPie3D.php?communityID=" + communityID);
        } else if (v == tvNormalMap) {
            if (isNormalMap == false) {
                isNormalMap = true;
                baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                tvNormalMap.setTextColor(0XFF3399CC);
                tvSatelliteMap.setTextColor(0X88000000);
            }
        } else if (v == tvSatelliteMap) {
            if (isNormalMap == true) {
                isNormalMap = false;
                baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                tvNormalMap.setTextColor(0X88000000);
                tvSatelliteMap.setTextColor(0XFF3399CC);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if(llPressList.getVisibility()==View.VISIBLE || flMeter.getVisibility()==View.VISIBLE){
                packUp();
            }else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}

