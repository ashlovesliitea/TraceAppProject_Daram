package com.example.traceappproject_daram;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class x_15Activity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x_15_1);
        Spinner spinner = findViewById(R.id.spinner);

        String[] items = new String[]{"010","011","016","02","031"};


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, items);

        spinner.setAdapter(adapter);


        Spinner spinner2 = findViewById(R.id.spinner2);

        String[] items2 = new String[]{"220","225","230","235","240","245","250","255","260","265","270"};


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, items2);

        spinner2.setAdapter(adapter2);

        Spinner spinner3 = findViewById(R.id.spinner3);

        String[] items3 = new String[]{"30","40","50","60","70","80","90","100","110","120"};


        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, items3);

        spinner3.setAdapter(adapter3);

        Spinner spinner4 = findViewById(R.id.spinner4);

        String[] items4 = new String[]{"130","140","150","160","170","180","190","200"};


        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, items4);

        spinner4.setAdapter(adapter4);













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

    }
}