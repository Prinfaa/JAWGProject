package com.jinganweigu.RoadWayFire.BeseClassUtils;

import android.os.Environment;

/**
 * @作者 : 房玉虎
 * @创建时间：2016/9/13 0013 15:39
 * @类说明 ：常量类
 */

public class ConstantCls {

    private static final String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
    //图片拍照、裁剪和压缩后保存的本地路径
    public static final String IMAGE_PATH = SDCARD + "/XI_HUI_JIA/IMAGE";
    //图片缓存路径
    public static final String CACHE_DIR = SDCARD + "/XI_HUI_JIA/cache/";

}
