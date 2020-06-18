package com.example.alarm;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class AlarmService extends Service {
    MediaPlayer mPlayer;
    private boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = createNotificationChannel();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
            Notification notification = builder.setOngoing(true).setSmallIcon(R.mipmap.ic_launcher).build();

            startForeground(1, notification);
        }

        String state = intent.getStringExtra("state");
        Uri musicUri = intent.getParcelableExtra("musicUri");
        System.out.println(musicUri);

        if(!this.isRunning && state.equals("on")){
                this.mPlayer = MediaPlayer.create(this, musicUri);
                this.mPlayer.start();

            Log.d("알람서비스", "알람스타트");

            this.isRunning = true;

        }else if(this.isRunning & state.equals("off")){
            this.mPlayer.stop();
            this.mPlayer.reset();
            this.mPlayer.release();

            Log.d("알람서비스", "알람스탑");

            this.isRunning = false;

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                stopForeground(true);
            }
        }

        return START_NOT_STICKY;
    }

    private String createNotificationChannel(){
        String channelId = "Alarm";
        String channelName = getString(R.string.app_name);
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
        channel.setSound(null, null);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        return channelId;
    }



}
