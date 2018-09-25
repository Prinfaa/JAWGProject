package com.jinganweigu.RoadWayFire.ToolsUtils.EZUIkitUtils;

import android.app.Activity;

import com.jinganweigu.RoadWayFire.BeseClassUtils.MyApplication;
import com.videogo.exception.BaseException;
import com.videogo.openapi.EZGlobalSDK;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.bean.EZAreaInfo;
import com.videogo.util.LogUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/10/11 0011.
 */

public class ActivityUtils {

    /**
     * 处理token过期的错误
     *
     * @throws
     */
    public static void handleSessionException(Activity activity) {
        goToLoginAgain(activity);
    }

    public static void goToLoginAgain(Activity activity) {

        if (EZGlobalSDK.class.isInstance(MyApplication.getOpenSDK())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<EZAreaInfo> areaList = EZGlobalSDK.getInstance().getAreaList();
                        if (areaList != null) {
                            LogUtil.debugLog("application", "list count: " + areaList.size());

                            EZAreaInfo areaInfo = areaList.get(0);
                            EZGlobalSDK.getInstance().openLoginPage(areaInfo.getId());
//                            EZGlobalSDK.getInstance().openLoginPage(areaInfo.);

                        }
                    } catch (BaseException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            EZOpenSDK.getInstance().openLoginPage();
        }
    }



}
