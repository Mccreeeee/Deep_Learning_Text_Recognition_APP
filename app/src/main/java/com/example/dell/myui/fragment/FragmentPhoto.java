package com.example.dell.myui.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.dell.myui.R;
import com.example.dell.myui.result;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class FragmentPhoto extends Fragment {
    private RelativeLayout relativeLayout;

    private static final int CROP_PHOTO = 2;
    private static final int REQUEST_CODE_PICK_IMAGE=3;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    private File output;
    private Uri imageUri;
    Bitmap bit;

    public FragmentPhoto()
    {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_photo, container, false);
        relativeLayout=view.findViewById(R.id.relative_one);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showButtonDialog();
            }
        });
        return view;
    }
    //弹出框，选择拍照或者从相册获取
    void showButtonDialog()
    {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(getActivity(),R.style.DialogTheme);
        //2、设置布局
        View view=View.inflate(getContext(),R.layout.button_dialog_picture,null);
        dialog.setContentView(view);

        Window window=dialog.getWindow();
        //设置弹出的位置
        window.setGravity(Gravity.BOTTOM);
        //设置动画
        window.setWindowAnimations(R.style.dialog_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        //拍照获取
        dialog.findViewById(R.id.tv_takephoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE2);
                }
                else {takephoto();dialog.dismiss();
                }
            }
        });
        //相册获取
        dialog.findViewById(R.id.tv_photograph).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        !=PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE2);
                }
                else {photograph();dialog.dismiss();}
            }
        });
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    public void takephoto()   //拍照
    {
        /**
         * 最后一个参数是文件夹的名称，可以随便起
         */
        File file=new File(Environment.getExternalStorageDirectory(),"拍照");
        if(!file.exists()){
            file.mkdir();
        }
        /**
         * 这里将时间作为不同照片的名称
         */
        output=new File(file,System.currentTimeMillis()+".jpg");
        /**
         * 如果该文件夹已经存在，则删除它，否则创建一个
         */
        try {
            if (output.exists()) {
                output.delete();
            }
            output.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * 隐式打开拍照的Activity，并且传入CROP_PHOTO常量作为拍照结束后回调的标志
         */
        imageUri = Uri.fromFile(output);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CROP_PHOTO);
    }
    public void photograph()  //相册
    {
        /**
         * 打开选择图片的界面
         */
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int req,int res,Intent data)
    {
        super.onActivityResult(req, res, data);
        switch (req)
        {
            case CROP_PHOTO:
                if(res==RESULT_OK)
                {
                    try {
                        Toast.makeText(getActivity(),"跳转",Toast.LENGTH_SHORT).show();
                        bit = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        Intent intent=new Intent(getActivity(), result.class);
                        intent.setData(imageUri);
                        startActivity(intent);
                    }catch(Exception e){
                        Toast.makeText(getActivity(),"程序崩溃",Toast.LENGTH_SHORT).show();
                    }
                }
                else {  Log.i("tag", "失败");}
                break;
            case REQUEST_CODE_PICK_IMAGE:
                if(res==RESULT_OK)
                {  try{
                    Toast.makeText(getActivity(),"跳转",Toast.LENGTH_SHORT).show();
                    Uri uri = data.getData();
                    bit = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                    Intent intent=new Intent(getActivity(),result.class);
                    intent.setData(uri);
                    startActivity(intent);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.d("tag",e.getMessage());
                    Toast.makeText(getContext(),"程序崩溃",Toast.LENGTH_SHORT).show();
                }}
                else{
                    Log.i("liang", "失败");
                }

                break;

            default:
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                takephoto();
            } else
            {
                // Permission Denied
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }


        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE2)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                photograph();
            } else
            {
                // Permission Denied
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
