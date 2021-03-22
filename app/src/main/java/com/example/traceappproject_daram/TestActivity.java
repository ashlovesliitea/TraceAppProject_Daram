package com.example.traceappproject_daram;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.traceappproject_daram.comm.SendImg;
import com.example.traceappproject_daram.data.LoginInfo;
import com.example.traceappproject_daram.data.Result;
import com.example.traceappproject_daram.x9.RecyclerViewActivity;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import no.nordicsemi.android.nrftoolbox.R;

import static com.example.traceappproject_daram.Util.makeFolderPath;
import static com.example.traceappproject_daram.Util.reviveResult;
import static com.example.traceappproject_daram.Util.storeObject;
import static com.example.traceappproject_daram.Util.uploadObjToFire;

public class TestActivity extends AppCompatActivity {
    private String TAG = "TESTACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        convTest();
        //testComm();
        //storeDummy(this);
        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                reviveDummy(TestActivity.this);
            }
        },2000);
         */
        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                uploadDummy();
            }
        },5000);
        */
        //fetchResults();
    }

    public void testComm(){
        SendImg sendImg = new SendImg("sdfg",3,this);
        sendImg.sendVolley();
        Log.i(TAG,"test comm end ");
    }
    public void uploadDummy(){
        uploadObjToFire(calendar,this,0,"rhalwlsdummy");
    }
    public Calendar calendar;
    public void storeDummy(Context context){
        Result result = new Result(new LoginInfo("rhalwlsdummy","rhalwls pw"));
        calendar = result.getCalendar();

        storeObject(result,makeFolderPath(context,calendar),0);
    }
    public void reviveDummy(Context context){
        Result result = reviveResult(makeFolderPath(context,calendar));
        Log.i(TAG,"result id and pass "+result.getID()+" , " +result.getCalendar());
    }
    public void fetchResults(){
        File dir = new File(this.getFilesDir().getPath());
        if (! dir.isDirectory()){
            if (! dir.mkdirs()){
                Log.i(TAG,"fetchResults : directory doesn't exist");
            }
        }

        String[] str = dir.list();
        for(String st : str) {
            Log.d("test"," st : "+st +" , "+st.endsWith(".gif"));
        }

        File[] files = dir.listFiles();
        for(File f : files) {
            Log.w("test"," f : "+f.getPath() +" , "+f.getPath().endsWith(".gif"));
            Log.i("test"," f : "+f.getName() +" , "+f.getName().endsWith(".gif"));
        }
    }
    public void convTest(){
        byte b = (byte) 0xff;
        Log.i(TAG,"BYTE : "+(int)b+" , "+(byte)(int)b);
    }
}