package com.example.dell.unittv;

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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.example.dell.unittv.adapter.Checked1Adapter;
import com.example.dell.unittv.adapter.DocumentPicAdapter;
import com.example.dell.unittv.adapter.FacAdapter;
import com.example.dell.unittv.adapter.HorizontalListViewAdapter;
import com.example.dell.unittv.adapter.MonthAdapter;
import com.example.dell.unittv.adapter.PatroledAdapter;
import com.example.dell.unittv.adapter.UnCheckAdapter;
import com.example.dell.unittv.adapter.UnPatrolAdapter;
import com.example.dell.unittv.charts.DrawBarTraining;
import com.example.dell.unittv.charts.DrawPieAlarm;
import com.example.dell.unittv.charts.DrawPieCheck;
import com.example.dell.unittv.charts.DrawPieElectric;
import com.example.dell.unittv.charts.DrawPieLevel;
import com.example.dell.unittv.charts.DrawPiePatrol;
import com.example.dell.unittv.charts.DrawPiePress;
import com.example.dell.unittv.charts.DrawPieTitleScore;
import com.example.dell.unittv.item.CheckedItem;
import com.example.dell.unittv.item.DeviceItem;
import com.example.dell.unittv.item.DocumentPicItem;
import com.example.dell.unittv.item.MonthItem;
import com.example.dell.unittv.item.PatroledItem;
import com.example.dell.unittv.item.ShopCheckedItem;
import com.example.dell.unittv.item.ShopProblemDealedItem;
import com.example.dell.unittv.item.ShopProblemNotDealItem;
import com.example.dell.unittv.item.ShopUnCheckedItem;
import com.example.dell.unittv.item.UnCheckItem;
import com.example.dell.unittv.item.UnPatrolItem;
import com.example.dell.unittv.tools.DownLoadManager;
import com.example.dell.unittv.tools.HttpUtils;
import com.example.dell.unittv.tools.MyAnimation;
import com.example.dell.unittv.view.HorizontalListView;
import com.ezvizuikit.open.EZUIError;
import com.ezvizuikit.open.EZUIKit;
import com.ezvizuikit.open.EZUIPlayer;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity implements View.OnClickListener {

    private String cityID = "370100";
    private String unitID, unitName;
    private TextView tvScore;

    private WebView webViewAssessUp, webViewAssessDown;
    private String urlAssessUp = "http://jn.xiaofang365.cn/xfwlw/xfwlw/assessRight_tv.php";
    private String urlAssessDown = "http://jn.xiaofang365.cn/xfwlw/xfwlw/assess_tv.php";

    public String URL_GET_PATROL_CARS = "http://dndzl.cn/daping/patrol_record.php?unitID=";
    public String URL_GET_CHECK_CARS = "http://dndzl.cn/daping/monthlyTest.php?unitID=";
    public String URL_GET_SHOP_CARS = "http://dndzl.cn/daping/shopAll.php?parentID=";
    public String URL_GET_PROBLEMS = "http://dndzl.cn/daping/shopProblem.php?parentID=";
    public String URL_GET_FIRE_ALARM_FAC_STATUE = "http://116.62.239.16/LSFPWEB/rest/platformInterface/platformData/getDeviceByInfo/all/1/1000";

    public static List<UnPatrolItem> unPatrolItemList = new ArrayList<UnPatrolItem>();
    public static List<PatroledItem> patroledItemList = new ArrayList<PatroledItem>();

    public static List<UnCheckItem> unCheckList = new ArrayList<UnCheckItem>();
    public static List<CheckedItem> checkedNormalList = new ArrayList<CheckedItem>();
    public static List<CheckedItem> checkAbnormalList = new ArrayList<CheckedItem>();
    private LinearLayout llAssess, llElectric, llWater, llAlarm, llCheckPatrol, llMonth, llShopCheck;
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

//    private String URL_GET_CAMERA_INFO = "http://jn.xiaofang365.cn/xfwlw/xfwlw/getCameraInfo?account=18615600710&typeID=1&unitID=";


    private PieChart piePatrol, pieCheck, pieWaterPress, pieWaterLevel, pieAlarm, pieElectric, pieTitleScore;
    private RadarChart radarAssess;
    private BarChart barTraining;

    private MyThread myThread = new MyThread();
    private MyAnimation myAnimation = new MyAnimation();

    private ListView lvMonth;
    private List<MonthItem> monthItemList = new ArrayList<MonthItem>();
    private MonthAdapter monthAdapter;

    private DrawPieTitleScore drawPieTitleScore = new DrawPieTitleScore();
    private DrawPiePatrol drawPiePatrol = new DrawPiePatrol();
    private DrawPieCheck drawPieCheck = new DrawPieCheck();
    private DrawPieAlarm drawPieAlarm = new DrawPieAlarm();
    private DrawPieElectric drawElectric = new DrawPieElectric();

    private DrawBarTraining drawBarTraining = new DrawBarTraining();

    private String URL_GET_EZUI_ACCESS_TOKEN = "https://open.ys7.com/api/lapp/token/get";

    //    private String URL_GET_FAC_INFO = "http://dndzl.cn/daping/zhongheng.php";
    private String URL_GET_FAC_INFO = "http://dndzl.cn/daping/syntheticData.php?unitID=";


    private TextView tvFireCheck, tvElectricCheck, tvFireAssess, tvMaintenance, tvMaintenanceMonth;
    private LinearLayout llDocument;
    private HorizontalListView hsPic;
    public static List<DeviceItem> deviceList = new ArrayList<DeviceItem>();//所有设备的列表
    public static List<DeviceItem> waterPressList = new ArrayList<DeviceItem>();//消防给水水压
    public static List<DeviceItem> waterLevelList = new ArrayList<DeviceItem>();//消防给水水位
    public static List<DeviceItem> electricList = new ArrayList<DeviceItem>();//电气火灾检测
    public static List<DeviceItem> fireAlarmList = new ArrayList<DeviceItem>();//火灾自动报警

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

    private boolean isAct = true;


    private void showRadar(){
        radarAssess.setDescription("");
        // 绘制线条宽度，圆形向外辐射的线条  
        radarAssess.setWebLineWidth(1.0f);
        // 内部线条宽度，外面的环状线条  
        radarAssess.setWebLineWidthInner(1.0f);
        // 所有线条WebLine透明度  
        radarAssess.setWebAlpha(100);
        radarAssess.setWebColor(0XFFFFFFFF);
        radarAssess.setWebColorInner(0XFFFFFFFF);

        // create a custom MarkerView (extend MarkerView) and specify the layout  
        // to use for it  
        // 点击的时候弹出对应的布局显示数据  
//        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
//
//        // set the marker to the chart
//        radarAssess.setMarkerView(mv);

        setData();

        XAxis xAxis = radarAssess.getXAxis();
        // X坐标值字体样式  
//        xAxis.setTypeface(tf);
        // X坐标值字体大小  
        xAxis.setTextSize(12f);
        xAxis.setTextColor(0XFFFFFFFF);

        YAxis yAxis = radarAssess.getYAxis();
        // Y坐标值字体样式  
//        yAxis.setTypeface(tf);
        // Y坐标值标签个数  
        yAxis.setLabelCount(3, false);
        // Y坐标值字体大小  
        yAxis.setTextSize(15f);
        // Y坐标值是否从0开始  
        yAxis.setStartAtZero(true);

        yAxis.setEnabled(false);


        Legend l = radarAssess.getLegend();
        // 图例位置  
        l.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        // 图例字体样式  
//        l.setTypeface(tf);
        // 图例X间距  
        l.setXEntrySpace(2f);
        // 图例Y间距  
        l.setYEntrySpace(1f);
        l.setEnabled(false);
    }

    private String[] mParties = new String[] {
            "水压监测", "水位监测", "火灾报警", "电气监测", "巡查检查", "维保检测"};

    public void setData() {

        float mult = 100;
        int cnt = 6; // 不同的维度Party A、B、C...总个数

        // Y的值，数据填充
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        float[] values = new float[cnt];

        values[0] = scoreWaterPress*100/15;
        values[1] = scoreWaterLevel*100/15;
        values[2] = scoreFireAlarm*100/15;
        values[3] = scoreElectric*100/15;
        values[4] = (scoreCheck + scorePatrol)*100/15;
        values[5] = scoreDayToDay*100/25;


        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < cnt; i++) {
            yVals.add(new Entry(values[i], i));
//            yVals.add(new Entry((float) (Math.random() * mult) + mult / 2, i));
        }


        // Party A、B、C..
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < cnt; i++)
            xVals.add(mParties[i % mParties.length]);


        RadarDataSet set = new RadarDataSet(yVals, "Set 2");
        set.setColor(0XFF229AFF);
        set.setDrawFilled(true);
        set.setLineWidth(2f);

        ArrayList<RadarDataSet> sets = new ArrayList<RadarDataSet>();
        sets.add(set);

        RadarData data = new RadarData(xVals, sets);
        // 数据字体样式
//        data.setValueTypeface(tf);
        // 数据字体大小
        data.setValueTextSize(4f);

        data.setValueTextColor(0X00FFFFFF);
        // 是否绘制Y值到图表
        data.setDrawValues(true);

        radarAssess.setData(data);

        radarAssess.invalidate();

    }

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



//        drawPieAlarm.draw(pieAlarm, MainActivity.this, 4219, 1233, 663);
//        drawElectric.draw(pieElectric, MainActivity.this, 5, 2);

        myThread.start();


    }

    private void getData(){
        HttpUtils.getJSON(URL_GET_DOCUMENT_PICS + "?unitID=" + unitID, getDocumentPicRecordHandler);
        HttpUtils.getJSON(URL_GET_FAC_INFO, getFacInfoHandler);
        HttpUtils.getJSON(URL_GET_PATROL_CARS, getPatrolRecordHandler);
        HttpUtils.getJSON(URL_GET_CHECK_CARS, getCheckRecordHandler);
        HttpUtils.getJSON(URL_GET_SHOP_CARS, getShopCheckRecordHandler);
        HttpUtils.getJSON(URL_GET_PROBLEMS, getProblemRecordHandler);

        final Map<String, String> params = new HashMap<String, String>();
        HttpUtils.myPost(URL_GET_FIRE_ALARM_FAC_STATUE, params, getFireAlarmStatueHandler);
    }

    private Handler getFireAlarmStatueHandler = new Handler() {
        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;
            fireAlarmList.clear();
            try {
                JSONObject object = new JSONObject(jsonData);
                JSONArray jsonArray = object.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String id = obj.getString("id");
                    String position = obj.getString("position");
                    String statue = obj.getString("statue");
                    String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                    if (statue.equals("正常")) {
                        statue = "normal";
                    } else {
                        statue = "alarm";
                    }
                    String facTypeID = "14";
                    String facType = obj.getString("factype");
                    fireAlarmList.add(new DeviceItem(id, facTypeID, facType, position, "", statue, true, timestamp));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ;
    };


    private void initView() {
        pieTitleScore = findViewById(R.id.pieTitleScore);
        pieCheck = (PieChart) findViewById(R.id.pieCheck);
        piePatrol = (PieChart) findViewById(R.id.piePatrol);
        pieWaterPress = (PieChart) findViewById(R.id.pieWaterPress);
        pieWaterLevel = (PieChart) findViewById(R.id.pieWaterLevel);
        pieAlarm = (PieChart) findViewById(R.id.pieAlarm);
        pieElectric = (PieChart) findViewById(R.id.pieElectric);
        barTraining = (BarChart) findViewById(R.id.barTraining);
        radarAssess = findViewById(R.id.radarAssess);
        llAssess = (LinearLayout) findViewById(R.id.llAssess);
        llElectric = (LinearLayout) findViewById(R.id.llElectric);
        llWater = (LinearLayout) findViewById(R.id.llWater);
        llAlarm = (LinearLayout) findViewById(R.id.llAlarm);
        llCheckPatrol = (LinearLayout) findViewById(R.id.llCheckPatrol);
        llMonth = (LinearLayout) findViewById(R.id.llMonth);
        llShopCheck = (LinearLayout) findViewById(R.id.llShopCheck);

        tvScore = findViewById(R.id.tvScore);

        llList = (LinearLayout) findViewById(R.id.llList);
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
                    System.out.println("month====" + datetime);
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


        lvFac = (ListView) findViewById(R.id.lvFac);
        facAdapter = new FacAdapter(this, deviceList);
        lvFac.setAdapter(facAdapter);
        facAdapter.notifyDataSetChanged();


        lvFac.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String deviceID, facTypeID, fac, Position, statue, time, value;
                Intent intent = new Intent();
                switch (listDeviceType) {
                    case 1:
                        deviceID = deviceList.get(position).getRtuID();
                        facTypeID = deviceList.get(position).getTypeID();
                        fac = deviceList.get(position).getType();
                        Position = deviceList.get(position).getPosition();
                        statue = deviceList.get(position).getState();
                        time = deviceList.get(position).getLastTimestamp();
                        value = deviceList.get(position).getValue();

                        if (facTypeID.equals("1") || facTypeID.equals("2")||facTypeID.equals("7")) {
                            value = String.format("%.3f", Float.valueOf(value));
                            intent.setClass(MainActivity.this, WaterPress.class);
                        } else if (facTypeID.equals("3") || facTypeID.equals("4")) {
                            value = String.valueOf(Math.round(Float.valueOf(value)));
                            intent.setClass(MainActivity.this, WaterLevel.class);
                        } else if (facTypeID.equals("13") || facTypeID.equals("8")||facTypeID.equals("12")) {
                            value = String.valueOf(Math.round(Float.valueOf(value)));
                            intent.setClass(MainActivity.this, Electric.class);
                        }


                        intent.putExtra("deviceID", deviceID);
                        intent.putExtra("facTypeID", facTypeID);
                        intent.putExtra("fac", fac);
                        intent.putExtra("position", Position);
                        intent.putExtra("statue", statue);
                        intent.putExtra("time", time);
                        intent.putExtra("value", value);
                        startActivity(intent);
//                        if (facTypeID.equals("1") || facTypeID.equals("2")) {
//                            intent.setClass(MainActivity.this, WaterPress.class);
//                            startActivity(intent);
//                        } else if(facTypeID.equals("3") || facTypeID.equals("4")){
//                            intent.setClass(MainActivity.this, WaterLevel.class);
//                            startActivity(intent);
//                        }
                        break;
                    case 2:
                        deviceID = waterPressList.get(position).getRtuID();
                        facTypeID = waterPressList.get(position).getTypeID();
                        fac = waterPressList.get(position).getType();
                        Position = waterPressList.get(position).getPosition();
                        statue = waterPressList.get(position).getState();
                        time = waterPressList.get(position).getLastTimestamp();
                        value = waterPressList.get(position).getValue();
                        value = String.format("%.3f", Float.valueOf(value));

                        intent.putExtra("deviceID", deviceID);
                        intent.putExtra("facTypeID", facTypeID);
                        intent.putExtra("fac", fac);
                        intent.putExtra("position", Position);
                        intent.putExtra("statue", statue);
                        intent.putExtra("time", time);
                        intent.putExtra("value", value);

                        intent.setClass(MainActivity.this, WaterPress.class);
                        startActivity(intent);
                        break;
                    case 3:
                        deviceID = waterLevelList.get(position).getRtuID();
                        facTypeID = waterLevelList.get(position).getTypeID();
                        fac = waterLevelList.get(position).getType();
                        Position = waterLevelList.get(position).getPosition();
                        statue = waterLevelList.get(position).getState();
                        time = waterLevelList.get(position).getLastTimestamp();
                        value = waterLevelList.get(position).getValue();
                        value = String.format("%.0f", Float.valueOf(value));

                        intent.putExtra("deviceID", deviceID);
                        intent.putExtra("facTypeID", facTypeID);
                        intent.putExtra("fac", fac);
                        intent.putExtra("position", Position);
                        intent.putExtra("statue", statue);
                        intent.putExtra("time", time);
                        intent.putExtra("value", value);

                        intent.setClass(MainActivity.this, WaterLevel.class);
                        startActivity(intent);
                        break;
                    case 5:
                        deviceID = electricList.get(position).getRtuID();
                        facTypeID = electricList.get(position).getTypeID();
                        fac = electricList.get(position).getType();
                        Position = electricList.get(position).getPosition();
                        statue = electricList.get(position).getState();
                        time = electricList.get(position).getLastTimestamp();
                        value = electricList.get(position).getValue();
                        value = String.valueOf(Math.round(Float.valueOf(value)));
                        intent.putExtra("deviceID", deviceID);
                        intent.putExtra("facTypeID", facTypeID);
                        intent.putExtra("fac", fac);
                        intent.putExtra("position", Position);
                        intent.putExtra("statue", statue);
                        intent.putExtra("time", time);
                        intent.putExtra("value", value);
                        intent.setClass(MainActivity.this, Electric.class);
                        startActivity(intent);
                        break;

                    default:
                        break;

                }
            }
        });


        tvAllFac.setOnClickListener(this);
        tvPress.setOnClickListener(this);
        tvLevel.setOnClickListener(this);
        tvFireAlarm.setOnClickListener(this);
        tvElectric.setOnClickListener(this);
        llWater.setOnClickListener(this);

        pieClickListener(pieWaterPress);
        pieClickListener(pieWaterLevel);
        pieClickListener(pieCheck);
        pieClickListener(piePatrol);
        llShopCheck.setOnClickListener(this);

    }

    private void pieClickListener(final PieChart pieChart) {
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                if (pieChart == pieWaterPress || pieChart == pieWaterLevel) {
                    Intent intent = new Intent(MainActivity.this, Water.class);
                    intent.putExtra("unitID", unitID);
                    startActivity(intent);
                } else if (pieChart == piePatrol) {
                    Intent intent = new Intent(MainActivity.this, Patrol.class);
                    intent.putExtra("unitID", unitID);
                    startActivity(intent);
                } else if (pieChart == pieCheck) {
                    Intent intent = new Intent(MainActivity.this, Check.class);
                    intent.putExtra("unitID", unitID);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }


    //获取本单位维保图片链接
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
                    String standardValue = object.getString("standardValue");
                    boolean online = Boolean.parseBoolean(object.getString("online"));
                    String lastTimestamp = object.getString("lastTime");


                    if (!value.equals("null")) {
                        switch (typeID) {
                            case "1"://室内消火栓
                                if (online == false) {
                                    scoreWaterPress = scoreWaterPress - 5;
//                                    waterPressOfflineList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
                                    deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));

                                    waterPressList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
                                } else {
                                    if (Float.parseFloat(value) >= Float.parseFloat(standardValue)) {
                                        String state = "normal";
//                                        waterPressNormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));

                                        waterPressList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));

                                    } else if (Float.parseFloat(value) < Float.parseFloat(standardValue)) {
                                        scoreWaterPress = scoreWaterPress - 15;
                                        String state = "alarm";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                        waterPressList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));

//                                        waterPressAbnormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                    }
                                }
                                break;
                            case "2"://室外消火栓
                                if (online == false) {
                                    scoreWaterPress = scoreWaterPress - 5;
                                    deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
                                    waterPressList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));

//                                    waterPressOfflineList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
                                } else {
                                    if (Float.parseFloat(value) >= Float.parseFloat(standardValue)) {
                                        String state = "normal";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                        waterPressList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));

//                                        waterPressNormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                    } else if (Float.parseFloat(value) < Float.parseFloat(standardValue)) {
                                        scoreWaterPress = scoreWaterPress - 15;
                                        String state = "alarm";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                        waterPressList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));

//                                        waterPressAbnormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                    }
                                }
                                break;
                            case "3"://消防水池
                                if (online == false) {
                                    scoreWaterLevel = scoreWaterLevel - 5;
                                    deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
                                    waterLevelList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
//                                    waterLevelOfflineList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
                                } else {
                                    if (Float.parseFloat(value) >= Float.parseFloat(standardValue)) {
                                        String state = "normal";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                        waterLevelList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
//                                        waterLevelNormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                    } else if (Float.parseFloat(value) < Float.parseFloat(standardValue)) {
                                        scoreWaterLevel = scoreWaterLevel - 15;
                                        String state = "alarm";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                        waterLevelList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
//                                        waterLevelAbnormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                    }
                                }
                                break;
                            case "4"://消防水箱
                                if (online == false) {
                                    scoreWaterLevel = scoreWaterLevel - 5;
                                    deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
                                    waterLevelList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
//                                    waterLevelOfflineList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
                                } else {
                                    if (Float.parseFloat(value) >= Float.parseFloat(standardValue)) {
                                        String state = "normal";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                        waterLevelList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
//                                        waterLevelNormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                    } else if (Float.parseFloat(value) < Float.parseFloat(standardValue)) {
                                        scoreWaterLevel = scoreWaterLevel - 15;
                                        String state = "alarm";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                        waterLevelList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
//                                        waterLevelAbnormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                    }
                                }

                                break;

                            case "7"://自动喷淋
                                if (online == false) {
                                    scoreWaterPress = scoreWaterPress - 5;
//                                    waterPressOfflineList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
                                    deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));

                                    waterPressList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
                                } else {
                                    if (Float.parseFloat(value) >= Float.parseFloat(standardValue)) {
                                        String state = "normal";
//                                        waterPressNormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));

                                        waterPressList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));

                                    } else if (Float.parseFloat(value) < Float.parseFloat(standardValue)) {
                                        scoreWaterPress = scoreWaterPress - 15;
                                        String state = "alarm";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                        waterPressList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));

//                                        waterPressAbnormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                    }
                                }
                                break;

                            case "8"://电路电流
                                if (online == false) {
                                    scoreElectric = scoreElectric - 5;
                                    deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
                                    electricList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
//                                    waterLevelList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
//                                    waterLevelOfflineList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
                                } else {
                                    if (Float.parseFloat(value) < Float.parseFloat(standardValue)) {
                                        String state = "normal";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                        electricList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
//                                        waterLevelList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
//                                        waterLevelNormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                    } else if (Float.parseFloat(value) >= Float.parseFloat(standardValue)) {
                                        scoreElectric = scoreElectric - 15;
                                        String state = "alarm";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                        electricList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
//                                        waterLevelList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
//                                        waterLevelAbnormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                    }
                                }
                                break;

                             case "11":

                                 if(online==true){

                                     String state = "normal";
                                     deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                 }else{

                                     deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));


                                 }



                                break;
                            case "12"://火焰探测器
                                if (online == false) {
                                    scoreFireAlarm = scoreFireAlarm - 5;
                                    deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
                                    fireAlarmList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
//                                    waterLevelList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
//                                    waterLevelOfflineList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
                                } else {
                                    if (Float.parseFloat(value) < Float.parseFloat(standardValue)) {
                                        String state = "normal";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                        fireAlarmList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
//                                        waterLevelList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
//                                        waterLevelNormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                    } else if (Float.parseFloat(value) >= Float.parseFloat(standardValue)) {
                                        scoreFireAlarm = scoreFireAlarm - 15;
                                        String state = "alarm";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                        fireAlarmList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
//                                        waterLevelList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
//                                        waterLevelAbnormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                    }
                                }
                                break;

                            case "13":
                                if (online == false) {
                                    scoreElectric = scoreElectric - 5;
                                    deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
                                    electricList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
//                                    waterLevelList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
//                                    waterLevelOfflineList.add(new DeviceItem(rtuID, typeID, type, position, value, "", online, lastTimestamp));
                                } else {
                                    if (Float.parseFloat(value) < Float.parseFloat(standardValue)) {
                                        String state = "normal";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                        electricList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
//                                        waterLevelList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
//                                        waterLevelNormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                    } else if (Float.parseFloat(value) >= Float.parseFloat(standardValue)) {
                                        scoreElectric = scoreElectric - 15;
                                        String state = "alarm";
                                        deviceList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                        electricList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
//                                        waterLevelList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
//                                        waterLevelAbnormalList.add(new DeviceItem(rtuID, typeID, type, position, value, state, online, lastTimestamp));
                                    }
                                }
                                break;
                            default:
                                break;
                        }

                    }
                }
//                drawPiePress.draw(pieWaterPress, MainActivity.this, waterPressNormalList.size(), waterPressAbnormalList.size(), waterPressOfflineList.size());
//                drawPieLevel.draw(pieWaterLevel, MainActivity.this, waterLevelNormalList.size(), waterLevelAbnormalList.size(), waterLevelOfflineList.size());
                makeUpList(deviceList);
                makeUpList(waterPressList);
                makeUpList(waterLevelList);
                makeUpList(fireAlarmList);
                makeUpList(electricList);
                facAdapter.notifyDataSetChanged();
                nWorkReceived = nWorkReceived + 3;
                ShowAssess();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };

    private void makeUpList(List list) {
        if (list.size() < 8) {
            int count = 8 - list.size();
            for (int i = 0; i < count; i++) {
                list.add(new DeviceItem("", "", "", "", "", "", true, ""));
            }
        }
    }


    //获取屏幕的宽度
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    //获取屏幕的高度
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    private void initData() {
        Intent intent = this.getIntent();
        unitID = intent.getStringExtra("unitID");
        unitName = intent.getStringExtra("unitName");
        URL_GET_PATROL_CARS = URL_GET_PATROL_CARS + unitID;
        URL_GET_CHECK_CARS = URL_GET_CHECK_CARS + unitID;
        URL_GET_SHOP_CARS = URL_GET_SHOP_CARS + unitID;
        URL_GET_PROBLEMS = URL_GET_PROBLEMS + unitID;
        URL_GET_FAC_INFO = URL_GET_FAC_INFO + unitID + "&cityID=" + cityID;

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
            int month = t.month;
            if (month == 1) {
                month = 12;
            } else {
                month = month - 1;
            }
            String monthDate = String.valueOf(year) + "-" + String.valueOf(month);

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
                            tvFireCheck.setTextColor(0XFF88CFFF);
                            tvFireCheck.setOnClickListener(MainActivity.this);
                            break;
                        case "2":
                            electricCheckPicList.add(new DocumentPicItem(id, picUrl, type, dateTime));
                            isElectricCheck = true;
                            tvElectricCheck.setText("已完成");
                            tvElectricCheck.setTextColor(0XFF88CFFF);
                            tvElectricCheck.setOnClickListener(MainActivity.this);

                            break;
                        case "3":
                            fireAssessPicList.add(new DocumentPicItem(id, picUrl, type, dateTime));
                            isFireAssess = true;
                            tvFireAssess.setText("已完成");
                            tvFireAssess.setTextColor(0XFF88CFFF);
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
                            tvMaintenance.setTextColor(0XFF88CFFF);
                            tvMaintenance.setOnClickListener(MainActivity.this);

                            break;
                        default:
//                            tvFireCheck.setText("未完成");
//                            tvElectricCheck.setText("未完成");
//                            tvFireAssess.setText("未完成");
//                            tvMaintenance.setText("未完成");
//
//                            tvFireCheck.setTextColor(0XFFFF0000);
//                            tvElectricCheck.setTextColor(0XFFFF0000);
//                            tvFireAssess.setTextColor(0XFFFF0000);
//                            tvMaintenance.setTextColor(0XFFFF0000);

                            break;
                    }
                    if (dateTime.equals(monthDate)) {
                        isMaintenanceMonth = true;
                        tvMaintenanceMonth.setText("已完成");
                        tvMaintenanceMonth.setTextColor(0XFF88CFFF);
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
//                    String fac = object.getString("type");
//                    String position = object.getString("position");
//                    String times = object.getString("times");
                    String statue = "";
//                    String imageName = object.getString("imageName");
//                    String problemID = object.getString("problemID");
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
        if (v == tvMaintenance) {
            viewCome(llDocument);
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
            //HorizontalListViewAdapter adapter = new HorizontalListViewAdapter(MainActivity.this, maintenancePicList);
//            hsPic.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
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
            changeAdapter(fireAlarmList);
            listDeviceType = ALARM_FAC;
        } else if (v == tvElectric) {
            changeTitleBG(tvElectric);
            changeAdapter(electricList);
            listDeviceType = ELECTRIC_FAC;
        } else if (v == llShopCheck) {
            Intent intent = new Intent(MainActivity.this, ShopCheck.class);
            intent.putExtra("unitID", unitID);
            startActivity(intent);
        }
    }

    private void changeTitleBG(View view) {
        tvAllFac.setBackgroundColor(0XFF336699);
        tvPress.setBackgroundColor(0XFF336699);
        tvLevel.setBackgroundColor(0XFF336699);
        tvFireAlarm.setBackgroundColor(0XFF336699);
        tvElectric.setBackgroundColor(0XFF336699);
        view.setBackgroundColor(0XFF0099CC);
    }

    private void changeAdapter(List list) {
        facAdapter = new FacAdapter(this, list);
        lvFac.setAdapter(facAdapter);
        facAdapter.notifyDataSetChanged();
    }


    private void viewGo(View view) {
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
        myAnimation.Come(llList, 500, 0, 0, -2000, 0);
        myAnimation.Come(llCheckPatrol, 500, 0, 0, 2000, 0);
    }

    private void viewCome(View view) {
        MyAnimation myAnimation = new MyAnimation();
        myAnimation.Go(llAssess, 500, 0, -2000, 0, 0);
        myAnimation.Go(llElectric, 500, 0, 0, 0, -2000);
        myAnimation.Go(llWater, 500, 0, 0, 0, -2000);
        myAnimation.Go(llAlarm, 500, 0, 2000, 0, 0);
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
            while (isAct) {
                try {
                    Message msg = new Message();
                    msg.arg1 = 1000;
                    handler.sendMessage(msg);
                    sleep(1000*60*60*3);

//                    if (i == 0) {
//                        i = 1;
//                    } else if (i == 1) {
//                        i = 0;
//                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    @Override
    protected void onStop() {
        super.onStop();
        isAct = false;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.arg1==1000){
                getData();
            }
//            int i = msg.arg1;
//            if (i == 0) {
////                tvCheckPatrol.setText("每月检查");
////                myAnimation.Go(piePatrol, 500, 0, 0, 0, 2000);
////                myAnimation.Come(pieCheck, 500, -2000, 0, 0, 0);
////                myAnimation.Go(pieWaterPress, 500, 0, 0, 0, 2000);
////                myAnimation.Come(pieWaterLevel, 500, 0, 0, -2000, 0);
//                drawPieAlarm.draw(pieAlarm, MainActivity.this, 4219, 1233, 663);
//                drawElectric.draw(pieElectric, MainActivity.this, 5, 2);
////                drawBarTraining.draw(barTraining);
//
//            } else if (i == 1) {
////                myAnimation.Go(pieCheck, 500, 0, -2000, 0, 0);
////                myAnimation.Come(piePatrol, 500, 0, 0, 2000, 0);
////                myAnimation.Go(pieWaterLevel, 500, 0, 0, 0, -2000);
////                myAnimation.Come(pieWaterPress, 500, 0, 0, 2000, 0);
//                drawPieAlarm.draw(pieAlarm, MainActivity.this, 4219, 1233, 663);
//                drawElectric.draw(pieElectric, MainActivity.this, 5, 2);
////                drawBarTraining.draw(barTraining);
//            }
////            webViewAssessUp.reload();
////            webViewAssessDown.reload();
        }

        ;
    };

    @Override
    protected void onPause() {
        super.onPause();
        isAct = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isAct = true;
    }

    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            myHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    //回退键控制
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (llDocument.getVisibility() == View.VISIBLE) {
                viewGo(llDocument);

                return true;
            }
            if (llMonth.getVisibility() == View.VISIBLE) {
                viewGo(llMonth);
                return true;
            }
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
            String url = "http://jn.xiaofang365.cn/xfwlw/xfwlw/checkVer.php?softName=unitTV";//版本控制链接，待定

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
        if (nWorkReceived == 7) {
            scoreTotal = scoreWaterPress + scoreWaterLevel + scoreElectric + scoreFireAlarm + scorePatrol + scoreCheck + scoreDayToDay;
            drawPieTitleScore.draw(pieTitleScore, MainActivity.this, (int) scoreTotal, 100 - (int) scoreTotal, false);
            tvScore.setText(String.valueOf((int) scoreTotal));


            showRadar();

        }
    }

}
