package com.coredump.synergyar.android.camera;

import com.coredump.synergyar.ar.hardware.DeviceCameraController;
import com.coredump.synergyar.android.SynergyActivity;

import android.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;

//DeviceCameraController
/**
 * This class controls the flow of the camera
 * @author fabio
 * @version 0.0.1
 * @since 0.0.1
 * @see SynergyActivity
 * @see Preview
 */
public class CameraController implements DeviceCameraController{//, Camera.PictureCallback, Camera.AutoFocusCallback {

    private static final String TAG = CameraController.class.getName();
    private static final int ONE_SECOND_IN_MILI = 1000;
    private final SynergyActivity mActivity;
    private final Preview mPreview;

    public CameraController(SynergyActivity activity, Preview preview) {
        Log.d(TAG, "Constructor");
        this.mActivity = activity;
        this.mPreview = preview;
    }


    @Override
    public synchronized void prepareCamera() {
        Log.d(TAG,"Sync PrepareCamera");
        if (mPreview != null) {
            View view = mPreview.getView();
            mActivity.addContentView(view, new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
   }

    @Override
    public synchronized void startPreview() {
        Log.d(TAG,"Sync StartPreview");
        // ...and start previewing. From now on, the camera keeps pushing
        // preview
        // images to the surface.
        if (mPreview != null && mPreview.hasCamera()) {
            mPreview.start();
        }
    }

    @Override
    public synchronized void stopPreview() {
        Log.d(TAG,"Sync StopPreview");
        // stop previewing.
        if (mPreview != null) {
            View view = mPreview.getView();
            ViewParent parentView = view.getParent();
            if (parentView instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) parentView;
                viewGroup.removeView(view);
            }
        }
        mActivity.restoreFixedSize();
    }

    //async methods
    @Override
    public void prepareCameraAsync() {
        Log.d(TAG,"Async PrepareCamera");
        Runnable runnable = new Runnable() {
            public void run() {
                prepareCamera();
            }
        };
        mActivity.post(runnable);
    }

    @Override
    public synchronized void startPreviewAsync() {
        Log.d(TAG, "Async PreviewAsync");
        Runnable runnable = new Runnable() {
            public void run() {
                startPreview();
            }
       };
        mActivity.post(runnable);
    }

    @Override
    public synchronized void stopPreviewAsync() {
        Log.d(TAG,"ASync StopPreview");
        Runnable runnable = new Runnable() {
            public void run() {
                stopPreview();
            }
        };
        mActivity.post(runnable);
    }

    @Override
    public boolean isReady() {
        //Log.d(TAG, "Is Ready?");
        boolean ready = false;
        if (mPreview != null) {
            ready = mPreview.hasCamera();
        }
        return ready;
    }
}