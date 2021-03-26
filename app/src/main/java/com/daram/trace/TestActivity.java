package com.daram.trace;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.daram.trace.comm.SendImg;
import com.daram.trace.data.LoginInfo;
import com.daram.trace.data.Result;

import androidx.appcompat.app.AppCompatActivity;


import java.io.File;
import java.util.Calendar;

import no.nordicsemi.android.nrftoolbox.R;

import static com.daram.trace.Util.makeFolderPath;
import static com.daram.trace.Util.reviveResult;
import static com.daram.trace.Util.storeObject;
import static com.daram.trace.Util.uploadObjToFire;

public class TestActivity extends AppCompatActivity {
    private String TAG = "TESTACTIVITY";
    private Button btnSave;
    public Result result ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        btnSave  = (Button) findViewById(R.id.test_save_button);


        /*
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = new Result(new LoginInfo("test","dummy"));
                if(result ==null){
                    Log.i(TAG,"RESULT WAS NULL");
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");//날짜(시간까지)
                String strDate = sdf.format(result.getCalendar().getTime());
                Log.i(TAG,"result cration time : "+ strDate);
                result.setVersion(0b010100);
                storeObject(result,makeFolderPath(TestActivity.this,result.getCalendar()),0);
                Util.sendResultRecord(TestActivity.this,result);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent endmeasure = new Intent(TestActivity.this, RecyclerViewActivity.class);
                        startActivity(endmeasure);
                    }
                },3000);
            }
        });

         */
    }
    public void dummyMeasure(){
        Result result = new Result(new LoginInfo("lovely","kei"));

    }
    public void testComm(){
        SendImg sendImg = new SendImg("sdfg",3,this);
        sendImg.sendVolley();
        Log.i(TAG,"test comm end ");
    }
    public void uploadDummy(){
        uploadObjToFire(calendar,this,0,LoginInfo.getId());
    }
    public Calendar calendar;
    public void storeDummy(Context context){
        Result result = new Result(new LoginInfo(LoginInfo.getId(),LoginInfo.getPw()));
        calendar = result.getCalendar();

        storeObject(result,makeFolderPath(context,calendar),0);
    }
    public void reviveDummy(Context context){
        Result result = reviveResult(makeFolderPath(context,calendar));
        Log.i(TAG,"result id and pass "+LoginInfo.getId()+" , " +result.getCalendar());
    }
    public void fetchResults(){
        File dir = new File(this.getFilesDir().getPath());
        if (! dir.isDirectory()){
            if (! dir.mkdirs()){
                Log.i(TAG,"fetchResults : directory doesn't exist");
            }
        }

        String[] str = dir.list();
        for(String st : str) {
            Log.d("test"," st : "+st +" , "+st.endsWith(".gif"));
        }

        File[] files = dir.listFiles();
        for(File f : files) {
            Log.w("test"," f : "+f.getPath() +" , "+f.getPath().endsWith(".gif"));
            Log.i("test"," f : "+f.getName() +" , "+f.getName().endsWith(".gif"));
        }
    }
    public void convTest(){
        byte b = (byte) 0xff;
        Log.i(TAG,"BYTE : "+(int)b+" , "+(byte)(int)b);
    }
}