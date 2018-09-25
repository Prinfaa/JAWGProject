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
import android.widget.Toast;

import com.example.dell_pc.unitpad.adapter.CheckAbnormalAdapter;
import com.example.dell_pc.unitpad.adapter.CheckNormalAdapter;
import com.example.dell_pc.unitpad.adapter.UnCheckAdapter;
import com.example.dell_pc.unitpad.charts.DrawBarCheck;
import com.example.dell_pc.unitpad.charts.DrawPieCheck;
import com.example.dell_pc.unitpad.tools.MyAnimation;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;


/**
 * Created by dell-pc on 2016/1/12.
 */
public class Check extends Activity implements View.OnClickListener {

    int year;

    String unitId, unitName;

    private PieChart pieCheck;
    private DrawPieCheck drawPieCheck = new DrawPieCheck();

    private BarChart barCheck;
    private DrawBarCheck drawBarCheck = new DrawBarCheck();

    private CheckAbnormalAdapter checkAbnormalAdapter;
    private CheckNormalAdapter CheckNormalAdapter;
    private ListView lvCheckNormal, lvCheckAbnormal;

    private UnCheckAdapter unCheckAdapter;
    private ListView lvUnCheck;
    private LinearLayout llCheckNormal, llCheckAbnormal;

    private TextView tvNormalFacTitle, tvAbnormalFacTitle, tvUnCheckedTitle;

    private MyAnimation myAnimation = new MyAnimation();

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check);

        initData();
        initView();


    }

    private void initData() {
        Intent intent = this.getIntent();
        unitId = intent.getStringExtra("unitID");

    }

    private void initView() {
        pieCheck = (PieChart) findViewById(R.id.pieCheck);

        int checkNormalCount = MainActivity.checkedNormalList.size();
        int checkAbnormalCount = MainActivity.checkAbnormalList.size();
        int unCheckCount = MainActivity.unCheckList.size();

        drawPieCheck.draw(pieCheck, Check.this, checkNormalCount, checkAbnormalCount, unCheckCount, true);

        lvCheckNormal = (ListView) findViewById(R.id.lvNoProblem);
        CheckNormalAdapter = new CheckNormalAdapter(this, MainActivity.checkedNormalList);
        lvCheckNormal.setAdapter(CheckNormalAdapter);
        CheckNormalAdapter.notifyDataSetChanged();

        lvCheckAbnormal = (ListView) findViewById(R.id.lvProblem);
        checkAbnormalAdapter = new CheckAbnormalAdapter(this, MainActivity.checkAbnormalList);
        lvCheckAbnormal.setAdapter(checkAbnormalAdapter);
        checkAbnormalAdapter.notifyDataSetChanged();

//        lvCheckNormal = (ListView) findViewById(R.id.lvUnCheck);

        lvUnCheck = (ListView) findViewById(R.id.lvUnCheck);
        unCheckAdapter = new UnCheckAdapter(this, MainActivity.unCheckList);
        lvUnCheck.setAdapter(unCheckAdapter);
        unCheckAdapter.notifyDataSetChanged();

        tvNormalFacTitle = (TextView) findViewById(R.id.tvNormalFacTitle);
        tvAbnormalFacTitle = (TextView) findViewById(R.id.tvAbnormalFacTitle);
        tvUnCheckedTitle = (TextView) findViewById(R.id.tvUnCheckedTitle);
        tvNormalFacTitle.setText("正常设备   " + String.valueOf(MainActivity.checkedNormalList.size()) + "处");
        tvAbnormalFacTitle.setText("不正常设备   " + String.valueOf(MainActivity.checkAbnormalList.size()) + "处");
        tvUnCheckedTitle.setText("本月未检查设备   " + String.valueOf(MainActivity.unCheckList.size()) + "处");

        tvNormalFacTitle.setOnClickListener(this);
        tvAbnormalFacTitle.setOnClickListener(this);

        llCheckNormal = (LinearLayout) findViewById(R.id.llCheckNormal);
        llCheckAbnormal = (LinearLayout) findViewById(R.id.llCheckAbnormal);

    }

    @Override
    public void onClick(View v) {
        if (v == tvAbnormalFacTitle) {
            if (llCheckNormal.getVisibility() == View.VISIBLE) {
                myAnimation.Go(llCheckNormal, 500, 0, 2000, 0, 0);
                myAnimation.Come(llCheckAbnormal, 500, 0, 0, 2000, 0);
                tvAbnormalFacTitle.setBackgroundColor(0X88339999);
                tvNormalFacTitle.setBackgroundColor(0XFF339999);

            }
        } else if (v == tvNormalFacTitle) {
            if (llCheckAbnormal.getVisibility() == View.VISIBLE) {
                myAnimation.Go(llCheckAbnormal, 500, 0, 0, 0, 2000);
                myAnimation.Come(llCheckNormal, 500, 2000, 0, 0, 0);
                tvAbnormalFacTitle.setBackgroundColor(0XFF339999);
                tvNormalFacTitle.setBackgroundColor(0X88339999);
            }
        }
    }
}
