package com.jinganweigu.RoadWayFire.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.Entries.Contacts;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.ToolUntils.NameUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsAdapter extends BaseAdapter {

    private Context context;
    private List<Contacts.ResultBean> contactsList = new ArrayList<>();
    private ViewHolder mViewHolder;

    public ContactsAdapter(Context context, List<Contacts.ResultBean> contactsList) {
        this.context = context;
        this.contactsList = contactsList;
    }

    @Override
    public int getCount() {
        return contactsList.size();
    }

    @Override
    public Contacts.ResultBean getItem(int position) {
        return contactsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_contacts_name, null);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder= (ViewHolder) convertView.getTag();
        }

        mViewHolder.tvFirstName.setText(NameUtils.getName(contactsList.get(position).getName()));
        mViewHolder.tvName.setText(contactsList.get(position).getName());


        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_first_name)
        TextView tvFirstName;
        @BindView(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
