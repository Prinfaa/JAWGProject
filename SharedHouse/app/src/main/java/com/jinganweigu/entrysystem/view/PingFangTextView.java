package com.jinganweigu.entrysystem.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jinganweigu.entrysystem.utils.FontCustom;

/**
 * 作者： Prinfaa .
 * 创建时间：2018/1/29 0029 13:10
 * 功能描述：
 */

public class PingFangTextView extends TextView {



    public PingFangTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

       init(context);

    }

    private void init(Context context) {
        //设置字体样式
        setTypeface(FontCustom.setFont(context));
    }
}
