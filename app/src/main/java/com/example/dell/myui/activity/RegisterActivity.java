package com.example.dell.myui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dell.myui.R;
import com.example.dell.myui.utils.HttpUtils;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText userName;
    private EditText loginName;
    private EditText loginPwd;
    private EditText loginRePwd;
    private EditText verifyNumber;
    private Button btnVerifyNumber;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        initView();
//        HttpUtils.postDataWithParame("http://10.0.2.2:8080/user/reg");
    }

    public void initView() {
        userName = findViewById(R.id.login_edtUserName);
        loginName = findViewById(R.id.login_edtLoginName);
        loginRePwd = findViewById(R.id.login_edtRePwd);
        loginPwd = findViewById(R.id.login_edtPwd);
        verifyNumber = findViewById(R.id.login_et_verifyNumber);
        btnVerifyNumber = findViewById(R.id.login_btn_verifyNumber);
        btnLogin = findViewById(R.id.login_btnLogin);
        Map<String, String> param = new HashMap<>();
        param.put("loginName", loginName.getText().toString());
        btnVerifyNumber.setOnClickListener(v -> {
            if(!loginPwd.getText().toString().equals(loginRePwd.getText().toString())){
                Toast.makeText(this,loginPwd.getText().toString() , Toast.LENGTH_SHORT).show();
                Toast.makeText(this, loginRePwd.getText().toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "密码输入错误！", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "正在获取验证码！", Toast.LENGTH_SHORT).show();
                HttpUtils.postDataWithParam(HttpUtils.getBaseUrl() + "/user/genVerifyNum",
                        param);
            }
        });
        btnLogin.setOnClickListener(v -> {
            Toast.makeText(this, "点击了登录按钮", Toast.LENGTH_SHORT).show();
            Map<String,String> infoMap = new HashMap<>();
            infoMap.put("code", verifyNumber.getText().toString());
            infoMap.put("loginName",loginName.getText().toString());
            infoMap.put("loginPwd",loginPwd.getText().toString());
            infoMap.put("userName", userName.getText().toString());
            HttpUtils.postDataWithParam(HttpUtils.getBaseUrl() + "/user/reg",infoMap);
        });
    }
}
