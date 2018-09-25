package com.example.dell_pc.qr_code_patrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;

/**
 * Created by dell-pc on 2017/10/1.
 */
public class FacOrShop extends Activity implements View.OnClickListener {
    private String unitID, unitName;
    private RadioGroup rgChose;
    private String objectType = "property";
    private Button btnBack, btnNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fac_or_shop);
        initData();
        initView();

    }

    private void initData() {
        Intent intent1 = this.getIntent();
        unitID = intent1.getStringExtra("unitID");
        unitName = intent1.getStringExtra("unitName");
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
                if(checkedId == R.id.rbProperty){
                    objectType = "property";
                }else if(checkedId == R.id.rbShop){
                    objectType = "shop";
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        if(v == btnBack){
            finish();
        }else if(v == btnNext){
            Intent intent = new Intent();
            intent.putExtra("unitID", unitID);
            intent.putExtra("unitName", unitName);
            intent.putExtra("objectType", objectType);
            intent.setClass(FacOrShop.this, ShopCheck.class);
            startActivity(intent);
            finish();
        }
    }
}
