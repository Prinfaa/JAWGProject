package com.example.dell_pc.microstationreceivealarm;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

public class WebFireCarActivity extends AppCompatActivity {

    private LinearLayout llRoot;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_fire_car);

        llRoot= (LinearLayout) findViewById(R.id.ll_root);

        mWebView = new WebView(getApplicationContext());
        llRoot.addView(mWebView);

        initData();

    }
    public void initData() {

        WebSettings settings = mWebView.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);

//                mDialog.dismiss();
            }

            @SuppressLint("NewApi")
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

                // this will ignore the Ssl error and will go forward to your site
                handler.proceed();
                error.getCertificate();
            }
        });

        mWebView.loadUrl("http://dndzl.cn/display/src/main/webapp/web/hydrant.html");
//        if ("活动规则".equals(mTopnameText)) {
//            mWebView.loadUrl(Url.HOME_IP + "/Area/UserPage/RuleView?ruleId=1");
//        } else if ("详情".equals(mTopnameText)) {
//
//        } else if ("服务协议".equals(mTopnameText)) {
//            mWebView.loadUrl(Url.HOME_IP + "/WAP/WAPUserCenter/WAPProtocol");
//        }

    }

    @Override
    protected void onDestroy() {
        try {
            if (mWebView != null) {
                mWebView.removeAllViews();
                mWebView.destroy();
                mWebView = null;
                llRoot.removeAllViews();
                llRoot = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }



}
