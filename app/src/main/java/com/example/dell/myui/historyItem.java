package com.example.dell.myui;

import android.graphics.Bitmap;

public class historyItem {
    private Bitmap picture;
    private String date;
    private String time;
    private boolean isChecked;   //判断是否选中复选框
    private boolean isShow;     //判断是否显示复选框
public historyItem(Bitmap picture,String date,String time,boolean isChecked,boolean isShow)
{
    this.picture=picture;
    this.date=date;
    this.time=time;
    this.isChecked=isChecked;
    this.isShow=isShow;
}

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public String getDate() {
        return date;
    }

    public void setData(String date) {
        this.date = date;
    }

    public boolean isShow() {
        return isShow;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
