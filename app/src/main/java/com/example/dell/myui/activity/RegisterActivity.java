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
        loginName = findViewById(R.id.login_edtId);
        loginRePwd = findViewById(R.id.login_edtRePwd);
        loginPwd = findViewById(R.id.login_edtPwd);
        verifyNumber = findViewById(R.id.login_et_verifyNumber);
        btnVerifyNumber = findViewById(R.id.login_btn_verifyNumber);
        btnLogin = findViewById(R.id.login_btnLogin);
        Map<String, String> param = new HashMap<>();
        param.put("loginName", loginName.getText().toString());
        btnVerifyNumber.setOnClickListener(v -> {
            Toast.makeText(this, "点击了获取验证码按钮", Toast.LENGTH_SHORT).show();
            HttpUtils.postDataWithParam(HttpUtils.getBaseUrl() + "/user/genVerifyNum",
                    param);
        });
        btnLogin.setOnClickListener(v -> {
            Toast.makeText(this, "点击了登录按钮", Toast.LENGTH_SHORT).show();
        });
    }
}
