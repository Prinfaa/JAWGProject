package com.example.dell_pc.unitpad.tools;

import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

/**
 * Created by dell-pc on 2017/10/5.
 */
public class MyAnimation {
    public void Come(View view, int duration, float f1, float f2, float f3, float f4) {
        view.setVisibility(View.INVISIBLE);
        //初始化 Translate动画
        TranslateAnimation translateAnimation = new TranslateAnimation(f1, f2, f3, f4);

        AnimationSet set = new AnimationSet(true);
        set.addAnimation(translateAnimation);

        set.setDuration(duration);
        view.startAnimation(set);
        view.setVisibility(View.VISIBLE);
    }

    public void Go(View view, int duration, float f1, float f2, float f3, float f4) {
        view.setVisibility(View.VISIBLE);
        //初始化 Translate动画
        TranslateAnimation translateAnimation = new TranslateAnimation(f1, f2, f3, f4);

        AnimationSet set = new AnimationSet(true);
        set.addAnimation(translateAnimation);

        set.setDuration(duration);
        view.startAnimation(set);
        view.setVisibility(View.INVISIBLE);
    }

}
