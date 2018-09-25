package com.jinganweigu.entrysystem.fragment.statisticfragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.adapter.EnterStatisticAdapter;
import com.jinganweigu.entrysystem.common.BaseFragment2;
import com.jinganweigu.entrysystem.entry.OutIntoStatisticBean;
import com.jinganweigu.entrysystem.utils.HttpCode;
import com.jinganweigu.entrysystem.utils.ToastUtil;
import com.jinganweigu.entrysystem.utils.Url;
import com.jinganweigu.entrysystem.utils.chartutils.CustomerPercentFormatter;
import com.jinganweigu.entrysystem.utils.chartutils.MyYValueFormatter;
import com.jinganweigu.entrysystem.utils.http.HttpTool;
import com.jinganweigu.entrysystem.utils.http.SMObjectCallBack;
import com.lidroid.xutils.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/3/3 0003.
 */

public class OutGoingStatisticFragment extends BaseFragment2 implements OnChartValueSelectedListener {

    protected String[] mMonths = new String[]{
            "入库", "出库", "退货"};
    protected BarChart mChart;
    @BindView(R.id.lv_device)
    ListView lvDevice;
    Unbinder unbinder;
    @BindView(R.id.tv_time)
    TextView tvTime;
    Unbinder unbinder1;
    @BindView(R.id.tv_show)
    TextView tvShow;
    Unbinder unbinder2;
    private Typeface mTf;

    ListView lv;  //listView控件
    ArrayList<String> list = new ArrayList<String>();//数据源
    EnterStatisticAdapter adapter; //适配器

    ArrayList<BarEntry> yVals1 = new ArrayList<>();


    List<OutIntoStatisticBean.ResultBean> outintoList = new ArrayList<>();

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.fragment_statistic_outgoing, container, false);

        unbinder = ButterKnife.bind(this, view);
        mChart = (BarChart) view.findViewById(R.id.chart_device_statistic);
        mChart.setOnChartValueSelectedListener(this);

        lv = (ListView) view.findViewById(R.id.lv_device);

        //下面设置悬浮和头顶部分内容
        final View header2 = View.inflate(getActivity(), R.layout.see_along, null);//头部内容,一直显示的部分
        lv.addHeaderView(header2);//添加头部
        final LinearLayout invis = (LinearLayout) view.findViewById(R.id.invis);

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 1) {
                    invis.setVisibility(View.VISIBLE);
                } else {

                    invis.setVisibility(View.GONE);
                }
            }
        });

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerView pvTime = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date2, View v) {//选中事件回调
                        String time = getTime(date2);
                        tvTime.setText(time);

                        String year = getYearTime(date2);
                        String month = getMonthTime(date2);

                        getStatisticsData(year, month);

                    }
                })
                        .setType(TimePickerView.Type.YEAR_MONTH)//默认全部显示
                        .setCancelText("取消")//取消按钮文字
                        .setSubmitText("确定")//确认按钮文字
                        .setContentSize(20)//滚轮文字大小
                        .setTitleSize(20)//标题文字大小
//                        .setTitleText("请选择时间")//标题文字
                        .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                        .isCyclic(true)//是否循环滚动
                        .setTextColorCenter(Color.BLACK)//设置选中项的颜色
                        .setTitleColor(Color.BLACK)//标题文字颜色
                        .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                        .setCancelColor(Color.BLUE)//取消按钮文字颜色
//                        .setTitleBgColor(0xFF666666)//标题背景颜色 Night mode
//                        .setBgColor(0xFF333333)//滚轮背景颜色 Night mode
//                        .setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR) + 20)//默认是1900-2100年
//                        .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
//                        .setRangDate(startDate,endDate)//起始终止年月日设定
//                        .setLabel("年","月","日","时","分","秒")
                        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                        .isDialog(true)//是否显示为对话框样式
                        .build();
                pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                pvTime.show();


            }
        });


        tvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {







            }
        });


        Date date=new Date();

        tvTime.setText(getTime(date));





        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if(hidden){


        }else{

            setCharData();

        }


    }






    @Override
    public void initData() {

      setCharData();

    }



    private void setCharData(){
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(false);

        mChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);
        mChart.getAxisRight().setEnabled(false);//右侧刻度线

        mTf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);
        xAxis.setDrawGridLines(false);//设置网格线

        YAxisValueFormatter custom = new MyYValueFormatter();

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
        rightAxis.setLabelCount(10, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(10f);//设置图表中的最高值的顶部间距占最高值的值的百分比（设置的百分比 = 最高柱顶部间距/最高柱的值）。默认值是10f，即10% 。
        rightAxis.setAxisMinValue(200f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);//设置图例的位置
        l.setForm(Legend.LegendForm.SQUARE);//设置图例形状， SQUARE(方格) CIRCLE（圆形） LINE（线性）
        l.setEnabled(false);//设置 图例是否显示
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
//         l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
//         "def", "ghj" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        Date date = new Date();



        getStatisticsData(getYearTime(date), getMonthTime(date));

        for (int i = 0; i < 3; i++) {
            float mult = (15 + 1);
            float val = (float) (mult);
            yVals1.add(new BarEntry(val, i));
        }

        setData(yVals1,3, 40);





    }




    private void setData(ArrayList<BarEntry> yVals1,int count, float range) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add(mMonths[i]);
        }


//        for (int i = 0; i < count; i++) {
//            float mult = (range + 1);
//            float val = (float) (mult);
//            yVals1.add(new BarEntry(val, i));
//        }


        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setBarSpacePercent(60f);//柱状图宽度，越大越细
//            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "");
            set1.setBarSpacePercent(60f);//柱状图宽度，越大越细
//            set1.setDrawIcons(false);

            set1.setColors(getColors());

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(xVals, dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(mTf);
//            data.setBarWidth(0.9f);

            mChart.setData(data);
        }


    }



//        BarDataSet set1 = new BarDataSet(yVals1, "");
//
//        set1.setColors(getColors());
////        set1.setStackLabels(new String[]{"a","b","c"});
////        set1.setStackLabels(new String[]{"及格", "优秀", "不及格"});
//
//        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
//        dataSets.add(set1);
//
//        BarData data = new BarData(xVals, dataSets);
////        data.setGroupSpace(100);
//        data.setValueFormatter(new CustomerPercentFormatter(data));
//        data.setValueTextSize(10f);
//        data.setValueTypeface(mTf);
//        mChart.setData(data);
//        mChart.setVisibleXRangeMaximum(3);//屏幕中柱状图数量


//    }
//
//    @SuppressLint("NewApi")
//    @Override
//    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
//
//        if (e == null)
//            return;
//
//        RectF bounds = mChart.getBarBounds((BarEntry) e);
//        PointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);
//
////        Log.i("bounds", bounds.toString());
////        Log.i("position", position.toString());
////
////        Log.i("x-index",
////                "low: " + mChart.getLowestVisibleXIndex() + ", high: "
////                        + mChart.getHighestVisibleXIndex());
//    }
//
//    public void onNothingSelected() {
//    }
//

    private int[] getColors() {
        int stacksize = 3;
        //有尽可能多的颜色每项堆栈值
        int[] colors = new int[stacksize];

        colors[0] = Color.parseColor("#8083d4");
        colors[1] = Color.parseColor("#db66ad");
        colors[2] = Color.parseColor("#86d9e1");
        return colors;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
        return format.format(date);
    }

    public String getYearTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(date);
    }

    public String getMonthTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("MM");
        return format.format(date);
    }


    private void getStatisticsData(String year, String month) {

        RequestParams params = new RequestParams();

        params.addBodyParameter("year", year);
        params.addBodyParameter("month", month);

        HttpTool.getInstance(getActivity()).httpWithParams(Url.GET_STATISTICS_OUT_AND_INTO_DATA, params, new SMObjectCallBack<OutIntoStatisticBean>() {

            @Override
            public void onSuccess(OutIntoStatisticBean outIntoStatisticBean) {


                if (outIntoStatisticBean.getCode() == HttpCode.REQUEST_SUCCESS) {

                    int in = 0;
                    int out = 0;
                    int ret = 0;

                    for (int i = 0; i < outIntoStatisticBean.getResult().size(); i++) {

                        in = in + outIntoStatisticBean.getResult().get(i).getWarehouse_in();

                        out = out + outIntoStatisticBean.getResult().get(i).getWarehouse_out();

                        ret = ret + outIntoStatisticBean.getResult().get(i).getWarehouse_return();

                    }

                    Log.e("outinto", "onSuccess: ===>" + in + "    ===out===>" + out + "    ====return===>" + ret);

                    yVals1.clear();
                    yVals1.add(new BarEntry((float) in, 0));
                    yVals1.add(new BarEntry((float) out, 1));
                    yVals1.add(new BarEntry((float) ret, 2));

//                    setData(yVals1,3, 40);



                    outintoList = outIntoStatisticBean.getResult();


                    adapter = new EnterStatisticAdapter(getActivity(), outintoList); //实例化适配器对象

                    lv.setAdapter(adapter); //给ListView设置适配器




                    setData(yVals1,3,50);
//                mChart.notifyDataSetChanged();
                    mChart.invalidate();

                }


            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
