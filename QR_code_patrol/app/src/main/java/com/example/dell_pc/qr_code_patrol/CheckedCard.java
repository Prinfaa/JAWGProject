package com.example.dell_pc.qr_code_patrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell_pc.qr_code_patrol.adpter.CheckedAdapter;
import com.example.dell_pc.qr_code_patrol.adpter.CheckRecordAdapter;
import com.example.dell_pc.qr_code_patrol.item.CheckCardItem;
import com.example.dell_pc.qr_code_patrol.item.CheckRecordItem;
import com.example.dell_pc.qr_code_patrol.item.CheckedItem;
import com.example.dell_pc.qr_code_patrol.tools.HttpUtils;

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

    private String unitId,picUrl,cardNo,Fac,Pisition,time,Note,Statue;
    private ListView lvCheckedCard,lvCheckRecord;
    private ProgressDialog pDialog;
    String urlCheckedCard,urlCheckRecord;
    String checkPeriod;

    private List<CheckedItem> checkedCardList;
    private CheckedAdapter adapter;

    private List<CheckRecordItem> checkRecordList;
    private CheckRecordAdapter adapter1;

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

        checkedCardList = new ArrayList<CheckedItem>();
        adapter = new CheckedAdapter(this, checkedCardList);
        lvCheckedCard.setAdapter(adapter);

        checkRecordList = new ArrayList<CheckRecordItem>();
        adapter1 = new CheckRecordAdapter(this,checkRecordList);
        lvCheckRecord.setAdapter(adapter1);

        unitId = getIntent().getStringExtra("unitId");
        checkPeriod = getIntent().getStringExtra("checkPeriod");



        urlCheckedCard = GET_CHECKEDCARDINFO_URL + "?unitId=" + unitId + "&startTime=" + checkPeriod;


        HttpUtils.getJSON(urlCheckedCard, getCheckedCardHandler);

        lvCheckedCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedItem checkCardItem = checkedCardList.get(position);
                cardNo = checkCardItem.getCardNo();
                Fac = checkCardItem.getFac();
                Pisition = checkCardItem.getPosition();


                urlCheckRecord = GET_CHECKRECORDINFO_URL + "?cardNo=" + cardNo + "&startTime=" + checkPeriod;
                checkRecordList.clear();
                HttpUtils.getJSON(urlCheckRecord,getCheckRecordHandler);
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
                finish();
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
                    //checkedCardList.add(new CheckedItem(cardNo1,position,fac));
                    cardNo = cardNo1;
                    if(i == 0){
                        urlCheckRecord = GET_CHECKRECORDINFO_URL + "?cardNo=" + cardNo + "&startTime=" + checkPeriod;
                        HttpUtils.getJSON(urlCheckRecord,getCheckRecordHandler);


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

                    checkRecordList.add(new CheckRecordItem(cardNo,time,Note,picUrl,Statue));
                }
                adapter1.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };

    public static String getStrTime(String cc_time){
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time*1000L));
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
            HttpURLConnection connInfo,connPic;
            InputStream isInfo,isPic;

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
            if(bitmap != null) {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.dialog,
                        (ViewGroup) findViewById(R.id.dialog));
                ImageView ivCheck = (ImageView) layout.findViewById(R.id.ivCheck);
                ivCheck.setImageBitmap(bitmap);
                TextView tvCardNo = (TextView)layout.findViewById(R.id.tvCardNo);
                tvCardNo.setText(cardNo);
                TextView tvFac = (TextView)layout.findViewById(R.id.tvFac);
                tvFac.setText(Fac);
                TextView tvPosition = (TextView)layout.findViewById(R.id.tvPosition);
                tvPosition.setText(Pisition);
                TextView tvTime = (TextView)layout.findViewById(R.id.tvTime);
                tvTime.setText(time);
                TextView tvStatue = (TextView)layout.findViewById(R.id.tvStatue);
                if(Statue.contains("不正常")){
                    tvStatue.setTextColor(0xffff0000);
                }
                else {
                    tvStatue.setTextColor(0xff0E583A);
                }
                Statue = Statue.replaceAll("_"," ");
                tvStatue.setText(Statue);
                TextView tvNote = (TextView)layout.findViewById(R.id.tvNote);
                tvNote.setText(Note);

                AlertDialog.Builder builder = new AlertDialog.Builder(CheckedCard.this);
                final AlertDialog dialog = builder.setView(layout).show();

                layout.findViewById(R.id.llCheckInfo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }

        }
    }




}
