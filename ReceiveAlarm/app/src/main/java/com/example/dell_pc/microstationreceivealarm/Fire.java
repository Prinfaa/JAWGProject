package com.example.dell_pc.microstationreceivealarm;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

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
import com.example.dell_pc.microstationreceivealarm.item.ConstructionItem;
import com.example.dell_pc.microstationreceivealarm.tools.HttpUtils;
import com.example.dell_pc.microstationreceivealarm.tools.MapTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell-pc on 2017/8/26.
 */
public class Fire extends Activity {

    private String communityID = "16";
    private MapView mapView;
    private BaiduMap map;
    private MapTools mapTools = new MapTools();
    boolean isReceived = false;
    boolean isFire = false;
    public MediaPlayer mediaPlayer = new MediaPlayer();


    private String URL_GET_FIRE = "http://jn.xiaofang365.cn/xfwlw/fireCommand/findFireAlarm.php?communityID=" + communityID;
    private List<ConstructionItem> constructionList = new ArrayList<ConstructionItem>();

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fire);

    }

    private Handler getConstructionHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String jsonData = (String) msg.obj;

            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                if(jsonArray.length()!=0){
                    mediaPlayer.start();
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String id = object.getString("constructionID");
                    String name = object.getString("constructionName");
                    String address = object.getString("address");
                    String type = object.getString("type");
                    String structure = object.getString("structure");
                    String area = object.getString("area");
                    String layer = object.getString("layer");
                    String height = object.getString("height");
                    String fac = object.getString("fac");
                    String goods = object.getString("goods");
                    String picUrl = object.getString("picUrl");
                    String lat = object.getString("lat");
                    String lon = object.getString("long");
                    constructionList.add(new ConstructionItem(id, name, address, type, structure, area, layer, height, fac, goods, picUrl));
                    isFire = true;
                    addFireMarker(i,lat,lon);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private void addFireMarker(int i, String lat, String lon){
        BitmapDescriptor bitmap = null;
        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.fire3);
        //定义Maker坐标点
        LatLng point = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));

        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap)
                .animateType(MarkerOptions.MarkerAnimateType.grow);
        //在地图上添加Marker，并显示

        Marker fireMarker = (Marker) map.addOverlay(option);

    }


}
