package com.example.dell_pc.qr_code_patrol;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;

import com.example.dell_pc.qr_code_patrol.adpter.CheckCardAdapter1;

import com.example.dell_pc.qr_code_patrol.db.MyAllCheckCardDB;
import com.example.dell_pc.qr_code_patrol.item.CheckCardItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell-pc on 2016/3/6.
 */
public class CheckCard extends Activity {
    private MyAllCheckCardDB mAllCardDB;
    private Cursor mCursor;
    private String unitId;
    private ListView lvCheckCard;
    String urlCheckCard;

    private List<CheckCardItem> checkCardList;
    private CheckCardAdapter1 adapter;

    public static final String GET_CHECKCARD_URL = "http://124.133.16.38:8093/test/mr/xfwlw/html/getCheckCardJSON.php";
    private void CheckCard() {

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.check_card);
        lvCheckCard = (ListView) findViewById(R.id.lvCheckCard123);
        checkCardList = new ArrayList<CheckCardItem>();
        adapter = new CheckCardAdapter1(this, checkCardList);
        lvCheckCard.setAdapter(adapter);


        mAllCardDB = new MyAllCheckCardDB(this);
        mCursor = mAllCardDB.select1();
        //Cursor c = mDb.rawQuery("select * from " + TABLE_NAME + " order by " + ID + " desc", null);
        int i=0;
        while (mCursor.moveToNext()){
            String cardNo = mCursor.getString(1);
            String Position = mCursor.getString(2);
            String Fac = mCursor.getString(3);
            checkCardList.add(new CheckCardItem(cardNo, Position, Fac));
        }

        //checkCardList = mCheckDB.getAllCard();


        //System.out.println("listCount============" + checkCardList.size());


        adapter.notifyDataSetChanged();
        //unitId = getIntent().getStringExtra("unitId");

        //urlCheckCard = GET_CHECKCARD_URL + "?unitId=" + unitId;

        //System.out.println(urlCheckCard);
       // HttpUtils.getJSON(urlCheckCard, getCheckCardHandler);

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }




    private Handler getCheckCardHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            String jsonData = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String cardNo = object.getString("cardNo");
                    String position = object.getString("Position");
                    String fac = object.getString("Fac");
                    checkCardList.add(new CheckCardItem(cardNo,position,fac));
                 }
                adapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };
}
