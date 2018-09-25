package com.jinganweigu.entrysystem.view;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * 作者： Prinfaa .
 * 创建时间：2018/1/29 0029 14:17
 * 功能描述：
 */

public class PhoneClickSpan extends ClickableSpan {

    public interface OnLinkClickListener {
        void onLinkClick(View view);
    }

    private OnLinkClickListener listener;

    public PhoneClickSpan(OnLinkClickListener listener) {
        super();
        this.listener = listener;
    }

    @Override
    public void onClick(View widget) {
        listener.onLinkClick(widget);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(Color.argb(255, 255,255,255)); // 设置字体颜色
        ds.setUnderlineText(true); //去掉下划线
    }

}