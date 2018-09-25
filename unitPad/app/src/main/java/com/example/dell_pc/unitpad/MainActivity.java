package com.example.dell_pc.unitpad;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.common.SysOSUtil;
import com.example.dell_pc.unitpad.adapter.AlarmDectorAdapter;
import com.example.dell_pc.unitpad.adapter.Checked1Adapter;
import com.example.dell_pc.unitpad.adapter.DocumentPicAdapter;
import com.example.dell_pc.unitpad.adapter.FacAdapter;
import com.example.dell_pc.unitpad.adapter.FacSmokeFeelAdapter;
import com.example.dell_pc.unitpad.adapter.HorizontalListViewAdapter;
import com.example.dell_pc.unitpad.adapter.MonthAdapter;
import com.example.dell_pc.unitpad.adapter.PatroledAdapter;
import com.example.dell_pc.unitpad.adapter.UnCheckAdapter;
import com.example.dell_pc.unitpad.adapter.UnPatrolAdapter;
import com.example.dell_pc.unitpad.charts.DrawBarTraining;
import com.example.dell_pc.unitpad.charts.DrawPieElectric;
import com.example.dell_pc.unitpad.charts.DrawPieAlarm;
import com.example.dell_pc.unitpad.charts.DrawPieCheck;
import com.example.dell_pc.unitpad.charts.DrawPieLevel;
import com.example.dell_pc.unitpad.charts.DrawPiePatrol;
import com.example.dell_pc.unitpad.charts.DrawPiePress;
import com.example.dell_pc.unitpad.item.CheckedItem;
import com.example.dell_pc.unitpad.item.DeviceItem;
import com.example.dell_pc.unitpad.item.DocumentPicItem;
import com.example.dell_pc.unitpad.item.MonthItem;
import com.example.dell_pc.unitpad.item.PatroledItem;
import com.example.dell_pc.unitpad.item.ShopCheckedItem;
import com.example.dell_pc.unitpad.item.ShopProblemDealedItem;
import com.example.dell_pc.unitpad.item.ShopProblemNotDealItem;
import com.example.dell_pc.unitpad.item.ShopUnCheckedItem;
import com.example.dell_pc.unitpad.item.UnCheckItem;
import com.example.dell_pc.unitpad.item.UnPatrolItem;
import com.example.dell_pc.unitpad.tools.DownLoadManager;
import com.example.dell_pc.unitpad.tools.HttpUtils;
import com.example.dell_pc.unitpad.tools.MyAnimation;
import com.example.dell_pc.unitpad.tools.MyWebViewSet;
import com.example.dell_pc.unitpad.view.HorizontalListView;
import com.ezvizuikit.open.EZUIError;
import com.ezvizuikit.open.EZUIKit;
import com.ezvizuikit.open.EZUIPlayer;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static com.videogo.ezlog.EZlogHttpUtil.dealResponseResult;


public class MainActivity extends Activity implements EZUIPlayer.EZUIPlayerCallBack, View.OnClickListener {

    private String cityID = "370100";
    private String unitID, unitName;


    private WebView webViewAssessUp, webViewAssessDown;
    private String urlAssessUp = "http://jn.xiaofang365.cn/xfwlw/xfwlw/assessRight.php";
    private String urlAssessDown = "http://jn.xiaofang365.cn/xfwlw/xfwlw/assess.php";


    public String URL_GET_PATROL_CARS = "http://dndzl.cn/daping/patrol_record.php?unitID=";
    public String URL_GET_CHECK_CARS = "http://dndzl.cn/daping/monthlyTest.php?unitID=";
    public String URL_GET_SHOP_CARS = "http://dndzl.cn/daping/shopAll.php?parentID=";
    public String URL_GET_PROBLEMS = "http://dndzl.cn/daping/shopProblem.php?parentID=";
    //    public String URL_GET_FIRE_ALARM_FAC_STATUE = "http://116.62.239.16/LSFPWEB/rest/platformInterface/platformData/getDeviceByInfo/all/1/1000";
    public String URL_GET_FIRE_ALARM_FAC_STATUE = "http://dndzl.cn/push/smoke_detector.php?company_id=";
    //报警探测器
    private String URL_ALARM_DETECTOR="http://dndzl.cn/push/alarm_detection.php?company_id=";


    public static List<UnPatrolItem> unPatrolItemList = new ArrayList<UnPatrolItem>();
    public static List<PatroledItem> patroledItemList = new ArrayList<PatroledItem>();

    public static List<UnCheckItem> unCheckList = new ArrayList<UnCheckItem>();
    public static List<CheckedItem> checkedNormalList = new ArrayList<CheckedItem>();
    public static List<CheckedItem> checkAbnormalList = new ArrayList<CheckedItem>();

    private UnPatrolAdapter unPatrolAdapter;
    private PatroledAdapter patroledAdapter;

    private UnCheckAdapter unCheckAdapter;
    private Checked1Adapter checkedNormalAdapter, checkAbnormalAdapter;

    private ListView lvPatrol, lvUnPatrol;
    private ListView lvUnCheck, lvCheckNormal, lvCheckAbnormal;
    private ImageView ivUp, ivDown, ivLeft, ivRight;
    private LinearLayout llPlayBig;
    private LinearLayout llPlayer;
    private LinearLayout llAssess, llElectric, llWater, llAlarm, llCheckPatrol, llTrainingDrill, llVideo, llMonth, llShopCheck;
    private Button btnBack;
    private TextView tvShopCount, tvShopCheckedCount, tvShopUnCheckedCount, tvProblemCount, tvDealCount, tvUnDealCount;

    //    private LinearLayout llPic;
    private List<DocumentPicItem> fireCheckPicList = new ArrayList<DocumentPicItem>();
    private List<DocumentPicItem> electricCheckPicList = new ArrayList<DocumentPicItem>();
    private List<DocumentPicItem> fireAssessPicList = new ArrayList<DocumentPicItem>();
    private List<DocumentPicItem> maintenancePicList = new ArrayList<DocumentPicItem>();
    private List<DocumentPicItem> maintenanceMonthPicList = new ArrayList<DocumentPicItem>();
    private List<DocumentPicItem> maintenanceOneMonthPicList = new ArrayList<DocumentPicItem>();

    private DocumentPicAdapter documentPicAdapter;
    //    private String URL_GET_DOCUMENT_PICS = "http://jn.xiaofang365.cn/xfwlw/xfwlw/getDocumentPic?unitID=232";
    private String URL_GET_DOCUMENT_PICS = "http://dndzl.cn/daping/logSheet.php";

    private String URL_GET_CAMERA_INFO = "http://jn.xiaofang365.cn/xfwlw/xfwlw/getCameraInfo?account=18615600710&typeID=1&unitID=";


    private PieChart piePatrol, pieCheck, pieWaterPress, pieWaterLevel, pieAlarm, pieElectric;
    private BarChart barTraining;

    private MyThread myThread = new MyThread();
    private MyAnimation myAnimation = new MyAnimation();

    private ListView lvMonth;
    private List<MonthItem> monthItemList = new ArrayList<MonthItem>();
    private MonthAdapter monthAdapter;

    private DrawPiePatrol drawPiePatrol = new DrawPiePatrol();
    private DrawPieCheck drawPieCheck = new DrawPieCheck();
    private DrawPiePress drawPiePress = new DrawPiePress();
    private DrawPieLevel drawPieLevel = new DrawPieLevel();
    private DrawPieAlarm drawPieAlarm = new DrawPieAlarm();
    private DrawPieElectric drawElectric = new DrawPieElectric();

    private DrawBarTraining drawBarTraining = new DrawBarTraining();

    private String URL_GET_EZUI_ACCESS_TOKEN = "https://open.ys7.com/api/lapp/token/get";

    private EZUIPlayer mPlayer, playerBig;
    private String accessToken;
    private String mUrl;
    private String URL_PLAY_HEAD = "ezopen://open.ys7.com/";
    private String URL_PLAY_TAIL = "/1.hd.live";

    //    private String URL_GET_FAC_INFO = "http://dndzl.cn/daping/zhongheng.php";
    private String URL_GET_FAC_INFO = "http://dndzl.cn/daping/syntheticData.php?unitID=";



    private TextView tvFireCheck, tvElectricCheck, tvFireAssess, tvMaintenance, tvMaintenanceMonth,tvAlarmDetector;
    private LinearLayout llDocument;
    private HorizontalListView hsPic;
    public static List<DeviceItem> deviceList = new ArrayList<DeviceItem>();
    public static List<DeviceItem> waterPressList = new ArrayList<DeviceItem>();
    public static List<DeviceItem> waterLevelList = new ArrayList<DeviceItem>();
    public static List<DeviceItem> electricList = new ArrayList<DeviceItem>();
    public static List<DeviceItem> fireAlarmList = new ArrayList<>();
    public static List<DeviceItem> AlarmDectorList = new ArrayList<>();

    public static List<ShopCheckedItem> shopCheckedItemList = new ArrayList<ShopCheckedItem>();
    public static List<ShopUnCheckedItem> shopUnCheckedItemList = new ArrayList<ShopUnCheckedItem>();

    public static List<ShopProblemDealedItem> shopProblemDealedItemList = new ArrayList<ShopProblemDealedItem>();
    public static List<ShopProblemNotDealItem> shopProblemNotDealItemList = new ArrayList<ShopProblemNotDealItem>();

    private String localVersion;
    private URL downLoadNewVerUrl;

    private TextView tvAllFac, tvPress, tvLevel, tvFireAlarm, tvElectric;
    private ListView lvFac;
    private FacAdapter facAdapter;

    private int ALL_FAC = 1;        //全部设备
    private int PRESS_FAC = 2;      //水压设备
    private int LEVEL_FAC = 3;      //水位设备
    private int ALARM_FAC = 4;      //报警设备
    private int ELECTRIC_FAC = 5;   //电气监测

    private int listDeviceType = ALL_FAC;
    private LinearLayout llList;

    private float scoreTotal = 0;
    private float scoreWaterPress = 15;
    private float scoreWaterLevel = 15;
    private float scoreFireAlarm = 15;
    private float scoreElectric = 15;
    private float scoreCheck = 10;
    private float scorePatrol = 5;
    private float scoreDayToDay = 0;

    private int nWorkReceived = 1;
    boolean isFire = false;
    boolean isK88Fire = false;
    boolean isFront = true;
    boolean isAct = true;

    public Resources getResources() {
        // TODO Auto-generated method stub
        //屏幕尺寸自适应
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkVer();
        initData();
        initView();

        HttpUtils.getJSON(URL_GET_DOCUMENT_PICS + "?unitID=" + unitID, getDocumentPicRecordHandler);

        drawPieAlarm.draw(pieAlarm, MainActivity.this, 4219, 1233, 663);
        drawElectric.draw(pieElectric, MainActivity.this, 5, 2);
        drawBarTraining.draw(barTraining);
        final String url = URL_GET_CAMERA_INFO + unitID;
        HttpUtils.getJSON(url, getCameraInfoHandler);

        myThread.start();

    }
    private Handler getFireAlarmStatueHandler = new Handler() {
        public void handleMessage(Message msg) {

            String jsonData = (String) msg.obj;

            try {
                JSONObject object = new JSONObject(jsonData);

                String code=object.getString("code");
                if(code.equals("200")){
                JSONArray jsonArray = object.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    String position = obj.getString("main_local");
                    String type = obj.getString("type");
                    String timestamp = obj.getString("times");

                    String facTypeID = "14";
                    String facType = obj.getString("device_type");
                    fireAlarmList.add(new DeviceItem("", facTypeID, facType, position, "", "", "", type, true, timestamp));

                }

                }

                FacSmokeFeelAdapter facsmokeAdapter = new FacSmokeFeelAdapter(MainActivity.this, fireAlarmList);
                lvFac.setAdapter(facsmokeAdapter);
                facsmokeAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ;
    };
    private Handler getAlarmDetactorHandler = new Handler() {
        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;

            try {
                JSONObject object = new JSONObject(jsonData);

                int code=object.getInt("code");

                if(code==200){

                    JSONArray jsonArray = object.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        String position = obj.getString("position");
                        String type = obj.getString("type");
                        String timestamp = obj.getString("timestamp");
                        String value=obj.getString("alarm_value");

//                        String facTypeID = "14";
                        String facType = obj.getString("alarm_desc");
                        AlarmDectorList.add(new DeviceItem("", "", facType, position, value, "", "", type, true, timestamp));


                    }

                }
                AlarmDectorAdapter alarmDectorAdapter = new AlarmDectorAdapter(MainActivity.this, AlarmDectorList);
                lvFac.setAdapter(alarmDectorAdapter);
                alarmDectorAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ;
    };


    private Handler getCameraInfoHandler = new Handler() {
        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;
            try {
                JSONObject object = new JSONObject(jsonData);
                String appKey = object.getString("appKey");
                String secret = object.getString("secret");
                String deviceID = object.getString("deviceID");
                HttpUtils.post(appKey, secret, URL_GET_EZUI_ACCESS_TOKEN, getEZUIAccessTokenHandler);
                initEZ(appKey);


                if (deviceID.equals("null")) {
                    mUrl = null;
                } else {
                    mUrl = URL_PLAY_HEAD + deviceID + URL_PLAY_TAIL;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ;
    };


    private void initView() {
        webViewAssessUp = (WebView) findViewById(R.id.webViewAssessUP);
        webViewAssessDown = (WebView) findViewById(R.id.webViewAssessDown);
        lvFac = (ListView) findViewById(R.id.lvFac);
        pieCheck = (PieChart) findViewById(R.id.pieCheck);
        piePatrol = (PieChart) findViewById(R.id.piePatrol);
        pieWaterPress = (PieChart) findViewById(R.id.pieWaterPress);
        pieWaterLevel = (PieChart) findViewById(R.id.pieWaterLevel);
        pieAlarm = (PieChart) findViewById(R.id.pieAlarm);
        pieElectric = (PieChart) findViewById(R.id.pieElectric);
        barTraining = (BarChart) findViewById(R.id.barTraining);
        ivUp = (ImageView) findViewById(R.id.ivUp);
        ivDown = (ImageView) findViewById(R.id.ivDown);
        ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivRight = (ImageView) findViewById(R.id.ivRight);
        llPlayer = (LinearLayout) findViewById(R.id.llPlayer);
        llPlayBig = (LinearLayout) findViewById(R.id.llPlayBig);
        llAssess = (LinearLayout) findViewById(R.id.llAssess);
        llElectric = (LinearLayout) findViewById(R.id.llElectric);
        llWater = (LinearLayout) findViewById(R.id.llWater);
        llAlarm = (LinearLayout) findViewById(R.id.llAlarm);
        llCheckPatrol = (LinearLayout) findViewById(R.id.llCheckPatrol);
        llVideo = (LinearLayout) findViewById(R.id.llVideo);
        llMonth = (LinearLayout) findViewById(R.id.llMonth);
        llShopCheck = (LinearLayout) findViewById(R.id.llShopCheck);

        llList = (LinearLayout) findViewById(R.id.llList);

        btnBack = (Button) findViewById(R.id.btnBack);
        tvFireCheck = (TextView) findViewById(R.id.tvFireCheck);
        tvElectricCheck = (TextView) findViewById(R.id.tvElectricCheck);
        tvFireAssess = (TextView) findViewById(R.id.tvFireAssess);
        tvMaintenance = (TextView) findViewById(R.id.tvMaintenance);
        tvMaintenanceMonth = (TextView) findViewById(R.id.tvMaintenanceMonth);
        llDocument = (LinearLayout) findViewById(R.id.llDocument);
        hsPic = (HorizontalListView) findViewById(R.id.hsPic);
        tvShopCount = (TextView) findViewById(R.id.tvShopCount);
        tvShopCheckedCount = (TextView) findViewById(R.id.tvShopCheckedCount);
        tvShopUnCheckedCount = (TextView) findViewById(R.id.tvShopUnCheckedCount);
        tvProblemCount = (TextView) findViewById(R.id.tvProblemCount);
        tvDealCount = (TextView) findViewById(R.id.tvDealCount);
        tvUnDealCount = (TextView) findViewById(R.id.tvUnDealCount);
        tvAllFac = (TextView) findViewById(R.id.tvAllFac);
        tvPress = (TextView) findViewById(R.id.tvPress);
        tvLevel = (TextView) findViewById(R.id.tvLevel);
        tvFireAlarm = (TextView) findViewById(R.id.tvFireAlarm);
        tvElectric = (TextView) findViewById(R.id.tvElectric);
        tvAlarmDetector = (TextView) findViewById(R.id.tvAlarmDetector);

        lvMonth = (ListView) findViewById(R.id.lvMonth);
        monthAdapter = new MonthAdapter(this, monthItemList);
        lvMonth.setAdapter(monthAdapter);

        lvMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                maintenanceOneMonthPicList.clear();
                String month = monthItemList.get(position).getMonth();
                for (int i = 0; i < maintenanceMonthPicList.size(); i++) {
                    String ID = maintenanceMonthPicList.get(i).getId();
                    String picUrl = maintenanceMonthPicList.get(i).getPicUrl();
                    String type = maintenanceMonthPicList.get(i).getType();
                    String datetime = maintenanceMonthPicList.get(i).getDateTime();
                    if (datetime.equals(month)) {
                        maintenanceOneMonthPicList.add(new DocumentPicItem(ID, picUrl, type, datetime));
                    }
                }
                llMonth.setVisibility(View.GONE);
                llDocument.setVisibility(View.VISIBLE);
                HorizontalListViewAdapter adapter = new HorizontalListViewAdapter(MainActivity.this, maintenanceOneMonthPicList);
                hsPic.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        });


        facAdapter = new FacAdapter(this, deviceList);
        lvFac.setAdapter(facAdapter);
        facAdapter.notifyDataSetChanged();
        mPlayer = (EZUIPlayer) findViewById(R.id.player_ui);
        playerBig = (EZUIPlayer) findViewById(R.id.playerBig);

        tvAllFac.setOnClickListener(this);
        tvPress.setOnClickListener(this);
        tvFireAlarm.setOnClickListener(this);
        tvElectric.setOnClickListener(this);
        tvAlarmDetector.setOnClickListener(this);



        lvFac.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String deviceID, facTypeID, fac, Position, statue, time, value, maxValue, standardValue;
                Intent intent = new Intent();
                switch (listDeviceType) {

                    case 5:
                        deviceID = electricList.get(position).getRtuID();
                        facTypeID = electricList.get(position).getTypeID();
                        fac = electricList.get(position).getType();
                        Position = electricList.get(position).getPosition();
                        statue = electricList.get(position).getState();
                        time = electricList.get(position).getLastTimestamp();
                        value = electricList.get(position).getValue();
                        if (!value.equals("")) {
                            value = String.valueOf(Math.round(Float.valueOf(value)));
                        }
                        intent.putExtra("deviceID", deviceID);
                        intent.putExtra("facTypeID", facTypeID);
                        intent.putExtra("fac", fac);
                        intent.putExtra("position", Position);
                        intent.putExtra("statue", statue);
                        intent.putExtra("time", time);
                        intent.putExtra("value", value);
                        intent.setClass(MainActivity.this, Electric.class);
                        if (!deviceID.equals("")) {
                            startActivity(intent);
                        }
                        break;

                    default:
                        break;

                }
            }
        });






    }


    private Handler getConstructionHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String jsonData = (String) msg.obj;

            if (jsonData.length() > 10) {

                isFire = true;

            }


        }
    };
    private Handler getK88ConstructionHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String jsonData = (String) msg.obj;

            if (jsonData.length() > 10) {
                isK88Fire = true;

            }

        }
    };


    private Handler getFacInfoHandler = new Handler() {


        @TargetApi(Build.VERSION_CODES.KITKAT)
        public void handleMessage(Message msg) {
            waterPressList.clear();
            waterLevelList.clear();
            electricList.clear();
            deviceList.clear();


            String jsonData = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String rtuID = object.getString("id");
                    String typeID = object.getString("typeID");
                    String type = object.getString("type");
                    String position = object.getString("position");
                    String value = object.getString("value");
                    String maxValue = object.getString("refer_value");
                    String standardValue = object.getString("standardValue");
                    boolean online = Boolean.parseBoolean(object.getString("online"));
                    String lastTimestamp = object.getString("lastTime");
                    if (!value.equals("null")) {

                        switch (typeID) {
                            case "1":
                                if (online == false) {
                                    scoreWaterPress = scoreWaterPress - 5;
//                                    waterPressOfflineList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
                                    deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", "", online, lastTimestamp));
                                    waterPressList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", "", online, lastTimestamp));
                                } else {
                                    if (Float.parseFloat(value) >= Float.parseFloat(standardValue)) {
                                        String state = "normal";
//                                        waterPressNormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", state, online, lastTimestamp));
                                        waterPressList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", state, online, lastTimestamp));
                                    } else if (Float.parseFloat(value) < Float.parseFloat(standardValue)) {
                                        scoreWaterPress = scoreWaterPress - 15;
                                        String state = "alarm";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", state, online, lastTimestamp));
                                        waterPressList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", state, online, lastTimestamp));
//                                        waterPressAbnormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                    }
                                }
                                break;
                            case "2":
                                if (online == false) {
                                    scoreWaterPress = scoreWaterPress - 5;
                                    deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", "", online, lastTimestamp));
                                    waterPressList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", "", online, lastTimestamp));
//                                    waterPressOfflineList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
                                } else {
                                    if (Float.parseFloat(value) >= Float.parseFloat(standardValue)) {
                                        String state = "normal";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", state, online, lastTimestamp));
                                        waterPressList.add(new DeviceItem(rtuID, typeID, type, position, "", "", value, state, online, lastTimestamp));
//                                        waterPressNormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                    } else if (Float.parseFloat(value) < Float.parseFloat(standardValue)) {
                                        scoreWaterPress = scoreWaterPress - 15;
                                        String state = "alarm";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", state, online, lastTimestamp));
                                        waterPressList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", state, online, lastTimestamp));
                                    }
                                }
                                break;
                            case "7":
                                if (online == false) {
                                    scoreWaterPress = scoreWaterPress - 5;
                                    deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", "", online, lastTimestamp));
                                    waterPressList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", "", online, lastTimestamp));
                                } else {
                                    if (Float.parseFloat(value) >= Float.parseFloat(standardValue)) {
                                        String state = "normal";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", state, online, lastTimestamp));
                                        waterPressList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", state, online, lastTimestamp));
//                                        waterPressNormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                    } else if (Float.parseFloat(value) < Float.parseFloat(standardValue)) {
                                        scoreWaterPress = scoreWaterPress - 15;
                                        String state = "alarm";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", state, online, lastTimestamp));
                                        waterPressList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", state, online, lastTimestamp));
                                    }
                                }
                                break;

                            case "3":
                                if (online == false) {
                                    scoreWaterLevel = scoreWaterLevel - 5;
                                    deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", "", online, lastTimestamp));
                                    waterPressList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", "", online, lastTimestamp));
//                                    waterLevelOfflineList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
                                } else {
                                    if (Float.parseFloat(value) >= Float.parseFloat(standardValue)) {
                                        String state = "normal";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", state, online, lastTimestamp));
                                        waterPressList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", state, online, lastTimestamp));
//                                        waterLevelNormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                    } else if (Float.parseFloat(value) < Float.parseFloat(standardValue)) {
                                        scoreWaterLevel = scoreWaterLevel - 15;
                                        String state = "alarm";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", state, online, lastTimestamp));
                                        waterPressList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", state, online, lastTimestamp));
                                    }
                                }
                                break;
                            case "4":
                                if (online == false) {
                                    scoreWaterLevel = scoreWaterLevel - 5;
                                    deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", "", online, lastTimestamp));
                                    waterPressList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", "", online, lastTimestamp));
//                                    waterLevelOfflineList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
                                } else {
                                    if (Float.parseFloat(value) >= Float.parseFloat(standardValue)) {
                                        String state = "normal";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", state, online, lastTimestamp));
                                        waterPressList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", state, online, lastTimestamp));
//                                        waterLevelNormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                    } else if (Float.parseFloat(value) < Float.parseFloat(standardValue)) {
                                        scoreWaterLevel = scoreWaterLevel - 15;
                                        String state = "alarm";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", state, online, lastTimestamp));
                                        waterPressList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", state, online, lastTimestamp));
//                                        waterLevelAbnormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                    }
                                }
                                break;
                            case "9":
                                if (online == false) {
                                    scoreWaterLevel = scoreWaterLevel - 5;
                                    deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", "", online, lastTimestamp));
                                    waterLevelList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", "", online, lastTimestamp));
//                                    waterLevelOfflineList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
                                } else {
                                    if (Float.parseFloat(value) >= Float.parseFloat(standardValue)) {
                                        String state = "normal";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", state, online, lastTimestamp));
                                        waterLevelList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", state, online, lastTimestamp));
//                                        waterLevelNormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                    } else if (Float.parseFloat(value) < Float.parseFloat(standardValue)) {
                                        scoreWaterLevel = scoreWaterLevel - 15;
                                        String state = "alarm";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", state, online, lastTimestamp));
                                        waterLevelList.add(new DeviceItem(rtuID, typeID, type, position, value, "", "", state, online, lastTimestamp));
//                                        waterLevelAbnormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                    }
                                }
                                break;
                            case "8":
                            case "15":
                                if (online == false) {
                                    scoreElectric = scoreElectric - 5;
                                    deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, maxValue, standardValue, "", online, lastTimestamp));
                                    electricList.add(new DeviceItem(rtuID, typeID, type, position, value, maxValue, standardValue, "", online, lastTimestamp));
                                } else {
                                    if (Float.parseFloat(value) < Float.parseFloat(standardValue)) {
                                        String state = "normal";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, maxValue, standardValue, state, online, lastTimestamp));
                                        electricList.add(new DeviceItem(rtuID, typeID, type, position, value, maxValue, standardValue, state, online, lastTimestamp));
                                    } else if (Float.parseFloat(value) >= Float.parseFloat(standardValue)) {
                                        scoreElectric = scoreElectric - 15;
                                        String state = "alarm";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, maxValue, standardValue, state, online, lastTimestamp));
                                        electricList.add(new DeviceItem(rtuID, typeID, type, position, value, maxValue, standardValue, state, online, lastTimestamp));
                                    }
                                }
                                break;

                            case "11":
                                if (value.equals("1")) {
                                    String state = "alarm";

                                    deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, maxValue, standardValue, state, online, lastTimestamp));
                                } else {
                                    String state = "normal";

                                    deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, maxValue, standardValue, state, online, lastTimestamp));
                                }

                                break;

                            default:
                                break;
                        }

                        facAdapter.notifyDataSetChanged();
                        nWorkReceived = nWorkReceived + 3;
                        ShowAssess();


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };

    private void initData() {
        Intent intent = this.getIntent();


        unitName = intent.getStringExtra("unitName");
        unitID = intent.getStringExtra("unitID");
        URL_GET_PATROL_CARS = URL_GET_PATROL_CARS + unitID;
        URL_GET_CHECK_CARS = URL_GET_CHECK_CARS + unitID;
        URL_GET_SHOP_CARS = URL_GET_SHOP_CARS + unitID;
        URL_GET_PROBLEMS = URL_GET_PROBLEMS + unitID;
        URL_GET_FAC_INFO = URL_GET_FAC_INFO + unitID + "&cityID=" + cityID;


    }

    private void initEZ(String appKey) {
        //初始化EZUIKit
        EZUIKit.initWithAppKey(this.getApplication(), appKey);
    }

    private Handler getEZUIAccessTokenHandler = new Handler() {
        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;
            try {
                JSONObject object = new JSONObject(jsonData);
                String data = object.getString("data");
                object = new JSONObject(data);
                accessToken = object.getString("accessToken");
                playEzui();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ;
    };


    private void playEzui() {
        if (mUrl != null) {


            //设置授权token
            EZUIKit.setAccessToken(accessToken);
            //设置debug模式，输出log信息
            EZUIKit.setDebug(true);
            mPlayer.setLoadingView(initProgressBar());
            //设置播放回调callback
            mPlayer.setCallBack(this);

            mPlayer.setPlayParams(mUrl, MainActivity.this);
            //设置播放参数
            mPlayer.setUrl(mUrl);

            //开始播放
            mPlayer.startPlay();

            playerBig.setLoadingView(initProgressBar());
            //设置播放回调callback
            playerBig.setCallBack(this);

            playerBig.setPlayParams(mUrl, MainActivity.this);
            //设置播放参数
            playerBig.setUrl(mUrl);

            //开始播放
//        playerBig.startPlay();

        }
    }

    private ProgressBar initProgressBar() {
        ProgressBar mProgressBar = new ProgressBar(this);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        mProgressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
        mProgressBar.setLayoutParams(lp);
        return mProgressBar;
    }

    //获取本单位维保图片链接
    public Handler getDocumentPicRecordHandler = new Handler() {


        public void handleMessage(Message msg) {
            hsPic.scrollTo(0, 0);
            String jsonData = (String) msg.obj;
            boolean isFireCheck = false;
            boolean isElectricCheck = false;
            boolean isFireAssess = false;
            boolean isMaintenance = false;
            boolean isMaintenanceMonth = false;
            Time t = new Time("GMT+8"); // or Time t=new Time("GMT+8"); 加上Time Zone资料
            t.setToNow(); // 取得系统时间。
            int year = t.year;
            int month = t.month + 1;
            String thisMonthDate = String.valueOf(year) + "-" + String.valueOf(month);
            int lastMonth, lastYear;
            if (month == 1) {
                lastMonth = 12;
                lastYear = year - 1;
            } else {
                lastMonth = month - 1;
                lastYear = year;
            }
            String lastMonthDate = String.valueOf(lastYear) + "-" + String.valueOf(lastMonth);

            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String id = object.getString("unitID");
                    String picUrl = object.getString("photos");
                    String type = object.getString("typeID");
                    String dateTime = object.getString("createTime");
//                    documentPicList.add(new DocumentPicItem(id, picUrl, type, dateTime));
                    switch (type) {
                        case "1":
                            fireCheckPicList.add(new DocumentPicItem(id, picUrl, type, dateTime));
                            isFireCheck = true;
                            tvFireCheck.setText("已完成");
                            tvFireCheck.setTextColor(0XFF0062FF);
                            tvFireCheck.setOnClickListener(MainActivity.this);
                            break;
                        case "2":
                            electricCheckPicList.add(new DocumentPicItem(id, picUrl, type, dateTime));
                            isElectricCheck = true;
                            tvElectricCheck.setText("已完成");
                            tvElectricCheck.setTextColor(0XFF0062FF);
                            tvElectricCheck.setOnClickListener(MainActivity.this);

                            break;
                        case "3":
                            fireAssessPicList.add(new DocumentPicItem(id, picUrl, type, dateTime));
                            isFireAssess = true;
                            tvFireAssess.setText("已完成");
                            tvFireAssess.setTextColor(0XFF0062FF);
                            tvFireAssess.setOnClickListener(MainActivity.this);

                            break;
                        case "4":
                            maintenanceMonthPicList.add(new DocumentPicItem(id, picUrl, type, dateTime));
                            boolean isExist = false;
                            for (int j = 0; j < monthItemList.size(); j++) {
                                if (monthItemList.get(j).getMonth().equals(dateTime)) {
                                    isExist = true;
                                }
                            }
                            if (!isExist) {
                                monthItemList.add(new MonthItem(dateTime));
                            }

                            break;
                        case "5":
                            maintenancePicList.add(new DocumentPicItem(id, picUrl, type, dateTime));
                            isMaintenance = true;
                            tvMaintenance.setText("已完成");
                            tvMaintenance.setTextColor(0XFF0062FF);
                            tvMaintenance.setOnClickListener(MainActivity.this);

                            break;
                        default:

                            break;
                    }
                    if (dateTime.equals(thisMonthDate)) {
                        isMaintenanceMonth = true;
                        tvMaintenanceMonth.setText("本月已完成");
                        tvMaintenanceMonth.setTextColor(0XFF0062FF);
                        tvMaintenanceMonth.setOnClickListener(MainActivity.this);
                    } else if (dateTime.equals(lastMonthDate)) {
                        isMaintenanceMonth = true;
                        tvMaintenanceMonth.setText("上月已完成");
                        tvMaintenanceMonth.setTextColor(0XFF0062FF);
                        tvMaintenanceMonth.setOnClickListener(MainActivity.this);
                    }


                }
                if (isFireAssess) {
                    scoreDayToDay = scoreDayToDay + 5;
                }
                if (isElectricCheck) {
                    scoreDayToDay = scoreDayToDay + 5;
                }
                if (isMaintenance) {
                    scoreDayToDay = scoreDayToDay + 5;
                }
                if (isFireCheck) {
                    scoreDayToDay = scoreDayToDay + 5;

                }
                if (isMaintenanceMonth) {
                    scoreDayToDay = scoreDayToDay + 5;
                }
                monthAdapter.notifyDataSetChanged();
                nWorkReceived++;
                ShowAssess();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };


    //获取本单位所有卡片当日巡查资料
    private Handler getPatrolRecordHandler = new Handler() {


        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;
            unPatrolItemList.clear();
            patroledItemList.clear();
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String cardNo = object.getString("code");
                    String position = object.getString("position");
                    String lasttime = object.getString("lasttime");
                    if (lasttime.equals("no UpdateTime")) {
                        unPatrolItemList.add(new UnPatrolItem(cardNo, position));
                    } else {
                        patroledItemList.add(new PatroledItem(cardNo, position, lasttime));
                    }
                }
                if (jsonArray.length() != 0) {
                    scorePatrol = 5 * (patroledItemList.size() / jsonArray.length());
                }
                drawPiePatrol.draw(piePatrol, MainActivity.this, patroledItemList.size(), unPatrolItemList.size(), false);
                nWorkReceived++;
                ShowAssess();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };

    //获取本单位所有卡片当月检查资料
    private Handler getCheckRecordHandler = new Handler() {


        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;
            unCheckList.clear();
            checkedNormalList.clear();
            checkAbnormalList.clear();
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String cardNo = object.getString("code");
                    String fac = object.getString("type");
                    String position = object.getString("position");
                    String imageName = object.getString("imageName");
                    String problemID = object.getString("problemID");
                    String problem = object.getString("problem");
                    String time = object.getString("lastTime");
                    if (problem.equals("noData")) {
                        unCheckList.add(new UnCheckItem(cardNo, position, fac));
                    } else {
                        if (problemID.equals("")) {
                            checkedNormalList.add(new CheckedItem(cardNo, position, time, imageName, fac, problem, problemID));
                        } else {
                            checkAbnormalList.add(new CheckedItem(cardNo, position, time, imageName, fac, problem, problemID));
                        }
                    }
                }
                if (jsonArray.length() != 0) {
                    scoreCheck = 10 * (checkedNormalList.size() / jsonArray.length());
                }
                drawPieCheck.draw(pieCheck, MainActivity.this, checkedNormalList.size(), checkAbnormalList.size(), unCheckList.size(), false);
                nWorkReceived++;
                ShowAssess();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };

    //获取所有商铺检查资料
    private Handler getShopCheckRecordHandler = new Handler() {


        public void handleMessage(Message msg) {
            shopUnCheckedItemList.clear();
            shopCheckedItemList.clear();
            String jsonData = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String name = object.getString("name");
                    String problem = object.getString("problem");
                    String isDeal = object.getString("isDeal");
                    String time = object.getString("lastTime");
                    String check = object.getString("check");

                    if (check.equals("noData")) {
                        shopUnCheckedItemList.add(new ShopUnCheckedItem(null, null, name, unitID));
                    } else {
                        if (problem.equals(isDeal)) {
                            shopCheckedItemList.add(new ShopCheckedItem(null, null, unitID, time, "无隐患或隐患已整改完毕"));
                        } else {
                            shopCheckedItemList.add(new ShopCheckedItem(null, null, unitID, time, "隐患未整改完毕"));
                        }
                    }
                }
                tvShopCount.setText(String.valueOf(jsonArray.length()) + "家");
                tvShopCheckedCount.setText(String.valueOf(shopCheckedItemList.size()) + "家");
                tvShopUnCheckedCount.setText(String.valueOf(shopUnCheckedItemList.size()) + "家");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };

    //获取所有隐患资料
    private Handler getProblemRecordHandler = new Handler() {


        public void handleMessage(Message msg) {
            shopProblemDealedItemList.clear();
            shopProblemNotDealItemList.clear();
            String jsonData = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String shopID = object.getString("shopID");
                    String name = object.getString("name");
                    String imageName = object.getString("imageName");
                    String problem = object.getString("problem");
                    String isDeal = object.getString("isDeal");
                    String time = object.getString("timestamp");
                    String dealPic = object.getString("dealPic");
                    String dealTimestamp = object.getString("dealTimestamp");

                    if (isDeal.equals("0")) {
                        shopProblemNotDealItemList.add(new ShopProblemNotDealItem(shopID, name, unitID, time, imageName, problem));
                    } else {

                        shopProblemDealedItemList.add(new ShopProblemDealedItem(shopID, name, unitID, time, imageName, problem, dealPic, dealTimestamp));
                    }
                }
                tvProblemCount.setText(String.valueOf(jsonArray.length()) + "处");
                tvDealCount.setText(String.valueOf(shopProblemDealedItemList.size()) + "处");
                tvUnDealCount.setText(String.valueOf(shopProblemNotDealItemList.size()) + "处");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };


    @Override
    public void onClick(View v) {
        if (v == llPlayer) {
            viewCome(llPlayBig);
        } else if (v == btnBack) {
            viewGo(llPlayBig);
        } else if (v == pieWaterPress) {
            mPlayer.stopPlay();
            Intent intent = new Intent(MainActivity.this, Water.class);
            startActivity(intent);
        } else if (v == tvFireCheck) {
            viewCome(llDocument);
            HorizontalListViewAdapter adapter = new HorizontalListViewAdapter(MainActivity.this, fireCheckPicList);
            hsPic.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } else if (v == tvElectricCheck) {
            viewCome(llDocument);
            HorizontalListViewAdapter adapter = new HorizontalListViewAdapter(MainActivity.this, electricCheckPicList);
            hsPic.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } else if (v == tvFireAssess) {
            viewCome(llDocument);
            HorizontalListViewAdapter adapter = new HorizontalListViewAdapter(MainActivity.this, fireAssessPicList);
            hsPic.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } else if (v == tvMaintenance) {
            viewCome(llDocument);
            HorizontalListViewAdapter adapter = new HorizontalListViewAdapter(MainActivity.this, maintenancePicList);
            hsPic.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else if (v == tvMaintenanceMonth) {
            viewCome(llMonth);
            HorizontalListViewAdapter adapter = new HorizontalListViewAdapter(MainActivity.this, maintenanceMonthPicList);
            hsPic.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else if (v == tvAllFac) {
            changeTitleBG(tvAllFac);
            changeAdapter(deviceList);
            listDeviceType = ALL_FAC;
        } else if (v == tvPress) {
            changeTitleBG(tvPress);
            changeAdapter(waterPressList);
            listDeviceType = PRESS_FAC;
        } else if (v == tvLevel) {
            changeTitleBG(tvLevel);
            changeAdapter(waterLevelList);
            listDeviceType = LEVEL_FAC;
        } else if (v == tvFireAlarm) {
            changeTitleBG(tvFireAlarm);
            fireAlarmList.clear();
            HttpUtils.getJSON(URL_GET_FIRE_ALARM_FAC_STATUE + unitID, getFireAlarmStatueHandler);

//            FacSmokeFeelAdapter facsmokeAdapter = new FacSmokeFeelAdapter(this, fireAlarmList);
//            lvFac.setAdapter(facsmokeAdapter);
//            facsmokeAdapter.notifyDataSetChanged();

//            changeAdapter(fireAlarmList);
            listDeviceType = ALARM_FAC;
        } else if (v == tvElectric) {
            changeTitleBG(tvElectric);
            changeAdapter(electricList);
            listDeviceType = ELECTRIC_FAC;
        } else if (v == tvAlarmDetector) {
            changeTitleBG(tvAlarmDetector);
            AlarmDectorList.clear();
            HttpUtils.getJSON(URL_ALARM_DETECTOR + unitID, getAlarmDetactorHandler);
//           AlarmDectorAdapter alarmDectorAdapter = new AlarmDectorAdapter(this, AlarmDectorList);
//            lvFac.setAdapter(alarmDectorAdapter);
//            alarmDectorAdapter.notifyDataSetChanged();
            listDeviceType = ELECTRIC_FAC;


        }
    }

    private void changeTitleBG(View view) {
        tvAllFac.setBackgroundColor(0XFF336699);
        tvPress.setBackgroundColor(0XFF336699);
        tvLevel.setBackgroundColor(0XFF336699);
        tvFireAlarm.setBackgroundColor(0XFF336699);
        tvElectric.setBackgroundColor(0XFF336699);
        tvAlarmDetector.setBackgroundColor(0XFF336699);
        view.setBackgroundColor(0XFF0099CC);
    }

    private void changeAdapter(List list) {
        facAdapter = new FacAdapter(this, list);
        lvFac.setAdapter(facAdapter);
        facAdapter.notifyDataSetChanged();
    }


    private void viewGo(View view) {
        if (view == llPlayBig) {
            playerBig.stopPlay();
        }
        mPlayer.startPlay();
        AnimationSet animationSet1 = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0, 1f, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);
        animationSet1.addAnimation(scaleAnimation);
        view.startAnimation(animationSet1);
        view.setVisibility(View.GONE);

        MyAnimation myAnimation = new MyAnimation();
        myAnimation.Come(llAssess, 500, -2000, 0, 0, 0);
        myAnimation.Come(llElectric, 500, 0, 0, -2000, 0);
        myAnimation.Come(llWater, 500, 0, 0, -2000, 0);
        myAnimation.Come(llAlarm, 500, 2000, 0, 0, 0);
        myAnimation.Come(llVideo, 500, 2000, 0, 0, 0);
        myAnimation.Come(llPlayer, 500, 2000, 0, 0, 0);
        myAnimation.Come(llList, 500, 0, 0, -2000, 0);
        myAnimation.Come(llCheckPatrol, 500, 0, 0, 2000, 0);
    }

    private void viewCome(View view) {
        mPlayer.stopPlay();

        if (view == llDocument) {
        }
        if (view == llPlayBig) {
            playerBig.startPlay();
        }

        MyAnimation myAnimation = new MyAnimation();
        myAnimation.Go(llAssess, 500, 0, -2000, 0, 0);
        myAnimation.Go(llElectric, 500, 0, 0, 0, -2000);
        myAnimation.Go(llWater, 500, 0, 0, 0, -2000);
        myAnimation.Go(llAlarm, 500, 0, 2000, 0, 0);
        myAnimation.Go(llVideo, 500, 0, 2000, 0, 0);
        myAnimation.Go(llPlayer, 500, 0, 2000, 0, 0);
        myAnimation.Go(llList, 500, 0, 0, 0, -2000);
        myAnimation.Go(llCheckPatrol, 500, 0, 0, 0, 2000);

        AnimationSet animationSet1 = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);
        animationSet1.addAnimation(scaleAnimation);
        view.startAnimation(animationSet1);
        view.setVisibility(View.VISIBLE);

    }


    class MyThread extends Thread {

        public void run() {
            int i = 0;
            while (true) {
                try {

                    HttpUtils.getJSON(URL_GET_FAC_INFO, getFacInfoHandler);
                    HttpUtils.getJSON(URL_GET_PATROL_CARS, getPatrolRecordHandler);
                    HttpUtils.getJSON(URL_GET_CHECK_CARS, getCheckRecordHandler);
                    HttpUtils.getJSON(URL_GET_SHOP_CARS, getShopCheckRecordHandler);
                    HttpUtils.getJSON(URL_GET_PROBLEMS, getProblemRecordHandler);


//                    HttpUtils.getJSON(URL_GET_FIRE_ALARM_FAC_STATUE + unitID, getFireAlarmStatueHandler);
//                    HttpUtils.getJSON(URL_ALARM_DETECTOR + unitID, getAlarmDetactorHandler);

                    sleep(30000);

                    Message msg = new Message();
                    msg.arg1 = i;
                    handler.sendMessage(msg);
                    if (i == 0) {
                        i = 1;
                    } else if (i == 1) {
                        i = 0;
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int i = msg.arg1;
            if (i == 0) {
                drawPieAlarm.draw(pieAlarm, MainActivity.this, 4219, 1233, 663);
                drawElectric.draw(pieElectric, MainActivity.this, 5, 2);
                drawBarTraining.draw(barTraining);

            } else if (i == 1) {
                drawPieAlarm.draw(pieAlarm, MainActivity.this, 4219, 1233, 663);
                drawElectric.draw(pieElectric, MainActivity.this, 5, 2);
                drawBarTraining.draw(barTraining);
            }


            webViewAssessDown.reload();


            webViewAssessUp.reload();


        }

        ;
    };







    @Override
    protected void onPause() {
        super.onPause();
        if (llPlayBig.getVisibility() == View.VISIBLE) {
            playerBig.stopPlay();
        } else {
            mPlayer.stopPlay();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        isAct = true;
        isFire = false;
        isK88Fire = false;

        if (llPlayBig.getVisibility() == View.VISIBLE) {
            playerBig.startPlay();
        } else {
            mPlayer.startPlay();
        }
    }


    @Override
    public void onPlaySuccess() {

    }

    @Override
    public void onPlayFail(EZUIError ezuiError) {
        String str = ezuiError.getErrorString();
        if (str.equals("UE105")) {
            if (llPlayBig.getVisibility() == View.VISIBLE) {
                playerBig.startPlay();
            } else {
                mPlayer.startPlay();
            }
        }

    }

    @Override
    public void onVideoSizeChange(int i, int i1) {

    }

    @Override
    public void onPrepared() {

    }

    @Override
    public void onPlayTime(Calendar calendar) {

    }

    @Override
    public void onPlayFinish() {

    }


    private void checkVer() {
        try {
            localVersion = getVersionName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new CheckVer().execute();
    }


    private String getVersionName() throws Exception {
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
                0);
        return packInfo.versionName;
    }

    class CheckVer extends AsyncTask<String, String, String> {
        String ver;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            HttpURLConnection conn;
            InputStream is;
            String url = "http://jn.xiaofang365.cn/xfwlw/xfwlw/checkVer.php?softName=unitPad";//版本控制链接，待定

            try {

                conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("GET");
                is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line = "";
                StringBuilder result = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                    String jsonData = result.toString();
                    JSONObject object = new JSONObject(jsonData);
                    ver = object.getString("ver");
                    downLoadNewVerUrl = new URL(object.getString("downLoadUrl"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {

            //pDialog.dismiss();
            if (ver != null) {
                if (ver.equals(localVersion)) {

                } else {
                    AlertDialog.Builder multiDia = new AlertDialog.Builder(MainActivity.this);
                    multiDia.setTitle("系统有新版本需要升级！");
                    multiDia.setPositiveButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                            finish();
                        }
                    });
                    multiDia.setNeutralButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                            downLoadApk();


                            //new DownLoadNewVer().execute();
                            //finish();
                        }
                    });

                    multiDia.create().show();
                }


            }

        }
    }

    protected void downLoadApk() {
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = DownLoadManager.getFileFromServer(String.valueOf(downLoadNewVerUrl), pd);
                    sleep(3000);
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
//                    Message msg = new Message();
//                    msg.what = DOWN_ERROR;
//                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private void ShowAssess() {

        if (nWorkReceived >= 7) {

            Log.e("nWorkReceived", "scoreWaterPress: " + scoreWaterPress);
            Log.e("nWorkReceived", "scoreWaterLevel: " + scoreWaterLevel);
            Log.e("nWorkReceived", "scoreElectric: " + scoreElectric);
            Log.e("nWorkReceived", "scoreFireAlarm: " + scoreFireAlarm);
            Log.e("nWorkReceived", "scorePatrol: " + scorePatrol);
            Log.e("nWorkReceived", "scoreCheck: " + scoreCheck);
            Log.e("nWorkReceived", "scoreDayToDay: " + scoreDayToDay);

            if (scoreWaterPress < 0) {
                scoreWaterPress = 0;
            } else if (scoreWaterLevel < 0) {
                scoreWaterLevel = 0;
            } else if (scoreElectric < 0) {
                scoreElectric = 0;
            } else if (scoreFireAlarm < 0) {
                scoreFireAlarm = 0;
            } else if (scorePatrol < 0) {
                scorePatrol = 0;
            } else if (scoreCheck < 0) {
                scoreCheck = 0;
            } else if (scoreDayToDay < 0) {
                scoreDayToDay = 0;
            }


            scoreTotal = scoreWaterPress + scoreWaterLevel + scoreElectric + scoreFireAlarm + scorePatrol + scoreCheck + scoreDayToDay;
            String urlUp = urlAssessUp + "?scoreTotal=" + scoreTotal;
            String urlDown = urlAssessDown + "?scoreWaterPress=" + scoreWaterPress +
                    "&scoreWaterLevel=" + scoreWaterLevel +
                    "&scoreFireAlarm=" + scoreFireAlarm +
                    "&scoreElectric=" + scoreElectric +
                    "&scorePatrol=" + scorePatrol +
                    "&scoreCheck=" + scoreCheck +
                    "&scoreDayToDay=" + scoreDayToDay;


            MyWebViewSet.Set(webViewAssessUp);
            webViewAssessUp.loadUrl(urlUp);


            webViewAssessDown.loadUrl(urlDown);
            MyWebViewSet.Set(webViewAssessDown);

        }
    }

}
