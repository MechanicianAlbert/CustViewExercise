package com.albertech.demo.scallop;

import com.albertech.demo.R;
import com.albertech.demo.audio.AudioCaptureActivity;


public class ScallopWaveActivity extends AudioCaptureActivity {

    private ScallopWaveView mSwv;


    protected void init() {
        setContentView(R.layout.activity_scallop);
        mSwv = findViewById(R.id.swv);
    }


    @Override
    public void onVolumeChange(float volume) {
        if (mSwv != null) {
            mSwv.input(volume);
        }
    }
}
