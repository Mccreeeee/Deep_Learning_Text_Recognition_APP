package com.example.dell.myui.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.dell.myui.R;
import com.example.dell.myui.fragment.FragmentHistory;
import com.example.dell.myui.fragment.FragmentMain;
import com.example.dell.myui.fragment.FragmentPhoto;
import com.example.dell.myui.fragment.FragmentUser;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    FrameLayout fl_container;
    private File output;
    Bitmap bit;
    private Uri imageUri;
    public static final int CROP_PHOTO = 2;
    private static final int REQUEST_CODE_PICK_IMAGE = 3;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
    /*   init_view();
       setRollPagerView();*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        BottomBarActivity bottomBarActivity = findViewById(R.id.bottom_bar);
        fl_container = findViewById(R.id.fl_container);
        bottomBarActivity.setContainer(R.id.fl_container)
                .setTitleBeforeAndAfterColor("#999999", "#ff5d5e")
                .addItem(FragmentMain.class,
                        "首页",
                        R.drawable.main,
                        R.drawable.main)
                .addItem(FragmentPhoto.class,
                        "照片",
                        R.drawable.picture,
                        R.drawable.picture)
                .addItem(FragmentHistory.class,
                        "历史",
                        R.drawable.history,
                        R.drawable.history).
                addItem(FragmentUser.class,
                        "我的",
                        R.drawable.user,
                        R.drawable.user)
                .build();

    }



}