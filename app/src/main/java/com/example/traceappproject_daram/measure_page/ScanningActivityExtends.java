package com.example.traceappproject_daram.measure_page;

import androidx.appcompat.app.AppCompatActivity;
import no.nordicsemi.android.nrftoolbox.R;
import no.nordicsemi.android.nrftoolbox.profile.BleProfileServiceReadyActivity;
import no.nordicsemi.android.nrftoolbox.scanner.ScannerNoUI;
import no.nordicsemi.android.nrftoolbox.uart.UARTActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.traceappproject_daram.reprot_page.MovingFeetHeatmapActivity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class ScanningActivityExtends extends UARTActivity {

    public Timer timer;
    public static Activity ScanningActivity;
    public ScannerNoUI scannerNoUI;
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
        setContentView(R.layout.activity_scanning);

        ScanningActivity = com.example.traceappproject_daram.measure_page.ScanningActivityExtends.this;
        hideUI();
        this.onMode(0);
        //블루투스 기능 숨기기
        //UARTActivity에 구현해둔 버튼 onclicklistener하면될듯

    }



    public void nextActivity(){

        Intent intent = new Intent(ScanningActivityExtends.this, WalkingActivity.class);
        startActivity(intent);

    }
}