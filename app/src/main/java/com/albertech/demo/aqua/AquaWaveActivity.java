package com.albertech.demo.aqua;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.albertech.demo.R;


public class AquaWaveActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aqua);
        init();
    }

    private void init() {
        AquaWaveView mAwv = findViewById(R.id.awv);
        mAwv.input(16);

    }


}
