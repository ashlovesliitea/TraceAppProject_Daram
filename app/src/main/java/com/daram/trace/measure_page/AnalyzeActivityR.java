package com.daram.trace.measure_page;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;

import no.nordicsemi.android.nrftoolbox.R;
import no.nordicsemi.android.nrftoolbox.uart.UARTActivity;

public class AnalyzeActivityR extends UARTActivity {
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
        LayoutInflater layoutInflater = getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.custom_toast,(ViewGroup)findViewById(R.id.custom_toast_container));
        /*
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("잠시 기다려주세요!");

        Toast toast = new Toast(getApplicationContext());
        //바닥에서 20dp 떨어져서 토스트 생성
        toast.setGravity(Gravity.BOTTOM,0,toPx(30));
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
         */
        /*
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                nextActivity();
            }
        },60000);
         */
        this.onMode(3);
        hideUI();
    }
    private int toPx(int a){
        return (int) (a * Resources.getSystem().getDisplayMetrics().density);
    }
    public void nextActivity(){

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(getApplicationContext(),"분석이 취소되었습니다", Toast.LENGTH_SHORT).show();
        timer.cancel();
    }
}