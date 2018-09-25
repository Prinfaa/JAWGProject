package com.jinganweigu.RoadWayFire.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.Entries.ClassManagerList;
import com.jinganweigu.RoadWayFire.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassManagerAdapter extends BaseAdapter {

    private Context context;
    private ViewHolder mViewHolder;
    private List<ClassManagerList.ResultBean> classList;
    private OnClassClickListener listener;

    public ClassManagerAdapter(Context context, List<ClassManagerList.ResultBean> classList) {
        this.context = context;
        this.classList = classList;
        listener= (OnClassClickListener) context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = View.inflate(context, R.layout.item_add_username, null);
            mViewHolder=new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        }else {
            mViewHolder= (ViewHolder) convertView.getTag();
        }

        mViewHolder.tvName.setText(classList.get(position).getClasses_name());

        mViewHolder.tvPhoneNum.setText(classList.get(position).getCla_starttime()+"--"+classList.get(position).getCla_stoptime());

        mViewHolder.llDelate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.OnClassClick(position);

            }
        });

        return convertView;
    }


    @Override
    public int getCount() {
        return classList.size();
    }

    @Override
    public Object getItem(int position) {
        return classList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_phone_num)
        TextView tvPhoneNum;
        @BindView(R.id.ll_delate)
        LinearLayout llDelate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnClassClickListener{

        void OnClassClick(int position);

    }

}
