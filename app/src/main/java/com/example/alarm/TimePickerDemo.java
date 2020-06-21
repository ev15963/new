package com.example.alarm;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class TimePickerDemo extends AppCompatActivity implements TimePicker.OnTimeChangedListener{

    TextView tv;
    TimePicker tp;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);
        c = Calendar.getInstance(); //캘린더 객체 생성
        int hourofDay = c.get(c.HOUR_OF_DAY); //현재 시
        int minute = c.get(c.MINUTE); //현재 분
        int second = c.get(c.SECOND); //현재 초
        tv = (TextView) findViewById(R.id.tv);
        tp = (TimePicker) findViewById(R.id.timePick);
        tv.setText("초기 설정된 시각 \n" + hourofDay + "시 " + minute + "분" + second + "초");
        tp.setOnTimeChangedListener(this);
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        tv.setText("현재 설정된 시간 \n" + hourOfDay + "시" + minute+ "분");
    }
}
