package com.jinganweigu.RoadWayFire.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinganweigu.RoadWayFire.Entries.BaseModel;
import com.jinganweigu.RoadWayFire.Entries.Contacts;
import com.jinganweigu.RoadWayFire.Interfaces.Mycontants;
import com.jinganweigu.RoadWayFire.Interfaces.Url;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.Sptools;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.ToastUtil;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.HttpTool;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.SMObjectCallBack;
import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsManagerAdapter extends BaseAdapter {

    private Context context;
    private List<Contacts.ResultBean> contactsList = new ArrayList<>();
    private ViewHolder mViewHolder;
    private onClickListener listener;

    public ContactsManagerAdapter(Context context, List<Contacts.ResultBean> contactsList) {
        this.context = context;
        this.contactsList = contactsList;
        listener= (onClickListener) context;
    }

    @Override
    public int getCount() {
        return contactsList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
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

        mViewHolder.tvName.setText(contactsList.get(position).getName());
        mViewHolder.tvPhoneNum.setText(contactsList.get(position).getPhone());
        mViewHolder.llDelate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                delateuser(Sptools.getString(context, Mycontants.API_TOKEN,""),contactsList.get(position).getUser_id(),Sptools.getString(context,Mycontants.COMPANY_ID,""));

                listener.onClick(position);

            }
        });


        return convertView;
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


    public interface onClickListener{

         void onClick(int position);


    }



}
