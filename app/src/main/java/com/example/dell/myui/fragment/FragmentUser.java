package com.example.dell.myui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.myui.activity.LoginActivity;
import com.example.dell.myui.R;
import com.example.dell.myui.utils.FileUtils;

import static android.content.Context.MODE_PRIVATE;

public class FragmentUser extends Fragment {
    private RelativeLayout relativeLayout_login;
    private RelativeLayout relativeLayout_exit;
    private RelativeLayout relativeLayout_clear;
    private boolean isLogin = false;   //判断是否登陆
    private TextView tv_userName; //处于登陆状态时将登陆栏改成用户名

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_user, container, false);
        relativeLayout_login=view.findViewById(R.id.relative_login);
        relativeLayout_exit = view.findViewById(R.id.settings_exitLayout);
        relativeLayout_clear = view.findViewById(R.id.relative_clear);
        relativeLayout_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

            }
        });
        relativeLayout_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLogin){
                   deleteUser();
                   isLogin = false;
                   tv_userName.setText("登陆");
                }
                else{
                    Toast.makeText(getActivity(),"你还没登陆！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        relativeLayout_clear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(FileUtils.delTxtAndPdf()){
                    Toast.makeText(getActivity(),"清楚成功",Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_userName = view.findViewById(R.id.tv_username);
        readUser();
        return view;
    }


    public void readUser(){
        //获取保存的用户名和账号
        SharedPreferences sp = getActivity().getSharedPreferences("info", MODE_PRIVATE);

        //获得保存在SharedPredPreferences中的用户名和密码
        String userName = sp.getString("username", "null");
        String password = sp.getString("password", "null");
        if(userName.equals("null") || password.equals("null"))
            isLogin = false;
        else
        {
            isLogin = true;
            tv_userName.setText(userName);
        }
    }
    public void deleteUser(){
        //注销并退出登录
        SharedPreferences sp = getActivity().getSharedPreferences("info", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.remove("username");
        ed.remove("password");
    }
}
