package com.example.dell_pc.unitpad.tools;

import android.annotation.TargetApi;
import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by dell-pc on 2017/10/5.
 */
public class MyWebViewSet {
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static WebView Set(WebView webView){
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
//        webSettings.setCacheMode(WebSettings.LOAD_NORMAL);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webSettings.setAllowFileAccessFromFileURLs(true);
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        return webView;
    }

//    WebSettings webSettings = webView.getSettings();
//        webSettings.setUseWideViewPort(true);
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//
//        webView.setWebViewClient(new WebViewClient());
}
