package com.example.dell.myui.entity;

import android.graphics.Bitmap;

public class HistoryItemEntity {
    private Bitmap picture;
    private String name;
    private String data;
    private boolean isChecked;   //判断是否选中复选框
    private boolean isShow;     //判断是否显示复选框
public HistoryItemEntity(Bitmap picture, String name, String data, boolean isChecked, boolean isShow)
{
    this.picture=picture;
    this.name=name;
    this.data = data;
    this.isChecked=isChecked;
    this.isShow=isShow;
}

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
