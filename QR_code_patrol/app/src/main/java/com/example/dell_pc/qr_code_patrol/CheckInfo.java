package com.example.dell_pc.qr_code_patrol;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell_pc.qr_code_patrol.db.CheckDB;
import com.example.dell_pc.qr_code_patrol.db.MyAllCheckCardDB;
import com.example.dell_pc.qr_code_patrol.item.CheckedItem;
import com.example.dell_pc.qr_code_patrol.tools.TimeManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cz.msebera.android.httpclient.Header;



public class CheckInfo extends Activity {
    private TextView tvRecord;
    private ListView lvRecord;
    private CheckDB mCheckDB;
    private MyAllCheckCardDB myAllCardDB;
    private ArrayList<CheckedItem> checkList;
    private Cursor mCursor, mCursor1;
    private String unitId;
    private int n = 0;
    private Bitmap bitmap;
    private int send = 0;
    private LinearLayout llSendStatus;
    private TextView tvSendStatus;

    //public static final String GET_CHECKCARD_URL = "http://124.133.16.38:8093/test/mr/xfwlw/html/getCheckCardJSON.php";
    public static final String GET_CHECKCARD_URL = "http://m.xiaofang365.cn/home/datexj/getCheckCard";

    //private static final String ADD_CHECK_RECORD = "http://124.133.16.38:8093/test/mr/xfwlw/html/addCheckRecordJSON.php";
    private static final String ADD_CHECK_RECORD = "http://m.xiaofang365.cn/home/index/xunjian";
    //private final String URL_CHECKIMAGEUPLOAD = "http://124.133.16.38:8093/test/mr/xfwlw/html/CheckImageUpload.php";
    private final String URL_CHECKIMAGEUPLOAD = "http://m.xiaofang365.cn/home/index/shangchuan";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View();


    }

    private void View() {
        setContentView(R.layout.check_info);
        Intent intent1 = this.getIntent();
        unitId = intent1.getStringExtra("unitId");
        //mCheckDB1 = new CheckCardUPDateDB(this);
        //mCursor1 = mCheckDB1.select();
        //String url = GET_CHECKCARD_URL + "?unitId=" + unitId;
        //HttpUtils.getJSON(url, getCheckCardHandler);
        mCheckDB = new CheckDB(this);
        mCursor = mCheckDB.select();
        //checkCardList.clear();
        llSendStatus = (LinearLayout) findViewById(R.id.llSendStatus);
        tvSendStatus = (TextView) findViewById(R.id.tvSendStatus);
        tvRecord = (TextView) findViewById(R.id.tvRecord);
        lvRecord = (ListView) findViewById(R.id.lvRcord);
        checkList = (ArrayList<CheckedItem>) mCheckDB.getAllData();
        lvRecord.setAdapter(new CheckListAdapter(this, checkList));
        lvRecord.invalidateViews();
        String Count = mCheckDB.getCount().toString();
        tvRecord.setText(Count);
        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.tvBeginCheck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CheckInfo.this, Patrol.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.tvSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llSendStatus.setVisibility(View.VISIBLE);
                n = 0;
                if(send == 0){
                    sendRecord();
                }else {
                    Toast.makeText(CheckInfo.this, "记录正在上传，请耐心等待!", Toast.LENGTH_LONG).show();
                }
            }
        });

        lvRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedItem checkedItem = checkList.get(position);
                String card = checkedItem.getCardNo();
                String time = checkedItem.getDateTime();
                String pn = checkedItem.getPosition();
                String fac = checkedItem.getFac();
                String pic = checkedItem.getImageName();
//                String note = checkedItem.getNote();
                String statue = checkedItem.getStatue();
                statue = statue.replaceAll("_"," ");

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.dialog1,
                        (ViewGroup) findViewById(R.id.dialog));
                String pathUrl = Environment.getExternalStorageDirectory().toString() + File.separator + File.separator + "IMAGE";
//                String pathUrl = Environment.getExternalStorageDirectory() + "/checkpic/";
                bitmap = getDiskBitmap(pathUrl + pic);
                ImageView ivCheck = (ImageView) layout.findViewById(R.id.ivCheck);
                ivCheck.setImageBitmap(bitmap);
                TextView tvCardNo = (TextView) layout.findViewById(R.id.tvCardNo);
                tvCardNo.setText(card);
                TextView tvFac = (TextView) layout.findViewById(R.id.tvFac);
                tvFac.setText(fac);
                TextView tvPosition = (TextView) layout.findViewById(R.id.tvPosition);
                tvPosition.setText(pn);
                TextView tvTime = (TextView) layout.findViewById(R.id.tvTime);
                tvTime.setText(time);
                TextView tvStatue = (TextView) layout.findViewById(R.id.tvStatue);
                if(statue.contains("不正常")){
                    tvStatue.setTextColor(0xffff0000);
                }
                else {
                    tvStatue.setTextColor(0xff0E583A);
                }
                tvStatue.setText(statue);
                TextView tvNote = (TextView) layout.findViewById(R.id.tvNote);
//                tvNote.setText(note);


                AlertDialog.Builder builder = new AlertDialog.Builder(CheckInfo.this);
                final AlertDialog dialog = builder.setView(layout).show();
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public class CheckListAdapter extends BaseAdapter {

        private Context context;
        private List<CheckedItem> checkList;

        public CheckListAdapter(Context context, List<CheckedItem> checkList) {
            this.context = context;
            this.checkList = checkList;
        }

        @Override
        public int getCount() {
            return checkList.size();
        }

        @Override
        public CheckedItem getItem(int position) {
            return checkList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        private Map<Integer, View> viewMap = new HashMap<Integer, View>();
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = new ViewHolder();

            convertView = this.viewMap.get(position);


            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.checked_item, null);
            }
            TextView tvCardNo = (TextView) convertView.findViewById(R.id.tvCardNoItem);
            TextView tvPosition = (TextView) convertView.findViewById(R.id.tvPosition);
            TextView tvDateTime = (TextView) convertView.findViewById(R.id.tvTime);
            TextView tvFac = (TextView) convertView.findViewById(R.id.tvFac);
            TextView tvStatue = (TextView) convertView.findViewById(R.id.tvStatue);

            ImageView ivCheck = (ImageView) convertView.findViewById(R.id.ivCheck);

            CheckedItem checkedItem = checkList.get(position);

            tvCardNo.setText(checkedItem.getCardNo());
            tvPosition.setText(checkedItem.getPosition());
            tvDateTime.setText(checkedItem.getDateTime());
            tvFac.setText(checkedItem.getFac());

            String statue = checkedItem.getStatue();
            if(statue.contains("不正常")){
                tvStatue.setTextColor(0xffff0000);
            }
            else {
                tvStatue.setTextColor(0xff0E583A);
            }
            statue = statue.replaceAll("_"," ");
            tvStatue.setText(statue);

            String picName = checkedItem.getImageName();
            String pathUrl = Environment.getExternalStorageDirectory().toString() + File.separator + File.separator + "IMAGE";
//            String pathUrl = Environment.getExternalStorageDirectory() + "/checkpic/";
            Bitmap bitmap = getDiskBitmap(pathUrl + picName);
            ivCheck.setImageBitmap(bitmap);

            if (position % 2 == 1) {
                convertView.setBackgroundColor(0xFFFFFFFF);
            } else {
                convertView.setBackgroundColor(0xFFF7F7F7);
            }

            return convertView;

        }

    }
    public class ViewHolder {
        public TextView mNameText;
        public ImageView mPhoto;
    }


    private Bitmap getDiskBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            //if(file.exists())
            //{
            bitmap = BitmapFactory.decodeFile(pathString);
            //}
        } catch (Exception e) {
            // TODO: handle exception
        }


        return bitmap;
    }


//    private Handler getCheckCardHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            String jsonData = (String) msg.obj;
//            try {
//                JSONArray jsonArray = new JSONArray(jsonData);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject object = jsonArray.getJSONObject(i);
//                    String cardNo = object.getString("cardno");
//                    String Position = object.getString("position");
//                    String Fac = object.getString("fac");
//
//                    add(cardNo, Position, Fac);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        ;
//    };

    private Handler addCheckRecordHandler = new Handler() {
        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;
            try {
                JSONObject object = new JSONObject(jsonData);
                String info = object.getString("info");
                if (info.equals("success")) {


                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };

    public void add(String cardNo, String Position, String Fac, String problemType) {
        myAllCardDB.insert(cardNo, Position, Fac, problemType);
        mCursor1.requery();
    }

    protected void onResume() {
        super.onResume();
        View();
    }

    protected void onResume1() {
        super.onResume();
        Toast.makeText(this, "上传成功!", Toast.LENGTH_SHORT).show();
        View();
    }


    public void sendRecord() {
        String url;
        tvSendStatus.setText("已上传    " + String.valueOf(n) + "/" + String.valueOf(checkList.size()));
        CheckedItem checkedItem = checkList.get(n);
        String cardNo = checkedItem.getCardNo();
        String dateTime = dataOne(checkedItem.getDateTime());
//        String Note = checkedItem.getNote();
        String imageName = checkedItem.getImageName();
        String Statue = checkedItem.getStatue();
        try {
//            Note = URLEncoder.encode(Note, "UTF-8");
            Statue = URLEncoder.encode(Statue,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String dateStr = TimeManager.getStrTime4(dateTime);
//        url = ADD_CHECK_RECORD + "?" + "cardNo=" + cardNo + "&unitId=" + unitId + "&Note=" + Note + "&imageName=" + imageName+ "&Statue=" + Statue;
//        System.out.println(url);
//        HttpUtils.getJSON(url, addCheckRecordHandler);

        String img64 = null;
        String pathUrl = Environment.getExternalStorageDirectory().toString() + File.separator + File.separator + "IMAGE";
//
//        String pathUrl = Environment.getExternalStorageDirectory() + "/checkpic/";
        bitmap = getDiskBitmap(pathUrl + imageName);
        img64 = Base64Image(bitmap);
        if (img64 != null) {
            sendImage(img64, imageName);
        }else {
        }







    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public String dataOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy/MM/dd/HH:mm",
                Locale.CHINA);
        Date date;
        String times = null;

        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return times;
    }

    private void sendImage(String str, String strPicName) {
        send = 1;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("img", str);
        params.add("checkPic", strPicName);


        client.post(URL_CHECKIMAGEUPLOAD, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                //Toast.makeText(CheckInfo.this, "成功上传一条记录!", Toast.LENGTH_LONG).show();
                n++;
                if (n == mCheckDB.getCount()) {
                    mCheckDB.deleteAll();
                    onResume1();
                    llSendStatus.setVisibility(View.INVISIBLE);
                    send = 0;
                }else {
                    sendRecord();
                }
                String pathUrl = Environment.getExternalStorageDirectory().toString() + File.separator + File.separator + "IMAGE";
//              String pathUrl = Environment.getExternalStorageDirectory() + "/checkpic/";
                File file = new File(pathUrl);
                delete(file);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(CheckInfo.this, "Upload Fail!", Toast.LENGTH_LONG).show();
            }


        });


    }

    private String Base64Image(Bitmap bm) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] bytes = stream.toByteArray();
        String img = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
        return img;
    }



    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }


}
