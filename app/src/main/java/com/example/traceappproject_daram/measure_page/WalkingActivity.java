package com.example.traceappproject_daram.measure_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.traceappproject_daram.measure_page.AnalyzeActivity;

import androidx.appcompat.app.AppCompatActivity;
import no.nordicsemi.android.nrftoolbox.R;

public class WalkingActivity extends AppCompatActivity {

    private Button stopmeasure;
    public static Activity WalkingActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking);
        //AnalyzeActivity가 종료 될 때 같이 종료하기 위해서 필요한 절차(다른 액티비티에서 액티비티 종료)
        WalkingActivity = com.example.traceappproject_daram.measure_page.WalkingActivity.this;

        stopmeasure = (Button) findViewById(R.id.stopmeasure);

        stopmeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent endmeasure = new Intent(com.example.traceappproject_daram.measure_page.WalkingActivity.this, AnalyzeActivity.class);
                startActivity(endmeasure);

            }
        });
    }
}