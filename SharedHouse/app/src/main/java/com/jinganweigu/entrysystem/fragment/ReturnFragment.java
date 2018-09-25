package com.jinganweigu.entrysystem.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.jinganweigu.entrysystem.R;
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
 * 创建时间：2018/1/30 0030 15:17
 * 功能描述：
 */

public class ReturnFragment extends BaseFragment2 implements PopupuWindow.PopupuwindowItemClickListener {

    @BindView(R.id.iv_photo)
    TextView ivPhoto;
    @BindView(R.id.tv_examine)
    TextView tvExamine;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.btn_scan_code)
    Button btnScanCode;
    @BindView(R.id.manual_enter)
    Button manualEnter;
    Unbinder unbinder;
    @BindView(R.id.btn_return_type)
    Button btnReturnType;
    PopupuWindow pwindow;
    private List<String> groups = new ArrayList<>();
    private Display display;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_return, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvExamine.setVisibility(View.GONE);
        groups.add("损坏退货");
        groups.add("正常退货");
        display = getActivity().getWindowManager().getDefaultDisplay();
        String RealName=  Sptools.getString(getActivity(),Mycontants.REAL_NAME,"");
        if(!TextUtils.isEmpty(RealName)){
            String  name= NameUtils.getName(RealName);
            ivPhoto.setText(name);
        }

        return view;
    }
    @Override
    public void initData() {

        String wareHouse = Sptools.getString(getActivity(), Mycontants.WAREHOUSE_NAME, "");

        if (!TextUtils.isEmpty(wareHouse)) {

            location.setText(wareHouse);

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_photo, R.id.location, R.id.btn_scan_code, R.id.manual_enter, R.id.btn_return_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_photo:

                intentMethod.startMethodTwo(getActivity(), MineActivity.class);

                break;

            case R.id.btn_return_type://退货类型
                pwindow = new PopupuWindow(getActivity(), groups,display.getWidth() * 3/ 10, ViewGroup.LayoutParams.WRAP_CONTENT);

                pwindow.setOnPopupuwindowItemClickListener(this);

                pwindow.showWindow(view);

                break;

            case R.id.location:

                break;
            case R.id.btn_scan_code:

//                String type = btnReturnType.getText().toString().trim();

//                if (type.equals("损坏退货")) {

//                    intentMethod.startMethodWithInt(getActivity(), CaptureActivity.class, "from", 3);

//                } else if (type.equals("正常退货")) {

                    intentMethod.startMethodWithInt(getActivity(), CaptureActivity.class, "from", 2);

//                } else {
//
//                    ToastUtil.showToast("请选择退货类型", getActivity());
//
//                }

//                intentMethod.startMethodTwo(getActivity(), CaptureActivity.class);
                break;
            case R.id.manual_enter:

                intentMethod.startMethodWithInt(getActivity(), ManualEntryActivity.class, "from", 2);


                break;
        }
    }


    @Override
    public void OnPopupuwindowClick(int postition) {


//        Log.e("OnPopupuwindowClick", "OnPopupuwindowClick: ===>"+groups.get(postition) );

        btnReturnType.setText(groups.get(postition) + "");

    }
}
