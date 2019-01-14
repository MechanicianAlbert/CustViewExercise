package com.albertech.demo.liquid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.albertech.demo.R;
import com.albertech.demo.scallop.ScallopWaveView;


public class LiquidWaveActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquid);
        init();
    }

    private void init() {
        LiquidWaveView mLwv = findViewById(R.id.lwv);
        mLwv.input(100);
        mLwv.startPhaseShifting();
    }


}
