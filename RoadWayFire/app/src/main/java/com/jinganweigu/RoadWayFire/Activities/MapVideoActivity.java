package com.jinganweigu.RoadWayFire.Activities;


import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseActivity;
import com.jinganweigu.RoadWayFire.BeseClassUtils.MyApplication;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.EZUIkitUtils.AudioPlayUtil;
import com.jinganweigu.RoadWayFire.ToolsUtils.EZUIkitUtils.EZUtils;
import com.jinganweigu.RoadWayFire.ToolsUtils.EZUIkitUtils.ScreenOrientationHelper;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.ToastUtil;
import com.videogo.constant.Constant;
import com.videogo.exception.InnerException;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZPlayer;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.videogo.realplay.RealPlayStatus;
import com.videogo.util.ConnectionDetector;
import com.videogo.util.LocalInfo;
import com.videogo.util.MediaScanner;
import com.videogo.util.SDCardUtil;
import com.videogo.util.Utils;
import com.videogo.widget.CheckTextButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jinganweigu.RoadWayFire.Activities.EZRealPlayActivity.MSG_SET_VEDIOMODE_SUCCESS;

public class MapVideoActivity extends BaseActivity implements Handler.Callback, SurfaceHolder.Callback {

    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.tv_Janurary)
    TextView tvJanurary;
    @BindView(R.id.tv_February)
    TextView tvFebruary;
    @BindView(R.id.tv_March)
    TextView tvMarch;
    @BindView(R.id.tv_April)
    TextView tvApril;
    @BindView(R.id.tv_May)
    TextView tvMay;
    @BindView(R.id.tv_June)
    TextView tvJune;
    @BindView(R.id.tv_July)
    TextView tvJuly;
    @BindView(R.id.tv_August)
    TextView tvAugust;
    @BindView(R.id.tv_September)
    TextView tvSeptember;
    @BindView(R.id.tv_October)
    TextView tvOctober;
    @BindView(R.id.tv_November)
    TextView tvNovember;
    @BindView(R.id.tv_December)
    TextView tvDecember;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.realplay_sv)
    SurfaceView realplaySv;
    @BindView(R.id.realplay_page_ly)
    LinearLayout realplayPageLy;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    @BindView(R.id.tv_play_code)
    TextView tvPlayCode;
    @BindView(R.id.btn_play)
    ImageButton btnPlay;
    @BindView(R.id.realplay_sound_btn)
    ImageButton realplaySoundBtn;
    @BindView(R.id.btn_get_picture)
    Button btnGetPicture;
    @BindView(R.id.tv_play_speed)
    TextView tvPlaySpeed;
    @BindView(R.id.btn_fullscreen)
    Button btnFullscreen;
    @BindView(R.id.fullscreen_full_button)
    CheckTextButton fullscreenFullButton;
    @BindView(R.id.fl_title)
    FrameLayout flTitle;
    @BindView(R.id.tv_surface_name)
    TextView tvSurfaceName;
    @BindView(R.id.btn_surface_back)
    ImageButton btnSurfaceBack;
    @BindView(R.id.rl_surface_title)
    RelativeLayout rlSurfaceTitle;
    @BindView(R.id.realplay_play_rl)
    RelativeLayout realplayPlayRl;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.tv_state)
    TextView tvState;
    private String deviceSerial, deviceName,position,status;
    private int cameraNo = 0;
    private EZPlayer mEZPlayer;
    private SurfaceHolder mRealPlaySh;
    private List<String> yearList = new ArrayList<>();
    private AudioPlayUtil mAudioPlayUtil = null;
    private boolean isbackPlay = false;

    private int mForceOrientation = 0;//横竖屏状态
    private ScreenOrientationHelper mScreenOrientationHelper;//切换横竖屏工具
    private LocalInfo mLocalInfo = null;
    // 播放比例
    private float mPlayScale = 1;

    private float mRealRatio = Constant.LIVE_VIEW_RATIO;
    /**
     * 屏幕当前方向
     */
    private int mOrientation = Configuration.ORIENTATION_PORTRAIT;
    /**
     * 标识是否正在播放
     */
    private int mStatus = RealPlayStatus.STATUS_STOP;
    private Handler mHandler = new Handler(this);

    private EZDeviceInfo info;


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            //播放成功的回调
            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_SUCCESS:

                pbProgress.setVisibility(View.GONE);

//                startUpdateTimer();
                tvPlayCode.setVisibility(View.GONE);
                break;

            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_FAIL:

                pbProgress.setVisibility(View.GONE);

                tvPlayCode.setVisibility(View.VISIBLE);

                break;
            case MSG_SET_VEDIOMODE_SUCCESS:
//                closeQualityPopupWindow();
//                setVideoLevel();
                if (mStatus == RealPlayStatus.STATUS_START) {
                    // 停止播放
                    stopRealPlay();
                    //下面语句防止stopRealPlay线程还没释放surface, startRealPlay线程已经开始使用surface
                    //因此需要等待500ms
                    SystemClock.sleep(5000);
                    // 开始播放
                    startRealPlay(deviceSerial, cameraNo);
                    tvPlayCode.setVisibility(View.GONE);
                }
                break;

        }
        return false;
    }


    @Override
    public void initViews() {
        setContentView(R.layout.activity_map_video);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        tvYear.setTextColor(Color.parseColor("#5a95fd"));
        deviceSerial = getIntent().getStringExtra("deviceSerial");
        deviceName = getIntent().getStringExtra("deviceName");
        cameraNo = getIntent().getIntExtra("cameraNo", 0);
        position=getIntent().getStringExtra("position");
        status=getIntent().getStringExtra("status");
//        setProgressDrawable(pbProgress,R.drawable.common_refresh);


        mScreenOrientationHelper = new ScreenOrientationHelper(this, fullscreenFullButton);

        if (!TextUtils.isEmpty(deviceName)) {

            topName.setText(deviceName);
        }
        mRealPlaySh = realplaySv.getHolder();
        mRealPlaySh.addCallback(this);
        mAudioPlayUtil = AudioPlayUtil.getInstance(getApplication());
    }

    @Override
    public void initData() {

        int year = Calendar.getInstance().get(Calendar.YEAR);

        tvYear.setText(year + "年");

        for (int i = year - 15; i <= year; i++) {

            yearList.add(i + "年");

        }

        if(!TextUtils.isEmpty(position)){

            tvPosition.setText("位置："+position);

        }

        if(!TextUtils.isEmpty(status)){


            tvState.setText("状态："+status);

        }else{

            tvState.setText("状态：正常");


        }
    }

    @Override
    public void initEvents() {

        mLocalInfo = LocalInfo.getInstance();
        // 获取屏幕参数
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mLocalInfo.setScreenWidthHeight(metric.widthPixels, metric.heightPixels);
        mLocalInfo.setNavigationBarHeight((int) Math.ceil(25 * getResources().getDisplayMetrics().density));

        btnFullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
//                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
                flTitle.setVisibility(View.GONE);
                ViewGroup.LayoutParams lp = realplaySv.getLayoutParams();
                lp.height = LinearLayout.LayoutParams.MATCH_PARENT;
                lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
                realplaySv.setLayoutParams(lp);
                isbackPlay = true;
                setForceOrientation(ActivityInfo.CONFIG_ORIENTATION);


            }
        });

        realplaySv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (rlSurfaceTitle.getVisibility() == View.GONE) {

                    rlSurfaceTitle.setVisibility(View.VISIBLE);

                } else {

                    rlSurfaceTitle.setVisibility(View.GONE);

                }

                return false;
            }
        });


        btnSurfaceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MapVideoActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                flTitle.setVisibility(View.GONE);
                ViewGroup.LayoutParams lp = realplaySv.getLayoutParams();
                lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
                lp.height = 700;
                realplaySv.setLayoutParams(lp);
                isbackPlay = false;
                setForceOrientation(0);
                rlSurfaceTitle.setVisibility(View.GONE);
                flTitle.setVisibility(View.VISIBLE);
            }
        });

        btnGetPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                onCapturePicBtnClick();

            }
        });


    }


    //设置全屏后状态栏和标题栏的显示问题

    //设置全屏后状态栏和标题栏的显示问题
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //显示状态栏
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//全部显示出来。
        } else {
            //横屏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏

            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (mStatus != RealPlayStatus.STATUS_STOP) {
            stopRealPlay();
        } else {
            startRealPlay(deviceSerial, cameraNo);
            pbProgress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRealPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mEZPlayer != null) {
            mEZPlayer.release();
        }

    }


    /**
     * 设置横屏
     *
     * @param orientation
     */

    public void setForceOrientation(int orientation) {
        if (mForceOrientation == orientation) {
            return;
        }
        mForceOrientation = orientation;
        if (mForceOrientation != 0) {
            if (mForceOrientation != mOrientation) {
                if (mForceOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    mScreenOrientationHelper.portrait();
                } else {
                    mScreenOrientationHelper.landscape();
                }
            }

            mScreenOrientationHelper.disableSensorOrientation();

        } else {

            updateOrientation();

        }
    }

    private void updateOrientation() {
        if (mStatus == RealPlayStatus.STATUS_PLAY) {
            setOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else {
            if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
                setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                setOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
        }
    }

    private void setOrientation(int sensor) {
        if (mForceOrientation != 0) {
//            LogUtil.debugLog(TAG, "setOrientation mForceOrientation:" + mForceOrientation);
            return;
        }

        if (sensor == ActivityInfo.SCREEN_ORIENTATION_SENSOR)
            mScreenOrientationHelper.enableSensorOrientation();
        else
            mScreenOrientationHelper.disableSensorOrientation();
    }


    private void stopRealPlay() {

        mStatus = RealPlayStatus.STATUS_STOP;
        if (mEZPlayer != null) {
            mEZPlayer.stopRealPlay();
        }
    }

    /**
     * 开始播放
     *
     * @see
     * @since V2.0
     */
    private void startRealPlay(String deviceSerial, int cameraNo) {

        if (mStatus == RealPlayStatus.STATUS_START || mStatus == RealPlayStatus.STATUS_PLAY) {
            return;
        }

        // 检查网络是否可用
        if (!ConnectionDetector.isNetworkAvailable(this)) {
            // 提示没有连接网络
            ToastUtil.showToast("没有连接网络", MapVideoActivity.this);
            return;
        }

        mStatus = RealPlayStatus.STATUS_START;
        if (deviceSerial != null) {
            if (mEZPlayer == null) {
                mEZPlayer = MyApplication.getOpenSDK().createPlayer(deviceSerial, cameraNo);//绑定设备
            }

            mEZPlayer.setHandler(mHandler);
            mEZPlayer.setSurfaceHold(mRealPlaySh);
            mEZPlayer.startRealPlay();
        }
//        updateLoadingProgress(0);
    }


    @OnClick({R.id.tv_year, R.id.back_btn, R.id.tv_Janurary, R.id.tv_February, R.id.tv_March, R.id.tv_April, R.id.tv_May, R.id.tv_June, R.id.tv_July, R.id.tv_August, R.id.tv_September, R.id.tv_October, R.id.tv_November, R.id.tv_December})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:

                finish();

                break;
            case R.id.tv_year:

                initNoLinkOptionsPicker(tvYear);

                break;

            case R.id.tv_Janurary:

                String year1 = tvYear.getText().toString().trim();

                if (!TextUtils.isEmpty(year1) && !TextUtils.isEmpty(deviceSerial)) {

                    String time = year1.substring(0, 4) + "-" + "01";

                    intentMethod.startMethodTwoString(this, PictureActivity.class, "time", time, "cameraNo", deviceSerial);

                }


                break;
            case R.id.tv_February:
                String year2 = tvYear.getText().toString().trim();

                if (!TextUtils.isEmpty(year2) && !TextUtils.isEmpty(deviceSerial)) {

                    String time = year2.substring(0, 4) + "-" + "02";

                    intentMethod.startMethodTwoString(this, PictureActivity.class, "time", time, "cameraNo", deviceSerial);

                }
                break;
            case R.id.tv_March:
                String year3 = tvYear.getText().toString().trim();

                if (!TextUtils.isEmpty(year3) && !TextUtils.isEmpty(deviceSerial)) {

                    String time = year3.substring(0, 4) + "-" + "03";

                    intentMethod.startMethodTwoString(this, PictureActivity.class, "time", time, "cameraNo", deviceSerial);

                }

                break;
            case R.id.tv_April:
                String year4 = tvYear.getText().toString().trim();

                if (!TextUtils.isEmpty(year4) && !TextUtils.isEmpty(deviceSerial)) {

                    String time = year4.substring(0, 4) + "-" + "04";

                    intentMethod.startMethodTwoString(this, PictureActivity.class, "time", time, "cameraNo", deviceSerial);

                }
                break;
            case R.id.tv_May:
                String year5 = tvYear.getText().toString().trim();

                if (!TextUtils.isEmpty(year5) && !TextUtils.isEmpty(deviceSerial)) {

                    String time = year5.substring(0, 4) + "-" + "05";

                    intentMethod.startMethodTwoString(this, PictureActivity.class, "time", time, "cameraNo", deviceSerial);

                }
                break;
            case R.id.tv_June:
                String year6 = tvYear.getText().toString().trim();

                if (!TextUtils.isEmpty(year6) && !TextUtils.isEmpty(deviceSerial)) {

                    String time = year6.substring(0, 4) + "-" + "06";

                    intentMethod.startMethodTwoString(this, PictureActivity.class, "time", time, "cameraNo", deviceSerial);

                }
                break;
            case R.id.tv_July:
                String year7 = tvYear.getText().toString().trim();

                if (!TextUtils.isEmpty(year7) && !TextUtils.isEmpty(deviceSerial)) {

                    String time = year7.substring(0, 4) + "-" + "07";

                    intentMethod.startMethodTwoString(this, PictureActivity.class, "time", time, "cameraNo", deviceSerial);

                }
                break;
            case R.id.tv_August:
                String year8 = tvYear.getText().toString().trim();

                if (!TextUtils.isEmpty(year8) && !TextUtils.isEmpty(deviceSerial)) {

                    String time = year8.substring(0, 4) + "-" + "08";

                    intentMethod.startMethodTwoString(this, PictureActivity.class, "time", time, "cameraNo", deviceSerial);

                }
                break;
            case R.id.tv_September:
                String year9 = tvYear.getText().toString().trim();

                if (!TextUtils.isEmpty(year9) && !TextUtils.isEmpty(deviceSerial)) {

                    String time = year9.substring(0, 4) + "-" + "09";

                    intentMethod.startMethodTwoString(this, PictureActivity.class, "time", time, "cameraNo", deviceSerial);

                }
                break;
            case R.id.tv_October:
                String year10 = tvYear.getText().toString().trim();

                if (!TextUtils.isEmpty(year10) && !TextUtils.isEmpty(deviceSerial)) {

                    String time = year10.substring(0, 4) + "-" + "10";

                    intentMethod.startMethodTwoString(this, PictureActivity.class, "time", time, "cameraNo", deviceSerial);

                }
                break;
            case R.id.tv_November:
                String year11 = tvYear.getText().toString().trim();

                if (!TextUtils.isEmpty(year11) && !TextUtils.isEmpty(deviceSerial)) {

                    String time = year11.substring(0, 4) + "-" + "11";

                    intentMethod.startMethodTwoString(this, PictureActivity.class, "time", time, "cameraNo", deviceSerial);

                }
                break;
            case R.id.tv_December:
                String year12 = tvYear.getText().toString().trim();

                if (!TextUtils.isEmpty(year12) && !TextUtils.isEmpty(deviceSerial)) {

                    String time = year12.substring(0, 4) + "-" + "12";

                    intentMethod.startMethodTwoString(this, PictureActivity.class, "time", time, "cameraNo", deviceSerial);

                }
                break;

        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mEZPlayer != null) {

            mEZPlayer.setSurfaceHold(holder);
        } else {

        }
        mRealPlaySh = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        if (mEZPlayer != null) {
            mEZPlayer.setSurfaceHold(null);
        }
        mRealPlaySh = null;

    }


    //时间选择器
    private void initNoLinkOptionsPicker(TextView tv) {// 不联动的多级选项
        OptionsPickerView pvNoLinkOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

                tv.setText(yearList.get(options1));

            }
        })
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
//                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
//
//                        Log.e("abc", "onOptionsSelectChanged: ===>"+str );

                    }
                })
                .build();
        pvNoLinkOptions.setPicker(yearList);


        pvNoLinkOptions.show();
    }

    //截图
    private void onCapturePicBtnClick() {

        if (!SDCardUtil.isSDCardUseable()) {
            // 提示SD卡不可用
            Utils.showToast(MapVideoActivity.this, R.string.remoteplayback_SDCard_disable_use);
            return;
        }

        if (SDCardUtil.getSDCardRemainSize() < SDCardUtil.PIC_MIN_MEM_SPACE) {
            // 提示内存不足
            Utils.showToast(MapVideoActivity.this, R.string.remoteplayback_capture_fail_for_memory);
            return;
        }

        if (mEZPlayer != null) {

            Thread thr = new Thread() {
                @Override
                public void run() {
                    Bitmap bmp = mEZPlayer.capturePicture();
                    if (bmp != null) {
                        try {
                            mAudioPlayUtil.playAudioFile(AudioPlayUtil.CAPTURE_SOUND);

                            // 可以采用deviceSerial+时间作为文件命名，demo中简化，只用时间命名
                            Date date = new Date();
                            final String path = Environment.getExternalStorageDirectory().getPath() + "/EZOpenSDK/CapturePicture/" + String.format("%tY", date)
                                    + String.format("%tm", date) + String.format("%td", date) + "/"
                                    + String.format("%tH", date) + String.format("%tM", date) + String.format("%tS", date) + String.format("%tL", date) + ".jpg";

                            if (TextUtils.isEmpty(path)) {
                                bmp.recycle();
                                bmp = null;
                                return;
                            }
                            EZUtils.saveCapturePictrue(path, bmp);
                            MediaScanner mMediaScanner = new MediaScanner(MapVideoActivity.this);
                            mMediaScanner.scanFile(path, "jpg");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MapVideoActivity.this, getResources().getString(R.string.already_saved_to_volume) + path, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (InnerException e) {
                            e.printStackTrace();
                        } finally {
                            if (bmp != null) {
                                bmp.recycle();
                                bmp = null;
                                return;
                            }
                        }
                    }
                    super.run();
                }
            };
            thr.start();
        }
    }

}
