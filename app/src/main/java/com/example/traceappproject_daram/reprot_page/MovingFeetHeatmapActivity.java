package com.example.traceappproject_daram.reprot_page;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.AnyThread;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArrayMap;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.traceappproject_daram.Picker;
import com.example.traceappproject_daram.Util;
import com.example.traceappproject_daram.data.Cons;
import com.example.traceappproject_daram.data.LoginInfo;
import com.example.traceappproject_daram.data.Result;
import com.example.traceappproject_daram.reprot_page.heatmap.FeetMultiFrames;
import com.example.traceappproject_daram.reprot_page.heatmap.FootOneFrame;
import com.example.traceappproject_daram.reprot_page.heatmap.HeatMapHolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ca.hss.heatmaplib.HeatMap;
import ca.hss.heatmaplib.HeatMapMarkerCallback;
import no.nordicsemi.android.nrftoolbox.R;

public class MovingFeetHeatmapActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    //reportpage
    //다시보기 누르면 움짤
    private HeatMapHolder map;
    private boolean testAsync = true;
    private FeetMultiFrames frames;
    public int showIdx = 0;
    private static final String TAG = "MovingFeetHeatmap";
    private Button btnReplay;
    private Button btnSend;
    private Result result = new Result(new LoginInfo(LoginInfo.getId(),LoginInfo.getPw()));//일단 여기선 더미데이터 만들게요 ㅠ
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x13);
        btnReplay = (Button) findViewById(R.id.replay);
        //frames = new FeetMultiFrames();//일단은 여기서 프레임들 다 초기화됨
        Log.i(TAG,"is savedInstanceState null ? "+(savedInstanceState == null));
        Bundle b = getIntent().getExtras();
        result = (Result) b.getSerializable("result");
        Log.i(TAG,"arch :"+result.getArchLevel()+", back : "+result.getBackLevel());
        Toast myToast = Toast.makeText(this.getApplicationContext(),"arch :"+result.getArchLevel()+", back : "+result.getBackLevel(), Toast.LENGTH_LONG);
        myToast.show();
        //frames = (FeetMultiFrames) b.getSerializable("frames");
        frames = result.parseRaw();
        //Log.i(TAG,"reuslt passed and print owner of this result "+result.getID());
        //frames = new FeetMultiFrames(peekctr());
        Log.i(TAG,"frames passed and length of this frame is : "+frames.getFramesSz());

        map = (HeatMapHolder) findViewById(R.id.feetmap);
        map.setMinimum(0.0);
        map.setMaximum(20.0);//강도의 최대값은 얼마냐
        map.setLeftPadding(0);
        map.setRightPadding(0);
        map.setTopPadding(0);
        map.setBottomPadding(0);
        //marker 색깔 바꿀 수 있음 원랜 0xff9400D3
        map.setMarkerCallback(new HeatMapMarkerCallback.CircleHeatMapMarker(0x00000000));
        map.setRadius(150.0);
        arch0 = findViewById(R.id.arch0);
        arch1 =findViewById(R.id.arch1);
        arch2 = findViewById(R.id.arch2);
        back0 = findViewById(R.id.back0);
        back1 = findViewById(R.id.back1);
        back2 = findViewById(R.id.back2);
        txtArch = findViewById(R.id.txt_arch);
        txtBack = findViewById(R.id.txt_back);
        setLevelUI(result.getArchLevel(),result.getBackLevel());
        Map<Float, Integer> colors = new ArrayMap<>();
        //build a color gradient in HSV from red at the center to green at the outside
        for (int i = 0; i < 21; i++) {
            float stop = ((float) i) / 20.0f;
            int color;
            //gradient 주는 강도 바꾸고싶으면 여기
            color = doGradient(i * 5, 0, 100, 0xff0000ff, 0xffff0000);
            colors.put(stop, color);
        }
        map.setColorStops(colors);
        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showIdx = 0;
                addData();
            }
        });

        //처음에 누르고 페이지 나오자마자 히트맵 로드되게..?
        drawNewMap();
        //영상을 다 본다음에 버튼을 눌러야댐

        btnSend = (Button) findViewById(R.id.send_admin);
        btnSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onClickSendButton();
            }
        });
    }


    public int peekctr(){
        //context로 파일 오픈
        Picker picker = Util.retrievePicker(this);
        Random random = new Random();
        int ret = random.nextInt(3);
        if(picker ==null){
            Picker first= new Picker();
            ret = first.getI();
            first.up();
            Util.savePicker(this,first);
        }
        else{
            ret= picker.getI();
            picker.up();
            Util.savePicker(this,picker);
        }
        return ret;
    }
    public void uploadResultImgs(){
        Log.i(TAG,"LOGIN INFO : "+ LoginInfo.getId());
        try {
            for(int i = 0;i<frames.getFramesSz();i++) {
                Util.clickUpload(this, LoginInfo.getId(), result.getCalendar(), i);
                Log.i(TAG,"UPLOAD IMAGE : "+i);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveBitmap(Bitmap bitmap,Calendar resultTime,int idx) {
        String strFilePath = Util.makeFolderPath(this, resultTime);
        File file = new File(strFilePath);
        Log.i(TAG,"bitmap path : "+file.getAbsolutePath());
        if (!file.exists())
            file.mkdirs();
        File fileCacheItem = new File(Util.make_path(this,resultTime,idx));
        Log.i(TAG,"bitmap path : "+fileCacheItem.getAbsolutePath());
        OutputStream out = null;
        try {

            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap getBitmapFromView() {
        HeatMapHolder view = map;
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);//이건 왜 필요한겨..?
        //return the bitmap
        return returnedBitmap;
    }
    private TextView arch0,arch1,arch2;
    private TextView back0,back1,back2;
    private TextView txtArch,txtBack;
    private void setLevelUI(int arch, int back){
        txtArch.setText("아치"+arch);
        txtBack.setText("꿈치"+back);
        int y = Color.parseColor("#FFE090"), g = Color.parseColor("#C4C4C4");
        arch0.setBackgroundColor(arch>=1?y:g);arch1.setBackgroundColor(arch>=2?y:g);arch2.setBackgroundColor(arch>=3?y:g);
        back0.setBackgroundColor(back>=1?y:g);back1.setBackgroundColor(back>=2?y:g);back2.setBackgroundColor(back>=3?y:g);
    }
    private void addData() {
        Log.i(TAG,"button replay : addData");
        if (testAsync) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG,"frame 개수 : "+frames.getFramesSz());

                    for(int i =0;i<frames.getFramesSz();i++) {
                        drawNewMap();
                        map.forceRefreshOnWorkerThread();
                        //getApplicationContext().getFilesDir().getPath().toString(),"/bitm"+i+".jpg"
                        //saveBitmap(getBitmapFromView(),result.getCalendar(),i);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                map.invalidate();
                            }
                        });
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        saveBitmap(getBitmapFromView(),result.getCalendar(),i);
                    }
                    //사실 최초1회만 해야하는데... 급해서 그냥 합니다 ㅠ
                    uploadResultImgs();
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
        for(int i = 0;i<Cons.SENSOR_NUM_FOOT;i++){
            Log.i(TAG,"sensor value comp : "+feet[0].ps[i]+","+feet[1].ps[i]);
        }
        Log.i(TAG,"left or right of each foot : "+feet[0].isRight +" , "+feet[1].isRight);
        showIdx++;
        if(showIdx>= Cons.HEATMAP_FRAMES_NUM){
            showIdx =0;
        }
    }

    private void passFeetToHeatMap(FootOneFrame footOneFrame, HeatMapHolder map) {
        for (int i = 0; i < Cons.SENSOR_NUM_FOOT; i++) {
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
    public void onClickSendButton(){
        Util.sendResultRecord(this, result);
    }
}