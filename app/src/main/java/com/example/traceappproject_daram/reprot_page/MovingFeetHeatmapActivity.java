package com.example.traceappproject_daram.reprot_page;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.annotation.AnyThread;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArrayMap;

import com.example.traceappproject_daram.R;
import com.example.traceappproject_daram.reprot_page.heatmap.FeettMultiFrames;
import com.example.traceappproject_daram.reprot_page.heatmap.FootOneFrame;
import com.example.traceappproject_daram.reprot_page.heatmap.HeatMapHolder;

import java.util.Map;
import java.util.Random;

import ca.hss.heatmaplib.HeatMap;
import ca.hss.heatmaplib.HeatMapMarkerCallback;

public class MovingFeetHeatmapActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    //한번은 화면 터치해야 점들이 나와요
    //지금은 터치할 때마다 점들이 리셋돼요
    private HeatMapHolder map;
    private boolean testAsync = true;
    private FeettMultiFrames frames;
    public int showIdx = 0;
    private static final String TAG = "MovingFeetHeatmap";
    private Button btnReplay;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x13);
        btnReplay = (Button) findViewById(R.id.replay);
        frames = new FeettMultiFrames();//일단은 여기서 프레임들 다 초기화됨
        map = (HeatMapHolder) findViewById(R.id.feetmap);
        map.setMinimum(0.0);
        map.setMaximum(9.0);//강도의 최대값은 얼마냐
        map.setLeftPadding(0);
        map.setRightPadding(0);
        map.setTopPadding(0);
        map.setBottomPadding(0);
        //marker 색깔 바꿀 수 있음 원랜 0xff9400D3
        map.setMarkerCallback(new HeatMapMarkerCallback.CircleHeatMapMarker(0x00000000));
        map.setRadius(80.0);

        Map<Float, Integer> colors = new ArrayMap<>();
        //build a color gradient in HSV from red at the center to green at the outside
        for (int i = 0; i < 21; i++) {
            float stop = ((float) i) / 20.0f;
            int color;
            //gradient 주는 강도 바꾸고싶으면 여기
            color = doGradient(i * 5, 0, 100, 0xff0000ff, 0xffff3000);
            colors.put(stop, color);
        }
        map.setColorStops(colors);
        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
            }
        });
    }


    private void addData() {
        if (testAsync) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    for(int i =0;i<5;i++) {
                        drawNewMap();
                        map.forceRefreshOnWorkerThread();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                map.invalidate();
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            drawNewMap();
            map.forceRefresh();
        }
    }

    @AnyThread
    private void drawNewMap() {
        map.clearData();
        Random rand = new Random();
        //여기서 ptr 움직이기
        FootOneFrame[] feet = frames.retrieveFrame(showIdx);
        passFeetToHeatMap(feet[0],map);
        passFeetToHeatMap(feet[1],map);
        Log.i(TAG,"left or right of each foot : "+feet[0].isleft+" , "+feet[1].isleft);
        showIdx++;
        if(showIdx>= FeettMultiFrames.NUM_FRAMES){
            showIdx =0;
        }
    }
    private void passFeetToHeatMap(FootOneFrame footOneFrame, HeatMapHolder map) {
        for (int i = 0; i < FootOneFrame.SENSOR_NUM; i++) {
            float c1 = footOneFrame.ratioW[i];
            float c2 = footOneFrame.ratioH[i];
            double c3 = footOneFrame.ps[i];
            HeatMap.DataPoint point = new HeatMap.DataPoint(c1, c2, c3);
            map.addData(point);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private float clamp(float value, float min, float max) {
        return value * (max - min) + min;
    }

    @SuppressWarnings("SameParameterValue")
    private double clamp(double value, double min, double max) {
        return value * (max - min) + min;
    }

    @SuppressWarnings("SameParameterValue")
    private static int doGradient(double value, double min, double max, int min_color, int max_color) {
        if (value >= max) {
            return max_color;
        }
        if (value <= min) {
            return min_color;
        }
        float[] hsvmin = new float[3];
        float[] hsvmax = new float[3];
        float frac = (float) ((value - min) / (max - min));
        Color.RGBToHSV(Color.red(min_color), Color.green(min_color), Color.blue(min_color), hsvmin);
        Color.RGBToHSV(Color.red(max_color), Color.green(max_color), Color.blue(max_color), hsvmax);
        float[] retval = new float[3];
        for (int i = 0; i < 3; i++) {
            retval[i] = interpolate(hsvmin[i], hsvmax[i], frac);
        }
        return Color.HSVToColor(retval);
    }

    private static float interpolate(float a, float b, float proportion) {
        return (a + ((b - a) * proportion));
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        testAsync = !testAsync;
    }
}