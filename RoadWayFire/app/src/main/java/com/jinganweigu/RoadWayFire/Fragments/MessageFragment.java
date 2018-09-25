package com.jinganweigu.RoadWayFire.Fragments;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseFragment1;
import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseFragment2;
import com.jinganweigu.RoadWayFire.Fragments.MessageManagerFragment.DealMessageFragment;
import com.jinganweigu.RoadWayFire.Fragments.MessageManagerFragment.UndealMessageFragment;
import com.jinganweigu.RoadWayFire.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MessageFragment extends BaseFragment2 {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.vp_message_pager)
    ViewPager vpMessagePager;
    Unbinder unbinder;
    private List<Fragment> list;
    private String[] titles = {"未处警", "已处警"};
    private MyAdapter fragmentAdapter;

    private String company_id;
    private String token;
    private String user_id;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.fragment_message, container, false);
        unbinder = ButterKnife.bind(this, view);
        //1.MODE_SCROLLABLE模式
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //2.MODE_FIXED模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        backBtn.setVisibility(View.GONE);
        topName.setText("消息");
        list = new ArrayList<>();
        list.add(new UndealMessageFragment());
        list.add(new DealMessageFragment());
        fragmentAdapter = new MyAdapter(getActivity().getSupportFragmentManager());
        vpMessagePager.setAdapter(fragmentAdapter);
        //将TabLayout和ViewPager绑定在一起，一个动另一个也会跟着动
        tabLayout.setupWithViewPager(vpMessagePager);

        return view;
    }

    @Override
    public void initData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    /**
     * ViewPager适配器
     */
    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        //获得每个页面的下标
        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        //获得List的大小
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }


}
