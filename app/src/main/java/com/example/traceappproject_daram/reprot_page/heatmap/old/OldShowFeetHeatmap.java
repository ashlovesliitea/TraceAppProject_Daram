package com.example.traceappproject_daram.reprot_page.heatmap.old;

import android.graphics.Color;
import android.os.AsyncTask;
import androidx.annotation.AnyThread;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.Map;
import java.util.Random;

import androidx.collection.ArrayMap;

import com.example.traceappproject_daram.R;
import com.example.traceappproject_daram.data.Cons;
import com.example.traceappproject_daram.reprot_page.heatmap.FootOneFrame;
import com.example.traceappproject_daram.reprot_page.heatmap.HeatMapHolder;

import ca.hss.heatmaplib.HeatMap;
import ca.hss.heatmaplib.HeatMapMarkerCallback;

public class OldShowFeetHeatmap extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    //한번은 화면 터치해야 점들이 나와요
    //지금은 터치할 때마다 점들이 리셋돼요
    private HeatMapHolder map;
    private boolean testAsync = false;

    private static final String TAG ="ShowFeetHeatmap";
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_feet_heatmap);

        CheckBox box = findViewById(R.id.change_async_status);
        box.setOnCheckedChangeListener(this);

        map = (HeatMapHolder)findViewById(R.id.example_map);//down casting이 되지 않음
        //map.setIdResource(R.mipmap.face);//이러면 ondraw한 다음이라 안되지
        map.setMinimum(0.0);
        map.setMaximum(100.0);
        map.setLeftPadding(100);
        map.setRightPadding(100);
        map.setTopPadding(100);
        map.setBottomPadding(100);
        map.setMarkerCallback(new HeatMapMarkerCallback.CircleHeatMapMarker(0xff9400D3));
        map.setRadius(80.0);
        //map.setBackgroundResource(R.mipmap.ic_launcher);//걍 이 이미지만 뜸 ㅠㅠ 벡터라 빈 곳은 맵이 비칠줄알았는데
        //map.setForeground(getDrawable(R.mipmap.ic_launcher));//위에 코드랑 똑같음 그리고 이 함수는 view에 속해있는 거임..
        //canvas사용했을때


        Map<Float, Integer> colors = new ArrayMap<>();
        //build a color gradient in HSV from red at the center to green at the outside
        for (int i = 0; i < 21; i++) {
            float stop = ((float)i) / 20.0f;
            int color = doGradient(i * 5, 0, 100, 0xff00ff00, 0xffff0000);
            colors.put(stop, color);
        }
        map.setColorStops(colors);

        map.setOnMapClickListener(new HeatMap.OnMapClickListener() {
            @Override
            public void onMapClicked(int x, int y, HeatMap.DataPoint closest) {
                addData();
                //addOverlayImage();
            }
        });

    }


    private void addData() {
        if (testAsync) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    drawNewMap();
                    map.forceRefreshOnWorkerThread();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            map.invalidate();
                        }
                    });
                }
            });
        }
        else {
            drawNewMap();
            map.forceRefresh();
        }
    }

    @AnyThread
    private void drawNewMap() {
        map.clearData();
        Random rand = new Random();
        //rand.nextFloat()는 [0,1.0) 만듦
        //add 20 random points of random intensity
        /*
        for (int i = 0; i < 20; i++) {
            /*
            float c1 = clamp(rand.nextFloat(), 0.0f, 1.0f),c2 = clamp(rand.nextFloat(), 0.0f, 1.0f);
            double c3 = clamp(rand.nextDouble(), 0.0, 100.0);

            float c1 = (float) 0.1,c2 = (float) 0.1;
            double c3 =10;
            Log.i(TAG,"CLAMS : "+c1+" , "+c2+" , "+c3);

            HeatMap.DataPoint point = new HeatMap.DataPoint(c1, c2, c3);
            map.addData(point);
        }
        */
        /*
        float c1 = (float) 0.1,c2 = (float) 0.1;
        double c3 =100;
                Log.i(TAG,"CLAMS : "+c1+" , "+c2+" , "+c3);

        HeatMap.DataPoint point = new HeatMap.DataPoint(c1, c2, c3);
        map.addData(point);
        c1 =(float)0.2;
        c2 = (float)0.2;
        c3=70;
        Log.i(TAG,"CLAMS : "+c1+" , "+c2+" , "+c3);
        point = new HeatMap.DataPoint(c1, c2, c3);
        map.addData(point);
         */
        FootOneFrame footOneFrame = new FootOneFrame(false);
        passFeetToHeatMap(footOneFrame, map);
    }
    private void passFeetToHeatMap(FootOneFrame footOneFrame, HeatMapHolder map){
        for(int i = 0; i< Cons.SENSOR_PER_FOOT; i++){
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
        float frac = (float)((value - min) / (max - min));
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
