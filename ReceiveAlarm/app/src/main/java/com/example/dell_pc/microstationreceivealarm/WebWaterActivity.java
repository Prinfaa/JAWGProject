package com.example.dell_pc.microstationreceivealarm;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.example.dell_pc.microstationreceivealarm.tools.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class WebWaterActivity extends AppCompatActivity {

    private WebView mWebView;
    private String mLink;
    private WebView webView;

    private LinearLayout llRoot;
    boolean isFire = false;
    boolean isAct = true;

    private String URL_GET_FIRE = "http://jn.xiaofang365.cn/xfwlw/fireCommand/findFireAlarm.php?communityID=13";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mLink = getIntent().getStringExtra("link");
        webView= (WebView) findViewById(R.id.webView);

        webView.loadUrl("http://dndzl.cn/display/src/main/webapp/web/hydrant.html");

        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);


//        webSettings.setUseWideViewPort(true);
//        webSettings.setLoadWithOverviewMode(true);

        //主要用于平板，针对特定屏幕代码调整分辨率
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型：
         * 1、LayoutAlgorithm.NARROW_COLUMNS ：适应内容大小
         * 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);


        webView.setWebViewClient(new WebViewClient());

//        new Thread() {
//            public void run() {
//                while (true) {
//                    try {
//                        if (isAct) {
//                            sleep(1000);
//                            if (isFire == true) {
//                                isAct = false;
//
//                                startActivity(new Intent(WebWaterActivity.this, Map.class));
//                            }
//                            HttpUtils.getJSON(URL_GET_FIRE, getConstructionHandler);
//                        }
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        }.start();





    }

    private Handler getConstructionHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String jsonData = (String) msg.obj;

            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String id = object.getString("constructionID");
                    String name = object.getString("constructionName");
                    String address = object.getString("address");
                    String type = object.getString("type");
                    String structure = object.getString("structure");
                    String area = object.getString("area");
                    String layer = object.getString("layer");
                    String height = object.getString("height");
                    String fac = object.getString("fac");
                    String goods = object.getString("goods");
                    String picUrl = object.getString("picUrl");
//                    fireLat = object.getString("lat");
//                    fireLng = object.getString("long");
//                    constructionList.add(new ConstructionItem(id, name, address, type, structure, area, layer, height, fac, goods, picUrl));
                    isFire = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


//
//    public void initData() {
//
//
//
//        // 设置WebView的客户端
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return false;// 返回false
//            }
//        });
//        webView.loadUrl("http://dndzl.cn/display/src/main/webapp/web/hydrant.html");
//
//    }

    @Override
    protected void onDestroy() {
//        try {
//            if (mWebView != null) {
//                mWebView.removeAllViews();
//                mWebView.destroy();
//                mWebView = null;
//                llRoot.removeAllViews();
//                llRoot = null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        super.onDestroy();
    }


    @Override
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }

//    @Override
//    public void initEvents() {
//        backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                intentMethod.rebackMethod(WebViewActivity.this);
//            }
//        });
//
//    }




}
