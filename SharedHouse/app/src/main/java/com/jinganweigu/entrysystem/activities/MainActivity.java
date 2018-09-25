package com.jinganweigu.entrysystem.activities;


import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.common.BaseActivity;
import com.jinganweigu.entrysystem.entry.VersionBean;
import com.jinganweigu.entrysystem.fragment.EnterFragment;
import com.jinganweigu.entrysystem.fragment.OuthouseFragment;
import com.jinganweigu.entrysystem.fragment.ReturnFragment;
import com.jinganweigu.entrysystem.fragment.StatisticsFrament;
import com.jinganweigu.entrysystem.service.DownloadService;
import com.jinganweigu.entrysystem.utils.HttpCode;
import com.jinganweigu.entrysystem.utils.MyApplication;
import com.jinganweigu.entrysystem.utils.UpdateManager;
import com.jinganweigu.entrysystem.utils.Url;
import com.jinganweigu.entrysystem.utils.http.HttpTool;
import com.jinganweigu.entrysystem.utils.http.SMObjectCallBack;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends BaseActivity {


    @BindView(R.id.main_fragment)
    FrameLayout mainFragment;
    @BindView(R.id.ll_enter)
    LinearLayout ll_enter;
    @BindView(R.id.ll_outhouse)
    LinearLayout ll_outhouse;
    @BindView(R.id.ll_return)
    LinearLayout ll_return;
    @BindView(R.id.ll_statistics)
    LinearLayout ll_statistics;
    private UpdateManager manager;
    private int oldVersion = 0;


    private FragmentManager mFragmentManager;
    private EnterFragment enterFragment;
    private OuthouseFragment outhouseFragment;
    private ReturnFragment returnFragment;
    private StatisticsFrament statisticsFrament;
    private static final String APK_URL = "http://www.fis119.com/app/OutGoingSystem.apk";
    private String updateMsg = "有最新的软件包，请下载！";
    private DownloadService.DownloadBinder mDownloadBinder;
    private Disposable mDisposable;//可以取消观察者
    private ProgressBar mProgress;
    private Dialog noticeDialog;// 提示有软件更新的对话框
    private Dialog downloadDialog;// 下载对话框


    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mDownloadBinder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mDownloadBinder = null;
        }
    };

    @Override
    public void initViews() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary), true);
        mFragmentManager = getSupportFragmentManager();

        setTabSelection(0);

        manager = new UpdateManager(this);

        oldVersion = UpdateManager.getVerCode(this);

        if (oldVersion != 0) {

            getVersionCode();

        }


    }


    @Override
    public void initData() {

        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent, mConnection, BIND_AUTO_CREATE);//绑定服务


    }

    @Override
    public void initEvents() {

    }


    //开始监听进度
    private void startCheckProgress(long downloadId) {
        Observable
                .interval(100, 200, TimeUnit.MILLISECONDS, Schedulers.io())//无限轮询,准备查询进度,在io线程执行
                .filter(times -> mDownloadBinder != null)
                .map(i -> mDownloadBinder.getProgress(downloadId))//获得下载进度
                .takeUntil(progress -> progress >= 100)//返回true就停止了,当进度>=100就是下载完成了
                .distinct()//去重复
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressObserver());
    }


    //观察者
    private class ProgressObserver implements Observer<Integer> {

        @Override
        public void onSubscribe(Disposable d) {
            mDisposable = d;
        }

        @Override
        public void onNext(Integer progress) {
            mProgress.setProgress(progress);//设置进度
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
            Toast.makeText(MainActivity.this, "出错", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete() {
            mProgress.setProgress(100);
            Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
        }
    }


    protected void showDownloadDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        builder.setTitle("软件版本更新");
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.progress, null);

        mProgress = (ProgressBar) v.findViewById(R.id.progress);
        builder.setView(v);// 设置对话框的内容为一个View
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                interceptFlag = true;
            }
        });
        downloadDialog = builder.create();
        downloadDialog.show();
//        downloadApk();
    }

    private void showNoticeDialog() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
                mContext);// Builder，可以通过此builder设置改变AleartDialog的默认的主题样式及属性相关信息
        builder.setTitle("软件版本更新");
        builder.setMessage(updateMsg);
        builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();// 当取消对话框后进行操作一定的代码？取消对话框
                showDownloadDialog();
                long downloadId = mDownloadBinder.startDownload(APK_URL);
                startCheckProgress(downloadId);

            }
        });
        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
    }


    @Override
    protected void onDestroy() {
        if (mDisposable != null) {
            //取消监听
            mDisposable.dispose();
        }
        super.onDestroy();
    }


    @OnClick({R.id.ll_enter, R.id.ll_outhouse, R.id.ll_return, R.id.ll_statistics})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_enter:

                setTabSelection(0);

                break;
            case R.id.ll_outhouse:

                setTabSelection(1);

                break;
            case R.id.ll_return:

                setTabSelection(2);

                break;

            case R.id.ll_statistics:

                setTabSelection(3);

                break;
        }
    }


    public void setTabSelection(int i) {
        resetBtn();
        // 开启一个Fragment事务
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (i) {
            case 0:
                ll_enter.setSelected(true);
                if (enterFragment == null) {
                    enterFragment = new EnterFragment();
                    transaction.add(R.id.main_fragment, enterFragment);
                } else {
                    transaction.show(enterFragment);
                }

                break;
            case 1:
                ll_outhouse.setSelected(true);
                if (outhouseFragment == null) {
                    outhouseFragment = new OuthouseFragment();
                    transaction.add(R.id.main_fragment, outhouseFragment);
                } else {
                    transaction.show(outhouseFragment);
                }
                break;
            case 2:
                ll_return.setSelected(true);
                if (returnFragment == null) {
                    returnFragment = new ReturnFragment();
                    transaction.add(R.id.main_fragment, returnFragment);
                } else {
                    transaction.show(returnFragment);
                }
                break;

            case 3:
                ll_statistics.setSelected(true);
                if (statisticsFrament == null) {
                    statisticsFrament = new StatisticsFrament();
                    transaction.add(R.id.main_fragment, statisticsFrament);
                } else {
                    transaction.show(statisticsFrament);
                }

                break;

            default:
                break;
        }
        transaction.commitAllowingStateLoss();

    }

    private void hideFragments(FragmentTransaction transaction) {
        if (enterFragment != null) {//入库
            transaction.hide(enterFragment);//隐藏
        }
        if (outhouseFragment != null) {//出库
            transaction.hide(outhouseFragment);
        }
        if (returnFragment != null) {//退货
            transaction.hide(returnFragment);
        }

        if (statisticsFrament != null) {//统计
            transaction.hide(statisticsFrament);
        }
    }

    private void resetBtn() {
        ll_enter.setSelected(false);
        ll_outhouse.setSelected(false);
        ll_return.setSelected(false);
        ll_statistics.setSelected(false);
    }

    private void getVersionCode() {

        HttpTool.getInstance(this).http(Url.GET_UPDATE_VERSION, new SMObjectCallBack<VersionBean>() {
            @Override
            public void onSuccess(VersionBean versionBean) {

                if (versionBean.getCode() == HttpCode.REQUEST_SUCCESS) {

                    if (versionBean.getResult() > oldVersion) {

                        showNoticeDialog();

                    }


                }

            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }
}
