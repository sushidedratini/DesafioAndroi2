package com.zappts.eduardosaito.desafioandroi2;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper( context );
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification( "TO-DO: Alarme de Tarefa",
                "Ei, você tem uma tarefa para fazer!" );
        notificationHelper.getManager().notify( 1, nb.build() );
    }
}
