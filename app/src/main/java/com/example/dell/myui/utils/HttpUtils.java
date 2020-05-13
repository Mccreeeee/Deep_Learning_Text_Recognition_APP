package com.example.dell.myui.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static java.security.AccessController.getContext;

public class HttpUtils {
    private static String baseUrl = "http://10.0.2.2:8080";
    public static String getBaseUrl() {
        return baseUrl;
    }
    public static void postDataWithParam(String url, Map<String, String> keyValue) {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        for(Map.Entry<String, String> entry : keyValue.entrySet()) {
            formBody.add(entry.getKey(), entry.getValue());
        }
        Request request = new Request.Builder()//创建Request 对象。
                .url(url)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.err.println("未能发送成功！！！");
                Log.d("httpUtils","未能发送成功！！！因为" + e.toString());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    System.err.println(result);
                    Log.d("httpUtils",result);
                }
            }
        });
    }

    public static void postJsonData(String url, String json) {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
//        以下是sample
//        String jsonStr = "{\"username\":\"lisi\",\"nickname\":\"李四\"}";//json数据.
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()//创建Request 对象。
                .url(url)
                .post(body)//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.err.println("未能发送成功！！！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    System.err.println(result);
                }
            }
        });
    }
}
