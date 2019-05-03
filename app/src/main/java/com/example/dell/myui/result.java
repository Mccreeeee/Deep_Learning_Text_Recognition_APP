package com.example.dell.myui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class result extends AppCompatActivity implements View.OnClickListener{
private TextView title,item_text,item_picture;
    private ArrayList<Fragment> fragment_list;
    private ViewPager vp;
    private FragmentText fragmentText;
    private FragmentPicture fragmentPicture;
    String[]titles=new String[]{"图片","文字"};

    Uri imageuri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_result);

       Intent intent=getIntent();
      imageuri=intent.getData();
        //ImageView imageView=(ImageView)findViewById(R.id.back);
       initview();
        vp.setOffscreenPageLimit(2);
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragment_list.get(i);
            }

            @Override
            public int getCount() {
                return fragment_list.size();
            }
        });
        vp.setCurrentItem(0);
        item_picture.setTextColor(Color.parseColor("#66CDAA"));

        //滑动改变viewpager内容
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                /*此方法在页面被选中时调用*/
               title.setText(titles[i]);
                changeTextColor(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    /**
     * 点击底部Text 动态修改ViewPager的内容
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_picture:
                vp.setCurrentItem(0,true);
                break;
            case R.id.item_text:
                vp.setCurrentItem(1,true);
                break;
        }
    }
    public void initview()
    {
        title=(TextView)findViewById(R.id.textView4);
        item_text=(TextView)findViewById(R.id.item_text);
        item_picture=(TextView)findViewById(R.id.item_picture);

        item_text.setOnClickListener(this);
        item_picture.setOnClickListener(this);

        vp=(ViewPager)findViewById(R.id.mainViewPager);

        fragmentText=new FragmentText();
        fragmentPicture=new FragmentPicture();
        fragmentPicture.getUri(imageuri);

        fragment_list=new ArrayList<>();
        fragment_list.add(fragmentPicture);
        fragment_list.add(fragmentText);
    }
    /*
     *由ViewPager的滑动修改底部导航Text的颜色
     */
    private void changeTextColor(int position)
    {
        if(position==0)
        {
            item_picture.setTextColor(Color.parseColor("#66CDAA"));
            item_text.setTextColor(Color.parseColor("#000000"));
        }
        else if(position==1)
        {
      item_text.setTextColor(Color.parseColor("#66CDAA"));
      item_picture.setTextColor(Color.parseColor("#000000"));
        }
    }

}
