package com.coredump.synergyar.ar.hardware;


/**
 * @author fabio
 * @version 0.0.1
 * @since 0.0.1
 * Created by fabio on 11/1/15.
 */

public interface DeviceCameraController {

    // Synchronous interface
    //void saveAsJpeg(FileHandle jpgfile, Pixmap cameraPixmap);

    boolean isReady();

    /**
     * Prepares the device for displaying images
     * Adds the camera view to the
     */
    void prepareCamera();

    void startPreview();

    void stopPreview();

    //Async needed when calling from libgdx
    /** Asynchronous interface - need when called from a non platform thread (GDX OpenGl thread)
     * called asynchronous from the Libgdx rendering thread
     */
    void prepareCameraAsync();

    /** Asynchronous interface - need when called from a non platform thread (GDX OpenGl thread)
     * called asynchronous from the Libgdx rendering thread
     */
    void startPreviewAsync();

    /** Asynchronous interface - need when called from a non platform thread (GDX OpenGl thread)
     * called asynchronous from the Libgdx rendering thread
     */
    void stopPreviewAsync();

}
