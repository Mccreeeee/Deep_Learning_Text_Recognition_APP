package com.example.dell.myui.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class CheckLoginStatus {
    public static boolean isLogin(Context context){
        //获取保存的用户名和账号
        SharedPreferences sp = context.getSharedPreferences("info", MODE_PRIVATE);

        //获得保存在SharedPredPreferences中的用户名和密码
        String userName = sp.getString("username", "null");
        String password = sp.getString("password", "null");
        if(userName.equals("null") || password.equals("null")) {
            return false;
        }else {return true;}

    }
}
