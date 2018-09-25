package com.example.dell_pc.microstationreceivealarm;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell_pc.microstationreceivealarm.item.ConstructionItem;
import com.example.dell_pc.microstationreceivealarm.tools.HttpUtils;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.dell_pc.microstationreceivealarm.R.*;


/**
 * Created by dell-pc on 2017/7/28.
 */
public class Chose extends Activity implements View.OnClickListener {
    private String communityID = "3";// 3 k88  13 经安纬固
    ImageView ivWater, ivElectric, ivFireCar, ivAlarmKeyFac, ivWeather;
    TextView tvDate, tvWeek, tvTime;
    TextView tvWeather, tvTmp, tvHum, tvDir, tvSc;
    MyThread myThread = new MyThread();
    boolean isFire = false;
    boolean isFront = true;
    boolean isAct = true;
    private FrameLayout flFireCar, flElectric, flWater;
    private LinearLayout llDate, llWeather, flAlarmKey,flGas;
    private ImageView ivCautionWater, ivCautionElectric, ivCautionFireCar, ivCautionAlarmKey;
    private List<ConstructionItem> constructionList = new ArrayList<ConstructionItem>();

    private String URL_GET_FIRE = "http://jn.xiaofang365.cn/xfwlw/fireCommand/findFireAlarm.php?communityID=" + communityID;


    private String URL_GET_WEATHER = "https://free-api.heweather.com/v5/now?city=CN101120101&key=3f5fdb353081431eb9b41d682e74fecf";
    private GoogleApiClient client;

    public Resources getResources() {
        // TODO Auto-generated method stub
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }


    @Override
    protected void onPause() {
        super.onPause();
        isFront = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFront = true;
        isAct = true;
        isFire = false;
    }

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);
        setContentView(layout.chose2);

        ivWeather = (ImageView) findViewById(id.ivWeather);
        ivWater = (ImageView) findViewById(id.ivWater);
        ivElectric = (ImageView) findViewById(id.ivElectric);
        ivFireCar = (ImageView) findViewById(id.ivFireCar);
        ivAlarmKeyFac = (ImageView) findViewById(id.ivAlarmKeyFac);
        tvDate = (TextView) findViewById(id.tvDate);
        tvWeek = (TextView) findViewById(id.tvWeek);
        tvTime = (TextView) findViewById(id.tvTime);
        tvWeather = (TextView) findViewById(id.tvWeather);
        tvTmp = (TextView) findViewById(id.tvTmp);
        tvHum = (TextView) findViewById(id.tvHum);
        tvDir = (TextView) findViewById(id.tvDir);
        tvSc = (TextView) findViewById(id.tvSc);

        flAlarmKey = (LinearLayout) findViewById(id.flAlarmKey);
        flFireCar = (FrameLayout) findViewById(id.flFireCar);
        flElectric = (FrameLayout) findViewById(id.flElectric);
        flWater = (FrameLayout) findViewById(id.flWater);
        flGas = (LinearLayout) findViewById(id.flGas);
        llDate = (LinearLayout) findViewById(id.llDate);
        llWeather = (LinearLayout) findViewById(id.llWeather);

        myAnimation(flAlarmKey, 1500, 900, 0, -900, 0);
        myAnimation(flFireCar, 1000, 900, 0, 0, 0);
        myAnimation(flElectric, 900, 0, 0, 900, 0);
        myAnimation(flWater, 1200, -900, 0, 0, 0);
        myAnimation(flGas, 800, -900, 0, 0, 0);
        myAnimation(llDate, 1400, 0, 0, -900, 0);
        myAnimation(llWeather, 1800, -900, 0, 0, 0);

        ivCautionWater = (ImageView) findViewById(id.ivCautionWater);
        ivCautionAlarmKey = (ImageView) findViewById(id.ivCautionAlaryKey);
        ivCautionElectric = (ImageView) findViewById(id.ivCautionElectric);
        ivCautionFireCar = (ImageView) findViewById(id.ivCautionFireCar);


        ivWater.setOnClickListener(this);
        ivElectric.setOnClickListener(this);
        ivFireCar.setOnClickListener(this);
        ivAlarmKeyFac.setOnClickListener(this);


        HttpUtils.getJSON(URL_GET_WEATHER, getWeatherHandler);


        myThread.start();

        new Thread() {
            public void run() {
                try {
                    sleep(2000);
                    Message msg = new Message();
                    msg.arg1 = 2000;
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }.start();


        new Thread() {
            public void run() {
                while (true) {
                    try {
                        if (isAct) {
                            sleep(1000);
                            if (isFire == true) {
                                isAct = false;

                                startActivity(new Intent(Chose.this, Map.class));
                            }
                            HttpUtils.getJSON(URL_GET_FIRE, getConstructionHandler);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();

    }


    public static String fireLat, fireLng;
    private Handler getConstructionHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String jsonData = (String) msg.obj;

            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String id = object.getString("constructionID");
                    String name = object.getString("constructionName");
                    String address = object.getString("address");
                    String type = object.getString("type");
                    String structure = object.getString("structure");
                    String area = object.getString("area");
                    String layer = object.getString("layer");
                    String height = object.getString("height");
                    String fac = object.getString("fac");
                    String goods = object.getString("goods");
                    String picUrl = object.getString("picUrl");
                    fireLat = object.getString("lat");
                    fireLng = object.getString("long");
                    constructionList.add(new ConstructionItem(id, name, address, type, structure, area, layer, height, fac, goods, picUrl));
                    isFire = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    private void myAnimation(View view, int duration, float f1, float f2, float f3, float f4) {
        view.setVisibility(View.INVISIBLE);
        //初始化 Translate动画
        TranslateAnimation translateAnimation = new TranslateAnimation(f1, f2, f3, f4);

        AnimationSet set = new AnimationSet(true);
        set.addAnimation(translateAnimation);

        set.setDuration(duration);
        view.startAnimation(set);
        view.setVisibility(View.VISIBLE);
    }

    private void myAnimationRate(View view, int duration, float f1, float f2) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "rotation", f1, f2);
        anim.setDuration(duration);
        anim.start();
        view.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        if (v == ivWater) {
//            startActivity(new Intent(Chose.this, Water.class));//消防给水
            startActivity(new Intent(Chose.this, WebWaterActivity.class));//消火栓
        } else if (v == ivElectric) {
//            startActivity(new Intent(Chose.this, Electric.class));//电气检测
        } else if (v == ivFireCar) {
            startActivity(new Intent(Chose.this, FireCar.class));//消防车辆
        } else if (v == ivAlarmKeyFac) {
//            startActivity(new Intent(Chose.this, AlarmKeyFac.class));//报警检测
            startActivity(new Intent(Chose.this, WebAlarmKeyFacActivity.class));//水箱
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    class MyThread extends Thread {
        int n = 0;

        public void run() {
            try {
                while (true) {
                    if (isFront == true) {
                        sleep(1000);
                        n++;
                        if (n == 600) {
                            HttpUtils.getJSON(URL_GET_WEATHER, getWeatherHandler);
                            n = 0;
                        }
                        Message msg = new Message();
                        msg.arg1 = 1000;
                        handler.sendMessage(msg);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Handler getWeatherHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JSONObject object = null;
            try {
                object = new JSONObject((String) msg.obj);
                String weatherData = object.getString("HeWeather5");
                JSONArray jsonArray = new JSONArray(weatherData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String nowData = obj.getString("now");
                    JSONObject objNow = new JSONObject(nowData);
                    String strCond = objNow.getString("cond");
                    JSONObject objCond = new JSONObject(strCond);
                    String weather = objCond.getString("txt");
                    String tmp = objNow.getString("tmp");
                    String hum = objNow.getString("hum");
                    String strWind = objNow.getString("wind");
                    JSONObject objWind = new JSONObject(strWind);
                    String dir = objWind.getString("dir");
                    String sc = objWind.getString("sc");

                    tvWeather.setText(weather);
                    tvTmp.setText(tmp + "℃");
                    tvHum.setText(hum + "%");
                    tvDir.setText(dir);
                    tvSc.setText(sc);
                    setWeatherImg(weather);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int i = msg.arg1;
            if (i == 1000) {
                getDateTime();
            } else if (i == 2000) {
                myAnimationRate(ivCautionWater, 1500, -180, 0);
                myAnimationRate(ivCautionAlarmKey, 2100, 180, 0);
                myAnimationRate(ivCautionElectric, 900, 180, 0);
                myAnimationRate(ivCautionFireCar, 1800, -180, 0);
            }
        }
    };

    private void getDateTime() {
        SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy.MM.dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String strDate = formatterDate.format(curDate);
        tvDate.setText(strDate);
        SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
        Date curTime = new Date(System.currentTimeMillis());//获取当前时间
        String strTime = formatterTime.format(curTime);
        tvTime.setText(strTime);
        String strWeek = getWeek(formatterDate.toString());
        tvWeek.setText(strWeek);
    }

    private String getWeek(String pTime) {
        String Week = "星期";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "六";
        }
        return Week;
    }

    private void setWeatherImg(String weather) {
        Bitmap bmpWeather = null;
        if (weather.contains("晴")) {
            bmpWeather = BitmapFactory.decodeResource(getResources(), drawable.clear);
        } else if (weather.contains("阴")) {
            bmpWeather = BitmapFactory.decodeResource(getResources(), drawable.overcast);
        } else if (weather.contains("云")) {
            bmpWeather = BitmapFactory.decodeResource(getResources(), drawable.cloud);
        } else if (weather.contains("雨")) {
            bmpWeather = BitmapFactory.decodeResource(getResources(), drawable.rain);
        }
        ivWeather.setImageBitmap(bmpWeather);
    }

    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

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
