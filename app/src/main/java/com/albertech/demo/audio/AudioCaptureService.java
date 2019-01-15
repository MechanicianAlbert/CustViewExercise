package com.albertech.demo.audio;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.albertech.demo.R;

import java.util.HashSet;
import java.util.Set;



public class AudioCaptureService extends Service {

    interface OnVolumeChangeListener {
        void onVolumeChange(float volume);
    }

    class AudioCaptureBinder extends Binder {
        void addListener(OnVolumeChangeListener listener) {
            LISTENERS.add(listener);
        }

        void removeListener(OnVolumeChangeListener listener) {
            LISTENERS.remove(listener);
        }
    }


    private static final long VOLUMN_UPDATE_INTERVAL = 50;
    private final Set<OnVolumeChangeListener> LISTENERS = new HashSet<>();
    private final Handler HANDLER = new Handler(Looper.getMainLooper());
    private final Runnable VOLUME_NOTIFIER = new Runnable() {
        @Override
        public void run() {
            for (OnVolumeChangeListener listener : LISTENERS) {
                if (listener != null && mAudioCapturer != null) {
                    listener.onVolumeChange(mAudioCapturer.getVolume());
                }
            }
            HANDLER.postDelayed(this, VOLUMN_UPDATE_INTERVAL);
        }
    };


    private Binder mBinder = new AudioCaptureBinder();
    private AudioCapturer mAudioCapturer;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HANDLER.post(VOLUME_NOTIFIER);
        mAudioCapturer = new AudioCapturer();
        try {
            mAudioCapturer.startCapture();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.str_permission, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HANDLER.removeCallbacksAndMessages(null);
        LISTENERS.clear();
        mAudioCapturer.stopCapture();
        mAudioCapturer = null;
    }
}
