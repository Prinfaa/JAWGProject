package com.jinganweigu.entrysystem.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;


import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.common.ConstantCls;

import java.io.File;

/**
 * 优化BitmapUtils,压缩，图片缓存
 */
public class BitmapUtilOptimize {
    BitmapUtils bitmapUtils;
    private BitmapDisplayConfig config;

    public BitmapUtilOptimize(Context context) {
        //bitmapUtils = new BitmapUtils(context);
        config = new BitmapDisplayConfig();
        config.setBitmapConfig(Bitmap.Config.RGB_565);
        config.setLoadFailedDrawable(context.getResources().getDrawable(R.mipmap.placeholder));
        config.setLoadingDrawable(context.getResources().getDrawable(R.mipmap.placeholder));

        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        //设置文件缓存、内存缓存大小
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        bitmapUtils = new BitmapUtils(context, ConstantCls.CACHE_DIR, cacheSize);
        bitmapUtils.configDefaultBitmapMaxSize(200, 200);
    }

    /*设置默认加载图片*/
    public void setDefaultImage(int resId) {
        bitmapUtils.configDefaultLoadFailedImage(resId);
    }

    public void display(View container, String uri) {
        bitmapUtils.display(container, uri, config);
    }

    public String getBitmapFileFromDiskCache(String uri) {
        File file = bitmapUtils.getBitmapFileFromDiskCache(uri);
        if (file != null && file.exists())
            return file.getAbsolutePath();
        else return "";
    }

}
