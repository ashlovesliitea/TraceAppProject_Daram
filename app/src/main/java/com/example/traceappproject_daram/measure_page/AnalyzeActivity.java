package com.example.traceappproject_daram.measure_page;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
        LayoutInflater layoutInflater = getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.custom_toast,(ViewGroup)findViewById(R.id.custom_toast_container));
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("분석은 5초 정도 걸립니다");

        Toast toast = new Toast(getApplicationContext());
        //바닥에서 20dp 떨어져서 토스트 생성
        toast.setGravity(Gravity.BOTTOM,0,toPx(30));
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                nextActivity();
            }
        },5000);
    }
    private int toPx(int a){
        return (int) (a * Resources.getSystem().getDisplayMetrics().density);
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