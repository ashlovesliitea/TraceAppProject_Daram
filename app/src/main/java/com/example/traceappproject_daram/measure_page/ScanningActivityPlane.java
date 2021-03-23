package com.example.traceappproject_daram.measure_page;

import androidx.appcompat.app.AppCompatActivity;
import no.nordicsemi.android.nrftoolbox.R;
import no.nordicsemi.android.nrftoolbox.scanner.ScannerNoUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class ScanningActivityPlane extends AppCompatActivity{

    public Timer timer;
    public static Activity ScanningActivity;
    public ScannerNoUI scannerNoUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        ScanningActivity = ScanningActivityPlane.this;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                nextActivity();
            }
        },5000);

        //블루투스 기능 숨기기
        //UARTActivity에 구현해둔 버튼 onclicklistener하면될듯
        hideUI();

    }
    private void hideUI(){
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                Button button = findViewById(R.id.action_connect);
                button.setVisibility(View.INVISIBLE);
                TextView view = findViewById(R.id.device_name);
                view.setVisibility(View.INVISIBLE);
            } });
    }


    private void nextActivity(){

        Intent intent = new Intent(ScanningActivityPlane.this, WalkingActivity.class);
        startActivity(intent);

    }
}