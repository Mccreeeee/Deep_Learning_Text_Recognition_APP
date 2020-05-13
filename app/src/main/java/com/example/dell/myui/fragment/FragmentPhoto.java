package com.example.dell.myui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.baidu.idl.util.FileUtil;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.example.dell.myui.R;
import com.example.dell.myui.RecognizeType.RecognizeType;
import com.example.dell.myui.SQLite.MYDB;
import com.example.dell.myui.SQLite.localDB;
import com.example.dell.myui.activity.IDCardIdentifyActivity;
import com.example.dell.myui.activity.ResultActivity;
import com.example.dell.myui.activity.SelfCameraActivity;

import java.io.File;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.example.dell.myui.entity.PictureEntity;
import com.example.dell.myui.utils.FileUtils;
import com.example.dell.myui.utils.GoToResult;
import com.example.dell.myui.utils.fastJsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentPhoto extends Fragment {
    private RelativeLayout relativeLayout1;    //通用,type=0
    private RelativeLayout relativeLayout2;    //银行卡,type=1
    private RelativeLayout relativeLayout3;    //身份证 2
    private RelativeLayout relativeLayout4;    //手写体 3
    private RelativeLayout relativeLayout5;    //ocr文档 4
    private RelativeLayout relativeLayout6;    //pdf文档 5

    private static final int REQUEST_CODE_BANKCARD = 111;   //银行卡
    private static final int REQUEST_CODE_HANDWRITING = 129; //手写
    private static final int REQUEST_CODE_GENERAL_WEBIMAGE= 110;//网络图片识别


    private boolean hasGotToken = false;
    private boolean isPDF = false;  //因为pdf和ocr文档共用百度同一个api，所以需要判断是否时pdf
    private AlertDialog.Builder alertDialog;
    private String filePath;  //图片路径
    private localDB my_db; // 数据库
    public FragmentPhoto()
    {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        my_db = ((MYDB)getActivity().getApplication()).getDB();
        View view=inflater.inflate(R.layout.fragment_photo, container, false);
        initAccessToken();
        alertDialog = new AlertDialog.Builder(getContext());
        relativeLayout1=view.findViewById(R.id.relative_one);
        relativeLayout2 = view.findViewById(R.id.relative_two);
        relativeLayout3 = view.findViewById(R.id.relative_three);
        relativeLayout4 = view.findViewById(R.id.relative_four);
        relativeLayout5 = view.findViewById(R.id.relative_five );
        relativeLayout6 = view.findViewById(R.id.relative_six);
        relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到拍照界面
                Intent intent =new Intent(getActivity(), SelfCameraActivity.class);
                startActivity(intent);
            }
        });
        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //银行卡识别
                if(!hasGotToken){ return;}
                filePath = FileUtils.getSaveFile(getContext()
                        .getApplicationContext())
                        .getAbsolutePath();
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        filePath);
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_BANK_CARD);
                startActivityForResult(intent, REQUEST_CODE_BANKCARD);

            }
        });
        relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasGotToken){return;}
                Intent intent = new Intent(getContext(), IDCardIdentifyActivity.class);
                startActivity(intent);
            }
        });
        relativeLayout4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!hasGotToken){return;}
                filePath = FileUtils.getSaveFile(getContext()
                        .getApplicationContext())
                        .getAbsolutePath();
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        filePath);
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_HANDWRITING);
            }
        });
        relativeLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //传统ocr（调用百度ocr的网络图片识别）
                if(!hasGotToken){return;}
                filePath = FileUtils.getSaveFile(getContext()
                        .getApplicationContext())
                        .getAbsolutePath();
                Intent intent = new Intent(getActivity(),CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        filePath);
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent,REQUEST_CODE_GENERAL_WEBIMAGE);
            }
        });
        relativeLayout6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //pdf（调用百度ocr的网络图片识别）
                if(!hasGotToken){return;}
                isPDF = true;
                filePath = FileUtils.getSaveFile(getContext()
                        .getApplicationContext())
                        .getAbsolutePath();
                Intent intent = new Intent(getActivity(),CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        filePath);
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent,REQUEST_CODE_GENERAL_WEBIMAGE);
            }
        });
        return view;
    }



    /**
     * 以license文件方式初始化
     */
    private void initAccessToken(){
        OCR.getInstance(getContext()).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError ocrError) {
                ocrError.printStackTrace();
            }
        },"aip.license",getContext().getApplicationContext());
    }

    private void alertText(final String title,final String message){
         alertDialog.setTitle(title)
                 .setMessage(message)
                 .setPositiveButton("确定",null)
                 .show();
    }
    private void infoPopText(final String result){
        alertText("",result);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initAccessToken();
        } else {
            Toast.makeText(getContext().getApplicationContext(), "需要android.permission.READ_PHONE_STATE", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        //银行卡识别
        if(requestCode == REQUEST_CODE_BANKCARD && resultCode==Activity.RESULT_OK){

            RecognizeType.recBankCard(getContext()
                    , filePath
                    , new RecognizeType.TypeListener() {
                        @Override
                        public void onResult(String result) {
                            //识别的结果
                            infoPopText(result);
                            insert(1,result);  //存入数据库
                        GoToResult.goToResActivity(getContext(),filePath, result);
                        }
                    });
        }
        //手写体识别
        if(requestCode == REQUEST_CODE_HANDWRITING && resultCode == Activity.RESULT_OK){

            RecognizeType.recHandWriting(getContext()
                    ,filePath
                    ,new RecognizeType.TypeListener(){
                        @Override
                        public void onResult(String result) {
                            //识别的结果
                            infoPopText(result);
                            insert(3,fastJsonUtils.decodeJson(result));
                            GoToResult.goToResActivity(getContext(),filePath, fastJsonUtils.decodeJson(result));
                        }
                    });
        }
        //网络图片识别
        if(requestCode == REQUEST_CODE_GENERAL_WEBIMAGE && resultCode==Activity.RESULT_OK){

            RecognizeType.recWebimage(getContext()
                    , filePath
                    , new RecognizeType.TypeListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                            if(isPDF){
                                insert(5,fastJsonUtils.decodeJson(result));
                            }
                            else{insert(4,fastJsonUtils.decodeJson(result));}
                            GoToResult.goToResActivity(getContext(),filePath,fastJsonUtils.decodeJson(result));
                        }
                    }
            );
        }

    }

    public void insert(int type,String result){
        PictureEntity picture = new PictureEntity();
        picture.setPictureType(type);
        picture.setPictureName(FileUtils.getFileName(filePath));
        picture.setPictureUrl(filePath);
        picture.setPictureData(result);
        my_db.insertData(picture);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 释放内存资源
        OCR.getInstance(getContext()).release();

    }

}
