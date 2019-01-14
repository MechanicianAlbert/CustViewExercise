package com.albertech.demo.aqua;


import com.albertech.demo.R;
import com.albertech.demo.ResUtil;



/**
 * Created by Albert on 2018/1/17.
 */
public class AquaDecorator implements IWaveDecorator {

    @Override
    public InputSourceFeature[] getInputSourceFeatures() {
        int trans = ResUtil.getColor(R.color.colorTrans);
        int purple = ResUtil.getColor(R.color.colorPurple);
        int blue = ResUtil.getColor(R.color.colorBlue);
        int cyan = ResUtil.getColor(R.color.colorCyan);
        InputSourceFeature[] inputSourceFeatures = new InputSourceFeature[2];
        inputSourceFeatures[0] = new InputSourceFeature(4, 0.4f, 0.01f, new int[]{trans, cyan, blue, trans}, new float[]{0.0f, 0.15f, 0.85f, 1.0f});
        inputSourceFeatures[1] = new InputSourceFeature(6, 0.9f, 0.01f, new int[]{trans, blue, purple, trans}, new float[]{0.0f, 0.15f, 0.85f, 1.0f});
        return inputSourceFeatures;
    }

    @Override
    public int getPeakCount() {
        return 26;
    }

    @Override
    public int getInputIgnore() {
        return 1;
    }

    @Override
    public boolean isPhaseIncreaseAuto() {
        return true;
    }

    @Override
    public float getPhaseIncreaseSpeed() {
        return -0.15f;
    }
}
