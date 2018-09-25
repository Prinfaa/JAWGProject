package com.example.dell.unittv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.example.dell.unittv.adapter.PatroledAdapter;
import com.example.dell.unittv.adapter.UnPatrolAdapter;
import com.example.dell.unittv.charts.DrawBarPatrol;
import com.example.dell.unittv.charts.DrawPiePatrol;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;


/**
 * Created by dell-pc on 2016/1/12.
 */
public class Patrol extends Activity {

    int year;

    String unitId, unitName;

    private PieChart piePatrol;
    private DrawPiePatrol drawPiePatrol = new DrawPiePatrol();

    private BarChart barPatrol;
    private DrawBarPatrol drawBarPatrol = new DrawBarPatrol();

    private PatroledAdapter patroledAdapter;
    private ListView lvPatroled;

    private UnPatrolAdapter unPatrolAdapter;
    private ListView lvUnPatrol;

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patrol);

        initData();
        initView();

        Intent intent1 = this.getIntent();
        unitId = intent1.getStringExtra("unitID");

    }

    private void initData(){

    }

    private void initView(){
        piePatrol = (PieChart) findViewById(R.id.piePatrol);
        barPatrol = (BarChart) findViewById(R.id.barPatrol);

        drawBarPatrol.draw(barPatrol);

        int patrolCount = MainActivity.patroledItemList.size();
        int unPatrolCount = MainActivity.unPatrolItemList.size();

        drawPiePatrol.draw(piePatrol,Patrol.this,patrolCount,unPatrolCount,true);

        lvPatroled = (ListView) findViewById(R.id.lvPatroled);
        patroledAdapter = new PatroledAdapter(this,MainActivity.patroledItemList);
        lvPatroled.setAdapter(patroledAdapter);
        patroledAdapter.notifyDataSetChanged();

        lvUnPatrol = (ListView) findViewById(R.id.lvUnPatrol);
        unPatrolAdapter = new UnPatrolAdapter(this,MainActivity.unPatrolItemList);
        lvUnPatrol.setAdapter(unPatrolAdapter);
        unPatrolAdapter.notifyDataSetChanged();
    }

}
