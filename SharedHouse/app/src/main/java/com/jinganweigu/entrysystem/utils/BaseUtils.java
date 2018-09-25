package com.jinganweigu.entrysystem.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.activities.LoginActivity;
import com.jinganweigu.entrysystem.utils.http.HttpTool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 公共工具
 *
 * @author WRB @createtime2105/9/26
 */
public class BaseUtils {
    public static int pagesize = 10;
    public static boolean isLogin = false;
    public static String user_id = "";
    public static String contentFlag = "one";
    public static int rebackTag = 0;// 标示是否隐藏底部view
    private static InputMethodManager mImm;
    private static LoadImageUtil.IntentMethod mIntentMethod;
    // public static LinkedList<ChatInfo> infos = new LinkedList<ChatInfo>();


    /**
     * 判断是否是正确手机号
     *^(0|86|17951)?(13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8}$
     * @param mobiles
     * @return
     */
    public static boolean isMobiles(String mobiles) {

//        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。

        Pattern p = Pattern.compile("[1][358]\\d{9}");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }


    /**
     * 拿到正确的url
     * @param homeUrl
     * @param url
     * @return
     */
    public static String makeUrlRight(String homeUrl, String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.contains("http")) {
                return url;
            } else {
                return homeUrl + url;
            }
        }else {

            return "";
        }
    }

    /**
     * 判断手机号是联通 、移动、电信
     *
     * @param mobiles
     * @return
     */
    public static int isYYSMobiles(String mobiles) {
        String gsm = "^((13[4-9])|(147)|(15[0-2,7-9])|(18[2-3,7-8]))\\d{8}$";
        String wcdma = "^((13[0-2])|(145)|(15[5-6])|(186))\\d{8}$";
        String cdma = "^((133)|(153)|(18[0,9]))\\d{8}$";
        int flag = 0;
        if (mobiles.matches(wcdma)) {
            flag = 1;
            System.out.println("联通号码");
        } else if (mobiles.matches(gsm)) {
            flag = 2;
            System.out.println("移动号码");
        } else if (mobiles.matches(cdma)) {
            flag = 3;
            System.out.println("电信号码");
        } else {
            flag = 4;
            System.out.println("号码有误");
        }
        return flag;
    }

    /**
     * 判断是否是正确身份证�?
     *
     * @param
     * @return
     */
    public static boolean isSFZCards(String idcard) {
        Pattern p = Pattern
                .compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])(\\d{4}|\\d{3}[x|X])$");
        Matcher m = p.matcher(idcard);
        return m.matches();
    }

    /**
     * 判断账号是否符合
     *
     * @param account
     * @return
     */
    public static boolean isAccounts(String account) {
        Pattern p = Pattern.compile("^(?![0-9])[a-zA-Z0-9_]{6,8}$");
        Matcher m = p.matcher(account);
        return m.matches();
    }

    /**
     * 只能输入数字
     *
     * @param mobiles
     * @return
     */
    public static boolean onlynumber(String mobiles) {
        Pattern p = Pattern.compile("^[0-9]*$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断是否网络链接正常
     *
     * @param context
     * @return true or false
     * @author: WRB
     * @Createtime: 2015/9/26
     */
    public static boolean isNetworkAvailable(Context context) {
        boolean flag = false;
        ConnectivityManager localConnectivityManager = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (localConnectivityManager != null) {
            try {
                NetworkInfo localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();
                if ((localNetworkInfo == null) || (!localNetworkInfo.isAvailable())) {
                    flag = false;
                } else {
                    flag = true;
                }
            } catch (Exception e) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 判断是否存在SD�?
     *
     * @return true or false
     * @author WRB
     */
    public static boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }


    /**
     * 提取手机号码
     *
     * @param text
     * @return
     */
    public static String cutPhoneNumber(String text) {
        List<Integer> datas = new ArrayList<Integer>();
        String phone = "";
        String str2 = text.replace(" ", "");
        for (int i = 0; i < str2.length() - 1; i++) {
            if ("1".equals(String.valueOf(str2.charAt(i)))) {
                datas.add(i);
            }
        }
        if (datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                int dex = datas.get(i) + 11;
                if (dex <= str2.length()) {
                    String str3 = str2.substring(datas.get(i), dex);
                    if (isMobiles(str3)) {
                        phone = str3;
                    }
                }

            }

        }
        return phone;
    }

    // 二进�?
    public static String getBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray().toString();
    }


    /**
     * 判断是否安装微信
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

    //判断是否安装目标应用
    public static boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName)
                .exists();
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 系统提示音播放
     *
     * @param context
     */
    public static void soundRing(Context context) {

        MediaPlayer mp = new MediaPlayer();
        mp.reset();
        try {
            mp.setDataSource(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            mp.prepare();
            mp.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 判断网络是否连接 true 连接 ；false 断开
     *
     * @param context
     * @return
     */
    public static boolean InternetStatus(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {// unconnect
            // network
            return false;
        } else {// connect network
            return true;
        }
    }


    /**
     * 截取含有多个index的字符串,返回集合
     * @param index
     * @param picture
     * @return
     */
    public static List<String> multiSubString(String index, String picture) {

        List<String> subArr = new ArrayList<String>();

        subStr(index, picture, subArr);

        return subArr;
    }

    private static void subStr(String index, String picture, List<String> subArr) {

        String substring = picture.substring(0, picture.indexOf(index));
        if (!TextUtils.isEmpty(substring)) {
            subArr.add(substring);
        }

        String substring1 = picture.substring(picture.indexOf(index) + 1, picture.length());
        if (!TextUtils.isEmpty(substring1)&&substring1.contains(index)&&substring1.length()>1) {
            subStr(index,substring1,subArr);
        }else if (!TextUtils.isEmpty(substring1)&&substring1.length()>1){
            subArr.add(substring1) ;
        }
    }


    /* 定义一个倒计时的内部类 */
    static class TimeCount extends CountDownTimer {

        TextView textView;

        public TimeCount(long millisInFuture, long countDownInterval, TextView textView) {

            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
            this.textView = textView;
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            textView.setText("重新获取");
            textView.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            textView.setEnabled(false);
            textView.setText(millisUntilFinished / 1000 + "秒");
        }
    }

    /**
     * 获取验证码计时器
     *
     * @param millisInFuture    总时长
     * @param countDownInterval 计时的时间间隔
     * @param textView          验证码按钮
     */
    public static void startCaptureTimeCount(long millisInFuture, long countDownInterval, TextView textView) {

        new TimeCount(millisInFuture, countDownInterval, textView).start();
    }

    /**
     * 设置不能输入空格
     *
     * @param textView 输入控件
     */
    public static void setNotBlankInput(TextView textView) {

        textView.setFilters(new InputFilter[]{filter});

    }

    /**
     * 设置最大输入长度
     *
     * @param textView
     * @param max
     */
    public static void setInputLength(TextView textView, int max) {

        textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max)});

    }

    public static void setInputLenWithNoBlank(TextView textView, int max) {

        textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max), filter});

    }

    /**
     * 可滚动,限制长度,无空格editText
     * @param textView
     * @param max
     */
    public static void setInputLenWithNoBlankScroll(EditText textView, int max) {

        textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max), filter});
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView.setSelection(textView.getText().length(), textView.getText().length());


    }

    private static InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            //返回null表示接收输入的字符,返回空字符串表示不接受输入的字符
            if (source.equals(" "))
                return "";
            else
                return null;
        }
    };


    /**
     * 验证邮箱格式是否正确
     */
    public static boolean emailValidation(String email) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return email.matches(regex);
    }


    /**
     * 四舍五入保留小数点后n位
     *
     * @param d
     * @return
     */
    public static double GetDoubleRound(double d, int n) {

        return new BigDecimal(d).setScale(n, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 四舍五入保留小数点后2位
     *
     * @param d
     * @return
     */
    public static double GetDoubleRoundTwo(double d) {

        return ((double) Math.round(d * 100) / 100.0);
    }

    /**
     * 强制四舍五入保留小数点后2位
     *
     * @param d
     * @return
     */
    public static String ForceSetDoubleRoundTwo(double d) {

//        double money = BaseUtils.GetDoubleRoundTwo(d);
//        String myMoney = formatFloatNumber(money);
//        if (myMoney.length() - myMoney.indexOf(".") == 2) {
//            myMoney = myMoney + "0";
//        }
        DecimalFormat ddf   = new DecimalFormat("######0.00");

        return ddf.format(d);
    }

    /**
     * 当浮点型数据位数超过10位之后，数据变成科学计数法显示。用此方法可以使其正常显示。
     * @param value
     * @return Sting
     */
    public static String formatFloatNumber(double value) {
        if(value != 0.00){
            java.text.DecimalFormat df = new java.text.DecimalFormat("########.00");
            return df.format(value);
        }else{
            return "0.00";
        }

    }

    /**
     * 取消输入框
     *
     * @param context
     * @param windowToken v.getWindowToken()
     */
    public static void hideEditextWindow(Context context, IBinder windowToken) {

        //隐藏输入框
        if (mImm == null) {
            mImm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        }

        mImm.hideSoftInputFromWindow(windowToken, 0);
    }

    //隐藏软键盘
    public static void showSoftKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     * 从路径获取文件名
     *
     * @param pathName
     * @return
     */
    public static String getFileName(String pathName) {
        int start = pathName.lastIndexOf("/");
        int end = pathName.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathName.substring(start + 1, end);
        } else {
            return null;
        }
    }

    /**
     * 确认提示弹出窗
     *
     * @param context
     * @param message
     * @param onConfirm
     */
    public static void showAlertDialog(Context context, String message, final OnConfirm onConfirm) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View
                .inflate(context, R.layout.custom_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView titleAlert = (TextView) view
                .findViewById(R.id.title);//设置标题
        TextView tvMessage = (TextView) view
                .findViewById(R.id.dialog_edit);//输入内容
        TextView cancel = (TextView) view
                .findViewById(R.id.btn_cancel);//取消按钮
        TextView comfirm = (TextView) view
                .findViewById(R.id.btn_confirm);//确定按钮
        titleAlert.setText("退货原因");
        comfirm.setTextColor(context.getResources().getColor(R.color.colorAccent));
        cancel.setTextColor(context.getResources().getColor(R.color.colorAccent));
        tvMessage.setText(message);
        //取消或确定按钮监听事件处理
        final AlertDialog dialog = builder.create();
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onConfirm.onConfirm();
                dialog.dismiss();

            }
        });


    }

    /**
     * 确认提示弹出窗
     *
     * @param context
     * @param message
     */
    public static void showAlertDialog3(Context context, String title, String message) {

        final Dialog mDialog=new Dialog(context,R.style.NoTitle);
//        mDialog.setTitle(null);
        View view = View
                .inflate(context, R.layout.custom_dialog3, null);
        mDialog.setContentView(view);
        TextView titleAlert = (TextView) view
                .findViewById(R.id.title);//设置标题
        titleAlert.setText(title);
        TextView tvMessage = (TextView) view
                .findViewById(R.id.dialog_edit);//输入内容
        TextView comfirm = (TextView) view
                .findViewById(R.id.btn_confirm);//确定按钮
        tvMessage.setText(message);
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();

    }


    /**
     * 确认提示弹出窗
     *
     * @param context
     * @param message
     * @param onConfirm
     */
    public static void showAlertDialog2(Context context, String title, String message, final OnConfirm onConfirm) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View
                .inflate(context, R.layout.custom_dialog2, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView titleAlert = (TextView) view
                .findViewById(R.id.title);//设置标题
        titleAlert.setText(title);
        TextView tvMessage = (TextView) view
                .findViewById(R.id.dialog_edit);//输入内容
        TextView cancel = (TextView) view
                .findViewById(R.id.btn_cancel);//取消按钮
        TextView comfirm = (TextView) view
                .findViewById(R.id.btn_confirm);//确定按钮
        tvMessage.setText(message);
        //取消或确定按钮监听事件处理
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);//取消点击外界消失
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                HttpTool.isDeleteUser = true;
            }
        });
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onConfirm.onConfirm();
                dialog.dismiss();

            }
        });


    }



    public interface OnConfirm {

        void onConfirm();

    }

    //是否登录
    public static void ifLogin(final Context context, OnSuccess onSuccess) {
        boolean ifLogin = Sptools.getBoolean(context, Mycontants.IF_LOGIN, false);
        if (ifLogin) {
            //已登录
            onSuccess.onSuccess();

        } else {
            showAlertDialog(context, "亲，您还未登录，确定登录吗？", new OnConfirm() {
                @Override
                public void onConfirm() {
                    if (mIntentMethod == null) {
                        mIntentMethod = new LoadImageUtil.IntentMethod();
                    }
                    mIntentMethod.startMethodTwo(context, LoginActivity.class);
                }
            });
        }
    }


    public interface OnSuccess {

        void onSuccess();

    }

    /**
     * 加载图片
     *
     * @param v
     * @param url
     */
    public static void loadImg(ImageView v, String url) {
        Glide.with(v.getContext())
                .load(url)
                //                .placeholder(R.mipmap.loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.placeholder)
                .into(v);

    }

    /**
     * 添加删除线
     * @param textView
     */
    public static void addDeleteLine(TextView textView) {
        textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

    }


}
