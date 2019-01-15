package com.albertech.demo.liquid;

import com.albertech.demo.R;
import com.albertech.demo.audio.AudioCaptureActivity;


public class LiquidWaveActivity extends AudioCaptureActivity {


    private LiquidWaveView mLwv;

    @Override
    protected void init() {
        setContentView(R.layout.activity_liquid);
        mLwv = findViewById(R.id.lwv);
        mLwv.startPhaseShifting();
    }


    @Override
    public void onVolumeChange(float volume) {
        if (mLwv != null) {
            mLwv.input(volume);
        }
    }
}
