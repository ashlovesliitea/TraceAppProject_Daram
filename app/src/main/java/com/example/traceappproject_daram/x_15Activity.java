package com.example.traceappproject_daram;

import android.graphics.Color;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class x_15Activity extends AppCompatActivity {


    EditText e1;//join_name
    EditText e2;//join_password
    EditText e3;//join_pwck
    EditText e4;//name
    EditText e5;//phone
    EditText e6;//address
    Spinner spinner2;//spinner2, 발사이즈
    Spinner spinner3;//spinner3,몸무게
    Spinner spinner4;//spinner4, 키
    RadioGroup rg;
    Button submitbtn;
    private RequestQueue queue;


    String id;
    String pw;
    String pwck;
    String name;
    String phone;
    String address;
    String footsize;
    String weight;
    String height;
    RadioButton r;
    String gender;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x_15_1);
        initViews();

        queue = Volley.newRequestQueue(this);
        String url = "https://clean-circuit-303203.appspot.com/communicatewAndroid/data.jsp";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("pw", pw);
                params.put("gender",gender);
                params.put("name",name);
                params.put("phone",phone);
                params.put("address",address);
                params.put("footsize",footsize);
                params.put("weight",weight);
                params.put("height",height);

                return params;
            }
        };

        stringRequest.setTag("main");
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valid()){
                    System.out.println(id+" "+pw+" "+pwck+" "+gender+" "+name+" "+phone+" "+address+" "+footsize+" "+weight+" "+height);
                    queue.add(stringRequest);
                }}
        });

    }

    private void initViews(){
        e1=findViewById(R.id.join_name);

        e2=findViewById(R.id.join_password);
        e3=findViewById(R.id.join_pwck);
        e4=findViewById(R.id.name);
        e5=findViewById(R.id.phone);
        e6=findViewById(R.id.address);

        rg=(RadioGroup)findViewById(R.id.rg);
        submitbtn=findViewById(R.id.submit);

        spinner2 = findViewById(R.id.spinner2);

        String[] items2 = new String[]{"220", "225", "230", "235", "240", "245", "250", "255", "260", "265", "270"};


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, items2);

        spinner2.setAdapter(adapter2);

        spinner3 = findViewById(R.id.spinner3);

        String[] items3 = new String[]{"30", "40", "50", "60", "70", "80", "90", "100", "110", "120"};


        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, items3);

        spinner3.setAdapter(adapter3);

        spinner4 = findViewById(R.id.spinner4);

        String[] items4 = new String[]{"130", "140", "150", "160", "170", "180", "190", "200"};


        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, items4);

        spinner4.setAdapter(adapter4);




    }

    private boolean valid(){
        id=e1.getText().toString();
        pw=e2.getText().toString();
        pwck=e3.getText().toString();
        name=e4.getText().toString();
        phone=e5.getText().toString();
        address=e6.getText().toString();
        footsize=spinner2.getSelectedItem().toString();
        weight=spinner3.getSelectedItem().toString();
        height=spinner4.getSelectedItem().toString();
        RadioButton rd=(RadioButton)findViewById(rg.getCheckedRadioButtonId());
        gender=rd.getText().toString();

        if(TextUtils.isEmpty(id)){
            Util.showWarning(this,"please enter id");
            e1.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(pw)){
            Util.showWarning(this,"please enter password");
            e2.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(pwck)){
            Util.showWarning(this,"please enter password check");
            e3.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(name)){
            Util.showWarning(this,"please enter name");
            e4.requestFocus();
            return false;
        } if(TextUtils.isEmpty(phone)){
            Util.showWarning(this,"please enter phone number");
            e5.requestFocus();
            return false;
        } if(TextUtils.isEmpty(address)){
            Util.showWarning(this,"please enter address");
            e6.requestFocus();
            return false;
        } if(TextUtils.isEmpty(footsize)){
            Util.showWarning(this,"please enter foot size");
            spinner2.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(gender)){
            Util.showWarning(this,"please enter your gender");
            rg.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(height)){
            Util.showWarning(this,"please enter your height");
            spinner3.requestFocus();
            return false;
        } if(TextUtils.isEmpty(weight)){
            Util.showWarning(this,"please enter your weight");
            spinner4.requestFocus();
            return false;
        }if(!pw.equals(pwck)){
            Util.showWarning(this,"비밀번호와 비밀번호 확인이 일치하지 않습니다!");
            e3.requestFocus();
            return false;
        }

        else{
            return true;
        }



    }




}









