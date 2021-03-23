package com.example.traceappproject_daram.measure_page;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traceappproject_daram.Util;
import com.example.traceappproject_daram.data.LoginInfo;
import com.example.traceappproject_daram.data.Result;
import com.example.traceappproject_daram.measure_page.WalkingActivity;
import com.example.traceappproject_daram.reprot_page.MovingFeetHeatmapActivity;
import com.example.traceappproject_daram.reprot_page.heatmap.FeetMultiFrames;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import no.nordicsemi.android.nrftoolbox.R;
import no.nordicsemi.android.nrftoolbox.uart.UARTActivity;

import static com.example.traceappproject_daram.Util.makeFolderPath;

public class AnalyzeActivity extends UARTActivity {
    private Timer timer;
    public static Result result= new Result(new LoginInfo(LoginInfo.getId(),LoginInfo.getPw()));//어디서나 접근가능함

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
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("걸음을 측정중입니다");

        Toast toast = new Toast(getApplicationContext());
        //바닥에서 20dp 떨어져서 토스트 생성
        toast.setGravity(Gravity.BOTTOM,0,toPx(30));
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
        /*
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                nextActivity();
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

    public void nextActivity(){

        Intent intent = new Intent(AnalyzeActivity.this, MovingFeetHeatmapActivity.class);
        Bundle bundle = new Bundle();
        //bundle.putSerializable("result", result);   // Object 넘기기
        //bundle.putSerializable("frames",result.parseRaw());
        Calendar calendar = Calendar.getInstance();
        // parse.
        try {
            Date formatData = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(dateString);
            calendar.setTime(formatData);
            Util.storeObject(result,makeFolderPath(this,calendar),0);
            FeetMultiFrames frames = result.parseRaw();
            Util.storeObject(frames,makeFolderPath(this, calendar),1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Add the bundle to the intent
        intent.putExtras(bundle);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                //종료!!
                finish();
            }
        },5000);

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