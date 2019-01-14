package com.albertech.demo.aqua;



/**
 * Created by Albert on 2018/1/17.
 */
public interface IWaveDecorator {

    InputSourceFeature[] getInputSourceFeatures();

    int getPeakCount();

    int getInputIgnore();

    boolean isPhaseIncreaseAuto();

    float getPhaseIncreaseSpeed();
}
