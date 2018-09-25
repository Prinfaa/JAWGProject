package com.example.dell_pc.qr_code_patrol;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell_pc.qr_code_patrol.adpter.CheckInfoAdapter;
import com.example.dell_pc.qr_code_patrol.item.CheckCardItem;
import com.example.dell_pc.qr_code_patrol.item.CheckedItem;
import com.example.dell_pc.qr_code_patrol.tools.DownLoadManager;
import com.example.dell_pc.qr_code_patrol.tools.HttpUtils;
import com.example.dell_pc.qr_code_patrol.view.RoundCornerImageView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PatrolHome extends AppCompatActivity implements OnChartValueSelectedListener {

    private PieChart mPieChart;
    int nCountChecked, nCountUnchecked;
    float fCheckRate;
    List<CheckCardItem> cardList = new ArrayList<CheckCardItem>();
    List<CheckedItem> checkedList = new ArrayList<CheckedItem>();
    List<CheckedItem> uncheckedList = new ArrayList<CheckedItem>();
    List<CheckedItem> showList = new ArrayList<CheckedItem>();
    private String localVersion;

    CheckInfoAdapter adapter;
    ListView lvShow;
    Button btnCheck;
    String unitId, unitName;
    Typeface mTf; // 自定义显示字体
    private URL downLoadNewVerUrl;

    private TextView tvFac, tvPosition, tvStatue, tvNote, tvTime;
    private LinearLayout llPage1, llPage2;
    private RoundCornerImageView ivCheckPic;
    public static int nStatue = 0;
    public static final String GET_CHECKCARD_URL = "http://api.xfyun.net/app/Number/NumberList";
    public static final String GET_CHECK_RECORD_URL = "http://api.xfyun.net/App/Number/InspectList";

    //获取本单位所有卡片资料
    private Handler getCheckCardHandler = new Handler() {


        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;
            cardList.clear();
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String cardNo = object.getString("cardno");
                    String Position = object.getString("position");
                    String Fac = object.getString("fac");
                    cardList.add(new CheckCardItem(cardNo, Position, Fac));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            HttpUtils.getJSON(GET_CHECK_RECORD_URL + "?business=" + unitId, getCheckRecordHandler);
        }

        ;
    };


//    private Handler getCheckRecordHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            nCountChecked = 0;
//            checkedList.clear();
//
//            String jsonData = (String) msg.obj;
//            try {
//                JSONArray jsonArray = new JSONArray(jsonData);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    int isExist = 0;
//                    JSONObject object = jsonArray.getJSONObject(i);
//                    String strCardNo = object.getString("cardno");
//                    String strPosition = object.getString("position");
//                    String strFac = object.getString("fac");
//                    String strStatue = object.getString("statue");
//                    String strInfo = object.getString("info");
//                    String strTime = object.getString("time").substring(11, 16);
//                    String strImageUrl = "http://api.xfyun.net" + object.getString("url").substring(1,object.getString("url").length());
//                    System.out.println("url=======" + strImageUrl);
//                    for (int j = 0; j < checkedList.size(); j++) {
//                        if (strCardNo.equals(checkedList.get(j).getCardNo())) {
//                            isExist++;
//                        }
//                    }
//                    if (isExist == 0) {
//                        checkedList.add(new CheckedItem(strCardNo, strPosition, strTime, strImageUrl, strFac, strInfo, strStatue));
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            nCountChecked = checkedList.size();
//            nCountUnchecked = cardList.size() - nCountChecked;
//            if(cardList.size()!=0){
//                fCheckRate = nCountChecked * 1000 / cardList.size() / 10;
//            }
//
//            for (int i = 0; i < cardList.size(); i++) {
//                int isExist = 0;
//                String strCardNo = cardList.get(i).getCardNo();
//                for (int j = 0; j < checkedList.size(); j++) {
//                    if (strCardNo.equals(checkedList.get(j).getCardNo())) {
//                        isExist++;
//                    }
//                }
//                if (isExist == 0) {
//                    uncheckedList.add(new CheckedItem(cardList.get(i).getCardNo(),
//                            cardList.get(i).getPosition(),
//                            "", "",
//                            cardList.get(i).getFac(),
//                            "", ""));
//                }
//            }
//
//
//            drawPieChart();
//
//
//        }
//
//        ;
//    };

    private Handler getPicHandler = new Handler() {
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap) msg.obj;

            int screenWidth;
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            screenWidth = metric.widthPixels;     // 屏幕宽度（像素）

            double w = bitmap.getWidth();
            double h = bitmap.getHeight();
            double scale;
            if (w > h) {
                scale = 0.9;
            } else {
                scale = 0.8;
            }
            double r = h / w;
            ivCheckPic.setLayoutParams(new LinearLayout.LayoutParams((int) (screenWidth * scale), (int) (screenWidth * r * scale)));
            ivCheckPic.setImageBitmap(bitmap);
        };
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkVer();

        Intent intent1 = this.getIntent();
        unitId = intent1.getStringExtra("unitId");
        unitName = intent1.getStringExtra("unitName");


        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Semibold.ttf");

        llPage1 = (LinearLayout) findViewById(R.id.ll_page1);
        llPage2 = (LinearLayout) findViewById(R.id.ll_page2);
        tvFac = (TextView) findViewById(R.id.tvFacDetail);
        tvPosition = (TextView) findViewById(R.id.tvPositionDetail);
        tvStatue = (TextView) findViewById(R.id.tvStatueDetail);
        tvNote = (TextView) findViewById(R.id.tvNoteDetail);
        tvTime = (TextView) findViewById(R.id.tvTimeDetail);
        ivCheckPic = (RoundCornerImageView) findViewById(R.id.ivCheck);

        btnCheck = (Button) findViewById(R.id.btn_check);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("unitId", unitId);
                intent.putExtra("unitName", unitName);
                intent.setClass(PatrolHome.this, Patrol.class);
                startActivity(intent);

            }
        });
        lvShow = (ListView) findViewById(R.id.lv_list);

        adapter = new CheckInfoAdapter(this, showList);
        lvShow.setAdapter(adapter);

        lvShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(nStatue == 0){
                    llPage1.setVisibility(View.GONE);
                    llPage2.setVisibility(View.VISIBLE);
                    tvFac.setText(checkedList.get(i).getFac());
                    tvPosition.setText(checkedList.get(i).getPosition());
                    if(checkedList.get(i).getStatue().equals("0")){
                        tvStatue.setText("正常");
                        tvStatue.setTextColor(0XFF038DAF);
                        tvNote.setTextColor(0XFF038DAF);
                    }else{
                        tvStatue.setText("不正常");
                        tvStatue.setTextColor(0XFFF9863A);
                        tvNote.setTextColor(0XFFF9863A);
                    }
//                    tvNote.setText(checkedList.get(i).getNote());
                    tvTime.setText(checkedList.get(i).getDateTime());
                    String imgUrl = checkedList.get(i).getImageName();
                    HttpUtils.getPicBitmap(imgUrl,getPicHandler);

                }
            }
        });

    }

    @Override

    protected void onResume() {
        super.onResume();
        String url = GET_CHECKCARD_URL + "?unitid=" + unitId;
        HttpUtils.getJSON(url, getCheckCardHandler);
    }


    private void drawPieChart() {
        mPieChart = (PieChart) findViewById(R.id.pie);
        PieData mPieData = getPieData(2, 100);
        showPieChart(mPieChart, mPieData);
        mPieChart.setOnChartValueSelectedListener(this);

    }


    private PieData getPieData(int count, float range) {
        ArrayList<String> xValues = new ArrayList<String>(); //用来表示每个饼块上的内容
        String[] content = new String[]{"已巡检", "未巡检"};
        for (int i = 0; i < count; i++) {
            xValues.add(content[i]);
        }

        ArrayList<Entry> yValue = new ArrayList<Entry>(); //用来表示封装每个饼块的实际数据

        List<Float> qs = new ArrayList<Float>();
        qs.add((float) nCountChecked);
        qs.add((float) nCountUnchecked);
        for (int i = 0; i < qs.size(); i++) {
            yValue.add(new Entry(qs.get(i), i));

        }

        PieDataSet pieDataSet = new PieDataSet(yValue, "");
        pieDataSet.setSliceSpace(0f);
        final ArrayList<Integer> colors = new ArrayList<Integer>();
        //饼图颜色

        colors.add(Color.rgb(114, 188, 223));
        colors.add(Color.rgb(255, 123, 124));

        pieDataSet.setColors(colors); //设置颜色
        pieDataSet.setValueTextSize(8f);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                switch (entry.getXIndex()) {
                    case 0:
                        return String.valueOf(nCountChecked) + "处";
                    case 1:
                        return String.valueOf(nCountUnchecked) + "处";
                }
                return "";
            }
        });
        //pieDataSet.setValueTypeface(mTf); //设置字体样式
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); //选中态多出的长度
        PieData pieData = new PieData(xValues, pieDataSet);
        return pieData;
    }

    private void showPieChart(PieChart pieChart, PieData pieData) {
        pieChart.setHoleColorTransparent(true);

        pieChart.setHoleRadius(60f); //半径
        pieChart.setTransparentCircleRadius(70f); //半透明圈

        pieChart.setDescription("");

        pieChart.setDrawHoleEnabled(true);
        pieChart.setCenterTextSize(30f);
        pieChart.setRotationAngle(270); //初始角度
        pieChart.setRotationEnabled(true); //可以手动旋转
        pieChart.setUsePercentValues(true); //显示百分比
        pieChart.getLegend().setEnabled(false);//隐藏图例
        pieChart.setDrawCenterText(true); //饼状图中间可以添加文字
        pieChart.setCenterText(String.valueOf((int) fCheckRate) + "%");
        if (fCheckRate < 60) {
            pieChart.setCenterTextColor(0X88FF0000);
        } else {
            pieChart.setCenterTextColor(0XFF038DAF);
        }
        pieChart.setCenterTextTypeface(mTf);

        pieChart.setData(pieData);

        Legend mLegend = pieChart.getLegend(); //设置比例图
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART); //坐右边显示
        mLegend.setXEntrySpace(10f);
        mLegend.setYEntrySpace(5f);
        //mLegend.setTypeface(mTf);
        mLegend.setTextColor(Color.GRAY);

        pieChart.animateXY(1000, 1000);


    }


    @Override
    public void onValueSelected(Entry entry, int i, Highlight highlight) {
        if (entry.getXIndex() == 0) {
            nStatue = 0;
            showList.clear();
            for (int j = 0; j < checkedList.size(); j++) {
                showList.add(checkedList.get(j));
            }
            adapter.notifyDataSetChanged();
            lvShow.setSelection(0);
        } else if (entry.getXIndex() == 1) {
            nStatue = 1;
            showList.clear();
            for (int j = 0; j < uncheckedList.size(); j++) {
                showList.add(uncheckedList.get(j));
            }
            adapter.notifyDataSetChanged();
            lvShow.setSelection(0);

        }

    }

    @Override
    public void onNothingSelected() {
    }

    public static int getStatue() {
        return nStatue;
    }

    //回退键控制
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {

            if (llPage2.getVisibility() == View.VISIBLE) {
                Bitmap bmp = null;
                ivCheckPic.setImageBitmap(bmp);
                tvFac.setText("");
                tvPosition.setText("");
                tvStatue.setText("");
                tvNote.setText("");
                tvTime.setText("");
                llPage2.setVisibility(View.GONE);
                llPage1.setVisibility(View.VISIBLE);
                return true;
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void checkVer(){
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
            /*pDialog = new ProgressDialog(Chose.this);
            pDialog.setMessage("请稍后...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            Log.d("Log_tag", "onPreExecute...");
            pDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {


            HttpURLConnection conn;
            InputStream is;
            String url = "";//版本控制链接，待定

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
                    AlertDialog.Builder multiDia = new AlertDialog.Builder(PatrolHome.this);
                    multiDia.setTitle("系统有新版本需要升级！");
                    multiDia.setPositiveButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                            //finish();
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
        pd = new  ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread(){
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
            }}.start();
    }

    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }


}
