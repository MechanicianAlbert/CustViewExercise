package com.albertech.demo.siri;

public class SiriWaveFeature {

    public int color;
    public SiriWaveDimens[] dimens;

    public static class SiriWaveDimens {
        public float amplitude;
        public float angularVelocity;
        public float originalPhase;
        public float phaseVelocity;

        public SiriWaveDimens(float amplitude, float angularVelocity, float originalPhase, float phaseVelocity) {
            this.amplitude = amplitude;
            this.angularVelocity = angularVelocity;
            this.originalPhase = originalPhase;
            this.phaseVelocity = phaseVelocity;
        }
    }
}
