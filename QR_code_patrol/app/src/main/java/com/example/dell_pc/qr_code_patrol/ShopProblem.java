package com.example.dell_pc.qr_code_patrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dell_pc.qr_code_patrol.adpter.ShopProblemNotDealAdapter;
import com.example.dell_pc.qr_code_patrol.item.ShopProblemNotDealItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell-pc on 2017/10/18.
 */
public class ShopProblem extends Activity {
    private String unitID, unitName, cardNo, shopName, linkman, phone;
    public static String shopID;
    private ListView lvProblem;
    private ShopProblemNotDealAdapter shopProblemNotDealAdapter;
    public static List<ShopProblemNotDealItem> shopProblemNotDealList = new ArrayList<ShopProblemNotDealItem>();

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_problem);

        initData();
        initView();
    }

    private void initData() {
        Intent intent = this.getIntent();
        unitID = intent.getStringExtra("unitID");
        unitName = intent.getStringExtra("unitName");
        cardNo = intent.getStringExtra("code");
        shopID = intent.getStringExtra("shopID");
        shopName = intent.getStringExtra("shopName");
        linkman = intent.getStringExtra("linkman");
        phone = intent.getStringExtra("phone");


        shopProblemNotDealList.clear();
        for(int i=0;i<Chose.shopProblemNotDealList.size();i++){
            ShopProblemNotDealItem shopProblemNotDealItem = Chose.shopProblemNotDealList.get(i);
            if(shopProblemNotDealItem.getShopID().equals(shopID)){
                shopProblemNotDealList.add(shopProblemNotDealItem);
            }
        }

    }

    private void initView() {
        lvProblem = (ListView) findViewById(R.id.lvProblem);
        shopProblemNotDealAdapter = new ShopProblemNotDealAdapter(ShopProblem.this, shopProblemNotDealList);
        lvProblem.setAdapter(shopProblemNotDealAdapter);
        shopProblemNotDealAdapter.notifyDataSetChanged();
        lvProblem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("unitID", unitID);
                intent.putExtra("unitName", unitName);
                intent.putExtra("code", cardNo);
                intent.putExtra("shopID", shopID);
                intent.putExtra("shopName", shopName);
                intent.putExtra("linkman", linkman);
                intent.putExtra("phone", phone);
                intent.putExtra("id", String.valueOf(position));
                intent.setClass(ShopProblem.this, ProblemDeal.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
