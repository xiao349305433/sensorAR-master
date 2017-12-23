package com.coredump.synergyar.ar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.coredump.synergyar.ar.hardware.DeviceCameraController;


/**
 * @author fabio
 * @version 0.0.1
 * @since 0.0.1
 * Created by fabio on 11/1/15.
 */
public class SynergyAdapter extends Game {
    private static final String TAG = SynergyAdapter.class.getName();;
    private final DeviceCameraController mDeviceCameraController;
    private ScreenAdapter mDisplay;
    private Mode mode = Mode.normal;
    private PerspectiveAR mCamera;

    public SynergyAdapter(DeviceCameraController cameraControl,PerspectiveAR perspectiveCamera) {
        mDeviceCameraController = cameraControl;
        mCamera = perspectiveCamera;
    }

    public enum Mode {
        normal,
        prepare,
        preview
    }

    @Override
    public void create() {
        Gdx.app.log(TAG, "Create");

        //TODO PUT this in PerspectiveAR
        //Is here because it needs the App listener to be initialized
        mCamera.fieldOfView = 67;
        mCamera.viewportWidth = Gdx.graphics.getWidth();
        mCamera.viewportHeight = Gdx.graphics.getHeight();
        mDisplay = new Display(mCamera);
        setScreen(mDisplay);
    }

    @Override
    public void dispose() {
        Gdx.app.log(TAG, "OnDispose");
        mDisplay.dispose();
    }

    @Override
    public void render() {
        Gdx.app.log(TAG, "Rendering");
        if (mode == Mode.normal) {
            if (mDeviceCameraController != null) {
                mode = Mode.prepare;
                mDeviceCameraController.prepareCameraAsync();
            }
        }
        if (mode == Mode.prepare) {
            if (mDeviceCameraController != null && mDeviceCameraController.isReady()) {
                mode = Mode.preview;
                mDeviceCameraController.startPreviewAsync();
            }
        }
        //mCamera.update();
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log(TAG, "Resizing");

    }

	@Override
	public void pause() {
        Gdx.app.log(TAG, "OnPause");
        if(mDeviceCameraController!= null && mDeviceCameraController.isReady()) {
            mode = Mode.normal;
            mDeviceCameraController.stopPreviewAsync();
        }
	}

	@Override
	public void resume() {
        Gdx.app.log(TAG,"OnResume");
	}

}
