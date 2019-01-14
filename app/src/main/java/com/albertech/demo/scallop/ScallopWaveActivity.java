package com.albertech.demo.scallop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.albertech.demo.R;



public class ScallopWaveActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scallop);
        init();
    }

    private void init() {
        ScallopWaveView mSwv = findViewById(R.id.swv);
        mSwv.input(100);
    }


}
