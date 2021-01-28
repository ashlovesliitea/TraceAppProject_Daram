package com.example.traceappproject_daram.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

import com.example.traceappproject_daram.R;

public class MainActivity extends AppCompatActivity {
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
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                startActivity(intent);
            }
        });

        button_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SubActivity2.class);
                startActivity(intent);
            }
        });
    }
}
