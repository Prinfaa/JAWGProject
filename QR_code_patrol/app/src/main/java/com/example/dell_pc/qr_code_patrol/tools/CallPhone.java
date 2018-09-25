package com.example.dell_pc.qr_code_patrol.tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by dell-pc on 2017/10/13.
 */
public class CallPhone {
    public void call(final Context context, final String inputStr) {
        if (inputStr.trim().length() != 0) {
            AlertDialog.Builder multiDia = new AlertDialog.Builder(context);
            multiDia.setTitle("您确定拨打电话" + inputStr + "?");
            multiDia.setPositiveButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                }
            });
            multiDia.setNeutralButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                    Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + inputStr));
                    context.startActivity(phoneIntent);
                }
            });
            multiDia.create().show();
        }

    }

}
