package com.example.dell.myui;

import android.graphics.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class CameraActivity extends AppCompatActivity {
     private SurfaceHolder holder;
     private SurfaceView surfaceView;
     private CircleImageView iv_cancel;
     private CircleImageView iv_takephoto;
     private CircleImageView iv_finish;
     private boolean status;
     private android.hardware.Camera camera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initView();
    }

    public void initView()
    {
        surfaceView=findViewById(R.id.surfaceView);
        iv_cancel=findViewById(R.id.iv_cancel);
        iv_takephoto=findViewById(R.id.iv_take_photo);
        iv_finish=findViewById(R.id.iv_finish);
        iv_finish.setVisibility(View.GONE);
    }
    public void initCamera()
    {
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }


}
