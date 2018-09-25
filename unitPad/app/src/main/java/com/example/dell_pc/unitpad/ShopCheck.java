package com.example.dell_pc.unitpad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell_pc.unitpad.adapter.CheckAbnormalAdapter;
import com.example.dell_pc.unitpad.adapter.CheckNormalAdapter;
import com.example.dell_pc.unitpad.adapter.ShopProblemDealAdapter;
import com.example.dell_pc.unitpad.adapter.ShopProblemNotDealAdapter;
import com.example.dell_pc.unitpad.adapter.ShopUnCheckedAdapter;
import com.example.dell_pc.unitpad.adapter.UnCheckAdapter;
import com.example.dell_pc.unitpad.charts.DrawBarCheck;
import com.example.dell_pc.unitpad.charts.DrawPieCheck;
import com.example.dell_pc.unitpad.charts.DrawPieShop;
import com.example.dell_pc.unitpad.item.ShopProblemNotDealItem;
import com.example.dell_pc.unitpad.tools.MyAnimation;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;


/**
 * Created by dell-pc on 2016/1/12.
 */
public class ShopCheck extends Activity implements View.OnClickListener {

    int year;

    String unitId, unitName;

    private PieChart pieCheck;
    private DrawPieShop drawPieShop = new DrawPieShop();


    private ShopProblemNotDealAdapter shopProblemNotDealAdapter;
    private ShopProblemDealAdapter shopProblemDealAdapter;
    private ListView lvNotDeal, lvDeal;

    private ShopUnCheckedAdapter shopUnCheckedAdapter;
    private ListView lvUnCheck;
    private LinearLayout llNotDeal, llDeal;

    private TextView tvNotDealTitle, tvDealTitle, tvShopUnCheck;

    private MyAnimation myAnimation = new MyAnimation();



    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_check);

        initData();
        initView();


    }

    private void initData() {
        Intent intent = this.getIntent();
        unitId = intent.getStringExtra("unitID");

    }

    private void initView() {
        pieCheck = (PieChart) findViewById(R.id.pieCheck);

        int shopUnCheckCount = MainActivity.shopUnCheckedItemList.size();
        int shopCheckCount = MainActivity.shopCheckedItemList.size();

        drawPieShop.draw(pieCheck, ShopCheck.this, shopCheckCount, shopUnCheckCount, true);

        tvNotDealTitle = (TextView) findViewById(R.id.tvNotDealTitle);
        tvDealTitle = (TextView) findViewById(R.id.tvDealTitle);

        lvNotDeal = (ListView) findViewById(R.id.lvNotDeal);
        shopProblemNotDealAdapter = new ShopProblemNotDealAdapter(this,MainActivity.shopProblemNotDealItemList);
        lvNotDeal.setAdapter(shopProblemNotDealAdapter);
//
        lvDeal = (ListView) findViewById(R.id.lvDeal);
        shopProblemDealAdapter = new ShopProblemDealAdapter(this,MainActivity.shopProblemDealedItemList);
        lvDeal.setAdapter(shopProblemDealAdapter);
//
        lvUnCheck = (ListView) findViewById(R.id.lvUnCheck);
        shopUnCheckedAdapter = new ShopUnCheckedAdapter(this,MainActivity.shopUnCheckedItemList);
        lvUnCheck.setAdapter(shopUnCheckedAdapter);
//
        tvShopUnCheck = (TextView) findViewById(R.id.tvUnCheckedTitle);
        tvNotDealTitle = (TextView) findViewById(R.id.tvNotDealTitle);
        tvDealTitle = (TextView) findViewById(R.id.tvDealTitle);

        tvShopUnCheck.setText("本月未检查商铺   " + String.valueOf(MainActivity.shopUnCheckedItemList.size()) + "家");
        tvNotDealTitle.setText("未整改隐患   " + String.valueOf(MainActivity.shopProblemNotDealItemList.size()) + "处");
        tvDealTitle.setText("已整改隐患   " + String.valueOf(MainActivity.shopProblemDealedItemList.size()) + "处");

        tvNotDealTitle.setOnClickListener(this);
        tvDealTitle.setOnClickListener(this);

        llDeal = (LinearLayout) findViewById(R.id.llDeal);
        llNotDeal = (LinearLayout) findViewById(R.id.llNotDeal);

    }

    @Override
    public void onClick(View v) {
        if (v == tvNotDealTitle) {
            if (llDeal.getVisibility() == View.VISIBLE) {
                myAnimation.Go(llDeal, 500, 0, 2000, 0, 0);
                myAnimation.Come(llNotDeal, 500, 0, 0, 2000, 0);
                tvNotDealTitle.setBackgroundColor(0X88336699);
                tvDealTitle.setBackgroundColor(0XFF336699);

            }
        } else if (v == tvDealTitle) {
            if (llNotDeal.getVisibility() == View.VISIBLE) {
                myAnimation.Go(llNotDeal, 500, 0, 0, 0, 2000);
                myAnimation.Come(llDeal, 500, 2000, 0, 0, 0);
                tvNotDealTitle.setBackgroundColor(0XFF336699);
                tvDealTitle.setBackgroundColor(0X88336699);
            }
        }
    }
}
