package com.example.traceappproject_daram.measure_page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traceappproject_daram.measure_page.WalkingActivity;
import com.example.traceappproject_daram.reprot_page.MovingFeetHeatmapActivity;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import no.nordicsemi.android.nrftoolbox.R;
import no.nordicsemi.android.nrftoolbox.uart.UARTActivity;

public class AnalyzeActivity extends UARTActivity {
    private Timer timer;
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
    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);
        Toast.makeText(getApplicationContext(),"잠시 기다려주세요!", Toast.LENGTH_SHORT).show();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                nextActivity();
            }
        },5000);
        this.onMode(0);
        hideUI();
    }

    private void nextActivity(){

        Intent intent = new Intent(AnalyzeActivity.this, MovingFeetHeatmapActivity.class);
        startActivity(intent);
        //종료!!
        finish();
        //WalkingActivity 도 종료
        /*
        ScanningActivity scanningActivity = (ScanningActivity)ScanningActivity.ScanningActivity;
        scanningActivity.finish();
       WalkingActivity walkingActivity = (WalkingActivity)WalkingActivity.WalkingActivity;
        walkingActivity.finish();

         */
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(getApplicationContext(),"분석이 취소되었습니다", Toast.LENGTH_SHORT).show();
        timer.cancel();
    }
}