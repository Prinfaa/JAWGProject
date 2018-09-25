package com.example.dell_pc.unitpad;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell_pc.unitpad.tools.HttpUtils;
import com.example.dell_pc.unitpad.tools.MySpKey;
import com.example.dell_pc.unitpad.tools.NetStatue;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;


public class Login extends Activity {
    private AutoCompleteTextView userNameAuto;
    private EditText editTextPassWord;
    private TextView textViewInfo;
    private Button btnLogin;
    private CheckBox remenberMeCB;
    private String companyID, companyName;
    private String user, psw;
    SharedPreferences sp;


    private ContentValues values;

    private ProgressDialog pDialog;
    private String url, url1;

    private String Info = "";
    private String userNameChn;
    private String userType = null;
    private String userId;
    private int index = 0;

    private int LOGIN = 1000;

    //    private static final String URL_LOGIN = "http://jn.xiaofang365.cn/xfwlw/xfwlw/login_app.php";
    private static final String URL_LOGIN = "http://dndzl.cn/jinan/log_in.php";

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 3;
    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 4;
    private static final int MY_PERMISSIONS_REQUEST_GET_TASKS = 5;
    private static final int MY_PERMISSIONS_REQUEST_MOUNT_UNMOUNT_FILESYSTEMS = 6;
    private static final int MY_PERMISSIONS_REQUEST_NFC = 7;
    private static final int MY_PERMISSIONS_RECEIVE_BOOT_COMPLETED = 8;
    private static final int MY_PERMISSIONS_REQUEST_DISABLE_KEYGUARD = 9;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 10;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_WIFI_STATE = 13;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE = 14;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 15;
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 16;

    private int[] permissionRequests = {
            MY_PERMISSIONS_REQUEST_CALL_PHONE,
            MY_PERMISSIONS_REQUEST_CAMERA,
            MY_PERMISSIONS_REQUEST_READ_CONTACTS,
            MY_PERMISSIONS_REQUEST_RECEIVE_SMS,
            MY_PERMISSIONS_REQUEST_GET_TASKS,
            MY_PERMISSIONS_REQUEST_MOUNT_UNMOUNT_FILESYSTEMS,
            MY_PERMISSIONS_REQUEST_NFC,
            MY_PERMISSIONS_RECEIVE_BOOT_COMPLETED,
            MY_PERMISSIONS_REQUEST_DISABLE_KEYGUARD,
            MY_PERMISSIONS_REQUEST_READ_PHONE_STATE,
            MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION,
            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION,
            MY_PERMISSIONS_REQUEST_ACCESS_WIFI_STATE,
            MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE,
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE,
            MY_PERMISSIONS_REQUEST_INTERNET
    };

    private String[] permissions = {
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.GET_TASKS,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            Manifest.permission.NFC,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.DISABLE_KEYGUARD,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
    };


    private void getMyPermissions(int i) {
        if (getAndroidSDKVersion() >= 23){
            if (ContextCompat.checkSelfPermission(this,
                    permissions[i])
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{permissions[i]},
                        permissionRequests[i]);
            } else {
                if (index < (permissionRequests.length - 1)) {
                    index++;
                    getMyPermissions(index);
                }
            }
        }
    }

    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            Log.i("errTag", e.toString());
        }
        return version;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == permissionRequests[index]) {
//            if(grantResults.length!=0){
                if (grantResults.length > 0 &&grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(MainActivity.this, this.permissions[index], Toast.LENGTH_SHORT).show();
                } else {
//                Toast.makeText(MainActivity.this, "权限被禁止，操作可能受限", Toast.LENGTH_SHORT).show();
                }
                if (index < (permissionRequests.length - 1)) {
                    index++;
                    getMyPermissions(index);
                }
//            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        getMyPermissions(0);

        userNameAuto = (AutoCompleteTextView) findViewById(R.id.user_name);
        editTextPassWord = (EditText) findViewById(R.id.password);
        textViewInfo = (TextView) findViewById(R.id.info);
        btnLogin = (Button) findViewById(R.id.btn_login);
        remenberMeCB = (CheckBox) findViewById(R.id.loginRememberMeCheckBox);

        sp = this.getSharedPreferences("passwordFile1", MODE_PRIVATE);
        user = sp.getString("u", "");
        psw = sp.getString("p", "");
        if (user.equals("")) {
        } else {
            userNameAuto.setText(user);
            editTextPassWord.setText(psw);
            url = URL_LOGIN + "?" + "userName=" + user + "&password=" + psw;
            switch (NetStatue.GetNetType(Login.this)) {
                case -1:
                    Toast.makeText(Login.this, "无可用网络", Toast.LENGTH_LONG).show();
                    return;
                default:
                    int arg = 1000;
                    postLogin(user, psw, URL_LOGIN, arg, handler);


//                    new GetLogInfo().execute();
                    break;
            }

        }
        remenberMeCB.setChecked(true);// 默认为记住密码
        userNameAuto.setThreshold(1);// 输入1个字母就开始自动提示

        userNameAuto.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                String[] allUserName = new String[sp.getAll().size()];// sp.getAll().size()返回的是有多少个键值对
                allUserName = sp.getAll().keySet().toArray(new String[0]);
                // sp.getAll()返回一张hash map
                // keySet()得到的是a set of the keys.
                // hash map是由key-value组成的

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        Login.this,
                        android.R.layout.simple_dropdown_item_1line,
                        allUserName);

                userNameAuto.setAdapter(adapter);// 设置数据适配器

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                editTextPassWord.setText(sp.getString(userNameAuto.getText()
                        .toString(), ""));// 自动输入密码


            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Info = "";
                user = userNameAuto.getText().toString();
                psw = editTextPassWord.getText().toString();
                postLogin(user, psw, URL_LOGIN, LOGIN, handler);
            }
        });
    }

    public static void postLogin(String userName, String passWord, String url, final int arg, final Handler handler) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_name", userName);
        params.add("user_password", passWord);


        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                Message msg = new Message();
                msg.obj = result;
                msg.arg1 = arg;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                String result = new String(bytes);
                Message msg = new Message();
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });

    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;
            try {
                JSONObject object = new JSONObject(jsonData);
                Info = object.getString("info");
                if(Info.equals("error")){
                    textViewInfo.setText("用户名或密码错误");
                    textViewInfo.setTextColor(0XFFFF0000);
                }else if(Info.equals("success")){
                    JSONObject object1 = object.getJSONObject("result");
                    companyID = object1.getString("company_id");
                    companyName = object1.getString("com_name");
                    String username = userNameAuto.getText().toString();
                    String password = editTextPassWord.getText().toString();
                    sp.edit().putString(username, password).commit();
                    textViewInfo.setText("欢迎您进入FIS智慧消防平台！");
                    sp.edit().putString("u", username).commit();
                    sp.edit().putString("p", password).commit();
                    sp.edit().putString(MySpKey.UNIT_ID, companyID).commit();
                    sp.edit().putString(MySpKey.UNIT_NAME, companyName).commit();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.putExtra("unitID", companyID);
                            intent.putExtra("unitName", companyName);

                            if(companyID.equals("342")){

                                intent.setClass(Login.this, CompanyOnMapActivity.class);
                                intent.putExtra("bloc",companyID);

                            }else{

                                intent.setClass(Login.this, MainActivity.class);

                            }

                            startActivity(intent);
                            finish();
                        }
                    }, 500);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ;
    };




    //回退键控制
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {

            finish();
            return true;
        } else if (KeyEvent.KEYCODE_HOME == keyCode) {
            //android.os.Process.killProcess(android.os.Process.myPid());
        }
        return super.onKeyDown(keyCode, event);
    }


}
