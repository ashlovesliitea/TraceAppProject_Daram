package com.example.traceappproject_daram.x9;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traceappproject_daram.R;

import java.util.ArrayList;

public class RecyclerViewActivity extends AppCompatActivity {

    private TextView name;
    private RecyclerView listview;
    private RecyclerView.Adapter adapter;
    private String userName;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x_9);

        Intent intent=getIntent();
        userName=intent.getStringExtra("UserName");
        userId=intent.getStringExtra("UserId");

        name=findViewById(R.id.name);
        name.setText(userName);

       // CommunicateWithWeb();

        listview = findViewById(R.id.MyRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listview.setLayoutManager(layoutManager);

      ArrayList<ItemForm> list=new ArrayList<>();
      list.add(new ItemForm("2020/10/28 22:39","아치 2단계, 꿈치 3단계"));
      list.add(new ItemForm("2020/10/11 21:39","아치 1단계, 꿈치 2단계"));
      list.add(new ItemForm("2020/10/04 20:39","아치 3단계, 꿈치 1단계"));

        adapter = new MyAdapter(list);
        listview.setAdapter(adapter);

        MyListDecoration decoration = new MyListDecoration();
        listview.addItemDecoration(decoration);
    }

    public void CommunicateWithWeb(){

    }
}