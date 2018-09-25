package com.example.dell.unittv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


/**
 * Created by dell-pc on 2016/1/12.
 */
public class Chose extends Activity implements View.OnClickListener {

    String unitId, unitName;
    Button btnJRZJ, btnK88;


    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chose);

        initView();


    }


    private void initView() {
        btnJRZJ = (Button) findViewById(R.id.btnJRZJ);
        btnK88 = (Button) findViewById(R.id.btnK88);
        btnJRZJ.setOnClickListener(this);
        btnK88.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnJRZJ) {
            unitId = "233";
            Intent intent = new Intent(Chose.this, MainActivity.class);
            intent.putExtra("unitID", unitId);
            startActivity(intent);
        } else if (v == btnK88) {
            unitId = "328";
            Intent intent = new Intent(Chose.this, MainActivity.class);
            intent.putExtra("unitID", unitId);
            startActivity(intent);
        }
    }
}
