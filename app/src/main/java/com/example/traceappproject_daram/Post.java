package com.example.traceappproject_daram;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Post extends StringRequest {

    final static private String URL = "";
    private Map<String, String> parameters;

    public Post(String id, Response .Listener<String> listener) {

        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("id", "rhalwls");
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}

/*
Button buttonSign = (Button) findViewById(R.id.buttonSign);
buttonSign.setOnClickListenr(new View.onClickListener(){

@Override
public void onClick(View v){
        String id = idText.getText().toString();
        Response.Lister<String> responseListenr = new Response.Listner<String>(){
@Override
public void onResponse(String response){

        try
        {
        AlertDialog.Builder builder = new AlertDialog.Builder(post.this);
        dialog = builder.setMessage("회원 등록에 성공하셨습니다.")
        .setPositiveButton("확인",null)
        .create();
        dialog.show();
        finish();
        } catch(Exception e)
        {
        e.printStackTrace();

        }
        }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(post. this);
        dialog = builder.setMessage("회원 등록에 성공했습니다.")
        .setPositiveButton("확인",null)
        .create();
        dialog.show();
        finish();

        try{
        RegisterRequest registerResquest=new RegisterRequest(id);
        RequestQueue queue=Volley.newRewquestQueue(post.this);
        queue.add(registerRequest);
        System.out.println("성공적");
        }catch(Exception e){
        e.printStackTrace();
        }
        }
        });
        }


   /* String id, pw, pwck, name, phone, phone2, phone3, feetSize, weight , height;
    Button gender1, gender2;

    @Override
    public void OnCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x_15_1);

        findViewById(R.id.buttonSign).setOnClickListener(buttonClick);

    }

    Button.OnClickListener buttonClick = new Button.OnClickListener() {
        public void onClick(View v) {
            // 사용자가 입력한 내용을 전역변수에 저장한다
            id = ((EditText)(findViewById(R.id.join_name))).getText().toString();
            pw = ((EditText)(findViewById(R.id.join_password))).getText().toString();
            pwck = ((EditText)(findViewById(R.id.join_pwck))).getText().toString();
            name = ((EditText)(findViewById(R.id.name))).getText().toString();
            phone = ((Spinner)(findViewById(R.id.spinner))).getSelectedItem().toString();
            phone2 =((EditText)(findViewById(R.id.spin))).getText().toString();
            phone3 =((EditText)(findViewById(R.id.spin2))).getText().toString();
           edItem().toString;

            HttpPostData();   // 서버와 자료 주고받기
        }
    };

    public void HttpPostData() {
    try{
        URL url = new URL("http://.php");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();

        http.setDefaultUseCaches(false);
        http.setDoInput(true);
        http.setDoOutput(true);
        http.setRequestMethod("Post");

        http.setRequestProperty("content-type","application/x-www-form-urlencoded");

        StringBuffer buffer =
    }


}
 */
