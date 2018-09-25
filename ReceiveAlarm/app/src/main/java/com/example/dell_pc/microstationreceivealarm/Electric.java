package com.example.dell_pc.microstationreceivealarm;

import android.app.Activity;
import android.graphics.Color;
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

import com.example.dell_pc.microstationreceivealarm.adapter.ElectricAdapter;
import com.example.dell_pc.microstationreceivealarm.item.ElectricItem;
import com.example.dell_pc.microstationreceivealarm.tools.HttpUtils;
import com.example.dell_pc.microstationreceivealarm.tools.MapTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dell-pc on 2017/7/28.
 */
public class Electric extends Activity implements View.OnClickListener {

    private MapView mMapView = null;
    private BaiduMap baiduMap = null;
    private MapTools mapTools = new MapTools();
    private String communityID;
    private List<ElectricItem> ElectricList = new ArrayList<ElectricItem>();
    private ListView lvElectric;
    private ElectricAdapter ElectricAdapter;
    private ImageView ivPackUp;
    private TextView tvListBtn;
    private Marker[] ElectricMarkers;
    private TextView tvNormalMap, tvSatelliteMap;
    private boolean isNormalMap = true;
    private LinearLayout llListBtn, llPie;
    private WebView wvElectricPie;
    private ImageView ivOffline;
    private View lastView = null;
    private LinearLayout llElectricList;
    private LinearLayout llInfo;
    private TextView tvCurrent;
    private TextView tvTemp;
    private ImageView ivRow;
    private LinearLayout llTmp1, llTmp2, llTmp3, llTmp4, llTmp5, llTmp6, llTmp7, llTmp8, llTmp9;
    private TextView tvUnitName, tvPosition;


    private String URL_GET_WATER_ELECTRIC = "http://jn.xiaofang365.cn/xfwlw/fireCommand/getElectric.php?communityID=";

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);

        initData();
        initView();
        HttpUtils.getJSON(URL_GET_WATER_ELECTRIC + communityID, getElectricHandler);

    }

    private void initData() {
        communityID = "16";
        SDKInitializer.initialize(getApplicationContext());


    }



    private void initView() {
        setContentView(R.layout.electric);

        tvUnitName = (TextView) findViewById(R.id.tvUnitName);
        tvPosition = (TextView) findViewById(R.id.tvPosition);

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

        ivOffline = (ImageView) findViewById(R.id.ivOffline);
        ivRow = (ImageView) findViewById(R.id.ivRow);

        llElectricList = (LinearLayout) findViewById(R.id.llElectricList);

        lvElectric = (ListView) findViewById(R.id.lvElectric);
        ElectricAdapter = new ElectricAdapter(this, ElectricList);
        lvElectric.setAdapter(ElectricAdapter);

        llInfo = (LinearLayout) findViewById(R.id.llInfo);
        tvCurrent = (TextView) findViewById(R.id.tvCurrent);
        tvTemp = (TextView) findViewById(R.id.tvTemp);

        llTmp1 = (LinearLayout) findViewById(R.id.llTmp1);
        llTmp2 = (LinearLayout) findViewById(R.id.llTmp2);
        llTmp3 = (LinearLayout) findViewById(R.id.llTmp3);
        llTmp4 = (LinearLayout) findViewById(R.id.llTmp4);
        llTmp5 = (LinearLayout) findViewById(R.id.llTmp5);
        llTmp6 = (LinearLayout) findViewById(R.id.llTmp6);
        llTmp7 = (LinearLayout) findViewById(R.id.llTmp7);
        llTmp8 = (LinearLayout) findViewById(R.id.llTmp8);
        llTmp9 = (LinearLayout) findViewById(R.id.llTmp9);
        lvElectric.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showElectricInfo(view, position);
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
                packUp();
                int i = marker.getZIndex();
                showElectricInfo(null, i);
                return false;
            }
        });

        wvElectricPie = (WebView) findViewById(R.id.wvElectricPie);
        wvElectricPie.loadUrl("http://jn.xiaofang365.cn/xfwlw/fireCommand/blank.php");

        WebSettings webSettingsElectric = wvElectricPie.getSettings();
        webSettingsElectric.setUseWideViewPort(true);
        webSettingsElectric.setLoadWithOverviewMode(true);
        webSettingsElectric.setJavaScriptEnabled(true);
        webSettingsElectric.setDomStorageEnabled(true);
        webSettingsElectric.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    private void showElectricInfo(View view, int position){
        tvUnitName.setText(ElectricList.get(position).getConstructionName());
        tvPosition.setText(ElectricList.get(position).getPosition());
        if (lastView != null) {
            lastView.setBackgroundColor(0XCCFFFFFF);
        }
        if(view!=null){
            view.setBackgroundColor(0XFFFFEECC);
            lastView = view;
        }
        String lat = ElectricList.get(position).getLat();
        String lon = ElectricList.get(position).getLon();
        mapTools.setCenterAndZoom(baiduMap, lat, lon, 18);
        llPie.setVisibility(View.GONE);
        llInfo.setVisibility(View.VISIBLE);
        if ((System.currentTimeMillis() / 1000 - Long.parseLong(ElectricList.get(position).getLastTime())) > 3600) {
            ivOffline.setVisibility(View.VISIBLE);
            return;
        } else {
            ivOffline.setVisibility(View.GONE);
        }
        if (Double.parseDouble(ElectricList.get(position).getCurrent()) < Double.parseDouble(ElectricList.get(position).getMaxCurrent())) {
            tvCurrent.setTextColor(Color.BLUE);
        } else {
            tvCurrent.setTextColor(Color.RED);
        }

        if (Double.parseDouble(ElectricList.get(position).getTemp()) < Double.parseDouble(ElectricList.get(position).getMaxTemp())) {
            tvTemp.setTextColor(Color.BLUE);
        } else {
            tvTemp.setTextColor(Color.RED);
        }

        tvCurrent.setText(ElectricList.get(position).getCurrent() + "A");
        Double current = Double.parseDouble(ElectricList.get(position).getCurrent());
        final float ang = (float) ((current / 30) * 90 - 45);
        new Thread() {
            public void run() {
                try {
                    for (int i = 0; i < (ang + 45); i++) {
                        Message msg = new Message();
                        msg.arg1 = i;
                        msg.arg2 = 1;
                        handler.sendMessage(msg);
                        sleep(10);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();


        tvTemp.setText(ElectricList.get(position).getTemp() + "℃");
        llTmp1.setBackgroundColor(0XFFCCCCCC);
        llTmp2.setBackgroundColor(0XFFCCCCCC);
        llTmp3.setBackgroundColor(0XFFCCCCCC);
        llTmp4.setBackgroundColor(0XFFCCCCCC);
        llTmp5.setBackgroundColor(0XFFCCCCCC);
        llTmp6.setBackgroundColor(0XFFCCCCCC);
        llTmp7.setBackgroundColor(0XFFCCCCCC);
        llTmp8.setBackgroundColor(0XFFCCCCCC);
        llTmp9.setBackgroundColor(0XFFCCCCCC);

        Double tmp = Double.parseDouble(ElectricList.get(position).getTemp());
        final int nTmp = (int) (tmp / 10 + 1);
        new Thread() {
            public void run() {
                try {
                    for (int i = 0; i < nTmp; i++) {
                        Message msg = new Message();
                        msg.arg1 = i;
                        msg.arg2 = 2;
                        handler.sendMessage(msg);
                        sleep(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg2 == 1) {
                int ang = msg.arg1 - 44;
                ivRow.setRotation(ang);
            } else if (msg.arg2 == 2) {
                switch (msg.arg1) {
                    case 1:
                        llTmp1.setBackgroundColor(0XFF99CC33);
                        break;
                    case 2:
                        llTmp2.setBackgroundColor(0XFF99CC33);
                        break;
                    case 3:
                        llTmp3.setBackgroundColor(0XFF99CC33);
                        break;
                    case 4:
                        llTmp4.setBackgroundColor(0XFFFF9900);
                        break;
                    case 5:
                        llTmp5.setBackgroundColor(0XFFFF9900);
                        break;
                    case 6:
                        llTmp6.setBackgroundColor(0XFFFF9900);
                        break;
                    case 7:
                        llTmp7.setBackgroundColor(0XFFFF6666);
                        break;
                    case 8:
                        llTmp8.setBackgroundColor(0XFFFF6666);
                        break;
                    case 9:
                        llTmp9.setBackgroundColor(0XFFFF6666);
                        break;
                    default:
                        break;
                }
            }
        }
    };

    private Handler getElectricHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ElectricList.clear();
            ElectricMarkers = null;
            String jsonData = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                ElectricMarkers = new Marker[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String id = object.getString("id");
                    String constructionName = object.getString("constructionName");
                    String position = object.getString("position");
                    String current = object.getString("current");
                    String maxCurrent = object.getString("maxCurrent");
                    String temp = object.getString("temp");
                    String maxTemp = object.getString("maxTemp");
                    String lat = object.getString("lat");
                    String lon = object.getString("lon");
                    String lastTime = object.getString("lastTime");
                    addElectricMarker(lat, lon, lastTime, constructionName, i, current, maxCurrent, temp, maxTemp);
                    ElectricList.add(new ElectricItem(id, constructionName, position, current, maxCurrent, temp, maxTemp, lat, lon, lastTime));
                }
                ElectricAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private void addElectricMarker(String lat, String lon, String lastTime, String name, int i, String current, String maxCurrent, String temp, String maxTemp) {
        BitmapDescriptor bitmap = null;


        //定义Maker坐标点
        LatLng point = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));


        long timeNow = System.currentTimeMillis() / 1000;
        if (timeNow - Long.parseLong(lastTime) < 3600) {
            if ((Double.parseDouble(current)) < (Double.parseDouble(maxCurrent)) && Double.parseDouble(temp) < (Double.parseDouble(maxTemp))) {
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.electric_normal);
            } else {
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.electric_abnormal);
            }

        } else {
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.electric_offline);
        }
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap)
                .animateType(MarkerOptions.MarkerAnimateType.grow);
        //在地图上添加Marker，并显示

        ElectricMarkers[i] = (Marker) baiduMap.addOverlay(option);
        ElectricMarkers[i].setTitle(name);
        ElectricMarkers[i].setZIndex(i);

    }

    private void packUp(){
        ivPackUp.setVisibility(View.GONE);
        llListBtn.setVisibility(View.VISIBLE);
        llPie.setVisibility(View.GONE);
        llElectricList.setVisibility(View.GONE);
        if (lastView != null) {
            lastView.setBackgroundColor(0XCCFFFFFF);
            lastView = null;
        }
        llInfo.setVisibility(View.GONE);
        wvElectricPie.loadUrl("http://jn.xiaofang365.cn/xfwlw/fireCommand/blank.php");
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
            wvElectricPie.reload();
            llElectricList.setVisibility(View.VISIBLE);
            wvElectricPie.loadUrl("http://jn.xiaofang365.cn/xfwlw/fireCommand/electricPie.php?communityID=" + communityID);
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
            if(llElectricList.getVisibility()==View.VISIBLE || llInfo.getVisibility()==View.VISIBLE){
                packUp();
            }else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}

