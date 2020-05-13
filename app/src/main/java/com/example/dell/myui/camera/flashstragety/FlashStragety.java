package com.example.dell.myui.camera.flashstragety;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;

public interface FlashStragety {
    void setCaptureRequest(CaptureRequest.Builder requestBuilder, CameraCaptureSession cameraCaptureSession,
                           Handler handler);
}
