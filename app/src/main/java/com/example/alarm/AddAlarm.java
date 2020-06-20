package com.example.alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddAlarm extends Activity {
    private static final int REQUEST_CODE_RINGTONE = 10005;
    ViewGroup viewGroup;
    private Button set,remove, ringtoneShow, ringtoneRemove, tv;
    private PendingIntent alarmIntent;
    private TimePicker timePicker;
    private AlarmManager alarmMgr;
    ListAdapter adapter = new ListAdapter();
    int hour, minute;
    static Uri ringtoneUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);

        timePicker = findViewById(R.id.timePick);
        set = findViewById(R.id.setAl);
        remove = findViewById(R.id.removeAl);
        ringtoneShow = findViewById(R.id.ringtoneShow);
        ringtoneRemove = findViewById(R.id.ringtoneRemove);


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

        /*ringtoneRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    public void startAlarm(View view){
        alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmBroadcast.class);

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


        //리시버 설정

        intent.putExtra("state", "on");

        if(ringtoneUri != null && !ringtoneUri.equals(Uri.EMPTY)) {
            //알람음을 선택했을때 선택한 알람소리로 재생
            intent.putExtra("musicUri", ringtoneUri);
        }else{
            //알람음을 선택하지 않았을때 기본알람소리 재생
            ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            intent.putExtra("musicUri", ringtoneUri);
        }

        this.alarmIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

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


        //Intent backIntent = new Intent(this, MainActivity.class);
        //startActivity(backIntent);


        SimpleDateFormat format = new SimpleDateFormat("MM월 dd일,  HH시 mm분", Locale.getDefault());
        Toast.makeText(this, format.format(calendar.getTime()) + " \n알람이 설정되었습니다.", Toast.LENGTH_LONG).show();

        int listHour = hour;
        int listMin = minute;
        String ampm;
        final int id = (int) System.currentTimeMillis();


        if(hour >= 0 && hour < 12){
            ampm = "AM";
        }else
            ampm = "PM";


        System.out.println(listHour + ", " + listMin + ", " + ampm +", id = " + id);

        Alarm_Item alarm_item = new Alarm_Item();
        alarm_item.setTime_hour(listHour);
        alarm_item.setTime_min(listMin);
        alarm_item.setTime_ampm(ampm);
        alarm_item.setId(id);

        adapter.addItem(alarm_item);
        adapter.notifyDataSetChanged();

        //finish();
    }

    public void removeAlarm(View view){
        if(this.alarmIntent == null){
            return;
        }

        this.alarmMgr.cancel(this.alarmIntent);

        Intent intent = new Intent(this, AlarmBroadcast.class);
        intent.putExtra("state", "off");

        this.sendBroadcast(intent);

        this.alarmIntent = null;


        Toast.makeText(this, "알람이 해제되었습니다.", Toast.LENGTH_SHORT).show();
    }

    public void showRingtone(){
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);

        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "알람음 선택");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        startActivityForResult(intent, REQUEST_CODE_RINGTONE);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_RINGTONE:
                if(resultCode == this.RESULT_OK){
                    ringtoneUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

                }
        }
    }


}
