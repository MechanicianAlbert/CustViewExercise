package com.albertech.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.albertech.demo.rocker.RockActivity;
import com.albertech.demo.scallopwave.ScallopActivity;
import com.albertech.demo.sirial.SirialActivity;


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
        if (id == R.id.btn1) {
            startActivity(RockActivity.class);
        } else if (id == R.id.btn2) {
            startActivity(ScallopActivity.class);
        } else if (id == R.id.btn3) {
            startActivity(SirialActivity.class);
        }
    }


    private void init() {
        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
    }

    private void startActivity(Class<? extends AppCompatActivity> activityClass) {
        startActivity(new Intent(getApplicationContext(), activityClass));
    }
}
