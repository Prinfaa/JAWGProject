/* 
 * @ProjectName VideoGoJar
 * @Copyright HangZhou Hikvision System Technology Co.,Ltd. All Right Reserved
 * 
 * @FileName CameraListAdapter.java
 * @Description 这里对文件进行描述
 * 
 * @author chenxingyf1
 * @data 2014-7-14
 * 
 * @note 这里写本文件的详细功能描述和注释
 * @note 历史记录
 * 
 * @warning 这里写本文件的相关警告
 */
package com.jinganweigu.RoadWayFire.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.EZUIkitUtils.EZUtils;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.EZDeviceInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 摄像头列表适配器
 * @author chenxingyf1
 * @data 2014-7-14
 */
public class EZCameraListAdapter extends BaseAdapter {
    private static final String TAG = "CameraListAdapter";

    private Context mContext = null;
    private List<EZDeviceInfo> mCameraInfoList = null;
    /** 监听对象 */
    private OnClickListener mListener;
    private ExecutorService mExecutorService = null;// 线程池
    public Map<String, EZDeviceInfo> mExecuteItemMap = null;


    public void clearAll(){
        mCameraInfoList.clear();
        notifyDataSetChanged();
    }

    public List<EZDeviceInfo> getDeviceInfoList() {
        return mCameraInfoList;
    }

    /**
     * 自定义控件集合
     * 
     * @author dengsh
     * @data 2012-6-25
     */
    public static class ViewHolder {
        public ImageView iconIv;

        public ImageView playBtn;

        public ImageView offlineBtn;

        public TextView cameraNameTv;
        




        public View itemIconArea;

        public ImageView offlineBgBtn;
        

        public ImageButton devicePicBtn;
        
        public ImageButton deviceVideoBtn;
        
        public View deviceDefenceRl;
        public ImageButton deviceDefenceBtn;
    }
    
    public EZCameraListAdapter(Context context) {
        mContext = context;
        mCameraInfoList = new ArrayList<EZDeviceInfo>();
        mExecuteItemMap = new HashMap<String, EZDeviceInfo>();

    }
    
    public void setOnClickListener(OnClickListener l) {
        mListener = l;
    }
    
    public void addItem(EZDeviceInfo item) {
        mCameraInfoList.add(item);
    }

    public void removeItem(EZDeviceInfo item) {
        for(int i = 0; i < mCameraInfoList.size(); i++) {
            if(item == mCameraInfoList.get(i)) {
                mCameraInfoList.remove(i);
            }
        }
    }
    
    public void clearItem() {
        //mExecuteItemMap.clear();
        mCameraInfoList.clear();
    }
    
    /* (non-Javadoc)
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        return mCameraInfoList.size();
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public EZDeviceInfo getItem(int position) {
        EZDeviceInfo item = null;
        if (position >= 0 && getCount() > position) {
            item = mCameraInfoList.get(position);
        }
        return item;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 自定义视图
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            // 获取list_item布局文件的视图
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cameralist_small_item, null);

            // 获取控件对象
            viewHolder.iconIv = (ImageView) convertView.findViewById(R.id.item_icon);
            viewHolder.playBtn = (ImageView) convertView.findViewById(R.id.item_play_btn);

            viewHolder.offlineBtn = (ImageView) convertView.findViewById(R.id.item_offline);
            viewHolder.cameraNameTv = (TextView) convertView.findViewById(R.id.camera_name_tv);
            viewHolder.offlineBgBtn = (ImageView) convertView.findViewById(R.id.offline_bg);
            viewHolder.itemIconArea = convertView.findViewById(R.id.item_icon_area);
            viewHolder.devicePicBtn = (ImageButton) convertView.findViewById(R.id.tab_devicepicture_btn);
            viewHolder.deviceVideoBtn = (ImageButton) convertView.findViewById(R.id.tab_devicevideo_btn);
            viewHolder.deviceDefenceRl = convertView.findViewById(R.id.tab_devicedefence_rl);
            viewHolder.deviceDefenceBtn = (ImageButton) convertView.findViewById(R.id.tab_devicedefence_btn);
            
            // 设置点击图标的监听响应函数
            viewHolder.playBtn.setOnClickListener(mOnClickListener);

            viewHolder.devicePicBtn.setOnClickListener(mOnClickListener);
            
            viewHolder.deviceVideoBtn.setOnClickListener(mOnClickListener);
            
            viewHolder.deviceDefenceBtn.setOnClickListener(mOnClickListener);
            
            // 设置控件集到convertView
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        // 设置position
        viewHolder.playBtn.setTag(position);
        viewHolder.devicePicBtn.setTag(position);
        viewHolder.deviceVideoBtn.setTag(position);
        viewHolder.deviceDefenceBtn.setTag(position);


        final EZDeviceInfo deviceInfo = getItem(position);
        final EZCameraInfo cameraInfo = EZUtils.getCameraInfoFromDevice(deviceInfo,0);
        if (deviceInfo != null){
            if (deviceInfo.getStatus() == 2) {
                viewHolder.offlineBtn.setVisibility(View.VISIBLE);
                viewHolder.offlineBgBtn.setVisibility(View.VISIBLE);
                viewHolder.playBtn.setVisibility(View.GONE);
                viewHolder.deviceDefenceRl.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.offlineBtn.setVisibility(View.GONE);
                viewHolder.offlineBgBtn.setVisibility(View.GONE);
                viewHolder.playBtn.setVisibility(View.VISIBLE);
                viewHolder.deviceDefenceRl.setVisibility(View.VISIBLE);
            }
            viewHolder.cameraNameTv.setText(deviceInfo.getDeviceName());
            viewHolder.iconIv.setVisibility(View.VISIBLE);
            String imageUrl = deviceInfo.getDeviceCover();
            if(!TextUtils.isEmpty(imageUrl)) {

               if(deviceInfo.getCameraInfoList().size()==1) {

                   viewHolder.iconIv.setImageResource(R.mipmap.ipc);
               }else{
//                   Glide.with(mContext).load(imageUrl).placeholder(R.mipmap.group_ipc).into(viewHolder.iconIv);
                    viewHolder.iconIv.setImageResource(R.mipmap.group_ipc);

               }

            }
        }
        return convertView;
    }

    public void shutDownExecutorService() {
        if (mExecutorService != null) {
            if (!mExecutorService.isShutdown()) {
                mExecutorService.shutdown();
            }
            mExecutorService = null;
        }
    }
    
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = (Integer) v.getTag();
                switch (v.getId()) {
                    case R.id.item_play_btn:
                        mListener.onPlayClick(EZCameraListAdapter.this, v, position);
                        break;

                    case R.id.tab_devicepicture_btn:
                        mListener.onDevicePictureClick(EZCameraListAdapter.this, v, position);
                        break;
                        
                    case R.id.tab_devicevideo_btn: 
                        mListener.onDeviceVideoClick(EZCameraListAdapter.this, v, position);
                        break;      
                        
                    case R.id.tab_devicedefence_btn: 
                        mListener.onDeviceDefenceClick(EZCameraListAdapter.this, v, position);
                        break;                          
                }
            }
        }
    };
    
    public interface OnClickListener {

        public void onPlayClick(BaseAdapter adapter, View view, int position);

        public void onDevicePictureClick(BaseAdapter adapter, View view, int position);
        
        public void onDeviceVideoClick(BaseAdapter adapter, View view, int position);
        
        public void onDeviceDefenceClick(BaseAdapter adapter, View view, int position);
    }
}
