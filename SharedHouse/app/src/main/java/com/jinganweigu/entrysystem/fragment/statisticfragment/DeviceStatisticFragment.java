package com.jinganweigu.entrysystem.fragment.statisticfragment;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.adapter.DeviceStatisticsDataAdapter;
import com.jinganweigu.entrysystem.common.BaseFragment2;
import com.jinganweigu.entrysystem.entry.DeviceStatisticsBean;
import com.jinganweigu.entrysystem.entry.DeviceStatisticsChartBean;
import com.jinganweigu.entrysystem.utils.HttpCode;
import com.jinganweigu.entrysystem.utils.ToastUtil;
import com.jinganweigu.entrysystem.utils.Url;
import com.jinganweigu.entrysystem.utils.chartutils.CustomerValueFormatter;
import com.jinganweigu.entrysystem.utils.http.HttpTool;
import com.jinganweigu.entrysystem.utils.http.SMObjectCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/3 0003.
 */

public class DeviceStatisticFragment extends BaseFragment2 implements OnChartValueSelectedListener {


    private PieChart mPieChart;
    private ArrayList<String> xValues = new ArrayList<>();
    ArrayList<Entry> entries = new ArrayList<Entry>();
    private List<DeviceStatisticsBean.ResultBean> dataList = new ArrayList<>();
    private DeviceStatisticsDataAdapter adapter;
    private ListView lvDevice;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.fragment_statistic_device, container, false);

        //饼状图
        mPieChart = (PieChart) view.findViewById(R.id.piechart);
        lvDevice = (ListView) view.findViewById(R.id.lv_device);

        mPieChart.setUsePercentValues(false);//使用百分比显示
//        mPieChart.
//        mPieChart.getDescription().setEnabled(false);
        mPieChart.setDescription("");
        mPieChart.setExtraOffsets(5, 10, 5, 5); //设置pieChart图表上下左右的偏移，类似于外边距

        mPieChart.setDragDecelerationFrictionCoef(0.95f);//设置pieChart图表转动阻力摩擦系数[0,1]
        //设置中间文件
//        mPieChart.setCenterText(generateCenterSpannableText());

        mPieChart.setDrawHoleEnabled(true); //是否显示PieChart内部圆环(true:下面属性才有意义)
        mPieChart.setHoleColor(Color.WHITE); //设置PieChart内部圆的颜色

        mPieChart.setTransparentCircleColor(Color.WHITE);//设置PieChart内部透明圆与内部圆间距(31f-28f)填充颜色
        mPieChart.setTransparentCircleAlpha(110);//设置PieChart内部透明圆与内部圆间距(31f-28f)透明度[0~255]数值越小越透明

        mPieChart.setHoleRadius(60f);//设置PieChart内部圆的半径(这里设置28.0f)
        mPieChart.setTransparentCircleRadius(40f);//设置PieChart内部透明圆的半径(这里设置31.0f)

        mPieChart.setDrawCenterText(true); //是否绘制PieChart内部中心文本（true：下面属性才有意义）

        mPieChart.setRotationAngle(0); //设置pieChart图表起始角度
        // 触摸旋转
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);//设置piecahrt图表点击Item高亮是否可用

        //变化监听
        mPieChart.setOnChartValueSelectedListener(this);

        //获取数据
        getDeviceStatisticDataChart();


        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // 输入标签样式
//        mPieChart.setEntryLabelColor(Color.WHITE);
//        mPieChart.setEntryLabelTextSize(12f);


        getDeviceStatisticsData();

        return view;
    }

    @Override
    public void initData() {


    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (hidden) {


        } else {

            getDeviceStatisticDataChart();

            getDeviceStatisticsData();

        }


    }


    //设置数据
    private void setData(ArrayList<Entry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(0f); //设置饼状Item之间的间隙
        dataSet.setSelectionShift(5f); //设置饼状Item被选中时变化的距离

        //xVals用来表示每个饼块上的内容


        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
        colors.add(Color.parseColor("#86d9e1"));
//        for (int c : ColorTemplate.JOYFUL_COLORS)
        colors.add(Color.parseColor("#e086ce"));
//        for (int c : ColorTemplate.COLORFUL_COLORS)
        colors.add(Color.parseColor("#7c7fce"));
//        for (int c : ColorTemplate.LIBERTY_COLORS)
        colors.add(Color.parseColor("#97e9a1"));
//        for (int c : ColorTemplate.PASTEL_COLORS)
        colors.add(Color.parseColor("#5f95f7"));
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(xValues, dataSet);
        data.setValueFormatter(new CustomerValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mPieChart.setData(data);
        mPieChart.highlightValues(null);
        //刷新
        mPieChart.invalidate();
    }

    //设置中间文字
//    private SpannableString generateCenterSpannableText() {
//        //原文：MPAndroidChart\ndeveloped by Philipp Jahoda
//        SpannableString s = new SpannableString("刘某人程序员\n我仿佛听到有人说我帅");
//        //s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
//        //s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
//        // s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
//        //s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
//        // s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
//        // s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
//        return s;
//    }
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {


//        ToastUtil.showToast("点击了===》" + xValues.get(e.getXIndex()) + "==dataSetIndex==>" + dataSetIndex, getActivity());

    }

    @Override
    public void onNothingSelected() {

    }


    private void getDeviceStatisticDataChart() {

        HttpTool.getInstance(getActivity()).http(Url.DEVICE_STATISTICS_CHARTS_DATA, new SMObjectCallBack<DeviceStatisticsChartBean>() {

            @Override
            public void onSuccess(DeviceStatisticsChartBean Bean) {

                if (Bean.getCode() == HttpCode.REQUEST_SUCCESS) {

                    entries.clear();
                    xValues.clear();

                    for (int i = 0; i < Bean.getResult().size(); i++) {

                        entries.add(new Entry(Float.valueOf(Bean.getResult().get(i).getTotal()), i));//一键报警
                        xValues.add(Bean.getResult().get(i).getMitu_type());


                    }
                    //设置数据
                    setData(entries);

                } else {


                    ToastUtil.showToast(Bean.getMsg(), getActivity());

                }


            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }


    private void getDeviceStatisticsData() {

        HttpTool.getInstance(getActivity()).http(Url.DEVICE_STATISTICS_DATA, new SMObjectCallBack<DeviceStatisticsBean>() {


            @Override
            public void onSuccess(DeviceStatisticsBean bean) {

                if (bean.getCode() == HttpCode.REQUEST_SUCCESS) {
                    dataList.clear();
                    dataList = bean.getResult();
                    adapter = new DeviceStatisticsDataAdapter(getActivity(), dataList);
                    lvDevice.setAdapter(adapter);


                } else {


                    ToastUtil.showToast(bean.getMsg(), getActivity());


                }


            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }


}
