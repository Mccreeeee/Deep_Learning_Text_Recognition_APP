package com.example.dell.myui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Camera;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.idl.util.FileUtil;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.baidu.ocr.ui.camera.CameraView;
import com.example.dell.myui.R;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.example.dell.myui.SQLite.MYDB;
import com.example.dell.myui.SQLite.localDB;
import com.example.dell.myui.entity.PictureEntity;
import com.example.dell.myui.utils.FileUtils;

import java.io.File;

//身份证识别
public class IDCardIdentifyActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_IMAGE_FRONT = 201;
    private static final int REQUEST_CODE_PICK_IMAGE_BACK = 202;
    private static final int REQUEST_CODE_CAMERA = 102;
    private AlertDialog.Builder alertDialog;
    private TextView tv_name,tv_gender,tv_ethnic,tv_birthday;
    private  TextView tv_address,tv_idNumber,tv_issueAuthority,tv_validdate;
    private Button btn_gallery_front,btn_gallery_back;
    private Button btn_idcard_front,btn_idcard_back;
    private localDB my_db;
    private String filePath="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard_identify);
        init();
    }
    private void init(){
        getSupportActionBar().hide();
        alertDialog = new AlertDialog.Builder(this);
        my_db = ((MYDB)this.getApplication()).getDB();
        btn_gallery_front = (Button)findViewById(R.id.btn_gallery_front);
        btn_gallery_back = (Button)findViewById((R.id.btn_gallery_back));
        btn_idcard_front = (Button)findViewById(R.id.btn_id_card_front);
        btn_idcard_back = (Button)findViewById(R.id.btn_id_card_back);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_gender = (TextView)findViewById(R.id.tv_gender);
        tv_ethnic = (TextView)findViewById(R.id.tv_ethnic);
        tv_birthday = (TextView)findViewById(R.id.tv_birthday);
        tv_address = (TextView)findViewById(R.id.tv_address);
        tv_idNumber = (TextView)findViewById(R.id.tv_idNumber);
        tv_issueAuthority = (TextView)findViewById(R.id.tv_issueAuthority);
        tv_validdate = (TextView)findViewById(R.id.tv_validdate);

        //相册选择身份证前面
        btn_gallery_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkGalleryPermission())
                {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE_FRONT);
                }
            }
        });
        //相册选择身份证背面
        btn_gallery_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE_BACK);

            }
        });

        btn_idcard_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IDCardIdentifyActivity.this, CameraActivity.class);
                filePath =  FileUtils.getSaveFile(getApplication()).getAbsolutePath();
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                       filePath);
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });
        btn_idcard_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IDCardIdentifyActivity.this, CameraActivity.class);
                filePath = FileUtils.getSaveFile(getApplication()).getAbsolutePath();
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        filePath);
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                startActivityForResult(intent,REQUEST_CODE_CAMERA);
            }
        });
        OCR.getInstance(getApplicationContext()).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                // 调用成功，返回AccessToken对象
                String token = accessToken.getAccessToken();

            }

            @Override
            public void onError(OCRError ocrError) {
                // 调用失败，返回OCRError子类SDKError对象
            }
        },getApplicationContext());
    }
    private void recIDcard(final String idCardSide,String filaPath){
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filaPath));
        param.setIdCardSide(idCardSide);
        param.setDetectDirection(true);
        OCR.getInstance(this).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult idCardResult) {
                if(idCardResult!=null)
                {
                    if(idCardSide.equals("front")){
                        //前面身份证扫描结果
                        //可以在这里把结果存入本地数据库
                        String name = idCardResult.getName().toString();
                        String gender = idCardResult.getGender().toString();
                        String ethnic = idCardResult.getEthnic().toString();
                        String birthday = idCardResult.getBirthday().toString();
                        String address = idCardResult.getAddress().toString();
                        String idNumber = idCardResult.getIdNumber().toString();
                        String result = name + "\n" + gender + "\n" + ethnic + "\n"+ birthday + "\n"
                                + address + "\n" + idNumber;
                        PictureEntity picture = new PictureEntity();
                        picture.setPictureName(FileUtils.getFileName(filaPath));
                        picture.setPictureUrl(filaPath);
                        picture.setPictureType(2);
                        picture.setPictureData(result);
                        my_db.insertData(picture);   //保存记录
                        tv_name.setText(name);

                        tv_gender.setText(gender);
                        tv_ethnic.setText(ethnic);
                        tv_birthday.setText(birthday);
                        tv_address.setText(address);
                        tv_idNumber.setText(idNumber);
                    }else if(idCardSide.equals("back")){
                        //后面身份证扫描结果
                        //可以在这里把结果存入本地数据库
                        String issueAuthority = idCardResult.getIssueAuthority().toString();
                        String validdate = idCardResult.getSignDate().toString();
                        String result = issueAuthority + "\n" + validdate;

                        PictureEntity picture = new PictureEntity();
                        picture.setPictureName(FileUtils.getFileName(filaPath));
                        picture.setPictureUrl(filaPath);
                        picture.setPictureType(2);
                        picture.setPictureData(result);
                        my_db.insertData(picture);
                        tv_issueAuthority.setText(issueAuthority);
                        tv_validdate.setText(validdate);
                    }
                }
            }

            @Override
            public void onError(OCRError ocrError) {
                alertText("",ocrError.getMessage());

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == REQUEST_CODE_PICK_IMAGE_FRONT && resultCode== Activity.RESULT_OK){
            Uri uri = data.getData();
            String filePath = getRealPathFromURI(uri);
            recIDcard(IDCardParams.ID_CARD_SIDE_FRONT,filePath);
        }
        if(requestCode == REQUEST_CODE_PICK_IMAGE_BACK && resultCode==Activity.RESULT_OK){
            Uri uri = data.getData();
            String filePath = getRealPathFromURI(uri);
            recIDcard(IDCardParams.ID_CARD_SIDE_BACK,filePath);
        }
        if (requestCode == REQUEST_CODE_CAMERA && resultCode==Activity.RESULT_OK){
            if(data != null){
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                if(!TextUtils.isEmpty(contentType)){
                    if(CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)){
                        recIDcard(IDCardParams.ID_CARD_SIDE_FRONT,filePath);
                    }else if(CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)){
                        recIDcard(IDCardParams.ID_CARD_SIDE_BACK,filePath);
                    }
                }
            }
        }
    }

    private boolean checkGalleryPermission(){
        int ret = ActivityCompat.checkSelfPermission(IDCardIdentifyActivity.this, Manifest.permission
                .READ_EXTERNAL_STORAGE);
        if (ret != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(IDCardIdentifyActivity.this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    1000);
            return false;
        }
        return true;
    }

    private void alertText(final String title, final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }
    private String getRealPathFromURI(Uri contentUri){
        String result;
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
    @Override
    protected void onDestroy() {
        // 释放本地质量控制模型
        CameraNativeHelper.release();
        super.onDestroy();
    }
}
