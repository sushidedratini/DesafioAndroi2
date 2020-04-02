package com.zappts.eduardosaito.desafioandroi2;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {

    public static final String channelID = "ChannelID";
    public static final String channelName = "Channel";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super( base );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChannel() {
        NotificationChannel channel = new NotificationChannel( channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT );
        channel.enableLights( true );
        channel.enableVibration( true );
        channel.setLightColor( R.color.colorPrimary );
        channel.setLockscreenVisibility( Notification.VISIBILITY_PRIVATE );

        getManager().createNotificationChannel( channel );

    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(String title, String text) {
        return new NotificationCompat.Builder( getApplicationContext(), channelID )
                .setContentTitle( title )
                .setContentText( text )
                .setSmallIcon( R.drawable.ic_check_circle_white_18dp );
    }

}
