package com.jinganweigu.entrysystem.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.activities.DeviceDeticalActivity;
import com.jinganweigu.entrysystem.common.BaseFragment2;
import com.jinganweigu.entrysystem.fragment.statisticfragment.DeviceStatisticFragment;
import com.jinganweigu.entrysystem.fragment.statisticfragment.OutGoingStatisticFragment;
import com.jinganweigu.entrysystem.fragment.statisticfragment.PersonStatisticFragment;
import com.jinganweigu.entrysystem.fragment.statisticfragment.ProjectStatisticsFragment;
import com.jinganweigu.entrysystem.fragment.statisticfragment.ReturnBackStatisticsFragment;
import com.jinganweigu.entrysystem.fragment.statisticfragment.WasteStatisticsFragment;
import com.jinganweigu.entrysystem.utils.ToastUtil;
import com.jinganweigu.entrysystem.view.PopupuWindow;
import com.xw.repo.xedittext.XEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * 作者： Prinfaa .
 * 创建时间：2018/1/30 0030 15:18
 * 功能描述：
 */

public class StatisticsFrament extends BaseFragment2 implements PopupuWindow.PopupuwindowItemClickListener {


    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.ll_resourses)
    LinearLayout llResourses;

    Unbinder unbinder;
    @BindView(R.id.et_num)
    XEditText etNum;
    Unbinder unbinder1;
    @BindView(R.id.ll_scan)
    LinearLayout llScan;
    private TextView tvX, tvY;


    private List<String> groups = new ArrayList<>();

    PopupuWindow pwindow;

    private FragmentManager mFragmentManager;
    private DeviceStatisticFragment deviceStatisticFragment;
    private OutGoingStatisticFragment outGoingStatisticFragment;
    private PersonStatisticFragment personStatisticFragment;
    private ProjectStatisticsFragment projectStatisticsFragment;
    private WasteStatisticsFragment wasteStatisticsFragment;
    private ReturnBackStatisticsFragment returnBackStatisticsFragment;


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        unbinder = ButterKnife.bind(this, view);
        mFragmentManager = getActivity().getSupportFragmentManager();

        etNum.setSeparator("-");//设置分隔符
        etNum.setPattern(new int[]{5, 5, 5});

        // 加载数据
        groups = new ArrayList<>();
        groups.add("设备统计");
        groups.add("出入库统计");
        groups.add("人员统计");
        groups.add("项目统计");
        groups.add("废品统计");
        groups.add("退货统计");


        llResourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                pwindow = new PopupuWindow(getActivity(), groups, display.getWidth() * 3 / 10, ViewGroup.LayoutParams.WRAP_CONTENT);
                pwindow.setOnPopupuwindowItemClickListener(StatisticsFrament.this);
                pwindow.showWindow(v);

            }
        });

        setTabSelection(0);


        etNum.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && KeyEvent.ACTION_UP == event.getAction()) {

                    String num = etNum.getNonSeparatorText().toString().trim();

                    if (!TextUtils.isEmpty(num)) {

                        // 先隐藏键盘
                        ((InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(getActivity().getCurrentFocus()
                                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        etNum.setText("");

                        intentMethod.startMethodWithStringInt(getActivity(), DeviceDeticalActivity.class, "num", num,"from",2);

                    } else {


                        ToastUtil.showToast("请输入设备号", getActivity());

                    }


                }

                return false;
            }
        });


        llScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentMethod.startMethodWithInt(getActivity(), CaptureActivity.class,"from",3);

            }
        });


        return view;
    }

    @Override
    public void initData() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void OnPopupuwindowClick(int position) {

//        ToastUtil.showToast("这是统计回调===>" + groups.get(position), getActivity());
//        if(position==0){
//
//
//            intentMethod.startMethodTwo(getActivity(), DetailActivity.class);
//
//
//        }

        Log.e("OnPopupuwindowClick", "OnPopupuwindowClick: ===>" + position);

        switch (position) {

            case 0:
                setTabSelection(0);
                break;
            case 1:
                setTabSelection(1);
                break;
            case 2:
                setTabSelection(2);
                break;
            case 3:
                setTabSelection(3);
                break;
            case 4:
                setTabSelection(4);
                break;
            case 5:
                setTabSelection(5);
                break;


        }


    }

    public void setTabSelection(int i) {
//        resetBtn();
        // 开启一个Fragment事务
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (i) {
            case 0:
//                ll_enter.setSelected(true);
                if (deviceStatisticFragment == null) {
                    deviceStatisticFragment = new DeviceStatisticFragment();
                    transaction.add(R.id.statistics_fragment, deviceStatisticFragment);
                } else {
                    transaction.show(deviceStatisticFragment);
                }

                break;
            case 1:
//                ll_outhouse.setSelected(true);
                if (outGoingStatisticFragment == null) {
                    outGoingStatisticFragment = new OutGoingStatisticFragment();
                    transaction.add(R.id.statistics_fragment, outGoingStatisticFragment);
                } else {
                    transaction.show(outGoingStatisticFragment);
                }
                break;
            case 2:
//                ll_return.setSelected(true);
                if (personStatisticFragment == null) {
                    personStatisticFragment = new PersonStatisticFragment();
                    transaction.add(R.id.statistics_fragment, personStatisticFragment);
                } else {
                    transaction.show(personStatisticFragment);
                }
                break;


            case 3:
//                ll_statistics.setSelected(true);
                if (projectStatisticsFragment == null) {
                    projectStatisticsFragment = new ProjectStatisticsFragment();
                    transaction.add(R.id.statistics_fragment, projectStatisticsFragment);
                } else {
                    transaction.show(projectStatisticsFragment);
                }

                break;
            case 4:
//                ll_statistics.setSelected(true);
                if (wasteStatisticsFragment == null) {
                    wasteStatisticsFragment = new WasteStatisticsFragment();
                    transaction.add(R.id.statistics_fragment, wasteStatisticsFragment);
                } else {
                    transaction.show(wasteStatisticsFragment);
                }

                break;
            case 5:
//                ll_statistics.setSelected(true);
                if (returnBackStatisticsFragment == null) {
                    returnBackStatisticsFragment = new ReturnBackStatisticsFragment();
                    transaction.add(R.id.statistics_fragment, returnBackStatisticsFragment);
                } else {
                    transaction.show(returnBackStatisticsFragment);
                }

                break;

            default:
                break;
        }
        transaction.commitAllowingStateLoss();

    }

    private void hideFragments(FragmentTransaction transaction) {
        if (deviceStatisticFragment != null) {//设备统计
            transaction.hide(deviceStatisticFragment);//隐藏
        }
        if (outGoingStatisticFragment != null) {//出入库统计
            transaction.hide(outGoingStatisticFragment);
        }
        if (personStatisticFragment != null) {//人员统计
            transaction.hide(personStatisticFragment);
        }

        if (projectStatisticsFragment != null) {//项目统计
            transaction.hide(projectStatisticsFragment);
        }
        if (wasteStatisticsFragment != null) {//废品统计
            transaction.hide(wasteStatisticsFragment);
        }
        if (returnBackStatisticsFragment != null) {//退货统计
            transaction.hide(returnBackStatisticsFragment);
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if(hidden){




        }else{

            setTabSelection(0);


        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
    }
}
