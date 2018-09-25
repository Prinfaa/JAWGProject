package com.jinganweigu.RoadWayFire.BeseClassUtils;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinganweigu.RoadWayFire.ToolsUtils.ImageUtil.LoadImageUtil;


/**
 * @author Administrator
 * @创建时间 2015-7-4 下午4:07:00
 * @描述 TODO
 *
 * @ svn提交者：$Author: gd $
 * @ 提交时间: $Date: 2015-07-04 16:27:53 +0800 (Sat, 04 Jul 2015) $
 * @ 当前版本: $Rev: 14 $
 */
public abstract class BaseFragment2 extends Fragment
{
	protected FragmentActivity mContext;//上下文
	public LoadImageUtil.IntentMethod intentMethod;

	// XXXDao dao = new XXXDao(getActivity);
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity();//获取fragment所在Activity;
		intentMethod = LoadImageUtil.IntentMethod.getInstance();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View root = initView(inflater,container); //View
		return root;
	}

	/**
	 * 必须覆盖此方法来完全界面的显示
	 * @return
	 * @param inflater
	 * @param container
	 */
	public abstract View initView(LayoutInflater inflater, ViewGroup container);


	/**
	 * 子类覆盖此方法来完成数据的初始化
	 */
	public abstract void initData();

	/**
	 * 子类覆盖此方法来完成事件的添加
	 */
	public void initEvent(){

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		//初始化事件和数据
		super.onActivityCreated(savedInstanceState);
		initData();//初始化数据
		initEvent();//初始化事件
	}

}
