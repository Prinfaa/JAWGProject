package com.example.dell_pc.microstationreceivealarm.tools;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by dell-pc on 2017/8/5.
 */
public class MapTools {
    public void setCenterAndZoom(BaiduMap baiduMap, String lat, String lon, int nZoom){
        LatLng cenpt = new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(nZoom)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化


        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        baiduMap.setMapStatus(mMapStatusUpdate);

    }
}
