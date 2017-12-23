package com.coredump.synergyar.ar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.coredump.synergyar.ar.hardware.OrientationSensor;

/**
 * Created by fernando on 08-Nov-15.
 */
public class PerspectiveAR extends PerspectiveCamera{
    private static String TAG = PerspectiveAR.class.getName();
    public OrientationSensor mOrientation;
    protected float [] orientationMatrix;
    protected float [] lookVector;
    protected float [] upVector;
    protected boolean mIsGeo ;

    public PerspectiveAR(OrientationSensor orientation) {
        super();
        mOrientation = orientation;
        mOrientation.start();
        mIsGeo = false;
    }

    public PerspectiveAR(OrientationSensor orientation, float f, int i, int j) {
        super(f,i,j);
        mOrientation = orientation;
        mOrientation.start();
    }

    public void setPosition(float x,float y,float z){
        this.position.set(x, y, z);
        mOrientation.setLookAtOffset(x,y,z);
    }

    public void render(){
        Gdx.app.log(TAG, "Rendering");
        this.update();
        orientationMatrix = mOrientation.getMatrix();
        upVector = mOrientation.getUp();
        this.up.set(upVector[0],upVector[1], upVector[2]);
        lookVector = mOrientation.getLookAt();
        this.lookAt(lookVector[0],lookVector[1],lookVector[2]);
        Gdx.app.log(TAG, orientationMatrix +"  "+lookVector);
    }

    // TODO: 12/11/15 why overriding
    @Override
    public void update() {
        Gdx.app.log(TAG, "Updating");
        super.update();
    }
    // TODO: 12/11/15 why overriding
    @Override
    public void update(boolean updateFrustum) {
        super.update(updateFrustum);
    }

    public void dispose(){
        mOrientation.finish();
        mOrientation = null;
        orientationMatrix = null;
        lookVector = null;
        upVector = null;
    }

    public float[] getOrientationMatrix(){
        return orientationMatrix;
    }

    public float[] geUpVector(){
        return upVector;
    }

    public float[] getLookVector(){
        float [] out = new float[3];
        out[0] = lookVector[0];
        out[1] = lookVector[1];
        out[2] = lookVector[2];
        return out;
    }

    public boolean getStability(){
        return mOrientation.getStable();
    }
}