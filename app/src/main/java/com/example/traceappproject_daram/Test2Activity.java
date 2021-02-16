package com.example.traceappproject_daram;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

public class Test2Activity extends AppCompatActivity {
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        button = findViewById(R.id.login_test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String id = "rhalwls";
                Response.Listener<String> responseListenr = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try {
                            Log.i("onresponse", "회원 등록에 성공하셨습니다."+response);
                           // finish();
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };

                try{
                    Post registerResquest=new Post(id,responseListenr);
                    RequestQueue queue=Volley.newRequestQueue(Test2Activity.this);
                    queue.add(registerResquest);
                    System.out.println("성공적");
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
