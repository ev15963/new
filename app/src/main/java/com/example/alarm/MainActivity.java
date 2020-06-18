package com.example.alarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    AlarmFragment alarmfragment;
    CalendarFragment calendarfragment;
    WeatherFragment weatherfragmet;
    ConfigFragment configfragment;
    Alarm_RecyclerView_Main alarm_recyclerView_main;


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);


        alarm_recyclerView_main = new Alarm_RecyclerView_Main();
        alarmfragment = new AlarmFragment();
        calendarfragment = new CalendarFragment();
        weatherfragmet = new WeatherFragment();
        configfragment = new ConfigFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, alarm_recyclerView_main).commitAllowingStateLoss();       //첫 화면은 알람

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.tab1:{        //탭1 클릭시 알람화면
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, alarm_recyclerView_main).commitAllowingStateLoss();
                        return true;
                    }

                    case R.id.tab2:{        //탭2 클릭시 캘린더화면
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, calendarfragment).commitAllowingStateLoss();
                        return true;
                    }

                    case R.id.tab3:{        //탭3 클릭시 날씨화면
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,weatherfragmet).commitAllowingStateLoss();
                        return true;
                    }

                    case R.id.tab4:{        //탭4 클릭시 어플설정 화면
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, configfragment).commitAllowingStateLoss();
                        return true;
                    }

                    default: return false;
                }
            }

        });

    }

}
