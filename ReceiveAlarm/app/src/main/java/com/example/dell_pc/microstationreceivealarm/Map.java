package com.example.dell_pc.microstationreceivealarm;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell_pc.microstationreceivealarm.adapter.UnitAdapter;
import com.example.dell_pc.microstationreceivealarm.item.CarItem;
import com.example.dell_pc.microstationreceivealarm.item.ConstructionItem;
import com.example.dell_pc.microstationreceivealarm.item.UnitItem;
import com.example.dell_pc.microstationreceivealarm.tools.HttpUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//import static com.example.dell_pc.microstationreceivealarm.R.drawable.pic001;
//import static com.example.dell_pc.microstationreceivealarm.R.drawable.pic002;


/**
 * Created by dell-pc on 2017/7/28.
 */
public class Map extends Activity implements View.OnClickListener {
    private String communityID = "3";//经安纬固 13  k88 3
    private String fireStationID = "4";
//    private LinearLayout llPic;
    private FrameLayout llConstruction;
    private FrameLayout flCar;
    private ImageView  ivPackUp1, ivPackUp2, ivConsPic, ivCarPic, ivDriverPhone, ivMasterPhone;
    private TextView tvConsName, tvConsAddress, tvConsType, tvConsStructure, tvConsArea, tvConsHeight, tvConsFac, tvConsGoods;
    private TextView tvCarName, tvMaterial, tvWeight, tvLevel, tvDriver, tvMaster, tvEquipment;
    private TextView tvConsCancel;
    private WebView webView;
    private Button btnReceive, btnClear, btnShowTrack, btnCarPackUp;
    private ListView lvUnit;
//    private Bitmap[] bitmaps = new Bitmap[2];
    private int nCarIndex;

    public MediaPlayer mediaPlayer = new MediaPlayer();
    boolean isReceived = false;
    boolean isFire = false;
    private Handler myHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what==1080){//有值报警

            }else{//没有值

                clearAlarm();
                flCar.setVisibility(View.GONE);
                btnShowTrack.setVisibility(View.GONE);
                finish();
            }




        }
    };



    private CarLevelThread carLevelThread = new CarLevelThread();

    private List<ConstructionItem> constructionList = new ArrayList<ConstructionItem>();
    private List<CarItem> carList = new ArrayList<CarItem>();
    private List<UnitItem> unitList = new ArrayList<UnitItem>();

    private UnitAdapter unitAdapter;

    private String URL_GET_FIRE = "http://jn.xiaofang365.cn/xfwlw/fireCommand/findFireAlarm.php?communityID=" + communityID;
    private String URL_GET_CARS_INFO = "http://jn.xiaofang365.cn/xfwlw/fireCommand/getCarsInfo.php?stationID=" + fireStationID;
    private String URL_GET_LEVEL = "http://jn.xiaofang365.cn/xfwlw/fireCommand/getCarLevel.php?carID=";
    private String URL_GET_UNITS_INFO = "http://jn.xiaofang365.cn/xfwlw/fireCommand/getUnitsInfo.php?constructionID=";
    private String URL_CHECK_ALARM="http://dndzl.cn/daping/fire.php?unit_id=328";
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)





    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





//        bitmaps[0] = BitmapFactory.decodeResource(getResources(), pic001);
//        bitmaps[1] = BitmapFactory.decodeResource(getResources(), pic002);

        flCar = (FrameLayout) findViewById(R.id.flCar);
//        llPic = (LinearLayout) findViewById(R.id.llPic);
        llConstruction = (FrameLayout) findViewById(R.id.llConstruction);


//        ivPic = (ImageView) findViewById(R.id.ivPic);
        ivPackUp1 = (ImageView) findViewById(R.id.ivPackUp1);
        ivPackUp1.setOnClickListener(this);

        webView = (WebView) findViewById(R.id.webView);
        btnReceive = (Button) findViewById(R.id.btnReceive);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnReceive.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        tvConsName = (TextView) findViewById(R.id.tvConsName);
        tvConsAddress = (TextView) findViewById(R.id.tvConsAddress);
        tvConsType = (TextView) findViewById(R.id.tvConsType);
        tvConsStructure = (TextView) findViewById(R.id.tvConsStructure);
        tvConsArea = (TextView) findViewById(R.id.tvConArea);
        tvConsHeight = (TextView) findViewById(R.id.tvConHeight);
        tvConsFac = (TextView) findViewById(R.id.tvConsFac);
        tvConsGoods = (TextView) findViewById(R.id.tvConsGoods);
        ivConsPic = (ImageView) findViewById(R.id.ivConsPic);

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
        btnShowTrack = (Button) findViewById(R.id.btnShowTrack);
        btnCarPackUp.setOnClickListener(this);
        ivMasterPhone.setOnClickListener(this);
        ivDriverPhone.setOnClickListener(this);
        btnShowTrack.setOnClickListener(this);

        tvConsCancel = (TextView) findViewById(R.id.tvConsCancel);
        tvConsCancel.setOnClickListener(this);

        lvUnit = (ListView) findViewById(R.id.lvUnit);
        unitAdapter = new UnitAdapter(Map.this,unitList);
        lvUnit.setAdapter(unitAdapter);

        webView = (WebView) findViewById(R.id.webView);
//        webView.loadUrl("http://jn.xiaofang365.cn/xfwlw/fireCommand/fireMap_new.php?communityID=" + communityID + "&fireStationID=" + fireStationID);
//        webView.loadUrl("http://dndzl.cn/alarm_map/car.html");
//        webView.loadUrl("http://dndzl.cn/alarm_map/electricCars.html");//经安纬固
        webView.loadUrl("http://ali.fis119.com/alarm_map/fangyuhu.html");//k88


        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(new Contact(), "FireAlarm");
        changePic();
        carLevelThread.start();

        mediaPlayer = MediaPlayer.create(Map.this, R.raw.alarm8);


        new Thread(){
            public void run(){
                while (true) {
                    try {
                        sleep(1000);
//                        if (isFire == true && isReceived == false) {

//                            getAlarmJSON(URL_CHECK_ALARM,myHandler);
                            getAlarmJSON(URL_CHECK_ALARM,myHandler);

                            if(mediaPlayer == null){

                                return;
                            }

                            if(mediaPlayer.isPlaying() == false){

                                mediaPlayer.start();
                            }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();

    }

    private void changePic() {
        new MyThread().start();
    }

    class MyThread extends Thread {
        public void run() {
            try {
                int i = 0;
                while (i < 2) {
                    sleep(4000);
                    Message msg = new Message();
                    msg.arg1 = i;
                    handler.sendMessage(msg);
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

    class CarLevelThread extends Thread {
        public void run() {
            try {
                while (true) {
                    sleep(1000);
                    if(flCar.getVisibility()==View.VISIBLE){
                        HttpUtils.getJSON(URL_GET_LEVEL + carList.get(nCarIndex).getID(), getCarLevelHandler);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }




    private final class Contact {


        public void clearAlarm() {
            // 调用JS中的方法
            webView.loadUrl("javascript:clearAlarm()");
//            webView.loadUrl("javascript:custom_close()");
        }







        @JavascriptInterface
        public void playAlarmSound() {
            isFire = true;
            Message msg = new Message();
            msg.arg1 = 1000;
            handler.sendMessage(msg);
        }

        @JavascriptInterface
        public void showCarInfo(String str) {
            Message msg = new Message();
            msg.arg1 = 2000;
            msg.arg2 = Integer.parseInt(str);
            handler.sendMessage(msg);
        }

        @JavascriptInterface
        public void showConstructionInfo(String str) {
            Message msg = new Message();
            msg.arg1 = 3000;
            msg.arg2 = Integer.parseInt(str) / 100 - 1;
            handler.sendMessage(msg);
        }
    }



    public void getAlarmJSON(final String url,final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn;
                InputStream is;
                try {
                    conn = (HttpURLConnection) new URL(url).openConnection();
                    conn.setRequestMethod("GET");
                    is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line = "";
                    StringBuilder result = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                   if(result.toString().length()>10) {

                       handler.sendEmptyMessage(1080);
                   }else {
                      handler.sendEmptyMessage(1090);

                   }



                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private Handler getConstructionHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String jsonData = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
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
                    constructionList.add(new ConstructionItem(id, name, address, type, structure, area, layer, height, fac, goods, picUrl));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private Handler getCarsHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String jsonData = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String id = object.getString("carID");
                    String name = object.getString("carName");
                    String materialType = object.getString("materialType");
                    String weight = object.getString("weight");
                    String driver = object.getString("driver");
                    String driverPhone = object.getString("driverPhone");
                    String master = object.getString("master");
                    String masterPhone = object.getString("masterPhone");
                    String equipment = object.getString("equipment");
                    String picUrl = object.getString("pic");
                    String level = object.getString("level");
                    String lat = object.getString("latitude");
                    String lon = object.getString("longitude");
                    carList.add(new CarItem(id, name, materialType, weight, driver, driverPhone, master, masterPhone, equipment, picUrl, level, lat, lon, null));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private Handler getCarLevelHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JSONObject object = null;
            try {
                object = new JSONObject((String) msg.obj);
                String level = object.getString("level");
                tvLevel.setText(level + "%");
                int nLevel = Integer.parseInt(level);
                if(nLevel>=80){
                    tvLevel.setTextColor(0XFF99CCFF);
                }else if(nLevel>=60){
                    tvLevel.setTextColor(0XFFCCFF99);
                }else if(nLevel>=40){
                    tvLevel.setTextColor(0XFFFFFF00);
                }else if(nLevel>=20){
                    tvLevel.setTextColor(0XFFFF9900);
                }else {
                    tvLevel.setTextColor(0XFFFF6666);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    private Handler getConsPicHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bitmap bitmap = (Bitmap) msg.obj;
            ivConsPic.setImageBitmap(bitmap);
        }
    };
    private Handler getCarPicHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bitmap bitmap = (Bitmap) msg.obj;
        }
    };


    private Handler getUnitsHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int i = msg.arg1;
            if (i == 1000) {
//                llPic.setVisibility(View.GONE);

                HttpUtils.getJSON(URL_GET_FIRE, getConstructionHandler);
                HttpUtils.getJSON(URL_GET_CARS_INFO, getCarsHandler);
            } else if (i == 2000) {
                int position = msg.arg2;
                nCarIndex = position;
                flCar.setVisibility(View.VISIBLE);
                tvCarName.setText(carList.get(position).getName());
                tvMaterial.setText(carList.get(position).getMaterialType());
                tvWeight.setText(carList.get(position).getWeight() + "吨");
                tvMaster.setText(carList.get(position).getMaster());
                tvDriver.setText(carList.get(position).getDriver());
                tvEquipment.setText(carList.get(position).getEquipment());
                HttpUtils.getPicBitmap(carList.get(position).getPicUrl(), getCarPicHandler);
            } else if (i == 3000) {
                llConstruction.setVisibility(View.VISIBLE);
                int position = msg.arg2;
                tvConsName.setText(constructionList.get(position).getName());
                tvConsAddress.setText(constructionList.get(position).getAddress());
                tvConsType.setText(constructionList.get(position).getType());
                tvConsStructure.setText(constructionList.get(position).getStructure());
                tvConsArea.setText(constructionList.get(position).getArea() + "平方米");
                tvConsHeight.setText(constructionList.get(position).getLayer() + "层，" + constructionList.get(position).getHeight() + "米");
                tvConsFac.setText(constructionList.get(position).getFac());
                tvConsGoods.setText(constructionList.get(position).getGoods());
                String constructionID = constructionList.get(position).getID();
                HttpUtils.getJSON(URL_GET_UNITS_INFO + constructionID, getUnitsHandler);
                HttpUtils.getPicBitmap(constructionList.get(position).getPicUrl(), getConsPicHandler);
            } else {
//                ivPic.setImageBitmap(bitmaps[i]);
            }
        }
    };


    @Override
    public void onClick(View view) {
        if (view == btnReceive) {
//            isReceived = true;

            if(mediaPlayer!=null&mediaPlayer.isPlaying()){


                mediaPlayer.stop();

            }



        } else if (view == btnClear) {

            AlertDialog.Builder multiDia = new AlertDialog.Builder(Map.this);
            multiDia.setTitle("您确定火警已消除？");
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
                    clearAlarm();
                    flCar.setVisibility(View.GONE);
                    btnShowTrack.setVisibility(View.GONE);
                    finish();
                }
            });
            multiDia.create().show();
        } else if (view == btnCarPackUp) {
            flCar.setVisibility(View.GONE);
            tvLevel.setText("");
        } else if (view == ivPackUp1) {
            llConstruction.setVisibility(View.GONE);
        } else if (view == ivPackUp2) {
        } else if (view == ivMasterPhone) {
            String inputStr = carList.get(nCarIndex).getMasterPhone();
            callPhone(inputStr);
        } else if (view == ivDriverPhone) {
            String inputStr = carList.get(nCarIndex).getDriverPhone();
            callPhone(inputStr);
        }else if(view == tvConsCancel){
            llConstruction.setVisibility(View.GONE);
        }else if(view == btnShowTrack){
            startActivity(new Intent(Map.this, TrackMap.class));
        }

    }

    public void callPhone(final String inputStr) {
        if (inputStr.trim().length() != 0) {
            AlertDialog.Builder multiDia = new AlertDialog.Builder(Map.this);
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

    private void clearAlarm() {

        String url = "http://jn.xiaofang365.cn/xfwlw/fireCommand/clearFireAlarm.php";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                Toast.makeText(Map.this, "火警已消除!", Toast.LENGTH_LONG).show();
                new Contact().clearAlarm();
                isReceived = false;
                isFire = false;
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(Map.this, "网络故障，火警未消除！", Toast.LENGTH_LONG).show();
            }

        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
