package com.example.dell_pc.qr_code_patrol;

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

import com.example.dell_pc.qr_code_patrol.tools.HttpUtils;
import com.example.dell_pc.qr_code_patrol.tools.NetStatue;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;




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
    private String url,url1;

    private String Info = "";
    private String userNameChn;
    private String userType = null;
    private String userId;
    private int index = 0;

    //private static final String URL_LOGIN = "http://124.133.16.38:8093/test/mr/xfwlw/html/LoginJSON.php";
    //private static final String URL_LOGIN = "http://api.xfyun.net/App/Index/App_Log";
    private static final String URL_LOGIN = "http://jn.xiaofang365.cn/xfwlw/xfwlw/getLoginInfo";

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
        if (ContextCompat.checkSelfPermission(this,
                permissions[i])
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{permissions[i]},
                    permissionRequests[i]);
        }else {
            if (index < (permissionRequests.length - 1)) {
                index++;
                getMyPermissions(index);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == permissionRequests[index]) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(MainActivity.this, this.permissions[index], Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(MainActivity.this, "权限被禁止，操作可能受限", Toast.LENGTH_SHORT).show();
            }
            if (index < (permissionRequests.length - 1)) {
                index++;
                getMyPermissions(index);
            }
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

//        getMyPermissions(0);

        userNameAuto = (AutoCompleteTextView) findViewById(R.id.user_name);
        editTextPassWord = (EditText) findViewById(R.id.password);
        textViewInfo = (TextView) findViewById(R.id.info);
        btnLogin = (Button) findViewById(R.id.btn_login);
        remenberMeCB = (CheckBox) findViewById(R.id.loginRememberMeCheckBox);

        sp = this.getSharedPreferences("passwordFile1", MODE_PRIVATE);
        user = sp.getString("u","");
        psw = sp.getString("p","");
        if(user.equals("")){
        }else {
            userNameAuto.setText(user);
            editTextPassWord.setText(psw);
            url = URL_LOGIN + "?" + "userName=" + user + "&password=" + psw;
            switch (NetStatue.GetNetType(Login.this)) {
                case -1:
                    Toast.makeText(Login.this, "无可用网络", Toast.LENGTH_LONG).show();
                    return;
                default:
                    new GetLogInfo().execute();
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
                String username = userNameAuto.getText().toString();
                String password = editTextPassWord.getText().toString();
                //url = URL_LOGIN + "?" + "userName=" + username + "&passWord=" + password;
                url = URL_LOGIN + "?" + "userName=" + username + "&password=" + password;
                new GetLogInfo().execute();

            }
        });
    }


    class GetLogInfo extends AsyncTask<String, String, String> {
        String Score, checkPatrolPoint, pressPoint, levelPoint, trainingPoint, drillingPoint;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("请稍后...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            Log.d("Log_tag", "onPreExecute...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
                    HttpURLConnection conn;
                    InputStream is;
                    try {
                        conn = (HttpURLConnection) new URL(url).openConnection();
                        conn.setRequestMethod("GET");
                        is = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        String line = "";
                        StringBuilder result = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                            String jsonData = result.toString();
                            JSONObject object = new JSONObject(jsonData);
                            Info = object.getString("info");
                            companyID = object.getString("companyID");
                            companyName = object.getString("companyName");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            return null;
        }

        protected void onPostExecute(String file_url) {

            pDialog.dismiss();

            if(Info.equals("true")){
                String username = userNameAuto.getText().toString();
                String password = editTextPassWord.getText().toString();
                sp.edit().putString(username, password).commit();
                textViewInfo.setText("欢迎您进入FIS智慧消防平台！");
                sp.edit().putString("u",username).commit();
                sp.edit().putString("p",password).commit();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.putExtra("unitId", companyID);
                        String Alias = getUniquePsuedoID();
                        Alias = Alias.replace('-','0');
                        intent.putExtra("unitName",companyName);
                        intent.putExtra("Alias",Alias);
                        intent.setClass(Login.this, Chose.class);
                        startActivity(intent);
                        finish();
                    }
                }, 0);
            }else if (Info.equals("false")) {
                textViewInfo.setText("账号或密码错误！");
            }
        }
    }


    //回退键控制
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {

            finish();
            return true;
        }else if(KeyEvent.KEYCODE_HOME==keyCode) {
            //android.os.Process.killProcess(android.os.Process.myPid());
        }
        return super.onKeyDown(keyCode, event);
    }





//    @Override
//    public void onAttachedToWindow() {
//        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
//        super.onAttachedToWindow();
//    }

    //    private TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
//    private String getDeviceId(){
//        final String tmDevice, tmSerial, tmPhone, androidId;
//        tmDevice = "" + tm.getDeviceId();
//        tmSerial = "" + tm.getSimSerialNumber();
//        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
//
//        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
//        String uniqueId = deviceUuid.toString();
//        return uniqueId;
//    }

    public static String getUniquePsuedoID() {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        return serial;
    }
}
