package com.albertech.demo.liquid;


import com.albertech.demo.R;
import com.albertech.demo.ResUtil;


/**
 * Created by Albert on 2018/1/17.
 */
public class LiquidDecorator implements IWaveDecorator {


    @Override
    public InputSourceFeature[] getInputSourceFeatures() {
        int color1 = ResUtil.getColor(R.color.colorSapphire);
        int color2 = ResUtil.getColor(R.color.colorAmethystine);
        int color3 = ResUtil.getColor(R.color.colorAmethystine);
        int color4 = ResUtil.getColor(R.color.colorSapphire);
        InputSourceFeature[] inputSourceFeatures = new InputSourceFeature[2];
        inputSourceFeatures[0] = new InputSourceFeature(0.85f, -19f, 0, 1f, new int[]{color1, color2}, new float[]{0.0f, 1.0f});
        inputSourceFeatures[1] = new InputSourceFeature(0.75f, -12f, 0.05f, 2f, new int[]{color3, color4}, new float[]{0.0f, 1.0f});
        return inputSourceFeatures;
    }

    @Override
    public boolean isPhaseIncreaseAuto() {
        return false;
    }
}
