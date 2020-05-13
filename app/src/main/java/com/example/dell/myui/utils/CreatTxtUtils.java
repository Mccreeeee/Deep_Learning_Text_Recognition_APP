package com.example.dell.myui.utils;

import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;

public class CreatTxtUtils {
    public static void createTxt(String filePath,String content){
            File file = new File(filePath);
            try {
                if(!file.exists()){
                    file.createNewFile();
                    Log.d("create fiel ",filePath); //坑，要动态设置权限
                }
                RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                raf.seek(file.length());
                raf.write(content.getBytes());
                raf.close();
            }catch (Exception e){
                e.printStackTrace();
                Log.d("create",e.getMessage());
            }

    }
}
