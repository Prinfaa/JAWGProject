package com.jinganweigu.entrysystem.utils;

import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 作者： Prinfaa .
 * 创建时间：2017/12/28 0028 13:50
 * 功能描述：
 */
public class ImageSizeUtil
{
    /**
     * 根据ImageView获适当的压缩的宽和高
     *
     * @param imageView
     * @return
     */
    public static ImageSize getImageViewSize(ImageView imageView)
    {

        ImageSize imageSize = new ImageSize();
        DisplayMetrics displayMetrics = imageView.getContext().getResources()
                .getDisplayMetrics();


        ViewGroup.LayoutParams lp = imageView.getLayoutParams();

        int width = imageView.getWidth();// 获取imageview的实际宽度
        if (width <= 0)
        {if(lp!=null){
            width = lp.width;// 获取imageview在layout中声明的宽度
        }

        }
        if (width <= 0)
        {
            width = imageView.getMaxWidth();// 检查最大值
//            width = getImageViewFieldValue(imageView, "mMaxWidth");
        }
        if (width <= 0)
        {
            width = displayMetrics.widthPixels;
        }

        int height = imageView.getHeight();// 获取imageview的实际高度
        if (height <= 0)
        {if(lp!=null){
            height = lp.height;// 获取imageview在layout中声明的宽度
        }

        }
        if (height <= 0)
        {
//            height = getImageViewFieldValue(imageView, "mMaxHeight");// 检查最大值
            height = displayMetrics.heightPixels;
        }
        if (height <= 0)
        {
            height = displayMetrics.heightPixels;
        }
        imageSize.width = width;
        imageSize.height = height;

        return imageSize;
    }

    public static class ImageSize
    {
        public int width;
        public  int height;
    }
}
