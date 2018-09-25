package com.example.dell.unittv;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.unittv.charts.DrawBarPatrol;
import com.example.dell.unittv.charts.DrawPieCheck;
import com.example.dell.unittv.charts.DrawPiePatrol;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by dell-pc on 2016/1/12.
 */
public class CheckPatrol extends Activity {

    //    GraphicalView graphicalView1, graphicalView2;
    double[] values1 = {148, 45};
    //double[] values2 = {215, 128, 55};

    //CategorySeries dataset2 = buildCategoryDataset("单位饼图", values2);

    public static String LABLE_TEXT[] = {"", "A", "B", "C", "D"};
    private LinearLayout llBarCheck;

    int rate1, rate2, rate3, rate4, rate5, rate6, rate7, rate8, rate9, rate10, rate11, rate12;
    Time time = new Time("GMT+8");
    int year;
    private double check[];
    private double second[] = new double[]{0, 0, 0, 0};
    private List<String> options = new ArrayList<String>();
    //private boolean isSingleView;
//    private BarChartViewCheck viewCheck;
    private TextView tvYear;
    RadioGroup rgCheck;
    RadioButton rbCheckEveryMonth, rbCheckEveryWeek;
    TextView tvCheckCardCount, tvCheckedCardCount, tvUnCheckCardCount, tvProblemCount, tvCheckRate;
    TextView tvCheckPeriod;

    String unitId, unitName;
    String checkCardCount, checkedCardCount, unCheckCardCount, problemCount, checkRate;
    String urlCheck;

    LinearLayout llPieCheck;
    LinearLayout llCard, llCheckedCard, llUnCheckCard, llProblemCard;


    String checkPeriod = "1", patrolPeriod = "2";

    private ProgressDialog pDialog;

    //public static final String GET_CHECKPATROLINFO_URL = "http://124.133.16.38:8093/test/mr/xfwlw/html/getCheckInfoJSON.php";
    public static final String GET_CHECKPATROLINFO_URL = "http://m.xiaofang365.cn/home/index/xjClientList";

    TextView tvPatroledCount, tvUnPatrolCount, tvAllPatrolCount;

    private PieChart piePatrol, pieCheck;
    private DrawPiePatrol drawPiePatrol = new DrawPiePatrol();
    private DrawPieCheck drawPieCheck = new DrawPieCheck();

    private BarChart barPatrol;
    private DrawBarPatrol drawBarPatrol = new DrawBarPatrol();

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_patrol);

        initData();
        initView();



        time.setToNow();
        year = time.year;
//
//
//        tvCheckPeriod = (TextView) findViewById(R.id.tvCheckPeriod);
//
//        rgCheck = (RadioGroup) findViewById(R.id.rgCheck);
//        rbCheckEveryMonth = (RadioButton) findViewById(R.id.rbCheckEveryMonth);
//        rbCheckEveryWeek = (RadioButton) findViewById(R.id.rbCheckEveryWeek);
//
//        tvCheckRate = (TextView) findViewById(R.id.tvCheckedRate);
//        tvYear = (TextView) findViewById(R.id.tvYear);
//        tvYear.setText(String.valueOf(year));
//
//        llCard = (LinearLayout) findViewById(R.id.llCard);
//        llCheckedCard = (LinearLayout) findViewById(R.id.llCheckedCard);
//        llUnCheckCard = (LinearLayout) findViewById(R.id.llUnCheckCard);
//        llProblemCard = (LinearLayout) findViewById(R.id.llProblemCard);
//
//
//        llPieCheck = (LinearLayout) findViewById(R.id.llPieCheck);
//
        Intent intent1 = this.getIntent();
        unitId = intent1.getStringExtra("unitID");
//        unitName = intent1.getStringExtra("unitName");
//        TextView tvUnitName = (TextView) findViewById(R.id.tvUnitName);
//        tvUnitName.setText(unitName);
//
//        rbCheckEveryMonth.setChecked(true);
//        getTrainingInfo();
//        llCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("unitId", unitId);
//                intent.putExtra("unitName",unitName);
//                intent.setClass(CheckPatrol.this, CheckCard.class);
//                startActivity(intent);
//            }
//        });
//
//        llCheckedCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("unitId", unitId);
//                intent.putExtra("unitName",unitName);
//                intent.putExtra("checkPeriod", checkPeriod);
//                intent.setClass(CheckPatrol.this, CheckedCard.class);
//                startActivity(intent);
//            }
//        });
//
//        llProblemCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("unitId", unitId);
//                intent.putExtra("unitName",unitName);
//                intent.putExtra("checkPeriod", checkPeriod);
//                intent.setClass(CheckPatrol.this, CheckProblem.class);
//                startActivity(intent);
//            }
//        });
//
//        llUnCheckCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("unitId", unitId);
//                intent.putExtra("unitName",unitName);
//                intent.putExtra("checkPeriod", checkPeriod);
//                intent.setClass(CheckPatrol.this, UnCheckCard.class);
//                startActivity(intent);
//            }
//        });
//
//
//        rgCheck.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                int radioButtonId = group.getCheckedRadioButtonId();
//                RadioButton rb = (RadioButton)findViewById(radioButtonId);
//                if(rb.getText().equals("每月")) {
//                    checkPeriod = "1";
//                }
//                else {
//                    checkPeriod = "2";
//                }
//                urlCheck = GET_CHECKPATROLINFO_URL + "?unitId=" + unitId + "&&type=" + checkPeriod;
//                System.out.println(urlCheck);
//                new GetCheckInfo().execute();
//            }
//        });


        urlCheck = GET_CHECKPATROLINFO_URL + "?unitId=" + unitId + "&type=" + checkPeriod;
//
//        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        System.out.println(urlCheck);
//
//        new GetCheckInfo().execute();



    }

    private void initData(){

    }

    private void initView(){
        piePatrol = (PieChart) findViewById(R.id.piePatrol);
        pieCheck = (PieChart) findViewById(R.id.pieCheck);
        barPatrol = (BarChart) findViewById(R.id.barPatrol);
        drawBarPatrol.draw(barPatrol);
        tvCheckCardCount = (TextView) findViewById(R.id.tvCheckCardCount);
        tvCheckedCardCount = (TextView) findViewById(R.id.tvCheckedCardCount);
        tvUnCheckCardCount = (TextView) findViewById(R.id.tvUnCheckCardCount);
        tvProblemCount = (TextView) findViewById(R.id.tvProblemCount);

        tvPatroledCount = (TextView) findViewById(R.id.tvPatroledCardCount);
        tvUnPatrolCount = (TextView) findViewById(R.id.tvUnPatrolCardCount);
        tvAllPatrolCount = (TextView) findViewById(R.id.tvPatrolCardCount);

        tvPatroledCount.setText(String.valueOf(MainActivity.patroledItemList.size()));
        tvUnPatrolCount.setText(String.valueOf(MainActivity.unPatrolItemList.size()));
        tvAllPatrolCount.setText(String.valueOf(MainActivity.patroledItemList.size() + MainActivity.unPatrolItemList.size()));

        int patrolCount = MainActivity.patroledItemList.size();
        int unPatrolCount = MainActivity.unPatrolItemList.size();

        drawPiePatrol.draw(piePatrol,CheckPatrol.this,patrolCount,unPatrolCount,true);
    }





    class GetCheckInfo extends AsyncTask<String, String, String> {
        String CheckPrl, PatrolPrl;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CheckPatrol.this);
            pDialog.setMessage("请稍后...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            Log.d("Log_tag", "onPreExecute...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn;
            InputStream is;
            try {
                conn = (HttpURLConnection) new URL(urlCheck).openConnection();
                conn.setRequestMethod("GET");
                is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line = "";
                StringBuilder result = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                    String jsonData = result.toString();
                    JSONObject object = new JSONObject(jsonData);
                    checkCardCount = object.getString("checkCardCount");
                    checkedCardCount = object.getString("checkedCardCount");
                    problemCount = object.getString("problemCount");
                    //System.out.println("checkCardCount===================" + checkCardCount);
                    //patrolCardCount = object.getString("patrolCardCount");
                    //patroledCardCount = object.getString("patroledCardCount");
                    CheckPrl = object.getString("startTime");
                    //PatrolPrl = object.getString("patrolStartTime");

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
            if (checkedCardCount != null) {

//                int checkCard = Integer.parseInt(checkCardCount);
//                int checked = Integer.parseInt(checkedCardCount);
//                int unCheck = checkCard - checked;


                int checkCard = Integer.parseInt(checkCardCount);
                int checked = Integer.parseInt(checkedCardCount);
                int unCheck = checkCard - checked;




//                tvCheckPeriod.setText(TimeManager.getStrTime2(CheckPrl) + "  至今");
                tvCheckCardCount.setText(checkCardCount + " ");
                tvCheckedCardCount.setText(checkedCardCount + " ");
                tvProblemCount.setText(problemCount + " ");


//                values1 = new double[]{checked, unCheck};
////                CategorySeries dataset1 = buildCategoryDataset("单位饼图", values1);
//                int[] colors = {0xFF365D96, 0X99FF0000};
////                DefaultRenderer renderer1 = buildCategoryRenderer(colors);
////                DefaultRenderer renderer2 = buildCategoryRenderer(colors);
////                //graphicalView=ChartFactory.getBarChartView(getBaseContext(), dataset, renderer, type);//柱状图
////                graphicalView1= ChartFactory.getPieChartView(getBaseContext(), dataset1, renderer1);//饼状图
//                //graphicalView2=ChartFactory.getPieChartView(getBaseContext(), dataset2, renderer2);//饼状图
//                llPieCheck.removeAllViews();
//                llPieCheck.setBackgroundColor(Color.WHITE);
//                llPieCheck.addView(graphicalView1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));


                unCheckCardCount = String.valueOf(unCheck);

                tvUnCheckCardCount.setText(unCheckCardCount + " ");
                //if(checkedCardCount.equals("0")){

                //}
                //else {
//                int rateC = Math.round((float) checked / checkCard * 100);
//                String strRateC = String.valueOf(rateC);
//                if (rateC <= 60) {
//                    tvCheckRate.setTextColor(0xffAA0000);
//                } else {
//                    tvCheckRate.setTextColor(0xff003D99);
//                }
//                tvCheckRate.setText(strRateC + " ");
                //Pie(checked,unCheck,llPieCheck);
                //}
            }
            /*if(patroledCardCount != null) {
                tvPatrolPeriod.setText(TimeManager.getStrTime1(PatrolPrl) + "  至今");
                tvPatrolCardCount.setText(patrolCardCount);
                tvPatroledCardCount.setText(patroledCardCount);
                int patrolCard = Integer.parseInt(patrolCardCount);
                int patroled = Integer.parseInt(patroledCardCount);
                int unPatrol = patrolCard - patroled;
                unPatrolCardCount = String.valueOf(unPatrol);

                tvUnPatrolCardCount.setText(unPatrolCardCount);
                //if(patrolCardCount.equals("0")){

                //}
                //else {
                    float rateP = (float)Math.round((float)patroled/patrolCard*1000)/10;
                    System.out.println(rateP);
                    String strRateP = String.valueOf(rateP);
                    if(rateP <= 60){
                        tvPatrolRate.setTextColor(0xffFF0033);
                    }
                    else {
                        tvCheckRate.setTextColor(0xee008836);
                    }
                    tvPatrolRate.setText(strRateP);
                    Pie(patroled,unPatrol,llPiePatrol);
                //}
            }*/
            else {
                Toast toast = Toast.makeText(CheckPatrol.this, "网络错误！", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

        }
    }

    public List<ddd> getData(int workedCardCount, int unWorkedCardCount) {

        List<ddd> l = new ArrayList<ddd>();

        ddd d1 = new ddd();
        d1.setDoor("已检查");
        d1.setTotal(workedCardCount);

        ddd d2 = new ddd();
        d2.setDoor("未检查");
        d2.setTotal(unWorkedCardCount);

        l.add(d1);
        l.add(d2);

        return l;
    }


    class ddd {

        String door;

        double total;

        public String getDoor() {
            return door;
        }

        public void setDoor(String door) {
            this.door = door;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

    }


    private void getTrainingInfo() {

        getTCount();

    }

    private void getTCount() {
        new GetTrainingCount().execute();
    }

    class GetTrainingCount extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection conn;
            InputStream is;
            //String url = "http://124.133.16.38:8093/test/mr/xfwlw/html/getTrainingCountJSON.php?unitId=" + unitId;
            String url = "http://m.xiaofang365.cn/home/index/yearXj?unitId=" + unitId;
            System.out.println("url=================" + url);
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
                    rate1 = Integer.parseInt(object.getString("t1"));
                    rate2 = Integer.parseInt(object.getString("t2"));
                    rate3 = Integer.parseInt(object.getString("t3"));
                    rate4 = Integer.parseInt(object.getString("t4"));
                    rate5 = Integer.parseInt(object.getString("t5"));
                    rate6 = Integer.parseInt(object.getString("t6"));
                    rate7 = Integer.parseInt(object.getString("t7"));
                    rate8 = Integer.parseInt(object.getString("t8"));
                    rate9 = Integer.parseInt(object.getString("t9"));
                    rate10 = Integer.parseInt(object.getString("t10"));
                    rate11 = Integer.parseInt(object.getString("t11"));
                    rate12 = Integer.parseInt(object.getString("t12"));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
//            showTrainingTable();
        }
    }


}
