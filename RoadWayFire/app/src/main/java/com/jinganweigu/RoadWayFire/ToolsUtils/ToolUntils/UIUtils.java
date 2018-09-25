package com.jinganweigu.RoadWayFire.ToolsUtils.ToolUntils;

import android.content.Context;

public class UIUtils {


    /**
     * 底部菜单栏红点尺寸
     */

    public static int dip2px(Context context,double dip) {
        //dp和px的转换关系比例值
//        float density = context.getResources().getDisplayMetrics().density;
//        return (int) (dip * density + 0.5);

        if(context!=null){
            float density = context.getResources().getDisplayMetrics().density;
            return (int) (dip * density + 0.5);
        }else
        return  (int)dip;
    }

}
