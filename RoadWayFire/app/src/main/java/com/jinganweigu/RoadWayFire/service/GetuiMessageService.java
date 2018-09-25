package com.jinganweigu.RoadWayFire.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.jinganweigu.RoadWayFire.Activities.MainActivity;
import com.jinganweigu.RoadWayFire.Fragments.MessageManagerFragment.DealMessageFragment;
import com.jinganweigu.RoadWayFire.Fragments.MessageManagerFragment.UndealMessageFragment;

public class GetuiMessageService extends GTIntentService {

    public GetuiMessageService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {

        String abc = new String(msg.getPayload());

        Log.e("abc", "onReceiveMessageData: ==>>"+abc );
        Intent intent=new Intent();
        intent.setAction(MainActivity.REFURSH_MESSAGE_COUNT);
        intent.putExtra("refrush",abc);
        sendBroadcast(intent);
//        Intent intent2=new Intent();

//        intent.setAction(DealMessageFragment.GET_DEAL_PUSH_MESSAGE);
//        intent.putExtra("refrush",abc);
//        sendBroadcast(intent2);
//        Intent intent3=new Intent();
//        intent.setAction(UndealMessageFragment.GET_UNDEAL_PUSH_MESSAGE);
//        intent.putExtra("refrush",abc);
//        sendBroadcast(intent3);

    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {


    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {


    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage msg) {


    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage msg) {


    }
}