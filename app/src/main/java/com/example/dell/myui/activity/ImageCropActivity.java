package com.example.dell.myui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.dell.myui.R;
import com.example.dell.myui.camera.CropImageView.ClipImageView;
import com.example.dell.myui.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class ImageCropActivity extends AppCompatActivity {
    //自定义剪裁界面
    private ClipImageView clipImageView;
    private Button btnCrop;
    private ImageView testImage;
    private Uri photoUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);
        getSupportActionBar().hide();
        init();
    }
    void init()
    {
        Intent intent=getIntent();
        photoUri=intent.getData();
        testImage = findViewById(R.id.img_translate);
        testImage.setVisibility(View.VISIBLE);
        clipImageView=findViewById(R.id.crop_image);
        try {
            clipImageView.setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        btnCrop=findViewById(R.id.btn_crop);
        btnCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               // Intent intent1 = new Intent(ImageCropActivity.this,ResultActivity.class);
                //startActivity(intent1);
                Bitmap bitmap = clipImageView.getCutBitmap();  //保存剪裁后的照片

                clipImageView.setVisibility(View.VISIBLE);
                clipImageView.setImageBitmap(bitmap);
                clipImageView.setTranslate(clipImageView,testImage);
                String path = FileUtils.getSaveFile(getApplicationContext()).getAbsolutePath();//保存路径
                FileUtils.writeBitmapToFile(path,bitmap); // 保存图片
                //调用识别模块提取文字然后保存记录到本地
            }

        });
    }

    public void goToResActivity(String pictureUrl,String res){
        Bundle bundle = new Bundle();
        bundle.putString("pictureUrl", pictureUrl);
        bundle.putString("result", res);

        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(this, ResultActivity.class);
        startActivity(intent);
    }
}
