package com.jinganweigu.entrysystem.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.common.BaseActivity;
import com.jinganweigu.entrysystem.utils.MyApplication;
import com.jinganweigu.entrysystem.utils.Mycontants;
import com.jinganweigu.entrysystem.view.CommonProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends BaseActivity {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.fl_title)
    FrameLayout flTitle;
    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    private String mTopnameText;
    private CommonProgressDialog mDialog;
//    private String mLink;
    private WebView mWebView;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        Intent intent = getIntent();
        mTopnameText = intent.getStringExtra(Mycontants.TOP_NAME);
//        mLink = intent.getStringExtra("link");
        topName.setText(mTopnameText);
        mDialog = new CommonProgressDialog(mContext, "正在加载...");
        mDialog.show();

        mWebView = new WebView(getApplicationContext());
        llRoot.addView(mWebView);
    }

    @Override
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

                mDialog.dismiss();
            }

            @SuppressLint("NewApi")
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

                // this will ignore the Ssl error and will go forward to your site
                handler.proceed();
                error.getCertificate();
            }
        });

        mWebView.loadUrl("");
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

    private void getDataFromNet() {

    }

    @Override
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }

    @Override
    public void initEvents() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentMethod.rebackMethod(WebViewActivity.this);
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation

    }
}
