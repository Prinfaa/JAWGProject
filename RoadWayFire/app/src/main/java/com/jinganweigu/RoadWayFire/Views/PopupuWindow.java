package com.jinganweigu.RoadWayFire.Views;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;


import com.jinganweigu.RoadWayFire.Adapters.GroupAdapter;
import com.jinganweigu.RoadWayFire.R;

import java.util.List;

/**
 * Created by Administrator on 2018/2/28 0028.
 */

public class PopupuWindow {

    Context context;

    private PopupWindow popupWindow;

    private ListView lv_group;

    private View view;

    private List<String> groups;

    private int width, hight;


    public PopupuWindow(Context context, List<String> groups, int width, int hight) {
        this.context = context;
        this.groups = groups;
        this.width = width;
        this.hight = hight;
    }

    public void showWindow(View parent) {

        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.group_list, null);

            lv_group = (ListView) view.findViewById(R.id.lvGroup);
            // 加载数据
//            groups = new ArrayList<String>();
//            groups.add("一键报警");
//            groups.add("气体探测");
//            groups.add("火焰探测");
//            groups.add("水位检测");
//            groups.add("水压检测");
//            groups.add("车载报警");

            GroupAdapter groupAdapter = new GroupAdapter(context, groups);
            lv_group.setAdapter(groupAdapter);
            // 创建一个PopuWidow对象
            popupWindow = new PopupWindow(view, width, hight);
        }

        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);

        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int yPos = (metrics.heightPixels) / 2;
        int xPos = (metrics.widthPixels) / 2;
        popupWindow.showAsDropDown(parent);

        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

//                Toast.makeText(context,
//                        groups.get(position), Toast.LENGTH_SHORT)
//                        .show();

                if(mlistener!=null){

                    mlistener.OnPopupuwindowClick(position);

                }

                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });
    }

    private static PopupuwindowItemClickListener mlistener;

    public interface PopupuwindowItemClickListener {

        void OnPopupuwindowClick(int postition);

    }

    public static void setOnPopupuwindowItemClickListener(PopupuwindowItemClickListener listener) {

        mlistener = listener;


    }


}
