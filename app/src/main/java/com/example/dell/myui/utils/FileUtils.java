package com.example.dell.myui.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.dell.myui.activity.SelfCameraActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class FileUtils {
    public  static File getSaveFile(Context context) {
        //调用百度ocr中图片的保存位置
        String picture_name = timeUtils.getCurrentTime() + ".jpg";
        File file = new File(context.getFilesDir(),picture_name);
        return file;
    }
    public static File getPdfFile(Context context){
        //分享时保存pdf文件的路径
        File file = new File(Environment.getExternalStorageDirectory(), "pdf");
        if(!file.exists()){
            file.mkdir();
        }
        File filePdf = new File(file,timeUtils.getCurrentTime()+ ".pdf");
        return filePdf;
    }
    public static File getTxtFile(Context context){
        //分享时保存pdf文件的路径
        File file = new File(Environment.getExternalStorageDirectory(), "txt");
        if(!file.exists()){
            file.mkdir();
        }
        File fileTxt = new File(file,timeUtils.getCurrentTime()+ ".txt");
        return fileTxt;
    }
    public static Bitmap getImageFromUrl(String url){
        //通过路径获取图片
        File file = new File(url);
        if(file.exists())
        {Bitmap bm = BitmapFactory.decodeFile(url);
            return bm;}
        return null;
    }
    public static String getFileName(String path){
        //获取图片名字
        if(TextUtils.isEmpty(path)){
            return "";
        }
        int start = path.lastIndexOf("/");
        if (start != -1 ) {
            return path.substring(start + 1);
        } else {
            return "";
        }
    }
    public static void writeBitmapToFile(String filePath,Bitmap bitmap){
        //将bitmap图片数据保存
        File picturePath = new File(filePath);
        try {
            OutputStream os = new FileOutputStream(picturePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            //qualty,30 是压缩率，表示压缩70%; 如果不压缩是100，表示压缩率为
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
    }
    public static void delFileFromUrl(String path){
        //根据绝对路径删除文件
        File file = new File(path);
        if(file.exists())
        {
            file.delete();
        }
    }


    //删除文件夹，例如分享时创建的txt和pdf文件夹
    public static boolean delDir(File dir){
        //删除文件夹及下面的内容
        if (dir == null) return false;
        // 目录不存在返回true
        if (!dir.exists()) return true;
        // 不是目录返回false
        if (!dir.isDirectory()) return false;
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) return false;
                } else if (file.isDirectory()) {
                    if (!delDir(file)) return false;
                }
            }
        }
        return dir.delete();
    }

    public static boolean delTxtAndPdf(){
        //清除缓存时把txt和pdf文件删除
        File filePdfDir = new File(Environment.getExternalStorageDirectory(), "pdf");
        if(filePdfDir.isDirectory()){
            if(!delDir(filePdfDir)){return false;}
        }
        File fileTxtDir = new File(Environment.getExternalStorageDirectory(), "txt");
        if(fileTxtDir.isDirectory()){
            if(!delDir(fileTxtDir)){return false;}
        }
        return true;
    }
}
