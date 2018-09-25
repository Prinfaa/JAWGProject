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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dell_pc.qr_code_patrol.adpter.PatrolAdapter;
import com.example.dell_pc.qr_code_patrol.item.PatrolItem;
import com.example.dell_pc.qr_code_patrol.view.MyProgress;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by dell-pc on 2016/2/2.
 */
public class Patrol extends Activity {
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

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patrol);
        initData();//数据初始化
        initView();//界面初始化
    }

    //初始化数据
    private void initData() {
        Intent intent1 = this.getIntent();
        unitId = intent1.getStringExtra("unitID");
        unitName = intent1.getStringExtra("unitName");
        String code = intent1.getStringExtra("code");
        String position = intent1.getStringExtra("position");



        patrolList = Chose.patrolDB.getAllData();



        long timeStamp = System.currentTimeMillis() / 1000;
        String strTime = String.valueOf(timeStamp);
        if(position != null){
            patrolList.add(new PatrolItem(code, position, strTime));
            Chose.patrolDB.insert(code, position, strTime);
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
        tvMsg = (TextView) findViewById(R.id.tv_msg);

        patrolAdapter = new PatrolAdapter(this, patrolList);
        lvPatrol = (ListView) findViewById(R.id.lvPatrol);
        lvPatrol.setAdapter(patrolAdapter);
        patrolAdapter.notifyDataSetChanged();
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

                    patrolAdapter.notifyDataSetChanged();
//                    tvMsg.setText("目前有 " + patrolList.size() + " 条巡检记录等待上传");

                } else {
                    Toast.makeText(Patrol.this, "未知卡片", Toast.LENGTH_LONG).show();
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















