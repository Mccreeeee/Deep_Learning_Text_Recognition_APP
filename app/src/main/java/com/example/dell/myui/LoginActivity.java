package com.example.dell.myui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
}
