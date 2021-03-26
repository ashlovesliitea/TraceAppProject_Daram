package com.daram.trace.measure_page;

import no.nordicsemi.android.nrftoolbox.R;
import no.nordicsemi.android.nrftoolbox.scanner.ScannerNoUI;
import no.nordicsemi.android.nrftoolbox.uart.UARTActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;

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

        ScanningActivity = com.daram.trace.measure_page.ScanningActivityExtends.this;
        hideUI();
        this.onMode(0);
        //블루투스 기능 숨기기
        //UARTActivity에 구현해둔 버튼 onclicklistener하면될듯

    }
    public static String TAG = "ScanningActivityExtends";


    public void nextActivity(){


    }
}