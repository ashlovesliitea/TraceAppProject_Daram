package com.example.traceappproject_daram.x9;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traceappproject_daram.R;

public class RecyclerViewActivity extends AppCompatActivity {


    private RecyclerView listview;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x_9);

        listview = findViewById(R.id.MyRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listview.setLayoutManager(layoutManager);

        String[] dataset={"아치 2단계, 뒤꿈치 3단계","아치 1단계, 뒤꿈치 2단계","아치 3단계, 뒤꿈치 1단계"};


        adapter = new MyAdapter(dataset);
        listview.setAdapter(adapter);

        MyListDecoration decoration = new MyListDecoration();
        listview.addItemDecoration(decoration);
    }
}