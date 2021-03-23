package com.example.traceappproject_daram.x9;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.traceappproject_daram.Util;
import com.example.traceappproject_daram.measure_page.ScanningActivityExtends;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import no.nordicsemi.android.nrftoolbox.R;
import com.example.traceappproject_daram.data.Result;
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
                Intent measureintent = new Intent(RecyclerViewActivity.this, ScanningActivityExtends.class);
                startActivity(measureintent);
            }
        });

        listview = findViewById(R.id.MyRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listview.setLayoutManager(layoutManager);

          ArrayList<ItemForm> list=new ArrayList<>();
          /*
          list.add(new ItemForm("2020/10/28 22:39","아치 2단계, 꿈치 3단계"));
          list.add(new ItemForm("2020/10/11 21:39","아치 1단계, 꿈치 2단계"));
          list.add(new ItemForm("2020/10/04 20:39","아치 3단계, 꿈치 1단계"));

           */
        //TODO: 경로에 있는 모든 내역들 다 읽어서 띄우기
        Result[] results = readAllResults();

        for(int i = 0;i<results.length;i++) {
            if(results[i] == null){
                Log.i(TAG,"result was null");
                continue;
            }
            Result result= results[i];
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");//날짜(시간까지)
            String strDate = sdf.format(result.getCalendar().getTime());

            Log.i(TAG,"appendig item to page : "+ strDate+","+result.getArchLevel()+","+result.getArchLevel());
            list.add(new ItemForm(strDate,"아치 "+result.getArchLevel()+"단계 뒷꿈치 "+result.getBackLevel()));
        }

        adapter = new MyAdapter(list);
        listview.setAdapter(adapter);

        MyListDecoration decoration = new MyListDecoration();
        listview.addItemDecoration(decoration);
    }

    private String TAG = "RecyclerViewActivity";
    //TODO: 밑에 함수 구현
    //모든 result 객체 읽어버리기
    public Result[] readAllResults(){
        File f = new File(this.getFilesDir().getPath().toString());//그냥 캐시 path);
        File[] files = f.listFiles();
        Result[] results = new Result[files.length];
        int i = 0;
        //File commonResult = new File("/result.bin");
        for (File inFile : files) {
            if (inFile.isDirectory()) {
                // is directory
                //result.bin을 그냥 다 읽어오기
                //File curResult = new File(inFile,"/result.bin");
                Log.i(TAG,"inFile 경로 : "+inFile.getAbsolutePath());
                results[i] = Util.reviveResult(inFile.getAbsolutePath());
                if(results[i] == null){
                    Log.i(TAG,"NULL RESULT");
                }
                else {
                    Log.i(TAG,"RESULT NOT NULL");
                }
                i++;
            }
        }
        return results;
    }
}