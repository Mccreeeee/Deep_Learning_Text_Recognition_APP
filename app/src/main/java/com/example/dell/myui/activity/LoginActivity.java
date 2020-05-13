package com.example.dell.myui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dell.myui.R;

public class LoginActivity extends AppCompatActivity {
        private EditText etUserName;
        private EditText etPassword;
        private Button btn_login;         //登陆
        private TextView tv_regiser;      //注册
        private TextView tv_forgotPsw; //忘记密码
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        init();
    }
    void init()
    {
      etUserName=(EditText)findViewById(R.id.login_edtId);
      etPassword=(EditText)findViewById(R.id.login_edtPwd);
      btn_login=(Button)findViewById(R.id.login_btnLogin);
      tv_regiser=(TextView) findViewById(R.id.login_tv_register);
      tv_forgotPsw=(TextView)findViewById(R.id.login_txtForgotPwd);

      btn_login.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              String UserName = etUserName.getText().toString();
              String Password = etPassword.getText().toString();

              Intent intent=new Intent(LoginActivity.this,MainActivity.class);
              startActivity(intent);
          }
      });
      tv_regiser.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
              startActivity(intent);
          }
      });
    }
    public void saveUser(){
        //保存用户名和密码
        String userName = etUserName.getText().toString();
        String passWord = etPassword.getText().toString();

        //创建sharedPreference对象，info表示文件名，MODE_PRIVATE表示访问权限为私有的
        SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);

        //获得sp的编辑器
        SharedPreferences.Editor ed = sp.edit();

        //以键值对的显示将用户名和密码保存到sp中
        ed.putString("username", userName);
        ed.putString("password", passWord);

        //提交用户名和密码
        ed.commit();
    }
}
