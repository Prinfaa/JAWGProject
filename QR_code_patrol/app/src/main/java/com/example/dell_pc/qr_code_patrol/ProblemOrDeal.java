package com.example.dell_pc.qr_code_patrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.dell_pc.qr_code_patrol.item.ShopProblemNotDealItem;

/**
 * Created by dell-pc on 2017/10/1.
 */
public class ProblemOrDeal extends Activity implements View.OnClickListener {
    private String unitID, unitName, cardNo, shopID, shopName, linkman, phone;
    private RadioGroup rgChose;
    private String objectType = "problem";
    private Button btnBack, btnNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.problem_or_deal);
        initData();
        initView();
        int problemCount = 0;
        for (int i = 0; i < Chose.shopProblemNotDealList.size(); i++) {

            ShopProblemNotDealItem shopProblemNotDealItem = Chose.shopProblemNotDealList.get(i);
            if (shopProblemNotDealItem.getShopID().equals(shopID)) {
                problemCount++;
            }
        }
        if (problemCount == 0) {
            objectType = "problem";
            nextPage();
        }

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
    }

    private void initView() {
        btnBack = (Button) findViewById(R.id.btnBack);
        btnNext = (Button) findViewById(R.id.btnNext);

        btnBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        rgChose = (RadioGroup) findViewById(R.id.rgChose);
        rgChose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbProblem) {
                    objectType = "problem";
                } else if (checkedId == R.id.rbDeal) {
                    objectType = "deal";
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            finish();
        } else if (v == btnNext) {
            nextPage();
        }
    }

    private void nextPage() {
        Intent intent = new Intent();
        intent.putExtra("unitID", unitID);
        intent.putExtra("unitName", unitName);
        intent.putExtra("code", cardNo);
        intent.putExtra("shopID", shopID);
        intent.putExtra("shopName", shopName);
        intent.putExtra("linkman", linkman);
        intent.putExtra("phone", phone);

        if (objectType.equals("problem")) {
            intent.setClass(ProblemOrDeal.this, ShopCheck.class);
        } else if (objectType.equals("deal")) {
            intent.setClass(ProblemOrDeal.this, ShopProblem.class);
        }
        startActivity(intent);
        finish();

    }
}
