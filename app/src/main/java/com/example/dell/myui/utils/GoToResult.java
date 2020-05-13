package com.example.dell.myui.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.dell.myui.activity.ResultActivity;

public class GoToResult {
    public static void goToResActivity(Context context, String pictureUrl, String res){
        Bundle bundle = new Bundle();
        bundle.putString("pictureUrl", pictureUrl);
        bundle.putString("result", res);

        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(context, ResultActivity.class);
        context.startActivity(intent);
    }
}
