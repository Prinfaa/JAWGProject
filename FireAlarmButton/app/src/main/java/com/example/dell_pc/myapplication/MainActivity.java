package com.example.dell_pc.myapplication;

import android.app.Service;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.entity.mime.Header;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnK88, btn_jawg, btn0, btn1, btn2, btn3, btn4;
    private Button btnUp1, btnDown1;
    private Button btnUp2, btnDown2;
    private Button btnUp3, btnDown3;

    private String constructionID;

    private String carID = null;
    private String URL_K88_ALARM = "http://jn.xiaofang365.cn/xfwlw/xfwlw/addRTURecord.php?deviceID=866710032533538&s0=1";
    private String URL_JAWG_ALARM = "http://jn.xiaofang365.cn/xfwlw/xfwlw/addRTURecord.php?deviceID=111111111111111&s0=1";
    private String URL_UP_LEVEL = "http://jn.xiaofang365.cn/xfwlw/fireCommand/upCarLevel.php?carID=";
    private String URL_DOWN_LEVEL = "http://jn.xiaofang365.cn/xfwlw/fireCommand/downCarLevel.php?carID=";
    private boolean isUp = false;
    private boolean isDown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnK88 = (Button) findViewById(R.id.btnK88);
        btn_jawg = (Button) findViewById(R.id.btnjawg);
        btn0 = (Button) findViewById(R.id.btn0);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);

        btnUp1 = (Button) findViewById(R.id.btnUp1);
        btnDown1 = (Button) findViewById(R.id.btnDown1);
        btnUp2 = (Button) findViewById(R.id.btnUp2);
        btnDown2 = (Button) findViewById(R.id.btnDown2);
        btnUp3 = (Button) findViewById(R.id.btnUp3);
        btnDown3 = (Button) findViewById(R.id.btnDown3);

        btnK88.setOnClickListener(this);
        btn_jawg.setOnClickListener(this);
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btnUp1.setOnClickListener(this);
        btnDown1.setOnClickListener(this);
        btnUp2.setOnClickListener(this);
        btnDown2.setOnClickListener(this);
        btnUp3.setOnClickListener(this);
        btnDown3.setOnClickListener(this);


    }



    @Override
    public void onClick(View view) {
        if (view == btn0) {
            Vibrator vib = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(1000);
            constructionID = "4";
            sendAlarm();
        } else if (view == btn1) {
            Vibrator vib = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(1000);
            constructionID = "1";
            sendAlarm();
        } else if (view == btn2) {
            Vibrator vib = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(1000);
            constructionID = "2";
            sendAlarm();

        } else if (view == btn3) {
            Vibrator vib = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(1000);
            constructionID = "3";
            sendAlarm();

        } else if (view == btn4) {
            Vibrator vib = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(1000);
            clearAlarm();
        }else if(view == btnUp1){
            carID = "0004";
            HttpUtils.getJSON(URL_UP_LEVEL + carID, null);
        }else if(view == btnUp2){
            carID = "0005";
            HttpUtils.getJSON(URL_UP_LEVEL + carID, null);
        }else if(view == btnUp3){
            carID = "0006";
            HttpUtils.getJSON(URL_UP_LEVEL + carID, null);
        }else if(view == btnDown1){
            carID = "0004";
            HttpUtils.getJSON(URL_DOWN_LEVEL + carID, null);
        }else if(view == btnDown2){
            carID = "0005";
            HttpUtils.getJSON(URL_DOWN_LEVEL + carID, null);
        }else if(view == btnDown3){
            carID = "0006";
            HttpUtils.getJSON(URL_DOWN_LEVEL + carID, null);
        }else if(view == btn_jawg){
            HttpUtils.getJSON(URL_JAWG_ALARM, null);
            Vibrator vib = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(500);
        }else if(view == btnK88){
            HttpUtils.getJSON(URL_K88_ALARM, null);
            Vibrator vib = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(500);
        }
    }




    private void sendAlarm() {

        String url = "http://jn.xiaofang365.cn/xfwlw/fireCommand/sendFireAlarm.php";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("constructionID", constructionID);

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                Toast.makeText(MainActivity.this, "报警已发送!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(MainActivity.this, "网络故障，报警未发送！", Toast.LENGTH_LONG).show();
            }

        });


    }

    private void clearAlarm() {

        String url = "http://jn.xiaofang365.cn/xfwlw/fireCommand/clearFireAlarm.php";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                Toast.makeText(MainActivity.this, "火警已消除!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(MainActivity.this, "网络故障，火警未消除！", Toast.LENGTH_LONG).show();
            }

        });


    }


}
