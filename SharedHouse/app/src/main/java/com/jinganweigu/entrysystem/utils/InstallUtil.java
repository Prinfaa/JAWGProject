package com.jinganweigu.entrysystem.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;

import java.io.File;


/**
 * If there is no bug, then it is created by ChenFengYao on 2017/4/19,
 * otherwise, I do not know who create it either.
 */
public class InstallUtil {
    /**
     * @param context
     * @param apkPath 要安装的APK
     * @param rootMode 是否是Root模式
     */
    public Context muContext;


    public void install(Context context, String apkPath, boolean rootMode) {

        this.muContext = context;

//        if (rootMode){
////            installRoot(context,apkPath);
//        }else {
        installNormal(context, apkPath);
//        }
    }

    /**
     * 通过非Root模式安装
     *
     * @param context
     * @param apkPath
     */
    public void install(Context context, String apkPath) {
        install(context, apkPath, false);
    }

    //普通安装
    @SuppressLint("ObsoleteSdkInt")
    private void installNormal(Context mContext, String apkPath) {


//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        //版本在7.0以上是不能直接通过uri访问的
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
//            File file = (new File(apkPath));
//            // 由于没有在Activity环境下启动Activity,设置下面的标签
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
//            Uri apkUri = FileProvider.getUriForFile(context, "com.jinganweigu.entrysystem.installapk", file);
//            //添加这一句表示对目标应用临时授权该Uri所代表的文件
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
//        } else {
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setDataAndType(Uri.fromFile(new File(apkPath)),
//                    "application/vnd.android.package-archive");
//        }
//        context.startActivity(intent);


        if (null != apkPath) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                File apkFile = new File(apkPath);
                //兼容7.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(mContext, "com.jinganweigu.entrysystem.fileProvider", apkFile);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                    //兼容8.0
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        boolean hasInstallPermission = mContext.getPackageManager().canRequestPackageInstalls();
                        if (!hasInstallPermission) {
//                            ToastUtil.makeText(MyApplication.getContext(), MyApplication.getContext().getString(R.string.string_install_unknow_apk_note), false);
                            ToastUtil.showToast("没有安装权限", mContext);
                            startInstallPermissionSettingActivity();
                            return;
                        }
                    }
                } else {
                    intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                if (mContext.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                    mContext.startActivity(intent);
                }
            } catch (Throwable e) {
                e.printStackTrace();
//                DataEmbeddingUtil.dataEmbeddingAPPUpdate(e.toString());
//                CommonUtils.makeEventToast(MyApplication.getContext(), MyApplication.getContext().getString(R.string.download_hint), false);
            }


        }


    }


    /**
     * 跳转到设置-允许安装未知来源-页面
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        muContext.startActivity(intent);
    }


    //通过Root方式安装
//    private static void installRoot(Context context, String apkPath) {
//        Observable.just(apkPath)
//                .map(mApkPath -> "pm install -r " + mApkPath)
//                .map(SystemManager::RootCommand)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(integer -> {
//                    if (integer == 0) {
//                        Toast.makeText(context, "安装成功", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(context, "root权限获取失败,尝试普通安装", Toast.LENGTH_SHORT).show();
//                        install(context,apkPath);
//                    }
//                });
//    }
}
