package com.jinganweigu.entrysystem.view;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import com.jinganweigu.entrysystem.R;


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
		setCanceledOnTouchOutside(false);
	}

	@Override
	public void dismiss() {
		super.dismiss();

	}
}
