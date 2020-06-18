package com.example.alarm;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class AlarmFragment extends Fragment {
    private static final int REQUEST_CODE_RINGTONE = 10005;
    ViewGroup viewGroup;
    private Button set,remove, ringtoneShow, ringtoneRemove;
    private PendingIntent alarmIntent;
    private TimePicker timePicker;
    private AlarmManager alarmMgr;
    int hour, minute;
    static Uri ringtoneUri;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.alarm, container, false);
        timePicker = viewGroup.findViewById(R.id.timePick);
        set = viewGroup.findViewById(R.id.setAl);
        remove = viewGroup.findViewById(R.id.removeAl);
        ringtoneShow = viewGroup.findViewById(R.id.ringtoneShow);
        ringtoneRemove = viewGroup.findViewById(R.id.ringtoneRemove);
        TextView tv = (TextView) getActivity().findViewById(R.id.tv); //추가

        //알람 설정
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAlarm(viewGroup);
            }
        });

        //알람 해제
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAlarm(viewGroup);
            }
        });

        ringtoneShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRingtone();
            }
        });

        ringtoneRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return viewGroup;
    }

    public void startAlarm(View view){
        alarmMgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmBroadcast.class);
        Intent uriIntent = new Intent(getActivity(), AlarmBroadcast.class);
        //PendingIntent pIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);   //타임피커에서 설정한 hour 를 저장
        calendar.set(Calendar.MINUTE,minute);       //타임피커에서 설정한 minute 을 저장
        calendar.set(Calendar.SECOND, 0);           //'초' 를 0으로 설정
        calendar.set(Calendar.MILLISECOND,0);       //'밀리초' 를 0으로 설정

        if(calendar.before(Calendar.getInstance())){
            // 설정한 알람이 현재시간보다 이전일때 다음날로 설정
            calendar.add(Calendar.DATE, 1);
        }

        //알람음 uri 전송
      //  uriIntent.putExtra("musicUri", ringtoneUri);
      //  getActivity().sendBroadcast(uriIntent);

        //리시버 설정

        intent.putExtra("state", "on");

        if(ringtoneUri != null && !ringtoneUri.equals(Uri.EMPTY)) {
            //알람음을 선택했을때 선택한 알람소리로 재생생            intent.putExtra("musicUri", ringtoneUri);
        }else{
            //알람음을 선택하지 않았을때 기본알람소리 재생
            ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            intent.putExtra("musicUri", ringtoneUri);
        }

        this.alarmIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //API 19 이상 API 23미만
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent) ;
        } else {
            //API 19미만
            alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        }
    } else {
        //API 23 이상
        alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }


        //this.alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

        /*intent.putExtra("musirUri", ringtoneUri);
        if(getActivity() != null){
            getActivity().sendBroadcast(intent);
        }*/

        //알람 매니저로 알람을 설정, (UTC표준시간 기기가 절전모드에서도 wakeup, 내가 설정한 시간에, 24시간 뒤 또 알람울림)
       //alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pIntent);

       /*uriIntent.putExtra("MusicUri", ringtoneUri);
       if(getActivity() != null){
           getActivity().sendBroadcast(uriIntent);
       }*/

        SimpleDateFormat format = new SimpleDateFormat("MM월 dd일,  HH시 mm분", Locale.getDefault());
        Toast.makeText(getActivity(), format.format(calendar.getTime()) + " \n알람이 설정되었습니다.", Toast.LENGTH_LONG).show();
    }

    public void removeAlarm(View view){
        if(this.alarmIntent == null){
            return;
        }

        this.alarmMgr.cancel(this.alarmIntent);

        Intent intent = new Intent(getActivity(), AlarmBroadcast.class);
        intent.putExtra("state", "off");

        getActivity().sendBroadcast(intent);

        this.alarmIntent = null;

        //Intent intent = new Intent(getActivity(), AlarmBroadcast.class);
        //PendingIntent pIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
        //alarmMgr.cancel(pIntent);
        Toast.makeText(getActivity(), "알람이 해제되었습니다.", Toast.LENGTH_SHORT).show();
    }

    public void showRingtone(){
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);

        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "알람음 선택");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        startActivityForResult(intent, REQUEST_CODE_RINGTONE);

        /*if(ringtoneUri != null && ringtoneUri.isEmpty()){
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(ringtoneUri));
        }
        this.startActivityForResult(intent, REQUEST_C);
        this.startActivityForResult(intent, REQUESTCODE_RINGTONE_PICKER);*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_RINGTONE:
                if(resultCode == getActivity().RESULT_OK){
                    ringtoneUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

                }
        }
    }

    //추가
    public void onButtonClicked(View v){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(),"TimePicker");
    }

    //    public void startAlarm(View view){
//
//        Log.i(TAG, "startAlarm");
//
//        alarmMgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(getActivity(), AlarmBroadcast.class);
//        alarmIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
//
//        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 60*1000, alarmIntent);
//        Toast.makeText(getActivity(), "알람이 설정되었습니다.", Toast.LENGTH_SHORT).show();
//    }
//
//
//    public void removeAlarm(View view){
//        Log.i(TAG, "removeAlarm");
//
//        if(alarmMgr!= null){
//            alarmMgr.cancel(alarmIntent);
//        }
//    }


//    public void createNotification(ViewGroup viewGroup){
//        show();
//    }
//
//    public void show() {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "default");
//
//        builder.setSmallIcon(R.mipmap.ic_launcher);
//        builder.setContentTitle("알람 제목");
//        builder.setContentText("알람 세부 텍스트");
//
//        Intent intent = new Intent(getActivity(), AlarmFragment.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        builder.setContentIntent(pendingIntent);
//
//        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        builder.setColor(Color.RED);
//
//        Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(getActivity(), RingtoneManager.TYPE_NOTIFICATION);
//        builder.setSound(ringtoneUri);
//
//        long[] vibrate = {0, 100, 200, 300};
//        builder.setVibrate(vibrate);
//        builder.setAutoCancel(true);
//
//        NotificationManager manager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
//        manager.notify(1,builder.build());
//    }
//
//    public void removeNotification(ViewGroup viewGroup){
//        hide();
//    }
//
//    public void hide() {
//        NotificationManagerCompat.from(getActivity()).cancel(1);
//    }

    //    public void setAlarm(){
//        Intent intent = new Intent(getActivity(), AlarmBroadcast.class);
//        PendingIntent pIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            hour = timePicker.getHour();
//            minute = timePicker.getMinute();
//        }
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY,hour);
//        calendar.set(Calendar.MINUTE,minute);
//        calendar.set(Calendar.SECOND,0);
//        calendar.set(Calendar.MILLISECOND,0);
//
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY
//        , pIntent);
//
//    }
//

}
