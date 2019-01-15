package com.albertech.demo.aqua;

import com.albertech.demo.R;
import com.albertech.demo.audio.AudioCaptureActivity;


public class AquaWaveActivity extends AudioCaptureActivity {

    private AquaWaveView mAwv;


    @Override
    protected void init() {
        setContentView(R.layout.activity_aqua);
        mAwv = findViewById(R.id.awv);
    }


    @Override
    public void onVolumeChange(float volume) {
        mAwv.input(volume / 16);
    }
}
