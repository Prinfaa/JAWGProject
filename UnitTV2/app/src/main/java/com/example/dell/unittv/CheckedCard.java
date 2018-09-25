package com.example.dell.unittv;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.dell.unittv.adapter.CheckCardAdapter;
import com.example.dell.unittv.adapter.CheckRecordAdapter;
import com.example.dell.unittv.item.CheckCardItem;
import com.example.dell.unittv.item.CheckRecordItem;
import com.example.dell.unittv.tools.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by dell-pc on 2016/3/6.
 */
public class CheckedCard extends Activity {

    private String unitId, unitName, picUrl, cardNo, Fac, Pisition, time, Note, Statue;
    private ListView lvCheckedCard, lvCheckRecord;
    private ProgressDialog pDialog;
    String urlCheckedCard, urlCheckRecord;
    String checkPeriod;

    private List<CheckCardItem> checkedCardList;
    private CheckCardAdapter adapter;

    private List<CheckRecordItem> checkRecordList;
    private CheckRecordAdapter adapter1;
    private ScrollView svInfo;
    private LinearLayout llMain;

    ImageView ivCheck;
    TextView tvCardNo;
    TextView tvFac;
    TextView tvPosition;
    TextView tvTime;
    TextView tvStatue;
    TextView tvNote;

    boolean isInfoShow = false;

    //public static final String GET_CHECKEDCARDINFO_URL = "http://124.133.16.38:8093/test/mr/xfwlw/html/getCheckedCardInfoJSON.php";
    public static final String GET_CHECKEDCARDINFO_URL = "http://m.xiaofang365.cn/home/index/HistoryXj";
    //public static final String GET_CHECKRECORDINFO_URL = "http://124.133.16.38:8093/test/mr/xfwlw/html/getCheckRecordInfoJSON.php";
    public static final String GET_CHECKRECORDINFO_URL = "http://m.xiaofang365.cn/home/index/HistoryCard";

    private void CheckedCard() {

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.checked_card);
        lvCheckedCard = (ListView) findViewById(R.id.lvCheckedCard);
        lvCheckRecord = (ListView) findViewById(R.id.lvCheckRecord);

        svInfo = (ScrollView) findViewById(R.id.sv_info);
        llMain = (LinearLayout) findViewById(R.id.ll_main);

        ivCheck = (ImageView) findViewById(R.id.ivCheck);
        tvCardNo = (TextView) findViewById(R.id.tvCardNo);
        tvFac = (TextView) findViewById(R.id.tvFac);
        tvPosition = (TextView) findViewById(R.id.tvPosition);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvStatue = (TextView) findViewById(R.id.tvStatue);
        tvNote = (TextView) findViewById(R.id.tvNote);


        checkedCardList = new ArrayList<CheckCardItem>();
        adapter = new CheckCardAdapter(this, checkedCardList);
        lvCheckedCard.setAdapter(adapter);

        checkRecordList = new ArrayList<CheckRecordItem>();
        adapter1 = new CheckRecordAdapter(this, checkRecordList);
        lvCheckRecord.setAdapter(adapter1);

        unitId = getIntent().getStringExtra("unitId");
        unitName = getIntent().getStringExtra("unitName");
        checkPeriod = getIntent().getStringExtra("checkPeriod");

        TextView tvUnitName = (TextView) findViewById(R.id.tvUnitName);
        tvUnitName.setText(unitName);


        urlCheckedCard = GET_CHECKEDCARDINFO_URL + "?unitId=" + unitId + "&startTime=" + checkPeriod;


        System.out.println(urlCheckedCard);
        HttpUtils.getJSON(urlCheckedCard, getCheckedCardHandler);

        lvCheckedCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckCardItem checkCardItem = checkedCardList.get(position);
                cardNo = checkCardItem.getCardNo();
                Fac = checkCardItem.getFac();
                Pisition = checkCardItem.getPosition();


                urlCheckRecord = GET_CHECKRECORDINFO_URL + "?cardNo=" + cardNo + "&startTime=" + checkPeriod;
                System.out.println(urlCheckRecord);
                checkRecordList.clear();
                HttpUtils.getJSON(urlCheckRecord, getCheckRecordHandler);
                adapter.setSelectItem(position);
                adapter.notifyDataSetInvalidated();

            }
        });

        lvCheckRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckRecordItem checkRecordItem = checkRecordList.get(position);
                cardNo = checkRecordItem.getCardNo();
                time = checkRecordItem.getTime();
                picUrl = checkRecordItem.getPicUrl();
                Note = checkRecordItem.getNote();
                Statue = checkRecordItem.getStatue();
                new GetCheckInfo().execute();

            }
        });

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInfoShow == true){
                    svInfo.setVisibility(View.GONE);
                    llMain.setVisibility(View.VISIBLE);
                    isInfoShow = false;
                }else {
                    finish();
                }

            }
        });

    }

    private Handler getCheckedCardHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            String jsonData = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String cardNo1 = object.getString("cardno");
                    String position = object.getString("position");
                    String fac = object.getString("fac");
                    System.out.println(cardNo1);
                    checkedCardList.add(new CheckCardItem(cardNo1, position, fac));
                    cardNo = cardNo1;
                    if (i == 0) {
                        urlCheckRecord = GET_CHECKRECORDINFO_URL + "?cardNo=" + cardNo + "&startTime=" + checkPeriod;
                        HttpUtils.getJSON(urlCheckRecord, getCheckRecordHandler);


                        Pisition = position;
                        Fac = fac;
                    }

                }
                adapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };

    private Handler getCheckRecordHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            String jsonData = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String time = object.getString("time");
                    //System.out.println(time);
                    time = getStrTime(time);
                    String picUrl = "http://m.xiaofang365.cn/public" + object.getString("pic_url");
                    String Note = object.getString("note");
                    String Statue = object.getString("statue");

                    checkRecordList.add(new CheckRecordItem(cardNo, time, Note, picUrl, Statue));
                }
                adapter1.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };

    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }


    class GetCheckInfo extends AsyncTask<String, String, String> {
        Bitmap bitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CheckedCard.this);
            pDialog.setMessage("请稍后...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            Log.d("Log_tag", "onPreExecute...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connInfo, connPic;
            InputStream isInfo, isPic;

            try {

                connPic = (HttpURLConnection) new URL(picUrl).openConnection();
                connPic.connect();
                isPic = connPic.getInputStream();
                bitmap = BitmapFactory.decodeStream(isPic);


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
            if (bitmap != null) {
                isInfoShow = true;

                int bmpWidth = bitmap.getWidth();
                int bmpHeight = bitmap.getHeight();


                int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                ivCheck.measure(w, h);
                int ivCheckWidth =ivCheck.getMeasuredWidth();


                int ivCheckHeight = ivCheckWidth * bmpHeight/bmpWidth;


//                String str = "bmpWidth:" + String.valueOf(bmpWidth) + "\r\n"
//                            +"bmpHeight:" + String.valueOf(bmpHeight) + "\r\n"
//                        +"ivCheckWidth:" + String.valueOf(ivCheckWidth) + "\r\n"
//                              +"ivCheckHeight:" + String.valueOf(ivCheckHeight) + "\r\n";
//                Toast.makeText(CheckedCard.this,str,Toast.LENGTH_LONG).show();

                LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) ivCheck.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20

                linearParams.height = ivCheckHeight;

                ivCheck.setLayoutParams(linearParams); //使设置好的布局参数应用到控件

                ivCheck.setImageBitmap(bitmap);
                tvCardNo.setText(cardNo);
                tvFac.setText(Fac);
                tvPosition.setText(Pisition);
                tvTime.setText(time);
                if (Statue.contains("不正常")) {
                    tvStatue.setTextColor(0xffff0000);
                } else {
                    tvStatue.setTextColor(0xff0E583A);
                }
                Statue = Statue.replaceAll("_", " ");
                tvStatue.setText(Statue);
                tvNote.setText(Note);

                llMain.setVisibility(View.GONE);
                svInfo.setVisibility(View.VISIBLE);

            }

        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (isInfoShow == true) {
                svInfo.setVisibility(View.GONE);
                llMain.setVisibility(View.VISIBLE);
                isInfoShow = false;
            }else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public int getBitmapHeight(Bitmap bitmap){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1){//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getHeight();                //earlier version
    }


    public int getBitmapWidth(Bitmap bitmap){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1){//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes();                //earlier version
    }

}
