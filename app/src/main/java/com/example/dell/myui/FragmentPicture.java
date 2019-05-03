package com.example.dell.myui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.myui.R;

public class FragmentPicture extends Fragment {
    private ImageView pic;
    Bitmap bitmap;
    Uri uri;
    public FragmentPicture(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_picture, container, false);
        pic=view.findViewById(R.id.picture);
        setBitmap();
        return view;
    }
    public void getUri(Uri uri)
    {
      this.uri=uri;
    }
 public void setBitmap()
 {
     try
     {
         // 读取uri资源中的bitmap
         bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
         pic.setImageBitmap(bitmap);
     }
     catch (Exception e)
     {
         Log.e("[Android]", e.getMessage());
         Log.e("[Android]", "目录为：" + uri);
         e.printStackTrace();
         bitmap=null;
     }
 }

}
