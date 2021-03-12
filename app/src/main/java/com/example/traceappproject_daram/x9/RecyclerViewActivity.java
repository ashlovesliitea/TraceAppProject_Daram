package com.example.traceappproject_daram.x9;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.traceappproject_daram.measure_page.ScanningActivity;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import no.nordicsemi.android.nrftoolbox.R;

public class RecyclerViewActivity extends AppCompatActivity {

    private TextView name;
    private RecyclerView listview;
    private RecyclerView.Adapter adapter;
    private String userName;
    private String userId;
    private Button measureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x_9);

        Intent intent=getIntent();
        userName=intent.getStringExtra("UserName");
        userId=intent.getStringExtra("UserId");

        measureButton=(Button)findViewById(R.id.measureStart);

        name=findViewById(R.id.name);
        name.setText(userName);

       // CommunicateWithWeb();

        measureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent measureintent = new Intent(RecyclerViewActivity.this, ScanningActivity.class);
                startActivity(measureintent);
            }
        });

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