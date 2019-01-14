package com.albertech.demo.scallop;

import com.albertech.demo.R;
import com.albertech.demo.ResUtil;


/**
 * Created by Albert on 2018/1/17.
 */
public class ScallopDecorator implements IWaveDecorator {

    @Override
    public InputSourceFeature[] getInputSourceFeatures() {
        int purple = ResUtil.getColor(R.color.colorSapphire);
        int blue = ResUtil.getColor(R.color.colorAmethystine);
        InputSourceFeature[] inputSourceFeatures = new InputSourceFeature[2];
        inputSourceFeatures[0] = new InputSourceFeature(1.8f, 0.0038f, 0, 0.05f, new int[]{purple, blue}, new float[]{0.0f, 1.0f});
        inputSourceFeatures[1] = new InputSourceFeature(2.6f, -0.0026f, 0.05f, -0.03f, new int[]{blue, purple}, new float[]{0.0f, 1.0f});
        return inputSourceFeatures;
    }

    @Override
    public boolean isPhaseIncreaseAuto() {
        return true;
    }
}
