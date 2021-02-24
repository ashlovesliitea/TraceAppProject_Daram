package com.example.traceappproject_daram;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class x_15Activity extends AppCompatActivity {

    private Button buttonSign;
    private EditText id;
    private Button male;
    private Button female;
    private Boolean malePressed, femalePressed;
    private EditText pw, pwck;
    private Spinner footsize, weight, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x_15_1);

        buttonSign=(Button)findViewById(R.id.buttonSign);
        id=(EditText)findViewById(R.id.join_name);

        buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent(x_15Activity.this, x13.class);
                result.putExtra("아이디", id.getText().toString());
                finish();



                id = (EditText)findViewById(R.id.join_name);
                pw =(EditText)findViewById(R.id.join_password);
                pwck=(EditText)findViewById(R.id.join_pwck);
                buttonSign=(Button)findViewById(R.id.buttonSign);
                footsize=(Spinner)findViewById(R.id.spinner);


                if(id.getText().toString().length()==0){
                    Toast.makeText(x_15Activity.this,"아이디를 입력하세요.",Toast.LENGTH_SHORT).show();
                    pw.requestFocus();
                }
                if(pw.getText().toString().length()==0){
                    Toast.makeText(x_15Activity.this,"비밀번호를 입력하세요.",Toast.LENGTH_SHORT).show();
                    pw.requestFocus();
                }
                if(pwck.getText().toString().length()==0){
                    Toast.makeText(x_15Activity.this,"비밀번호 확인을 입력하세요.",Toast.LENGTH_SHORT).show();
                    pwck.requestFocus();
                }
                if(!pw.getText().toString().equals(pwck.getText().toString()) ){
                    Toast.makeText (x_15Activity.this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                    pw.setText("");
                    pwck.setText("");
                    pw.requestFocus();

                }

            }
        });




        male=(Button)findViewById(R.id.button4);
        female=(Button)findViewById(R.id.button5);

        male.setOnTouchListener(new View.OnTouchListener() {//성별 1택택
            @Override
           public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(!femalePressed) {
                            v.setPressed(true);
                        }
                        malePressed = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        v.setPressed(false);
                        malePressed = false;
                        break;
                }
                return true;
            }
        });


    }

        public void onCreate(View v){//스피너 값

        Spinner spinner = findViewById(R.id.spinner);

        String[] items = new String[]{"010", "011", "016", "02", "031"};


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, items);

        spinner.setAdapter(adapter);


        Spinner spinner2 = findViewById(R.id.spinner2);

        String[] items2 = new String[]{"220", "225", "230", "235", "240", "245", "250", "255", "260", "265", "270"};


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, items2);

        spinner2.setAdapter(adapter2);


        Spinner spinner3 = findViewById(R.id.spinner3);

        String[] items3 = new String[]{"30", "40", "50", "60", "70", "80", "90", "100", "110", "120"};


        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, items3);

        spinner3.setAdapter(adapter3);


        Spinner spinner4 = findViewById(R.id.spinner4);

        String[] items4 = new String[]{"130", "140", "150", "160", "170", "180", "190", "200"};


        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, items4);

        spinner4.setAdapter(adapter4);
    };

}

  /*  public void onButtonClicked(View v) {
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);

        switch(v.getId()){
            case R.id.button4:
                setSelected();
                break;
            case R.id.button5:
                setSelected();
                break;
            }

            setToggle();
        }

        public void setSelected(){
            if(v.isSelected()){
                v.setSelected(true);
            }else{
                v.setSelected(false);
            }

        }
    };





    /*private Webview webview;
    private Textview txt_address;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        txt_address = findViewById(R.id.txt_address);

        init_webView();

        handler = new Handler();

     */




















        /*
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */






    /*@NonNull
    @Override
    public void onClick(View view) {
        TextView textView1 =(TextView) findViewById(R.id.gendertext)
                switch(view.getId()){
                    case R.id.button4 :
                        gendertext.setText("남자") ;
                        gendertext.buttonColor(#6A6B8C);
                        break;

                    case R.id.button5 :
                        gendertext.setText("여자") ;
                        gendertext.buttonColor*/