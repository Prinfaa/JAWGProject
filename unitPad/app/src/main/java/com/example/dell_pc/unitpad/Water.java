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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell_pc.unitpad.adapter.Checked1Adapter;
import com.example.dell_pc.unitpad.adapter.DocumentPicAdapter;
import com.example.dell_pc.unitpad.adapter.FacAdapter;
import com.example.dell_pc.unitpad.adapter.PatroledAdapter;
import com.example.dell_pc.unitpad.adapter.UnCheckAdapter;
import com.example.dell_pc.unitpad.adapter.UnPatrolAdapter;
import com.example.dell_pc.unitpad.charts.DrawBarTraining;
import com.example.dell_pc.unitpad.charts.DrawPieAlarm;
import com.example.dell_pc.unitpad.charts.DrawPieCheck;
import com.example.dell_pc.unitpad.charts.DrawPieElectric;
import com.example.dell_pc.unitpad.charts.DrawPieLevel;
import com.example.dell_pc.unitpad.charts.DrawPiePatrol;
import com.example.dell_pc.unitpad.charts.DrawPiePress;
import com.example.dell_pc.unitpad.charts.LineChartManager;
import com.example.dell_pc.unitpad.item.CheckedItem;
import com.example.dell_pc.unitpad.item.DeviceItem;
import com.example.dell_pc.unitpad.item.DocumentPicItem;
import com.example.dell_pc.unitpad.item.PatroledItem;
import com.example.dell_pc.unitpad.item.UnCheckItem;
import com.example.dell_pc.unitpad.item.UnPatrolItem;
import com.example.dell_pc.unitpad.tools.DownLoadManager;
import com.example.dell_pc.unitpad.tools.HttpUtils;
import com.example.dell_pc.unitpad.tools.MyAnimation;
import com.example.dell_pc.unitpad.tools.MyWebViewSet;
import com.example.dell_pc.unitpad.tools.downImageTask;
import com.ezvizuikit.open.EZUIError;
import com.ezvizuikit.open.EZUIKit;
import com.ezvizuikit.open.EZUIPlayer;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Water extends Activity implements View.OnClickListener {


    private String unitID, unitName;


    private PieChart pieWaterPress, pieWaterLevel;
    private LineChart lineChart;

    private DrawPiePress drawPiePress = new DrawPiePress();
    private DrawPieLevel drawPieLevel = new DrawPieLevel();

    private ListView lv;
    private FacAdapter facAdapter;
    private String URL_GET_24_INFO = "http://dndzl.cn/daping/twefourRecord.php?rtuID=";
    private String listShow = "waterPressNormalList";
    private String description = "24小时水压曲线（Mpa）";
    private String facTypeIDSel = "";


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
        setContentView(R.layout.water);

        initData();
        initView();

//        drawPiePress.draw(pieWaterPress, this, MainActivity.waterPressNormalList.size(), MainActivity.waterPressAbnormalList.size(), MainActivity.waterPressOfflineList.size());
//        drawPieLevel.draw(pieWaterLevel, this, MainActivity.waterLevelNormalList.size(), MainActivity.waterLevelAbnormalList.size(), MainActivity.waterLevelOfflineList.size());
        pieClickListener(pieWaterPress);
        pieClickListener(pieWaterLevel);

//        facTypeIDSel = MainActivity.waterPressNormalList.get(0).getTypeID();
//        String rtuID = MainActivity.waterPressNormalList.get(0).getRtuID();
//        String url = URL_GET_24_INFO + rtuID;

//        System.out.println("url===================" + url);
//        HttpUtils.getJSON(url, get24HoursDeviceValueHandler);
        lineChart.setDescription(description);
        lineChart.setDescriptionTextSize(30);
        lineChart.setDescriptionPosition(1200, 500);

    }


    private void initData() {
        Intent intent = this.getIntent();
        unitID = intent.getStringExtra("unitID");
        unitName = intent.getStringExtra("unitName");
    }

    private void initView() {
        pieWaterPress = (PieChart) findViewById(R.id.pieWaterPress);
        pieWaterLevel = (PieChart) findViewById(R.id.pieWaterLevel);
        lv = (ListView) findViewById(R.id.lv);
//        facAdapter = new FacAdapter(Water.this, MainActivity.waterPressNormalList);
        lv.setAdapter(facAdapter);
        facAdapter.notifyDataSetChanged();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String rtuID = "", url = "";
                lineChart.setVisibility(View.VISIBLE);
                switch (listShow) {
                    case "waterPressNormalList":
//                        rtuID = MainActivity.waterPressNormalList.get(position).getRtuID();
//                        facTypeIDSel = MainActivity.waterPressNormalList.get(position).getTypeID();
                        description = "24小时水压曲线（Mpa）";
                        break;
                    case "waterPressAbnormalList":
//                        rtuID = MainActivity.waterPressAbnormalList.get(position).getRtuID();
//                        facTypeIDSel = MainActivity.waterPressAbnormalList.get(position).getTypeID();
                        description = "24小时水压曲线（Mpa）";
                        break;
                    case "waterPressOfflineList":
                        lineChart.setVisibility(View.INVISIBLE);
//                        rtuID = MainActivity.waterPressOfflineList.get(position).getRtuID();
//                        facTypeIDSel = MainActivity.waterPressOfflineList.get(position).getTypeID();
                        description = "24小时水压曲线（Mpa）";
                        break;
                    case "waterLevelNormalList":
//                        rtuID = MainActivity.waterLevelNormalList.get(position).getRtuID();
//                        facTypeIDSel = MainActivity.waterLevelNormalList.get(position).getTypeID();
                        description = "24小时水位曲线（%）";
                        break;
                    case "waterLevelAbnormalList":
//                        rtuID = MainActivity.waterLevelAbnormalList.get(position).getRtuID();
//                        facTypeIDSel = MainActivity.waterLevelAbnormalList.get(position).getTypeID();
                        description = "24小时水位曲线（%）";
                        break;
                    case "waterLevelOfflineList":
                        lineChart.setVisibility(View.INVISIBLE);
//                        rtuID = MainActivity.waterLevelOfflineList.get(position).getRtuID();
//                        facTypeIDSel = MainActivity.waterLevelOfflineList.get(position).getTypeID();
                        description = "24小时水位曲线（%）";
                        break;

                    default:
                        break;
                }
                url = URL_GET_24_INFO + rtuID;
                HttpUtils.getJSON(url, get24HoursDeviceValueHandler);
                lineChart.setDescription(description);

            }
        });


        lineChart = (LineChart) findViewById(R.id.lineChart);

    }


    private void pieClickListener(final PieChart pieChart) {
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                String rtuID = "";
                String url = "";
                if (pieChart == pieWaterPress) {
                    switch (entry.getXIndex()) {
                        case 0:
//                            facAdapter = new FacAdapter(Water.this, MainActivity.waterPressNormalList);
//                            lv.setAdapter(facAdapter);
//                            facAdapter.notifyDataSetChanged();
//                            listShow = "waterPressNormalList";
//                            lineChart.setVisibility(View.VISIBLE);
//                            rtuID = MainActivity.waterPressNormalList.get(0).getRtuID();
//                            facTypeIDSel = MainActivity.waterPressNormalList.get(0).getTypeID();
//                            url = URL_GET_24_INFO + rtuID;
//                            HttpUtils.getJSON(url, get24HoursDeviceValueHandler);
//                            description = "24小时水压曲线（Mpa）";
                            break;
                        case 1:
//                            facAdapter = new FacAdapter(Water.this, MainActivity.waterPressAbnormalList);
//                            lv.setAdapter(facAdapter);
//                            facAdapter.notifyDataSetChanged();
//                            listShow = "waterPressAbnormalList";
//                            lineChart.setVisibility(View.VISIBLE);
//                            rtuID = MainActivity.waterPressAbnormalList.get(0).getRtuID();
//                            facTypeIDSel = MainActivity.waterPressAbnormalList.get(0).getTypeID();
//                            url = URL_GET_24_INFO + rtuID;
//                            HttpUtils.getJSON(url, get24HoursDeviceValueHandler);
//                            description = "24小时水压曲线（Mpa）";
                            break;
                        case 2:
//                            facAdapter = new FacAdapter(Water.this, MainActivity.waterPressOfflineList);
//                            lv.setAdapter(facAdapter);
//                            facAdapter.notifyDataSetChanged();
//                            listShow = "waterPressOfflineList";
//                            lineChart.setVisibility(View.INVISIBLE);
                            break;
                    }
                } else if (pieChart == pieWaterLevel) {
                    switch (entry.getXIndex()) {
                        case 0:
//                            facAdapter = new FacAdapter(Water.this, MainActivity.waterLevelNormalList);
//                            lv.setAdapter(facAdapter);
//                            facAdapter.notifyDataSetChanged();
//                            listShow = "waterLevelNormalList";
//                            lineChart.setVisibility(View.VISIBLE);
//                            rtuID = MainActivity.waterLevelNormalList.get(0).getRtuID();
//                            facTypeIDSel = MainActivity.waterLevelNormalList.get(0).getTypeID();
//                            url = URL_GET_24_INFO + rtuID;
//                            HttpUtils.getJSON(url, get24HoursDeviceValueHandler);
//                            description = "24小时水位曲线（%）";
                            break;
                        case 1:
//                            facAdapter = new FacAdapter(Water.this, MainActivity.waterLevelAbnormalList);
//                            lv.setAdapter(facAdapter);
//                            facAdapter.notifyDataSetChanged();
//                            listShow = "waterLevelAbnormalList";
//                            lineChart.setVisibility(View.VISIBLE);
//                            rtuID = MainActivity.waterLevelAbnormalList.get(0).getRtuID();
//                            facTypeIDSel = MainActivity.waterLevelAbnormalList.get(0).getTypeID();
//                            url = URL_GET_24_INFO + rtuID;
//                            HttpUtils.getJSON(url, get24HoursDeviceValueHandler);
//                            description = "24小时水位曲线（%）";
                            break;
                        case 2:
//                            facAdapter = new FacAdapter(Water.this, MainActivity.waterLevelOfflineList);
//                            lv.setAdapter(facAdapter);
//                            facAdapter.notifyDataSetChanged();
//                            listShow = "waterLevelOfflineList";
//                            lineChart.setVisibility(View.INVISIBLE);
                            break;
                    }
                }
                lineChart.setDescription(description);

            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void drawLine(ArrayList<Entry> yValueMax, ArrayList<Entry> yValueMin, ArrayList<Entry> yValueRefer) {
        //设置x轴的数据
        ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd日HH时");
            Date curDate = new Date(System.currentTimeMillis() - 3600000 * (24 - i));//获取当前时间
            String str = formatter.format(curDate);
            xValues.add("" + str);
        }


        //设置折线的名称
        LineChartManager.setLineName1("最高值");
        LineChartManager.setLineName2("最低值");
        LineChartManager.setLineName3("参考值");

        //创建三条折线的图表
//        LineChartManager.initThreeLineChart(this, lineChart, xValues, yValueMax, yValueMin, yValueRefer);
        LineChartManager.initThreeLineChart(this, lineChart, xValues, yValueMax, yValueMin, yValueRefer);
    }


    //获取本单位所有卡片当月检查资料
    private Handler get24HoursDeviceValueHandler = new Handler() {


        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;
            ArrayList<Entry> yValueMax = new ArrayList<>();
            ArrayList<Entry> yValueMin = new ArrayList<>();
            ArrayList<Entry> yValueRefer = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    float maxValue = Float.parseFloat(object.getString("maxValue"));
                    float minValue = Float.parseFloat(object.getString("minValue"));
                    if (minValue == 1000) {
                        minValue = 0;
                    }
                    if (facTypeIDSel.equals("1")) {
                        yValueRefer.add(new Entry(0.05f, i));
                    } else if (facTypeIDSel.equals("2")) {
                        yValueRefer.add(new Entry(0.1f, i));
                    } else {
                        yValueRefer.add(new Entry(80f, i));
                    }
                    yValueMax.add(new Entry(maxValue, i));
                    yValueMin.add(new Entry(minValue, i));
                }
                drawLine(yValueMax, yValueMin, yValueRefer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };

    @Override
    public void onClick(View v) {
    }


    //回退键控制
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
