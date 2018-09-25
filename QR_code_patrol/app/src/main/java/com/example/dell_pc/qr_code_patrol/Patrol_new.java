package com.example.dell_pc.qr_code_patrol;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell_pc.qr_code_patrol.adpter.PatrolAdapter;
import com.example.dell_pc.qr_code_patrol.item.PatrolItem;
import com.example.dell_pc.qr_code_patrol.view.MyProgress;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by dell-pc on 2016/2/2.
 */
public class Patrol_new extends Activity {
    private String unitId, unitName;
    private String cardNo = "";

    private TextView tvMsg;


    public static final String ADD_PATROL_RECORD_URL = "http://jn.xiaofang365.cn/xfwlw/xfwlw/addPatrolRecord";

    private NfcAdapter nfcAdapter = null;
    private PendingIntent mPendingIntent = null;
    private IntentFilter[] mIntentFilter = null;
    private String[][] mTechList = null;

    private List<PatrolItem> patrolList = new ArrayList<PatrolItem>();

    private PatrolAdapter patrolAdapter;
    private ListView lvPatrol;

    private MyProgress myProgress = null;
    private Handler mHandler;
    int nCountRecord = 0;
    int nCountSend = 0;

    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    // 图片封装为一个数组
//    private int[] icon = { R.drawable.patroled_icon, R.drawable.patroled_icon,
//            R.drawable.patroled_icon, R.drawable.patroled_icon, R.drawable.patroled_icon,
//            R.drawable.patroled_icon, R.drawable.patroled_icon, R.drawable.patroled_icon,
//            R.drawable.dispatroled_icon, R.drawable.patroled_icon, R.drawable.patroled_icon,
//            R.drawable.patroled_icon };
    private String[] iconName = { "通讯录", "日历", "照相机", "时钟", "游戏", "短信", "铃声",
            "设置", "语音", "天气", "浏览器", "视频" };

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patrol_new);
        initData();//数据初始化
        initView();//界面初始化

        gview = (GridView) findViewById(R.id.gv);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();
        //新建适配器
        String [] from ={"image","text"};
        int [] to = {R.id.image,R.id.tvPosition};
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.patrol_item_new, from, to);
        //配置适配器
        gview.setAdapter(sim_adapter);
        for(int i=0;i<Chose.myAllPatrolCardDB.count();i++){
            if(Chose.myAllPatrolCardDB.getCodeById(i).equals(cardNo)){
                gview.smoothScrollToPosition(i);
            }
        }
    }

    public List<Map<String, Object>> getData(){
        //cion和iconName的长度是相同的，这里任选其一都可以
        boolean isPatroled = false;
        for(int i=0;i<Chose.myAllPatrolCardDB.count();i++){
            Map<String, Object> map = new HashMap<String, Object>();
            for(int j=0;j<patrolList.size();j++){
                if(Chose.myAllPatrolCardDB.getCodeById(i).equals(patrolList.get(j).getCardNo())){
                    isPatroled = true;
                }
            }
            if(isPatroled){
                map.put("image", R.drawable.patroled_icon);
            }else {
                map.put("image", R.drawable.dispatroled_icon);
            }
            map.put("text", Chose.myAllPatrolCardDB.getPositionById(i));
            data_list.add(map);
        }

        return data_list;
    }

    //初始化数据
    private void initData() {
        Intent intent1 = this.getIntent();
        unitId = intent1.getStringExtra("unitID");
        unitName = intent1.getStringExtra("unitName");
        cardNo = intent1.getStringExtra("code");
        String position = intent1.getStringExtra("position");



        patrolList = Chose.patrolDB.getAllData();



        long timeStamp = System.currentTimeMillis() / 1000;
        String strTime = String.valueOf(timeStamp);
        if(position != null){
            patrolList.add(new PatrolItem(cardNo, position, strTime));
            Chose.patrolDB.insert(cardNo, position, strTime);
        }

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

    //初始化界面
    private void initView() {
//        tvMsg = (TextView) findViewById(R.id.tv_msg);
//
//        patrolAdapter = new PatrolAdapter(this, patrolList);
//        lvPatrol = (ListView) findViewById(R.id.lvPatrol);
//        lvPatrol.setAdapter(patrolAdapter);
//        patrolAdapter.notifyDataSetChanged();
    }


    //NFC扫描卡片获取信息
    protected void onNewIntent(Intent intent) {
        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
        NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
        try {
            if (mNdefRecord != null) {
                cardNo = new String(mNdefRecord.getPayload(), "UTF-8");
                cardNo = cardNo.substring(1, cardNo.length());

                if (Chose.myAllPatrolCardDB.isMyCard(cardNo) != 0) {
                    long timeStamp = System.currentTimeMillis() / 1000;
                    String strTime = String.valueOf(timeStamp);

                    patrolList.add(new PatrolItem(cardNo, Chose.myAllPatrolCardDB.getPosition(cardNo), strTime));
                    Chose.patrolDB.insert(cardNo, Chose.myAllPatrolCardDB.getPosition(cardNo), strTime);

//                    patrolAdapter.notifyDataSetChanged();
//                    tvMsg.setText("目前有 " + patrolList.size() + " 条巡检记录等待上传");

                } else {
                    Toast.makeText(Patrol_new.this, "未知卡片", Toast.LENGTH_LONG).show();
                }

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilter, mTechList);

    }

    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
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















