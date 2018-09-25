package com.example.dell.unittv;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell.unittv.adapter.CheckCardAdapter1;
import com.example.dell.unittv.item.CheckCardItem;
import com.example.dell.unittv.tools.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell-pc on 2016/3/6.
 */
public class UnCheckCard extends Activity {

    private String unitId, unitName;
    private ListView lvUnCheckCard;
    private String urlUnCheckCard;
    private String checkPeriod;

    private List<CheckCardItem> checkCardList;
    private CheckCardAdapter1 adapter;

    //public static final String GET_UNCHECKCARD_URL = "http://124.133.16.38:8093/test/mr/xfwlw/html/getUnCheckedCardInfoJSON.php";
    public static final String GET_UNCHECKCARD_URL = "http://m.xiaofang365.cn/home/index/Noxj";


    private void UnCheckCard() {

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.uncheck_card);
        lvUnCheckCard = (ListView) findViewById(R.id.lvUnCheckCard);


        checkCardList = new ArrayList<CheckCardItem>();
        adapter = new CheckCardAdapter1(this, checkCardList);
        lvUnCheckCard.setAdapter(adapter);

        unitId = getIntent().getStringExtra("unitId");
        unitName = getIntent().getStringExtra("unitName");
        TextView tvUnitName = (TextView) findViewById(R.id.tvUnitName);
        tvUnitName.setText(unitName);
        checkPeriod = getIntent().getStringExtra("checkPeriod");

        urlUnCheckCard = GET_UNCHECKCARD_URL + "?unitId=" + unitId + "&startTime=" + checkPeriod;

        System.out.println(urlUnCheckCard);
        HttpUtils.getJSON(urlUnCheckCard, getUnCheckCardHandler);

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private Handler getUnCheckCardHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            String jsonData = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String cardNo = object.getString("cardno");
                    String position = object.getString("position");
                    String fac = object.getString("fac");
                    System.out.println(cardNo);
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
