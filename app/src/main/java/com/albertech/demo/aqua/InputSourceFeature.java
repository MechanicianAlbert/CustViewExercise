package com.albertech.demo.aqua;

import android.graphics.LinearGradient;


/**
 * Created by Albert on 2018/1/17.
 */
public class InputSourceFeature {

    public int strokeWidth;
    public float maxHeightRatio;
    public float minHeightRatio;
    public int[] colors;
    public float[] positions;
    public LinearGradient gradient;

    public InputSourceFeature(int strokeWidth, float maxHeightRatio, float minHeightRatio, int[] colors, float[] positions) {
        this.strokeWidth = strokeWidth;
        this.colors = colors;
        this.positions = positions;
        this.maxHeightRatio = maxHeightRatio;
        this.minHeightRatio = minHeightRatio;
    }
}
