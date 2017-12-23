package com.coredump.synergyar.ar.hardware;

/**
 * @author fabio, 12/11/15
 * @version 0.0.1
 * @since 0.0.1
 */
public interface OrientationSensor {
    void start();
    void setLookAtOffset(float x,float y,float z);
    void setUp();
    void finish();

    float[] lowPass(float[] input, float[] output, float alpha);
    float[] getOldOrientation();
    boolean getStable();
    float[] getMatrix();
    float[] getLookAt();
    float[] getUp();
}