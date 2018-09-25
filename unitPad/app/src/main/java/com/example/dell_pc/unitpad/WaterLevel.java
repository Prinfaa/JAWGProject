package com.example.dell_pc.unitpad;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell_pc.unitpad.adapter.FacAdapter;
import com.example.dell_pc.unitpad.charts.DrawPieLevel;
import com.example.dell_pc.unitpad.charts.DrawPiePress;
import com.example.dell_pc.unitpad.charts.LineChartManager;
import com.example.dell_pc.unitpad.tools.HttpUtils;
import com.example.dell_pc.unitpad.tools.MyWebViewSet;
import com.example.dell_pc.unitpad.tools.TimeManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WaterLevel extends Activity implements View.OnClickListener {


    private String unitID, unitName, deviceID;


    private PieChart pieWaterPress, pieWaterLevel;
    private LineChart lineChart;
    private TextView tvFac, tvPosition, tvStatue, tvTime, tvIntroduce;


    private DrawPiePress drawPiePress = new DrawPiePress();
    private DrawPieLevel drawPieLevel = new DrawPieLevel();

    private ListView lv;
    private FacAdapter facAdapter;
    private String URL_GET_24_INFO = "http://dndzl.cn/daping/twefourRecord.php?rtuID=";
    private String listShow = "waterPressNormalList";
    private String description = "24小时水位曲线（%）";
    private String facTypeID = "";
    private String fac, position, statue, time, value;

    private WebView webView;
    private String URL_WATER_LEVEL_CHART = "http://jn.xiaofang365.cn/xfwlw/xfwlw/waterLevelChart.php";

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
        setContentView(R.layout.water_level);

        initData();
        initView();

//        drawPiePress.draw(pieWaterPress, this, MainActivity.waterPressNormalList.size(), MainActivity.waterPressAbnormalList.size(), MainActivity.waterPressOfflineList.size());
//        drawPieLevel.draw(pieWaterLevel, this, MainActivity.waterLevelNormalList.size(), MainActivity.waterLevelAbnormalList.size(), MainActivity.waterLevelOfflineList.size());


        String url = URL_GET_24_INFO + deviceID;
        System.out.println("jjjjjjjjjjkkkk============="+url);
        HttpUtils.getJSON(url, get24HoursDeviceValueHandler);
        lineChart.setDescription(description);
        lineChart.setDescriptionTextSize(30);
        lineChart.setDescriptionPosition(1200, 500);

    }


    private void initData() {
        Intent intent = this.getIntent();
        unitID = intent.getStringExtra("unitID");
        unitName = intent.getStringExtra("unitName");
        deviceID = intent.getStringExtra("deviceID");
        facTypeID = intent.getStringExtra("facTypeID");
        fac = intent.getStringExtra("fac");
        position = intent.getStringExtra("position");
        statue = intent.getStringExtra("statue");
        time = intent.getStringExtra("time");
        value = intent.getStringExtra("value");


        URL_WATER_LEVEL_CHART = URL_WATER_LEVEL_CHART + "?value=" + value;
    }

    private void initView() {
        pieWaterPress = (PieChart) findViewById(R.id.pieWaterPress);
        pieWaterLevel = (PieChart) findViewById(R.id.pieWaterLevel);
        lv = (ListView) findViewById(R.id.lv);
        facAdapter = new FacAdapter(WaterLevel.this, MainActivity.waterPressList);
        lv.setAdapter(facAdapter);
        facAdapter.notifyDataSetChanged();

        webView = (WebView) findViewById(R.id.wvWaterPress);
        webView.loadUrl(URL_WATER_LEVEL_CHART);
        MyWebViewSet.Set(webView);

        tvFac = (TextView) findViewById(R.id.tvFac);
        tvPosition = (TextView) findViewById(R.id.tvPosition);
        tvStatue = (TextView) findViewById(R.id.tvStatue);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvIntroduce = (TextView) findViewById(R.id.tvIntroduce);

        tvIntroduce.setText("");

        tvFac.setText(fac);
        tvPosition.setText(position);
        if (statue.equals("normal")) {
            tvStatue.setText("水位正常");
        } else if (statue.equals("alarm")) {
            tvStatue.setText("水位不足");
        } else {
            tvStatue.setText("设备离线");
        }
        tvTime.setText(TimeManager.getStrTime4(time));


        lineChart = (LineChart) findViewById(R.id.lineChart);

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
                    if (facTypeID.equals("1")) {
                        yValueRefer.add(new Entry(0.05f, i));
                    } else if (facTypeID.equals("2")) {
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
