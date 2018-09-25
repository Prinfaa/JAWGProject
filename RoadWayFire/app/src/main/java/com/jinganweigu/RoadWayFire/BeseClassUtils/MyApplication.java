package com.jinganweigu.RoadWayFire.BeseClassUtils;

import android.app.Activity;
import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.jinganweigu.RoadWayFire.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.videogo.openapi.EZOpenSDK;

import java.util.LinkedList;
import java.util.List;

public class MyApplication extends Application {

    public static MyApplication instance;

    private static final String APPKEY="3b2f0a0c37b44e1388658e58622cd7bd";
    //运用list来保存们每一个activity是关键
    public List<Activity> mList = new LinkedList<Activity>();

    public static EZOpenSDK getOpenSDK() {
        return EZOpenSDK.getInstance();
    }


    //构造方法
    public MyApplication(){}
    //实例化一次
    public synchronized static MyApplication getInstance(){
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);


        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(this);

        ImageLoader.getInstance().init(config);     //UniversalImageLoader初始化

        /**
         * sdk日志开关，正式发布需要去掉false
         */
        EZOpenSDK.showSDKLog(true);

        /**
         * 设置是否支持P2P取流,详见api
         */
        EZOpenSDK.enableP2P(false);

        //初始化
        EZOpenSDK.initLib(this,APPKEY,"");

    }

    public static DisplayImageOptions imageLoaderOptions = new DisplayImageOptions.Builder()//
            .showImageOnLoading(R.drawable.ic_default_image)         //设置图片在下载期间显示的图片
            .showImageForEmptyUri(R.drawable.ic_default_image)       //设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.drawable.ic_default_image)            //设置图片加载/解码过程中错误时候显示的图片
            .cacheInMemory(true)                                //设置下载的图片是否缓存在内存中
            .cacheOnDisk(true)                                  //设置下载的图片是否缓存在SD卡中
            .build();                                           //构建完成

//    public static ImageOptions xUtilsOptions = new ImageOptions.Builder()//
//            .setIgnoreGif(false)                                //是否忽略GIF格式的图片
//            .setImageScaleType(ImageView.ScaleType.FIT_CENTER)  //缩放模式
//            .setLoadingDrawableId(R.drawable.ic_default_image)       //下载中显示的图片
//            .setFailureDrawableId(R.drawable.ic_default_image)       //下载失败显示的图片
//            .build();                                           //得到ImageOptions对象

    public void addActivity(Activity activity) {
        mList.add(activity);
    }
    //关闭每一个list内的activity
    public void exit() {
        try {
            for (Activity activity:mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }



}
