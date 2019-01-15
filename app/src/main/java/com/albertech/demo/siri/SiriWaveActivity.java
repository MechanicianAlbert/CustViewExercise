package com.albertech.demo.siri;

import com.albertech.demo.R;
import com.albertech.demo.audio.AudioCaptureActivity;



public class SiriWaveActivity extends AudioCaptureActivity {


    private SiriWaveView mSwv;

    protected void init() {
        setContentView(R.layout.activity_siri);
        mSwv = findViewById(R.id.swv);
    }


    @Override
    public void onVolumeChange(float volume) {
        if (mSwv != null) {
            mSwv.input(volume);
        }
    }
}
