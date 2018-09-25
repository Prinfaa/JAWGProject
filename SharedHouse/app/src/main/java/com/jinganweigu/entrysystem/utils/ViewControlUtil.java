package com.jinganweigu.entrysystem.utils;

import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * @作者: prinfaa
 * @创建时间: 2017/2/8
 * @功能描述:
 */
public class ViewControlUtil {

    /**
     * 如果有一行，右对齐。有两行，左对齐。
     * @param textView
     */
    public static void ControlTextGravity(final TextView textView) {
        ViewTreeObserver vto = textView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int lineCount = textView.getLineCount();
                if (lineCount == 1) {
                    textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                } else {
                    textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                }
                return true;
            }
        });

    }
}
