package com.coredump.synergyar.android.sensors.orientation;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.badlogic.gdx.math.Matrix4;
import com.coredump.synergyar.android.utils.BufferAlgo;
import com.coredump.synergyar.ar.hardware.OrientationSensor;

import java.util.concurrent.Semaphore;

/**
 * Created by fernando on 08-Nov-15.
 */
public class AndroidOrientationSensor implements OrientationSensor{
    protected static final float CHANGE_FACT = 3.5f;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private SensorEventListener mListener;
    private float mMatrix[] = new float[16];
    private BufferAlgo mAccelerometerBuffer;
    private BufferAlgo mMagnetometerBuffer;
    private float[] mLookAt = { 0, 0, -100, 1 };
    private float[] mUp = { 0, 1, 0, 1 };
    private float[] mPosition = { 0, 0, 0 };
    private float mFar;
    private Sensor mOrientationSensor;
    private boolean mStable;
    private Sensor mSensorLinAcce;
    private float mOrientation[] = new float[3];
    private float mAcceleration[] = new float[3];
    private float mOldOrientation[];
    private float mOldAcceleration[];
    private float mRotation[];
    private float[] mNewMat;
    private Matrix4 mMatT;
    private Semaphore mClear = new Semaphore(1);
    private boolean mQuit;

    public AndroidOrientationSensor(Context context, float mFar, boolean isMarker) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometerBuffer = new BufferAlgo(0.1f, 0.2f);
        mMagnetometerBuffer = new BufferAlgo(0.1f, 0.2f);
        this.mFar = mFar;
    }
    public AndroidOrientationSensor(Context context){
        this(context,1000,true);
    }

    public float[] getOldOrientation() {
        return mRotation;
    }

    public void start() {
        mListener = new MyOrientationListener();
        mAccelerometer = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
        mMagnetometer = mSensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).get(0);
        mSensorManager.registerListener(mListener, mAccelerometer,
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(mListener, mMagnetometer,
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(mListener, mOrientationSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(mListener, mSensorLinAcce,
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    public boolean getStable() {
        return mStable;
    }

    public float[] getMatrix() {
        return mMatrix;
    }

    public float[] getLookAt() {
        float[] out = new float[3];
        out[0] = mLookAt[0];
        out[1] = mLookAt[1];
        out[2] = mLookAt[2];
        return out;
    }

    public float[] getUp() {
        float[] out = new float[3];
        out[0] = mUp[0];
        out[1] = mUp[1];
        out[2] = mUp[2];
        return out;
    }

    public void setLookAtOffset(float x, float y, float z) {
        mPosition[0] = x;
        mPosition[1] = y;
        mPosition[2] = z;
    }

    public void setUp() {

    }

    public void finish() {
        mSensorManager.unregisterListener(mListener);
        mSensorManager = null;

        try {
            mClear.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mAccelerometer = null;
        mMagnetometer = null;
        mListener = null;
        mMatrix = null;
        mAccelerometerBuffer = null;
        mMagnetometerBuffer = null;
        mLookAt = null;
        mUp = null;
        mPosition = null;
        mOrientationSensor = null;
        mSensorLinAcce = null;
        mOrientation = null;
        mAcceleration = null;
        mOldOrientation = null;
        mOldAcceleration = null;
        mRotation = null;
        mMatT = null;
        mNewMat = null;
        mQuit = true;
        mClear.release();

    }
    // LowPass Filter
    public float[] lowPass(float[] input, float[] output, float alpha) {
        if (output == null)
            return input;

        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + alpha * (input[i] - output[i]);
        }
        return output;
    }

    public class MyOrientationListener implements SensorEventListener {

        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        public void onSensorChanged(SensorEvent evt) {

            if (mClear.tryAcquire()) {
                if (!mQuit) {
                    int type = evt.sensor.getType();
                    // Smoothing the sensor data a bit seems like a good
                    // idea.
                    if (type == Sensor.TYPE_MAGNETIC_FIELD) {
                        mOrientation = lowPass(evt.values, mOrientation, 0.1f);
                    } else if (type == Sensor.TYPE_ACCELEROMETER) {

                        mAcceleration = lowPass(evt.values, mAcceleration,
                                0.05f);
                    }
                    if (mOldAcceleration != null || mOldOrientation != null) {
                        mAccelerometerBuffer.execute(mOldAcceleration,
                                mAcceleration);
                        mMagnetometerBuffer.execute(mOldOrientation,
                                mOrientation);
                    } else {
                        mOldAcceleration = mAcceleration;
                        mOldOrientation = mOrientation;
                    }

                    if ((type == Sensor.TYPE_MAGNETIC_FIELD)
                            || (type == Sensor.TYPE_ACCELEROMETER)) {
                        mNewMat = new float[16];
                        SensorManager.getRotationMatrix(mNewMat, null,
                                mOldAcceleration, mOldOrientation);
                        SensorManager.remapCoordinateSystem(mNewMat,
                                SensorManager.AXIS_Y,
                                SensorManager.AXIS_MINUS_X, mNewMat);
                        mMatT = new Matrix4(mNewMat).tra();
                        float[] newLookAt = { 0, 0, -mFar, 1 };
                        float[] newUp = { 0, 1, 0, 1 };
                        Matrix4.mulVec(mMatT.val, newLookAt);
                        Matrix4.mulVec(mMatT.val, newUp);
                        mMatrix = mMatT.val;
                        mLookAt[0] = newLookAt[0] + mPosition[0];
                        mLookAt[1] = newLookAt[1] + mPosition[1];
                        mLookAt[2] = newLookAt[2] + mPosition[2];

                        mUp = newUp;
                        newLookAt = null;
                        newUp = null;
                    }
                    mClear.release();
                }
            }
        }

    }
}
