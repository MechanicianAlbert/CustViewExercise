package com.albertech.demo.audio;

import android.media.MediaRecorder;

import com.albertech.demo.util.FileUtil;


/**
 * Created by Albert on 2019/1/15.
 */
class AudioCapturer {

    private static class RecordInterceptByPermissionDeniedException extends Exception {

    }

    private final static int SAMPLE_RATE_IN_HZ = 8000; // 采样率
    private final MediaRecorder CAPTURER = new MediaRecorder();


    private boolean mIsCapturing;


    AudioCapturer() {

    }

    boolean isCapturing() {
        return mIsCapturing;
    }

    float getVolume() {
        float volume;
        try {
            volume = (float) Math.pow(Math.log(CAPTURER.getMaxAmplitude()) / Math.log(2), 2);
        } catch (Exception e) {
            volume = 0;
        }
        return volume;
    }

    void startCapture() throws RecordInterceptByPermissionDeniedException {
        try {
            CAPTURER.setAudioSource(MediaRecorder.AudioSource.MIC);
            CAPTURER.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            CAPTURER.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            CAPTURER.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
            CAPTURER.setOutputFile(FileUtil.getExternalCacheDirPath() + "/temp.amr");
            CAPTURER.prepare();
            CAPTURER.start();
            mIsCapturing = true;
        } catch (Exception e) {
            throw new RecordInterceptByPermissionDeniedException();
        }
    }

    void stopCapture() {
        try {
            mIsCapturing = false;
            CAPTURER.stop();
            CAPTURER.reset();
            CAPTURER.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}