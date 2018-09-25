package com.jinganweigu.entrysystem.fragment.statisticfragment;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
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
import com.jinganweigu.entrysystem.adapter.PersonStaisticsAdapter;
import com.jinganweigu.entrysystem.common.BaseFragment2;
import com.jinganweigu.entrysystem.entry.PersonStatisticsBean;
import com.jinganweigu.entrysystem.utils.HttpCode;
import com.jinganweigu.entrysystem.utils.ToastUtil;
import com.jinganweigu.entrysystem.utils.Url;
import com.jinganweigu.entrysystem.utils.http.HttpTool;
import com.jinganweigu.entrysystem.utils.http.SMObjectCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/3 0003.
 */

public class PersonStatisticFragment extends BaseFragment2 implements OnChartValueSelectedListener {

    protected HorizontalBarChart mChartLeft;
    protected HorizontalBarChart mChartRight;
    private TextView tvX, tvY;
    protected List<String> mMonths = new ArrayList<>();
    protected List<String> RMonths = new ArrayList<>();

    private List<PersonStatisticsBean.ResultBean.FirstBean> datas = new ArrayList<>();
    private Typeface tf;
    private PersonStaisticsAdapter adapter;
    private ListView lvPerson;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_statistic_person, container, false);

        lvPerson= (ListView) view.findViewById(R.id.lv_person);
        mChartLeft = (HorizontalBarChart) view.findViewById(R.id.chart_left);


        mChartRight = (HorizontalBarChart) view.findViewById(R.id.chart_right);


        leftChartsSet();
        RightChartSet();

        getDataFromOnline();
        return view;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if(!hidden){


            getDataFromOnline();

        }


    }

    private void leftChartsSet() {


        mChartLeft.setOnChartValueSelectedListener(this);
        mChartLeft.setDrawBarShadow(false);
        mChartLeft.setDrawValueAboveBar(true);
        mChartLeft.setDescription("");
        mChartLeft.setMaxVisibleValueCount(100);
        mChartLeft.setPinchZoom(false);
        mChartLeft.setDrawGridBackground(false);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
//        mChartLeft.setViewPortOffsets(0,0,0,0);
        XAxis xl = mChartLeft.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.TOP);//x轴显示位置
        xl.setTypeface(tf);
        xl.setDrawLabels(true);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGridLineWidth(0.3f);
        xl.setAvoidFirstLastClipping(true);
//        xl.setXOffset(10);

        YAxis yl = mChartLeft.getAxisLeft();
        yl.setTypeface(tf);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setInverted(true);//反转该轴，如果为true，最大值在底部，顶部是最小值。
        yl.setGridLineWidth(0.3f);
        yl.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        yl.setAxisMaxValue(100f);
        YAxis yr = mChartLeft.getAxisRight();
        yr.setTypeface(tf);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(true);
        yr.setInverted(true);//反转该轴，如果为true，最大值在底部，顶部是最小值。
        yr.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        yr.setAxisMaxValue(100f);

//        setDataLeft(7, 50);


        mChartLeft.animateY(2500);
        Legend l = mChartLeft.getLegend();
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        l.setFormSize(10f);
        l.setXEntrySpace(4f);
        l.setEnabled(false);
    }


    private void RightChartSet() {

        mChartRight.setOnChartValueSelectedListener(this);
        mChartRight.setDrawBarShadow(false);
        mChartRight.setDrawValueAboveBar(true);
        mChartRight.setDescription("");
        mChartRight.setMaxVisibleValueCount(100);
        mChartRight.setPinchZoom(false);
        mChartRight.setDrawGridBackground(false);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
//        mChartRight.setViewPortOffsets(20,0,0,0);
        XAxis xl = mChartRight.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTH_SIDED);//x轴显示位置
        xl.setTypeface(tf);
        xl.setDrawLabels(false);//设置标签是否显示
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGridLineWidth(0.3f);
        xl.setAvoidFirstLastClipping(true);

        YAxis yl = mChartRight.getAxisLeft();
        yl.setTypeface(tf);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
//        yl.setInverted(true);//反转该轴，如果为true，最大值在底部，顶部是最小值。
        yl.setGridLineWidth(0.3f);
        yl.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        yl.setAxisMaxValue(100f);
        YAxis yr = mChartRight.getAxisRight();
        yr.setTypeface(tf);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(true);
//        yr.setInverted(true);//反转该轴，如果为true，最大值在底部，顶部是最小值。
        yr.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        yr.setAxisMaxValue(100f);

//        setDataRight(7, 50);


        mChartRight.animateY(1000);
        Legend l = mChartRight.getLegend();
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        l.setFormSize(10f);
        l.setXEntrySpace(4f);
        l.setEnabled(false);
    }


    private void setDataLeft() {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();

        yVals1.clear();
        xVals.clear();

        for (int i = 0; i < datas.size(); i++) {
            xVals.add(datas.get(i).getWorker_name());
            float val1 = Float.valueOf(datas.get(i).getSend_back());//退回
            float val2 = Float.valueOf(datas.get(i).getAll());//入库

            yVals1.add(new BarEntry((new float[]{val1, val2}), i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "DataSet 1");
        set1.setBarSpacePercent(60f);
        set1.setColors(getColors());
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
        data.setValueTypeface(tf);
        mChartLeft.setData(data);
    }

    private void setDataRight() {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();

        yVals1.clear();
        xVals.clear();

        for (int i = 0; i < datas.size(); i++) {
            xVals.add(" ");
            float val1 = Float.valueOf(datas.get(i).getInstall());//安装
            float val2 = Float.valueOf(datas.get(i).getUninstall());//带安装

            yVals1.add(new BarEntry((new float[]{val1, val2}), i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "DataSet 1");
        set1.setBarSpacePercent(60f);
        set1.setColors(getColors());
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
        data.setValueTypeface(tf);

        mChartRight.setData(data);
    }


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        if (e == null)
            return;

        RectF bounds = mChartLeft.getBarBounds((BarEntry) e);
        PointF position = mChartLeft.getPosition(e, mChartLeft.getData().getDataSetByIndex(dataSetIndex)
                .getAxisDependency());

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());
    }

    @Override
    public void onNothingSelected() {

    }

    private int[] getColors() {
        int stacksize = 2;
        //有尽可能多的颜色每项堆栈值
        int[] colors = new int[stacksize];

        colors[0] = Color.parseColor("#db66ad");
        colors[1] = Color.parseColor("#8083d4");

        return colors;
    }

    @Override
    public void initData() {


    }









    private void getDataFromOnline() {

        HttpTool.getInstance(getActivity()).http(Url.GET_STATISTICS_PERSON_DATA, new SMObjectCallBack<PersonStatisticsBean>() {

            @Override
            public void onSuccess(PersonStatisticsBean Bean) {

                if (Bean.getCode() == HttpCode.REQUEST_SUCCESS) {

                    datas.clear();
                    datas = Bean.getResult().getFirst();

                    setDataLeft();
                    setDataRight();

                    adapter=new PersonStaisticsAdapter(datas,getActivity());

                    lvPerson.setAdapter(adapter);

                } else {

                    ToastUtil.showToast(Bean.getMsg(), getActivity());


                }


            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }


}
