package com.example.traceappproject_daram.measure_page;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.traceappproject_daram.measure_page.WalkingActivity;
import com.example.traceappproject_daram.reprot_page.MovingFeetHeatmapActivity;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import no.nordicsemi.android.nrftoolbox.R;

public class AnalyzeActivity extends AppCompatActivity {
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);
        Toast.makeText(getApplicationContext(),"분석은 5초정도 걸립니다", Toast.LENGTH_SHORT).show();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                nextActivity();
            }
        },5000);
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