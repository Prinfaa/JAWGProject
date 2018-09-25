package com.jinganweigu.RoadWayFire.Activities;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jinganweigu.RoadWayFire.BeseClassUtils.MyApplication;
import com.jinganweigu.RoadWayFire.R;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZPlayer;

public class VideoActivity extends AppCompatActivity implements Handler.Callback, SurfaceHolder.Callback {

    private SurfaceView mRealPlaySv = null;
    private SurfaceHolder mRealPlaySh = null;
    private EZPlayer mEZPlayer;
    private Handler mHandler = new Handler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        MyApplication.getInstance().addActivity(this);
        mRealPlaySv= (SurfaceView) findViewById(R.id.realplay_sv);
        mRealPlaySh = mRealPlaySv.getHolder();
        mRealPlaySh.addCallback(VideoActivity.this);
        mEZPlayer = MyApplication.getOpenSDK().createPlayer("818226750", 1);//绑定设备
        mEZPlayer.setHandler(mHandler);
        mEZPlayer.setSurfaceHold(mRealPlaySh);
        mEZPlayer.startRealPlay();//开始播放

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
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mEZPlayer != null) {
            mEZPlayer.setSurfaceHold(null);
        }
        mRealPlaySh = null;

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mEZPlayer != null) {
            mEZPlayer.release();
        }

    }

    /*
  视频的回调
   */
    @Override
    public boolean handleMessage(Message msg) {
        //   Log.e("回调","true+zong"+msg);
        switch (msg.what) {
//播放成功的回调
            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_SUCCESS:


                Log.e("abc", "handleMessage:====> 播放成功" );

                break;

        }

        return false;
    }
}
