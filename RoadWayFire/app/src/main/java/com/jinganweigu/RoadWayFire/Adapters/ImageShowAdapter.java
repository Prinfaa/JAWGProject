package com.jinganweigu.RoadWayFire.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.jinganweigu.RoadWayFire.Entries.ImagePathBean;
import com.jinganweigu.RoadWayFire.ToolsUtils.ImageUtil.LoadImageUtil;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class ImageShowAdapter extends PagerAdapter {

    private List<ImagePathBean.ResultBean> path= new ArrayList<>();
    private Context context;
    private OnPicturePositionChangeListner listner;

    public ImageShowAdapter(List<ImagePathBean.ResultBean> path, Context context) {
        this.path = path;
        this.context = context;
        listner= (OnPicturePositionChangeListner) context;
    }

    @Override
    public int getCount() {
        return path.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        PhotoView  photoView=new PhotoView(context);
        String d = path.get(position).getPhoto_adress();

        LoadImageUtil.ShowImage(context,d,photoView);

//        imagePicker.getImageLoader().displayImagePreview(mActivity, imageItem.path, photoView, screenWidth, screenHeight);

        container.addView(photoView);

        return photoView;
    }

    @Override  //获取当前位置和对象
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        listner.OnPicturePositionChange(position);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface OnPicturePositionChangeListner{

        void OnPicturePositionChange(int position);

    }




}
