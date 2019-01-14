package com.albertech.demo.scallop;

import android.graphics.LinearGradient;



/**
 * Created by Albert on 2018/1/17.
 */
public class InputSourceFeature {

    public float peakCount;
    public float phaseSpeed;
    public float baseOffeetY;
    public float cycleOffsetY;
    public int[] colors;
    public float[] positions;

    public LinearGradient gradient;
    public float phase;

    public InputSourceFeature(float peakCount, float phaseSpeed, float baseOffeetY, float cycleOffsetY, int[] colors, float[] positions) {
        this.peakCount = peakCount;
        this.phaseSpeed = phaseSpeed;
        this.baseOffeetY = baseOffeetY;
        this.cycleOffsetY = cycleOffsetY;
        this.colors = colors;
        this.positions = positions;
    }
}
