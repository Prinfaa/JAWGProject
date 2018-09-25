package com.example.dell.unittv.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.webkit.WebView;

/**
 * Created by dell-pc on 2017/10/5.
 */
public class MyWebView extends WebView {
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public MyWebView(Context context) {
        super(context);
//        WebSettings webSettings = this.getSettings();
//        webSettings.setUseWideViewPort(true);
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setCacheMode(WebSettings.LOAD_NORMAL);
//        webSettings.setAllowFileAccessFromFileURLs(true);
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

    }
}
