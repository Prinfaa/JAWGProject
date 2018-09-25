package com.jinganweigu.entrysystem.fragment.statisticfragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.adapter.ReturnBackDeviceAdapter;
import com.jinganweigu.entrysystem.common.BaseFragment2;
import com.jinganweigu.entrysystem.entry.ReturnBackDeviceBean;
import com.jinganweigu.entrysystem.utils.HttpCode;
import com.jinganweigu.entrysystem.utils.ToastUtil;
import com.jinganweigu.entrysystem.utils.Url;
import com.jinganweigu.entrysystem.utils.chartutils.CustomerValueFormatter;
import com.jinganweigu.entrysystem.utils.http.HttpTool;
import com.jinganweigu.entrysystem.utils.http.SMObjectCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ReturnBackStatisticsFragment extends BaseFragment2 implements OnChartValueSelectedListener {


    @BindView(R.id.tv_device_all)
    TextView tvDeviceAll;
    @BindView(R.id.tv_good_return)
    TextView tvGoodReturn;
    @BindView(R.id.tv_bad_return)
    TextView tvBadReturn;
    Unbinder unbinder;
    @BindView(R.id.lv_reBack_device)
    ListView lvReBackDevice;
    private PieChart mPieChart;
    private ArrayList<String> xValues;
    ArrayList<Entry> entries = new ArrayList<Entry>();

    private ReturnBackDeviceAdapter adapter;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.fragment_return_back_statistics, container, false);
        unbinder = ButterKnife.bind(this, view);

        //饼状图
        mPieChart = (PieChart) view.findViewById(R.id.piechart);
//        lvDevice = (ListView) view.findViewById(R.id.lv_device);

        mPieChart.setUsePercentValues(false);//使用百分比显示
//        mPieChart.
//        mPieChart.getDescription().setEnabled(false);
        mPieChart.setDescription("");
        mPieChart.setExtraOffsets(5, 10, 5, 5); //设置pieChart图表上下左右的偏移，类似于外边距

        mPieChart.setDragDecelerationFrictionCoef(0.95f);//设置pieChart图表转动阻力摩擦系数[0,1]
        //设置中间文件
//        mPieChart.setCenterText(generateCenterSpannableText());

        mPieChart.setDrawHoleEnabled(false); //是否显示PieChart内部圆环(true:下面属性才有意义)
//        mPieChart.setHoleColor(Color.WHITE); //设置PieChart内部圆的颜色

//        mPieChart.setTransparentCircleColor(Color.WHITE);//设置PieChart内部透明圆与内部圆间距(31f-28f)填充颜色
        mPieChart.setTransparentCircleAlpha(110);//设置PieChart内部透明圆与内部圆间距(31f-28f)透明度[0~255]数值越小越透明

        mPieChart.setHoleRadius(0f);//设置PieChart内部圆的半径(这里设置28.0f)
        mPieChart.setTransparentCircleRadius(40f);//设置PieChart内部透明圆的半径(这里设置31.0f)

//        mPieChart.setDrawCenterText(true); //是否绘制PieChart内部中心文本（true：下面属性才有意义）

        mPieChart.setRotationAngle(0); //设置pieChart图表起始角度
        // 触摸旋转
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);//设置piecahrt图表点击Item高亮是否可用

        //变化监听
        mPieChart.setOnChartValueSelectedListener(this);

        //获取数据
//        getDeviceStatisticDataChart();


        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setEnabled(false);//设置 图例是否显示
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

//        entries.add(new Entry(80, 0));//损坏退货
//        entries.add(new Entry(20, 1));//正常退货

//        setData(entries);

        GetReturnBackDeviceData();
        return view;
    }

    @Override
    public void initData() {

    }

    //设置数据
    private void setData(ArrayList<Entry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f); //设置饼状Item之间的间隙
        dataSet.setSelectionShift(5f); //设置饼状Item被选中时变化的距离
//        dataSet.setValueLinePart1OffsetPercentage(80f);//数据连接线距图形片内部边界的距离，为百分数
        //xVals用来表示每个饼块上的内容
        xValues = new ArrayList<>();

        xValues.add("损坏退货");
        xValues.add("正常退货");
//        xValues.add("火焰探测");
//        xValues.add("车载报警");
//        xValues.add("电气检测");

        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
        colors.add(Color.parseColor("#40a8ff"));
//        for (int c : ColorTemplate.JOYFUL_COLORS)
        colors.add(Color.parseColor("#45d687"));
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//        colors.add(Color.parseColor("#7c7fce"));
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//        colors.add(Color.parseColor("#97e9a1"));
//        for (int c : ColorTemplate.PASTEL_COLORS)
//        colors.add(Color.parseColor("#5f95f7"));
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(xValues, dataSet);
        data.setValueFormatter(new CustomerValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mPieChart.setData(data);
        mPieChart.highlightValues(null);
        //刷新
        mPieChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private void GetReturnBackDeviceData() {

        HttpTool.getInstance(getActivity()).http(Url.GET_STATISTICS_RETURN_BACK_DATA, new SMObjectCallBack<ReturnBackDeviceBean>() {

            @Override
            public void onSuccess(ReturnBackDeviceBean returnBackDeviceBean) {

                if (returnBackDeviceBean.getCode() == HttpCode.REQUEST_SUCCESS) {

                    tvDeviceAll.setText("退货统计：" + returnBackDeviceBean.getResult().getTotal().get(0).getAll());
                    tvBadReturn.setText("损坏退货：" + returnBackDeviceBean.getResult().getTotal().get(0).getDamages());
                    tvGoodReturn.setText("正常退货：" + returnBackDeviceBean.getResult().getTotal().get(0).getNormals());

                    float bad = Float.valueOf(returnBackDeviceBean.getResult().getTotal().get(0).getDamages());
                    float good = Float.valueOf(returnBackDeviceBean.getResult().getTotal().get(0).getNormals());

                    entries.add(new Entry(bad, 0));
                    entries.add(new Entry(good, 1));

                    setData(entries);

                    List<ReturnBackDeviceBean.ResultBean.ParticularBean> list = new ArrayList<>();

                   list=returnBackDeviceBean.getResult().getParticular();

                    adapter = new ReturnBackDeviceAdapter(getActivity(), list);

                    lvReBackDevice.setAdapter(adapter);


                } else {

                    ToastUtil.showToast(returnBackDeviceBean.getMsg(), getActivity());

                }


            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
