package com.jinganweigu.RoadWayFire.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.jinganweigu.RoadWayFire.Activities.EZRealPlayActivity;
import com.jinganweigu.RoadWayFire.Activities.EZVideoGroupActivity;
import com.jinganweigu.RoadWayFire.Activities.MapVideoActivity;
import com.jinganweigu.RoadWayFire.Adapters.EZCameraListAdapter;
import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseFragment2;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.EZUIkitUtils.ActivityUtils;
import com.jinganweigu.RoadWayFire.ToolsUtils.EZUIkitUtils.EZUtils;
import com.jinganweigu.RoadWayFire.ToolsUtils.EZUIkitUtils.SelectCameraDialog;
import com.jinganweigu.RoadWayFire.ToolsUtils.widget.EZDeviceAddLatAndLangInfo;
import com.videogo.constant.Constant;
import com.videogo.constant.IntentConsts;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.exception.BaseException;
import com.videogo.exception.ErrorCode;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.videogo.openapi.bean.req.GetDeviceInfo;
import com.videogo.util.ConnectionDetector;
import com.videogo.util.LogUtil;
import com.videogo.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.jinganweigu.RoadWayFire.BeseClassUtils.MyApplication.getOpenSDK;
import static com.videogo.openapi.EzvizAPI.TAG;

public class VideoFragment extends BaseFragment2 implements View.OnClickListener, SelectCameraDialog.CameraItemClick {
    Unbinder unbinder;
    @BindView(R.id.pull_refresh_grid)
    PullToRefreshGridView pullRefreshGrid;
    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    Unbinder unbinder1;

    private EZCameraListAdapter mAdapter = null;
    private BroadcastReceiver mReceiver = null;
    public final static int REQUEST_CODE = 100;
    public final static int RESULT_CODE = 101;
    private boolean bIsFromSetting = false;

    public final static int TAG_CLICK_PLAY = 1;
    public final static int TAG_CLICK_REMOTE_PLAY_BACK = 2;

    private int mClickType;

    private final static int LOAD_MY_DEVICE = 0;
    private final static int LOAD_SHARE_DEVICE = 1;
    private int mLoadType = LOAD_MY_DEVICE;
    public static List<EZDeviceAddLatAndLangInfo> infos = new ArrayList<>();

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.fragment_video, container, false);
        unbinder = ButterKnife.bind(this, view);
        backBtn.setVisibility(View.GONE);
        topName.setText("视频");


        return view;
    }

    @Override
    public void initData() {

        inotData();

//        Resources res = getActivity().getResources();
//        Bitmap bmp = BitmapFactory.decodeResource(res, R.mipmap.aaaa);
//
//        if (bmp != null) {
////          给图片添加水印
//            Bitmap bitmap = WaterMarkBg.drawCenterLable(getActivity(), bmp, "车道占用专用");
//            ivPhoto.setImageBitmap(bitmap);
//
//        }

        pullRefreshGrid.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {

                getCameraInfoList(true);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {

                getCameraInfoList(true);

            }
        });

        mAdapter = new EZCameraListAdapter(getActivity());
        mAdapter.setOnClickListener(new EZCameraListAdapter.OnClickListener() {
            @Override
            public void onPlayClick(BaseAdapter adapter, View view, int position) {
                mClickType = TAG_CLICK_PLAY;
                final EZDeviceInfo deviceInfo = mAdapter.getItem(position);

                if (deviceInfo.getCameraNum() <= 0 || deviceInfo.getCameraInfoList() == null || deviceInfo.getCameraInfoList().size() <= 0) {
                    return;
                }
                if (deviceInfo.getCameraNum() == 1 && deviceInfo.getCameraInfoList() != null && deviceInfo.getCameraInfoList().size() == 1) {

                    final EZCameraInfo cameraInfo = EZUtils.getCameraInfoFromDevice(deviceInfo, 0);

                    if (cameraInfo == null) {
                        return;
                    }
//                    Intent intent = new Intent(getActivity(), EZRealPlayActivity.class);
//                    intent.putExtra(IntentConsts.EXTRA_CAMERA_INFO, cameraInfo);
//                    intent.putExtra(IntentConsts.EXTRA_DEVICE_INFO, deviceInfo);
//                    startActivityForResult(intent, REQUEST_CODE);

                 Intent intent = new Intent(getActivity(), MapVideoActivity.class);

                    intent.putExtra("deviceSerial",deviceInfo.getDeviceSerial());
                    intent.putExtra("deviceName",deviceInfo.getDeviceName());
                    intent.putExtra("cameraNo", cameraInfo.getCameraNo());
                    intent.putExtra("position",deviceInfo.getDeviceName());
                    intent.putExtra("status",deviceInfo.getStatus());

                    startActivity(intent);

                    return;
                }
                Intent intent = new Intent(getActivity(), EZVideoGroupActivity.class);
                intent.putExtra("group", deviceInfo);
                startActivity(intent);


            }

            @Override
            public void onDevicePictureClick(BaseAdapter adapter, View view, int position) {
            }

            @Override
            public void onDeviceVideoClick(BaseAdapter adapter, View view, int position) {
            }

            @Override
            public void onDeviceDefenceClick(BaseAdapter adapter, View view,
                                             int position) {
            }
        });

        pullRefreshGrid.setAdapter(mAdapter);


        Utils.clearAllNotification(getActivity());

    }


    private void inotData() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                LogUtil.debugLog(TAG, "onReceive:" + action);
                if (action.equals(Constant.ADD_DEVICE_SUCCESS_ACTION)) {
//                    refreshButtonClicked();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ADD_DEVICE_SUCCESS_ACTION);
        getActivity().registerReceiver(mReceiver, filter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getActivity().unregisterReceiver(mReceiver);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCameraItemClick(EZDeviceInfo deviceInfo, int camera_index) {

        EZCameraInfo cameraInfo = null;
        Intent intent = null;
        switch (mClickType) {
            case TAG_CLICK_PLAY:
//              String pwd = DESHelper.decryptWithBase64("EyCs73n8m5s=", Utils.getAndroidID(this));
                cameraInfo = EZUtils.getCameraInfoFromDevice(deviceInfo, camera_index);
                if (cameraInfo == null) {
                    return;
                }
                intent = new Intent(getActivity(), MapVideoActivity.class);
                intent.putExtra("deviceSerial",deviceInfo.getDeviceSerial());
                intent.putExtra("deviceName",deviceInfo.getDeviceName());
                intent.putExtra("cameraNo", camera_index);
                intent.putExtra("position",deviceInfo.getDeviceName());
                intent.putExtra("status",deviceInfo.getStatus());
                startActivity(intent);
//                intent.putExtra(IntentConsts.EXTRA_CAMERA_INFO, cameraInfo);
//                intent.putExtra(IntentConsts.EXTRA_DEVICE_INFO, deviceInfo);
//                startActivityForResult(intent, REQUEST_CODE);

                break;
            case TAG_CLICK_REMOTE_PLAY_BACK:
                cameraInfo = EZUtils.getCameraInfoFromDevice(deviceInfo, camera_index);
                if (cameraInfo == null) {
                    return;
                }
//                intent = new Intent(EZCameraListActivity.this, PlayBackListActivity.class);
//                intent.putExtra(RemoteListContant.QUERY_DATE_INTENT_KEY, DateTimeUtil.getNow());
//                intent.putExtra(IntentConsts.EXTRA_CAMERA_INFO, cameraInfo);
//                startActivity(intent);
                break;
            default:
                break;
        }


    }

    /**
     * 从服务器获取最新事件消息
     */
    private void getCameraInfoList(boolean headerOrFooter) {

        if (getActivity().isFinishing()) {
            return;
        }
        new GetCamersInfoListTask(headerOrFooter).execute();
    }


    /**
     * 获取事件消息任务
     */
    private class GetCamersInfoListTask extends AsyncTask<Void, Void, List<EZDeviceInfo>> {
        private boolean mHeaderOrFooter;
        private int mErrorCode = 0;

        public GetCamersInfoListTask(boolean headerOrFooter) {
            mHeaderOrFooter = headerOrFooter;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mListView.setFooterRefreshEnabled(true);
//            if (mHeaderOrFooter) {
//                mListView.setVisibility(View.VISIBLE);
//                mNoCameraTipLy.setVisibility(View.GONE);
//                mGetCameraFailTipLy.setVisibility(View.GONE);
//            }
//            mListView.getRefreshableView().removeFooterView(mNoMoreView);
        }

        @Override
        protected List<EZDeviceInfo> doInBackground(Void... params) {
            if (getActivity().isFinishing()) {
                return null;
            }
            if (!ConnectionDetector.isNetworkAvailable(getActivity())) {
                mErrorCode = ErrorCode.ERROR_WEB_NET_EXCEPTION;
                return null;
            }

            try {
                List<EZDeviceInfo> result = null;
                if (mLoadType == LOAD_MY_DEVICE) {
                    if (mHeaderOrFooter) {
                        result = getOpenSDK().getDeviceList(0, 20);
                    } else {
                        result = getOpenSDK().getDeviceList((mAdapter.getCount() / 20) + (mAdapter.getCount() % 20 > 0 ? 1 : 0), 20);
                    }
                } else if (mLoadType == LOAD_SHARE_DEVICE) {
                    if (mHeaderOrFooter) {
                        result = getOpenSDK().getSharedDeviceList(0, 20);
                    } else {
                        result = getOpenSDK().getSharedDeviceList((mAdapter.getCount() / 20) + (mAdapter.getCount() % 20 > 0 ? 1 : 0), 20);
                    }
                }

                return result;

            } catch (BaseException e) {
                ErrorInfo errorInfo = (ErrorInfo) e.getObject();
                mErrorCode = errorInfo.errorCode;
                LogUtil.debugLog(TAG, errorInfo.toString());

                return null;
            }
        }

        @Override
        protected void onPostExecute(List<EZDeviceInfo> result) {
            super.onPostExecute(result);
//            mListView.onRefreshComplete();
            pullRefreshGrid.onRefreshComplete();
            if (getActivity().isFinishing()) {
                return;
            }

            Log.e("abc", "onPostExecute======>"+result.size() );
            if (result != null) {
//                if (mHeaderOrFooter) {
//                    CharSequence dateText = DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date());
//                    for (LoadingLayout layout : mListView.getLoadingLayoutProxy(true, false).getLayouts()) {
//                        ((PullToRefreshHeader) layout).setLastRefreshTime(":" + dateText);
//                    }
                mAdapter.clearItem();
//                }
                if (mAdapter.getCount() == 0 && result.size() == 0) {
//                    mListView.setVisibility(View.GONE);
//                    mNoCameraTipLy.setVisibility(View.VISIBLE);
//                    mGetCameraFailTipLy.setVisibility(View.GONE);
//                    mListView.getRefreshableView().removeFooterView(mNoMoreView);
                    pullRefreshGrid.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

                } else if (result.size() < 10) {
//                    mListView.setFooterRefreshEnabled(false);
//                    mListView.getRefreshableView().addFooterView(mNoMoreView);
                    pullRefreshGrid.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

                } else if (mHeaderOrFooter) {
//                    mListView.setFooterRefreshEnabled(true);
//                    mListView.getRefreshableView().removeFooterView(mNoMoreView);
                }
                addCameraList(result);
                mAdapter.notifyDataSetChanged();
            }

            if (mErrorCode != 0) {
                onError(mErrorCode);
            }
        }

        protected void onError(int errorCode) {
            switch (errorCode) {
                case ErrorCode.ERROR_WEB_SESSION_ERROR:
                case ErrorCode.ERROR_WEB_SESSION_EXPIRE:
                    ActivityUtils.handleSessionException(getActivity());
                    break;
                default:
                    if (mAdapter.getCount() == 0) {
//                        mListView.setVisibility(View.GONE);
//                        mNoCameraTipLy.setVisibility(View.GONE);
//                        mCameraFailTipTv.setText(Utils.getErrorTip(EZCameraListActivity.this, R.string.get_camera_list_fail, errorCode));
//                        mGetCameraFailTipLy.setVisibility(View.VISIBLE);
                    } else {
                        Utils.showToast(getActivity(), R.string.get_camera_list_fail, errorCode);
                    }
                    break;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.shutDownExecutorService();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (bIsFromSetting || (mAdapter != null && mAdapter.getCount() == 0)) {
            getCameraInfoList(true);
            bIsFromSetting = false;
        }
    }

    private void addCameraList(List<EZDeviceInfo> result) {
        int count = result.size();
        EZDeviceInfo item = null;
        for (int i = 0; i < count; i++) {
            item = result.get(i);
            EZDeviceAddLatAndLangInfo info = new EZDeviceAddLatAndLangInfo();
            info.setDeviceSerical(item);
            info.setLat(Double.valueOf("36.69" + i));
            info.setLag(Double.valueOf("117.07" + i));
            infos.add(info);

            mAdapter.addItem(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_CODE) {
            if (requestCode == REQUEST_CODE) {
                String deviceSerial = intent.getStringExtra(IntentConsts.EXTRA_DEVICE_ID);
                int cameraNo = intent.getIntExtra(IntentConsts.EXTRA_CAMERA_NO, -1);
                int videoLevel = intent.getIntExtra("video_level", -1);
                if (TextUtils.isEmpty(deviceSerial)) {
                    return;
                }
                if (videoLevel == -1 || cameraNo == -1) {
                    return;
                }
                if (mAdapter.getDeviceInfoList() != null) {
                    for (EZDeviceInfo deviceInfo : mAdapter.getDeviceInfoList()) {
                        if (deviceInfo.getDeviceSerial().equals(deviceSerial)) {
                            if (deviceInfo.getCameraInfoList() != null) {
                                for (EZCameraInfo cameraInfo : deviceInfo.getCameraInfoList()) {
                                    if (cameraInfo.getCameraNo() == cameraNo) {
                                        cameraInfo.setVideoLevel(videoLevel);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}
