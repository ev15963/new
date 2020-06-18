package com.example.alarm;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Alarm_RecyclerView_Main extends Fragment {

    ViewGroup viewGroup;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    ListAdapter adapter;
    //ItemTouchHelper helper;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_alarm__recycler_view__main, container, false);
        floatingActionButton = viewGroup.findViewById(R.id.floatingActionButton);
        recyclerView = viewGroup.findViewById(R.id.alarmRecyclerView);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddAlarm.class);
                startActivity(intent);
            }
        });


        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        adapter = new ListAdapter();
        recyclerView.setAdapter(adapter);

        return viewGroup;
    }
}
