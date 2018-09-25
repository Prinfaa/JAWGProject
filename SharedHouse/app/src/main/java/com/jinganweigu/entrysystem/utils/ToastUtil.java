package com.jinganweigu.entrysystem.utils;

import android.content.Context;
import android.widget.Toast;

/**  
 * Filename: ToastUtil.java  <br>
 *
 * Description:   <br>
 * 
 * @author: HLJ <br> 
 * @version: 1.0 <br> 
 * @Createtime: 2014�?2�?11�? <br>
 *
 * @Copyright: Copyright (c)2014 by HLJ <br>
 *  
 */

public class ToastUtil {

	public static void showToast(String things, Context context){
		Toast.makeText(context, things, Toast.LENGTH_SHORT).show();
	}


	/**
	 * 可以在子线程中弹出toast
	 *
	 * @param context
	 * @param text
	 */
	public static void showToastSafe(final Context context, final String text) {
		ThreadUtils.runInUIThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
