package com.albertech.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.albertech.demo.liquid.LiquidWaveActivity;
import com.albertech.demo.rocker.RockActivity;
import com.albertech.demo.scallop.ScallopWaveActivity;
import com.albertech.demo.siri.SiriWaveActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_rocker) {
            startActivity(RockActivity.class);
        } else if (id == R.id.btn_liquid) {
            startActivity(LiquidWaveActivity.class);
        } else if (id == R.id.btn_scallop) {
            startActivity(ScallopWaveActivity.class);
        } else if (id == R.id.btn_siri) {
            startActivity(SiriWaveActivity.class);
        }
    }


    private void init() {
        Button btn1 = findViewById(R.id.btn_rocker);
        Button btn2 = findViewById(R.id.btn_liquid);
        Button btn3 = findViewById(R.id.btn_scallop);
        Button btn4 = findViewById(R.id.btn_siri);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
    }

    private void startActivity(Class<? extends AppCompatActivity> activityClass) {
        startActivity(new Intent(getApplicationContext(), activityClass));
    }
}
