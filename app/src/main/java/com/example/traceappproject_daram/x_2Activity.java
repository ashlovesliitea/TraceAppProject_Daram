package com.example.traceappproject_daram;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.example.traceappproject_daram.x9.RecyclerViewActivity;

import org.jsoup.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.util.HashMap;
import java.util.Map;


public class x_2Activity extends AppCompatActivity {

    private EditText login_id, login_password;
    private Button button2;
    private Button button3;
    private TextView text;
    private RequestQueue queue;

    String id,pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x_2);


        login_id = findViewById(R.id.login_id);
        login_password = findViewById(R.id.login_password);
        button2 = findViewById(R.id.button2);



        queue = Volley.newRequestQueue(this);
        String url = "https://clean-circuit-303203.appspot.com/communicatewAndroid/loginAndroid.jsp";



        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    Document doc=Jsoup.parse(response);
                    Elements a=doc.select("body");
                    String tmp=a.text();
                    System.out.println(tmp);
                    JSONObject json = new JSONObject(tmp);
                    JSONArray jArr=json.getJSONArray("dataSend");

                    json=jArr.getJSONObject(0);
                    String UserId = json.getString("id");
                    String UserPwd = json.getString("pw");
                    String UserName = json.getString("name");

                    Integer success = json.getInt("success");

                    if (success==1) {//로그인 성공시



                        Toast.makeText(getApplicationContext(), String.format("%s님 환영합니다.", UserName), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(x_2Activity.this, RecyclerViewActivity.class);

                        intent.putExtra("UserEmail", UserId);
                        intent.putExtra("UserPwd", UserPwd);
                        intent.putExtra("UserName", UserName);

                        startActivity(intent);

                    } else if(success==0){//로그인 실패시
                        Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다!", Toast.LENGTH_SHORT).show();
                    }
                    else if(success==-1){//로그인 실패시
                        Toast.makeText(getApplicationContext(), "해당 아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("pw", pw);


                return params;
            }
        };
        button2.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                if(valid()){
                    System.out.println(id+" "+pw);
                    queue.add(stringRequest);}
            }
        });

        text = findViewById(R.id.button3);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(x_2Activity.this, x_15Activity.class);
                startActivity(intent);

            }
        });

    }

    private boolean valid(){
        id = login_id.getText().toString();
        pw= login_password.getText().toString();

        if (TextUtils.isEmpty(id)) {
            Util.showWarning(this,"please enter id");
            login_id.requestFocus();
            return false;

        }else if (TextUtils.isEmpty(pw)){
            Util.showWarning(this,"please enter password");
            login_password.requestFocus();
            return false;
        }else{
            return true;
        }
    }
}

