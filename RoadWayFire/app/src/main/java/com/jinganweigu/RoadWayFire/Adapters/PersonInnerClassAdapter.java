package com.jinganweigu.RoadWayFire.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.Entries.InnerClassPerson;
import com.jinganweigu.RoadWayFire.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonInnerClassAdapter extends BaseAdapter {

    private List<InnerClassPerson.ResultBean> list;
    private Context context;
    private ViewHolder mViewHolder;

    public PersonInnerClassAdapter(List<InnerClassPerson.ResultBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = View.inflate(context, R.layout.item_person_inner_class, null);
            mViewHolder=new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        }else{

            mViewHolder= (ViewHolder) convertView.getTag();

        }

        mViewHolder.itemPerson.setText(list.get(position).getName());


        return convertView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    static class ViewHolder {
        @BindView(R.id.item_person)
        TextView itemPerson;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
