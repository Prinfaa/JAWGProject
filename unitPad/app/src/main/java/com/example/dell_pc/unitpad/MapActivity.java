package com.example.dell_pc.unitpad;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MapActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView webView;
    private Button btnReceive, btnClear;
    public MediaPlayer mediaPlayer = new MediaPlayer();
    boolean isReceived = false;
    boolean isFire = false;
    int adds = 0;
    private String URL_CHECK_ALARM = "http://dndzl.cn/daping/fire.php?unit_id=330";
    private String K88_URL_CHECK_ALARM = "http://dndzl.cn/daping/fire.php?unit_id=328";
    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1080) {//有值报警

            } else {//没有值

                clearAlarm();

                finish();
            }
        }
    };


    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        adds = getIntent().getIntExtra("where", 1);
        btnReceive = (Button) findViewById(R.id.btnReceive);
        btnClear = (Button) findViewById(R.id.btnClear);
        webView = (WebView) findViewById(R.id.webView);
        btnReceive.setOnClickListener(this);
        btnClear.setOnClickListener(this);
//        webView.loadUrl("http://jn.xiaofang365.cn/xfwlw/fireCommand/fireMap_new.php?communityID=" + communityID + "&fireStationID=" + fireStationID);
//        webView.loadUrl("http://dndzl.cn/alarm_map/car.html");

        if (adds == 1) {

            webView.loadUrl("http://dndzl.cn/alarm_map/electricCars.html");

        } else if (adds == 2) {

            webView.loadUrl("http://ali.fis119.com/alarm_map/fangyuhu.html");

        }


        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(new Contact(), "FireAlarm");
//        carLevelThread.start();


        mediaPlayer = MediaPlayer.create(MapActivity.this, R.raw.alarm8);


        new Thread() {
            public void run() {
                while (true) {
                    try {
                        sleep(1000);
//                        if (isFire == true && isReceived == false) {


                        if (adds == 1) {
                            getAlarmJSON(URL_CHECK_ALARM, myHandler);
                        } else if (adds == 2) {
                            getAlarmJSON(K88_URL_CHECK_ALARM, myHandler);
                        }

                        if (mediaPlayer == null) {
                            return;
                        }

                        if (mediaPlayer.isPlaying() == false) {

                            mediaPlayer.start();
                        }


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();

    }


    public void getAlarmJSON(final String url, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn;
                InputStream is;
                try {
                    conn = (HttpURLConnection) new URL(url).openConnection();
                    conn.setRequestMethod("GET");
                    is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line = "";
                    StringBuilder result = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    if (result.toString().length() > 10) {

                        handler.sendEmptyMessage(1080);
                    } else {
                        handler.sendEmptyMessage(1090);

                    }


                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void clearAlarm() {

        String url = "http://jn.xiaofang365.cn/xfwlw/fireCommand/clearFireAlarm.php";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                Toast.makeText(MapActivity.this, "火警已消除!", Toast.LENGTH_SHORT).show();
                new Contact().clearAlarm();
                isReceived = false;
                isFire = false;
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(MapActivity.this, "网络故障，火警未消除！", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onClick(View view) {

        if (view == btnReceive) {
//            isReceived = true;

            if (mediaPlayer != null & mediaPlayer.isPlaying()) {


                mediaPlayer.stop();

            }


        } else if (view == btnClear) {

            AlertDialog.Builder multiDia = new AlertDialog.Builder(MapActivity.this);
            multiDia.setTitle("您确定火警已消除？");
            multiDia.setPositiveButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                }
            });
            multiDia.setNeutralButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                    clearAlarm();

                    finish();
                }
            });
            multiDia.create().show();
        }


    }


    private final class Contact {


        public void clearAlarm() {
            // 调用JS中的方法
            webView.loadUrl("javascript:clearAlarm()");
//            webView.loadUrl("javascript:custom_close()");
        }

    }

}
