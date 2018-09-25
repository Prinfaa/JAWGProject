package com.jinganweigu.entrysystem.fragment.statisticfragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.adapter.WasteStatisticsDownAdapter;
import com.jinganweigu.entrysystem.adapter.WasteStatisticsUpAdapter;
import com.jinganweigu.entrysystem.common.BaseFragment2;
import com.jinganweigu.entrysystem.entry.WasteDeviceBean;
import com.jinganweigu.entrysystem.utils.HttpCode;
import com.jinganweigu.entrysystem.utils.ToastUtil;
import com.jinganweigu.entrysystem.utils.Url;
import com.jinganweigu.entrysystem.utils.http.HttpTool;
import com.jinganweigu.entrysystem.utils.http.SMObjectCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WasteStatisticsFragment extends BaseFragment2 {


    @BindView(R.id.tv_waste_all_num)
    TextView tvWasteAllNum;
    @BindView(R.id.lv_device_type)
    ListView lvDeviceType;
    @BindView(R.id.lv_device_information)
    ListView lvDeviceInformation;
    Unbinder unbinder;

    private WasteStatisticsUpAdapter upAdapter;
    private List<WasteDeviceBean.ResultBean.TotalBean> uList = new ArrayList<>();

    private WasteStatisticsDownAdapter downAdapter;
    private List<WasteDeviceBean.ResultBean.ParticularBean> dList = new ArrayList<>();


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.fragment_waste_statistic, container, false);
        unbinder = ButterKnife.bind(this, view);

        GetWasteDeviceStatisticsData();


        return view;
    }

    @Override
    public void initData() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if(!hidden){

            GetWasteDeviceStatisticsData();

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void GetWasteDeviceStatisticsData() {

        HttpTool.getInstance(getActivity()).http(Url.GET_STATISTICS_WASTE_DATA, new SMObjectCallBack<WasteDeviceBean>() {

            @Override
            public void onSuccess(WasteDeviceBean wasteDeviceBean) {

                if (wasteDeviceBean.getCode() == HttpCode.REQUEST_SUCCESS) {

                    uList.clear();

                    uList = wasteDeviceBean.getResult().getTotal();

                    int a=0;

                    for (int i = 0; i < uList.size(); i++) {

                       a=a+Integer.valueOf(uList.get(i).getQuantity());

                    }

                    tvWasteAllNum.setText("废品统计："+a);


                    upAdapter = new WasteStatisticsUpAdapter(getActivity(), uList);

                    lvDeviceType.setAdapter(upAdapter);

                    dList.clear();

                    dList = wasteDeviceBean.getResult().getParticular();
                    downAdapter = new WasteStatisticsDownAdapter(getActivity(), dList);

                    lvDeviceInformation.setAdapter(downAdapter);


                } else {

                    ToastUtil.showToast(wasteDeviceBean.getMsg(), getActivity());

                }


            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }


}
