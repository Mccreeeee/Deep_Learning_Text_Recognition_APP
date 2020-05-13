package com.example.dell.myui.entity;

import java.util.Map;

public class PictureEntity {
    //picture对象类
    private int id;  //图片的主键id，
    private String loginName ;  //未登录时默认为default
    private String pictureData;    //图片识别出来的文字
    private String pictureName;    //图片名字
    private int pictureType;    //图片类型
    private String pictureUrl;     //保存图片的url
    private String upLoadTime;     //上传时间

    public PictureEntity(){
        loginName = "default";
    }
    public void setId(int id1){
        id = id1;
    }
    public int getId(){
        return id;
    }
    public void setLoginName(String loginName1){
        loginName = loginName1;
    }
    public String getLoginName(){
        return loginName;
    }
    public void setPictureData(String pictureData1){
        pictureData = pictureData1;
    }
    public String getPictureData(){
        return pictureData;
    }
    public void setPictureName(String pictureName1){
        pictureName = pictureName1;
    }
    public String getPictureName(){
        return pictureName;
    }
    public void setPictureType(int pictureType1){
        pictureType = pictureType1;
    }
    public int getPictureType(){
        return pictureType;
    }
    public void setPictureUrl(String pictureUrl1){
        pictureUrl = pictureUrl1;
    }
    public String getPictureUrl(){
        return pictureUrl;
    }
    public void setUpLoadTime(String upLoadTime1){
        upLoadTime = upLoadTime1;
    }
    public String getUpLoadTime(){
        return upLoadTime;
    }


}
