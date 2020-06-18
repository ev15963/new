package com.example.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class AlarmBroadcast extends BroadcastReceiver {

    MediaPlayer mediaPlayer;
    private static final String TAG = AlarmBroadcast.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive");
            Log.e("Alarm", "알람설정!");
            Intent mIntent = new Intent(context, AlarmService.class);
            mIntent.putExtra("state", intent.getStringExtra("state"));
            mIntent.putExtra("musicUri", intent.getParcelableExtra("musicUri"));

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                context.startForegroundService(mIntent);
            }else{
                context.startService(mIntent);
            }
         //   mUri = intent.getExtras().getString("MusicUri");
         //   String uri2 = intent.getParcelableExtra("MusicUri");
    //        stopRingtone();

    //  mediaPlayer = MediaPlayer.create(context, Uri.parse(uri));
    //  mediaPlayer.start();
    }

   /* private void startRingtone(Uri uriRingtone){
        this.stopRingtone();

        try {
            mediaPlayer = MediaPlayer.create(this, uriRingtone);

            if(mMediaPlayer == null){
                throw new Exception("플레이어 생성 불가능");
            }
            //  mMediaPlayer.setAudioStreamType();
            mMediaPlayer.setAudioAttributes(
                    new AudioAttributes
                            .Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build());

            mMediaPlayer.start();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

    }*/
   /* public void stopRingtone(){
        if(mediaPlayer != null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();;
            }
            mediaPlayer.release();;
            mediaPlayer = null;
        }
    }*/
}
