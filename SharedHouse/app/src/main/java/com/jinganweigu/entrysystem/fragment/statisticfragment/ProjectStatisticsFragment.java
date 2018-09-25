package com.jinganweigu.entrysystem.fragment.statisticfragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ListView;

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
import com.jinganweigu.entrysystem.adapter.ProjectDeviceStatisticAdapter;
import com.jinganweigu.entrysystem.common.BaseFragment2;
import com.jinganweigu.entrysystem.entry.ProjectStaticPicBean;
import com.jinganweigu.entrysystem.entry.ProjectStatisticListBean;
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

public class ProjectStatisticsFragment extends BaseFragment2 implements OnChartValueSelectedListener {

    protected HorizontalBarChart mChartRight;
    private Typeface tf;
    protected String[] Months = new String[]{
            " 单位", "一键报警", " 水压水位", "电气检测", "火焰探测", "车载报警"};

    private Float[] floats = new Float[6];


    private List<ProjectStatisticListBean.ResultBean> lists;
    private ListView lvProject;
    private ProjectDeviceStatisticAdapter adapter;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.fragment_statistic_project, container, false);
        lvProject = (ListView) view.findViewById(R.id.lv_project);
        mChartRight = (HorizontalBarChart) view.findViewById(R.id.chart_right);

        getDataPicStatistics();
        getDataProjectListData();//列表数据

        RightChartSet();


        return view;
    }

    @Override
    public void initData() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){

            getDataPicStatistics();
            getDataProjectListData();//列表数据

            RightChartSet();


        }


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
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);//x轴显示位置
        xl.setTypeface(tf);
        xl.setDrawLabels(true);//设置标签是否显示
        xl.setDrawAxisLine(false);
        xl.setDrawGridLines(false);
        xl.setGridLineWidth(0.3f);
        xl.setAvoidFirstLastClipping(true);

        YAxis yl = mChartRight.getAxisLeft();
        yl.setTypeface(tf);
        yl.setDrawLabels(false);
        yl.setDrawAxisLine(false);
        yl.setDrawGridLines(false);
//        yl.setInverted(true);//反转该轴，如果为true，最大值在底部，顶部是最小值。
        yl.setGridLineWidth(0.3f);
        yl.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        yl.setAxisMaxValue(100f);
        YAxis yr = mChartRight.getAxisRight();
        yr.setTypeface(tf);
        yr.setDrawLabels(false);
        yr.setDrawAxisLine(false);
        yr.setDrawGridLines(false);
//        yr.setInverted(true);//反转该轴，如果为true，最大值在底部，顶部是最小值。
        yr.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        yr.setAxisMaxValue(100f);

//        setDataRight(6, 50);


        mChartRight.animateY(1000);
        Legend l = mChartRight.getLegend();
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        l.setFormSize(13f);
        l.setXEntrySpace(4f);
        l.setEnabled(false);
    }


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }


    private void setDataRight(int count, float range) {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count; i++) {
            xVals.add(Months[i % 6]);
            float mult = (100);
            float val1 = (float) floats[i];
//            float val2 = (float)  mult / 6;

            yVals1.add(new BarEntry(val1, i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "DataSet 1");
        set1.setBarSpacePercent(60f);
        set1.setColor(Color.parseColor("#db66ad"));
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
        data.setValueTypeface(tf);

        mChartRight.setData(data);
    }


    private void getDataPicStatistics() {


        HttpTool.getInstance(getActivity()).http(Url.GET_STATISTICS_PROJECT_DATA, new SMObjectCallBack<ProjectStaticPicBean>() {

            @Override
            public void onSuccess(ProjectStaticPicBean projectStaticPicBean) {

                if (projectStaticPicBean.getCode() == HttpCode.REQUEST_SUCCESS) {
                    floats[0] = Float.valueOf(projectStaticPicBean.getResult().get(0).getCompany());//单位
                    floats[1] = Float.valueOf(projectStaticPicBean.getResult().get(0).getAlarm());//一键报警
                    floats[2] = Float.valueOf(projectStaticPicBean.getResult().get(0).getWater());//水位水压
                    floats[3] = Float.valueOf(projectStaticPicBean.getResult().get(0).getGas_sensing());//电气检测
                    floats[4] = Float.valueOf(projectStaticPicBean.getResult().get(0).getFlame());//火焰探测
                    floats[5] = Float.valueOf(projectStaticPicBean.getResult().get(0).getVehicular_unit());//车载报警

                    setDataRight(6, 50);

                    mChartRight.invalidate();


                } else {

                    ToastUtil.showToast(projectStaticPicBean.getMsg(), getActivity());

                }


            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }

    private void getDataProjectListData() {

        HttpTool.getInstance(getActivity()).http(Url.GET_STATISTICS_PROJECT_LIST_DATA, new SMObjectCallBack<ProjectStatisticListBean>() {

            @Override
            public void onSuccess(ProjectStatisticListBean projectStatisticListBean) {

                if (projectStatisticListBean.getCode() == HttpCode.REQUEST_SUCCESS) {

                    lists.clear();

                    lists = projectStatisticListBean.getResult();

                    adapter = new ProjectDeviceStatisticAdapter(getActivity(), lists);
                    lvProject.setAdapter(adapter);


//                    adapter.notifyDataSetChanged();

                } else {

                    ToastUtil.showToast(projectStatisticListBean.getMsg(), getActivity());

                }


            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }


}
