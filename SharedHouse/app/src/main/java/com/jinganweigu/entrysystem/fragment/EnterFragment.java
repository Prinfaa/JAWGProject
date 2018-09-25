package com.jinganweigu.entrysystem.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.activities.DeviceEnterExamineActivity;
import com.jinganweigu.entrysystem.activities.ExamineActivity;
import com.jinganweigu.entrysystem.activities.ManualEntryActivity;
import com.jinganweigu.entrysystem.activities.MineActivity;
import com.jinganweigu.entrysystem.common.BaseFragment2;
import com.jinganweigu.entrysystem.utils.Mycontants;
import com.jinganweigu.entrysystem.utils.NameUtils;
import com.jinganweigu.entrysystem.utils.Sptools;
import com.jinganweigu.entrysystem.utils.ToastUtil;
import com.jinganweigu.entrysystem.view.PopupuWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 作者： Prinfaa .
 * 创建时间：2018/1/30 0030 15:16
 * 功能描述：
 */

public class EnterFragment extends BaseFragment2 implements PopupuWindow.PopupuwindowItemClickListener {


    Unbinder unbinder;

    @BindView(R.id.iv_photo)
    TextView ivPhoto;
    @BindView(R.id.tv_examine)
    TextView tvExamine;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.btn_enter_type)
    Button btnEnterType;
    @BindView(R.id.btn_scan_code)
    Button btnScanCode;
    @BindView(R.id.manual_enter)
    Button manualEnter;


//    private BeiJingFragment beiJingFragment;
//    private DaLiFragment daLiFragment;
//    private HangZhouFragment hangZhouFragment;
//    private NanJingFragment nanJingFragment;
//    private ShanghaiFragment shanghaiFragment;

    private int mTabCount = 5;
    private int mCurrentItem;
    PopupuWindow pwindow;
    private List<String> groups;
    private Display display;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_enter, container, false);
        unbinder = ButterKnife.bind(this, view);
        display = getActivity().getWindowManager().getDefaultDisplay();

        // 加载数据
        groups = new ArrayList<String>();
        groups.add("新品入库");
        groups.add("成品入库");
        groups.add("废品入库");



        String wareHouse = Sptools.getString(getActivity(), Mycontants.WAREHOUSE_NAME, "");


        if (!TextUtils.isEmpty(wareHouse)) {

            location.setText(wareHouse);
        }

        String RealName = Sptools.getString(getActivity(), Mycontants.REAL_NAME, "");

        if (!TextUtils.isEmpty(RealName)) {

            String name = NameUtils.getName(RealName);

            ivPhoto.setText(name);

        }


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


    @OnClick({R.id.iv_photo, R.id.tv_examine, R.id.location, R.id.btn_scan_code, R.id.manual_enter, R.id.btn_enter_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_photo:

                intentMethod.startMethodTwo(getActivity(), MineActivity.class);

                break;
            case R.id.tv_examine:

                Intent intent = new Intent(getActivity(), ExamineActivity.class);

                startActivity(intent);

//                intentMethod.startMethodWithInt(getActivity(), DeviceEnterExamineActivity.class, "from", 0);//0是入库，1是出库
//                String ename = btnEnterType.getText().toString().trim();
//
//                if(!TextUtils.isEmpty(ename)&&groups.contains(ename)){
//
//                intentMethod.startMethodWithStringInt(getActivity(), DeviceEnterExamineActivity.class, "deviceType", ename, "from", 0);//0是入库，1是出库
//                }else {
//
//                    ToastUtil.showToast("请选择入库类别",getActivity());
//
//                }

                break;
            case R.id.location:






                break;
//            case R.id.btn_enter_type:
//
//
//
//                break;
            case R.id.btn_scan_code://扫码入库




                String name = btnEnterType.getText().toString().trim();

//                    intentMethod.startMethodTwo(getActivity(), CaptureActivity.class);

                if(!TextUtils.isEmpty(name)&&groups.contains(name)){

                    intentMethod.startMethodWithStringInt(getActivity(), CaptureActivity.class, "deviceType", name, "from", 0);//0是入库，1是出库

                }else {

                    ToastUtil.showToast("请选择入库类别",getActivity());

                }


                break;
            case R.id.manual_enter://手动入库

                String sname = btnEnterType.getText().toString().trim();

                if(!TextUtils.isEmpty(sname)&&groups.contains(sname)){

                    intentMethod.startMethodWithStringInt(getActivity(), ManualEntryActivity.class, "deviceType", sname, "from", 0);//0是入库，1是出库
//                    intentMethod.startMethodWithInt(getActivity(), ManualEntryActivity.class, "from", 0);//0是入库，1是出库
                }else {

                    ToastUtil.showToast("请选择入库类别",getActivity());

                }





                break;

            case R.id.btn_enter_type:

                pwindow = new PopupuWindow(getActivity(), groups, display.getWidth() * 3 / 10, ViewGroup.LayoutParams.WRAP_CONTENT);
                pwindow.setOnPopupuwindowItemClickListener(EnterFragment.this);
                pwindow.showWindow(view);

                break;


        }
    }

    @Override
    public void OnPopupuwindowClick(int postition) {

        btnEnterType.setText(groups.get(postition));


    }


//    @Override
//    public void OnPopupuwindowClick(int position) {
//
////        ToastUtil.showToast("这是入库回调===>"+groups.get(position),getActivity());
//        btnEnterType.setText(groups.get(position));
//
//
//    }
}
