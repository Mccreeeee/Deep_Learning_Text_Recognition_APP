package com.example.dell.myui.activity;

import com.example.dell.myui.camera.flashstragety.FlashStragety;
import com.example.dell.myui.camera.flashstragety.AutoStragety;
import com.example.dell.myui.camera.flashstragety.CloseStragety;
import com.example.dell.myui.camera.flashstragety.KeepOpenStragety;
import com.example.dell.myui.camera.flashstragety.OpenStragety;
import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.myui.R;
import com.example.dell.myui.utils.timeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class SelfCameraActivity extends AppCompatActivity implements View.OnClickListener{
    //自定义相机界面
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final String TAG = "SelfCameraActivity";
    private File picturePath;

    ///为了使照片竖直显示
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private View flashLayout;
    private TextView tv_flashStatus, tv_flashAuto,
            tv_flashKeepOpen, tv_flashOpen, tv_flashClose;
    private ImageButton btn_takePhoto;
    private ImageView iv_album, iv_changeCamera, iv_flash;
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    private Handler childHandler;
    private Handler mainHandler;
    private int cameraID;
    private ImageReader imageReader;
    private CameraDevice cameraDevice;//摄像头设备
    private CameraManager cameraManager;
    private CameraCaptureSession cameraCaptureSession;
    public final int FLASH_ON = 1;   //闪光灯状态
    public final int FLASH_OFF = 2;
    private CaptureRequest.Builder previewRequestBuilder;
    public final int BACK_CAMERA = 0;//前后摄像头
    public final int FRONT_CAMERA = 1;

    //使用相册中的图片
    public static final int SELECT_PIC_CODE = 3;
    //图片裁剪

    //定义图片的Uri
    private Uri photoUri=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        getSupportActionBar().hide();
        setContentView(R.layout.activity_camera);
        initView();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "对不起，没有权限，无法正常使用相机", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            initCamera2();
        }
    }

    private void initView() {
        iv_album = findViewById(R.id.iv_album);
        iv_changeCamera = findViewById(R.id.iv_change);
        iv_flash = findViewById(R.id.iv_flash);
        surfaceView = findViewById(R.id.surfaceView);
        tv_flashStatus = findViewById(R.id.tv_flash_status);
        btn_takePhoto = findViewById(R.id.btn_take_photo);


        flashLayout = findViewById(R.id.layout_flash_text);
        tv_flashAuto = findViewById(R.id.tv_flash_auto);
        tv_flashKeepOpen = findViewById(R.id.tv_flash_keep_open);
        tv_flashClose = findViewById(R.id.tv_flash_close);
        tv_flashOpen = findViewById(R.id.tv_flash_open);

        tv_flashAuto.setOnClickListener(this);
        tv_flashKeepOpen.setOnClickListener(this);
        tv_flashOpen.setOnClickListener(this);
        tv_flashClose.setOnClickListener(this);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setKeepScreenOn(true);//surfaceView设置回调
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //初始化camera,获取摄像头权限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        findViewById(R.id.btn_take_photo).setClickable(false);
                    } else {
                        initCamera2();
                    }
                } else {
                    initCamera2();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //释放camera资源
                if (cameraDevice != null) {
                    cameraDevice.close();
                    cameraDevice = null;
                }
            }
        });

        //拍照
        btn_takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        //点击闪光灯图标
        iv_flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(SelfCameraActivity.this, R.anim.flash_in);
                flashLayout.setAnimation(anim);
                flashLayout.setVisibility(View.VISIBLE);
            }
        });
        //相册获取
        iv_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGallery();
            }
        });
        //更换摄像头
        iv_changeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCamera();
            }
        });
    }

    @SuppressLint({"MissingPermission", "NewApi"})
    private void changeCamera() {
        try {
            //先关闭之前的摄像头
            if (cameraDevice != null) {
                cameraDevice.close();
                cameraDevice = null;
            }
            cameraID ^= 1;
            Toast.makeText(SelfCameraActivity.this, cameraID+"", Toast.LENGTH_LONG).show();
            cameraManager.openCamera(cameraID + "", stateCallback, mainHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    private void initCamera2() {
        btn_takePhoto.setClickable(true);
        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        childHandler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(getMainLooper());
        Log.d(TAG, "initCamera2: " + CameraCharacteristics.LENS_FACING_BACK);
        cameraID = BACK_CAMERA;//后摄像头
        int width = surfaceView.getWidth();
        int height = surfaceView.getHeight();
        Toast.makeText(SelfCameraActivity.this,width+" and " + height,Toast.LENGTH_LONG).show();
        imageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
        imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            //可以在这里处理拍照得到的临时照片 例如，写入本地
            @Override
            public void onImageAvailable(ImageReader reader) {
                // 拿到拍照照片数据
                reader.getWidth();
                Toast.makeText(SelfCameraActivity.this,reader.getWidth()+" and " + reader.getHeight(),Toast.LENGTH_LONG).show();
                Image image = reader.acquireNextImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);//由缓冲区存入字节数组
                //保存

                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                if (bitmap != null) {
                    if (cameraID == FRONT_CAMERA) {
                        //前置摄像头拍的要先旋转180度
                        Toast.makeText(SelfCameraActivity.this, "前置摄像头", Toast.LENGTH_LONG).show();
                        bitmap = adjustPhotoRotation(bitmap, 180);
                    }
                    writeToFile(bitmap);
                    if(photoUri==null)
                    {
                        Toast.makeText(SelfCameraActivity.this, "uri==null", Toast.LENGTH_LONG).show();
                    }
                    else{

                        gotoCrop();//跳转到剪裁界面
                    }
                }
                image.close();
            }

        }, mainHandler);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            //打开摄像头
            cameraManager.openCamera(cameraID + "", stateCallback, mainHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    ;

    private void writeToFile(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory(), "拍照");
        if(!file.exists()){
            file.mkdir();
        }
        picturePath = new File(file, timeUtils.getCurrentTime() + ".jpg");
        try {
            OutputStream os = new FileOutputStream(picturePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            //qualty,30 是压缩率，表示压缩70%; 如果不压缩是100，表示压缩率为0
            photoUri=Uri.fromFile(picturePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(SelfCameraActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            return bm1;
        } catch (OutOfMemoryError ex) {
        }
        return null;
    }

    /*开始预览*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void takePreview() {
        try {
            //创建预览需要的CaptureRequest.Builder
            previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            //将surfaceView的surface作为目标
            previewRequestBuilder.addTarget(surfaceHolder.getSurface());
            // 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求
            cameraDevice.createCaptureSession(Arrays.asList(surfaceHolder.getSurface(), imageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    if (cameraDevice == null) return;
                    //当摄像头准备好时,准备预览
                    cameraCaptureSession = session;
                    try {
                        // 自动对焦
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        // 打开闪光灯
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.FLASH_MODE_OFF);
                        // 显示预览
                        CaptureRequest previewRequest = previewRequestBuilder.build();
                        cameraCaptureSession.setRepeatingRequest(previewRequest, null, childHandler);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                    Toast.makeText(SelfCameraActivity.this, "配置失败", Toast.LENGTH_SHORT).show();
                }
            }, mainHandler);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    public void changeFlash(int type) {
        try {
            CaptureRequest.Builder requestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            requestBuilder.addTarget(surfaceHolder.getSurface());
            if (type == FLASH_ON)
                requestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            else if (type == FLASH_OFF)
                requestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.FLASH_MODE_OFF);
            cameraCaptureSession.setRepeatingRequest(requestBuilder.build(), null, childHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //摄像头创建监听
    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onOpened(CameraDevice camera) {     //打开摄像头
            cameraDevice = camera;
            //开启预览
            takePreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {//关闭摄像头
            if (cameraDevice != null)
                cameraDevice.close();
            SelfCameraActivity.this.cameraDevice = null;
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            Toast.makeText(SelfCameraActivity.this, "摄像头开启失败", Toast.LENGTH_SHORT).show();
        }
    };

    //拍照
    @SuppressLint("NewApi")
    private void takePicture() {
        if (cameraDevice == null) return;
        // 创建拍照需要的CaptureRequest.Builder
        final CaptureRequest.Builder captureRequestBuilder;
        try {
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            // 将imageReader的surface作为CaptureRequest.Builder的目标
            captureRequestBuilder.addTarget(imageReader.getSurface());
            // 自动对焦
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            // 自动曝光
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            // 获取手机方向
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            // 根据设备方向计算设置照片的方向
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            //拍照
            CaptureRequest mCaptureRequest = captureRequestBuilder.build();
            cameraCaptureSession.capture(mCaptureRequest, null, childHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        FlashStragety strategy = null;
        switch (v.getId()) {
            case R.id.tv_flash_close:
                tv_flashStatus.setText("关闭");
                strategy = new CloseStragety();
                break;
            case R.id.tv_flash_auto:
                tv_flashStatus.setText("自动");
                strategy = new AutoStragety();
                break;
            case R.id.tv_flash_keep_open:
                tv_flashStatus.setText("常亮");
                strategy = new KeepOpenStragety();
                break;
            case R.id.tv_flash_open:
                tv_flashStatus.setText("打开");
                strategy = new OpenStragety();
                break;
        }
        if (strategy != null) {
            //重新设置背景颜色
            clearFlashTextBackground(v.getId());
            //消失动画
            closeFlashLayout();
            //重新设置闪光灯
            strategy.setCaptureRequest(previewRequestBuilder, cameraCaptureSession, childHandler);
        }
    }

    public void closeFlashLayout() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.flash_out);
        flashLayout.setAnimation(anim);
        flashLayout.setVisibility(View.GONE);
    }

    public void clearFlashTextBackground(int id) {
        tv_flashClose.setBackground(null);
        tv_flashOpen.setBackground(null);
        tv_flashKeepOpen.setBackground(null);
        tv_flashAuto.setBackground(null);
        findViewById(id).setBackground(getResources().getDrawable(R.drawable.flash_text_shape));
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode== Activity.RESULT_OK)
        {
            //相册获取图片
            if(requestCode==SELECT_PIC_CODE)
            {
                if (null != data && null != data.getData()) {
                    photoUri = data.getData();
                    gotoCrop();
                } else {
                    Toast.makeText(this, "图片选择失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    //打开系统相册
    private void gotoGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_PIC_CODE);

    }

    //裁剪(参数是需要裁剪的图片的uri)
    private void gotoCrop() {
        Intent intent=new Intent(this,ImageCropActivity.class);
        intent.setData(photoUri);
        startActivity(intent);
    }

}
