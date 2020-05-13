package com.example.dell.myui.camera.flashstragety;

import android.annotation.SuppressLint;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;

public class OpenStragety implements FlashStragety {
    @SuppressLint("NewApi")
    @Override
    public void setCaptureRequest(CaptureRequest.Builder requestBuilder, CameraCaptureSession cameraCaptureSession, Handler handler) {
        requestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH);
        requestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);

        try {
            cameraCaptureSession.setRepeatingRequest(requestBuilder.build(),null,handler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
