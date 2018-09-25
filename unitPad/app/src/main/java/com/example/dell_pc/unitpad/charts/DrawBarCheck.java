package com.example.dell_pc.unitpad.charts;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dell-pc on 2017/10/6.
 */
public class DrawBarCheck {

    public void draw(BarChart barChart) {

        barChart.setBackgroundColor(0X00FF0000);

        //设置条形数据
        barChart.setData(getBarData());
        //设置描述
        barChart.setDescription("每日消防巡查率历史统计");
        barChart.setDescriptionTextSize(30);
        barChart.setDescriptionColor(0XFFFFFFFF);
        barChart.setDescriptionPosition(1500, 50);
        //设置绘制bar的阴影
        barChart.setDrawBarShadow(false);
        //设置绘制的动画时间
        barChart.animateXY(1000, 1000);
        barChart.setDrawGridBackground(false);



        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(0XFFFFFFFF);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        Legend mLegend = barChart.getLegend(); //设置比例图
        mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER); //坐右边显示
        mLegend.setXEntrySpace(5f);
        mLegend.setYEntrySpace(90f);
        mLegend.setTextColor(0XFFFFFFFF);

    }

    public BarData getBarData() {
        int maxX = 10;
        int[] rate = {89,77,91,99,100,54,66,89,80,92};
        //创建集合，存放每个柱子的数据
        List<BarEntry> list = new ArrayList<>();
//        List<BarEntry> list2 = new ArrayList<>();
        for (int i = 0; i < maxX; i++) {
            //一个BarEntry就是一个柱子的数据对象
            BarEntry barEntry = new BarEntry(rate[i], i);
            list.add(barEntry);
//            BarEntry barEntry2 = new BarEntry(i + 3, i);
//            list2.add(barEntry2);
        }
        //创建BarDateSet对象，其实就是一组柱形数据
        BarDataSet barSet = new BarDataSet(list, "巡查率");
//        BarDataSet barSet2 = new BarDataSet(list2, "演练");

        //设置柱形的颜色
        barSet.setColor(0XAAFFFFFF);
//        barSet2.setColor(0X44000000);

        barSet.setValueTextColor(0XFFFFFFFF);
//        barSet2.setValueTextColor(0XFFFFFFFF);
        //设置是否显示柱子上面的数值


        barSet.setValueTextSize(10f);
//        barSet2.setValueTextSize(10f);

        barSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                String str = String.valueOf((int) v) + "%";
                return str;
            }
        });

        barSet.setBarSpacePercent(40);
//        barSet2.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
//                String str = String.valueOf((int) v) + "次";
//                return str;
//            }
//        });



        //设置柱子阴影颜色
//        barSet.setBarShadowColor(Color.GRAY);
        //创建集合，存放所有组的柱形数据
        List<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barSet);
//        dataSets.add(barSet2);
        ArrayList<String> xValues = new ArrayList<String>(); //用来表示每个饼块上的内容
//        String[] content = new String[]{"一季度", "二季度", "三季度", "四季度"};
        for (int i = 0; i < 10; i++) {

            SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
            Date curDate = new Date(System.currentTimeMillis() - 86400000 * (10 - i));//获取当前时间
            String str = formatter.format(curDate);
            xValues.add(str);
        }
        
        BarData barData = new BarData(xValues, dataSets);
        return barData;
    }
}