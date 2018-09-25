package com.example.dell_pc.microstationreceivealarm.adapter;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dell_pc.microstationreceivealarm.R;
import com.example.dell_pc.microstationreceivealarm.item.UnitItem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UnitAdapter extends BaseAdapter {

    private Context context;
    private List<UnitItem> unitList;

    private Map<Integer, View> viewMap = new HashMap<Integer, View>();


    public UnitAdapter(Context context, List<UnitItem> unitList){
        this.context = context;
        this.unitList = unitList;
    }
    @Override
    public int getCount() {
        return unitList.size();
    }

    @Override
    public UnitItem getItem(int position) {
        return unitList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = this.viewMap.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.unit_item,null);
        }

        final UnitItem unitItem = unitList.get(position);

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvPosition = (TextView) convertView.findViewById(R.id.tvPosition);
        TextView tvLinkman = (TextView) convertView.findViewById(R.id.tvLinkman);
        TextView tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);
        TextView tvGoods = (TextView) convertView.findViewById(R.id.tvGoods);

        tvName.setText(unitItem.getName());
        tvPosition.setText(unitItem.getPosition());
        tvLinkman.setText(unitItem.getLinkman());
        tvPhone.setText(unitItem.getPhone());
        tvGoods.setText(unitItem.getGoods());

        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String inputStr = unitItem.getPhone();
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
        });

        return convertView;
    }


}
