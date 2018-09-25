package com.example.dell_pc.microstationreceivealarm;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

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
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.dell_pc.microstationreceivealarm.item.CarGPSHistoryItem;
import com.example.dell_pc.microstationreceivealarm.tools.HttpUtils;
import com.example.dell_pc.microstationreceivealarm.tools.MapTools;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by dell-pc on 2017/7/28.
 */
public class TrackMap extends Activity  {
    private String URL_GET_CAR_GPS_HISTORY = "http://jn.xiaofang365.cn/xfwlw/fireCommand/getCarGpsHistory.php?carID=0004";
    private SeekBar seekBar;
    private TextView tvBarTime;
    private MapView mapViewTrack = null;
    private BaiduMap mapTrack = null;
    private Marker markerPosition;
    private List<CarGPSHistoryItem> fireCarGPSHistorList = new ArrayList<CarGPSHistoryItem>();
    private MapTools mapTools = new MapTools();




    private Handler getCarGPSHistoryHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String jsonData = (String) msg.obj;
            LatLng pointLast = null;
            int lastID = 0;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String id = object.getString("id");
                    String lat = object.getString("lat");
                    String lon = object.getString("long");
                    String dateTime = object.getString("time");
                    LatLng p = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                    if (i != 0 && DistanceUtil.getDistance(pointLast, p) < 50 * (i - lastID)) {
                        fireCarGPSHistorList.add(new CarGPSHistoryItem(id, null, null, lat, lon, dateTime));
                        pointLast = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                        lastID = i;
                    }
                }
                seekBar.setMax(fireCarGPSHistorList.size()-1);//设置最大刻度
                clearPoint();
                showMapTrack();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private void clearPoint() {
        for (int i = 0; i < fireCarGPSHistorList.size(); i++) {
            if (i != 0 && i != (fireCarGPSHistorList.size() - 1)) {
                LatLng pointA = new LatLng(Double.parseDouble(fireCarGPSHistorList.get(i-1).getLat()),Double.parseDouble(fireCarGPSHistorList.get(i-1).getLon()));
                LatLng pointB = new LatLng(Double.parseDouble(fireCarGPSHistorList.get(i).getLat()),Double.parseDouble(fireCarGPSHistorList.get(i).getLon()));
                LatLng pointC = new LatLng(Double.parseDouble(fireCarGPSHistorList.get(i+1).getLat()),Double.parseDouble(fireCarGPSHistorList.get(i+1).getLon()));
                Double c = DistanceUtil.getDistance(pointA, pointB);
                Double b = DistanceUtil.getDistance(pointA, pointC);
                Double a = DistanceUtil.getDistance(pointB, pointC);

                Double Ang = Math.cos(Math.cos((a*a + c*c - b*b)/(2*a*c)));
                if(Ang<0.85){
                    fireCarGPSHistorList.get(i+1).setLat(String.valueOf((Double.parseDouble(fireCarGPSHistorList.get(i).getLat()) + Double.parseDouble(fireCarGPSHistorList.get(i+2).getLat()))/2));
                    fireCarGPSHistorList.get(i+1).setLon(String.valueOf((Double.parseDouble(fireCarGPSHistorList.get(i).getLon()) + Double.parseDouble(fireCarGPSHistorList.get(i+2).getLon()))/2));
                }

                System.out.println("Ang==" + Ang);
            }
        }
    }


    private void addCarPositionMarkers(String lat, String lon) {
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.fire_car_icon1);
        //定义Maker坐标点
        LatLng point = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));

        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap)
                .animateType(MarkerOptions.MarkerAnimateType.grow);
        //在地图上添加Marker，并显示

        markerPosition = (Marker) mapTrack.addOverlay(option);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
                tvBarTime.setText(fireCarGPSHistorList.get(seekBar.getProgress()).getDateTime());
                Double dLat = Double.parseDouble(fireCarGPSHistorList.get(seekBar.getProgress()).getLat());
                Double dLon = Double.parseDouble(fireCarGPSHistorList.get(seekBar.getProgress()).getLon());
                LatLng p = new LatLng(dLat, dLon);
                markerPosition.setPosition(p);
                mapTools.setCenterAndZoom(mapTrack, fireCarGPSHistorList.get(seekBar.getProgress()).getLat(), fireCarGPSHistorList.get(seekBar.getProgress()).getLon(), 18);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {//开始拖动
                tvBarTime.setText(fireCarGPSHistorList.get(seekBar.getProgress()).getDateTime());
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {//结束拖动
                tvBarTime.setText(fireCarGPSHistorList.get(seekBar.getProgress()).getDateTime());
            }
        });

    }


    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track);

        tvBarTime = (TextView) findViewById(R.id.tvBarTime);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        //获取地图控件引用
        mapViewTrack = (MapView) findViewById(R.id.mapTrack);
        mapTrack = mapViewTrack.getMap();
        //卫星地图
        mapTrack.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        HttpUtils.getJSON(URL_GET_CAR_GPS_HISTORY, getCarGPSHistoryHandler);

    }

    private void showMapTrack() {
        mapTools.setCenterAndZoom(mapTrack, fireCarGPSHistorList.get(0).getLat(), fireCarGPSHistorList.get(0).getLon(), 18);
        MapStatus mapStatus = new MapStatus.Builder().overlook(-180).build();
        MapStatusUpdate msu = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mapTrack.setMapStatus(msu);

        List<LatLng> points = new ArrayList<LatLng>();
        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < fireCarGPSHistorList.size(); i++) {
            System.out.println("______" + fireCarGPSHistorList.get(i).getLat());
            points.add(new LatLng(Double.parseDouble(fireCarGPSHistorList.get(i).getLat()), Double.parseDouble(fireCarGPSHistorList.get(i).getLon())));
            colors.add(Integer.valueOf(Color.RED));
        }

        OverlayOptions ooPolyline = new PolylineOptions().width(15).color(0XFFFF6666).points(points);
        Polyline mPolyline = (Polyline) mapTrack.addOverlay(ooPolyline);
        addCarPositionMarkers(fireCarGPSHistorList.get(0).getLat(),fireCarGPSHistorList.get(0).getLon());
    }





}
