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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dell_pc.qr_code_patrol.R;
import com.example.dell_pc.qr_code_patrol.item.ProblemItem;
import com.example.dell_pc.qr_code_patrol.item.UnPatrolItem;

import java.util.List;


public class ProblemAdapter extends BaseAdapter {

    private Context context;
    private List<ProblemItem> problemList;


    public ProblemAdapter(Context context, List<ProblemItem> problemList){
        this.context = context;
        this.problemList = problemList;
    }
    @Override
    public int getCount() {
        return problemList.size();
    }

    @Override
    public ProblemItem getItem(int position) {
        return problemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final int index = position;
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.problem_item, null);
            viewHolder.tvProblem = (TextView) convertView.findViewById(R.id.tvProblem);
            viewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.checkbox);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    problemList.get(position).setChecked(true);
                }else{
                    problemList.get(position).setChecked(false);
                }
            }
        });

        viewHolder.tvProblem.setText(problemList.get(position).getProblem());
        if(problemList.get(position).isChecked() == true){
            viewHolder.checkBox.setChecked(true);
        }else{
            viewHolder.checkBox.setChecked(false);
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
