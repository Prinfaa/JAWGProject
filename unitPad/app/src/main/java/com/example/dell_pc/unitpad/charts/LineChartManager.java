package com.example.dell_pc.unitpad.charts;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

/**
 * Created by dell-pc on 2017/10/11.
 */
public class LineChartManager {

    private static String lineName1 = null;
    private static String lineName2 = null;
    private static String lineName3 = null;
    /**
     * @Description:创建两条折线
     * @param context 上下文
     * @param mLineChart 折线图控件
     * @param xValues 折线在x轴的值
     * @param yValue 折线在y轴的值
     */
    public static void initSingleLineChart(Context context, LineChart mLineChart, ArrayList<String> xValues,
                                           ArrayList<Entry> yValue) {
        initDataStyle(context,mLineChart);
        //设置折线的样式
        LineDataSet dataSet = new LineDataSet(yValue, lineName1);
        dataSet.setColor(Color.RED);
        dataSet.setCircleColor(Color.RED);
        dataSet.setDrawValues(false);
//        dataSet.setValueFormatter(new PercentFormatter(new DecimalFormat("%").format()));

        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        //构建一个LineData  将dataSets放入
        LineData lineData = new LineData(xValues, dataSets);
        //将数据插入
        mLineChart.setData(lineData);

        //设置动画效果
        mLineChart.animateY(1000, Easing.EasingOption.Linear);
        mLineChart.animateX(1000, Easing.EasingOption.Linear);
        mLineChart.invalidate();
    }
    /**
     * @Description:创建两条折线
     * @param context 上下文
     * @param mLineChart 折线图控件
     * @param xValues 折线在x轴的值
     * @param yValue1 折线在y轴的值
     * @param yValue2 另一条折线在y轴的值
     */
    public static void initThreeLineChart(Context context, LineChart mLineChart, ArrayList<String> xValues,
                                          ArrayList<Entry> yValue1, ArrayList<Entry> yValue2, ArrayList<Entry> yValue3) {

        initDataStyle(context,mLineChart);

        LineDataSet dataSet1 = new LineDataSet(yValue1, lineName1);
        dataSet1.setColor(Color.BLUE);
        dataSet1.setCircleColor(Color.BLUE);
        dataSet1.setDrawValues(false);

        LineDataSet dataSet2 = new LineDataSet(yValue2, lineName2);
        dataSet2.enableDashedLine(0f, 0f, 0f);//将折线设置为曲线
        dataSet2.setColor(Color.parseColor("#66CDAA"));
        dataSet2.setCircleColor(Color.parseColor("#66CDAA"));
        dataSet2.setDrawValues(false);

        LineDataSet dataSet3 = new LineDataSet(yValue3, lineName3);
        dataSet3.enableDashedLine(0f, 0f, 0f);//将折线设置为曲线
        dataSet3.setColor(Color.RED);
        dataSet3.setCircleColor(Color.RED);
        dataSet3.setDrawValues(true);


        //构建一个类型为LineDataSet的ArrayList 用来存放所有 y的LineDataSet   他是构建最终加入LineChart数据集所需要的参数
        ArrayList<LineDataSet> dataSets = new ArrayList<>();

        //将数据加入dataSets
        dataSets.add(dataSet1);
        dataSets.add(dataSet2);
        dataSets.add(dataSet3);

        //构建一个LineData  将dataSets放入
        LineData lineData = new LineData(xValues, dataSets);
        //将数据插入
        mLineChart.setData(lineData);
        //设置动画效果
        mLineChart.animateY(1000, Easing.EasingOption.Linear);
        mLineChart.animateX(1000, Easing.EasingOption.Linear);
        mLineChart.invalidate();
    }

    /**
     *  @Description:初始化图表的样式
     * @param context
     * @param mLineChart
     */
    private static void initDataStyle(Context context, LineChart mLineChart) {
        //设置图表是否支持触控操作
        mLineChart.setTouchEnabled(true);
        mLineChart.setScaleEnabled(true);
        mLineChart.setBackgroundColor(0XFFFFFFFF);
        //设置点击折线点时，显示其数值
//        MyMakerView mv = new MyMakerView(context, R.layout.item_mark_layout);
//        mLineChart.setMarkerView(mv);
        //设置折线的描述的样式（默认在图表的左下角）
        Legend title = mLineChart.getLegend();
        title.setForm(Legend.LegendForm.LINE);
        //设置x轴的样式
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(Color.parseColor("#336699"));
        xAxis.setAxisLineWidth(5);
        xAxis.setDrawGridLines(true);
        //设置是否显示x轴
        xAxis.setEnabled(true);

        //设置左边y轴的样式
        YAxis yAxisLeft = mLineChart.getAxisLeft();
        yAxisLeft.setAxisLineColor(Color.parseColor("#336699"));
        yAxisLeft.setAxisLineWidth(5);
        yAxisLeft.setDrawGridLines(false);

        //设置右边y轴的样式
        YAxis yAxisRight = mLineChart.getAxisRight();
        yAxisRight.setEnabled(false);

    }

    public static void initDoubleLineChart(Context context, LineChart mLineChart, ArrayList<String> xValues,
                                          ArrayList<Entry> yValue1, ArrayList<Entry> yValue2) {

        initDataStyle(context,mLineChart);

        LineDataSet dataSet1 = new LineDataSet(yValue1, lineName1);
        dataSet1.setColor(Color.RED);
        dataSet1.setCircleColor(Color.RED);
        dataSet1.setDrawValues(false);

        LineDataSet dataSet2 = new LineDataSet(yValue2, lineName2);
        dataSet2.enableDashedLine(0f, 0f, 0f);//将折线设置为曲线
        dataSet2.setColor(Color.parseColor("#66CDAA"));
        dataSet2.setCircleColor(Color.parseColor("#66CDAA"));
        dataSet2.setDrawValues(true);



        //构建一个类型为LineDataSet的ArrayList 用来存放所有 y的LineDataSet   他是构建最终加入LineChart数据集所需要的参数
        ArrayList<LineDataSet> dataSets = new ArrayList<>();

        //将数据加入dataSets
        dataSets.add(dataSet1);
        dataSets.add(dataSet2);

        //构建一个LineData  将dataSets放入
        LineData lineData = new LineData(xValues, dataSets);
        //将数据插入
        mLineChart.setData(lineData);
        //设置动画效果
        mLineChart.animateY(1000, Easing.EasingOption.Linear);
        mLineChart.animateX(1000, Easing.EasingOption.Linear);
        mLineChart.invalidate();
    }



    /**
     * @Description:设置折线的名称
     * @param name
     */
    public static void setLineName1(String name){
        lineName1 = name;
    }

    /**
     * @Description:设置另一条折线的名称
     * @param name
     */
    public static void setLineName2(String name){
        lineName2 = name;
    }

    /**
     * @Description:设置另一条折线的名称
     * @param name
     */
    public static void setLineName3(String name){
        lineName3 = name;
    }

}
