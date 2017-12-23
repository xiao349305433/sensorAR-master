package com.coredump.synergyar.android.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;
import java.util.List;


/**
 * Class for displaying the camera view
 * @author fabio
 * @version 0.0.1
 * @since 0.0.1
 * @see Camera
 * @see SurfaceHolder
 */
public class CameraPreview extends SurfaceView implements Preview {//, SurfaceHolder.Callback {
    private static final String TAG = CameraPreview.class.getName();
    //device camera
    private Camera mCamera;
    //pass image data from the camera to the application
    private SurfaceHolder mHolder;
    private boolean mReady;


    public CameraPreview(Context context) {
        super(context);
        Log.d(TAG, "Constructor");
        initialize();
    }

    @Override
    public View getView() {
        View view = this;
        return view;
    }

    private void initialize() {
        Log.d(TAG, "Initializing object");
        mReady = false;
        //monitor changes to the surface
        mHolder = this.getHolder();
        mHolder.addCallback(new SurfaceCallback());
        // We're changing the surface to a PUSH surface, meaning we're receiving
        // all buffer data from another component - the mCamera, in this case.
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    //Public interface

    @Override
    public void start() {
        Log.d(TAG, "Starting camera preview");
        if(mCamera != null) {
            mCamera.startPreview();
            mReady = true;
        }
    }

    @Override
    public void stop() {
        Log.d(TAG, "Stopping camera preview");
        // stop preview before making changes
        if(mCamera!=null) {
            mCamera.stopPreview();
            mReady = false;
        }
    }

    @Override
    public boolean hasCamera() {
        return (mCamera != null);
    }

    private class SurfaceCallback implements  SurfaceHolder.Callback {
        private final String INNER_TAG = SurfaceCallback.class.getName();

        public void surfaceCreated(SurfaceHolder mHolder) {
            Log.d(INNER_TAG, "Create surface");
            mCamera = CameraPreview.safeCameraOpen(Camera.CameraInfo.CAMERA_FACING_BACK, CameraPreview.this.getContext());
        }

        // This method is called when the surface changes, e.g. when it's size is set.
        @Override
        public void surfaceChanged(SurfaceHolder mHolder, int format, int width, int height) {
            Log.d(INNER_TAG, "Change on surface");
            //if there is no surface return
            if (mHolder.getSurface() == null || !hasCamera()) {
                return;
            }
            if(mReady) {
                // Stop the preview before resizing or reformatting it.
                CameraPreview.this.stop();

                // set preview size and make any resize, rotate or
                // reformatting changes here
                Camera.Parameters params = mCamera.getParameters();

                //Get the device's supported sizes and pick the first,
                // which is the largest
                List<Camera.Size> sizes = params.getSupportedPreviewSizes();
                Camera.Size selected = sizes.get(0);

                params.setPreviewSize(selected.width, selected.height);
                mCamera.setParameters(params);
            }
            try {
                mCamera.setPreviewDisplay( mHolder );
            } catch( IOException e ) {
                Log.e(INNER_TAG,"Something went wrong with the hardware", e);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder mHolder) {
            Log.d(INNER_TAG, "Destroy surface");
            if(mCamera!=null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        }
    }

    /**
     * Checks if the device has a {@link Camera) needs
     * the {@link Context} to get the {@link PackageManager}
     * <p> This method should be run before the opening the camera
     *
     * @param context application context, needed to check device hardware
     *               using the PackageManager.
     */
    private static boolean hasCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * This class initializes the camera object in a safe way
     *
     * <p> method uses this method to
     * get an instance of the {@link Camera}
     * @param cameraId identifies the device camera
     * @param context check the existence of the camera
     * @return a Camera instance
     */
    public static Camera safeCameraOpen(int cameraId, Context context){
        //useful if we need to send a notification to the user
        Log.d(TAG, "Getting mCamera");
        Camera camera = null;
        //@// TODO: 10/31/15 algorithm to wait response for mCamera
        if(hasCameraHardware(context))
            camera = Camera.open(cameraId);
        return camera;
    }
    /****************************************************************************************/
}