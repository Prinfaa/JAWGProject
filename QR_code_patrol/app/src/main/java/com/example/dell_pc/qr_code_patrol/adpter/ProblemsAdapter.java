package com.example.dell_pc.qr_code_patrol.adpter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.dell_pc.qr_code_patrol.R;
import com.example.dell_pc.qr_code_patrol.item.ProblemsItem;
import com.example.dell_pc.qr_code_patrol.item.ProblemsItem;

import java.util.List;


public class ProblemsAdapter extends BaseAdapter {

    private Context context;
    private List<ProblemsItem> problemsList;


    public ProblemsAdapter(Context context, List<ProblemsItem> problemsList){
        this.context = context;
        this.problemsList = problemsList;
    }
    @Override
    public int getCount() {
        return problemsList.size();
    }

    @Override
    public ProblemsItem getItem(int position) {
        return problemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.problems_item, null);
            TextView tvProblems = (TextView) convertView.findViewById(R.id.tvProblem);
            tvProblems.setText(problemsList.get(position).getType());
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
