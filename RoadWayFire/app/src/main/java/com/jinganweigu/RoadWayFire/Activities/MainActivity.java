package com.jinganweigu.RoadWayFire.Activities;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseActivity;
import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseFragment2;
import com.jinganweigu.RoadWayFire.BeseClassUtils.MyApplication;
import com.jinganweigu.RoadWayFire.Entries.AllMessageCount;
import com.jinganweigu.RoadWayFire.Entries.EZUIkitLogin;
import com.jinganweigu.RoadWayFire.Fragments.ContactsFragment;
import com.jinganweigu.RoadWayFire.Fragments.MapFragment;
import com.jinganweigu.RoadWayFire.Fragments.MessageFragment;
import com.jinganweigu.RoadWayFire.Fragments.PersonFragment;
import com.jinganweigu.RoadWayFire.Fragments.VideoFragment;
import com.jinganweigu.RoadWayFire.Interfaces.Mycontants;
import com.jinganweigu.RoadWayFire.Interfaces.Url;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.Sptools;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.ToastUtil;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.HttpTool;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.SMObjectCallBack;
import com.jinganweigu.RoadWayFire.ToolsUtils.widget.BadgeRadioButton;
import com.jinganweigu.RoadWayFire.service.DownloadService;
import com.jinganweigu.RoadWayFire.service.GetuiMessageService;
import com.jinganweigu.RoadWayFire.service.GetuipushService;
import com.lidroid.xutils.http.RequestParams;
import com.videogo.openapi.EZOpenSDK;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends BaseActivity {

    /**
     * 通过apk_url 下载apk，在downloadservice中设置apk名字
     */

    private static final String APK_URL = "http://www.fis119.com/app/OutGoingSystem.apk";
    public static final String REFURSH_MESSAGE_COUNT = "com.jinganweigu.roadwayfire.refursh";

    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.rg_main)
    RadioGroup rgMain;
    @BindView(R.id.rb_map)
    BadgeRadioButton rbMap;
    @BindView(R.id.rb_video)
    BadgeRadioButton rbVideo;
    @BindView(R.id.rb_message)
    BadgeRadioButton rbMessage;
    @BindView(R.id.rb_contacts)
    BadgeRadioButton rbContacts;
    @BindView(R.id.rb_person)
    BadgeRadioButton rbPerson;


    private String updateMsg = "有最新的软件包，请下载！";
    private DownloadService.DownloadBinder mDownloadBinder;
    private Disposable mDisposable;//可以取消观察者
    private ProgressBar mProgress;
    private Dialog noticeDialog;// 提示有软件更新的对话框
    private Dialog downloadDialog;// 下载对话框
    //记录用户首次点击返回键的时间
    private long firstTime = 0;
    //装fragment的实例集合
    private ArrayList<BaseFragment2> fragments;
    private int position = 0;
    //缓存Fragment或上次显示的Fragment
    private Fragment tempFragment;

    private String appKey;
    private String appSecret;
    private String username;

    private OngetUndealPushDataMessage UndealPushDataMessage;
    private OngetdealPushDataMessage getDealMessage;


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
    private String token;
    private String rece_user;


    @Override
    public void initViews() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        PushManager.getInstance().initialize(this.getApplicationContext(), GetuipushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), GetuiMessageService.class);
        username = Sptools.getString(this, Mycontants.USER_NAME, "");
        appKey = Sptools.getString(this, Mycontants.EZUIkit_APPKEY, "");
        appSecret = Sptools.getString(this, Mycontants.EZUIkit_SECRET, "");
        IntentFilter filter = new IntentFilter();
        filter.addAction(REFURSH_MESSAGE_COUNT);
        registerReceiver(receiver, filter);


        //初始化Fragment
        initFragment();
        //设置RadioGroup的监听
        initListener();

//       rbMessage.setBadgeNumber(0);
//
//       rbMessage.setBadgeTextSize(-1);

    }

    @Override
    public void initData() {
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent, mConnection, BIND_AUTO_CREATE);//绑定服务
    }

    @Override
    public void initEvents() {

        /**
         * 在这实现 showNoticeDialog 方法，然后下载apk
         */
//        showNoticeDialog();

        if (TextUtils.isEmpty(appKey)) {

            ToastUtil.showToast("萤石APPKEY为空", this);

        } else if (TextUtils.isEmpty(appSecret)) {

            ToastUtil.showToast("萤石APPSECRET为空", this);

        } else {


            getData(appKey, appSecret);

        }

        if (!TextUtils.isEmpty(username)) {

            boolean bind = PushManager.getInstance().bindAlias(this, username);

            if (bind) {

//                ToastUtil.showToast("绑定成功", this);

            } else {

//                ToastUtil.showToast("绑定失败", this);

            }

        }

    }

    private void getData(String appKey, String appSecret) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("appKey", appKey);
        params.addBodyParameter("appSecret", appSecret);

        HttpTool.getInstance(this).httpWithParams(Url.EZUIkit_LOGIN, params, new SMObjectCallBack<EZUIkitLogin>() {

            @Override
            public void onSuccess(EZUIkitLogin ezuIkitLogin) {

                if (ezuIkitLogin.getCode().equals("200")) {

                    String accessToken = ezuIkitLogin.getData().getAccessToken();
                    Sptools.putString(MainActivity.this, Mycontants.EZUIkit_ACCESS_TOKEN, accessToken);
                    EZOpenSDK.getInstance().setAccessToken(accessToken);

                } else {

                    ToastUtil.showToast("视频注册失败", MainActivity.this);

                }


            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }


    private void initListener() {
        rgMain.check(R.id.rb_message);
        switchFragment(tempFragment, getFragment(2));
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_map: //地图
                        position = 0;
                        break;
                    case R.id.rb_video: //视频
                        position = 1;
                        break;
                    case R.id.rb_message: //消息
                        position = 2;
                        break;
                    case R.id.rb_contacts: //通讯录
                        position = 3;
                        break;
                    case R.id.rb_person: //个人中心
                        position = 4;
                        break;
                    default:
                        position = 0;
                        break;
                }
                //根据位置得到相应的Fragment
                BaseFragment2 baseFragment = getFragment(position);
                /**
                 * 第一个参数: 上次显示的Fragment
                 * 第二个参数: 当前正要显示的Fragment
                 */
                switchFragment(tempFragment, baseFragment);
            }
        });
    }

    /**
     * 添加的时候按照顺序
     */
    private void initFragment() {
        fragments = new ArrayList<>();
        fragments.add(new MapFragment());
        fragments.add(new VideoFragment());
        fragments.add(new MessageFragment());
        fragments.add(new ContactsFragment());
        fragments.add(new PersonFragment());
    }

    /**
     * 根据位置得到对应的 Fragment
     *
     * @param position
     * @return
     */
    private BaseFragment2 getFragment(int position) {
        if (fragments != null && fragments.size() > 0) {
            BaseFragment2 baseFragment = fragments.get(position);
            return baseFragment;
        }
        return null;
    }

    /**
     * 切换Fragment
     *
     * @param fragment
     * @param nextFragment
     */
    private void switchFragment(Fragment fragment, BaseFragment2 nextFragment) {
        if (tempFragment != nextFragment) {
            tempFragment = nextFragment;
            if (nextFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //判断nextFragment是否添加成功
                if (!nextFragment.isAdded()) {
                    //隐藏当前的Fragment
                    if (fragment != null) {
                        transaction.hide(fragment);
                    }
                    //添加Fragment
                    transaction.add(R.id.frameLayout, nextFragment).commit();
                } else {
                    //隐藏当前Fragment
                    if (fragment != null) {
                        transaction.hide(fragment);
                    }
                    transaction.show(nextFragment).commit();
                }
            }
        }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
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

    /**
     * 版本更新dialog
     */
    private void showNoticeDialog() {//提示该更新的dialog

        AlertDialog.Builder builder = new AlertDialog.Builder(
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


    protected void showDownloadDialog() {//点击下载 apk的dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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

    Uri sound=Uri.parse("android.resource://com.jinganweigu.RoadWayFire" + "/" + R.raw.alarm );
    private void setnotification(Context context, String title, String contants, Intent intent, int id) {

        /**
         *  创建通知栏管理工具
         */

        NotificationManager notificationManager = (NotificationManager) getSystemService
                (NOTIFICATION_SERVICE);

        /**
         *  实例化通知栏构造器
         */

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        /**
         *  设置Builder
         */
        //设置标题
        mBuilder.setContentTitle(title)
                //设置内容
                .setContentText(contants)
                //设置大图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo))
                //设置小图标
                .setSmallIcon(R.mipmap.logo)
                //设置通知时间
                .setWhen(System.currentTimeMillis())
                //首次进入时显示效果
                .setTicker("我是测试内容")
                //设置通知方式，声音，震动，呼吸灯等效果，这里通知方式为声音
//                .setDefaults(Notification.DEFAULT_SOUND)
                .setSound(sound)
                .setContentIntent(PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        //发送通知请求
        notificationManager.notify(10, mBuilder.build());


    }



//    public void createNotification(String Quantity, MediaPlayer player) {
////      创建通知栏
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        int icon = R.mipmap.logo;
//        long when = System.currentTimeMillis();
////		创建通知栏的显示
//        CharSequence tickerText = "未读消息提醒";
//        Notification notification = new Notification(icon, tickerText, when);
//        notification.defaults |= Notification.DEFAULT_LIGHTS;  // 通知灯光
//        notification.defaults |= Notification.DEFAULT_VIBRATE; // 震动
//        notification.flags |= Notification.FLAG_NO_CLEAR;   // 通知不可以清除
////		notification.flags = Notification.FLAG_AUTO_CANCEL;  // 通知可以清除
////		notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); // 系统默认铃声
////		notification.sound = Uri.parse("file:///sdcard/notification/ringer.mp3");// 播放自定义的铃声
////		notification.flags |= Notification.FLAG_INSISTENT; // 声音一直响到用户相应，就是通知会一直响起，直到你触碰通知栏的时间就会停止
////		创建后在状态栏中通知的内容
//        Context context = getApplicationContext();
//        CharSequence contentTitle = "未读消息提醒";
//        CharSequence contentText = "您有" + Quantity + "条未读消息,请及时读取。";
////		点击后打开的项目   创建一个Intent
//        Intent notificationIntent = new Intent(context, MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
////        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
//        notification.setContentIntent(PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT));
//        notificationManager.notify(NOTIFICATION_ID, notification);
//        try {
//            if (Integer.parseInt(Quantity) == 0 && player.isPlaying()) {
//                player.reset();  // 到初始化状态，这里需要判断是否正在响铃，如果直接在开启一次会出现2个铃声一直循环响起，您不信可以尝试
//            } else if (!player.isPlaying()) {
//                NotificationUtil.ring(player);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//    }

//    /**
//     * 一直响铃
//     * @param player
//     * @return
//     * @throws Exception
//     * @throws IOException
//     */
//    private  MediaPlayer ring(MediaPlayer player) throws Exception, IOException {
//        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
////		MediaPlayer player = new MediaPlayer();
//        player.setDataSource(getApplicationContext(), alert);
//        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        if (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) != 0) {
//            player.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
//            player.setLooping(true);
//            player.prepare();
//            player.start();
//        }
//        return player;
//    }














    @Override
    protected void onResume() {
        super.onResume();
        token = Sptools.getString(this, Mycontants.API_TOKEN, "");
        rece_user = Sptools.getString(this, Mycontants.USER_ID, "");

        if (TextUtils.isEmpty(token)) {
            ToastUtil.showToast("TOKEN错误", this);
        } else if (TextUtils.isEmpty(rece_user)) {
            ToastUtil.showToast("用户ID错误", this);
        } else {
            getAllMessageCount(token, rece_user);
        }

    }

    @Override
    protected void onDestroy() {
        if (mDisposable != null) {
            //取消监听
            mDisposable.dispose();
        }

        if (mDownloadBinder != null) {

            unbindService(mConnection);

        }

        if (receiver != null) {

            unregisterReceiver(receiver);

        }

        super.onDestroy();
    }


    /**
     * 第一种解决办法 通过监听keyDown
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                MyApplication.getInstance().exit();
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    //获取所有未读消息数量
    private void getAllMessageCount(String api_token, String rece_user) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("rece_user", rece_user);
        params.addBodyParameter("api_token", api_token);

        HttpTool.getInstance(this).httpWithParams(Url.GET_ALRAM_MESSAGE_ALL_COUNT, params, new SMObjectCallBack<AllMessageCount>() {

            @Override
            public void onSuccess(AllMessageCount allMessageCount) {


                if (allMessageCount.getCode() == 200) {

                    int count = allMessageCount.getResult().getCount();

                    if (Integer.valueOf(count) > 0) {

//                        rbMessage.setBadgeNumber(Integer.valueOf(count));

                    } else {
//                        rbMessage.setBadgeTextSize(0);
                    }
                }

            }

            @Override
            public void onError(int error, String msg) {

            }
        });

    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String msg = intent.getStringExtra("refrush");

            if (msg.equals("yes")) {

                getAllMessageCount(token, rece_user);

            } else if (msg.equals("finished")) {

                finish();

            } else {

                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                // 当用户点击通知栏的Notification时候，切换回MainActivity。


                setnotification(MainActivity.this, "您有一条报警信息", msg, intent1, 10);
//                getAllMessageCount(token, rece_user);

                UndealPushDataMessage.OnUndealDataReceiver(msg);
                getDealMessage.OnDealDataReceiver(msg);


            }

        }
    };


    public void setOnReceicerUndealMessage(OngetUndealPushDataMessage UndealMessager) {

        UndealPushDataMessage = UndealMessager;

    }

    public void setOnReceicerDealMessage(OngetdealPushDataMessage dealMessager) {

        getDealMessage = dealMessager;

    }


    public interface OngetUndealPushDataMessage {

        void OnUndealDataReceiver(String data);


    }

    public interface OngetdealPushDataMessage {

        void OnDealDataReceiver(String data);


    }


}
