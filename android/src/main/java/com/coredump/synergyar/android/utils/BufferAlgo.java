package com.coredump.synergyar.android.utils;

/**
 * @author fabio, 12/11/15
 * @version 0.0.1
 * @since 0.0.1
 */
// Filter Class1
public class BufferAlgo {

    private final float a;
    private final float b;
    private final float m;
    private final float n;

    public BufferAlgo(float a, float b) {
        this.a = a;
        this.b = b;
        m = 1f / (b - a);
        n = a / (a - b);
    }

    public boolean execute(float[] target, float[] values) {
        target[0] = morph(target[0], values[0]);
        target[1] = morph(target[1], values[1]);
        target[2] = morph(target[2], values[2]);
        return true;
    }

    /**
     * @param v
     * @param newV
     * @return newT=t+f(|v-t|)
     */
    private float morph(float v, float newV) {
        float x = newV - v;
        if (x >= 0) {
            if (x < a)
                return v; // v+0*x
            if (b <= x)
                return newV; // v+1*x
            return v + x * m + n;
        } else {
            if (-x < a)
                return v;
            if (b <= -x)
                return newV;
            return v + x * m + n;
        }
    }

}