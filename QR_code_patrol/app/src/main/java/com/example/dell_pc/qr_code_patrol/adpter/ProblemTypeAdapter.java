package com.example.dell_pc.qr_code_patrol.adpter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.dell_pc.qr_code_patrol.R;
import com.example.dell_pc.qr_code_patrol.item.ProblemItem;
import com.example.dell_pc.qr_code_patrol.item.ProblemTypeItem;

import java.util.List;


public class ProblemTypeAdapter extends BaseAdapter {

    private Context context;
    private List<ProblemTypeItem> problemTypeList;


    public ProblemTypeAdapter(Context context, List<ProblemTypeItem> problemTypeList){
        this.context = context;
        this.problemTypeList = problemTypeList;
    }
    @Override
    public int getCount() {
        return problemTypeList.size();
    }

    @Override
    public ProblemTypeItem getItem(int position) {
        return problemTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.problem_type_item, null);
            TextView tvProblemType = (TextView) convertView.findViewById(R.id.tvProblemType);
            tvProblemType.setText(problemTypeList.get(position).getType());
        }else{
        }

        return convertView;
    }


























//        ViewHolder holder = new ViewHolder();
//
//
//        if(convertView == null){
//            convertView = LayoutInflater.from(context).inflate(R.layout.problem_item,null);
//            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
//            holder.tvProblem = (TextView) convertView.findViewById(R.id.tvProblem);
//
//            ProblemItem problemItem = problemList.get(position);
//            holder.tvProblem.setText(problemItem.getProblem());
//
//            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if(isChecked){
//                        problemList.get(position).setChecked(true);
//                    }else {
//                        problemList.get(position).setChecked(false);
//                    }
//                }
//            });
//            convertView.setTag(holder);
//        }
//
//
//        return convertView;
//    }
//
//
    public class ViewHolder {
        public CheckBox checkBox;
        public TextView tvProblem;
    }


}
