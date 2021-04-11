package com.daram.trace.measure_page;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.daram.trace.data.LoginInfo;
import com.daram.trace.data.Result;

import java.util.Timer;

import no.nordicsemi.android.nrftoolbox.R;
import no.nordicsemi.android.nrftoolbox.uart.UARTActivity;

public class AnalyzeActivity extends UARTActivity {
    private Timer timer;
    public static Result result= new Result(new LoginInfo(LoginInfo.getId(),LoginInfo.getPw()));//어디서나 접근가능함
    public TextView textAnal;
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
    public static String TAG = "AnalyzeActivity";
    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_analyze);
        LayoutInflater layoutInflater = getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.custom_toast,(ViewGroup)findViewById(R.id.custom_toast_container));
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("걸음을 측정중입니다");

        Toast toast = new Toast(getApplicationContext());
        //바닥에서 20dp 떨어져서 토스트 생성
        toast.setGravity(Gravity.BOTTOM,0,toPx(30));
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
        textAnal = (TextView) findViewById(R.id.analyzetext);
        timer = new Timer();
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          if(result.isEmpty(false)){
                                              textAnal.setText("왼발통신 실패! 앱을 재시작하세요");
                                          }
                                      }
                                  }, 18000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(result.isEmpty(true)){
                    textAnal.setText("오른발통신 실패! 앱을 재시작하세요");
                }
            }
        }, 30000);
        /*
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(result.isEmpty(false)){
                    alert.setText("왼발통신 실패! 앱을 재시작하세요");
                }
            }
        },30000);
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if(result.isEmpty(false)){
                    alert.setText("오른발통신 실패! 앱을 재시작하세요");
                }
            }
        },60000);
         */

        this.onMode(2);
        hideUI();

    }
    private int toPx(int a){
        return (int) (a * Resources.getSystem().getDisplayMetrics().density);
    }
    String dateString = "2017-3-26 16:40";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(getApplicationContext(),"분석이 취소되었습니다", Toast.LENGTH_SHORT).show();
        timer.cancel();
    }
}