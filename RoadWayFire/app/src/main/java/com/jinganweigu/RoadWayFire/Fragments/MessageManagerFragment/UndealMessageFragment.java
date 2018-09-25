package com.jinganweigu.RoadWayFire.Fragments.MessageManagerFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.jinganweigu.RoadWayFire.Activities.AlarmInformationDetailActivity;
import com.jinganweigu.RoadWayFire.Activities.MainActivity;
import com.jinganweigu.RoadWayFire.Adapters.MessageInformationAdapter;
import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseFragment1;
import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseFragment2;
import com.jinganweigu.RoadWayFire.Entries.ListMessage;
import com.jinganweigu.RoadWayFire.Interfaces.Mycontants;
import com.jinganweigu.RoadWayFire.Interfaces.OnRefreshListener;
import com.jinganweigu.RoadWayFire.Interfaces.Url;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.Sptools;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.ToastUtil;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.HttpTool;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.SMObjectCallBack;
import com.jinganweigu.RoadWayFire.ToolsUtils.widget.RefreshListView;
import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UndealMessageFragment extends BaseFragment1 implements MainActivity.OngetUndealPushDataMessage {
    @BindView(R.id.lv_undeal_message)
    RefreshListView lvUndealMessage;
    Unbinder unbinder;

    private String company_id;
    private String token;
    private String user_id;
    private MessageInformationAdapter adapter;
    private List<ListMessage.ResultBean> messageList=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {

            rootView = inflater.inflate(R.layout.fragment_undeal_message, container, false);
            unbinder = ButterKnife.bind(this, rootView);
            company_id = Sptools.getString(getActivity(), Mycontants.COMPANY_ID, "");
            token = Sptools.getString(getActivity(), Mycontants.API_TOKEN, "");
            user_id = Sptools.getString(getActivity(), Mycontants.USER_ID, "");
            lvUndealMessage.setDivider(null);

            ((MainActivity)getActivity()).setOnReceicerUndealMessage(this);

            lvUndealMessage.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void downPullRefresh() {

                    getData(token,company_id,user_id,""+0);

                }
            });

            initData();

        }

        return rootView;
    }


    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);

        if(isVisible){

            Log.e("abc", "onFragmentVisibleChange: ===undeal===>isvisible" );

            if(TextUtils.isEmpty(token)){

            ToastUtil.showToast("TOKEN错误",getActivity());
        }else if(TextUtils.isEmpty(user_id)){

            ToastUtil.showToast("用户ID错误",getActivity());
        }else if(TextUtils.isEmpty(company_id)){

            ToastUtil.showToast("COMPSNYID错误",getActivity());

        }else{

            getData(token,company_id,user_id,""+0);
        }

        }else{


        }


    }


    public void initData() {

        if(TextUtils.isEmpty(token)){

            ToastUtil.showToast("TOKEN错误",getActivity());
        }else if(TextUtils.isEmpty(user_id)){

            ToastUtil.showToast("用户ID错误",getActivity());
        }else if(TextUtils.isEmpty(company_id)){

            ToastUtil.showToast("COMPSNYID错误",getActivity());

        }else{

            getData(token,company_id,user_id,""+0);
        }


        lvUndealMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(getActivity(), AlarmInformationDetailActivity.class);
                intent.putExtra("entity",messageList.get(position-1));
                intent.putExtra("from","undeal");
                startActivity(intent);

            }
        });

    }

    private void getData(String token,String company_id,String user_id,String staus){

        RequestParams params=new RequestParams();
        params.addBodyParameter("api_token",token);
        params.addBodyParameter("company_id",company_id);
        params.addBodyParameter("user_id",user_id);
        params.addBodyParameter("status",staus);

        HttpTool.getInstance(getActivity()).httpWithParams(Url.URL_MESSAGE_LIST, params, new SMObjectCallBack<ListMessage>() {


            @Override
            public void onSuccess(ListMessage listMessage) {

                if(listMessage.getCode()==200){

                        messageList.clear();
                    messageList=listMessage.getResult();

                    if(messageList.size()>0){

                        adapter=new MessageInformationAdapter(getActivity(),messageList,0);
                        lvUndealMessage.setAdapter(adapter);
                    }else{

                        messageList.clear();

                        adapter=new MessageInformationAdapter(getActivity(),messageList,0);
                        lvUndealMessage.setAdapter(adapter);


                    }

                }else{
                    messageList.clear();
                    adapter=new MessageInformationAdapter(getActivity(),messageList,0);
                    lvUndealMessage.setAdapter(adapter);
                    ToastUtil.showToast(listMessage.getMsg(),getActivity());
                }

                lvUndealMessage.hideHeaderView();

            }

            @Override
            public void onError(int error, String msg) {

                lvUndealMessage.hideHeaderView();

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void OnUndealDataReceiver(String data) {
        getData(token,company_id,user_id,""+0);
    }
}
