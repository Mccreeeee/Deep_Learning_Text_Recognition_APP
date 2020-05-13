package com.example.dell.myui.entity;

public class UserEntity {
    //User对象类
    private int id;
    private String loginName;  //用户名，邮箱
    private String loginPwd;
    private String loginTime;
    private String regiserTime;
    private String userAvater; //用户头像地址
    private String userName; //用户昵称

    public UserEntity(){
        userAvater = "default";   //默认头像
    }
    public void setId(int id1){
        id =id1;
    }
    public int getId(){
        return id;
    }
    public void setLoginName(String loginName1){
        loginName = loginName1;
    }
    public String getLoginName(){
        return getLoginName();
    }
    public void setLoginPwd(String loginPwd1){
        loginPwd = loginPwd1;
    }
    public String getLoginPwd(){
        return loginPwd;
    }
    public void setLoginTime(String loginTime1){
        loginTime = loginTime1;
    }
    public String getLoginTime(){
        return loginTime;
    }
    public void setRegiserTime(String regiserTime1){
        regiserTime = regiserTime1;
    }
    public String getRegiserTime(){
        return regiserTime;
    }
    public void setUserAvater(String userAvater1){
        userAvater = userAvater1;
    }
    public String getUserAvater(){
        return userAvater;
    }
    public void setUserName(String userName1){
        userName = userName1;
    }
    public String getUserName(){
        return userName;
    }
}
