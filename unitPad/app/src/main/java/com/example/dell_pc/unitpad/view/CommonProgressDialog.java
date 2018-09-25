package com.example.dell_pc.unitpad.view;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.example.dell_pc.unitpad.R;


/**
 * 自定义进度等待框
 * 
 * @author Administrator
 * 
 */
public class CommonProgressDialog extends ProgressDialog {
	private Context context;
	private String waittext;
	private ProgressDialog pd;

	public CommonProgressDialog(Context context, String waittext) {
		super(context, R.style.myDialogTheme);
		this.context = context;
		this.waittext = waittext;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.waitdialog);
		this.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));//取消对话框黑底色
		TextView text = (TextView) findViewById(R.id.pbview);
		text.setText(waittext);
//		setCanceledOnTouchOutside(false);
        Animation mRotateAnimation = new RotateAnimation(0.0f, 720.0f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setFillAfter(true);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setDuration(1200);
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
        mRotateAnimation.setRepeatMode(Animation.RESTART);
        findViewById(R.id.pb).setAnimation(mRotateAnimation);


	}

	@Override
	public void dismiss() {
		super.dismiss();

	}



}
