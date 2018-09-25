package com.jinganweigu.entrysystem.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.common.BaseActivity;
import com.jinganweigu.entrysystem.utils.MyApplication;
import com.jinganweigu.entrysystem.utils.chartutils.CustomerPercentFormatter;
import com.jinganweigu.entrysystem.utils.chartutils.CustomerValueFormatter;
import com.jinganweigu.entrysystem.utils.chartutils.MyYValueFormatter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends BaseActivity implements OnChartValueSelectedListener {


    protected String[] mMonths = new String[]{
            "一键报警", "水压水位", "电气检测", "火焰探测", "车载报警"};
    protected BarChart mChart;
    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.fl_title)
    FrameLayout flTitle;
    private Typeface mTf;

    @Override
    public void initViews() {

        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        tvSave.setVisibility(View.GONE);
        topName.setText("详细信息");
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary), true);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentMethod.rebackMethod(DetailActivity.this);

            }
        });




    }

    @Override
    public void initData() {

        mChart = (BarChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);
        mChart.getAxisRight().setEnabled(false);//右侧刻度线

        mTf = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);
        xAxis.setDrawGridLines(false);//设置网格线

        MyYValueFormatter custom = new MyYValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(10, false);// 设置y轴的标签数量。 请注意，这个数字是不固定 if(force == false)，只能是近似的。 如果if(force == true)，则确切绘制指定数量的标签，但这样可能导致轴线分布不均匀。
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinValue(0f); // t 设置该轴的自定义最小值。 如果设置了，这个值将不会是根据提供的数据计算出来的。
        leftAxis.setAxisMaxValue(200f);// 设置该轴的最大值。 如果设置了，这个值将不会是根据提供的数据计算出来的。s

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(15, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(10f);//设置图表中的最高值的顶部间距占最高值的值的百分比（设置的百分比 = 最高柱顶部间距/最高柱的值）。默认值是10f，即10% 。
        rightAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);//设置图例的位置
        l.setForm(Legend.LegendForm.SQUARE);//设置图例形状， SQUARE(方格) CIRCLE（圆形） LINE（线性）
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        setData(5, 40);


    }

    @Override
    public void initEvents() {

    }


    private void setData(int count, float range) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add(mMonths[i]);
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

//        for (int i = 0; i < count; i++) {
//            float mult = (range + 1);
//            float val = (float) (Math.random() * mult);
//            yVals1.add(new BarEntry(val, i));
//        }


        for (int i = 0; i < 5; i++) {//几个柱状图
            float mult = (100);
            float val1 = (float) mult / 8;
            float val2 = (float) mult / 6;
            float val3 = (float) mult / 5;
            yVals1.add(new BarEntry(new float[]{val1, val2, val3}, i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "");
        set1.setBarSpacePercent(60f);//柱状图宽度，越大越细
        set1.setColors(getColors());

        set1.setStackLabels(new String[]{"退货", "安装", "待安装"});

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
//        data.setGroupSpace(100);
        data.setValueFormatter(new CustomerPercentFormatter(data));
        data.setValueTextSize(10f);
        data.setValueTypeface(mTf);
        mChart.setData(data);
        mChart.setVisibleXRangeMaximum(5);//屏幕中柱状图数量
    }

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;

        RectF bounds = mChart.getBarBounds((BarEntry) e);
        PointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);

//        Log.i("bounds", bounds.toString());
//        Log.i("position", position.toString());
//
//        Log.i("x-index",
//                "low: " + mChart.getLowestVisibleXIndex() + ", high: "
//                        + mChart.getHighestVisibleXIndex());
    }

    public void onNothingSelected() {
    }


    private int[] getColors() {
        int stacksize = 3;
        //有尽可能多的颜色每项堆栈值
        int[] colors = new int[stacksize];

        colors[0] = Color.parseColor("#86d9e1");
        colors[1] = Color.parseColor("#8083d4");
        colors[2] = Color.parseColor("#db66ad");
        return colors;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation

    }
}
