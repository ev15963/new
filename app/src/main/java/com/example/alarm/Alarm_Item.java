package com.example.alarm;

public class Alarm_Item {



    private int id;
    private int time_hour;
    private int time_min;
    private String time_ampm;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime_hour() {
        return time_hour;
    }

    public void setTime_hour(int time_hour) {
        this.time_hour = time_hour;
    }

    public int getTime_min() {
        return time_min;
    }

    public void setTime_min(int time_min) {
        this.time_min = time_min;
    }

    public String getTime_ampm() {
        return time_ampm;
    }

    public void setTime_ampm(String time_ampm) {
        this.time_ampm = time_ampm;
    }



}
