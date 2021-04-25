package com.example.todo.Notifications;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.todo.R;

public class Notificationchannel extends ContextWrapper {
    public static final String CHANNEL_ID = "channel id";
    public static final String CHANNEL_NAME = "channel name";

    private NotificationManager mManager;

    public Notificationchannel(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel notificationchannel =new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(notificationchannel);
    }

   public NotificationManager getManager(){
        if(mManager==null){
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
   }

   public NotificationCompat.Builder getChannelNotification(){
        return new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentTitle("Alarm")
                .setContentText("Your Alarm Manager is Working")
                .setSmallIcon(R.drawable.ic_launcher_background);
   }
}
