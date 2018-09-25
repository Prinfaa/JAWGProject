package com.example.dell.unittv.charts;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell-pc on 2017/10/5.
 */
public class DrawPieCheck {
    private Context context;
    private boolean isDetail;

    public void draw(PieChart pie, Context context, int arg1, int arg2, int arg3, boolean isDetail) {
        this.context = context;
        this.isDetail = isDetail;
        PieData mPieData = getPieData(3, 100, arg1, arg2, arg3);
        showPie(pie, mPieData);
    }

    private void showPie(PieChart pieChart, PieData pieData) {
        pieChart.setHoleColorTransparent(true);
        pieChart.setHoleRadius(40f); //半径
        pieChart.setTransparentCircleRadius(50f); //半透明圈
        pieChart.setDescription("");
        pieChart.setDrawHoleEnabled(true);
        pieChart.setRotationAngle(90); //初始角度
        pieChart.setRotationEnabled(true); //可以手动旋转
        pieChart.setUsePercentValues(false); //显示百分比
        pieChart.setDrawCenterText(true); //饼状图中间可以添加文字
        pieChart.setCenterText("");
        pieChart.setCenterTextColor(Color.GRAY);
        pieChart.setData(pieData);
        Legend mLegend = pieChart.getLegend(); //设置比例图
        mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER); //坐右边显示
        mLegend.setXEntrySpace(5f);
        mLegend.setYEntrySpace(100f);
        mLegend.setTextColor(0XFFFFFFFF);
        mLegend.setEnabled(false);
        pieChart.animateXY(1000, 1000);
        if(isDetail == true){
            mLegend.setEnabled(false);
        }
    }

    private PieData getPieData(int count, float range, int arg1, int arg2, int arg3) {
        ArrayList<String> xValues = new ArrayList<String>(); //用来表示每个饼块上的内容
        String[] content = new String[]{"合格", "不合格", "未检查"};
        for (int i = 0; i < count; i++) {
            xValues.add(content[i]);
        }
        ArrayList<Entry> yValue = new ArrayList<Entry>(); //用来表示封装每个饼块的实际数据

        List<Float> qs = new ArrayList<Float>();
        qs.add((float) arg1);
        qs.add((float) arg2);
        qs.add((float) arg3);

        for (int i = 0; i < qs.size(); i++) {
            yValue.add(new Entry(qs.get(i), i));
        }

        PieDataSet pieDataSet = new PieDataSet(yValue, "");
        pieDataSet.setSliceSpace(0f);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        //饼图颜色
        colors.add(0XFF229AFF);
        colors.add(0XFFDD0000);
        colors.add(0XFFEE1199);
        pieDataSet.setColors(colors); //设置颜色
        pieDataSet.setValueTextSize(12f);
        pieDataSet.setValueTextColor(Color.WHITE);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 280f);
        pieDataSet.setSelectionShift(0); //选中态多出的长度
        PieData pieData = new PieData(xValues, pieDataSet);
        pieData.setValueTextColor(0XFFFFFFFF);
        pieDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                String str = String.valueOf((int) v) + "处";
                return str;
            }
        });
        return pieData;
    }

}
