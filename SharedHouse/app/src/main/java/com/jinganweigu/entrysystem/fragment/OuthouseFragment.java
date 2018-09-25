package com.jinganweigu.entrysystem.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.zxing.activity.CaptureActivity;
import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.activities.DeviceEnterExamineActivity;
import com.jinganweigu.entrysystem.activities.DeviceOutExamineActivity;
import com.jinganweigu.entrysystem.activities.ExamineActivity;
import com.jinganweigu.entrysystem.activities.ManualEntryActivity;
import com.jinganweigu.entrysystem.activities.MineActivity;
import com.jinganweigu.entrysystem.common.BaseFragment2;
import com.jinganweigu.entrysystem.entry.WorkerInfoBean;
import com.jinganweigu.entrysystem.utils.HttpCode;
import com.jinganweigu.entrysystem.utils.Mycontants;
import com.jinganweigu.entrysystem.utils.NameUtils;
import com.jinganweigu.entrysystem.utils.Sptools;
import com.jinganweigu.entrysystem.utils.ToastUtil;
import com.jinganweigu.entrysystem.utils.Url;
import com.jinganweigu.entrysystem.utils.http.HttpTool;
import com.jinganweigu.entrysystem.utils.http.SMObjectCallBack;

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

public class OuthouseFragment extends BaseFragment2 {

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
    Unbinder unbinder;
    @BindView(R.id.tv_project)
    TextView tvProject;
    Unbinder unbinder1;

    private OptionsPickerView optionsPickerView;

    private List<String> person = new ArrayList<>();
    private ArrayList<String> project = new ArrayList<>();
    private ArrayList<List<String>> company = new ArrayList<>();


    //    PopupuWindow pwindow;
    private List<WorkerInfoBean.ResultBean> list = new ArrayList<>();

    List<String> groups = new ArrayList<>();
    private Display display;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.fragment_outhouse, container, false);
        unbinder = ButterKnife.bind(this, view);
//        tvExamine.setVisibility(View.GONE);

//        tvExamine.setVisibility(View.GONE);
        display = getActivity().getWindowManager().getDefaultDisplay();

        getWorkInfoList();

        String RealName = Sptools.getString(getActivity(), Mycontants.REAL_NAME, "");

        if (!TextUtils.isEmpty(RealName)) {

            String name = NameUtils.getName(RealName);

            ivPhoto.setText(name);

        }


//        getNoLinkData();


        return view;
    }

    @Override
    public void initData() {

//        String wareHouse = Sptools.getString(getActivity(), Mycontants.WAREHOUSE_NAME, "");
//
//
//        if (!TextUtils.isEmpty(wareHouse)) {
//
//            location.setText(wareHouse);
//
//        }


//        tvExamine.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(getActivity(), DeviceOutExamineActivity.class);
//                startActivity(intent);
//
//
//            }
//        });



    }


    private void initNoLinkOptionsPicker() {// 不联动的多级选项
        optionsPickerView = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {

                location.setText(person.get(options1));

                tvProject.setText(project.get(option2));
//                Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
            }
        })

                .build();
        optionsPickerView.setPicker(person, company);

        optionsPickerView.show();

    }


    private void getNoLinkData() {
//        food.add("KFC");
//        food.add("MacDonald");
//        food.add("Pizza hut");
//
//        clothes.add("Nike");
//        clothes.add("Adidas");
//        clothes.add("Anima");
//
//        computer.add(clothes);
//        computer.add(clothes);
//        computer.add(clothes);
//        computer.add(clothes);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_photo, R.id.tv_examine,R.id.tv_project, R.id.location, R.id.btn_enter_type, R.id.btn_scan_code, R.id.manual_enter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_photo:

                intentMethod.startMethodTwo(getActivity(), MineActivity.class);
                break;
            case R.id.tv_examine:

                Intent eintent = new Intent(getActivity(), ExamineActivity.class);

                startActivity(eintent);


//                intentMethod.startMethodWithInt(getActivity(), DeviceEnterExamineActivity.class, "from", 1);//0是入库，1是出库
                break;
            case R.id.location:
            case R.id.tv_project:

                initNoLinkOptionsPicker();

                break;
            case R.id.btn_enter_type:




//                if(groups.size()>0){
//                    pwindow = new PopupuWindow(getActivity(), groups,display.getWidth() * 3/ 10, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//                    pwindow.setOnPopupuwindowItemClickListener(this);
//                    pwindow.showWindow(view);
//                }else{
//
//                    ToastUtil.showToast("领用人为空",getActivity());
//
//                }


                break;
            case R.id.btn_scan_code:// 扫码出库

//                String name = btnEnterType.getText().toString().trim();

                String outPeople=Sptools.getString(getActivity(),Mycontants.REAL_NAME,"");

                String receivePeople=location.getText().toString().trim();

                String receive_unit=tvProject.getText().toString().trim();

                if (TextUtils.isEmpty(outPeople)) {
                    ToastUtil.showToast("出库人为空", getActivity());
                } else if(TextUtils.isEmpty(receivePeople)){
                    ToastUtil.showToast("请选择领用人", getActivity());
                } else if(TextUtils.isEmpty(receive_unit)){
                    ToastUtil.showToast("请选择工程项目", getActivity());
                }else{

                    Intent intent=new Intent(getActivity(),CaptureActivity.class);
                    intent.putExtra("out_people",outPeople);
                    intent.putExtra("receivePeople",receivePeople);
                    intent.putExtra("receive_unit",receive_unit);
                    intent.putExtra("from",1);//0是入库，1是出库
                    startActivity(intent);
//                    intentMethod.startMethodWithStringInt(getActivity(), CaptureActivity.class, "name", name, "from", 1);//0是入库，1是出库
//
//                    ToastUtil.showToast("先选择领用人", getActivity());

                }

                break;
            case R.id.manual_enter://手动出库
//
//                String mname = btnEnterType.getText().toString().trim();
//
//                if (groups.size() > 0 && groups.contains(mname)) {
//
////                    intentMethod.startMethodWithInt(getActivity(), ManualEntryActivity.class, "from", 1);//0是入库，1是出库
//
//                    intentMethod.startMethodWithStringInt(getActivity(), ManualEntryActivity.class, "name", mname, "from", 1);
//
//                } else {
//
//                    ToastUtil.showToast("先选择领用人", getActivity());
//
//                }

                String soutPeople=Sptools.getString(getActivity(),Mycontants.REAL_NAME,"");

                String sreceivePeople=location.getText().toString().trim();

                String sreceive_unit=tvProject.getText().toString().trim();

                if (TextUtils.isEmpty(soutPeople)) {
                    ToastUtil.showToast("出库人为空", getActivity());
                } else if(TextUtils.isEmpty(sreceivePeople)){
                    ToastUtil.showToast("请选择领用人", getActivity());
                } else if(TextUtils.isEmpty(sreceive_unit)){
                    ToastUtil.showToast("请选择工程项目", getActivity());
                }else{

                    Intent intent=new Intent(getActivity(),ManualEntryActivity.class);
                    intent.putExtra("out_people",soutPeople);
                    intent.putExtra("receivePeople",sreceivePeople);
                    intent.putExtra("receive_unit",sreceive_unit);
                    intent.putExtra("from",1);//0是入库，1是出库
                    startActivity(intent);
//                    intentMethod.startMethodWithStringInt(getActivity(), CaptureActivity.class, "name", name, "from", 1);//0是入库，1是出库
//
//                    ToastUtil.showToast("先选择领用人", getActivity());

                }




                break;
        }
    }

//    @Override
//    public void OnPopupuwindowClick(int postition) {
//
////        ToastUtil.showToast("这是入库回调===>"+groups.get(postition),getActivity());
//
//        btnEnterType.setText(list.get(postition).getName());
//
//
//        Log.e("OnPopupuwindowClick", "OnPopupuwindowClick: ===>"+list.get(postition).getName() );
//
//    }


    private void getWorkInfoList() {


        HttpTool.getInstance(getActivity()).http(Url.WORKER_INFO_LIST, new SMObjectCallBack<WorkerInfoBean>() {


            @Override
            public void onSuccess(WorkerInfoBean workerInfoBean) {

                if (workerInfoBean.getCode() == HttpCode.REQUEST_SUCCESS) {

                    person.clear();
                    company.clear();
                    project.clear();

                    for (int i = 0; i <workerInfoBean.getResult().getCompany().size() ; i++) {


                        project.add(workerInfoBean.getResult().getCompany().get(i).getProjects());


                    }
                    for (int i = 0; i <workerInfoBean.getResult().getPeople().size() ; i++) {


                        person.add(workerInfoBean.getResult().getPeople().get(i).getName());

                        company.add(project);
                    }





//                    person = workerInfoBean.getResult().getPeople();
//
//                    company.add(workerInfoBean.getResult().getCompany());


//                    list=workerInfoBean.getResult();


                    // 加载数据
//                    groups = new ArrayList<>();

//                    for (int i = 0; i < list.size(); i++) {


//                        groups.add(list.get(i).getName());

//                    }


//                    pwindow=new PopupuWindow(getActivity(),groups,200,200);


                } else {

                    ToastUtil.showToast(workerInfoBean.getMsg(), getActivity());

                }


            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
    }
}
