package com.example.dell_pc.microstationreceivealarm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
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
import com.example.dell_pc.microstationreceivealarm.adapter.AlarmKeyAdapter;
import com.example.dell_pc.microstationreceivealarm.adapter.UnitAdapter;
import com.example.dell_pc.microstationreceivealarm.item.AlarmKeyItem;
import com.example.dell_pc.microstationreceivealarm.item.UnitItem;
import com.example.dell_pc.microstationreceivealarm.tools.HttpUtils;
import com.example.dell_pc.microstationreceivealarm.tools.MapTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dell-pc on 2017/7/28.
 */
public class AlarmKeyFac extends Activity implements View.OnClickListener {

    private MapView mMapView = null;
    private BaiduMap baiduMap = null;
    private MapTools mapTools = new MapTools();
    private String communityID;
    private List<AlarmKeyItem> alarmKeyList = new ArrayList<AlarmKeyItem>();
    private ListView lvAlarmKey;
    private AlarmKeyAdapter alarmKeyAdapter;
    private TextView tvAlarmKeyListTitle;
    private ImageView ivPackUp;
    private TextView tvListBtn;
    private Marker[] markers;
    private TextView tvNormalMap, tvSatelliteMap;
    private boolean isNormalMap = true;
    private LinearLayout llListBtn, llPie, llList;
    private WebView wvPie;
    private View lastView = null;
    private FrameLayout llConstruction;
    private TextView tvConsName, tvConsAddress, tvConsType, tvConsStructure, tvConsArea, tvConsHeight, tvConsFac, tvConsGoods;
    private TextView tvConsCancel;
    private List<UnitItem> unitList = new ArrayList<UnitItem>();
    private UnitAdapter unitAdapter;
    private ImageView ivConsPic;
    private ListView lvUnit;
    private ProgressDialog pDialog;

    private String URL_GET_CONSTRUCTION_WITH_ALARM_KEY = "http://jn.xiaofang365.cn/xfwlw/fireCommand/getAlarmKeys.php?communityID=";
    private String URL_GET_UNITS_INFO = "http://jn.xiaofang365.cn/xfwlw/fireCommand/getUnitsInfo.php?constructionID=";
    private String URL_GET_CONSTRUCTION_INFO = "http://jn.xiaofang365.cn/xfwlw/fireCommand/getConstructionInfo.php?constructionID=";

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);

        initData();
        initView();
        HttpUtils.getJSON(URL_GET_CONSTRUCTION_WITH_ALARM_KEY + communityID, getConstructionsHandler);

    }

    private void initData() {
        communityID = "16";
        SDKInitializer.initialize(getApplicationContext());

    }

    private void initView() {
        setContentView(R.layout.alarm_key_fac);
        pDialog = new ProgressDialog(AlarmKeyFac.this);

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
        llList = (LinearLayout) findViewById(R.id.llList);

        lvAlarmKey = (ListView) findViewById(R.id.lvAlarmKey);
        alarmKeyAdapter = new AlarmKeyAdapter(this, alarmKeyList);
        lvAlarmKey.setAdapter(alarmKeyAdapter);
        lvAlarmKey.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lastView != null) {
                    lastView.setBackgroundColor(0XCCFFFFFF);
                }
                view.setBackgroundColor(0XFFFFEEEE);
                lastView = view;
                String lat = alarmKeyList.get(position).getLat();
                String lon = alarmKeyList.get(position).getLon();
                mapTools.setCenterAndZoom(baiduMap, lat, lon, 20);
                llPie.setVisibility(View.GONE);
            }
        });

        llConstruction = (FrameLayout) findViewById(R.id.llConstruction);
        tvConsName = (TextView) findViewById(R.id.tvConsName);
        tvConsAddress = (TextView) findViewById(R.id.tvConsAddress);
        tvConsType = (TextView) findViewById(R.id.tvConsType);
        tvConsStructure = (TextView) findViewById(R.id.tvConsStructure);
        tvConsArea = (TextView) findViewById(R.id.tvConArea);
        tvConsHeight = (TextView) findViewById(R.id.tvConHeight);
        tvConsFac = (TextView) findViewById(R.id.tvConsFac);
        tvConsGoods = (TextView) findViewById(R.id.tvConsGoods);
        ivConsPic = (ImageView) findViewById(R.id.ivConsPic);
        tvConsCancel = (TextView) findViewById(R.id.tvConsCancel);
        tvConsCancel.setOnClickListener(this);

        lvUnit = (ListView) findViewById(R.id.lvUnit);
        unitAdapter = new UnitAdapter(AlarmKeyFac.this, unitList);
        lvUnit.setAdapter(unitAdapter);


        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mMapView.getMap();
        //卫星地图
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mapTools.setCenterAndZoom(baiduMap, "36.7345976613", "117.0448044073", 18);
        final MapStatus mapStatus = new MapStatus.Builder().overlook(-180).build();
        MapStatusUpdate msu = MapStatusUpdateFactory.newMapStatus(mapStatus);
        baiduMap.setMapStatus(msu);
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int i = marker.getZIndex();
                pDialog.show();
                HttpUtils.getJSON(URL_GET_UNITS_INFO + alarmKeyList.get(i).getConstructionID(), getUnitsHandler);
                HttpUtils.getJSON(URL_GET_CONSTRUCTION_INFO + alarmKeyList.get(i).getConstructionID(), getConstructionHandler);
                return false;
            }
        });

        wvPie = (WebView) findViewById(R.id.wvPie);
        wvPie.loadUrl("http://jn.xiaofang365.cn/xfwlw/fireCommand/blank.php");

        WebSettings webSettings = wvPie.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    private Handler getUnitsHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            packUp();
            pDialog.dismiss();
            llConstruction.setVisibility(View.VISIBLE);
            unitList.clear();
            String jsonData = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String id = object.getString("id");
                    String name = object.getString("name");
                    String linkman = object.getString("linkman");
                    String phone = object.getString("phone");
                    String goods = object.getString("goods");
                    String position = object.getString("position");
                    unitList.add(new UnitItem(id, name, linkman, phone, goods, position));
                }
                unitAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private Handler getConstructionHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String jsonData = (String) msg.obj;
            try {
                JSONObject object = new JSONObject(jsonData);
                String name = object.getString("name");
                String address = object.getString("address");
                String constructionType = object.getString("constructionType");
                String structureType = object.getString("structureType");
                String buildingArea = object.getString("buildingArea");
                String layer = object.getString("layer");
                String height = object.getString("height");
                String fireFac = object.getString("fireFac");
                tvConsName.setText(name);
                tvConsAddress.setText(address);
                tvConsType.setText(constructionType);
                tvConsStructure.setText(structureType);
                tvConsArea.setText(buildingArea + "平方米");
                tvConsHeight.setText(layer + "层，" + height + "米");
                tvConsFac.setText(fireFac);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    private Handler getConstructionsHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            alarmKeyList.clear();
            markers = null;
            String jsonData = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                markers = new Marker[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String id = object.getString("id");
                    String name = object.getString("name");
                    String address = object.getString("address");
                    String lat = object.getString("lat");
                    String lon = object.getString("lon");
                    String lastTime = object.getString("lastTime");
                    String constructionID = object.getString("constructionID");
                    addMarker(lat, lon, lastTime, name, i);
                    alarmKeyList.add(new AlarmKeyItem(id, name, address, lat, lon, lastTime, constructionID));
                }
                alarmKeyAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private void addMarker(String lat, String lon, String lastTime, String name, int i) {
        BitmapDescriptor bitmap = null;


        //定义Maker坐标点
        LatLng point = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));


        long timeNow = System.currentTimeMillis() / 1000;
        if (timeNow - Long.parseLong(lastTime) < 3600) {
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.alarm_key_icon);
        } else {
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.alarm_key_offline_icon);
        }
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap)
                .animateType(MarkerOptions.MarkerAnimateType.grow);
        //在地图上添加Marker，并显示

        markers[i] = (Marker) baiduMap.addOverlay(option);
        markers[i].setTitle(name);
        markers[i].setZIndex(i);
    }

    private void packUp() {
        lvAlarmKey.setVisibility(View.GONE);
        ivPackUp.setVisibility(View.GONE);
        tvAlarmKeyListTitle.setVisibility(View.GONE);
        llListBtn.setVisibility(View.VISIBLE);
        llPie.setVisibility(View.GONE);
        llList.setVisibility(View.GONE);
        llConstruction.setVisibility(View.GONE);
        wvPie.loadUrl("http://jn.xiaofang365.cn/xfwlw/fireCommand/blank.php");
        if (lastView != null) {
            lastView.setBackgroundColor(0XCCFFFFFF);
            lastView = null;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == ivPackUp) {
            packUp();
        } else if (v == tvListBtn) {
            tvAlarmKeyListTitle.setVisibility(View.VISIBLE);
            lvAlarmKey.setVisibility(View.VISIBLE);
            ivPackUp.setVisibility(View.VISIBLE);
            llListBtn.setVisibility(View.GONE);
            llPie.setVisibility(View.VISIBLE);
            llList.setVisibility(View.VISIBLE);
            wvPie.loadUrl("http://121.42.35.49/xfwlw/fireCommand/alarmKeyPie.php?communityID=" + communityID);
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
        } else if (v == tvConsCancel) {
            llConstruction.setVisibility(View.GONE);
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
            if (llList.getVisibility() == View.VISIBLE || llConstruction.getVisibility() == View.VISIBLE) {
                packUp();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
