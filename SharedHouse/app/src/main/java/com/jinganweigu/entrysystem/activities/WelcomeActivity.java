package com.jinganweigu.entrysystem.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.common.BaseActivity;
import com.jinganweigu.entrysystem.utils.MyApplication;
import com.jinganweigu.entrysystem.utils.Mycontants;
import com.jinganweigu.entrysystem.utils.PermissionsChecker;
import com.jinganweigu.entrysystem.utils.StatusColorSetting;
import com.jinganweigu.entrysystem.view.PhoneClickSpan;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends BaseActivity {

//
//    @BindView(R.id.btn_login)
//    Button btnLogin;
//    @BindView(R.id.cb_consumer_register)
//    CheckBox cbConsumerRegister;
//    @BindView(R.id.cb_agent_register)
//    CheckBox cbAgentRegister;
//    @BindView(R.id.tv_attention)
//    TextView tvAttention;
//    private TextView tvTextview;
//


    @Override
    public void initViews() {

        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        StatusColorSetting.getInstance().setStatusBar(this,false,true);


//        tvTextview = (TextView) findViewById(R.id.tv_attention);
//        tvTextview.setText(getClickableHtml("点击“注册”即代表我同意去哪儿的" + "<a href='https://www.baidu.com'>服务条款</a>" + "、" + "<a href='https://www.baidu.com'>支付服务条款</a>" + "、" + "<a href='https://www.baidu.com'>隐私政策</a>" + "及" + "<a href='https://www.baidu.com'>非歧视政策</a>" + "。我的信息将按照中国的相关法律法规存储和处理，包括隐私及信息披露条款。" + "<a href='https://www.baidu.com'>了解更多.</a>"));
//        tvTextview.setMovementMethod(LinkMovementMethod.getInstance()); //设置超链接为可点击状态

        Timer timer=new Timer();
        TimerTask task=new TimerTask()
        {
            @Override
            public void run(){
               intentMethod.startMethodOne(WelcomeActivity.this,LoginActivity.class);


            }
        };
        timer.schedule(task,2*1000);//此处的Delay可以是3*1000，代表三秒


    }

    @Override
    public void initData() {

        // 缺少权限时, 进入权限配置页面



    }

    @Override
    public void initEvents() {

    }




    // 为所有超链接设置样式
//    private CharSequence getClickableHtml(String html) {
//        Spanned spannedHtml = Html.fromHtml(html);
//        SpannableStringBuilder clickableHtmlBuilder = new SpannableStringBuilder(spannedHtml);
//        // 获取所有超链接
//        URLSpan[] urls = clickableHtmlBuilder.getSpans(0, spannedHtml.length(), URLSpan.class);
//        for (final URLSpan span : urls) {
//            setLinkClickable(clickableHtmlBuilder, span); // 为每个超链接样式设置
//        }
//        return clickableHtmlBuilder;
//    }
//
//    // 确定可点区域，并设置点击事件
//    private void setLinkClickable(final SpannableStringBuilder clickableHtmlBuilder,
//                                  final URLSpan urlSpan) {
//        final int start = clickableHtmlBuilder.getSpanStart(urlSpan);
//        final int end = clickableHtmlBuilder.getSpanEnd(urlSpan);
//        final int flags = clickableHtmlBuilder.getSpanFlags(urlSpan);
//
//        PhoneClickSpan phoneClickSpan = new PhoneClickSpan(new PhoneClickSpan.OnLinkClickListener() {
//            @Override
//            public void onLinkClick(View view) {
//                // do something
////                doSomething();
//                String a = tvTextview.getText().toString().substring(start, end);
//                intentMethod.startMethodTwoString(WelcomeActivity.this, WebViewActivity.class, Mycontants.TOP_NAME, a, "link", urlSpan.getURL());
//            }
//        });
//
//        clickableHtmlBuilder.setSpan(phoneClickSpan, start, end, flags);
//    }



//    @OnClick(R.id.btn_login)
//    public void onViewClicked() {
//
//        intentMethod.startMethodOne(this,LoginActivity.class);
//
//    }
}
