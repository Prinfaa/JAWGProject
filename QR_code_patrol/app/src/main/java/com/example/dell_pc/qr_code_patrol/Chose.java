package com.example.dell_pc.qr_code_patrol;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell_pc.qr_code_patrol.adpter.AllShopProblemNotDealAdapter;
import com.example.dell_pc.qr_code_patrol.adpter.Checked1Adapter;
import com.example.dell_pc.qr_code_patrol.adpter.PatroledAdapter;
import com.example.dell_pc.qr_code_patrol.adpter.ShopNoProblemAdapter;
import com.example.dell_pc.qr_code_patrol.adpter.ShopProblemDealAdapter;
import com.example.dell_pc.qr_code_patrol.adpter.UnCheckAdapter;
import com.example.dell_pc.qr_code_patrol.adpter.UnPatrolAdapter;
import com.example.dell_pc.qr_code_patrol.db.CheckDB;
import com.example.dell_pc.qr_code_patrol.db.DealDB;
import com.example.dell_pc.qr_code_patrol.db.MyAllCheckCardDB;
import com.example.dell_pc.qr_code_patrol.db.MyAllPatrolCardDB;
import com.example.dell_pc.qr_code_patrol.db.MyAllShopCardDB;
import com.example.dell_pc.qr_code_patrol.db.PatrolDB;
import com.example.dell_pc.qr_code_patrol.db.ShopCheckDB;
import com.example.dell_pc.qr_code_patrol.item.CheckedItem;
import com.example.dell_pc.qr_code_patrol.item.DealItem;
import com.example.dell_pc.qr_code_patrol.item.PatrolItem;
import com.example.dell_pc.qr_code_patrol.item.PatroledItem;
import com.example.dell_pc.qr_code_patrol.item.ShopCheckedItem;
import com.example.dell_pc.qr_code_patrol.item.ShopNoProblemItem;
import com.example.dell_pc.qr_code_patrol.item.ShopProblemDealedItem;
import com.example.dell_pc.qr_code_patrol.item.ShopProblemNotDealItem;
import com.example.dell_pc.qr_code_patrol.item.UnCheckItem;
import com.example.dell_pc.qr_code_patrol.item.UnPatrolItem;
import com.example.dell_pc.qr_code_patrol.tools.DownLoadManager;
import com.example.dell_pc.qr_code_patrol.tools.HttpUtils;
import com.example.dell_pc.qr_code_patrol.tools.NetStatue;
import com.example.dell_pc.qr_code_patrol.tools.PicManager;
import com.example.dell_pc.qr_code_patrol.tools.TimeManager;
import com.example.dell_pc.qr_code_patrol.view.RoundCornerImageView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell-pc on 2017/10/1.
 */
public class Chose extends Activity implements View.OnClickListener {
    public static MyAllCheckCardDB myAllCheckCardDB;
    public static MyAllPatrolCardDB myAllPatrolCardDB;
    public static MyAllShopCardDB myAllShopCardDB;

    public static PatrolDB patrolDB;
    public static CheckDB checkDB;
    public static ShopCheckDB shopCheckDB;
    public static DealDB dealDB;

    private String unitID, unitName;
    private PieChart piePatrol, pieCheck;
    private FrameLayout flPatrol, flCheck, flShop;
    private ListView lvPatrol, lvUnPatrol;
    private ListView lvUnCheck, lvCheckNormal, lvCheckAbnormal;
    private ListView lvShopProblemNotDeal, lvShopNoProblem, lvShopProblemDeal;
    float mPosX = 0, mCurPosX = 0;

    private List<UnPatrolItem> unPatrolItemList = new ArrayList<UnPatrolItem>();
    private List<PatroledItem> patroledItemList = new ArrayList<PatroledItem>();

    private List<UnCheckItem> unCheckList = new ArrayList<UnCheckItem>();
    private List<CheckedItem> checkedNormalList = new ArrayList<CheckedItem>();
    private List<CheckedItem> checkAbnormalList = new ArrayList<CheckedItem>();

    public static List<ShopProblemNotDealItem> shopProblemNotDealList = new ArrayList<ShopProblemNotDealItem>();
    private List<ShopProblemDealedItem> shopProblemDealedList = new ArrayList<ShopProblemDealedItem>();
    private List<ShopNoProblemItem> shopNoProblemList = new ArrayList<ShopNoProblemItem>();

    private UnPatrolAdapter unPatrolAdapter;
    private PatroledAdapter patroledAdapter;

    private UnCheckAdapter unCheckAdapter;
    private Checked1Adapter checkedNormalAdapter, checkAbnormalAdapter;


    private RoundCornerImageView ivInfoPic;
    private TextView tvInfoTime, tvInfoFac, tvInfoPosition, tvInfoStatue, tvInfoProblem, tvInfoProblemLabel;
    private ScrollView svInfo;
    private Button btnInfoOK;
    private TextView tvProblemCount, tvProblemDealCount, tvNoProblemCount;

    private String code = "";

    private NfcAdapter nfcAdapter = null;
    private PendingIntent mPendingIntent = null;
    private IntentFilter[] mIntentFilter = null;
    private String[][] mTechList = null;

    private Thread mThread;
    private PicManager mPicManger = new PicManager();

    private String localVersion;
    private URL downLoadNewVerUrl;

    private boolean isActive = true;

    private TextView tvRefresh;

    public String URL_GET_PATROL_CARDS = "http://dndzl.cn/daping/patrol_record.php?unitID=";
    public String URL_GET_CHECK_CARDS = "http://dndzl.cn/daping/monthlyTest.php?unitID=";
    public String URL_GET_SHOP_CARDS = "http://jn.xiaofang365.cn/xfwlw/xfwlw/getShopCards.php?unitID=";
    public static final String GET_ALL_PATROL_CARDS_URL = "http://jn.xiaofang365.cn/xfwlw/xfwlw/getPatrolCards";
    public static final String GET_ALL_CHECK_CARDS_URL = "http://jn.xiaofang365.cn/xfwlw/xfwlw/getCheckCards?unitID=";

    //    public String GET_PATROL_RECORD_URL = "http://jn.xiaofang365.cn/xfwlw/xfwlw/getPatrolRecord?unitID=";
//    public String GET_CHECK_RECORD_URL = "http://jn.xiaofang365.cn/xfwlw/xfwlw/getCheckRecord?unitID=";
    public String GET_SHOP_CHECK_RECORD = "http://jn.xiaofang365.cn/xfwlw/xfwlw/getShopCheckRecord?unitID=";

    //    public String URL_GET_CARD_INFO = "http://jn.xiaofang365.cn/xfwlw/xfwlw/getCardInfo";
    public String URL_SEND_SHOP_RECORD_TEXT = "http://jn.xiaofang365.cn/xfwlw/xfwlw/addShopCheckRecord";
    public String URL_SEND_SHOP_RECORD_PIC = "http://jn.xiaofang365.cn/xfwlw/xfwlw/uploadShopCheckRecordPic";

    public String SEND_PATROL_RECORD_URL = "http://jn.xiaofang365.cn/xfwlw/xfwlw/addPatrolRecord";
    public String SEND_CHECK_RECORD_TEXT_URL = "http://jn.xiaofang365.cn/xfwlw/xfwlw/addCheckRecord";
    public String SEND_CHECK_RECORD_PIC_URL = "http://jn.xiaofang365.cn/xfwlw/xfwlw/uploadCheckRecordPic";

    public static final String SEND_DEAL_TEXT_URL = "http://jn.xiaofang365.cn/xfwlw/xfwlw/dealShopProblem";
    public static final String SEND_DEAL_PIC_URL = "http://jn.xiaofang365.cn/xfwlw/xfwlw/uploadDealPic";

    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isPatrolOver = true;
    private boolean isCheckOver = true;
    private boolean isShopOver = true;

    private int CLOSE_REFRESH_BAR = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chose);
        initData();
        initView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    Message msg = new Message();
                    msg.arg1 = CLOSE_REFRESH_BAR;
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        initNFC();
        initDB();
        checkVer();

        String url = URL_GET_PATROL_CARDS + unitID;
        HttpUtils.getJSON(url, getPatrolRecordHandler);
        isPatrolOver = false;
        url = URL_GET_CHECK_CARDS + unitID;
        HttpUtils.getJSON(url, getCheckRecordHandler);
        isCheckOver = false;
        HttpUtils.getJSON(GET_SHOP_CHECK_RECORD, getShopCheckRecordHandler);
        isShopOver = false;
    }

    private void initDB() {
        myAllPatrolCardDB = new MyAllPatrolCardDB(this);
        myAllPatrolCardDB.delete();
        String url = URL_GET_PATROL_CARDS + unitID;
        HttpUtils.getJSON(url, getAllPatrolCardsHandler);


        myAllCheckCardDB = new MyAllCheckCardDB(this);
        myAllCheckCardDB.delete();
        url = URL_GET_CHECK_CARDS + unitID;
        HttpUtils.getJSON(url, getAllCheckCardHandler);

        myAllShopCardDB = new MyAllShopCardDB(this);
        myAllShopCardDB.delete();
        url = URL_GET_SHOP_CARDS + unitID;
        HttpUtils.getJSON(url, getAllShopCardHandler);


        patrolDB = new PatrolDB(this);
        checkDB = new CheckDB(this);
        shopCheckDB = new ShopCheckDB(this);
        dealDB = new DealDB(this);

    }

    private void initData() {
        Intent intent = this.getIntent();
        unitID = intent.getStringExtra("unitId");
        unitName = intent.getStringExtra("unitName");
        GET_SHOP_CHECK_RECORD = GET_SHOP_CHECK_RECORD + unitID;
    }

    private void initView() {
        piePatrol = (PieChart) findViewById(R.id.piePatrol);
        pieCheck = (PieChart) findViewById(R.id.pieCheck);

        flPatrol = (FrameLayout) findViewById(R.id.flPiePatrol);
        flCheck = (FrameLayout) findViewById(R.id.flPieCheck);
        flShop = (FrameLayout) findViewById(R.id.flPieShop);

        lvPatrol = (ListView) findViewById(R.id.lvPatrol);
        lvUnPatrol = (ListView) findViewById(R.id.lvUnPatrol);

        unPatrolAdapter = new UnPatrolAdapter(this, unPatrolItemList);
        patroledAdapter = new PatroledAdapter(this, patroledItemList);

        lvUnPatrol.setAdapter(unPatrolAdapter);
        lvPatrol.setAdapter(patroledAdapter);

        lvShopProblemNotDeal = (ListView) findViewById(R.id.lvShopProblemNotDeal);
        AllShopProblemNotDealAdapter shopProblemNotDealAdapter = new AllShopProblemNotDealAdapter(this, shopProblemNotDealList);
        lvShopProblemNotDeal.setAdapter(shopProblemNotDealAdapter);

        lvShopNoProblem = (ListView) findViewById(R.id.lvShopNoProblem);
        ShopNoProblemAdapter shopNoProblemAdapter = new ShopNoProblemAdapter(this, shopNoProblemList);
        lvShopNoProblem.setAdapter(shopNoProblemAdapter);

        lvShopProblemDeal = (ListView) findViewById(R.id.lvShopProblemDeal);
        ShopProblemDealAdapter shopProblemDealAdapter = new ShopProblemDealAdapter(this, shopProblemDealedList);
        lvShopProblemDeal.setAdapter(shopProblemDealAdapter);

        tvProblemCount = (TextView) findViewById(R.id.tvProblemCount);
        tvProblemDealCount = (TextView) findViewById(R.id.tvProblemDealCount);
        tvNoProblemCount = (TextView) findViewById(R.id.tvNoProblemCount);

        tvRefresh = (TextView) findViewById(R.id.tvRefresh);

        tvProblemCount.setOnClickListener(this);
        tvProblemDealCount.setOnClickListener(this);
        tvNoProblemCount.setOnClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_srl);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getInfo();
            }
        });
        piePatrol.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                if (entry.getXIndex() == 0) {
                    if (flCheck.getVisibility() == View.VISIBLE) {
                        pageGo();
                        myAnimationCome(lvPatrol, 500, 0, 0, 2000, 0);
                    } else if (lvUnPatrol.getVisibility() == View.VISIBLE) {
                        pageGo();
                        myAnimationCome(lvPatrol, 500, 0, 0, 2000, 0);
                    } else if (lvPatrol.getVisibility() == View.VISIBLE) {
                        pageGo();
                        myAnimationCome(flCheck, 500, 2000, 0, 0, 0);
                    }
                } else if (entry.getXIndex() == 1) {
                    if (flCheck.getVisibility() == View.VISIBLE) {
                        pageGo();
                        myAnimationCome(lvUnPatrol, 500, 0, 0, 2000, 0);
                    } else if (lvPatrol.getVisibility() == View.VISIBLE) {
                        pageGo();
                        myAnimationCome(lvUnPatrol, 500, 0, 0, 2000, 0);
                    } else if (lvUnPatrol.getVisibility() == View.VISIBLE) {
                        pageGo();
                        myAnimationCome(flCheck, 500, 2000, 0, 0, 0);
                    }
                }

            }

            @Override
            public void onNothingSelected() {

            }
        });

        pieCheck.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                if (flPatrol.getVisibility() == View.VISIBLE) {
                    if (entry.getXIndex() == 0) {
                        pageGo();
                        myAnimationCome(lvCheckNormal, 500, 0, 0, 2000, 0);
                    } else if (entry.getXIndex() == 1) {
                        pageGo();
                        myAnimationCome(lvCheckAbnormal, 500, 0, 0, 2000, 0);
                    } else if (entry.getXIndex() == 2) {
                        pageGo();
                        myAnimationCome(lvUnCheck, 500, 0, 0, 2000, 0);
                    }
                } else if (lvCheckNormal.getVisibility() == View.VISIBLE) {
                    if (entry.getXIndex() == 1) {
                        pageGo();
                        myAnimationCome(lvCheckAbnormal, 500, 0, 0, 2000, 0);
                    } else if (entry.getXIndex() == 2) {
                        pageGo();
                        myAnimationCome(lvUnCheck, 500, 0, 0, 2000, 0);
                    }
                } else if (lvCheckAbnormal.getVisibility() == View.VISIBLE) {
                    if (entry.getXIndex() == 0) {
                        pageGo();
                        myAnimationCome(lvCheckNormal, 500, 0, 0, 2000, 0);
                    } else if (entry.getXIndex() == 2) {
                        pageGo();
                        myAnimationCome(lvUnCheck, 500, 0, 0, 2000, 0);
                    }
                } else if (lvUnCheck.getVisibility() == View.VISIBLE) {
                    if (entry.getXIndex() == 0) {
                        pageGo();
                        myAnimationCome(lvCheckNormal, 500, 0, 0, 2000, 0);
                    } else if (entry.getXIndex() == 1) {
                        pageGo();
                        myAnimationCome(lvCheckAbnormal, 500, 0, 0, 2000, 0);
                    }
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });


        lvPatrol.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        mPosX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        mCurPosX = event.getX();
                        if (mCurPosX - mPosX > 0
                                && (Math.abs(mCurPosX - mPosX) > 300)) {
                            myAnimationGo(lvPatrol, 500, 0, 2000, 0, 0);
                            myAnimationCome(flCheck, 500, -2000, 0, 0, 0);
                        } else if (mCurPosX - mPosX < 0
                                && (Math.abs(mPosX - mCurPosX) > 300)) {
                            myAnimationGo(lvPatrol, 500, 0, -2000, 0, 0);
                            myAnimationCome(flCheck, 500, 2000, 0, 0, 0);
                        }

                        break;
                }
                return false;
            }

        });

        lvUnPatrol.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        mPosX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        mCurPosX = event.getX();
                        if (mCurPosX - mPosX > 0
                                && (Math.abs(mCurPosX - mPosX) > 300)) {
                            myAnimationGo(lvUnPatrol, 500, 0, 2000, 0, 0);
                            myAnimationCome(flCheck, 500, -2000, 0, 0, 0);
                        } else if (mCurPosX - mPosX < 0
                                && (Math.abs(mPosX - mCurPosX) > 300)) {
                            myAnimationGo(lvUnPatrol, 500, 0, -2000, 0, 0);
                            myAnimationCome(flCheck, 500, 2000, 0, 0, 0);
                        }

                        break;
                }
                return false;
            }

        });

        lvUnCheck = (ListView) findViewById(R.id.lvUnCheck);
        lvCheckNormal = (ListView) findViewById(R.id.lvCheckNormal);
        lvCheckAbnormal = (ListView) findViewById(R.id.lvCheckAbnormal);

        unCheckAdapter = new UnCheckAdapter(this, unCheckList);
        checkedNormalAdapter = new Checked1Adapter(this, checkedNormalList);
        checkAbnormalAdapter = new Checked1Adapter(this, checkAbnormalList);
        lvUnCheck.setAdapter(unCheckAdapter);
        lvCheckNormal.setAdapter(checkedNormalAdapter);
        lvCheckAbnormal.setAdapter(checkAbnormalAdapter);

        lvCheckNormal.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        mPosX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        mCurPosX = event.getX();
                        if (mCurPosX - mPosX > 0
                                && (Math.abs(mCurPosX - mPosX) > 300)) {
                            myAnimationGo(lvCheckNormal, 500, 0, 2000, 0, 0);
                            myAnimationCome(flPatrol, 500, -2000, 0, 0, 0);
                        } else if (mCurPosX - mPosX < 0
                                && (Math.abs(mPosX - mCurPosX) > 300)) {
                            myAnimationGo(lvCheckNormal, 500, 0, -2000, 0, 0);
                            myAnimationCome(flPatrol, 500, 2000, 0, 0, 0);
                        }

                        break;
                }
                return false;
            }

        });

        lvCheckAbnormal.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        mPosX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        mCurPosX = event.getX();
                        if (mCurPosX - mPosX > 0
                                && (Math.abs(mCurPosX - mPosX) > 300)) {
                            myAnimationGo(lvCheckAbnormal, 500, 0, 2000, 0, 0);
                            myAnimationCome(flPatrol, 500, -2000, 0, 0, 0);
                        } else if (mCurPosX - mPosX < 0
                                && (Math.abs(mPosX - mCurPosX) > 300)) {
                            myAnimationGo(lvCheckAbnormal, 500, 0, -2000, 0, 0);
                            myAnimationCome(flPatrol, 500, 2000, 0, 0, 0);
                        }

                        break;
                }
                return false;
            }

        });

        lvUnCheck.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        mPosX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        mCurPosX = event.getX();
                        if (mCurPosX - mPosX > 0
                                && (Math.abs(mCurPosX - mPosX) > 300)) {
                            myAnimationGo(lvUnCheck, 500, 0, 2000, 0, 0);
                            myAnimationCome(flPatrol, 500, -2000, 0, 0, 0);
                        } else if (mCurPosX - mPosX < 0
                                && (Math.abs(mPosX - mCurPosX) > 300)) {
                            myAnimationGo(lvUnCheck, 500, 0, -2000, 0, 0);
                            myAnimationCome(flPatrol, 500, 2000, 0, 0, 0);
                        }

                        break;
                }
                return false;
            }

        });

        svInfo = (ScrollView) findViewById(R.id.svInfo);
        ivInfoPic = (RoundCornerImageView) findViewById(R.id.ivInfoPic);
        tvInfoTime = (TextView) findViewById(R.id.tvInfoTime);
        tvInfoFac = (TextView) findViewById(R.id.tvInfoFac);
        tvInfoPosition = (TextView) findViewById(R.id.tvInfoPosition);
        tvInfoStatue = (TextView) findViewById(R.id.tvInfoStatue);
        tvInfoProblem = (TextView) findViewById(R.id.tvInfoProblem);
        tvInfoProblemLabel = (TextView) findViewById(R.id.tvInfoProblemLabel);
        btnInfoOK = (Button) findViewById(R.id.btnInfoOK);

        btnInfoOK.setOnClickListener(this);

        lvCheckNormal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedItem checkedItem = checkedNormalList.get(i);
                String strImageName = checkedItem.getImageName();
                svInfo.setVisibility(View.VISIBLE);
                svInfo.scrollTo(0, 0);
                HttpUtils.getPicBitmap(strImageName, getPicHandler);
                String strStatue;
                if (checkedItem.getStatue().equals("1")) {
                    strStatue = "不正常";
                    tvInfoStatue.setTextColor(0xFFFF0000);
                    tvInfoProblem.setTextColor(0xFFFF0000);
                } else {
                    strStatue = "正常";
                    tvInfoStatue.setTextColor(0xFF3C70B6);
                    tvInfoProblemLabel.setVisibility(View.INVISIBLE);

                }
                String problem = checkedItem.getProblem();
                if (problem.equals("noProblem")) {
                    problem = "";
                }

                tvInfoTime.setText(TimeManager.getStrTime2(checkedItem.getDateTime()));
                tvInfoFac.setText(checkedItem.getFac());
                tvInfoPosition.setText(checkedItem.getPosition());
                tvInfoStatue.setText(strStatue);
                tvInfoProblem.setText(problem.replace("_", "\r\n"));
            }
        });

        lvCheckAbnormal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedItem checkedItem = checkAbnormalList.get(i);
                String strImageName = checkedItem.getImageName();
                svInfo.setVisibility(View.VISIBLE);
                svInfo.scrollTo(0, 0);
                HttpUtils.getPicBitmap(strImageName, getPicHandler);
//                Bitmap pic = mPicManger.getDiskBitmap(strImageName);
                String strStatue;
                if (checkedItem.getStatue().equals("1")) {
                    strStatue = "不正常";
                    tvInfoStatue.setTextColor(0xFFFF0000);
                    tvInfoProblem.setTextColor(0xFFFF0000);
                } else {
                    strStatue = "正常";
                    tvInfoStatue.setTextColor(0xFF3C70B6);
                    tvInfoProblemLabel.setVisibility(View.INVISIBLE);

                }
                tvInfoTime.setText(TimeManager.getStrTime2(checkedItem.getDateTime()));
                tvInfoFac.setText(checkedItem.getFac());
                tvInfoPosition.setText(checkedItem.getPosition());
                tvInfoStatue.setText(strStatue);
                tvInfoProblem.setText(checkedItem.getProblem().replace("_", "\r\n"));
            }
        });

    }

    private void pageGo() {
        myAnimationGo(flPatrol, 500, 0, 2000, 0, 0);
        myAnimationGo(flCheck, 500, 0, -2000, 0, 0);
        myAnimationGo(flShop, 500, 0, 2000, 0, 0);
    }

    private void pageBack() {
        myAnimationCome(flPatrol, 500, 2000, 0, 0, 0);
        myAnimationCome(flCheck, 500, -2000, 0, 0, 0);
        myAnimationCome(flShop, 500, 2000, 0, 0, 0);
    }

    private void initNFC() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "设备不支持NFC！", Toast.LENGTH_LONG).show();
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, "请在系统设置中先启用NFC功能！", Toast.LENGTH_LONG).show();
        }

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter intentFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            intentFilter.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        mIntentFilter = new IntentFilter[]{intentFilter};
        mTechList = null;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (isActive) {

                try {
                    switch (NetStatue.GetNetType(Chose.this)) {
                        case -1:
                            //Toast.makeText(C.this, "无可用网络", Toast.LENGTH_LONG).show();
                            return;
//                        case 2:
//                            return;
                        default:
                            SendPatrolRecord();
                            SendCheckRecord();
                            SendShopCheckRecord();
                            SendDealRecord();
                            break;
                    }
                    Thread.sleep(10000*6);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void getInfo() {
        switch (NetStatue.GetNetType(Chose.this)) {
            case -1:
                Toast.makeText(Chose.this, "无可用网络", Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
                return;
            default:
                String url = URL_GET_PATROL_CARDS + unitID;
                HttpUtils.getJSON(url, getPatrolRecordHandler);
                url = URL_GET_CHECK_CARDS + unitID;
                HttpUtils.getJSON(url, getCheckRecordHandler);
                HttpUtils.getJSON(GET_SHOP_CHECK_RECORD, getShopCheckRecordHandler);
                break;
        }
    }

    private void SendPatrolRecord() {
        List<PatrolItem> patrolList = patrolDB.getAllData();
        for (int i = 0; i < patrolList.size(); i++) {
            PatrolItem patrolItem = patrolList.get(i);
            String cardNO = null, position = null, timestamp = null;
            cardNO = patrolItem.getCardNo();
            position = patrolItem.getPosition();
            timestamp = patrolItem.getDateTime();

            String url = SEND_PATROL_RECORD_URL
                    + "?cardNo=" + cardNO
                    + "&position=" + position
                    + "&timestamp=" + timestamp
                    + "&unitID=" + unitID;
            HttpUtils.getJsonWithArg(url, addPatrolRecordHandler, i);
        }
    }

    private void SendCheckRecord() {
        List<CheckedItem> checkedItemList = checkDB.getAllData();


        for (int i = 0; i < checkedItemList.size(); i++) {
            CheckedItem checkedItem = checkedItemList.get(i);
            String strCardNO = null, strStatue = null, strProblemID = null, strProblem = null, strImageName = null, strTime = null;
            try {
                strCardNO = URLEncoder.encode(checkedItem.getCardNo(), "UTF-8");
                strStatue = URLEncoder.encode(checkedItem.getStatue(), "UTF-8");
                strProblemID = URLEncoder.encode(checkedItem.getProblemID(), "UTF-8");
                strProblem = URLEncoder.encode(checkedItem.getProblem(), "UTF-8");
                strImageName = URLEncoder.encode(checkedItem.getImageName(), "UTF-8");
                strTime = URLEncoder.encode(checkedItem.getDateTime(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String url = SEND_CHECK_RECORD_TEXT_URL
                    + "?cardNo=" + strCardNO
                    + "&unitID=" + unitID
                    + "&statue=" + strStatue
                    + "&problemID=" + strProblemID
                    + "&problem=" + strProblem
                    + "&imageName=" + strImageName
                    + "&time=" + strTime;
            HttpUtils.getJSONWithPosition(url, sendCheckRecordHandler, i);
            Bitmap bitmap = mPicManger.getDiskBitmap(strImageName);

            if(bitmap!=null){

                HttpUtils.uploadBitmap(bitmap, strImageName, SEND_CHECK_RECORD_PIC_URL, sendCheckPicHandler, i);

            }


        }
    }

    private void SendShopCheckRecord() {
        List<ShopCheckedItem> shopCheckedList = shopCheckDB.getLocalRecord();
        for (int i = 0; i < shopCheckedList.size(); i++) {
            ShopCheckedItem shopCheckedItem = shopCheckedList.get(i);
            String strCardNO = null, strShopID = null, strParentID = null, strStatue = null, strProblemID = null, strProblem = null, strImageName = null, strTime = null;
            try {
                strCardNO = URLEncoder.encode(shopCheckedItem.getCardNo(), "UTF-8");
                strShopID = URLEncoder.encode(shopCheckedItem.getShopID(), "UTF-8");
                strParentID = URLEncoder.encode(shopCheckedItem.getParentID(), "UTF-8");
                strStatue = URLEncoder.encode(shopCheckedItem.getStatue(), "UTF-8");
                strProblemID = URLEncoder.encode(shopCheckedItem.getProblemID(), "UTF-8");
                strProblem = URLEncoder.encode(shopCheckedItem.getProblem(), "UTF-8");
                strImageName = URLEncoder.encode(shopCheckedItem.getImageName(), "UTF-8");
                strTime = URLEncoder.encode(shopCheckedItem.getTimestamp(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String url = URL_SEND_SHOP_RECORD_TEXT
                    + "?cardNo=" + strCardNO
                    + "&shopID=" + strShopID
                    + "&parentID=" + strParentID
                    + "&statue=" + strStatue
                    + "&problemID=" + strProblemID
                    + "&problem=" + strProblem
                    + "&imageName=" + strImageName
                    + "&time=" + strTime;

            HttpUtils.getJSONWithPosition(url, sendShopRecordHandler, i);
            Bitmap bitmap = mPicManger.getDiskBitmap(strImageName);
            if (bitmap != null) {
                HttpUtils.uploadBitmap(bitmap, strImageName, URL_SEND_SHOP_RECORD_PIC, sendShopCheckPicHandler, i);
            } else {
            }


        }
    }

    private void SendDealRecord() {
        List<DealItem> dealItemList = dealDB.getLocalRecord();

        for (int i = 0; i < dealItemList.size(); i++) {
            DealItem dealItem = dealItemList.get(i);
            String strCardNO = null, strRecordID = null, strImageName = null, strTime = null;
            try {
                strCardNO = URLEncoder.encode(dealItem.getCardNo(), "UTF-8");
                strRecordID = URLEncoder.encode(dealItem.getRecordID(), "UTF-8");
                strImageName = URLEncoder.encode(dealItem.getImageName(), "UTF-8");
                strTime = URLEncoder.encode(dealItem.getTimestamp(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String url = SEND_DEAL_TEXT_URL
                    + "?cardNo=" + strCardNO
                    + "&recordID=" + strRecordID
                    + "&imageName=" + strImageName
                    + "&time=" + strTime;

            HttpUtils.getJSONWithPosition(url, sendDealHandler, i);
            Bitmap bitmap = mPicManger.getDiskBitmap(strImageName);
            if (bitmap != null) {
                HttpUtils.uploadBitmap(bitmap, strImageName, SEND_DEAL_PIC_URL, sendDealPicHandler, i);
            } else {
            }


        }
    }

    private void drawPiePatrol() {
        PieData mPieData = getPiePatrolData(2, 100);
        showPiePatrol(piePatrol, mPieData);
    }

    private void showPiePatrol(PieChart pieChart, PieData pieData) {
        pieChart.setHoleColorTransparent(true);

        pieChart.setHoleRadius(40f); //半径
        pieChart.setTransparentCircleRadius(50f); //半透明圈

        pieChart.setDescription("");

        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90); //初始角度
        pieChart.setRotationEnabled(true); //可以手动旋转
        pieChart.setUsePercentValues(false); //显示百分比

        pieChart.setDrawCenterText(true); //饼状图中间可以添加文字
        pieChart.setCenterText("");
        pieChart.setCenterTextColor(Color.GRAY);

        pieChart.setData(pieData);

        Legend mLegend = pieChart.getLegend(); //设置比例图
        mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER); //坐右边显示
        mLegend.setXEntrySpace(5f);
        mLegend.setYEntrySpace(100f);
        mLegend.setTextColor(Color.GRAY);
        mLegend.setEnabled(false);
        pieChart.animateXY(1000, 1000);
    }

    private PieData getPiePatrolData(int count, float range) {
        ArrayList<String> xValues = new ArrayList<String>(); //用来表示每个饼块上的内容
        String[] content = new String[]{"已巡查", "未巡查"};
        for (int i = 0; i < count; i++) {
            xValues.add(content[i]);
        }

        ArrayList<Entry> yValue = new ArrayList<Entry>(); //用来表示封装每个饼块的实际数据

        List<Float> qs = new ArrayList<Float>();
        qs.add((float) patroledItemList.size());
        qs.add((float) unPatrolItemList.size());

        for (int i = 0; i < qs.size(); i++) {
            yValue.add(new Entry(qs.get(i), i));
        }

        PieDataSet pieDataSet = new PieDataSet(yValue, "");
        pieDataSet.setSliceSpace(0f);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        //饼图颜色
        colors.add(0XFF3C70B6);
        colors.add(0XFFCC3399);
        pieDataSet.setColors(colors); //设置颜色
        pieDataSet.setValueTextSize(8f);
        pieDataSet.setValueTextColor(Color.WHITE);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 280f);
        pieDataSet.setSelectionShift(px); //选中态多出的长度
        PieData pieData = new PieData(xValues, pieDataSet);
        return pieData;
    }

    private void drawPieCheck() {
        PieData mPieData = getPieCheckData(3, 100);
        showPieCheck(pieCheck, mPieData);
    }

    private PieData getPieCheckData(int count, float range) {
        ArrayList<String> xValues = new ArrayList<String>(); //用来表示每个饼块上的内容
        String[] content = new String[]{"合格", "不合格", "未检查"};
        for (int i = 0; i < count; i++) {
            xValues.add(content[i]);
        }

        ArrayList<Entry> yValue = new ArrayList<Entry>(); //用来表示封装每个饼块的实际数据

        List<Float> qs = new ArrayList<Float>();
        qs.add((float) checkedNormalList.size());
        qs.add((float) checkAbnormalList.size());
        qs.add((float) unCheckList.size());

        for (int i = 0; i < qs.size(); i++) {
            yValue.add(new Entry(qs.get(i), i));
        }

        PieDataSet pieDataSet = new PieDataSet(yValue, "");
        pieDataSet.setSliceSpace(0f);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        //饼图颜色
        colors.add(0XFF3C70B6);
        colors.add(0XFFFF5555);
        colors.add(0XFFCC3399);

        pieDataSet.setColors(colors); //设置颜色
        pieDataSet.setValueTextSize(8f);
        pieDataSet.setValueTextColor(Color.WHITE);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 280f);
        pieDataSet.setSelectionShift(px); //选中态多出的长度
        PieData pieData = new PieData(xValues, pieDataSet);
        return pieData;
    }

    private void showPieCheck(PieChart pieChart, PieData pieData) {
        pieChart.setHoleColorTransparent(true);

        pieChart.setHoleRadius(40f); //半径
        pieChart.setTransparentCircleRadius(50f); //半透明圈

        pieChart.setDescription("");

        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90); //初始角度
        pieChart.setRotationEnabled(true); //可以手动旋转
        pieChart.setUsePercentValues(false); //显示百分比

        pieChart.setDrawCenterText(true); //饼状图中间可以添加文字
        pieChart.setCenterText("");
        pieChart.setCenterTextColor(Color.GRAY);


        pieChart.setData(pieData);

        Legend mLegend = pieChart.getLegend(); //设置比例图
        mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER); //坐右边显示
        mLegend.setXEntrySpace(5f);
        mLegend.setYEntrySpace(100f);
        mLegend.setTextColor(Color.GRAY);
        mLegend.setEnabled(false);

        pieChart.animateXY(1000, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
        nfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilter, mTechList);
        String url = URL_GET_PATROL_CARDS + unitID;
        HttpUtils.getJSON(url, getPatrolRecordHandler);
        url = URL_GET_CHECK_CARDS + unitID;
        HttpUtils.getJSON(url, getCheckRecordHandler);
        HttpUtils.getJSON(GET_SHOP_CHECK_RECORD, getShopCheckRecordHandler);

        mThread = new Thread(runnable);
        mThread.start();

    }

    @Override
    public void onClick(View v) {
        if (v == btnInfoOK) {
            svInfo.setVisibility(View.GONE);
            ivInfoPic.setImageResource(R.drawable.loading_pic);
        } else if (v == tvProblemCount) {
            if (shopProblemNotDealList.size() == 0) {
                Toast.makeText(Chose.this, "无隐患记录", Toast.LENGTH_LONG).show();
                return;
            }
            pageGo();
            myAnimationCome(lvShopProblemNotDeal, 500, 0, 0, 2000, 0);
            lvShopProblemNotDeal.scrollTo(0, 0);
        } else if (v == tvProblemDealCount) {
            pageGo();
            myAnimationCome(lvShopProblemDeal, 500, 0, 0, 2000, 0);
            lvShopProblemDeal.scrollTo(0, 0);
        } else if (v == tvNoProblemCount) {
            pageGo();
            myAnimationCome(lvShopNoProblem, 500, 0, 0, 2000, 0);
            lvShopNoProblem.scrollTo(0, 0);
        }
    }

    private void myAnimationCome(View view, int duration, float f1, float f2, float f3, float f4) {
        view.setVisibility(View.INVISIBLE);
        //初始化 Translate动画
        TranslateAnimation translateAnimation = new TranslateAnimation(f1, f2, f3, f4);

        AnimationSet set = new AnimationSet(true);
        set.addAnimation(translateAnimation);

        set.setDuration(duration);
        view.startAnimation(set);
        view.setVisibility(View.VISIBLE);
    }

    private void myAnimationGo(View view, int duration, float f1, float f2, float f3, float f4) {
        view.setVisibility(View.VISIBLE);
        //初始化 Translate动画
        TranslateAnimation translateAnimation = new TranslateAnimation(f1, f2, f3, f4);

        AnimationSet set = new AnimationSet(true);
        set.addAnimation(translateAnimation);

        set.setDuration(duration);
        view.startAnimation(set);
        view.setVisibility(View.INVISIBLE);
    }


    protected void onPause() {
        super.onPause();
//        isActive = false;
        nfcAdapter.disableForegroundDispatch(this);
    }

    protected void onNewIntent(Intent intent) {
        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
        NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
        try {
            if (mNdefRecord != null) {
                code = new String(mNdefRecord.getPayload(), "UTF-8");
                code = code.substring(1, code.length());
//                String url = URL_GET_CARD_INFO + "?unitID=" + unitID + "&code=" + code;
//                System.out.println("url============" + url);
//                HttpUtils.getJSON(url, getCardInfoHandler);
                findTheCard(code);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void findTheCard(String code) {
        Intent intent = new Intent();
        String position = myAllPatrolCardDB.getPosition(code);
        if (position != null) {
            intent.putExtra("unitID", unitID);
            intent.putExtra("unitName", unitName);
            intent.putExtra("code", code);
            intent.putExtra("position", position);
//            intent.setClass(Chose.this, Patrol_new.class);
            intent.setClass(Chose.this, Patrol.class);
            startActivity(intent);
            return;
        }
        position = myAllCheckCardDB.getPosition(code);
        if (position != null) {
            intent.putExtra("unitID", unitID);
            intent.putExtra("unitName", unitName);
            intent.putExtra("code", code);
            intent.putExtra("position", position);
            intent.putExtra("facType", myAllCheckCardDB.getFac(code));
            intent.setClass(Chose.this, Check.class);
            startActivity(intent);
            return;
        }
        String shopName = myAllShopCardDB.getShopName(code);
        if (shopName != null) {
            intent.putExtra("unitID", unitID);
            intent.putExtra("unitName", unitName);
            intent.putExtra("code", code);
            intent.putExtra("shopID", myAllShopCardDB.getShopID(code));
            intent.putExtra("shopName", myAllShopCardDB.getShopName(code));
            intent.putExtra("linkman", myAllShopCardDB.getLinkman(code));
            intent.putExtra("phone", myAllShopCardDB.getPhone(code));
            intent.setClass(Chose.this, ProblemOrDeal.class);
            startActivity(intent);
        }
//        Toast.makeText(Chose.this, "无效卡片", Toast.LENGTH_LONG).show();
    }


    private Handler handler = new Handler() {


        public void handleMessage(Message msg) {
            if (msg.arg1 == CLOSE_REFRESH_BAR) {
                tvRefresh.setVisibility(View.GONE);
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
                    String times = object.getString("statistics");
                    if (times.equals("0")) {
                        unPatrolItemList.add(new UnPatrolItem(cardNo, position));
                    } else {
                        patroledItemList.add(new PatroledItem(cardNo, position, times));
                    }
                }
                unPatrolAdapter.notifyDataSetChanged();
                patroledAdapter.notifyDataSetChanged();
                drawPiePatrol();
                isPatrolOver = true;
                if (isPatrolOver && isCheckOver && isShopOver) {
                    swipeRefreshLayout.setRefreshing(false);
                }
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

                    String times = "null";
                    String statue = null;
                    String imageName = object.getString("imageName");
                    String problemID = object.getString("problemID");
                    String problem = object.getString("problem");
                    String time = object.getString("lastTime");
                    if (problem.equals("noData")) {
                        times = "0";
                    } else if (problem.equals("noProblem")) {
                        statue = "0";
                    } else {
                        statue = "1";
                    }

                    if (times.equals("0")) {
                        unCheckList.add(new UnCheckItem(cardNo, position, fac));
                    } else {
                        if (statue.equals("0")) {
                            checkedNormalList.add(new CheckedItem(cardNo, position, time, imageName, fac, problem, problemID, statue));
                        } else if (statue.equals("1")) {
                            checkAbnormalList.add(new CheckedItem(cardNo, position, time, imageName, fac, problem, problemID, statue));
                        }
                    }
                }
                unCheckAdapter.notifyDataSetChanged();
                checkedNormalAdapter.notifyDataSetChanged();
                checkAbnormalAdapter.notifyDataSetChanged();
                drawPieCheck();
                isCheckOver = true;
                if (isPatrolOver && isCheckOver && isShopOver) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };
    //获取本单位所有卡片当月检查资料
    private Handler getShopCheckRecordHandler = new Handler() {


        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;
            shopProblemNotDealList.clear();
            shopProblemDealedList.clear();
            shopNoProblemList.clear();
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String id = object.getString("id");
                    String cardNo = object.getString("cardNo");
                    String shopID = object.getString("shopID");
                    String shopName = object.getString("shopName");
                    String statue = object.getString("statue");
                    String imageName = object.getString("imageName");
                    String problemID = object.getString("problemID");
                    String problem = object.getString("problem");
                    String time = object.getString("timestamp");
                    String isDeal = object.getString("isDeal");
                    String dealPic = object.getString("dealPic");
                    String dealTimestamp = object.getString("dealTimestamp");
                    if (statue.equals("1") && isDeal.equals("0")) {
                        shopProblemNotDealList.add(new ShopProblemNotDealItem(id, cardNo, shopID, shopName, unitID, time, imageName, problem, problemID, statue));
                    } else if (statue.equals("1") && isDeal.equals("1")) {
                        shopProblemDealedList.add(new ShopProblemDealedItem(id, cardNo, shopID, shopName, unitID, time, imageName, problem, problemID, statue, dealPic, dealTimestamp));
                    } else if (statue.equals("0")) {
                        shopNoProblemList.add(new ShopNoProblemItem(id, cardNo, shopID, shopName, unitID, time, imageName));
                    }
                }
                tvProblemCount.setText(String.valueOf(shopProblemNotDealList.size()));
                tvProblemDealCount.setText(String.valueOf(shopProblemDealedList.size()));
                tvNoProblemCount.setText(String.valueOf(shopNoProblemList.size()));


                AllShopProblemNotDealAdapter shopProblemNotDealAdapter = new AllShopProblemNotDealAdapter(Chose.this, shopProblemNotDealList);
                ShopProblemDealAdapter shopProblemDealAdapter = new ShopProblemDealAdapter(Chose.this, shopProblemDealedList);
                ShopNoProblemAdapter shopNoProblemAdapter = new ShopNoProblemAdapter(Chose.this, shopNoProblemList);

                lvShopProblemNotDeal.setAdapter(shopProblemNotDealAdapter);
                lvShopProblemDeal.setAdapter(shopProblemDealAdapter);
                lvShopNoProblem.setAdapter(shopNoProblemAdapter);

                shopProblemNotDealAdapter.notifyDataSetChanged();
                shopNoProblemAdapter.notifyDataSetChanged();
                shopProblemDealAdapter.notifyDataSetChanged();
                isShopOver = true;
                if (isPatrolOver && isCheckOver && isShopOver) {
                    swipeRefreshLayout.setRefreshing(false);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };
    //获取本单位所有卡片资料
    private Handler getCardInfoHandler = new Handler() {
        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;
            Intent intent = new Intent();

            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                if (jsonObject.getString("result").equals("noThisCard")) {
//                    Toast.makeText(Chose.this, "无效卡片", Toast.LENGTH_LONG).show();
                    return;
                }
                String cardType = jsonObject.getString("cardType");
                if (cardType.equals("1")) {
                    Toast.makeText(Chose.this, "扫描了卡" + code, Toast.LENGTH_LONG).show();
                    intent.putExtra("unitID", unitID);
                    intent.putExtra("unitName", unitName);
                    intent.putExtra("code", code);
                    intent.putExtra("position", jsonObject.getString("position"));
                    intent.setClass(Chose.this, Patrol.class);
                    startActivity(intent);
                } else if (cardType.equals("2")) {
                    intent.putExtra("unitID", unitID);
                    intent.putExtra("unitName", unitName);
                    intent.putExtra("code", code);
                    intent.putExtra("position", jsonObject.getString("position"));
                    intent.putExtra("facType", jsonObject.getString("facType"));
                    intent.setClass(Chose.this, Check.class);
                    startActivity(intent);
                } else if (cardType.equals("3")) {
                    intent.putExtra("unitID", unitID);
                    intent.putExtra("unitName", unitName);
                    intent.putExtra("code", code);
                    intent.putExtra("shopID", jsonObject.getString("shopID"));
                    intent.putExtra("shopName", jsonObject.getString("shopName"));
                    intent.putExtra("linkman", jsonObject.getString("linkman"));
                    intent.putExtra("phone", jsonObject.getString("phone"));
                    intent.setClass(Chose.this, ProblemOrDeal.class);
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };
    //获取本单位所有巡检卡片资料
    private Handler getAllPatrolCardsHandler = new Handler() {
        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String code = object.getString("code");
                    String position = object.getString("position");
                    myAllPatrolCardDB.insert(code, position);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };
    //获取本单位所有卡片资料
    private Handler getAllCheckCardHandler = new Handler() {
        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String cardNo = object.getString("code");
                    String Position = object.getString("position");
                    String Fac = object.getString("type");
                    String problemType = object.getString("problemType");
                    myAllCheckCardDB.insert(cardNo, Position, Fac, problemType);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };

    //获取本单位所有商铺卡片资料
    private Handler getAllShopCardHandler = new Handler() {
        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String cardNo = object.getString("code");
                    String shopID = object.getString("id");
                    String shopName = object.getString("shopName");
                    String linkman = object.getString("linkman");
                    String phone = object.getString("phone");
                    myAllShopCardDB.insert(cardNo, shopID, shopName, linkman, phone);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };


    private Handler sendCheckRecordHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                return;
            }

            String jsonData = (String) msg.obj;
            int position = msg.arg1;
            if (jsonData.equals("null")) {
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                String strResponse = jsonObject.getString("response");
                checkDB.delete(strResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ;
    };

    private Handler sendCheckPicHandler = new Handler() {
        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;

            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                String strResponse = jsonObject.getString("response");
                mPicManger.deleteFile(strResponse);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ;
    };

    private Handler addPatrolRecordHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == -1) {
                return;
            }
            String jsonData = (String) msg.obj;
            if (jsonData.equals("null")) {
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                String info = jsonObject.getString("info");
                if (!info.equals("false")) {
                    patrolDB.deleteByCardNo(info);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ;
    };

    private Handler sendShopCheckPicHandler = new Handler() {
        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                String strResponse = jsonObject.getString("response");
                shopCheckDB.delete(strResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ;
    };

    private Handler sendDealPicHandler = new Handler() {
        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                String strResponse = jsonObject.getString("response");
                dealDB.delete(strResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ;
    };

    private Handler sendShopRecordHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                return;
            }

            String jsonData = (String) msg.obj;

            if (jsonData.equals("null")) {
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                String strResponse = jsonObject.getString("response");
                shopCheckDB.delete(strResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ;
    };

    private Handler sendDealHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                return;
            }

            String jsonData = (String) msg.obj;

            if (jsonData.equals("null")) {
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                String strResponse = jsonObject.getString("response");
                dealDB.delete(strResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ;
    };

    //获取本单位所有卡片当日巡查资料
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
                scale = 0.85;
            } else {
                scale = 0.6;
            }
            double r = h / w;
            ivInfoPic.setLayoutParams(new LinearLayout.LayoutParams((int) (screenWidth * scale), (int) (screenWidth * r * scale)));

            ivInfoPic.setImageBitmap(bitmap);
        }

        ;
    };


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
            if (svInfo.getVisibility() == View.VISIBLE) {
                svInfo.setVisibility(View.GONE);
                ivInfoPic.setImageResource(R.drawable.loading_pic);
                return true;
            } else if (lvPatrol.getVisibility() == View.VISIBLE) {
                myAnimationGo(lvPatrol, 500, 0, 0, 0, 2000);
                pageBack();
                return true;
            } else if (lvUnPatrol.getVisibility() == View.VISIBLE) {
                myAnimationGo(lvUnPatrol, 500, 0, 0, 0, 2000);
                pageBack();
                return true;
            } else if (lvCheckNormal.getVisibility() == View.VISIBLE) {
                myAnimationGo(lvCheckNormal, 500, 0, 0, 0, 2000);
                pageBack();
                return true;
            } else if (lvCheckAbnormal.getVisibility() == View.VISIBLE) {
                myAnimationGo(lvCheckAbnormal, 500, 0, 0, 0, 2000);
                pageBack();
                return true;
            } else if (lvUnCheck.getVisibility() == View.VISIBLE) {
                myAnimationGo(lvUnCheck, 500, 0, 0, 0, 2000);
                pageBack();
                return true;
            } else if (lvShopProblemNotDeal.getVisibility() == View.VISIBLE) {
                myAnimationGo(lvShopProblemNotDeal, 500, 0, 0, 0, 2000);
                pageBack();
                return true;
            } else if (lvShopNoProblem.getVisibility() == View.VISIBLE) {
                myAnimationGo(lvShopNoProblem, 500, 0, 0, 0, 2000);
                pageBack();
                return true;
            } else if (lvShopProblemNotDeal.getVisibility() == View.VISIBLE) {
                myAnimationGo(lvShopProblemNotDeal, 500, 0, 0, 0, 2000);
                pageBack();
            } else if (lvShopProblemDeal.getVisibility() == View.VISIBLE) {
                myAnimationGo(lvShopProblemDeal, 500, 0, 0, 0, 2000);
                pageBack();
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
            String url = "http://jn.xiaofang365.cn/xfwlw/xfwlw/checkVer.php?softName=checkPatrol";//版本控制链接，待定

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
                    downLoadApk();
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

}
