package com.example.traceappproject_daram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.traceappproject_daram.comm.SendImg;

public class TestActivity extends AppCompatActivity {
    private String TAG = "TESTACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        testComm();
    }

    public void testComm(){
        SendImg sendImg = new SendImg("sdfg",3,this);
        sendImg.sendVolley();
        Log.i(TAG,"test comm end ");
    }

}