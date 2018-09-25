package com.jinganweigu.RoadWayFire.Activities;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseActivity;
import com.jinganweigu.RoadWayFire.BeseClassUtils.MyApplication;
import com.jinganweigu.RoadWayFire.Entries.StatisticAlarmCount;
import com.jinganweigu.RoadWayFire.Entries.StatisticsData;
import com.jinganweigu.RoadWayFire.Interfaces.Mycontants;
import com.jinganweigu.RoadWayFire.Interfaces.Url;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.Sptools;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.ToastUtil;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.HttpTool;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.SMObjectCallBack;
import com.jinganweigu.RoadWayFire.ToolsUtils.ImageUtil.LoadImageUtil;
import com.jinganweigu.RoadWayFire.ToolsUtils.widget.BarChartManager;
import com.jinganweigu.RoadWayFire.ToolsUtils.widget.MyLineChart;
import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GeneralStatisticActivity extends BaseActivity {

    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.iv_company_logo)
    ImageView ivCompanyLogo;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.btn_change_date)
    Button btnChangeDate;
    @BindView(R.id.tv_alarm_num)
    TextView tvAlarmNum;
    @BindView(R.id.tv_alarm_time)
    TextView tvAlarmTime;
    @BindView(R.id.lineChart)
    MyLineChart mLineChart;
    @BindView(R.id.barchat)
    BarChart barChart;
    @BindView(R.id.tv_offline_device)
    TextView tvOfflineDevice;
    @BindView(R.id.tv_normal_device)
    TextView tvNormalDevice;
    @BindView(R.id.tv_line_chart_nodata)
    TextView tvLineChartNodata;
    @BindView(R.id.tv_bar_chart_nodata)
    TextView tvBarChartNodata;
    private ArrayList<Entry> pointValues;
    private List<String> hourList = new ArrayList<>();//折线x数据
    private List<String> countList = new ArrayList<>();//折线y数据
    ArrayList<String> xValues = new ArrayList<>();//柱状图x
    List<Float> yValues = new ArrayList<>();//柱状图y
    private String token;
    private String company_id;
    private Dialog dialog;
    private String mYear, mMonth;
    private TimePickerView pvTime;


    @Override
    public void initViews() {

        setContentView(R.layout.activity_general_statistic);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
//        initChart();//初始化折线图表
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        topName.setText("统计");

        mYear = year + "";

        if (month > 9) {

            mMonth = month + "";

        } else {

            mMonth = "0" + month;

        }

    }

    @Override
    public void initData() {


        token = Sptools.getString(this, Mycontants.API_TOKEN, "");
        company_id = Sptools.getString(this, Mycontants.COMPANY_ID, "");

        if (TextUtils.isEmpty(mYear)) {

            ToastUtil.showToast("请选择时间", this);

        } else if (TextUtils.isEmpty(mMonth)) {

            ToastUtil.showToast("请选择时间", this);

        } else {

            getData(token, mYear, mMonth, company_id);
            btnChangeDate.setText(mYear + "年" + mMonth + "月");
            getInformationData(token, mYear, mMonth, company_id);
        }

    }

    @Override
    public void initEvents() {
        initTimePicker();//时间选择器

    }

    private void getInformationData(String token, String year, String month, String company_id) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("api_token", token);
        params.addBodyParameter("year", year);
        params.addBodyParameter("month", month);
        params.addBodyParameter("company_id", company_id);

        HttpTool.getInstance(this).httpWithParams(Url.GET_STATISTICS_DATA_ON_FORM, params, new SMObjectCallBack<StatisticsData>() {

            @Override
            public void onSuccess(StatisticsData statisticsData) {

                if (statisticsData.getCode() == 200) {

                    LoadImageUtil.ShowImage(GeneralStatisticActivity.this, statisticsData.getResult().getLogo(), ivCompanyLogo);

                    tvAlarmNum.setText(statisticsData.getResult().getWarning_num() + "次");
                    tvAlarmTime.setText(secondsToTime(Integer.valueOf(statisticsData.getResult().getTotal_time())));
                    tvCompanyName.setText(statisticsData.getResult().getCom_name());
                    tvOfflineDevice.setText(statisticsData.getResult().getOff_line() + "台");
                    tvNormalDevice.setText(statisticsData.getResult().getNormal() + "台");


                } else {

                    ToastUtil.showToast(statisticsData.getMsg(), GeneralStatisticActivity.this);


                }


            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }


    //设置chart基本属性
    private void initChart() {
        //描述信息
//        EventLogTags.Description description = new EventLogTags.Description();
//        description.setText("");//添加描述信息
//        //设置描述信息
//        mLineChart.setDescription(description);
        mLineChart.getDescription().setEnabled(false);
        //设置没有数据时显示的文本
        mLineChart.setNoDataText("没有数据喔~~");
        //设置是否绘制chart边框的线
        mLineChart.setDrawBorders(true);
        //设置chart边框线颜色
        mLineChart.setBorderColor(Color.GRAY);
        //设置chart边框线宽度
        mLineChart.setBorderWidth(1f);
        //设置chart是否可以触摸
        mLineChart.setTouchEnabled(true);
        //设置是否可以拖拽
        mLineChart.setDragEnabled(true);
        //设置是否可以缩放 x和y，默认true
        mLineChart.setScaleEnabled(false);
        //设置是否可以通过双击屏幕放大图表。默认是true
        mLineChart.setDoubleTapToZoomEnabled(false);
        //设置chart动画
        mLineChart.animateXY(1000, 1000);

        //=========================设置图例=========================
        // 像"□ xxx"就是图例
        Legend legend = mLineChart.getLegend();
        //设置图例显示在chart那个位置 setPosition建议放弃使用了
        //设置垂直方向上还是下或中
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //设置水平方向是左边还是右边或中
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        //设置所有图例位置排序方向
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //设置图例的形状 有圆形、正方形、线
        legend.setForm(Legend.LegendForm.CIRCLE);
        //是否支持自动换行 目前只支持BelowChartLeft, BelowChartRight, BelowChartCenter
        legend.setWordWrapEnabled(true);


        //=======================设置X轴显示效果==================
        XAxis xAxis = mLineChart.getXAxis();
        //是否启用X轴
        xAxis.setEnabled(true);
        //是否绘制X轴线
        xAxis.setDrawAxisLine(true);
        //设置X轴上每个竖线是否显示
        xAxis.setDrawGridLines(true);
        //设置是否绘制X轴上的对应值(标签)
        xAxis.setDrawLabels(true);
        //设置X轴显示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置竖线为虚线样式
        // xAxis.enableGridDashedLine(10f, 10f, 0f);
        //设置x轴标签数
        xAxis.setLabelCount(hourList.size(), true);
        //图表第一个和最后一个label数据不超出左边和右边的Y轴
        // xAxis.setAvoidFirstLastClipping(true);

        //设置限制线 12代表某个该轴某个值，也就是要画到该轴某个值上
//        LimitLine limitLine = new LimitLine(12);
        //设置限制线的宽
//        limitLine.setLineWidth(1f);
        //设置限制线的颜色
//        limitLine.setLineColor(Color.RED);
        //设置基线的位置
//        limitLine.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
//        limitLine.setLabel("水位线");
        //设置限制线为虚线
//        limitLine.enableDashedLine(10f, 10f, 0f);


        //=================设置左边Y轴===============
        YAxis axisLeft = mLineChart.getAxisLeft();
        //左边Y轴添加限制线
//        axisLeft.addLimitLine(limitLine);
        //是否启用左边Y轴
        axisLeft.setEnabled(true);
        //设置最小值（这里就按demo里固死的写）
        axisLeft.setAxisMinimum(0);
        //设置最大值（这里就按demo里固死的写了）
        axisLeft.setAxisMaximum(50);
        //设置横向的线为虚线
        axisLeft.enableGridDashedLine(10f, 10f, 0f);
        //axisLeft.setDrawLimitLinesBehindData(true);


        //====================设置右边的Y轴===============
        YAxis axisRight = mLineChart.getAxisRight();
        //是否启用右边Y轴
        axisRight.setEnabled(false);
        //设置最小值（这里按demo里的数据固死写了）
        axisRight.setAxisMinimum(0);
        //设置最大值（这里按demo里的数据固死写了）
        axisRight.setAxisMaximum(50);
        //设置横向的线为虚线
        axisRight.enableGridDashedLine(10f, 10f, 0f);

    }

    //设置数据
    private void initChatData() {
        //每个点的坐标,自己随便搞点（x,y）坐标就可以了
        pointValues = new ArrayList<>();
        pointValues.clear();

        for (int i = 0; i < hourList.size(); i++) {

            int y = Integer.valueOf(countList.get(i));
            int x = Integer.valueOf(hourList.get(i));
            pointValues.add(new Entry(x, y));


        }
        //点构成的某条线
        LineDataSet lineDataSet = new LineDataSet(pointValues, mMonth + "月 各时段报警次数统计");
        //设置该线的颜色
        lineDataSet.setColor(Color.parseColor("#854ea2"));
        //设置每个点的颜色
        lineDataSet.setCircleColor(Color.BLUE);
        //设置该线的宽度
        lineDataSet.setLineWidth(1f);
        //设置每个坐标点的圆大小
        lineDataSet.setCircleRadius(1f);
        //设置是否画圆
        lineDataSet.setDrawCircles(true);
        // 设置平滑曲线模式
        //  lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        //设置线一面部分是否填充颜色
        lineDataSet.setDrawFilled(true);
        //设置填充的颜色
        lineDataSet.setFillColor(Color.WHITE);
        //设置是否显示点的坐标值
        lineDataSet.setDrawValues(false);

        //线的集合（可单条或多条线）
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        //把要画的所有线(线的集合)添加到LineData里
        LineData lineData = new LineData(dataSets);
        //把最终的数据setData
        mLineChart.setData(lineData);

    }


    private void getData(String token, String year, String month, String company_id) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("api_token", token);
        params.addBodyParameter("year", year);
        params.addBodyParameter("month", month);
        params.addBodyParameter("company_id", company_id);

        HttpTool.getInstance(this).httpWithParams(Url.GET_STATISTICS_DATA, params, new SMObjectCallBack<StatisticAlarmCount>() {

            @Override
            public void onSuccess(StatisticAlarmCount statisticCount) {

                if (statisticCount.getCode() == 200) {

                    tvBarChartNodata.setVisibility(View.GONE);
                    tvLineChartNodata.setVisibility(View.GONE);

                    barChart.setVisibility(View.VISIBLE);
                    mLineChart.setVisibility(View.VISIBLE);

                    hourList.clear();
                    countList.clear();

                    if (statisticCount.getResult().getFirst()!=null&&statisticCount.getResult().getFirst().size() > 0) {

                        for (int i = 0; i < statisticCount.getResult().getFirst().size(); i++) {

                            hourList.add(statisticCount.getResult().getFirst().get(i).getHour());
                            countList.add(statisticCount.getResult().getFirst().get(i).getCount());
                        }
                        initChart();//初始化折线图表
                        initChatData();//添加折线统计数据


                    }


                    xValues.clear();
                    yValues.clear();

                    if (statisticCount.getResult().getSecond() != null) {

                        for (int j = 0; j < statisticCount.getResult().getSecond().size(); j++) {

                            xValues.add(statisticCount.getResult().getSecond().get(j).getW_camera());
                            Log.e("abc", "onSuccess:===> "+ statisticCount.getResult().getSecond().get(j).getW_camera());
                            yValues.add(Float.valueOf(statisticCount.getResult().getSecond().get(j).getNums()));

                        }

                    }

                    BarChartManager barChartManager2 = new BarChartManager(barChart);
                    barChartManager2.showBarChart(xValues, yValues, mMonth + "月  设备报警统计", Color.parseColor("#43cb83"));


                } else {

                    ToastUtil.showToast(statisticCount.getMsg(), GeneralStatisticActivity.this);

                    barChart.setVisibility(View.GONE);
                    mLineChart.setVisibility(View.GONE);

                    tvLineChartNodata.setVisibility(View.VISIBLE);
                    tvBarChartNodata.setVisibility(View.VISIBLE);

                    tvBarChartNodata.setText("没有"+mMonth+"月份数据");
                    tvLineChartNodata.setText("没有"+mMonth+"月份数据");
                }
            }
            @Override
            public void onError(int error, String msg) {

            }
        });

    }

    //时间选择器
    private void initTimePicker() {//Dialog 模式下，在底部弹出

        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(date);

                mYear = String.valueOf(calendar.get(Calendar.YEAR));
                int month = calendar.get(Calendar.MONTH) + 1;
                if (month > 9) {

                    mMonth = month + "";

                } else {

                    mMonth = "0" + month;

                }
                btnChangeDate.setText(mYear + "年" + mMonth + "月");
                getData(token, mYear, mMonth, company_id);
                getInformationData(token, mYear, mMonth, company_id);

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {


                    }
                })
                .setType(new boolean[]{true, true, false, false, false, false})
                .isDialog(true)
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);
            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }
    }


    @OnClick({R.id.back_btn, R.id.btn_change_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_change_date:

                pvTime.show();

                break;
        }
    }


    /*
     * 秒数转时间
     * */
    private String secondsToTime(int seconds) {
        StringBuilder time = new StringBuilder();
        int min = 0;int hour=0; int day=0;
        //将毫秒转换成秒

        if (seconds > 60) {
            min = seconds / 60;
            seconds = seconds % 60;
        }
        if (min > 60) {
            hour = min / 60;
            min = min % 60;
        }

        if(hour>24){

           day=hour/24;
            hour=hour%24;

        }

        //拼接
        time.append(day);
        time.append("天");
        if (hour< 10)
            time.append("0");
        time.append(hour);
        time.append("时");
        if (min < 10)
            time.append("0");
        time.append(min);
        time.append("分");
        if (seconds < 10)
            time.append("0");
        time.append(seconds);
        time.append("秒");
        return time.toString();
    }
}
