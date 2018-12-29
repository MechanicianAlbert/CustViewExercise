package com.albertech.demo.rocker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.albertech.demo.R;



public class RockActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_rock);
        init();
    }

    private void init() {
        RockerView rv = findViewById(R.id.rv);
        AimerView av = findViewById(R.id.av);
        rv.addOnRockListener(av);
    }

}
