package com.albertech.demo.audio;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;



public abstract class AudioCaptureActivity extends AppCompatActivity implements AudioCaptureService.OnVolumeChangeListener {

    private final ServiceConnection CONNECTION = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (AudioCaptureService.AudioCaptureBinder) service;
            mBinder.addListener(AudioCaptureActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    private AudioCaptureService.AudioCaptureBinder mBinder;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindService(new Intent(getApplicationContext(), AudioCaptureService.class), CONNECTION, BIND_AUTO_CREATE);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(CONNECTION);
        if (mBinder != null) {
            mBinder.removeListener(this);
            mBinder = null;
        }
    }


    protected abstract void init();

}
