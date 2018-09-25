package com.example.dell_pc.unitpad.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static com.videogo.ezlog.EZlogHttpUtil.dealResponseResult;


/**
 * Created by dell-pc on 2016/1/8.
 */
public class HttpUtils {


    public static void getJSON(final String url, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn;
                InputStream is;
                try {
                    conn = (HttpURLConnection) new URL(url).openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(6000);
                    conn.setReadTimeout(6000);

                    //获取状态码
                    int code = conn.getResponseCode();

                    is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line = "";
                    StringBuilder result = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    Message msg = new Message();
                    msg.obj = result.toString();
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }).start();
    }

    public static void getJSONWithPosition(final String url, final Handler handler, final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn;
                InputStream is;
                Message msg = new Message();
                try {
                    conn = (HttpURLConnection) new URL(url).openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(6000);
                    conn.setReadTimeout(6000);
                    is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line = "";
                    StringBuilder result = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    msg.obj = result.toString();
                    msg.arg1 = position;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("抛出信息：" + e.getMessage());
                    if (e.getMessage().contains("failed to connect to")) {
                        msg.what = -1;
                        handler.sendMessage(msg);
                    }
                }
            }
        }).start();
    }


    public static void getJsonWithArg(final String url, final Handler handler, final
    int arg) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn;
                InputStream is;
                try {
                    conn = (HttpURLConnection) new URL(url).openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(6000);
                    conn.setReadTimeout(6000);
                    is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line = "";
                    StringBuilder result = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    Message msg = new Message();
                    msg.obj = result.toString();
                    msg.arg1 = arg;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void uploadBitmap(final Bitmap bitmap, final String picName, final String url, final Handler handler, final int position) {

        String strPic = Base64Image.Base64Image(bitmap, 100);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("pic", strPic);
        params.add("picName", picName);


        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                Message msg = new Message();
                msg.obj = result;
                msg.arg1 = position;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });

    }

    public static void post(String param1, String param2, String url, final Handler handler) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("appKey", param1);
        params.add("appSecret", param2);


        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                Message msg = new Message();
                msg.obj = result;
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




    public static void getPicBitmap(final String pic_url, final Handler handler) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL(pic_url).openConnection();
                    conn.setConnectTimeout(6000);
                    conn.setReadTimeout(6000);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    Message msg = new Message();

                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void getPicBitmapWithID(final String pic_url, final Handler handler, final int id) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL(pic_url).openConnection();
                    conn.setConnectTimeout(6000);
                    conn.setReadTimeout(6000);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    Message msg = new Message();
                    msg.arg1 = id;
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void setPicBitmap(final ImageView ivPic, final String pic_url) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL(pic_url).openConnection();
                    conn.setConnectTimeout(6000);
                    conn.setReadTimeout(6000);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    //ivPic.setImageBitmap(bitmap);
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void myPost(final String urlQ, final Map<String,String> params, final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] data = getRequestData(params, "UTF-8").toString().getBytes();//获得请求体
                try {
                    URL url = new URL(urlQ);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(3000);     //设置连接超时时间
                    httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
                    httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
                    httpURLConnection.setRequestMethod("POST");     //设置以Post方式提交数据
                    httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存
                    //设置请求体的类型是文本类型
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");
                    //设置请求体的长度
                    httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
                    //获得输出流，向服务器写入数据
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    outputStream.write(data);

                    int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
                    if (response == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        String str = dealResponseResult(inputStream);                     //处理服务器的响应结果
                        Message msg = new Message();
                        msg.obj = str;
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                    System.out.println("err: " + e.getMessage().toString());
                }
            }
        }).start();

    }

//    public static StringBuffer submitPostData(final String strUrlPath, final Map<String, String> params, final String encode) {
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                byte[] data = getRequestData(params, encode).toString().getBytes();//获得请求体
//                try {
//
//                    //String urlPath = "http://192.168.1.9:80/JJKSms/RecSms.php";
//                    URL url = new URL(strUrlPath);
//
//                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                    httpURLConnection.setConnectTimeout(3000);     //设置连接超时时间
//                    httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
//                    httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
//                    httpURLConnection.setRequestMethod("POST");     //设置以Post方式提交数据
//                    httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存
//                    //设置请求体的类型是文本类型
//                    httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                    //设置请求体的长度
//                    httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
//                    //获得输出流，向服务器写入数据
//                    OutputStream outputStream = httpURLConnection.getOutputStream();
//                    outputStream.write(data);
//
//                    int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
//                    if (response == HttpURLConnection.HTTP_OK) {
//                        InputStream inptStream = httpURLConnection.getInputStream();
//                        return dealResponseResult(inptStream);                     //处理服务器的响应结果
//                    }
//                } catch (IOException e) {
//                    //e.printStackTrace();
//                    return "err: " + e.getMessage().toString();
//                }
//                return "-1";
//            }
//            }
//        }).start();
//    }










    /*
    * Function  :   封装请求体信息
    * Param     :   params请求体内容，encode编码格式
    */
    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
//            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }
}


