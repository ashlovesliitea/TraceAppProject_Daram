package com.example.traceappproject_daram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class x_1Activity extends AppCompatActivity {
    private Button button_login, button_sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x_1);

        button_login = findViewById(R.id.button_login);
        button_sign = findViewById(R.id.button_sign);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(x_1Activity.this, x_2Activity.class);
                startActivityForResult(intent,1000);
            }
        });

        button_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(x_1Activity.this, x_15Activity.class);
                startActivity(intent);
            }
        });
    }
}
