package com.example.traceappproject_daram.measure_page;

import androidx.appcompat.app.AppCompatActivity;
import no.nordicsemi.android.nrftoolbox.R;
import no.nordicsemi.android.nrftoolbox.profile.BleProfileServiceReadyActivity;
import no.nordicsemi.android.nrftoolbox.scanner.ScannerNoUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.traceappproject_daram.reprot_page.MovingFeetHeatmapActivity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class ScanningActivity extends AppCompatActivity{

    public Timer timer;
    public static Activity ScanningActivity;
    public ScannerNoUI scannerNoUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        ScanningActivity = com.example.traceappproject_daram.measure_page.ScanningActivity.this;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                nextActivity();
            }
        },5000);


    }



    private void nextActivity(){

        Intent intent = new Intent(ScanningActivity.this, WalkingActivity.class);
        startActivity(intent);

    }
}